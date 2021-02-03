package engine.model.activity.rowing;

import engine.api.EngineInterface;
import engine.model.Modifier;
import engine.model.activity.request.Request;
import engine.model.boat.Boat;

import java.util.function.Consumer;

public class RowingActivityModifier extends Modifier<RowingActivity> {
    public RowingActivityModifier(EngineInterface engineContext, RowingActivity objectToEdit, Runnable collectionSaveFunc, Consumer<String> callBack) {
        super(engineContext, objectToEdit, collectionSaveFunc, callBack);
    }

    public void cloneBoat() {
        this.objectToEdit.cloneBoat();
        this.markModify();
        this.invokeCallBack("Scheduler boat successfully cloned.");
    }

    public void shallowSetRequest(Request request) {
        Request requestRef = getEngine().getRequestsCollectionManager().find(request);
        this.objectToEdit.shallowSetRequest(requestRef);
    }

    public void shallowSetBoat(Boat boat) {
        Boat boatRef = getEngine().getBoatsCollectionManager().find(boat);
        this.objectToEdit.shallowSetBoat(boatRef);
    }
}
