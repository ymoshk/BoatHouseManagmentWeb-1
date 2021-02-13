package server.servlet.json.template.model.boat;

import java.util.List;

public class BoatTypeListJson {

    public List<BoatTypeJson> types;

    public BoatTypeListJson(List<BoatTypeJson> types) {
        this.types = types;
    }
}
