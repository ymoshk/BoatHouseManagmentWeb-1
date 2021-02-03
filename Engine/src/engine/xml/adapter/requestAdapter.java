package engine.xml.adapter;

import engine.model.activity.request.AdaptedRequest;
import engine.model.activity.request.Request;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.boat.Boat;
import engine.model.rower.Rower;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class requestAdapter extends XmlAdapter<AdaptedRequest, Request> {

    @Override
    public Request unmarshal(AdaptedRequest adaptedRequest) throws Exception {
        Rower mainRower = adaptedRequest.getMainRower();
        Rower creator = adaptedRequest.getRequestCreator();
        WeeklyActivity weeklyActivity = adaptedRequest.getWeeklyActivity();
        LocalDate registrationDate = adaptedRequest.getRegistrationDate();
        LocalDate trainingDate = adaptedRequest.getTrainingDate();
        List<Boat.eBoatType> type = adaptedRequest.getBoatTypesList();
        List<Rower> otherRowers = new ArrayList<>();
        boolean isApprove = adaptedRequest.isApproved();

        return new Request(mainRower, creator, weeklyActivity,
                trainingDate, otherRowers, type, registrationDate, isApprove);
    }

    @Override
    public AdaptedRequest marshal(Request request) {
        AdaptedRequest result = new AdaptedRequest();
        result.setApproved(request.isApproved());
        result.setBoatTypesList(request.getBoatTypesList());
        result.setMainRower(request.getMainRower());
        result.setRegistrationDate(request.getRegistrationDate());
        result.setRequestCreator(request.getRequestCreator());
        result.setWeeklyActivity(request.getWeeklyActivityActivity());
        result.setTrainingDate(request.getTrainingDate());

        return result;
    }
}
