package server.servlet.general;

import engine.api.EngineContext;
import server.BHMServer;
import server.constant.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@WebServlet(name = "logout", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, LocalDateTime> sessionExpMap = (Map<String, LocalDateTime>) req.getServletContext().getAttribute(Constants.sessionExpMap);
        sessionExpMap.remove(req.getRequestedSessionId());
        EngineContext.getInstance().logout(req.getRequestedSessionId());
        req.getSession().invalidate();
        resp.sendRedirect("/login");

    }
}
