package server.servlet.views.boats;

import engine.api.EngineContext;
import engine.model.boat.Boat;
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


@WebServlet(urlPatterns = "/boats/delete")
public class DeleteBoatServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        HashMap<String, String> reqArgs = Utils.parsePostData(req);
        String serialNumber = reqArgs.get("serialNumber");
        Boat boatToDelete = eng.getBoatsCollectionManager().findBySerialNumber(serialNumber);

        try (PrintWriter out = resp.getWriter()) {
            if (boatToDelete == null) {
                out.println(Utils.getErrorListJson(Collections.singletonList("Boat not found")));
            } else {
                List<Object> res = new ArrayList<>();
                res.add(eng.getBoatsCollectionManager().remove(boatToDelete));
                out.println(Utils.getSuccessJson(res));
            }
        }
    }
}
