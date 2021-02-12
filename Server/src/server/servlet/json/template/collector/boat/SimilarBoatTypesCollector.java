package server.servlet.json.template.collector.boat;

import engine.api.EngineContext;
import engine.model.boat.Boat;
import javafx.util.Pair;
import server.servlet.json.template.model.boat.BoatTypeJson;
import server.servlet.json.template.model.boat.BoatTypeListJson;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@WebServlet("/collectors/SimilarBoatTypes")
public class SimilarBoatTypesCollector extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        HashMap<String, String> data = Utils.parsePostData(req);
        List<Pair<Boat.eBoatType, Integer>> types;

        try (PrintWriter out = resp.getWriter()) {

            String serial = data.get("serialNumber");
            BoatTypeListJson result;

            if (serial == null || serial.isEmpty()) {
                types = Boat.eBoatType.toList();
                result = prepareResult(types);
            } else {
                Boat boat = eng.getBoatsCollectionManager().findBySerialNumber(serial);
                types = Boat.eBoatType.toList(boat.getBoatType());

                result = prepareResult(types);

                for (BoatTypeJson type : result.boats) {
                    if (type.name.equals(boat.getBoatType().name())) {
                        type.select = true;
                        break;
                    }
                }
            }

            out.println(Utils.getSuccessJson(Collections.singletonList(result)));
        }

    }

    private BoatTypeListJson prepareResult(List<Pair<Boat.eBoatType, Integer>> types) {
        List<BoatTypeJson> list = new ArrayList<>();
        types.forEach(pair -> list.add(new BoatTypeJson(pair.getKey().name(), pair.getValue(), false)));
        return new BoatTypeListJson(list);
    }
}
