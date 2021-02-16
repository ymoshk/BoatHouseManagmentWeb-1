package server.servlet.collector.request;

import com.google.gson.Gson;
import engine.api.EngineContext;
import engine.model.activity.request.Request;
import engine.model.rower.Rower;
import server.servlet.json.template.model.request.RequestsJson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/collectors/requests")
public class RequestsCollector extends HttpServlet {


    //return only the relevant requests
    // if the user is not an admin he will see only his requests
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        Rower user = eng.getRowerBySessionId(req.getRequestedSessionId());
        List<Request> requests = eng.getRequestsCollectionManager().getRelevantRequests(user);

        try (PrintWriter out = resp.getWriter()) {
            out.print(new Gson().toJson(new RequestsJson(requests)));
        }
    }
}
