package server.servlet.views.weeklyActivities;


import engine.api.EngineContext;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.activity.weekly.activity.WeeklyActivityModifier;
import engine.model.boat.Boat;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(urlPatterns = "/weekly-activities/update")
public class UpdateWeeklyActivityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();

        try (PrintWriter out = resp.getWriter()) {

            String id = (String) req.getSession().getAttribute("UpdateWeeklyActivityId");

            if (id == null) {
                resp.sendRedirect("/weekly-activities/index");
            } else {
                req.getSession().removeAttribute("UpdateWeeklyActivityId");
                List<WeeklyActivity> tempList = eng.getWeeklyActivitiesCollectionManager()
                        .filter(weeklyActivity -> weeklyActivity.getId().equals(id));

                if (tempList != null && tempList.size() == 1) {
                    WeeklyActivity weeklyActivityToEdit = tempList.get(0);

                    if (weeklyActivityToEdit == null) {
                        out.println(Utils.createJsonErrorObject("Couldn't find the requested weekly activity"));
                    } else {
                        String updatePage = Utils.readHtmlPage("/public/html/views/weeklyActivities/update.html", req);
                        updatePage = prepareUpdatePage(updatePage, weeklyActivityToEdit);
                        Utils.renderLayoutString(req, resp, updatePage, ePages.WEEKLY_ACTIVITIES);
                    }
                } else {
                    resp.sendRedirect("/weekly-activities/index");
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

                WeeklyActivity weeklyActivityToEdit = (WeeklyActivity) eng.getWeeklyActivitiesCollectionManager()
                        .filter(weeklyActivity -> weeklyActivity.getId().equals(id));

                if (weeklyActivityToEdit == null) {
                    out.println(Utils.createJsonErrorObject("Unknown error occurred during weekly activity editing."));
                } else {
                    List<String> errors = new ArrayList<>(updateTheWeeklyActivity(weeklyActivityToEdit, data));

                    if (!errors.isEmpty()) {
                        out.println(Utils.createJsonErrorsListObject(errors));
                    } else {
                        out.println(Utils.createJsonSuccessObject(true));
                    }
                }

            } catch (Exception ex) {
                out.println(Utils.createJsonErrorObject("Unknown error occurred during rower creation."));
            }
        }
    }

    private List<String> updateTheWeeklyActivity(WeeklyActivity weeklyActivityToEdit, HashMap<String, String> data) {
        List<String> errors = new ArrayList<>();
        EngineContext eng = EngineContext.getInstance();
        WeeklyActivityModifier modifier = eng.getWeeklyActivityModifier(weeklyActivityToEdit, null);

        handleNameSet(data.get("name"), errors, modifier);
        handleStartTime(data.get("startTime"), errors, modifier);
        handleEndTime(data.get("endTime"), errors, modifier);
        handleBoatType(data.get("BoatTypeIndex"), modifier);

        return errors;
    }

    private void handleBoatType(String boatTypeIndex, WeeklyActivityModifier modifier) {
        try {
            Boat.eBoatType boatType = Boat.eBoatType.getTypeFromInt(Integer.parseInt(boatTypeIndex));
            modifier.setRequiredBoatType(boatType);
        } catch (Exception ex) {
            modifier.setRequiredBoatType(null);
        }
    }

    private void handleStartTime(String startTime, List<String> errors, WeeklyActivityModifier modifier) {
        try {
            LocalTime asTime = LocalTime.parse(startTime);
            modifier.setStartTime(asTime);
        } catch (DateTimeParseException ex) {
            errors.add("Invalid time format received for start time.");
        }
    }

    private void handleEndTime(String endTime, List<String> errors, WeeklyActivityModifier modifier) {
        try {
            LocalTime asTime = LocalTime.parse(endTime);
            modifier.setEndTime(asTime);
        } catch (DateTimeParseException ex) {
            errors.add("Invalid time format received for end time.");
        }
    }


    private void handleNameSet(String name, List<String> errors, WeeklyActivityModifier modifier) {
        if (name.isEmpty()) {
            errors.add("Weekly activity name can't be empty");
        } else {
            modifier.setName(name);
        }
    }

    private String prepareUpdatePage(String updatePage, WeeklyActivity weeklyActivity) {
        return updatePage.replace("{id}", weeklyActivity.getId())
                .replace("{name}", weeklyActivity.getName())
                .replace("{startTime}", weeklyActivity.getStartTime()
                        .format(DateTimeFormatter.ofPattern("HH:mm")))
                .replace("{endTime}", weeklyActivity.getEndTime()
                        .format(DateTimeFormatter.ofPattern("HH:mm")));
    }

}
