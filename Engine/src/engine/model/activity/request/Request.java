package engine.model.activity.request;

import engine.model.Model;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import engine.xml.adapter.LocalDateAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class Request extends Model implements Serializable {

    @XmlList
    private final List<Boat.eBoatType> boatTypesList;
    private Rower mainRower;
    private Rower requestCreator;
    private WeeklyActivity weeklyActivity;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate registrationDate;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate trainingDate;
    @XmlElementWrapper
    private List<Rower> otherRowersList;
    private boolean isApproved = false;

    public Request() {
        this.boatTypesList = null;
        this.otherRowersList = null;
        this.registrationDate = null;
        this.requestCreator = null;
    }

    public Request(Rower rower, Rower theRegisteredRower, WeeklyActivity weeklyActivity, LocalDate trainingDate,
                   List<Rower> otherRowersList, List<Boat.eBoatType> boatType) {

        this.mainRower = rower;
        this.requestCreator = theRegisteredRower;
        this.weeklyActivity = weeklyActivity;

        if (otherRowersList != null) {
            this.otherRowersList = otherRowersList;
        } else {
            this.otherRowersList = new ArrayList<>();
        }
        this.boatTypesList = boatType;
        this.trainingDate = trainingDate;
        this.registrationDate = LocalDate.now();
    }

    // Constructor for XML adapter only.
    public Request(Rower rower, Rower theRegisteredRower, WeeklyActivity weeklyActivity, LocalDate trainingDate,
                   List<Rower> otherRowersList, List<Boat.eBoatType> boatType, LocalDate registrationDate, boolean isApproved) {
        this(rower, theRegisteredRower, weeklyActivity, trainingDate, otherRowersList, boatType);
        this.isApproved = isApproved;
        this.registrationDate = registrationDate;
    }

    public static int getMaxPossibleRowers(List<Boat.eBoatType> boatTypesList) {
        return boatTypesList
                .stream()
                .mapToInt(Boat.eBoatType::getNumOfRowers)
                .max().orElse(0);
    }

    public Rower getMainRower() {
        return this.mainRower;
    }

    void setMainRower(Rower mainRower) {
        this.mainRower = mainRower;
    }

    void setCreator(Rower rower) {
        this.requestCreator = rower;
    }

    public Rower getRequestCreator() {
        return this.requestCreator;
    }

    public WeeklyActivity getWeeklyActivityActivity() {
        return this.weeklyActivity;
    }

    void setWeeklyActivity(WeeklyActivity weeklyActivity) {
        this.weeklyActivity = weeklyActivity;
    }

    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public LocalDate getTrainingDate() {
        return this.trainingDate;
    }

    void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public List<Boat.eBoatType> getBoatTypesList() {
        return Collections.unmodifiableList(this.boatTypesList);
    }

    protected List<Boat.eBoatType> getModifiableBoatTypesList() {
        return this.boatTypesList;
    }

    public List<Rower> getOtherRowersList() {
        if (this.otherRowersList != null) {
            return Collections.unmodifiableList(this.otherRowersList);
        } else {
            return null;
        }
    }

    protected List<Rower> getModifiableRowersList() {
        return this.otherRowersList;
    }

    public boolean isApproved() {
        return this.isApproved;
    }

    void setApprovement(boolean newStatus) {
        this.isApproved = newStatus;
    }

    public int getMaxPossibleRowers() {
        return getMaxPossibleRowers(this.boatTypesList);
    }

    public boolean isOver() {
        return this.getTrainingDate().isBefore(LocalDate.now()) ||
                (this.getTrainingDate().equals(LocalDate.now()) &&
                        this.getWeeklyActivityActivity().getEndTime().isBefore(LocalTime.now()));
    }

    public int getTotalCountOfRowers() {
        return this.otherRowersList.size() + 1;
    }

    public Request clone() {
        try {
            return new Request(this.mainRower, this.requestCreator, this.weeklyActivity.cloneWeeklyActivity(),
                    this.trainingDate, this.otherRowersList, this.boatTypesList);
        } catch (Exception e) {
            return null;
        }
    }

    public Request clone(List<Rower> newRowers) {
        Rower newMain = newRowers.get(0);
        newRowers.remove(0);
        List<Boat.eBoatType> newBoatTypeList = new ArrayList<>(this.boatTypesList);

        return new Request(newMain, this.requestCreator, this.weeklyActivity.cloneWeeklyActivity(),
                this.trainingDate, newRowers, newBoatTypeList);
    }

    void shallowSetOtherRowersList(List<Rower> list) {
        this.otherRowersList = list;
    }
}
