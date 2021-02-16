package server.servlet.data.export;

import engine.api.EngineContext;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

@WebServlet(urlPatterns = "/data/export/weekly-activities")
public class ExportWeeklyActivitiesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String fileName = "weekly-activities_" + Instant.now().getEpochSecond();
            String xml = EngineContext.getInstance().getWeeklyActivitiesCollectionManager().exportToXml();
            Utils.exportHandler(resp, xml, fileName);
        } catch (JAXBException e) {
            try (PrintWriter out = resp.getWriter()) {
                out.println(Utils.createJsonErrorObject("Exporting weekly activities failed due to an unknown error."));
            }
        }
    }
}
