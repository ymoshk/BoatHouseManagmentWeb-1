package server.servlet.collector.boat;

import engine.api.EngineContext;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import server.servlet.json.template.model.boat.BoatsJson;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

@WebServlet("/collectors/publicBoats")
public class PublicBoatsCollector extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        HashMap<String, String> data = Utils.parsePostData(req);
        List<Boat> boats;

        try (PrintWriter out = resp.getWriter()) {
            String serial = data.get("serialNumber");

            if (serial == null || serial.isEmpty()) {
                boats = eng.getBoatsCollectionManager()
                        .filter(boat -> !boat.hasOwner());
            } else {
                Rower owner = eng.getRowersCollectionManager().findRowerBySerialNumber(serial);
                boats = eng.getBoatsCollectionManager()
                        .filter(boat -> boat.hasOwner() && !boat.getOwner().equals(owner));
            }

            out.println(Utils.createJsonSuccessObject(new BoatsJson(boats)));
        }

    }
}
