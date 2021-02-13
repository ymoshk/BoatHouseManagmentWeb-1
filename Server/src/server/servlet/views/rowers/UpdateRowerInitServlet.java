package server.servlet.views.rowers;

import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


@WebServlet(urlPatterns = "/rowers/update/init")
public class UpdateRowerInitServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            HashMap<String, String> data = Utils.parsePostData(req);

            if (data.isEmpty()) {
                out.println(Utils.createJsonErrorObject("Can't update the requested rower due to an unknown error"));
            } else {
                String serialNumber = data.get("serialNumber");

                if (serialNumber == null) {
                    out.println(Utils.createJsonErrorObject("Can't update the requested rower due to an unknown error"));
                }

                req.getSession().setAttribute("UpdateRowerSerial", serialNumber);
                out.println(Utils.createJsonSuccessObject(true));
            }
        }
    }


}

