package engine.model.activity.request;

import engine.model.Model;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.boat.Boat;
import engine.model.rower.Rower;

import java.time.LocalDate;
import java.util.List;

public class AdaptedRequest extends Model {

    private Rower mainRower;
    private Rower requestCreator;
    private WeeklyActivity weeklyActivity;
    private LocalDate registrationDate;
    private LocalDate trainingDate;
    private List<Boat.eBoatType> boatTypesList;
    private List<Rower> otherRowersList;
    private boolean isApproved = false;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rower getMainRower() {
        return mainRower;
    }

    public void setMainRower(Rower mainRower) {
        this.mainRower = mainRower;
    }

    public Rower getRequestCreator() {
        return requestCreator;
    }

    public void setRequestCreator(Rower requestCreator) {
        this.requestCreator = requestCreator;
    }

    public WeeklyActivity getWeeklyActivity() {
        return weeklyActivity;
    }

    public void setWeeklyActivity(WeeklyActivity weeklyActivity) {
        this.weeklyActivity = weeklyActivity;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public List<Boat.eBoatType> getBoatTypesList() {
        return boatTypesList;
    }

    public void setBoatTypesList(List<Boat.eBoatType> boatTypesList) {
        this.boatTypesList = boatTypesList;
    }

    public List<Rower> getOtherRowersList() {
        return otherRowersList;
    }

    public void setOtherRowersList(List<Rower> otherRowersList) {
        this.otherRowersList = otherRowersList;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}

