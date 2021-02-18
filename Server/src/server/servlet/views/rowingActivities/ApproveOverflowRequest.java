package server.servlet.views.rowingActivities;

import com.google.gson.Gson;
import engine.api.EngineContext;
import engine.model.activity.request.Request;
import engine.model.activity.request.RequestModifier;
import engine.model.activity.rowing.RowingActivity;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import server.servlet.json.template.model.rower.RowerJson;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/rowing-activities/approveOverflowRequest")
public class ApproveOverflowRequest extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        Gson gson = new Gson();
        HashMap<String, String> reqArgs = Utils.parsePostData(req);
        String requestToApproveId = reqArgs.get("reqId");
        String boatSerialNumber = reqArgs.get("boatSerialNumber");
        try (PrintWriter out = resp.getWriter()) {
            try {
                Request requestToApprove = eng.getRequestsCollectionManager()
                        .filter(request -> request.getId().equals(requestToApproveId)).get(0);
                Boat theBoat = eng.getBoatsCollectionManager()
                        .filter(boat -> boat.getSerialNumber().equals(boatSerialNumber)).get(0);

                List<Rower> deletedRowers = Arrays.stream(gson.fromJson(reqArgs.get("deletedRowers"), String[].class))
                        .map(id -> eng.getRowersCollectionManager().findRowerBySerialNumber(id))
                        .collect(Collectors.toList());

                prepareRequestToApprove(requestToApprove, deletedRowers);
                Request ClonedRequest = requestToApprove.clone(deletedRowers);

                RowingActivity rowingActivity = new RowingActivity(theBoat, requestToApprove);

                List<Boolean> res = new ArrayList<>();
                res.add(eng.getRowingActivitiesCollectionManager().add(rowingActivity));
                res.add(eng.getRequestsCollectionManager().add(ClonedRequest));

                out.println(Utils.createJsonSuccessObject(res));
            } catch (Exception e) {
                out.println(Utils.createJsonErrorObject(null));
            }
        }
    }

    private void prepareRequestToApprove(Request requestToApprove, List<Rower> deletedRowers) {
        RequestModifier requestModifier = EngineContext.getInstance()
                .getRequestModifier(requestToApprove, null);

        deletedRowers.forEach(rowerToDelete -> {
            if (rowerToDelete.equals(requestToApprove.getMainRower())) {
                requestModifier.removeMainRower();
            } else {
                requestModifier.removeRowerFromOtherRowersList(rowerToDelete);
            }
        });
    }
}
