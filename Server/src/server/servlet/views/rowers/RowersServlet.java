package server.servlet.views.rowers;

import server.constant.ePages;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "rowers", urlPatterns = "/rowers/index")
public class RowersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utils.renderLayout(req,resp, "/public/html/views/rowers/index.html", ePages.ROWERS);
    }
}
