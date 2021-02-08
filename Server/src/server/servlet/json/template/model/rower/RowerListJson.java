package server.servlet.json.template.model.rower;

import engine.model.rower.Rower;

import java.util.ArrayList;
import java.util.List;

public class RowerListJson {
    public List<RowerJson> rowers = new ArrayList<>();

    public RowerListJson(List<Rower> rowers) {
        for (Rower rower : rowers) {
            this.rowers.add(new RowerJson(rower));
        }
    }
}
