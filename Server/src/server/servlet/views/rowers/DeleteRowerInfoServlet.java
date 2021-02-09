package server.servlet.views.rowers;


import com.google.gson.Gson;
import com.sun.deploy.ref.Helpers;
import com.sun.xml.internal.ws.api.pipe.Engine;
import engine.api.EngineContext;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import server.servlet.json.template.Response;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(urlPatterns = "/rowers/delete/info")
public class DeleteRowerInfoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        HashMap<String, String> data = Utils.parsePostData(req);
        String serialNumber  = data.get("serialNumber");
        Rower rowerToDelete = eng.getRowersCollectionManager().findRowerBySerialNumber(serialNumber);

        try (PrintWriter out = resp.getWriter()) {
            if (rowerToDelete == null) {
                out.println(Utils.getErrorListJson(Collections.singletonList("Rower not found")));
                return;
            } else {
                List<Object> res = new ArrayList<>();
                res.add(rowerToDelete.hasPrivateBoat());
                res.add(!eng.canRowerBeRemoved(rowerToDelete)); //false if rower take part in activities
                out.println(Utils.getSuccessJson(res));
            }
        }
    }
}
