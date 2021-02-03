package engine.api;

import engine.model.activity.request.Request;
import engine.model.activity.rowing.RowingActivity;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import engine.model.rower.RowerModifier;
import engine.utils.crypto.RC4;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class AdaptedEngineContext {
    private List<Rower> rowers;
    private List<Boat> boats;
    private List<WeeklyActivity> weeklyActivities;
    private List<Request> requests;
    private List<RowingActivity> rowingActivities;

    public AdaptedEngineContext() {

    }

    public AdaptedEngineContext(EngineInterface engineContext) {

        // Bonus #1 - encryption
        this.rowers = new ArrayList<>();
        // To prevent passwords from being changed in RAM
        engineContext.getRowersCollectionManager().forEach(rower -> this.rowers.add(rower.clone(true)));

        for (Rower rower : this.rowers) {
            RowerModifier modifier = new RowerModifier(null, rower, null, null);
            modifier.setRowerPassword(RC4.encrypt(rower.getPassword()));
        }

        this.boats = engineContext.getBoatsCollectionManager().toArrayList();
        this.weeklyActivities = engineContext.getWeeklyActivitiesCollectionManager().toArrayList();
        this.requests = engineContext.getRequestsCollectionManager().toArrayList();
        this.rowingActivities = engineContext.getRowingActivitiesCollectionManager().toArrayList();
    }

    public List<Rower> getRowers() {
        return this.rowers;
    }

    public void setRowers(List<Rower> rowers) {
        this.rowers = rowers;
    }

    public List<Boat> getBoats() {
        return this.boats;
    }

    public void setBoats(List<Boat> boats) {
        this.boats = boats;
    }

    public List<WeeklyActivity> getWeeklyActivities() {
        return this.weeklyActivities;
    }

    public void setWeeklyActivities(List<WeeklyActivity> weeklyActivities) {
        this.weeklyActivities = weeklyActivities;
    }

    public List<Request> getRequests() {
        return this.requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public List<RowingActivity> getRowingActivities() {
        return this.rowingActivities;
    }

    public void setRowingActivities(List<RowingActivity> rowingActivities) {
        this.rowingActivities = rowingActivities;
    }
}
