package server.servlet.json.template.model.boat;

import engine.model.boat.Boat;

import java.util.ArrayList;
import java.util.List;

public class BoatsJson {
    public List<BoatJson> boats = new ArrayList<>();

    public BoatsJson(List<Boat> boats) {
        for (Boat boat : boats) {
            this.boats.add(new BoatJson(boat));
        }
    }
}
