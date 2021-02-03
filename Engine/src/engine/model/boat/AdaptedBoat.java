package engine.model.boat;

import engine.model.Model;
import engine.model.rower.Rower;

public class AdaptedBoat extends Model {
    private String serialNumber;
    private String name;
    private Boat.eBoatType boatType;
    private boolean isWide;
    private boolean isSeaBoat;
    private boolean isDisable;
    private Rower owner;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boat.eBoatType getBoatType() {
        return boatType;
    }

    public void setBoatType(Boat.eBoatType boatType) {
        this.boatType = boatType;
    }

    public boolean isWide() {
        return isWide;
    }

    public void setWide(boolean wide) {
        isWide = wide;
    }

    public boolean isSeaBoat() {
        return isSeaBoat;
    }

    public void setSeaBoat(boolean seaBoat) {
        isSeaBoat = seaBoat;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    public Rower getOwner() {
        return owner;
    }

    public void setOwner(Rower owner) {
        this.owner = owner;
    }
}

