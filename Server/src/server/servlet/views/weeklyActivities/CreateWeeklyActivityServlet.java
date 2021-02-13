package server.servlet.views.weeklyActivities;

import engine.api.EngineContext;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.boat.Boat;
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
import java.util.HashMap;

@WebServlet(urlPatterns = "/weekly-activities/create")
public class CreateWeeklyActivityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utils.renderLayout(req, resp, "/public/html/views/weeklyActivities/create.html", ePages.WEEKLY_ACTIVITIES);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext engine = EngineContext.getInstance();
        HashMap<String, String> reqArgsMap = Utils.parsePostData(req);
        String name = reqArgsMap.get("name");
        Boat.eBoatType boatType = Boat.eBoatType.getTypeFromInt(Integer.parseInt(reqArgsMap.get("boatType")));
        String startTimeString = reqArgsMap.get("startTime");
        String endTimeString = reqArgsMap.get("endTime");
        LocalTime startTime = LocalTime.parse(startTimeString, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(endTimeString, DateTimeFormatter.ofPattern("HH:mm"));
        WeeklyActivity weeklyActivityToAdd = new WeeklyActivity(name, startTime, endTime, boatType);

        try (PrintWriter out = resp.getWriter()) {
            if (engine.getWeeklyActivitiesCollectionManager().add(weeklyActivityToAdd)) {
                out.println(Utils.createJsonSuccessObject(true));
            } else {
                out.println(Utils.createJsonSuccessObject(false));
            }
        }
    }
}
