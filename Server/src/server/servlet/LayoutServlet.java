package server.servlet;

import com.google.gson.Gson;
import engine.api.EngineContext;
import server.servlet.json.template.MenuItem;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

@WebServlet(name = "layout", urlPatterns = "/layout")
public class LayoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean isAdmin = EngineContext.getInstance().getRowerBySessionId(req.getRequestedSessionId()).isAdmin();
        List<MenuItem> menuItems = createNavMenuItemsList(isAdmin);
        Map<String, Object> res = new HashMap<>();
        res.put("menu", menuItems);

        try(PrintWriter out = resp.getWriter()){
            Gson gson = new Gson();
            out.println(gson.toJson(res));
        }
    }

    private List<MenuItem> createNavMenuItemsList(boolean isAdmin){
        List<MenuItem> res = new ArrayList<>();
        res.add(new MenuItem(true, "navHome", "fa fa-home", "Home"));
        res.add(new MenuItem(false, "navPersonalDetails", "fa fa-user-o", "Personal Details"));

        if(isAdmin) {
            res.add(new MenuItem(false, "navRowers", "fa fa-users", "Rowers"));
            res.add(new MenuItem(false, "navBoats", "fa fa-anchor", "Boats"));
            res.add(new MenuItem(false, "navWeeklyActivities", "fa fa-clock-o", "Weekly Activities"));
            res.add(new MenuItem(false, "navRowingActivities", "fa fa-ship", "Home"));
        }

        res.add(new MenuItem(false, "navRequest", "fa fa-calendar", "Requests"));
        res.add(new MenuItem(false, "navData", "fa fa-info-circle", "Manage Data"));
        res.add(new MenuItem(false, "navLogout", "fa fa-sign-out", "Logout"));

        return res;
    }


}
