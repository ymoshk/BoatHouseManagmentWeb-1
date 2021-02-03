package engine.model.activity.rowing;

import engine.model.Model;
import engine.model.activity.request.Request;
import engine.model.boat.Boat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "RowingActivity")
@XmlAccessorType(XmlAccessType.FIELD)
public class RowingActivity extends Model implements Serializable {
    Request request;
    private Boat boat;

    public RowingActivity() {

    }

    public RowingActivity(Boat boat, Request request) throws IllegalArgumentException {

        if (boat == null || request == null) {
            throw new IllegalArgumentException();
        }

        if (!boat.isPrivate()) {
            if (!request.getBoatTypesList().contains(boat.getBoatType())) {
                throw new IllegalArgumentException();
            }

            if (boat.getBoatType().getNumOfRowers() != request.getTotalCountOfRowers()) {
                throw new IllegalArgumentException();
            }
        }

        this.request = request;
        this.boat = boat;
    }

    public Request getRequest() {
        return this.request;
    }


    public Boat getBoat() {
        return this.boat;
    }

    public boolean isOver() {
        return this.getRequest().isOver();
    }

    protected void cloneBoat() {
        this.boat = this.boat.cloneWithOutOwner();
    }

    void shallowSetRequest(Request request) {
        this.request = request;
    }

    public void shallowSetBoat(Boat boat) {
        this.boat = boat;
    }
}
