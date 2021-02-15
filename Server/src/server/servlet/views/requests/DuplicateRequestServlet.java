package server.servlet.views.requests;

import engine.api.EngineContext;
import engine.model.activity.request.Request;
import engine.model.activity.request.RequestModifier;
import server.servlet.json.template.model.request.RequestJson;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet(urlPatterns = "/requests/duplicate")
public class DuplicateRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        HashMap<String, String> reqArgs = Utils.parsePostData(req);
        String requestToDuplicateId = reqArgs.get("reqId");
        Request requestToDuplicate = eng.getRequestsCollectionManager()
                .filter(request -> request.getId().equals(requestToDuplicateId)).get(0);

        try (PrintWriter out = resp.getWriter()) {
            if (requestToDuplicate != null) {
                Request duplicated = requestToDuplicate.clone();
                RequestModifier requestModifier = eng.getRequestModifier(duplicated, null);
                requestModifier.setApprovementStatus(false);
                if (eng.getRequestsCollectionManager().add(duplicated)) {
                    out.println(Utils.createJsonSuccessObject(new RequestJson(duplicated)));
                } else {
                    out.println(Utils.createJsonErrorObject(null));
                }
            } else {
                out.println(Utils.createJsonErrorObject(null));
            }
        }
    }
}
