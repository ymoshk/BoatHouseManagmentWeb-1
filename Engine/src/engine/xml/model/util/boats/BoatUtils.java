package engine.xml.model.util.boats;

import engine.api.EngineContext;
import engine.api.EngineInterface;
import engine.database.collection.BoatsCollectionManager;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import jaxb.schema.engine.generated.boat.BoatType;

import java.util.List;

public class BoatUtils {
    private static EngineInterface engine;
    private static final BoatsCollectionManager boatsCollection;

    static {
        try {
            engine = EngineContext.getInstance();
        } catch (Exception ignored) {

        }
        boatsCollection = engine.getBoatsCollectionManager();
    }


    public static int importBoatsToCollection(jaxb.schema.engine.generated.boat.Boats boats, StringBuilder stringBuilder) {
        int counter = 0;

        for (jaxb.schema.engine.generated.boat.Boat boat : boats.getBoat()) {
            engine.model.boat.Boat convertedBoat = convertBoatSchemeObjectToBoat(boat, stringBuilder);
            if (convertedBoat != null) {
                boolean isAdded = boatsCollection.add(convertedBoat);

                if (!isAdded) {
                    stringBuilder.append(String.format(
                            "A Boat (name: %s | serial number: %s | short code: %s) already exists%n",
                            convertedBoat.getName(), convertedBoat.getSerialNumber(),
                            convertedBoat.getBoatType().getShortCode()));
                } else {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static jaxb.schema.engine.generated.boat.Boat convertBoatToBoatSchemeObject(engine.model.boat.Boat boat) {
        jaxb.schema.engine.generated.boat.Boat result = new jaxb.schema.engine.generated.boat.Boat();
        result.setCostal(boat.isSeaBoat());
        result.setHasCoxswain(boat.getBoatType().hasCoxwain());
        result.setId(boat.getSerialNumber());
        result.setName(boat.getName());
        result.setOutOfOrder(boat.isDisable());
        result.setPrivate(boat.hasOwner());
        result.setType(convertBoatTypeToXmlBoatType(boat.getBoatType()));
        result.setWide(boat.isWide());
        return result;
    }

    public static engine.model.boat.Boat convertBoatSchemeObjectToBoat(jaxb.schema.engine.generated.boat.Boat boat, StringBuilder stringBuilder) {

        if (isValidBoatScheme(boat, stringBuilder)) {
            String name = boat.getName();
            boolean isSeaBoat = boat.isCostal() != null && boat.isCostal();
            engine.model.boat.Boat.eBoatType type = convertXmlBoatTypeToBoatType(boat.getType());
            boolean isDisabled = boat.isOutOfOrder() != null && boat.isOutOfOrder();
            boolean isWide = boat.isWide() != null && boat.isWide();
            String serialNumber = boat.getId();
            Rower owner = null;

            if (boat.isPrivate() != null && boat.isPrivate()) {
                List<Rower> list = engine.getRowersCollectionManager()
                        .filter(rower -> rower.getPrivateBoatsSerialNumbers().contains(serialNumber));

                if(!list.isEmpty()){
                    owner = list.get(0);
                }
            }

            if (owner != null) {
                return new engine.model.boat.Boat(name, serialNumber, type, isWide, isSeaBoat, isDisabled, owner);
            } else {
                return new engine.model.boat.Boat(name, serialNumber, type, isWide, isSeaBoat, isDisabled);
            }
        }
        return null;
    }

    private static boolean isValidBoatScheme(jaxb.schema.engine.generated.boat.Boat boat, StringBuilder stringBuilder) {
        boolean result = true;
        StringBuilder errors = new StringBuilder();

        if (boat.getId().isEmpty()) {
            result = false;
            errors.append("'Boat id' can't be empty.\n");
        }

        if (boat.getName().isEmpty()) {
            result = false;
            errors.append("'Boat name' can't be empty.\n");
        }

        if (boat.getType() == null) {
            result = false;
            errors.append("'Boat type' must be provided.\n");
        } else if (!checkBoatTypeAndHasCoxswain(boat.getType(), boat.isHasCoxswain())) {
            result = false;
            errors.append("'Boat type' and 'hasCoxswain' doesn't match.\n");
        }

        if (errors.length() != 0) {
            stringBuilder.append(String.format("Boat (name: %S | serial : %s) isn't valid.%nerrors:%n%s",
                    boat.getName(), boat.getId(), errors.toString()));
        }

        return result;
    }

    private static boolean checkBoatTypeAndHasCoxswain(BoatType type, Boolean hasCoxswain) {
        engine.model.boat.Boat.eBoatType converted = convertXmlBoatTypeToBoatType(type);

        if (hasCoxswain != null) {
            return converted.hasCoxwain() == hasCoxswain;
        } else {
            return !converted.hasCoxwain();
        }
    }

    private static jaxb.schema.engine.generated.boat.BoatType convertBoatTypeToXmlBoatType(engine.model.boat.Boat.eBoatType boatType) {

        jaxb.schema.engine.generated.boat.BoatType res = null;

        switch (boatType) {
            case SINGLE:
                res = jaxb.schema.engine.generated.boat.BoatType.SINGLE;
                break;
            case DUE:
                res = jaxb.schema.engine.generated.boat.BoatType.DOUBLE;
                break;
            case DUE_WITH_COXWAIN:
                res = jaxb.schema.engine.generated.boat.BoatType.COXED_DOUBLE;
                break;
            case DUE_SINGLE_OAR:
                res = jaxb.schema.engine.generated.boat.BoatType.PAIR;
                break;
            case DUE_SINGLE_OAR_WITH_COXWAIN:
                res = jaxb.schema.engine.generated.boat.BoatType.COXED_PAIR;
                break;
            case FOUR:
                res = jaxb.schema.engine.generated.boat.BoatType.QUAD;
                break;
            case FOUR_WITH_COXWAIN:
                res = jaxb.schema.engine.generated.boat.BoatType.COXED_QUAD;
                break;
            case FOUR_SINGLE_OAR:
                res = jaxb.schema.engine.generated.boat.BoatType.FOUR;
                break;
            case FOUR_SINGLE_OAR_WITH_COXWAIN:
                res = jaxb.schema.engine.generated.boat.BoatType.COXED_FOUR;
                break;
            case EIGHT_SINGLE_OAR:
                res = jaxb.schema.engine.generated.boat.BoatType.EIGHT;
                break;
            case EIGHT:
                res = jaxb.schema.engine.generated.boat.BoatType.OCTUPLE;
                break;
        }

        return res;
    }


    private static engine.model.boat.Boat.eBoatType convertXmlBoatTypeToBoatType(jaxb.schema.engine.generated.boat.BoatType boatType) {
        Boat.eBoatType res = null;

        switch (boatType) {
            case SINGLE:
                res = Boat.eBoatType.SINGLE;
                break;
            case DOUBLE:
                res = Boat.eBoatType.DUE;
                break;
            case COXED_DOUBLE:
                res = Boat.eBoatType.DUE_WITH_COXWAIN;
                break;
            case PAIR:
                res = Boat.eBoatType.DUE_SINGLE_OAR;
                break;
            case COXED_PAIR:
                res = Boat.eBoatType.DUE_SINGLE_OAR_WITH_COXWAIN;
                break;
            case QUAD:
                res = Boat.eBoatType.FOUR;
                break;
            case COXED_QUAD:
                res = Boat.eBoatType.FOUR_WITH_COXWAIN;
                break;
            case FOUR:
                res = Boat.eBoatType.FOUR_SINGLE_OAR;
                break;
            case COXED_FOUR:
                res = Boat.eBoatType.FOUR_SINGLE_OAR_WITH_COXWAIN;
                break;
            case EIGHT:
                res = Boat.eBoatType.EIGHT_SINGLE_OAR;
                break;
            case OCTUPLE:
                res = Boat.eBoatType.EIGHT;
                break;
        }

        return res;
    }
}
