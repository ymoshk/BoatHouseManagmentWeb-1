package server.servlet.views.requests;

import com.google.gson.Gson;
import engine.api.EngineContext;
import engine.model.activity.request.Request;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import server.constant.ePages;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(urlPatterns = "/requests/create")
public class CreateRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utils.renderLayout(req, resp, "/public/html/views/requests/create.html", ePages.REQUESTS);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Request newRequest = null;
        EngineContext eng = EngineContext.getInstance();
        HashMap<String, String> reqData = Utils.parsePostData(req);
        List<String> errors = new ArrayList<>();

        try {
            Rower mainRower = eng.getRowersCollectionManager().findRowerBySerialNumber(reqData.get("mainRowerSerial"));

            if (mainRower == null) {
                errors.add("Couldn't attach this main rower to the request");
            }

            LocalDate activityDate = validateDate(reqData.get("activityDate"), errors);
            WeeklyActivity weeklyActivity = parseWeeklyActivity(reqData, errors);
            List<Boat.eBoatType> boatTypes = parseBoatTypes(reqData.get("boatTypes"), errors);
            List<Rower> otherRowers = parseOtherRowers(reqData.get("otherRowers"), errors, weeklyActivity, activityDate);
            Rower creator = eng.getLoggedInUser(req.getRequestedSessionId());
            newRequest = new Request(mainRower, creator, weeklyActivity, activityDate, otherRowers, boatTypes);

        } catch (Exception ex) {
            errors.add("Couldn't create the request due to an unknown error.");
        }

        try (PrintWriter out = resp.getWriter()) {
            if (errors.size() == 0 && newRequest != null) {
                out.println(Utils.createJsonSuccessObject(eng.getRequestsCollectionManager().add(newRequest)));
            } else {
                out.println(Utils.createJsonErrorsListObject(errors));
            }
        }
    }

    private List<Rower> parseOtherRowers(String otherRowers, List<String> errors,
                                         WeeklyActivity weeklyActivity, LocalDate activityDate) {
        Gson gson = new Gson();
        List<Rower> result = new ArrayList<>();
        String[] serials = gson.fromJson(otherRowers, String[].class);

        if (serials.length > 0) {
            for (String serial : serials) {
                Rower temp = EngineContext.getInstance().getRowersCollectionManager().findRowerBySerialNumber(serial);

                if (temp == null) {
                    errors.add("Some of the other rowers couldn't be added to the request.");
                    break;
                } else if (!EngineContext.getInstance().getRequestsCollectionManager()
                        .isRowerAvailableForActivity(temp, weeklyActivity, activityDate)) {
                    errors.add(String.format(
                            "%s already takes a part in other club activity during the request time frame",
                            temp.getName()));
                    break;
                }
                result.add(temp);
            }
        }
        return result;
    }

    private List<Boat.eBoatType> parseBoatTypes(String boatTypes, List<String> errors) {
        Gson gson = new Gson();
        List<Boat.eBoatType> result = new ArrayList<>();
        String[] indexes = gson.fromJson(boatTypes, String[].class);

        if (indexes.length > 0) {
            for (String index : indexes) {
                Boat.eBoatType type = Boat.eBoatType.getTypeFromInt(Integer.parseInt(index));
                if (type != null) {
                    result.add(type);
                } else {
                    errors.add(String.format("Boat type #%d couldn't be added to the request",
                            Integer.parseInt(index)));
                }
            }
        } else {
            errors.add("Couldn't find any boat types");

        }
        return result;
    }

    private WeeklyActivity parseWeeklyActivity(HashMap<String, String> reqData, List<String> errors) {
        boolean isNewActivity = Boolean.parseBoolean(reqData.get("isNewWeeklyActivity"));

        if (isNewActivity) {
            LocalTime startTime = LocalTime.parse(reqData.get("startTime"), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime endTime = LocalTime.parse(reqData.get("endTime"), DateTimeFormatter.ofPattern("HH:mm"));

            return new WeeklyActivity("Auto Generated", startTime, endTime);

        } else {
            String weeklyActivityId = reqData.get("weeklyActivityId");
            List<WeeklyActivity> temp = EngineContext.getInstance().getWeeklyActivitiesCollectionManager()
                    .filter(weeklyActivity -> weeklyActivity.getId().equals(weeklyActivityId));

            if (temp != null && temp.size() == 1) {
                return temp.get(0);
            } else {
                errors.add("Couldn't use the selected weekly activity.");
                return null;
            }
        }
    }

    private LocalDate validateDate(String date, List<String> errors) {
        try {
            LocalDate theDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (theDate.isAfter(LocalDate.now())) {
                return theDate;
            } else {
                errors.add("Activity date must be greater then today");
                return null;
            }
        } catch (Exception ex) {
            errors.add("Invalid date received.");
            return null;
        }
    }
}
