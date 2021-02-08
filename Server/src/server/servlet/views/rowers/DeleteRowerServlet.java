package server.servlet.views.rowers;

import com.google.gson.Gson;
import engine.api.EngineContext;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import server.servlet.json.template.Response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

@WebServlet(name = "delete rower", urlPatterns = "/rowers/delete")
public class DeleteRowerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        String rowerSerialNumber = req.getParameter("serialNumber");
        Gson gson = new Gson();
        Rower rowerToDelete =
                EngineContext.getInstance().getRowersCollectionManager().findRowerBySerialNumber(rowerSerialNumber);

        try (PrintWriter out = resp.getWriter()) {
            if (eng.getLoggedInUser(req.getRequestedSessionId()).getSerialNumber().equals(rowerSerialNumber)) {
                out.println(gson.toJson
                        (new Response(false, Collections.singletonList("You cannot delete your own user"))));
            } else if (rowerToDelete == null) {
                out.println(gson.toJson
                        (new Response(false, Collections.singletonList("Rower not found"))));
            } else {
                Args deleteRowerArgs = gson.fromJson(req.getParameter("args"), Args.class);
                if (deleteRowerArgs.shouldDeleteRower) {
                    if (deleteRowerArgs.shouldDeleteBoats) {
                        for (String serial : rowerToDelete.getPrivateBoatsSerialNumbers()) {
                            Boat boatToDelete = eng.getBoatsCollectionManager().findBySerialNumber(serial);
                            eng.removeObject(boatToDelete);
                        }
                    }

                    out.println(gson.toJson(new Response(eng.removeObject(rowerToDelete))));
                }
            }
        }
    }


    private class Args {
        public boolean shouldDeleteBoats;
        public boolean shouldDeleteRower;
    }
}
