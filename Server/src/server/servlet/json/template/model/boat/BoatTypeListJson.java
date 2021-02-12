package server.servlet.json.template.model.boat;

import java.util.List;

public class BoatTypeListJson {

    public List<BoatTypeJson> boats;

    public BoatTypeListJson(List<BoatTypeJson> boats) {
        this.boats = boats;
    }
}
