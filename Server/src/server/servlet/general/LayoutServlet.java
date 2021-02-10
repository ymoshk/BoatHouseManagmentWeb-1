package server.servlet.general;

import com.google.gson.Gson;
import engine.api.EngineContext;
import server.constant.Constants;
import server.constant.ePages;
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

    private ePages currentPage;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.currentPage = (ePages) req.getServletContext().getAttribute(Constants.currentActivePagAttr);

        boolean isAdmin = EngineContext.getInstance().getRowerBySessionId(req.getRequestedSessionId()).isAdmin();
        List<MenuItem> menuItems = createNavMenuItemsList(isAdmin);
        Map<String, Object> res = new HashMap<>();
        res.put("menu", menuItems);
        res.put("active", req.getServletContext().getAttribute(Constants.currentActivePagAttr));

        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            out.println(gson.toJson(res));
        }
    }

    private List<MenuItem> createNavMenuItemsList(boolean isAdmin) {
        List<MenuItem> res = new ArrayList<>();
        res.add(new MenuItem(false, "navHome", "fa fa-home", ePages.HOME.getTitle(), "/home"));
        res.add(new MenuItem(false, "navPersonalDetails", "fa fa-user-o"
                , ePages.PERSONAL_DETAILS.getTitle(), "/personal-details"));

        if (isAdmin) {
            res.add(new MenuItem(false, "navRowers"
                    , "fa fa-users", ePages.ROWERS.getTitle(), "/rowers/index"));
            res.add(new MenuItem(false, "navBoats"
                    , "fa fa-anchor", ePages.BOATS.getTitle(), "/boats/index"));
            res.add(new MenuItem(false, "navWeeklyActivities"
                    , "fa fa-clock-o", ePages.WEEKLY_ACTIVITIES.getTitle(), "/weekly-activities/index"));
            res.add(new MenuItem(false, "navRowingActivities"
                    , "fa fa-ship", ePages.ROWING_ACTIVITY.getTitle(), "/rowing-activities/index"));
        }

        res.add(new MenuItem(false, "navRequest"
                , "fa fa-calendar", ePages.REQUESTS.getTitle(), "/requests/index"));
        res.add(new MenuItem(false, "navData"
                , "fa fa-info-circle", ePages.MANAGE_DATA.getTitle(), "/manage-data"));
        res.add(new MenuItem(false, "navLogout"
                , "fa fa-sign-out", "Logout", "/logout"));

        setActive(res);

        return res;
    }

    private void setActive(List<MenuItem> menuItems) {
        for (MenuItem item : menuItems) {
            if(item.text.equals(this.currentPage.getTitle())){
                item.isActive = true;
            }
        }
    }


}
