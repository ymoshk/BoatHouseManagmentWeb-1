package server.servlet.collector.rower;

import com.google.gson.Gson;
import engine.api.EngineContext;
import engine.model.rower.Rower;
import server.servlet.json.template.model.rower.RowerListJson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = "/collectors/rowers")
public class GetRowersCollectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        List<Rower> rowers = eng.getRowersCollectionManager().toArrayList();

        try(PrintWriter out = resp.getWriter()){
            out.print(new Gson().toJson(new RowerListJson(rowers)));
        }
    }
}
