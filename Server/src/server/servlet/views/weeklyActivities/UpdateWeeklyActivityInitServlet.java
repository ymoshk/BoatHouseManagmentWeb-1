package server.servlet.views.weeklyActivities;

import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


@WebServlet(urlPatterns = "/weekly-activities/update/init")
public class UpdateWeeklyActivityInitServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msg = "Can't update the requested weekly activity due to an unknown error";
        try (PrintWriter out = resp.getWriter()) {
            HashMap<String, String> data = Utils.parsePostData(req);

            if (data.isEmpty()) {
                out.println(Utils.createJsonErrorObject(msg));
            } else {
                String id = data.get("id");

                if (id == null) {
                    out.println(Utils.createJsonErrorObject(msg));
                }

                req.getSession().setAttribute("UpdateWeeklyActivityId", id);
                out.println(Utils.createJsonSuccessObject(true));
            }
        }
    }
}

