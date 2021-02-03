package engine.database.collection;

import engine.api.EngineInterface;
import engine.database.CollectionManager;
import engine.model.activity.request.Request;
import engine.model.activity.rowing.RowingActivity;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Objects;


public class RowingActivitiesCollectionManager extends CollectionManager<RowingActivity> {
    public RowingActivitiesCollectionManager(EngineInterface engine) {
        super(engine);
    }

    public RowingActivitiesCollectionManager(EngineInterface engineContext, List<RowingActivity> rowingActivities) {
        super(engineContext, rowingActivities);
    }

    @Override
    public boolean add(RowingActivity toAdd) {
        boolean requestAlreadyApproved = !filter(rowingActivity -> rowingActivity.getRequest().equals(toAdd.getRequest())).isEmpty();
        boolean res = false;
        Objects.requireNonNull(this.engine)
                .getRowingActivityModifier(toAdd, null).shallowSetBoat((Boat) this.engine.find(toAdd.getBoat()));
        this.engine
                .getRowingActivityModifier(toAdd, null).shallowSetRequest((Request) this.engine.find(toAdd.getRequest()));
        if (!requestAlreadyApproved) {
            this.data.add(toAdd);
            try {
                this.engine.getRequestModifier(toAdd.getRequest(), null).setApprovementStatus(true);
                res = true;
                this.engine.saveRowingActivitiesCollection();
            } catch (Exception e) {
                res = false;
            }
        }
        return res;
    }

    @Override
    public boolean remove(RowingActivity toRemove) {
        boolean result = false;

        if (this.data.remove(toRemove)) {
            Request request = (Request) Objects.requireNonNull(this.engine).find(toRemove.getRequest());
            Objects.requireNonNull(this.engine).getRequestModifier(request, null).setApprovementStatus(false);
            this.engine.saveRowingActivitiesCollection();
            result = true;
        }
        return result;
    }

    @Override
    public void importFromXml(String path, boolean cleanAll, StringBuilder stringBuilder) {
        throw new NotImplementedException();
    }

    @Override
    public String exportToXml() {
        throw new NotImplementedException();
    }

    public void cloneBoatsForPastActivities() {
        this.filter(RowingActivity::isOver).forEach(rowingActivity -> Objects.requireNonNull(this.engine)
                .getRowingActivityModifier(rowingActivity, null).cloneBoat());
    }

    public boolean isBoatAvailable(String serialNumber, Request request) {
        Boat boatToCheck = Objects.requireNonNull(this.engine).getBoatsCollectionManager().findBySerialNumber(serialNumber);

        if (boatToCheck != null) {
            return this.data.stream()
                    .filter(rowingActivity -> rowingActivity.getBoat().equals(boatToCheck))
                    .filter(rowingActivity -> rowingActivity.getRequest().getTrainingDate().equals(request.getTrainingDate()))
                    .noneMatch(rowingActivity -> rowingActivity.getRequest().getWeeklyActivityActivity().getEndTime()
                            .isAfter(request.getWeeklyActivityActivity().getStartTime()) ||
                            rowingActivity.getRequest().getWeeklyActivityActivity().getStartTime()
                                    .isBefore(request.getWeeklyActivityActivity().getEndTime()));
        } else {
            return false;
        }
    }

    public boolean isBoatAvailable(Boat boatToCheck, Request request) {
        return this.data.stream()
                .filter(rowingActivity -> rowingActivity.getBoat().equals(boatToCheck))
                .filter(rowingActivity -> rowingActivity.getRequest().getTrainingDate().equals(request.getTrainingDate()))
                .noneMatch(rowingActivity -> request.getWeeklyActivityActivity()
                        .isOverlapping(rowingActivity.getRequest().getWeeklyActivityActivity()));
    }

    public boolean isRowerAvailable(Rower rowerToCheck, Request request) {
        return this.data.stream()
                .filter(rowingActivity -> rowingActivity.getRequest().getMainRower().equals(rowerToCheck) ||
                        rowingActivity.getRequest().getOtherRowersList().contains(rowerToCheck))
                .filter(rowingActivity -> rowingActivity.getRequest().getTrainingDate().equals(request.getTrainingDate()))
                .noneMatch(rowingActivity -> request.getWeeklyActivityActivity()
                        .isOverlapping(rowingActivity.getRequest().getWeeklyActivityActivity()));
    }

    public boolean removeFutureBoatlessActivities(Boat activityBoat) {
        boolean activityDeleted = false;
        List<RowingActivity> activities = filter(rowingActivity -> !rowingActivity.isOver());

        for (RowingActivity activity : activities) {
            if (activity.getBoat().equals(activityBoat)) {
                remove(activity);
                activityDeleted = true;
            }
        }

        return activityDeleted;
    }
}
