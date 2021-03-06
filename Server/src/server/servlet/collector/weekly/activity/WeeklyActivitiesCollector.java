package server.servlet.collector.weekly.activity;

import com.google.gson.Gson;
import engine.api.EngineContext;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import server.servlet.json.template.model.boat.BoatsJson;
import server.servlet.json.template.model.rower.RowerListJson;
import server.servlet.json.template.model.weekly.activity.WeeklyActivitiesJson;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(urlPatterns = "/collectors/weekly-activities")
public class WeeklyActivitiesCollector extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        List<WeeklyActivity> activities = eng.getWeeklyActivitiesCollectionManager().toArrayList();


        try(PrintWriter out = resp.getWriter()){
            out.print(new Gson().toJson(new WeeklyActivitiesJson(activities)));
        }
    }
}
