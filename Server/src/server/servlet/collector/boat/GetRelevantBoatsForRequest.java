package server.servlet.collector.boat;

import engine.api.EngineContext;
import engine.database.collection.RowingActivitiesCollectionManager;
import engine.model.activity.request.Request;
import engine.model.boat.Boat;
import server.servlet.json.template.model.boat.BoatJson;
import server.servlet.json.template.model.boat.BoatsJson;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(urlPatterns = "collectors/boats/getRelevantBoatsForRequest")
public class GetRelevantBoatsForRequest extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        HashMap<String, String> reqArgs = Utils.parsePostData(req);
        String reqId = reqArgs.get("id");
        Request requestToApprove = eng.getRequestsCollectionManager()
                .filter(request -> request.getId().equals(reqId)).get(0);

        List<Boat> relevantBoats = getRelevantBoat(requestToApprove, requestToApprove.getBoatTypesList());
        BoatsJson result = new BoatsJson(relevantBoats);

        try (PrintWriter out = resp.getWriter()) {
            out.println(Utils.createJsonSuccessObject(result));
        }
    }

    private List<Boat> getRelevantBoat(Request requestToApprove, List<Boat.eBoatType> boatTypeList) {
        RowingActivitiesCollectionManager rowingActivityCollectionManager =
                EngineContext.getInstance().getRowingActivitiesCollectionManager();

        return EngineContext.getInstance().getBoatsCollectionManager().filter(boat ->
                !boat.isDisable() &&
                        !boat.isPrivate() &&
                        boatTypeList.contains(boat.getBoatType()) &&
                        rowingActivityCollectionManager.isBoatAvailable(boat, requestToApprove));
    }

}