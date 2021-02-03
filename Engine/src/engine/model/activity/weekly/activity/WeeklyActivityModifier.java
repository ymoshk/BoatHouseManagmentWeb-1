package engine.model.activity.weekly.activity;

import engine.api.EngineInterface;
import engine.model.Modifier;
import engine.model.boat.Boat;

import java.time.LocalTime;
import java.util.function.Consumer;

public class WeeklyActivityModifier extends Modifier<WeeklyActivity> {
    public WeeklyActivityModifier(EngineInterface engineContext, WeeklyActivity objectToEdit, Runnable collectionSaveFunc, Consumer<String> callBack) {
        super(engineContext, objectToEdit, collectionSaveFunc, callBack);
    }


    public void setName(String newName) {
        if (newName != null && !newName.isEmpty()) {
            this.objectToEdit.setName(newName);
            this.markModify();
            this.invokeCallBack("Weekly activity name successfully changed.");

        } else {
            invokeCallBack("Changing name failed. The new name can't be empty.");
        }
    }

    public void setStartTime(LocalTime newStartTime) {
        if (newStartTime != null) {
            if (newStartTime.isBefore(this.objectToEdit.getEndTime()) &&
                    !newStartTime.equals(this.objectToEdit.getEndTime())) {
                this.objectToEdit.setStartTime(newStartTime);
                this.markModify();
                this.invokeCallBack("Weekly activity start time successfully changed.");
            } else {
                invokeCallBack("Changing start time failed. New start must be before the current end time.");
            }

        } else {
            invokeCallBack("Changing start time failed. The new start time can't be empty.");
        }
    }

    public void setEndTime(LocalTime newEndTime) {
        if (newEndTime != null) {
            if (newEndTime.isAfter(this.objectToEdit.getStartTime()) &&
                    !newEndTime.equals(this.objectToEdit.getStartTime())) {
                this.objectToEdit.setEndTime(newEndTime);
                this.markModify();
                this.invokeCallBack("Weekly activity end time successfully changed.");
            } else {
                invokeCallBack("Changing end time failed. The new end must be after the current start time.");
            }

        } else {
            invokeCallBack("Changing end time failed. The new end time can't be empty.");
        }
    }

    public void setRequiredBoatType(Boat.eBoatType newType) {
        if (newType != null) {
            this.objectToEdit.setBoatType(newType);
            this.markModify();
            this.invokeCallBack("Weekly activity boat type successfully changed.");
        } else {
            this.invokeCallBack("Changing boat type failed. The boat type can't be empty.");
        }
    }
}