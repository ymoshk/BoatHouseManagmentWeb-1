package servlet;

import constant.Constants;
import engine.api.EngineContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "index", urlPatterns = "/")
public class IndexServlet extends HttpServlet {

    //TODO check if the user is already logged in

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //EngineContext engine = (EngineContext) req.getServletContext().getAttribute(Constants.engineAtt);

        try (PrintWriter out = resp.getWriter()) {
            out.println("engine.getRowersCollectionManager().get(0).getName()");
        }
    }
}
