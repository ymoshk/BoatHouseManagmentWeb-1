package server.servlet.views.boats;

import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.util.HTTPInputSource;
import engine.api.EngineContext;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import server.servlet.json.template.model.boat.BoatJson;
import server.servlet.json.template.model.boat.BoatsJson;
import server.servlet.json.template.model.rower.RowerListJson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/boats/index/getBoats")
public class GetBoatsCollectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
//        List<Boat> boats = eng.getBoatsCollectionManager().toArrayList();
        Boat b = new Boat("boat", "boatSerial", Boat.eBoatType.DUE_SINGLE_OAR_WITH_COXWAIN, true, true, false);
        List<Boat> boats = new ArrayList<>();
        boats.add(b);

        try(PrintWriter out = resp.getWriter()){
            out.print(new Gson().toJson(new BoatsJson(boats)));
        }
    }
}
