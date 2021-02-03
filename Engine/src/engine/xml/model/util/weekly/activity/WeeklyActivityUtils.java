package engine.xml.model.util.weekly.activity;

import engine.api.EngineContext;
import engine.api.EngineInterface;
import engine.database.collection.WeeklyActivityCollectionManager;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.boat.Boat;
import jaxb.schema.engine.generated.activity.Activities;
import jaxb.schema.engine.generated.activity.BoatType;
import jaxb.schema.engine.generated.activity.Timeframe;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class WeeklyActivityUtils {
    private static final WeeklyActivityCollectionManager activities;
    private static EngineInterface engine;

    static {
        try {
            engine = EngineContext.getInstance();
        } catch (Exception ignored) {

        }

        activities = engine.getWeeklyActivitiesCollectionManager();
    }

    public static int addAllTimeFrames(Activities timeFrames, StringBuilder stringBuilder) {
        List<Timeframe> times = timeFrames.getTimeframe();
        int counter = 0;
        if (!timeFrames.getTimeframe().isEmpty()) {
            for (Timeframe timeframe : times) {
                WeeklyActivity activityToAdd = convertFromTimeFrameToWeeklyActivity(timeframe, stringBuilder);
                if (activityToAdd != null) {
                    boolean isAdded = activities.add(activityToAdd);

                    if (!isAdded) {
                        stringBuilder.append(String.format(
                                "A Activity (name: %s | start time: %s | end time: %s) already exists%n",
                                activityToAdd.getName(), activityToAdd.getStartTime(), activityToAdd.getEndTime()));
                    } else {
                        counter++;
                    }
                }
            }
        } else {
            stringBuilder.append("The weekly activity xml file must has at least one element.\n");
        }
        return counter;
    }

    public static Timeframe convertWeeklyActivityToTimeFrame(WeeklyActivity weeklyActivity) {
        String name = weeklyActivity.getName();
        String start = weeklyActivity.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String end = weeklyActivity.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        BoatType boatType = null;
        if (weeklyActivity.getBoatType() != null) {
            boatType = boatTypeToTrainingFrameBoatType(weeklyActivity.getBoatType());
        }

        Timeframe res = new Timeframe();
        res.setName(name);
        res.setStartTime(start);
        res.setEndTime(end);
        res.setBoatType(boatType);

        return res;
    }

    private static WeeklyActivity convertFromTimeFrameToWeeklyActivity(Timeframe timeframe, StringBuilder stringBuilder) {
        WeeklyActivity res = null;

        if (isValidTimeFrame(timeframe, stringBuilder)) {
            LocalTime startTime = LocalTime.parse(timeframe.getStartTime());
            LocalTime endTime = LocalTime.parse(timeframe.getEndTime());
            String name = timeframe.getName();
            Boat.eBoatType boatType = null;
            if (timeframe.getBoatType() != null) {
                boatType = trainingFrameBoatTypeToBoatType(timeframe.getBoatType());
            }

            res = new WeeklyActivity(name, startTime, endTime, boatType);
        }

        return res;
    }

    private static BoatType boatTypeToTrainingFrameBoatType(Boat.eBoatType boatType) {
        BoatType res = null;

        switch (boatType) {
            case SINGLE:
                res = BoatType.SINGLE;
                break;
            case DUE:
                res = BoatType.DOUBLE;
                break;
            case DUE_WITH_COXWAIN:
                res = BoatType.COXED_DOUBLE;
                break;
            case DUE_SINGLE_OAR:
                res = BoatType.PAIR;
                break;
            case DUE_SINGLE_OAR_WITH_COXWAIN:
                res = BoatType.COXED_PAIR;
                break;
            case FOUR:
                res = BoatType.QUAD;
                break;
            case FOUR_WITH_COXWAIN:
                res = BoatType.COXED_QUAD;
                break;
            case FOUR_SINGLE_OAR:
                res = BoatType.FOUR;
                break;
            case FOUR_SINGLE_OAR_WITH_COXWAIN:
                res = BoatType.COXED_FOUR;
                break;
            case EIGHT_SINGLE_OAR:
                res = BoatType.EIGHT;
                break;
            case EIGHT:
                res = BoatType.OCTUPLE;
                break;
        }

        return res;
    }

    private static Boat.eBoatType trainingFrameBoatTypeToBoatType(BoatType boatType) {
        Boat.eBoatType res = null;

        switch (boatType) {
            case SINGLE:
                res = Boat.eBoatType.SINGLE;
                break;
            case DOUBLE:
                res = Boat.eBoatType.DUE;
                break;
            case COXED_DOUBLE:
                res = Boat.eBoatType.DUE_WITH_COXWAIN;
                break;
            case PAIR:
                res = Boat.eBoatType.DUE_SINGLE_OAR;
                break;
            case COXED_PAIR:
                res = Boat.eBoatType.DUE_SINGLE_OAR_WITH_COXWAIN;
                break;
            case QUAD:
                res = Boat.eBoatType.FOUR;
                break;
            case COXED_QUAD:
                res = Boat.eBoatType.FOUR_WITH_COXWAIN;
                break;
            case FOUR:
                res = Boat.eBoatType.FOUR_SINGLE_OAR;
                break;
            case COXED_FOUR:
                res = Boat.eBoatType.FOUR_SINGLE_OAR_WITH_COXWAIN;
                break;
            case EIGHT:
                res = Boat.eBoatType.EIGHT_SINGLE_OAR;
                break;
            case OCTUPLE:
                res = Boat.eBoatType.EIGHT;
                break;
        }

        return res;
    }

    private static boolean isValidTimeFrame(Timeframe timeframe, StringBuilder stringBuilder) {
        boolean result = true;
        StringBuilder errors = new StringBuilder();

        try {
            LocalTime startTime = LocalTime.parse(timeframe.getStartTime());
            LocalTime endTime = LocalTime.parse(timeframe.getEndTime());
            if (timeframe.getName().isEmpty()) {
                result = false;
                errors.append("Name can't be empty\n");
            }
            if (startTime.isAfter(endTime) || timeframe.getName().isEmpty()) {
                errors.append("Start time must be earlier then end time\n");
                result = false;
            }
        } catch (DateTimeParseException e) {
            errors.append("'Start/End time' aren't valid\n");
            result = false;
        }

        if (errors.length() != 0) {
            stringBuilder.append(String.format("Weekly activity %s isn't valid.%nerrors:%n%s", timeframe.getName(), errors.toString()));
        }

        return result;
    }
}
