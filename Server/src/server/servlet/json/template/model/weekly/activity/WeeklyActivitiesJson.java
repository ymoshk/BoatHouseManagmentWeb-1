package server.servlet.json.template.model.weekly.activity;

import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.boat.Boat;
import server.servlet.json.template.model.boat.BoatJson;

import java.util.ArrayList;
import java.util.List;

public class WeeklyActivitiesJson {
    List<WeeklyActivityJson> activities = new ArrayList<>();

    public WeeklyActivitiesJson(List<WeeklyActivity> activities) {
        for (WeeklyActivity activity : activities) {
            this.activities.add(new WeeklyActivityJson(activity));
        }
    }
}
