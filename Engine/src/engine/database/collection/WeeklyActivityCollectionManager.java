package engine.database.collection;

import engine.api.EngineInterface;
import engine.database.CollectionManager;
import engine.model.activity.request.Request;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.xml.XmlConverter;
import engine.xml.model.util.weekly.activity.WeeklyActivityUtils;
import jaxb.schema.engine.generated.activity.Activities;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeeklyActivityCollectionManager extends CollectionManager<WeeklyActivity> {

    public WeeklyActivityCollectionManager(EngineInterface engine) {
        super(engine);
    }

    public WeeklyActivityCollectionManager(EngineInterface engineContext, List<WeeklyActivity> weeklyActivities) {
        super(engineContext, weeklyActivities);
    }

    @Override
    public boolean add(WeeklyActivity toAdd) {
        boolean result = true;

        if (this.data.contains(toAdd)) {
            result = false;
        } else {
            this.data.add(toAdd);
            Objects.requireNonNull(this.engine).saveWeeklyActivitiesCollection();
        }

        return result;
    }

    @Override
    public boolean remove(WeeklyActivity toRemove) {
        if (this.data.remove(toRemove)) {
            Objects.requireNonNull(this.engine).saveWeeklyActivitiesCollection();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void importFromXml(String xmlAsString, boolean cleanAll, StringBuilder stringBuilder) throws JAXBException {
        InputStream inputStream = new ByteArrayInputStream(xmlAsString.getBytes());
        Activities timeFrames = deserializeTimeframe(inputStream);
        List<WeeklyActivity> backUp = new ArrayList<>(this.data);

        if (cleanAll) {
            this.data.clear();
        }

        int count = WeeklyActivityUtils.addAllTimeFrames(timeFrames, stringBuilder);

        if (this.data.isEmpty()) {
            stringBuilder.append("Could not import any of the weekly activities. " +
                    "The weekly activities weren't removed.\n");
            backUp.forEach(this::add);
        }

        stringBuilder.append("Successfully imported ").append(count)
                .append(" weekly activities out of ").append(timeFrames.getTimeframe().size())
                .append(" weekly activities.\n");
    }

    private Activities deserializeTimeframe(InputStream inputStream) throws JAXBException {
        Unmarshaller unmarshaller = initializeJAXB(Activities.class, "activities.xsd");
        return (Activities) unmarshaller.unmarshal(inputStream);
    }

    @Override
    public String exportToXml() throws JAXBException {
        Activities activities = new Activities();
        this.data.forEach(weeklyActivity -> activities.getTimeframe()
                .add(WeeklyActivityUtils.convertWeeklyActivityToTimeFrame(weeklyActivity)));
        return XmlConverter.convertObjectToXMLString(activities);
    }

    @Override
    public WeeklyActivity find(WeeklyActivity objectToFind) {
        WeeklyActivity res = super.find(objectToFind);

        if (res == null) {
            List<Request> tempReqList = Objects.requireNonNull(engine).getRequestsCollectionManager().filter(
                    request -> request.getWeeklyActivityActivity().equals(objectToFind));
            if (!tempReqList.isEmpty()) {
                return tempReqList.get(0).getWeeklyActivityActivity();
            }
        }
        return objectToFind;
    }
}
