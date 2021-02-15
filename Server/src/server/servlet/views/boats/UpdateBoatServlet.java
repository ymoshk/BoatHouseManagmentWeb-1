package server.servlet.views.boats;


import engine.api.EngineContext;
import engine.model.boat.Boat;
import engine.model.boat.BoatModifier;
import engine.model.rower.Rower;
import javafx.util.Pair;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/boats/update")
public class UpdateBoatServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {

            String serial = (String) req.getSession().getAttribute("UpdateBoatSerial");

            if (serial == null) {
                resp.sendRedirect("/boats/index");
            } else {
                req.getSession().removeAttribute("UpdateBoatSerial");
                Boat boatToEdit = EngineContext.getInstance().getBoatsCollectionManager().findBySerialNumber(serial);
                if (boatToEdit == null) {
                    out.println(Utils.createJsonErrorObject("Couldn't find the requested boat"));
                } else {
                    String updatePage = Utils.readHtmlPage("/public/html/views/boats/update.html", req);
                    updatePage = prepareUpdatePage(updatePage, boatToEdit);
                    Utils.renderLayoutFromHtml(req, resp, updatePage, ePages.BOATS);
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

                Boat boatToEdit = eng.getBoatsCollectionManager().findBySerialNumber(serial);
                if (boatToEdit == null) {
                    out.println(Utils.createJsonErrorObject("Unknown error occurred during boat editing."));
                } else {
                    List<String> errors = new ArrayList<>(updateTheBoat(boatToEdit, data));

                    if (!errors.isEmpty()) {
                        out.println(Utils.createJsonErrorsListObject(errors));
                    } else {
                        out.println(Utils.createJsonSuccessObject(true));
                    }
                }

            } catch (Exception ex) {
                out.println(Utils.createJsonErrorObject("Unknown error occurred during boat editing."));
            }
        }
    }

    private List<String> updateTheBoat(Boat boatToEdit, HashMap<String, String> data) {
        List<String> errors = new ArrayList<>();
        EngineContext eng = EngineContext.getInstance();
        BoatModifier modifier = eng.getBoatModifier(boatToEdit, null);

        handleNameSet(data.get("name"), errors, modifier);
        handleOwnerSet(data.get("owner"), errors, modifier);
        handleTypeSet(data.get("boatType"), errors, modifier);
        handleCheckBoxes(data, modifier);

        return errors;
    }

    private void handleOwnerSet(String owner, List<String> errors, BoatModifier modifier) {
        if (owner.equals("none")) {
            modifier.removeOwner();
        } else {
            Rower newOwner = EngineContext.getInstance().getRowersCollectionManager().findRowerBySerialNumber(owner);
            if (newOwner == null) {
                errors.add("Couldn't attach the new owner to this boat.");
                modifier.removeOwner();
            } else {
                modifier.setOwner(newOwner);
            }
        }
    }

    private void handleCheckBoxes(HashMap<String, String> data, BoatModifier modifier) {
        modifier.setState(Boolean.parseBoolean(data.get("isDisabled")));
        modifier.setIsSeaBoat(Boolean.parseBoolean(data.get("isSeaBoat")));
        modifier.setIsWide(Boolean.parseBoolean(data.get("isWide")));
    }


    private void handleNameSet(String name, List<String> errors, BoatModifier modifier) {
        if (name.isEmpty()) {
            errors.add("Boat name can't be empty");
        } else {
            modifier.setName(name);
        }
    }

    private void handleTypeSet(String typeIndex, List<String> errors, BoatModifier modifier) {
        String msg = "Boat type couldn't be set for unknown reason.";
        try {
            if (typeIndex.isEmpty()) {
                errors.add(msg);
            } else {
                int index = Integer.parseInt(typeIndex);
                List<Pair<Boat.eBoatType, Integer>> types =
                        Boat.eBoatType.toList(modifier.getObjectToEdit().getBoatType());
                Optional<Pair<Boat.eBoatType, Integer>> newTypePair = types.stream()
                        .filter(pair -> pair.getValue().equals(index)).findFirst();
                if (newTypePair.isPresent()) {
                    modifier.setBoatType(newTypePair.get().getKey());
                } else {
                    errors.add(msg);
                }
            }
        } catch (Exception ex) {
            errors.add(msg);
        }
    }

    private String prepareUpdatePage(String updatePage, Boat boat) {
        return updatePage.replace("{serialNumber}", boat.getSerialNumber())
                .replace("{name}", boat.getName())
                .replace("{isWide}", boat.isWide() ? "checked" : "")
                .replace("{isSeaBoat}", boat.isSeaBoat() ? "checked" : "")
                .replace("{isDisable}", boat.isDisable() ? "checked" : "");
    }

}
