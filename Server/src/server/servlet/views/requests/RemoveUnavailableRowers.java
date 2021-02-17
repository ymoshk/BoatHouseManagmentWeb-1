package server.servlet.views.requests;

import engine.api.EngineContext;
import engine.model.activity.request.Request;
import engine.model.activity.request.RequestModifier;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet(urlPatterns = "/requests/removeUnavailableRowers")
public class RemoveUnavailableRowers extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        HashMap<String, String> reqArgs = Utils.parsePostData(req);
        String reqId = reqArgs.get("id");
        Request requestToApprove = eng.getRequestsCollectionManager()
                .filter(request -> request.getId().equals(reqId)).get(0);
        StringBuilder errors = new StringBuilder();

        try(PrintWriter out = resp.getWriter()){
            RequestModifier requestModifier = eng.getRequestModifier(requestToApprove, errors::append);

            if(!requestModifier.removeUnavailableRowers()){
                out.println(Utils.createJsonErrorObject(errors.toString()));
            }
            else {
                out.println(Utils.createJsonSuccessObject(true));
            }
        }
    }
}
