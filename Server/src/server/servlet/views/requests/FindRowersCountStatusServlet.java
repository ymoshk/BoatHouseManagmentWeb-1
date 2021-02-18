package server.servlet.views.requests;

import engine.api.EngineContext;
import engine.model.activity.request.Request;
import engine.model.boat.Boat;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet(urlPatterns = "/requests/find-rowers-status")
public class FindRowersCountStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        HashMap<String, String> reqArgs = Utils.parsePostData(req);
        String boatId = reqArgs.get("boatId");
        String reqId = reqArgs.get("id");

        try (PrintWriter out = resp.getWriter()) {
            Request requestToCheck = eng.getRequestsCollectionManager()
                    .filter(request -> request.getId().equals(reqId)).get(0);

            Boat.eBoatType boatType = eng.getBoatsCollectionManager()
                    .filter(boat -> boat.getSerialNumber().equals(boatId)).get(0).getBoatType();

            if (requestToCheck != null && boatType != null) {
                int result = 0;

                if (requestToCheck.getTotalCountOfRowers() > boatType.getNumOfRowers()) {
                    result = 1;
                } else if (requestToCheck.getTotalCountOfRowers() < boatType.getNumOfRowers()) {
                    result = -1;
                }
                out.println(Utils.createJsonSuccessObject(result));

            } else {
                out.println(Utils.createJsonErrorObject("Unknown error occurred during approving the request."));
            }
        }
    }

}
