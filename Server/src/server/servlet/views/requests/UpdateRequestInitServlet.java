package server.servlet.views.requests;

import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


@WebServlet(urlPatterns = "/requests/update/init")
public class UpdateRequestInitServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            HashMap<String, String> data = Utils.parsePostData(req);

            if (data.isEmpty()) {
                out.println(Utils.createJsonErrorObject("Can't update the request due to an unknown error"));
            } else {
                String id = data.get("id");

                if (id == null) {
                    out.println(Utils.createJsonErrorObject("Can't update the request due to an unknown error"));
                }

                req.getSession().setAttribute("UpdateRequestId", id);
                out.println(Utils.createJsonSuccessObject(true));
            }
        }
    }


}

