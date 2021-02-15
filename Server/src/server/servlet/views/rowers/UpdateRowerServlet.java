package server.servlet.views.rowers;


import com.google.gson.Gson;
import engine.api.EngineContext;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import engine.model.rower.RowerModifier;
import engine.utils.RegexHandler;
import server.constant.Constants;
import server.constant.ePages;
import server.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet(urlPatterns = "/rowers/update")
public class UpdateRowerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {

            String serial = (String) req.getSession().getAttribute("UpdateRowerSerial");

            if (serial == null) {
                resp.sendRedirect("/rowers/index");
            } else {
                req.getSession().removeAttribute("UpdateRowerSerial");
                Rower rowerToEdit = EngineContext.getInstance().getRowersCollectionManager().findRowerBySerialNumber(serial);

                if (rowerToEdit == null) {
                    out.println(Utils.createJsonErrorObject("Couldn't find the requested rower"));
                } else {
                    String updatePage = Utils.readHtmlPage("/public/html/views/rowers/update.html", req);
                    updatePage = prepareUpdatePage(updatePage, rowerToEdit, req.getSession().getId());
                    Utils.renderLayoutFromHtml(req, resp, updatePage, ePages.ROWERS);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {
            HashMap<String, String> data = Utils.parsePostData(req);

            try {
                EngineContext eng = (EngineContext) req.getServletContext().getAttribute(Constants.engineAtt);
                String serial = data.get("serialNumber");

                Rower rowerToEdit = eng.getRowersCollectionManager().findRowerBySerialNumber(serial);
                if (rowerToEdit == null) {
                    out.println(Utils.createJsonErrorObject("Unknown error occurred during rower editing."));
                } else {
                    List<String> errors = new ArrayList<>(updateTheRower(rowerToEdit, data));

                    if (!errors.isEmpty()) {
                        out.println(Utils.createJsonErrorObject(errors));
                    } else {
                        out.println(Utils.createJsonSuccessObject(true));
                    }
                }

            } catch (Exception ex) {
                out.println(Utils.createJsonErrorObject("Unknown error occurred during rower editing."));
            }
        }
    }

    private List<String> updateTheRower(Rower rowerToEdit, HashMap<String, String> data) {
        List<String> errors = new ArrayList<>();
        EngineContext eng = EngineContext.getInstance();
        RowerModifier modifier = eng.getRowerModifier(rowerToEdit, null);

        handleEmailSet(eng, rowerToEdit, modifier, data.get("email"), errors);
        handleNameSet(data.get("name"), errors, modifier);
        handleAgeSet(data.get("age"), errors, modifier);
        handlePhoneSet(data.get("phone"), errors, modifier);
        handleExpDateSet(data.get("expirationDate"), errors, modifier);
        if (!handlePrivateBoats(new Gson().fromJson(data.get("boatsId"), String[].class), rowerToEdit)) {
            errors.add("Error occurred while updating the private boats");
        }
        modifier.setIsAdminStatus(Boolean.parseBoolean(data.get("isAdmin")));

        Rower.eRowerRank rank = Rower.eRowerRank.getFromInt(Integer.parseInt(data.get("level")));
        modifier.setRowerRank(rank);

        List<String> notes = Utils.splitsNotes(data.get("notes"));
        modifier.cleanNotes();
        if (notes != null) {
            notes.forEach(modifier::addNewNote);
        }
        return errors;
    }

    private boolean handlePrivateBoats(String[] boatsId, Rower rower) {
        try {
            EngineContext eng = EngineContext.getInstance();
            List<String> idToBoatList = Arrays.asList(boatsId);
            List<Boat> currentBoats = eng.getBoatsCollectionManager()
                    .filter(boat -> boat.hasOwner() && boat.getOwner().equals(rower));
            List<Boat> newBoats = eng.getBoatsCollectionManager()
                    .filter(boat -> idToBoatList.contains(boat.getSerialNumber()));

            currentBoats.forEach(boat -> {
                rower.removePrivateBoat(boat);
                eng.getBoatModifier(boat, null).removeOwner();
            });

            for (Boat current : newBoats) {
                rower.addPrivateBoatAndChangeItsOwner(current);
            }

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void handleExpDateSet(String expirationDate, List<String> errors, RowerModifier modifier) {
        if (expirationDate.isEmpty()) {
            errors.add("Subscription expiration date can't be empty");
        } else {
            LocalDate parsedDate;

            try {
                parsedDate = LocalDate.parse(expirationDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (parsedDate.isBefore(LocalDate.now())) {
                    errors.add("Subscription expiration date must be greater than today");
                } else {
                    modifier.setRowerExpirationDate(parsedDate);
                }
            } catch (Exception ex) {
                errors.add("Invalid subscription expiration date received");
            }
        }
    }

    private void handlePhoneSet(String phone, List<String> errors, RowerModifier modifier) {
        if (!phone.isEmpty()) {
            if (!RegexHandler.isPhoneNumberValid(phone)) {
                errors.add("Invalid phone number received.");
            } else {
                modifier.setRowerPhoneNumber(phone);
            }
        } else {
            errors.add("Rower phone can't be empty.");
        }
    }

    private void handleAgeSet(String age, List<String> errors, RowerModifier modifier) {
        if (age.isEmpty()) {
            errors.add("Rower age can't be empty");
        } else {
            if (Integer.parseInt(age) >= 13) {
                modifier.setRowerAge(Integer.parseInt(age));
            } else {
                errors.add("Rower age must be at least 13");
            }
        }
    }

    private void handleNameSet(String name, List<String> errors, RowerModifier modifier) {
        if (name.isEmpty()) {
            errors.add("Rower name can't be empty");
        } else {
            modifier.setRowerName(name);
        }
    }

    private void handleEmailSet(EngineContext eng, Rower rowerToEdit, RowerModifier modifier,
                                String email, List<String> result) {
        if (!email.equals(rowerToEdit.getEmail())) {
            if (email.isEmpty() || !RegexHandler.isEmailAddressValid(email)) {
                result.add("Email address isn't valid");
            } else if (eng.getRowersCollectionManager().emailExist(email)) {
                result.add("The new email address already exists.");
            } else {
                modifier.setRowerEmail(email);
            }
        }
    }

    private String prepareUpdatePage(String updatePage, Rower rower, String sessionId) {
        String loggedInSerial = EngineContext.getInstance().getLoggedInUser(sessionId).getSerialNumber();

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
                .replace("{selected2}", rower.getRank() == Rower.eRowerRank.PRO ? "selected" : "")
                .replace("{isAdminDisabled}", loggedInSerial.equals(rower.getSerialNumber()) ? "disabled" : "");
    }

    private CharSequence mergeNotes(Rower rower) {
        StringBuilder res = new StringBuilder();
        rower.getNotes().forEach(note -> res.append(note).append('\n'));
        return res.toString();
    }
}
