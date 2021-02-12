package server.servlet.json.template.model.boat;

public class BoatTypeJson {

    public String name;
    public int index;
    public boolean select;

    public BoatTypeJson(String name, int index, boolean select) {
        this.name = name;
        this.index = index;
        this.select = select;
    }
}
