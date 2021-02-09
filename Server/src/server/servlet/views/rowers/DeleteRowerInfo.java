package server.servlet.views.rowers;


import engine.api.EngineContext;
import engine.model.rower.Rower;
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

@WebServlet(urlPatterns = "/rowers/delete/info")
public class DeleteRowerInfo extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {
            HashMap<String, String> data = Utils.parsePostData(req);
            if (data == null) {
                out.println(Utils.getErrorJson(Collections.singletonList("Delete failed due to unknown error.")));
            } else {

                EngineContext eng = EngineContext.getInstance();
                Rower rowerToDelete = eng.getRowersCollectionManager()
                        .findRowerBySerialNumber(data.get("serialNumber"));

                if (rowerToDelete == null) {
                    out.println(Utils.getErrorJson(Collections.singletonList("Rower not found")));
                } else {
                    List<Object> res = new ArrayList<>();
                    res.add(rowerToDelete.hasPrivateBoat());
                    res.add(!eng.canRowerBeRemoved(rowerToDelete)); //false if rower take part in activities
                    out.println(Utils.getSuccessJson(res));
                }
            }
        }
    }
}
