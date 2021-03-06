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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@WebServlet(name = "CreateRower", urlPatterns = "/rowers/create")
public class CreateRowerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utils.renderLayout(req, resp, "/public/html/views/rowers/create.html", ePages.ROWERS);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {
            HashMap<String, String> data = Utils.parsePostData(req);
            try {
                String serial = data.get("serialNumber").trim();
                String name = data.get("name").trim();
                int age = Integer.parseInt(data.get("age"));
                String phone = data.get("phone").trim();
                String email = data.get("email").trim();
                String password = data.get("password").trim();
                List<String> notes = Utils.splitsNotes(data.get("notes"));
                boolean isAdmin = Boolean.parseBoolean(data.get("isAdmin"));
                Rower.eRowerRank rank = Rower.eRowerRank.getFromInt(Integer.parseInt(data.get("level")) - 1);
                String[] privateBoats = new Gson().fromJson(data.get("boatsId"), String[].class);


                EngineContext eng = (EngineContext) req.getServletContext().getAttribute(Constants.engineAtt);
                List<String> errors = new ArrayList<>(validatePhone(phone));

                if (!errors.isEmpty()) {
                    out.println(Utils.createJsonErrorsListObject(errors));
                } else {
                    Rower newRower;
                    if (privateBoats.length > 0) {
                        newRower = new Rower(serial, name, age, rank, password, isAdmin, email, phone, privateBoats);
                    } else {
                        newRower = new Rower(serial, name, age, rank, password, isAdmin, email, phone);
                    }
                    errors.addAll(validateRower(newRower, eng));

                    if (!errors.isEmpty()) {
                        // Failed
                        out.println(Utils.createJsonErrorsListObject(errors));
                    } else {
                        // Success
                        eng.getRowersCollectionManager().add(newRower);
                        attachNewRowerToItsPrivateBoats(newRower);
                        if (notes != null) {
                            RowerModifier modifier = eng.getRowerModifier(newRower, null);
                            notes.forEach(modifier::addNewNote);
                        }
                        out.println(Utils.createJsonSuccessObject(true));
                    }
                }
            } catch (Exception ex) {
                out.println(Utils.createJsonErrorObject("Unknown error occurred during rower creation."));
            }
        }
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

    private void attachNewRowerToItsPrivateBoats(Rower newRower) {
        EngineContext eng = EngineContext.getInstance();
        for (String serialNumber : newRower.getPrivateBoatsSerialNumbers()) {
            Boat bt = eng.getBoatsCollectionManager().findBySerialNumber(serialNumber);
            if (bt != null) {
                eng.getBoatModifier(bt, System.out::println).setOwner(newRower);
            }
        }
    }
}

