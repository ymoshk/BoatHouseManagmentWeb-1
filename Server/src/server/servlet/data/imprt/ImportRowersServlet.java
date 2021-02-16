package server.servlet.data.imprt;

import engine.api.EngineContext;
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

@WebServlet(urlPatterns = "/data/import/rowers")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5)
public class ImportRowersServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Collection<Part> parts = req.getParts();
        String xml = Utils.readFileParts(parts);

        try (PrintWriter out = resp.getWriter()) {
            StringBuilder result = new StringBuilder();
            Map<String, String> data = Utils.parsePostData(req);
            boolean deleteAll = Boolean.parseBoolean(data.get("deleteAll"));
            EngineContext.getInstance().getRowersCollectionManager().importFromXml(xml, deleteAll, result);
            out.println(Utils.createJsonSuccessObject(result.toString()));
        } catch (Exception ex) {
            try (PrintWriter out = resp.getWriter()) {
                out.println(Utils.createJsonErrorObject("Importing file failed due to an unknown error."));
            }
        }
    }
}
