package server.servlet.json.template.model.boat;

import engine.model.Model;
import engine.model.boat.Boat;
import server.servlet.json.template.model.rower.RowerJson;

public class BoatJson {
    public final String serialNumber;
    public final boolean isWide;
    public final boolean isSeaBoat;
    public final String name;
    public final String description;
    public final int maxNumberOfRowers;
    public final String code;
    public final boolean isDisable;
    public final RowerJson owner;


    public BoatJson(Boat boat){
        this.serialNumber = boat.getSerialNumber();
        this.isDisable = boat.isDisable();
        this.isWide = boat.isWide();
        this.isSeaBoat = boat.isSeaBoat();
        this.name = boat.getName();
        this.description = boat.getBoatType().getTypeDescription();
        this.code = boat.getCode();
        this.owner = boat.getOwner() != null ? new RowerJson(boat.getOwner()) : null;
        this.maxNumberOfRowers = boat.getBoatType().getNumOfRowers();
    }
}
