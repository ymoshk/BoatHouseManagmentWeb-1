package engine.model.activity.request;

import engine.api.EngineInterface;
import engine.database.collection.RowingActivitiesCollectionManager;
import engine.model.Modifier;
import engine.model.activity.rowing.RowingActivity;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.boat.Boat;
import engine.model.rower.Rower;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RequestModifier extends Modifier<Request> {
    final String APPROVED_ERROR = "You can't modify an approved training request. Ask an admin to disapprove the request";

    public RequestModifier(EngineInterface engineContext, Request objectToEdit, Runnable collectionSaveFunc, Consumer<String> callBack) {
        super(engineContext, objectToEdit, collectionSaveFunc, callBack);
    }

    public void setMainRower(Rower newRower) {
        if (!this.objectToEdit.isApproved()) {
            if (newRower != null) {
                Rower rowerRef = getEngine().getRowersCollectionManager().find(newRower);
                this.objectToEdit.setMainRower(rowerRef);
                this.markModify();
                this.invokeCallBack("Request main rower successfully changed.");
            }
        } else {
            this.invokeCallBack(this.APPROVED_ERROR);
        }
    }

    public void setWeeklyActivity(WeeklyActivity newWeeklyActivity) {
        if (!this.objectToEdit.isApproved()) {
            WeeklyActivity weeklyActivity = getEngine().getWeeklyActivitiesCollectionManager().find(newWeeklyActivity);
            this.objectToEdit.setWeeklyActivity(weeklyActivity);
            this.markModify();
            this.invokeCallBack("Request weekly activity successfully changed.");
        } else {
            this.invokeCallBack(this.APPROVED_ERROR);
        }
    }

    public void shallowSetWeeklyActivity(WeeklyActivity newWeeklyActivity) {
        WeeklyActivity weeklyActivity = getEngine().getWeeklyActivitiesCollectionManager().find(newWeeklyActivity);
        this.objectToEdit.setWeeklyActivity(weeklyActivity);
    }

    public void setTrainingDate(LocalDate newTrainingDate) {
        if (!this.objectToEdit.isApproved()) {
            if (!newTrainingDate.isBefore(LocalDate.now())) {
                this.objectToEdit.setTrainingDate(newTrainingDate);
                this.markModify();
                this.invokeCallBack("Request training date successfully changed.");
            } else {
                this.invokeCallBack("The new training date must be greater then the current date.");
            }
        } else {
            this.invokeCallBack(this.APPROVED_ERROR);
        }
    }

    public void setApprovementStatus(boolean newStatus) {
        this.objectToEdit.setApprovement(newStatus);
        this.markModify();
        this.invokeCallBack("Request status successfully changed.");
    }

    public void addRowerToRequest(Rower newRower) {
        if (!this.objectToEdit.isApproved()) {
            int maxNumOfRowers = this.objectToEdit.getMaxPossibleRowers() - 1; // '-1' cause the main rower
            if (this.objectToEdit.getOtherRowersList().size() < maxNumOfRowers) {
                if (newRower.equals(this.objectToEdit.getMainRower())) {
                    this.invokeCallBack("You can't add the main rower of the request to the other rowers list");
                } else if (this.objectToEdit.getOtherRowersList().contains(newRower)) {
                    this.invokeCallBack("The other rowers list already contains the rower you're trying to add");
                } else {
                    Rower rowerRef = getEngine().getRowersCollectionManager().find(newRower);
                    this.objectToEdit.getModifiableRowersList().add(rowerRef);
                    markModify();
                    this.invokeCallBack("Rower successfully added to the request.");
                }
            } else {
                this.invokeCallBack("You already reached the maximum number of rowers you can add for the" +
                        " boat types you selected. Adding rower failed.");
            }
        } else {
            this.invokeCallBack(this.APPROVED_ERROR);
        }
    }

    public void removeRowerFromOtherRowersList(Rower toRemove) {
        if (!this.objectToEdit.isApproved()) {
            Rower rowerRef = getEngine().getRowersCollectionManager().find(toRemove);
            if (this.objectToEdit.getModifiableRowersList().remove(rowerRef)) {
                markModify();
                this.invokeCallBack("Rower successfully removed from request rowers list.");
            } else {
                this.invokeCallBack("Couldn't remove the rower from the request rowers list.");
            }
        } else {
            this.invokeCallBack(this.APPROVED_ERROR);
        }
    }

    public void addBoatTypeOption(Boat.eBoatType newBoatType) {
        if (!this.objectToEdit.isApproved()) {
            if (!this.objectToEdit.getBoatTypesList().contains(newBoatType)) {
                this.objectToEdit.getModifiableBoatTypesList().add(newBoatType);
                this.markModify();
                this.invokeCallBack("Boat type successfully added to request boat type list.");
            } else {
                this.invokeCallBack("You can't add a boat type that already exist in this request");
            }
        } else {
            this.invokeCallBack(this.APPROVED_ERROR);
        }
    }

    public void removeBoatType(Boat.eBoatType toRemove) {
        if (!this.objectToEdit.isApproved()) {
            if (this.objectToEdit.getModifiableBoatTypesList().size() > 1) {
                if (numberOfRowersMinimized(toRemove)) {
                    if (this.objectToEdit.getModifiableBoatTypesList().remove(toRemove)) {
                        markModify();
                        this.invokeCallBack("Boat type successfully removed from request.");
                    } else {
                        this.invokeCallBack("Couldn't remove the boat type from the request.");
                    }
                }
            } else {
                this.invokeCallBack("You must have at least 1 boat type per request. You can't remove the only boat type.");
            }
        } else {
            this.invokeCallBack(this.APPROVED_ERROR);
        }
    }

    private boolean numberOfRowersMinimized(Boat.eBoatType toRemove) {

        if (!this.objectToEdit.getModifiableBoatTypesList().remove(toRemove)) {
            this.invokeCallBack("You're trying to remove a boat type that isn't a part of the request.");
            return false;
        } else if (this.objectToEdit.getOtherRowersList().size() > this.objectToEdit.getMaxPossibleRowers()) {
            this.objectToEdit.getModifiableBoatTypesList().add(toRemove);
            this.invokeCallBack("If you delete this boat type, the request other rowers list will contain too many rowers." +
                    " Please delete some to continue this action.");
            return false;
        } else {
            this.objectToEdit.getModifiableBoatTypesList().add(toRemove);
            return true;
        }
    }

    public void removeRowerFromPassedRequest(Rower rower) {
        if (objectToEdit.isOver()) {
            Rower clonedRower = rower.clone();

            if (this.objectToEdit.getMainRower().equals(rower)) {
                this.objectToEdit.setMainRower(clonedRower);
            }
            if (this.objectToEdit.getRequestCreator().equals(rower)) {
                this.objectToEdit.setCreator(clonedRower);
            }
            if (this.objectToEdit.getOtherRowersList().contains(rower)) {
                this.objectToEdit.getOtherRowersList().remove(rower);
                this.objectToEdit.getOtherRowersList().add(clonedRower);
            }
            markModify();
        }
    }

    public void removeRowerFromFutureRequest(Rower rower) {

        if (!objectToEdit.isOver()) {

            if (this.objectToEdit.isApproved()) {
                RowingActivity activity = this.getEngine().getRowingActivitiesCollectionManager()
                        .filter(rowingActivity -> rowingActivity.getRequest().equals(this.objectToEdit)).get(0);
                this.getEngine().getRowingActivitiesCollectionManager().remove(activity);
            }

            this.objectToEdit.getModifiableRowersList().remove(rower);

            if (this.objectToEdit.getMainRower().equals(rower)) {
                removeMainRower();
            }

            if (this.objectToEdit.getRequestCreator().equals(rower)) {
                this.objectToEdit.setCreator(rower.clone());
            }

            markModify();
        }
    }

    public boolean removeMainRower() {
        List<Rower> otherRowers = this.objectToEdit.getOtherRowersList();
        boolean result;

        if (otherRowers.size() != 0) {
            Rower newMainRower = this.objectToEdit.getOtherRowersList().get(0);
            setMainRower(newMainRower);
            removeRowerFromOtherRowersList(newMainRower);
            result = true;
        } else {
            getEngine().getRequestsCollectionManager().remove(this.objectToEdit);
            result = false;
        }

        markModify();
        return result;
    }


    /**
     * @return false if the modified request was deleted because there were no rowers left in it.
     */
    public boolean removeUnavailableRowers() {
        RowingActivitiesCollectionManager rowingActivities = this.getEngine().getRowingActivitiesCollectionManager();
        boolean result = true;
        List<Rower> temp = new ArrayList<>(this.objectToEdit.getOtherRowersList());

        for (Rower rower : temp) {
            if (!rowingActivities.isRowerAvailable(rower, this.objectToEdit)) {
                disableCallback();
                removeRowerFromOtherRowersList(rower);
                enableCallback();
            }
        }

        if (!rowingActivities.isRowerAvailable(this.objectToEdit.getMainRower(), this.objectToEdit)) {
            disableCallback();
            if (removeMainRower()) {
                enableCallback();
                invokeCallBack("The main rower of the request takes a part in other activity during the time" +
                        " window of this one. \nHe automatically been removed from the request.");
                result = true;
            } else {
                enableCallback();
                // The main rower was the only rower, request is now removed.
                invokeCallBack("The main rower of the request takes a part in other activity during the time" +
                        " window of this one. \nSince he was the only rower of this request, the request was removed from the system.");
                result = false;
            }
        }
        return result;
    }

    public void shallowSetMainRower(Rower rower) {
        Rower rowerRef = getEngine().getRowersCollectionManager().find(rower);
        this.objectToEdit.setMainRower(rowerRef);
    }

    public void shallowSetCreator(Rower rower) {
        Rower rowerRef = getEngine().getRowersCollectionManager().find(rower);
        this.objectToEdit.setCreator(rowerRef);
    }

    public void shallowSetOtherRowers(List<Rower> list) {
        List<Rower> newList = new ArrayList<>();
        list.forEach(rower -> newList.add(getEngine().getRowersCollectionManager().find(rower)));
        this.objectToEdit.shallowSetOtherRowersList(newList);
    }
}
