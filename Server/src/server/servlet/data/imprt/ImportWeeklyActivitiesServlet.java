package server.servlet.data.imprt;

import engine.api.EngineContext;
import javafx.util.Pair;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

@WebServlet(urlPatterns = "/data/import/weekly-activities")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5)
public class ImportWeeklyActivitiesServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Pair<Boolean, String> data = Utils.parsePartsRequest(req);

        try (PrintWriter out = resp.getWriter()) {
            if (data.getKey() == null || data.getValue().isEmpty()) {
                out.println(Utils.createJsonErrorObject("Importing weekly activities failed due to an unknown error."));
            } else {
                StringBuilder result = new StringBuilder();
                try {
                    EngineContext.getInstance().getWeeklyActivitiesCollectionManager()
                            .importFromXml(data.getValue(), data.getKey(), result);
                    out.println(Utils.createJsonSuccessObject(result.toString()));
                } catch (Exception e) {
                    out.println(Utils.createJsonErrorObject("Importing weekly activities failed due to an unknown error."));
                }
            }
        }
    }
}
