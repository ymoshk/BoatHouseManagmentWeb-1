package server.servlet.views.requests;

import engine.api.EngineContext;
import engine.model.activity.request.Request;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet(urlPatterns = "/requests/delete")
public class DeleteRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        HashMap<String, String> reqArgs = Utils.parsePostData(req);
        String requestToDuplicateId = reqArgs.get("reqId");
        Request reqToDelete = eng.getRequestsCollectionManager()
                .filter(request -> request.getId().equals(requestToDuplicateId)).get(0);

        try (PrintWriter out = resp.getWriter()) {
            if (eng.getRequestsCollectionManager().remove(reqToDelete)) {
                out.println(Utils.createJsonSuccessObject(true));
            } else {
                out.println(Utils.createJsonErrorObject(null));
            }
        }
    }
}
