package server.servlet.json.template.model.request;

import engine.model.activity.request.Request;
import engine.model.boat.Boat;
import server.servlet.json.template.model.rower.RowerJson;
import server.servlet.json.template.model.weekly.activity.WeeklyActivityJson;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RequestJson {
    public final String id;
    public final List<Integer> boatTypesList;
    private final RowerJson mainRower;
    private final RowerJson requestCreator;
    private final WeeklyActivityJson weeklyActivity;
    public final String trainingDate;
    public final String creationDate;
    public final List<RowerJson> otherRowersList;
    public final boolean isApproved;

    public RequestJson(Request req){
        this.id = req.getId();
        this.boatTypesList = new ArrayList<>();
        req.getBoatTypesList()
                .forEach(boatType -> this.boatTypesList.add(Boat.eBoatType.getIntFromBoatType(boatType)));
        this.mainRower = new RowerJson(req.getMainRower());
        this.requestCreator = new RowerJson(req.getRequestCreator());
        this.weeklyActivity = new WeeklyActivityJson(req.getWeeklyActivityActivity());
        this.trainingDate = req.getTrainingDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.creationDate = req.getRegistrationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.otherRowersList = new ArrayList<>();
        req.getOtherRowersList().forEach(rower -> otherRowersList.add(new RowerJson(rower)));
        this.isApproved = req.isApproved();
    }

}
