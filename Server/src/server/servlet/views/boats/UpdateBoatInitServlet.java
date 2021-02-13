package server.servlet.views.boats;

import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;


@WebServlet(urlPatterns = "/boats/update/init")
public class UpdateBoatInitServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            HashMap<String, String> data = Utils.parsePostData(req);

            if (data.isEmpty()) {
                out.println(Utils.getErrorJson(
                        Collections.singletonList("Can't update the requested boat due to an unknown error")));
            } else {
                String serialNumber = data.get("serialNumber");

                if (serialNumber == null) {
                    out.println(Utils.getErrorJson(
                            Collections.singletonList("Can't update the requested boat due to an unknown error")));
                }

                req.getSession().setAttribute("UpdateBoatSerial", serialNumber);
                out.println(Utils.getSuccessJson(true));
            }
        }
    }
}

