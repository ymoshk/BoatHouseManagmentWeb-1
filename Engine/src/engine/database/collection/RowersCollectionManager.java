package engine.database.collection;

import engine.api.EngineContext;
import engine.api.EngineInterface;
import engine.database.CollectionManager;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import engine.xml.XmlConverter;
import engine.xml.model.util.member.MemberUtils;
import jaxb.schema.engine.generated.member.Members;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;


public class RowersCollectionManager extends CollectionManager<Rower> {

    public RowersCollectionManager(EngineInterface engine) {
        super(engine);
    }

    public RowersCollectionManager(EngineInterface engine, List<Rower> data) {
        super(engine, data);
    }

    @Override
    public boolean add(Rower toAdd) {
        if (!rowerExist(toAdd)) {
            shallowAdd(toAdd);
            Objects.requireNonNull(this.engine).saveRowersCollection();
            return true;
        } else {
            return false;
        }
    }

    public void shallowAdd(Rower rower) {
        this.data.add(rower);
    }

    @Override
    public boolean remove(Rower toRemove) {
        boolean result = false;
        if (rowerExist(toRemove)) {
            if (toRemove.hasPrivateBoat()) {
                Set<String> tempSet = new HashSet<>(toRemove.getPrivateBoatsSerialNumbers());
                for (String serialNumber : tempSet) {
                    Boat boat = Objects.requireNonNull(engine).getBoatsCollectionManager()
                            .findBySerialNumber(serialNumber);
                    if (boat != null) {
                        this.engine.getBoatModifier(boat, null).removeOwner();
                    }
                }
            }

            if (!Objects.requireNonNull(engine).canRowerBeRemoved(toRemove)) {
                engine.getRequestsCollectionManager().removeRowerFromFutureRequest(toRemove);
            }

            result = this.data.remove(toRemove);
        }

        if (result) {
            this.engine.saveRowersCollection();
        }

        return result;
    }

    @Override
    public void importFromXml(String xmlAsString, boolean cleanAll, StringBuilder stringBuilder) throws JAXBException {
        InputStream inputStream = new ByteArrayInputStream(xmlAsString.getBytes());
        Members members = deserializeMember(inputStream);

        if (cleanAll) {
            if (MemberUtils.verifyAdmin(members)) {
                List<Rower> tempList = new ArrayList<>(this.data);
                tempList.stream()
                        .filter(rower -> !(((EngineContext) Objects.requireNonNull(this.engine))
                                .getCurrentLoggedInUsers().contains(rower)))
                        .forEach(this::remove);
            } else {
                stringBuilder.append("Couldn't find a valid admin in the imported data\n");
                return;
            }
        }

        int count = MemberUtils.addValidMembers(members, stringBuilder);
        stringBuilder.append("Successfully imported ").append(count)
                .append(" rowers out of ").append(members.getMember().size()).append(" rowers.\n");
    }

    //"members.xsd"
    private Members deserializeMember(InputStream inputStream) throws JAXBException {
        Unmarshaller unmarshaller = initializeJAXB(Members.class, "/jaxb/schema/engine/generated/member/members.xsd");
        return (Members) unmarshaller.unmarshal(inputStream);
    }


    @Override
    public String exportToXml() throws JAXBException {
        Members members = new Members();
        this.data.forEach(rower -> members.getMember().add(MemberUtils.convertRowerToMember(rower)));
        return XmlConverter.convertObjectToXMLString(members);
    }

    @Override
    public Rower findByUniqueIdentifier(String uniqueString) {
        return findRowerBySerialNumber(uniqueString);
    }

    public boolean isSerialNumberAvailable(String serialNumber) {
        return serialNumber != null &&
                !serialNumber.isEmpty() &&
                this.data.stream().noneMatch(rower -> rower.getSerialNumber().equalsIgnoreCase(serialNumber));
    }

    public boolean rowerExist(Rower rower) {
        return this.data.contains(rower);
    }

    public Rower findRower(String email, String password) {
        return this.data.stream()
                .filter(rower -> rower.getEmail().equalsIgnoreCase(email))
                .filter(rower -> rower.getPassword().equalsIgnoreCase(password))
                .findFirst().orElse(null);
    }

    public Rower findRowerBySerialNumber(String serialNumber) {
        return this.data.stream()
                .filter(rower -> rower.getSerialNumber().equalsIgnoreCase(serialNumber))
                .findFirst().orElse(null);
    }

    public boolean emailExist(String email) {
        return this.data.stream().anyMatch(rower -> rower.getEmail().equalsIgnoreCase(email));
    }
}
