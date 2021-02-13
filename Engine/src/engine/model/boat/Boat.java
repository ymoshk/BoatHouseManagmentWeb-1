package engine.model.boat;


import engine.model.Model;
import engine.model.rower.Rower;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@XmlRootElement(name = "Boat")
@XmlAccessorType(XmlAccessType.FIELD)
public class Boat extends Model implements Serializable {

    private final String serialNumber;
    private final boolean isWide;
    private final boolean isSeaBoat;
    private String name;
    private eBoatType boatType;
    private boolean isDisable;
    private Rower owner;


    public Boat(String name, String serialNumber, eBoatType boatType, boolean isWide, boolean isSeaBoat,
                boolean isDisable) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.boatType = boatType;
        this.isWide = isWide;
        this.isSeaBoat = isSeaBoat;
        this.isDisable = isDisable;
    }

    public Boat(String name, String serialNumber, eBoatType boatType, boolean isWide, boolean isSeaBoat,
                boolean isDisable, Rower owner) {
        this(name, serialNumber, boatType, isWide, isSeaBoat, isDisable);

        if (owner != null) {
            setOwner(owner);
        } else {
            throw new IllegalArgumentException("Boat owner can't be null");
        }
    }

    public Boat() {
        this.isWide = false;
        this.serialNumber = null;
        this.isSeaBoat = false;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }


    public String getName() {
        return this.name;
    }

    void setName(String name) {
        this.name = name;
    }

    public eBoatType getBoatType() {
        return this.boatType;
    }

    void setBoatType(eBoatType newType) {
        this.boatType = newType;
    }

    public boolean isPrivate() {
        return this.owner != null;
    }

    public boolean isWide() {
        return this.isWide;
    }

    public boolean isSeaBoat() {
        return this.isSeaBoat;
    }

    public boolean isDisable() {
        return this.isDisable;
    }

    void setState(boolean newState) {
        this.isDisable = newState;
    }

    public String getCode() {
        return this.boatType.shortCode + (this.isWide ? " wide" : "") + (this.isSeaBoat ? " costal" : "");
    }

    public boolean hasOwner() {
        return this.owner != null;
    }

    public Rower getOwner() {
        return owner;
    }

    void setOwner(Rower newOwner) {
        if (newOwner != null) {
            removeOwner();
            this.owner = newOwner;
            newOwner.addPrivateBoat(this);
        }
    }

    void shallowSetOwner(Rower newOwner) {
        this.owner = newOwner;
    }

    void removeOwner() {
        if (this.owner != null) {
            this.owner.removePrivateBoat(this);
            this.owner = null;
        }
    }

    public Boat cloneWithOutOwner() {
        return new Boat(this.name, this.serialNumber, this.boatType,
                this.isWide, this.isSeaBoat, this.isDisable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Boat boat = (Boat) o;
        return serialNumber.equals(boat.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), serialNumber);
    }

    public enum eBoatType {

        SINGLE(true, (short) 1, "1X", "Single", false),
        DUE(false, (short) 2, "2X", "Due", false),
        DUE_WITH_COXWAIN(false, (short) 2, "2X+", "Due with coxwain", true),
        DUE_SINGLE_OAR(true, (short) 2, "2-", "Due single oar", false),
        DUE_SINGLE_OAR_WITH_COXWAIN(true, (short) 2, "2+", "Due single oar with coxwain", true),
        FOUR(false, (short) 4, "4X", "Four", false),
        FOUR_WITH_COXWAIN(false, (short) 4, "4X+", "Four with coxwain", true),
        FOUR_SINGLE_OAR(true, (short) 4, "4-", "Four single oar", false),
        FOUR_SINGLE_OAR_WITH_COXWAIN(true, (short) 4, "4+", "Four single oar with coxwain", true),
        EIGHT(false, (short) 8, "8X+", "Eight", true),
        EIGHT_SINGLE_OAR(true, (short) 8, "8+", "Eight single oar", true);

        private final boolean hasCoxwain;
        private final boolean isSingleOar;
        private final int numOfRowers;
        private final String shortCode;
        private final String description;

        eBoatType(boolean isSingleOar, short numOfRowers, String shortCode, String description, boolean hasCoxwain) {
            this.isSingleOar = isSingleOar;
            this.numOfRowers = numOfRowers;
            this.shortCode = shortCode;
            this.hasCoxwain = hasCoxwain;
            this.description = description;
        }

        public static List<eBoatType> getSimilarTypes(eBoatType boatType) {
            List<eBoatType> result = new ArrayList<>();

            for (eBoatType type : eBoatType.values()) {
                if (boatType.numOfRowers == type.numOfRowers) {
                    result.add(type);
                }
            }

            return Collections.unmodifiableList(result);
        }

        public static eBoatType getTypeFromInt(int boatTypeAsInt) {
            if (boatTypeAsInt <= 0) {
                return null;
            } else {
                return eBoatType.values()[boatTypeAsInt - 1];
            }
        }

        public static int getIntFromBoatType(eBoatType boatType) {
            int i = 0;

            for (eBoatType type : eBoatType.values()) {
                if (type == boatType) {
                    return i;
                }

                i++;
            }

            return -1;
        }

        public int getNumOfRowers() {
            return numOfRowers + (this.hasCoxwain ? 1 : 0);
        }

        public String getTypeDescription() {
            return this.description;
        }

        public String getShortCode() {
            return this.shortCode;
        }

        public boolean hasCoxwain() {
            return this.hasCoxwain;
        }

        public boolean isSingleOar() {
            return this.isSingleOar;
        }
    }
}
