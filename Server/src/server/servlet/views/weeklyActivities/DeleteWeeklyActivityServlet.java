package server.servlet.views.weeklyActivities;

import engine.api.EngineContext;
import engine.database.collection.WeeklyActivityCollectionManager;
import engine.model.activity.weekly.activity.WeeklyActivity;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

@WebServlet(urlPatterns = "/weekly-activities/delete")
public class DeleteWeeklyActivityServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        HashMap<String, String> reqArgs = Utils.parsePostData(req);
        String serialNumber = reqArgs.get("serialNumber");
        WeeklyActivityCollectionManager weeklyActivities = eng.getWeeklyActivitiesCollectionManager();
        List<WeeklyActivity> weeklyActivityList = weeklyActivities
                .filter(weeklyActivity -> weeklyActivity.getId().equals(serialNumber));
        WeeklyActivity weeklyActivityToDelete = weeklyActivityList.isEmpty() ? null : weeklyActivityList.get(0);

        try (PrintWriter out = resp.getWriter()) {
            if (weeklyActivityToDelete != null) {
                out.println(Utils.getSuccessJson(weeklyActivities.remove(weeklyActivityToDelete)));
            } else {
                out.println(Utils.getSuccessJson(false));
            }
        }
    }
}
