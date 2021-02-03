package engine.database.collection;

import engine.api.EngineInterface;
import engine.database.CollectionManager;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import engine.xml.XmlConverter;
import engine.xml.model.util.boats.BoatUtils;
import jaxb.schema.engine.generated.boat.Boats;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BoatsCollectionManager extends CollectionManager<Boat> {

    public BoatsCollectionManager(EngineInterface engine) {
        super(engine);
    }

    public BoatsCollectionManager(EngineInterface engineContext, List<Boat> boats) {
        super(engineContext, boats);
    }

    @Override
    public boolean add(Boat toAdd) {
        if (!isBoatExist(toAdd)) {
            this.data.add(toAdd);
            Objects.requireNonNull(this.engine).saveBoatsCollection();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(Boat toRemove) {
        if (this.data.contains(toRemove)) {
            if (toRemove.hasOwner()) {
                Rower owner = (Rower) Objects.requireNonNull(this.engine)
                        .find(toRemove.getOwner());
                owner.removePrivateBoat(toRemove);
            }
            boolean res = this.data.remove(toRemove);
            if (res) {
                Objects.requireNonNull(this.engine).getRowingActivitiesCollectionManager()
                        .removeFutureBoatlessActivities(toRemove);
                this.engine.saveBoatsCollection();
            }
            return res;
        } else {
            return false;
        }
    }

    @Override
    public void importFromXml(String xmlAsString, boolean cleanAll, StringBuilder stringBuilder) throws JAXBException {
        InputStream inputStream = new ByteArrayInputStream(xmlAsString.getBytes());
        Boats boats = deserializeBoats(inputStream);
        List<Boat> backUp = new ArrayList<>(this.data);

        if (cleanAll) {
            backUp.forEach(this::remove);
        }

        int count = BoatUtils.importBoatsToCollection(boats, stringBuilder);

        if (this.data.isEmpty()) {
            stringBuilder.append("Could not import any of the boats. The boats weren't removed.\n");
            backUp.forEach(this::add);
        }

        stringBuilder.append("Successfully imported ").append(count)
                .append(" boats out of ").append(boats.getBoat().size()).append(" boats.\n");
    }

    private Boats deserializeBoats(InputStream inputStream) throws JAXBException {
        Unmarshaller unmarshaller = initializeJAXB(Boats.class, "boats.xsd");
        return (Boats) unmarshaller.unmarshal(inputStream);
    }


    @Override
    public String exportToXml() throws JAXBException {
        Boats boats = new Boats();
        this.data.forEach(boat -> boats.getBoat()
                .add(BoatUtils.convertBoatToBoatSchemeObject(boat)));
        return XmlConverter.convertObjectToXMLString(boats);
    }

    public Boat findBySerialNumber(String serial) {
        for (Boat boat : this) {
            if (boat.getSerialNumber().equalsIgnoreCase(serial))
                return boat;
        }
        return null;
    }

    public boolean isBoatExist(Boat boat) {
        for (Boat bt : this) {
            if (bt.equals(boat)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBoatSerialNumberAvailable(String serial) {
        return this.data.stream().noneMatch(boat -> boat.getSerialNumber().equalsIgnoreCase(serial));
    }
}
