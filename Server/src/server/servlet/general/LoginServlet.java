package server.servlet.general;


import engine.api.EngineContext;
import javafx.util.Pair;
import server.constant.Constants;
import server.utils.Utils;

import javax.rmi.CORBA.Util;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       HttpSession session = req.getSession();

       if(Utils.isSessionInvalid(session)) {
           RequestDispatcher view = req.getRequestDispatcher("/public/html/login.html");
           view.forward(req, resp);
       }
       else {
           resp.sendRedirect("/home");
       }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try (PrintWriter out = resp.getWriter()) {

            if (email == null || password == null) {
                out.println(Utils.standardJsonResponse(false, "Email and password can't be empty"));
            } else {
                EngineContext eng = (EngineContext) req.getServletContext().getAttribute(Constants.engineAtt);
                Pair<Boolean, String> res = eng.verifyLoginDetails(email, password);

                if (!res.getKey()) {
                    out.println(Utils.standardJsonResponse(false, res.getValue()));
                } else {
                    eng.login(email, password, req.getSession().getId());
                    Utils.updateSession(req.getSession());
                    out.println(Utils.standardJsonResponse(true));
                }
            }
        }
    }
}
