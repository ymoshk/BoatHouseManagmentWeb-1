package server.servlet.collector.rower;

import engine.api.EngineContext;
import engine.model.rower.Rower;
import server.servlet.json.template.model.rower.RowerJson;
import server.utils.Utils;

import javax.rmi.CORBA.Util;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/collectors/loggedInUser")
public class GetLoggedInUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        Rower loggedInUser = eng.getLoggedInUser(req.getRequestedSessionId());

        try (PrintWriter out = resp.getWriter()) {
            if (loggedInUser == null) {
                out.println(Utils.createJsonErrorObject(null));
            } else {
                out.println(Utils.createJsonSuccessObject(new RowerJson(loggedInUser)));
            }
        }
    }
}
