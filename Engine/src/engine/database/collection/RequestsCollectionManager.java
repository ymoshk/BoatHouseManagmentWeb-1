package engine.database.collection;

import engine.api.EngineInterface;
import engine.database.CollectionManager;
import engine.model.activity.request.Request;
import engine.model.activity.request.RequestModifier;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.rower.Rower;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RequestsCollectionManager extends CollectionManager<Request> {
    public RequestsCollectionManager(EngineInterface engine) {
        super(engine);
    }

    public RequestsCollectionManager(EngineInterface engineContext, List<Request> requests) {
        super(engineContext, requests);
    }

    @Override
    public boolean add(Request toAdd) {
        Objects.requireNonNull(this.engine).getRequestModifier(toAdd, null)
                .shallowSetMainRower((Rower) this.engine.find(toAdd.getMainRower()));
        this.engine.getRequestModifier(toAdd, null).shallowSetCreator((Rower) this.engine
                .find(toAdd.getRequestCreator()));
        this.engine.getRequestModifier(toAdd, null).shallowSetOtherRowers(toAdd.getOtherRowersList());
        this.data.add(toAdd);
        this.engine.saveRequestsCollection();
        return true;
    }

    @Override
    public boolean remove(Request toRemove) {
        boolean res = this.data.remove(toRemove);
        if (res) {
            Objects.requireNonNull(this.engine).saveRequestsCollection();
        }
        return res;
    }

    @Override
    public void importFromXml(String path, boolean cleanAll, StringBuilder stringBuilder) {
        throw new NotImplementedException();
    }

    @Override
    public String exportToXml() {
        throw new NotImplementedException();
    }

    public List<Request> getRelevantRequests(Rower loggedInRower) {
        return getRelevantRequests(loggedInRower, null);
    }

    public List<Request> getRelevantRequests(Rower loggedInRower, Predicate<Request> filter) {
        List<Request> result;

        if (!loggedInRower.isAdmin()) {
            result = this.filter(request -> request.getOtherRowersList().contains(loggedInRower)
                    || request.getMainRower().equals(loggedInRower)
                    || request.getRequestCreator().equals(loggedInRower));
        } else {
            result = this.data;
        }

        if (filter != null) {
            result = result.stream().filter(filter).collect(Collectors.toList());
        }

        return Collections.unmodifiableList(result);
    }

    public void removeRowersFromPastActivities(Rower rower) {
        this.data.stream()
                .filter(Request::isOver)
                .filter(request -> request.getMainRower().equals(rower) ||
                        request.getOtherRowersList().contains(rower) ||
                        request.getRequestCreator().equals(rower))
                .forEach(request -> {
                    try {
                        Objects.requireNonNull(this.engine)
                                .getRequestModifier(request, null).removeRowerFromPassedRequest(rower);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public boolean isRowerAvailableForActivity(Rower rower, WeeklyActivity weeklyActivity, LocalDate date) {
        return this.data.stream()
                .filter(request -> request.getOtherRowersList().contains(rower) || request.getMainRower().equals(rower))
                .filter(request -> request.getTrainingDate().equals(date))
                .allMatch(request -> request.getWeeklyActivityActivity().getEndTime().isBefore(weeklyActivity.getStartTime()) ||
                        request.getWeeklyActivityActivity().getStartTime().isAfter(weeklyActivity.getEndTime()));
    }

    public void removeRowerFromFutureRequest(Rower rower) {
        this.data.stream()
                .filter(request -> !request.isOver())
                .filter(request -> request.getMainRower().equals(rower) ||
                        request.getRequestCreator().equals(rower) ||
                        request.getOtherRowersList().contains(rower))
                .forEach(request -> removeRowerFromFutureRequestHelper(request, rower));
    }

    private void removeRowerFromFutureRequestHelper(Request request, Rower rower) {
        RequestModifier requestModifier = Objects.requireNonNull(engine).getRequestModifier(request, null);
        requestModifier.removeRowerFromFutureRequest(rower);
    }
}

