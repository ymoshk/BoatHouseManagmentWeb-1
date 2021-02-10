package server.servlet.views.rowers;

import com.google.gson.Gson;
import engine.api.EngineContext;
import engine.model.rower.Rower;
import engine.model.rower.RowerModifier;
import engine.utils.RegexHandler;
import server.constant.Constants;
import server.constant.ePages;
import server.servlet.json.template.ErrorsList;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@WebServlet(urlPatterns = "/rowers/update")
public class UpdateRowerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {

            String serial = req.getParameter("serialNumber");
            Rower rowerToEdit = EngineContext.getInstance().getRowersCollectionManager().findRowerBySerialNumber(serial);

            if (rowerToEdit == null) {
                out.println(Utils.getErrorJson(Collections.singletonList("Couldn't find the requested rower")));
            } else {
                String updatePage = Utils.readHtmlPage("/public/html/views/rowers/update.html", req);
                updatePage = prepeareUpdatePage(updatePage, rowerToEdit);
                Utils.renderLayoutString(req, resp, updatePage, ePages.ROWERS);
            }
        }
    }

    private String prepeareUpdatePage(String updatePage, Rower rower) {
        String age = String.valueOf(rower.getAge());
        return updatePage.replace("{serialNumber}", rower.getSerialNumber())
                .replace("{name}", rower.getName())
                .replace("{email}", rower.getEmail())
                .replace("{phone}", rower.getPhoneNumber())
                .replace("{age}", age)
                .replace("{notes}", mergeNotes(rower))
                .replace("{expiration}", rower.getFormattedExpirationDate())
                .replace("{isAdmin}", rower.isAdmin() ? "checked" : "")
                .replace("{selected0}", rower.getRank() == Rower.eRowerRank.BEGINNER ? "selected" : "")
                .replace("{selected1}", rower.getRank() == Rower.eRowerRank.AVERAGE ? "selected" : "")
                .replace("{selected2}", rower.getRank() == Rower.eRowerRank.PRO ? "selected" : "");
    }

    private CharSequence mergeNotes(Rower rower) {
        StringBuilder res = new StringBuilder();
        rower.getNotes().forEach(note -> res.append(note).append('\n'));
        return res.toString();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        // TODO handle private boats
        resp.setContentType("application/json");
        String serial = req.getParameter("serialNumber");
        String name = req.getParameter("name");
        int age = Integer.parseInt(req.getParameter("age"));
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        List<String> notes = splitsNotes(req.getParameter("notes"));
        boolean isAdmin = Boolean.parseBoolean(req.getParameter("isAdmin"));
        Rower.eRowerRank rank = Rower.eRowerRank.getFromInt(Integer.parseInt(req.getParameter("level")) - 1);


        try (PrintWriter out = resp.getWriter()) {
            EngineContext eng = (EngineContext) req.getServletContext().getAttribute(Constants.engineAtt);
            List<String> errors = new ArrayList<>(validatePhone(phone));

            if (!errors.isEmpty()) {
                out.println(new Gson().toJson(new ErrorsList(false, errors)));
            } else {
                Rower newRower = new Rower(serial, name, age, rank, password, isAdmin, email, phone);
                errors.addAll(validateRower(newRower, eng));

                if (!errors.isEmpty()) {
                    // Failed
                    out.println(new Gson().toJson(new ErrorsList(false, errors)));
                } else {
                    // Success
                    eng.getRowersCollectionManager().add(newRower);
                    if (notes != null) {
                        RowerModifier modifier = eng.getRowerModifier(newRower, null);
                        notes.forEach(modifier::addNewNote);
                    }
                    out.println(Utils.standardJsonResponse(true));
                }
            }
        }
    }

    private List<String> splitsNotes(String notes) {
        if (notes.length() == 0) {
            return null;
        }
        List<String> temp = Arrays.asList(notes.split(String.valueOf('\n')).clone());
        List<String> res = new ArrayList<>();
        temp.forEach(str -> res.add(str.trim()));
        return res;
    }

    private List<String> validateRower(Rower newRower, EngineContext eng) {
        List<String> result = new ArrayList<>();

        if (!eng.getRowersCollectionManager().isSerialNumberAvailable(newRower.getSerialNumber())) {
            result.add("Serial number already exist.");
        }

        if (eng.getRowersCollectionManager().emailExist(newRower.getEmail())) {
            result.add("Email address already exists.");
        }

        return result;
    }

    private List<String> validatePhone(String phone) {
        List<String> result = new ArrayList<>();
        if (!RegexHandler.isPhoneNumberValid(phone)) {
            result.add("Invalid phone number received.");
        }
        return result;
    }
}

