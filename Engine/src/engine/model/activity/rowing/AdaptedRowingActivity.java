package engine.model.activity.rowing;

import engine.model.Model;
import engine.model.activity.request.Request;
import engine.model.boat.Boat;

public class AdaptedRowingActivity extends Model {

    private Boat boat;
    private Request request;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boat getBoat() {
        return boat;
    }

    public void setBoat(Boat boat) {
        this.boat = boat;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}

