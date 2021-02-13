package server.servlet.views.rowers;

import com.google.gson.Gson;
import engine.api.EngineContext;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
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

@WebServlet(urlPatterns = "/rowers/delete")
public class DeleteRowerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();

        try (PrintWriter out = resp.getWriter()) {

            HashMap<String, String> data = Utils.parsePostData(req);
            EngineContext eng = EngineContext.getInstance();
            Rower rowerToDelete =
                    EngineContext.getInstance().getRowersCollectionManager()
                            .findRowerBySerialNumber(data.get("serialNumber"));

            if (eng.getLoggedInUser(req.getRequestedSessionId())
                    .getSerialNumber().equals(data.get("serialNumber"))) {
                out.println(Utils.createJsonErrorObject("You cannot delete your own user"));
            } else if (rowerToDelete == null) {
                out.println(Utils.createJsonErrorObject("Rower not found"));
            } else {
                List<Boat> rowerBoats = eng.getBoatsCollectionManager().filter(boat ->
                        boat.hasOwner() && boat.getOwner().equals(rowerToDelete));
                String argsParam = data.get("args");
                Args deleteRowerArgs = gson.fromJson(argsParam, Args.class);
                if (deleteRowerArgs.shouldDeleteRower) {
                    for (Boat boat : rowerBoats) {
                        if (deleteRowerArgs.shouldDeleteBoats) {
                            eng.getBoatsCollectionManager().remove(boat);
                        } else {
                            eng.getBoatModifier(boat, null).removeOwner();
                        }
                    }
                    out.println(Utils.createJsonSuccessObject(eng.removeObject(rowerToDelete)));
                }
            }
        }
    }


    private static class Args {
        public boolean shouldDeleteBoats;
        public boolean shouldDeleteRower;
    }
}
