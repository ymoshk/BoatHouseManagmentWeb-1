package server.servlet.views.rowers;

import engine.api.EngineContext;
import engine.model.rower.Rower;
import engine.model.rower.RowerModifier;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet(urlPatterns = "/rowers/update/password")
public class UpdateRowerPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {

            HashMap<String, String> data = Utils.parsePostData(req);
            EngineContext eng = EngineContext.getInstance();
            Rower rowerToUpdate =
                    EngineContext.getInstance().getRowersCollectionManager()
                            .findRowerBySerialNumber(data.get("serialNumber"));

            if (rowerToUpdate == null) {
                out.println(Utils.createJsonErrorObject("Changing password failed due to unknown error."));
            } else {
                RowerModifier modifier = eng.getRowerModifier(rowerToUpdate, null);
                modifier.setRowerPassword(data.get("password"));
                out.println(Utils.createJsonSuccessObject(true));
            }
        }
    }
}

