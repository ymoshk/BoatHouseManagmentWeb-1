package server.servlet.views.rowers;


import com.google.gson.Gson;
import com.sun.deploy.ref.Helpers;
import com.sun.xml.internal.ws.api.pipe.Engine;
import engine.api.EngineContext;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(urlPatterns = "/rowers/delete")
public class DeleteRowerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String serialNumber = (String) req.getAttribute("serialNumber");
        Rower rowerToDelete = EngineContext.getInstance()
                .getRowersCollectionManager().findRowerBySerialNumber(serialNumber);
        EngineContext eng = EngineContext.getInstance();
        Gson gson = new Gson();

        try (PrintWriter out = resp.getWriter()) {
            if (rowerToDelete == null) {
                out.println(gson.toJson
                        (new Response(false, Collections.singletonList("Rower not found"))));
                return;
            } else {
                List<Object> res = new ArrayList<>();
                res.add(rowerToDelete.hasPrivateBoat());
                res.add(eng.canRowerBeRemoved(rowerToDelete)); //true if rower take part in activities
                out.println(gson.toJson(new Response(true, res)));
            }
        }
    }
}
