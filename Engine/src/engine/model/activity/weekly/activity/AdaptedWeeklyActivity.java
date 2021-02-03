package engine.model.activity.weekly.activity;

import engine.model.Model;
import engine.model.boat.Boat;

import java.time.LocalTime;

public class AdaptedWeeklyActivity extends Model {

    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boat.eBoatType boatType = null;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Boat.eBoatType getBoatType() {
        return boatType;
    }

    public void setBoatType(Boat.eBoatType boatType) {
        this.boatType = boatType;
    }
}

