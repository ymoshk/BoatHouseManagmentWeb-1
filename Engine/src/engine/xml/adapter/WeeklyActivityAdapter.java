package engine.xml.adapter;


import engine.model.activity.weekly.activity.AdaptedWeeklyActivity;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.boat.Boat;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;

public class WeeklyActivityAdapter extends XmlAdapter<AdaptedWeeklyActivity, WeeklyActivity> {

    @Override
    public WeeklyActivity unmarshal(AdaptedWeeklyActivity adaptedWeeklyActivity) throws Exception {
        String name = adaptedWeeklyActivity.getName();
        LocalTime startTime = adaptedWeeklyActivity.getStartTime();
        LocalTime endTime = adaptedWeeklyActivity.getEndTime();
        Boat.eBoatType type = adaptedWeeklyActivity.getBoatType();

        return new WeeklyActivity(name, startTime, endTime, type);
    }

    @Override
    public AdaptedWeeklyActivity marshal(WeeklyActivity weeklyActivity) throws Exception {
        AdaptedWeeklyActivity adaptedWeeklyActivity = new AdaptedWeeklyActivity();
        adaptedWeeklyActivity.setBoatType(weeklyActivity.getBoatType());
        adaptedWeeklyActivity.setEndTime(weeklyActivity.getEndTime());
        adaptedWeeklyActivity.setStartTime(weeklyActivity.getStartTime());
        adaptedWeeklyActivity.setName(weeklyActivity.getName());

        return adaptedWeeklyActivity;
    }
}
