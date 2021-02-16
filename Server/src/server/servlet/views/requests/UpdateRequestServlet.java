package server.servlet.views.requests;


import com.google.gson.Gson;
import engine.api.EngineContext;
import engine.model.activity.request.Request;
import engine.model.activity.request.RequestModifier;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import server.constant.Constants;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@WebServlet(urlPatterns = "/requests/update")
public class UpdateRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {

            String id = (String) req.getSession().getAttribute("UpdateRequestId");

            if (id == null) {
                resp.sendRedirect("/requests/index");
            } else {
                req.getSession().removeAttribute("UpdateRequestId");
                List<Request> tempList = EngineContext.getInstance().getRequestsCollectionManager()
                        .filter(request -> request.getId().equals(id));

                if (tempList != null && tempList.size() == 1) {
                    String updatePage = Utils.readHtmlPage("/public/html/views/requests/update.html", req);
                    updatePage = prepareUpdatePage(updatePage, tempList.get(0));
                    Utils.renderLayoutFromHtml(req, resp, updatePage, ePages.ROWERS);

                } else {
                    out.println(Utils.createJsonErrorObject("Updating request failed due to an unknown error."));
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {
            HashMap<String, String> data = Utils.parsePostData(req);

            try {
                EngineContext eng = (EngineContext) req.getServletContext().getAttribute(Constants.engineAtt);
                String id = data.get("id");

                if (id != null) {
                    Request requestToEdit = eng.getRequestsCollectionManager().findByUniqueIdentifier(id);

                    if (requestToEdit == null) {
                        out.println(Utils.createJsonErrorObject("Unknown error occurred during rower editing."));
                    } else {
                        List<String> errors = new ArrayList<>(updateTheRequest(requestToEdit, data));

                        if (!errors.isEmpty()) {
                            out.println(Utils.createJsonErrorObject(errors));
                        } else {
                            out.println(Utils.createJsonSuccessObject(true));
                        }
                    }
                } else {
                    out.println(Utils.createJsonErrorObject("Unknown error occurred during rower editing."));
                }
            } catch (Exception ex) {
                out.println(Utils.createJsonErrorObject("Unknown error occurred during rower editing."));
            }
        }
    }

    private List<String> updateTheRequest(Request requestToEdit, HashMap<String, String> data) {
        List<String> errors = new ArrayList<>();
        EngineContext eng = EngineContext.getInstance();
        RequestModifier modifier = eng.getRequestModifier(requestToEdit, null);

        handleMainRower(eng, modifier, data, errors);
        handleActivityDate(eng, modifier, data.get("activityDate"), errors);
        handleWeeklyActivity(eng, modifier, data.get("weeklyActivity"), errors);
        handleBoatTypes(modifier, data.get("boatTypes"), errors);
        handleOtherRowers(eng, modifier, data, errors);

        return errors;
    }

    private void handleOtherRowers(EngineContext eng, RequestModifier modifier, HashMap<String, String> data, List<String> errors) {
        try {
            String[] otherRowersSerials = new Gson().fromJson(data.get("otherRowers"), String[].class);
            List<Rower> otherRowers = new ArrayList<>();
            Rower mainRower = eng.getRowersCollectionManager().findRowerBySerialNumber(data.get("mainRower"));

            for (String serial : otherRowersSerials) {
                Rower rowerToAdd = eng.getRowersCollectionManager().findRowerBySerialNumber(serial);
                if (rowerToAdd == null) {
                    errors.add("One of the other rowers couldn't be added to the request due to an unknown error.");
                }
                otherRowers.add(rowerToAdd);
            }

            if (otherRowers.size() > modifier.getObjectToEdit().getMaxPossibleRowers() - 1) {
                errors.add("Count of other rowers exceeds the maximum number allowed.");

            } else if (otherRowers.contains(mainRower)) {
                errors.add("The main rower cannot be also in the other rowers list");
            } else {
                modifier.getObjectToEdit().getOtherRowersList().forEach(modifier::removeRowerFromOtherRowersList);
                otherRowers.forEach(modifier::addRowerToRequest);
            }
        } catch (Exception ex) {
            errors.add("Updating other rowers failed due to an unknown error");
        }
    }

    private void handleBoatTypes(RequestModifier modifier, String boatTypes, List<String> errors) {
        try {
            String[] boatTypesIndexes = new Gson().fromJson(boatTypes, String[].class).clone();
            List<Boat.eBoatType> types = new ArrayList<>();

            for (String index : boatTypesIndexes) {
                types.add(Boat.eBoatType.getTypeFromInt(Integer.parseInt(index)));
            }

            modifier.getObjectToEdit().getBoatTypesList().forEach(modifier::removeBoatType);
            types.forEach(modifier::addBoatTypeOption);
        } catch (Exception ex) {
            errors.add("Updating boat types failed due to an unknown error");

        }
    }

    private void handleWeeklyActivity(EngineContext eng, RequestModifier modifier, String weeklyActivityId, List<String> errors) {
        WeeklyActivity activity = eng.getWeeklyActivitiesCollectionManager().findByUniqueIdentifier(weeklyActivityId);

        if (activity == null) {
            errors.add("Updating failed because the weekly activity you're trying to use isn't valid.");
        } else {
            modifier.setWeeklyActivity(activity);
        }
    }

    private void handleActivityDate(EngineContext eng, RequestModifier modifier, String activityDate, List<String> errors) {
        try {
            LocalDate date = LocalDate.parse(activityDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            modifier.setTrainingDate(date);
        } catch (Exception ex) {
            errors.add("Couldn't update the request because the activity date you provided isn't a valid date.");
        }
    }

    private void handleMainRower(EngineContext eng, RequestModifier modifier, HashMap<String, String> data, List<String> errors) {
        Rower mainRower = eng.getRowersCollectionManager().findRowerBySerialNumber(data.get("mainRower"));
        List<String> otherRowersIds = Arrays.asList(new Gson().fromJson(data.get("otherRowers"), String[].class).clone());

        if (mainRower == null) {
            errors.add("Couldn't attach the main rower to this request");
        } else if (otherRowersIds.contains(mainRower.getId())) {
            errors.add("The main rower you selected can't also be in the other rowers list");
        } else {
            modifier.setMainRower(mainRower);
        }
    }

    private String prepareUpdatePage(String updatePage, Request request) {

        return updatePage.replace("{id}", request.getId())
                .replace("{date}", request.getTrainingDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}
