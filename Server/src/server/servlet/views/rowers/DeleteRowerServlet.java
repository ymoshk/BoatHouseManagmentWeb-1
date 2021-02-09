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
import java.util.Collections;
import java.util.HashMap;

@WebServlet(urlPatterns = "/rowers/delete")
public class DeleteRowerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();

        try (PrintWriter out = resp.getWriter()) {

            HashMap<String, String> data = Utils.parsePostData(req);
            if (data == null) {
                out.println(Utils.getErrorJson(Collections.singletonList("Delete failed due to unknown error.")));
            } else {
                EngineContext eng = EngineContext.getInstance();
                Rower rowerToDelete =
                        EngineContext.getInstance().getRowersCollectionManager()
                                .findRowerBySerialNumber(data.get("serialNumber"));

                if (eng.getLoggedInUser(req.getRequestedSessionId())
                        .getSerialNumber().equals(data.get("serialNumber"))) {
                    out.println(Utils.getErrorJson(Collections.singletonList("You cannot delete your own user")));
                } else if (rowerToDelete == null) {
                    out.println(Utils.getErrorJson(Collections.singletonList("Rower not found")));
                } else {
                    String argsParam = data.get("args");
                    Args deleteRowerArgs = gson.fromJson(argsParam, Args.class);
                    if (deleteRowerArgs.shouldDeleteRower) {
                        if (deleteRowerArgs.shouldDeleteBoats) {
                            for (String serial : rowerToDelete.getPrivateBoatsSerialNumbers()) {
                                Boat boatToDelete = eng.getBoatsCollectionManager().findBySerialNumber(serial);
                                eng.removeObject(boatToDelete);
                            }
                        }
                        out.println(Utils.getSuccessJson(eng.removeObject(rowerToDelete)));
                    }
                }
            }
        }
    }


    private static class Args {
        public boolean shouldDeleteBoats;
        public boolean shouldDeleteRower;
    }
}
