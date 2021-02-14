package server.servlet.json.template.model.boat;

import engine.model.boat.Boat;

public class BoatTypeJson {

    public String name;
    public int index;
    public boolean select;
    public int numOfRowers;

    public BoatTypeJson(String name, int index, boolean select, int numOfRowers) {
        this.name = name;
        this.index = index;
        this.select = select;
        this.numOfRowers = numOfRowers;
    }
}
