package engine.model.boat;

import engine.api.EngineInterface;
import engine.model.Modifier;
import engine.model.rower.Rower;

import java.util.function.Consumer;

public class BoatModifier extends Modifier<Boat> {

    public BoatModifier(EngineInterface engineContext, Boat objectToEdit, Runnable collectionSaveFunc, Consumer<String> callBack) {
        super(engineContext, objectToEdit, collectionSaveFunc, callBack);
    }

    public void setName(String newName) {
        if (newName != null && !newName.isEmpty()) {
            this.objectToEdit.setName(newName);
            this.markModify();
            invokeCallBack("Boat name successfully changed.");
        } else {
            invokeCallBack("Couldn't change boat name. The new name can't be empty.");
        }
    }

    public void setBoatType(Boat.eBoatType newBoatType) {
        if (newBoatType != null) {
            this.objectToEdit.setBoatType(newBoatType);
            this.markModify();
            invokeCallBack("Boat type successfully changed.");
        } else {
            invokeCallBack("Couldn't change boat type.");
        }
    }

    public void shallowSetOwner(Rower newOwner) {
        if (newOwner != null) {
            this.objectToEdit.shallowSetOwner(getEngine().getRowersCollectionManager().find(newOwner));
        }
    }

    public void setOwner(Rower newOwner) {
        if (newOwner != null) {
            Rower RowerRef = this.getEngine().getRowersCollectionManager().find(newOwner);
            this.objectToEdit.setOwner(RowerRef);
            this.objectToEdit.getOwner().addPrivateBoat(this.objectToEdit);
            this.markModify();
            this.getEngine().saveRowersCollection();
            invokeCallBack("Boat owner successfully changed.");
        } else {
            invokeCallBack("Couldn't change boat owner. New owner must be provided.");
        }
    }

    public void removeOwner() {
        this.objectToEdit.removeOwner();
        this.markModify();
        this.getEngine().saveRowersCollection();
        invokeCallBack("Owner successfully changed.");
    }

    public void setState(boolean isDisable) {
        this.objectToEdit.setState(isDisable);

        if (isDisable) {
            boolean activitiesDeleted = getEngine().getRowingActivitiesCollectionManager()
                    .removeFutureBoatlessActivities(this.objectToEdit);

            if (activitiesDeleted) {
                invokeCallBack("This boat took part in some rowing activities," +
                        "\n these activities have been removed and their requests became disproved");
            }
        }

        this.markModify();
        invokeCallBack("Boat new state is " + (isDisable ? "disabled" : "enabled"));
    }

    public void setIsWide(boolean isWide) {
        this.objectToEdit.setIsWide(isWide);
        this.markModify();
        invokeCallBack("Boat new state is " + (isWide ? "wide" : "not wide"));
    }

    public void setIsSeaBoat(boolean isSeaBoat) {
        this.objectToEdit.setIsSeaBoat(isSeaBoat);
        this.markModify();
        invokeCallBack("Boat new state is " + (isSeaBoat ? "sea boat" : "not sea boat"));
    }
}