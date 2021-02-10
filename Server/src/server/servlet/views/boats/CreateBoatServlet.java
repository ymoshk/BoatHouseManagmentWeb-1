package server.servlet.views.boats;

import com.sun.deploy.ref.Helpers;
import engine.api.EngineContext;
import engine.model.boat.Boat;
import engine.model.rower.Rower;
import server.constant.ePages;
import server.utils.Utils;

import javax.rmi.CORBA.Util;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/boats/create")
public class CreateBoatServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utils.renderLayout(req, resp, "/public/html/views/boats/create.html", ePages.BOATS);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineContext eng = EngineContext.getInstance();
        HashMap<String, String> reqData = Utils.parsePostData(req);
        String serialNumber = reqData.get("serialNumber");
        String name = reqData.get("name");
        String ownerSerialNumber = reqData.get("ownerSerialNumber");
        Rower owner = null;

        if (ownerSerialNumber != null && !ownerSerialNumber.isEmpty()) {
            owner = eng.getRowersCollectionManager().findRowerBySerialNumber(ownerSerialNumber);
        }

        int boatTypeAsInt = Integer.parseInt(reqData.get("boatType"));
        Boat.eBoatType boatType = Boat.eBoatType.getTypeFromInt(boatTypeAsInt);
        boolean isWide = Boolean.parseBoolean(reqData.get("isWide"));
        boolean isSeaBoat = Boolean.parseBoolean(reqData.get("isSeaBoat"));
        boolean isDisable = Boolean.parseBoolean(reqData.get("isDisable"));
        Boat boatToAdd;

        if (owner != null) {
            boatToAdd = new Boat(name, serialNumber, boatType, isWide, isSeaBoat, isDisable, owner);
        } else {
            boatToAdd = new Boat(name, serialNumber, boatType, isWide, isSeaBoat, isDisable);
        }

        try (PrintWriter out = resp.getWriter()) {
            List<Object> errors = new ArrayList<>();

            if (!eng.isBoatSerialNumberFree(serialNumber)) {
                errors.add("Serial number already exist");
                out.println(Utils.getErrorJson(errors));
            } else if (eng.getBoatsCollectionManager().add(boatToAdd)) {
                out.println(Utils.getSuccessJson(true));
            } else {
                out.println(Utils.getSuccessJson(false));
            }
        }


    }
}
