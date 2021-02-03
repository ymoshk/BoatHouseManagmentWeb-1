package engine.xml.adapter;

import engine.model.boat.AdaptedBoat;
import engine.model.boat.Boat;
import engine.model.rower.Rower;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BoatAdapter extends XmlAdapter<AdaptedBoat, Boat> {

    @Override
    public Boat unmarshal(AdaptedBoat adaptedBoat) {
        String name = adaptedBoat.getName();
        String serialNumber = adaptedBoat.getSerialNumber();
        Boat.eBoatType boatType = adaptedBoat.getBoatType();
        boolean isWide = adaptedBoat.isWide();
        boolean isSeaBoat = adaptedBoat.isSeaBoat();
        boolean isDisable = adaptedBoat.isDisable();
        Rower owner = adaptedBoat.getOwner();

        if (owner != null) {
            return new Boat(name, serialNumber, boatType, isWide, isSeaBoat, isDisable, owner);
        } else {
            return new Boat(name, serialNumber, boatType, isWide, isSeaBoat, isDisable);
        }
    }

    @Override
    public AdaptedBoat marshal(Boat boat) {
        AdaptedBoat adaptedBoat = new AdaptedBoat();
        adaptedBoat.setBoatType(boat.getBoatType());
        adaptedBoat.setDisable(boat.isDisable());
        adaptedBoat.setName(boat.getName());
        adaptedBoat.setOwner(boat.getOwner());
        adaptedBoat.setSeaBoat(boat.isSeaBoat());
        adaptedBoat.setSerialNumber(boat.getSerialNumber());
        adaptedBoat.setWide(boat.isWide());
        return adaptedBoat;
    }
}
