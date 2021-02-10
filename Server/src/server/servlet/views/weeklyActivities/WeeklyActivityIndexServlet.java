package server.servlet.views.weeklyActivities;

import com.sun.org.apache.xerces.internal.util.HTTPInputSource;
import server.constant.ePages;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/weekly-activities/index")
public class WeeklyActivityIndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utils.renderLayout(req,resp, "/public/html/views/weeklyActivities/index.html", ePages.WEEKLY_ACTIVITIES);
    }
}
