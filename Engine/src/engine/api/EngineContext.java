package engine.api;

import engine.database.collection.*;
import engine.model.Model;
import engine.model.Modifier;
import engine.model.activity.request.Request;
import engine.model.activity.request.RequestModifier;
import engine.model.activity.rowing.RowingActivity;
import engine.model.activity.rowing.RowingActivityModifier;
import engine.model.activity.weekly.activity.WeeklyActivity;
import engine.model.activity.weekly.activity.WeeklyActivityModifier;
import engine.model.boat.Boat;
import engine.model.boat.BoatModifier;
import engine.model.rower.Rower;
import engine.model.rower.RowerModifier;
import engine.utils.crypto.RC4;
import engine.utils.data.structure.Triple;
import engine.xml.XmlConverter;
import javafx.util.Pair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.bind.JAXBException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public class EngineContext implements EngineInterface, Serializable {

    private static final String SAVE_FILE_PATH = "engine.xml";
    private static EngineContext instance = null;
    private final RowersCollectionManager rowers;
    private final BoatsCollectionManager boats;
    private final WeeklyActivityCollectionManager weeklyActivities;
    private final RequestsCollectionManager requests;
    private final RowingActivitiesCollectionManager rowingActivities;
    private String modifyCallback;
    private final Map <String, String> sessionsUsersMap = new HashMap<>();

    private EngineContext() {
        this.rowers = new RowersCollectionManager(this);
        this.boats = new BoatsCollectionManager(this);
        createAdminUser();
        this.requests = new RequestsCollectionManager(this);
        this.weeklyActivities = new WeeklyActivityCollectionManager(this);
        this.rowingActivities = new RowingActivitiesCollectionManager(this);
    }

    // Constructor to convert a AdaptedEngineContext (XML object) into a EngineContext.
    private EngineContext(AdaptedEngineContext adaptedEngineContext) throws Exception {
        List<Rower> rowers = adaptedEngineContext.getRowers();
        if (adaptedEngineContext.getRowers() == null) {
            rowers = new ArrayList<>();
        }

        List<Boat> boats = adaptedEngineContext.getBoats();
        if (adaptedEngineContext.getBoats() == null) {
            boats = new ArrayList<>();
        }

        List<WeeklyActivity> weeklyActivities = adaptedEngineContext.getWeeklyActivities();
        if (adaptedEngineContext.getWeeklyActivities() == null) {
            weeklyActivities = new ArrayList<>();
        }

        List<Request> requests = adaptedEngineContext.getRequests();
        if (adaptedEngineContext.getRequests() == null) {
            requests = new ArrayList<>();
        }

        List<RowingActivity> rowingActivities = adaptedEngineContext.getRowingActivities();
        if (adaptedEngineContext.getRowingActivities() == null) {
            rowingActivities = new ArrayList<>();
        }

        this.rowers = new RowersCollectionManager(this, rowers);

        // Bonus #1 - decryption
        for (Rower rower : this.rowers) {
            RowerModifier modifier = new RowerModifier(null, rower, null, null);
            modifier.setRowerPassword(RC4.decrypt(rower.getPassword()));
        }

        fixBoatsList(boats, this.rowers);
        this.boats = new BoatsCollectionManager(this, boats);
        this.weeklyActivities = new WeeklyActivityCollectionManager(this, weeklyActivities);
        fixRequestsList(requests, this.rowers, this.weeklyActivities);
        this.requests = new RequestsCollectionManager(this, requests);
        fixRowingActivityList(rowingActivities, this.requests, this.boats);
        this.rowingActivities = new RowingActivitiesCollectionManager(this, rowingActivities);
    }

    public static EngineContext getInstance() {
        if (instance == null) {
            instance = loadOrCreateInstance();
        }
        return instance;
    }

    private static EngineContext loadOrCreateInstance() {
        try {
            AdaptedEngineContext adaptedEngineContext = XmlConverter.parseEngineFromXML(SAVE_FILE_PATH);
            if (adaptedEngineContext != null) {
                try {
                    return new EngineContext(adaptedEngineContext);
                } catch (Exception ex) {
                    return new EngineContext();
                }
            } else {
                return new EngineContext();
            }

        } catch (Exception ex) {
            return new EngineContext();
        }
    }

    /**
     * XML methods.
     */

    @Override
    public String importCollectionFromXml(String xmlAsString, Boolean cleanAll, Model.eImportExportModelType type) {
        StringBuilder res = new StringBuilder();
        try {
            switch (type) {
                case BOATS:
                    this.boats.importFromXml(xmlAsString, cleanAll, res);
                    break;
                case ROWERS:
                    this.rowers.importFromXml(xmlAsString, cleanAll, res);
                    break;
                case WEEKLY_ACTIVITIES:
                    this.weeklyActivities.importFromXml(xmlAsString, cleanAll, res);
                    break;
            }
        } catch (JAXBException e) {
            res = new StringBuilder("Error importing data");
        }

        return res.toString();
    }

    @Override
    public String exportCollectionToXml(String path, Model.eImportExportModelType type) {
        try {
            switch (type) {
                case BOATS:
                    return this.boats.exportToXml();
                case ROWERS:
                    return this.rowers.exportToXml();
                case WEEKLY_ACTIVITIES:
                    return this.weeklyActivities.exportToXml();
            }
        } catch (JAXBException e) {
            return "Error exporting data";
        }
        return "Error exporting data";
    }

    @Override
    public Rower getLoggedInUser(String sessionId) {
        return this.rowers.findRowerBySerialNumber(this.sessionsUsersMap.get(sessionId));
    }

    /**
     * @param rowingActivities A list of rowing activities to fix each element's references.
     * @param requests         A requestCollectionManager to find the actual requests in it.
     * @param boats            A requestCollectionManager to find the actual boat in it.
     * @throws Exception will be thrown in case the real object wasn't found in the collection managers.
     */
    private void fixRowingActivityList(List<RowingActivity> rowingActivities, RequestsCollectionManager requests, BoatsCollectionManager boats) throws Exception {
        for (RowingActivity activity : rowingActivities) {
            Request requestRef = requests.filter(request -> request.equals(activity.getRequest())).get(0);
            if (requestRef == null) {
                throw new Exception("Rowing activity request ref wasn't found.");
            }

            Boat boatRef = boats.findBySerialNumber(activity.getBoat().getSerialNumber());
            if (boatRef == null) {
                throw new Exception("Rowing activity boat ref wasn't found.");
            }

            RowingActivityModifier rowingActivityModifier = getRowingActivityModifier(activity, null);
            rowingActivityModifier.shallowSetBoat(boatRef);
            rowingActivityModifier.shallowSetRequest(requestRef);
        }
    }

    /**
     * @param requests A list of request to fix each element's references.
     * @param rowers   A rowersCollectionManager to find the actual rowers in it.
     * @throws Exception will be thrown in case the real object wasn't found in the collection managers.
     */
    private void fixRequestsList(List<Request> requests, RowersCollectionManager rowers, WeeklyActivityCollectionManager weeklyActivities) throws Exception {
        for (Request request : requests) {
            List<WeeklyActivity> tmp = weeklyActivities.filter(act -> act.equals(request.getWeeklyActivityActivity()));
            WeeklyActivity weeklyActivityRef = tmp.size() > 0 ? tmp.get(0) : null;

            Rower mainRowerRef = rowers.findRower(request.getMainRower().getEmail(), request.getMainRower().getPassword());
            if (mainRowerRef == null) {
                throw new Exception("Request main rower ref wasn't found.");
            }

            Rower creatorRef = rowers.findRower(request.getRequestCreator().getEmail(), request.getRequestCreator().getPassword());
            if (creatorRef == null) {
                throw new Exception("Request creator ref wasn't found.");
            }

            List<Rower> otherRowersListRef = new ArrayList<>();
            otherRowersListRef.addAll(rowers.filter(rower -> request.getOtherRowersList().contains(rower)));

            RequestModifier requestModifier = getRequestModifier(request, null);
            requestModifier.shallowSetMainRower(mainRowerRef);
            requestModifier.shallowSetCreator(creatorRef);
            requestModifier.shallowSetOtherRowers(otherRowersListRef);

            if (weeklyActivityRef != null) {
                requestModifier.shallowSetWeeklyActivity(weeklyActivityRef);
            }
        }
    }

    /**
     * @param boats  A list of boats to fix each element's references.
     * @param rowers A rowersCollectionManager to find the actual rowers (owner) in it.
     * @throws Exception will be thrown in case the real object wasn't found in the collection managers.
     */
    private void fixBoatsList(List<Boat> boats, RowersCollectionManager rowers) throws Exception {
        for (Boat boat : boats) {
            if (boat.hasOwner()) {
                Rower rowerRef = rowers.findRower(boat.getOwner().getEmail(), boat.getOwner().getPassword());
                if (rowerRef == null) {
                    throw new Exception("Owner real ref was not found");
                } else {
                    getBoatModifier(boat, null).shallowSetOwner(rowerRef);
                }
            }
        }
    }

    /**********************************************************************************************************/

    public void saveEngine() {
        try {
            AdaptedEngineContext adaptedEngineContext = new AdaptedEngineContext(this);
            XmlConverter.convertObjectToXML(adaptedEngineContext, SAVE_FILE_PATH);
        } catch (Exception e) {
        }
    }

    /**
     * Collection managers getters.
     */

    @Override
    public RowersCollectionManager getRowersCollectionManager() {
        return this.rowers;
    }

    @Override
    public RowingActivitiesCollectionManager getRowingActivitiesCollectionManager() {
        return this.rowingActivities;
    }

    @Override
    public RequestsCollectionManager getRequestsCollectionManager() {
        return this.requests;
    }

    @Override
    public BoatsCollectionManager getBoatsCollectionManager() {
        return this.boats;
    }

    /************************************************************************************************************/

    @Override
    public WeeklyActivityCollectionManager getWeeklyActivitiesCollectionManager() {
        return this.weeklyActivities;
    }

    /**
     * Data modifiers getters.
     */

    @Override
    public BoatModifier getBoatModifier(Boat boatToEdit, Consumer<String> callBack) {
        try (BoatModifier modifier = new BoatModifier(this, boatToEdit, this::saveBoatsCollection, callBack)) {
            return modifier;
        }
    }

    @Override
    public RowerModifier getRowerModifier(Rower rowerToEdit, Consumer<String> callBack) {
        try (RowerModifier modifier = new RowerModifier(this, rowerToEdit, this::saveRowersCollection, callBack)) {
            return modifier;
        }
    }

    @Override
    public WeeklyActivityModifier getWeeklyActivityModifier(WeeklyActivity weeklyActivityToEdit, Consumer<String> callBack) {
        try (WeeklyActivityModifier modifier = new WeeklyActivityModifier(this, weeklyActivityToEdit, this::saveWeeklyActivitiesCollection, callBack)) {
            return modifier;
        }
    }

    @Override
    public RequestModifier getRequestModifier(Request requestToEdit, Consumer<String> callBack) {
        try (RequestModifier modifier = new RequestModifier(this, requestToEdit, this::saveRequestsCollection, callBack)) {
            return modifier;
        }
    }

    @Override
    public RowingActivityModifier getRowingActivityModifier(RowingActivity rowingActivityToEdit, Consumer<String> callBack) {
        try (RowingActivityModifier modifier = new RowingActivityModifier(this, rowingActivityToEdit, this::saveRequestsCollection, callBack)) {
            return modifier;
        }
    }

    @Override
    public Triple<String, Object, Object> updateObject(Object arg, Object objectToUpdate) {
        // Client side use only.
        throw new NotImplementedException();
    }

    @Override
    public Triple<String, Object, Object> updateObject(String methodName, Object arg, Object objectToUpdate) {
        this.modifyCallback = "";
        Object result = null;
        Object afterChange = null;

        if (objectToUpdate instanceof Rower) {
            afterChange = this.rowers.find((Rower) objectToUpdate);
            result = updateRower((Rower) afterChange, methodName, arg);
        } else if (objectToUpdate instanceof Boat) {
            afterChange = this.boats.find((Boat) objectToUpdate);
            result = updateBoat((Boat) afterChange, methodName, arg);
        } else if (objectToUpdate instanceof WeeklyActivity) {
            afterChange = this.weeklyActivities.find((WeeklyActivity) objectToUpdate);
            result = updateWeeklyActivity((WeeklyActivity) afterChange, methodName, arg);
        } else if (objectToUpdate instanceof Request) {
            afterChange = this.requests.find((Request) objectToUpdate);
            result = updateRequest((Request) afterChange, methodName, arg);
        } else if (objectToUpdate instanceof RowingActivity) {
            afterChange = this.rowingActivities.find((RowingActivity) objectToUpdate);
            result = updateRowingActivity((RowingActivity) afterChange, methodName, arg);
        }

        return new Triple<>(this.modifyCallback, result, afterChange);
    }

    @Override
    public List<Rower> getCurrentLoggedInUsers() {
        Set<Rower> res = new HashSet<>();

        for (String serialNumber : this.sessionsUsersMap.values()) {
            res.add(this.rowers.findRowerBySerialNumber(serialNumber));
        }

        return new ArrayList<>(res);
    }

    @Override
    public Rower getRowerBySessionId(String sessionId) {
        return this.rowers.findRowerBySerialNumber(this.sessionsUsersMap.get(sessionId));
    }

    @Override
    public boolean isUseAlreadyLoggedIn(String sessionId) {
        return this.sessionsUsersMap.containsKey(sessionId);
    }

    private Object updateBoat(Boat originalObject, String methodName, Object arg) {
        BoatModifier boatModifier = getBoatModifier(originalObject, this::modifyCallbackHelper);
        return invokeModifierMethod(boatModifier, methodName, arg);
    }

    private Object updateWeeklyActivity(WeeklyActivity weeklyActivity, String methodName, Object arg) {
        WeeklyActivityModifier weeklyActivityModifier = getWeeklyActivityModifier(weeklyActivity, this::modifyCallbackHelper);
        return invokeModifierMethod(weeklyActivityModifier, methodName, arg);
    }

    private Object updateRowingActivity(RowingActivity originalObject, String methodName, Object arg) {
        RowingActivityModifier rowingActivityModifier = getRowingActivityModifier(originalObject, this::modifyCallbackHelper);
        return invokeModifierMethod(rowingActivityModifier, methodName, arg);
    }

    private Object updateRequest(Request originalObject, String methodName, Object arg) {
        RequestModifier requestModifier = getRequestModifier(originalObject, this::modifyCallbackHelper);
        return invokeModifierMethod(requestModifier, methodName, arg);
    }

    private Object updateRower(Rower originalObject, String methodName, Object arg) {
        RowerModifier rowerModifier = getRowerModifier(originalObject, this::modifyCallbackHelper);
        return invokeModifierMethod(rowerModifier, methodName, arg);
    }

    private void modifyCallbackHelper(String string) {
        this.modifyCallback = string;
    }

    private Object invokeModifierMethod(Modifier<?> modifier, String methodName, Object arg) {
        Object result;
        Class<?> argClass = geArgClass(arg);
        try {
            Method method = argClass != null ?
                    modifier.getClass().getDeclaredMethod(methodName, argClass) :
                    modifier.getClass().getDeclaredMethod(methodName);

            if (arg != null) {
                result = method.invoke(modifier, arg);
            } else {
                result = method.invoke(modifier);
            }

            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            modifyCallbackHelper("Error updating object");
            return null;
        }
    }


    /***********************************************************************************************************/

    private Class<?> geArgClass(Object arg) {
        if (arg != null) {
            if (arg.getClass().equals(Integer.class)) {
                return int.class;
            } else if (arg.getClass().equals(Boolean.class)) {
                return boolean.class;
            }

            return arg.getClass();
        }
        return null;
    }

    /**
     * Save data to XML functions (each collection has it's own saving method but actually each of the saves the whole engine)
     */

    @Override
    public void saveBoatsCollection() {
        saveEngine();
    }

    @Override
    public void saveRowersCollection() {
        saveEngine();
    }

    @Override
    public void saveRowingActivitiesCollection() {
        saveEngine();
    }

    @Override
    public void saveWeeklyActivitiesCollection() {
        saveEngine();
    }

    /************************************************************************************************************/

    @Override
    public void saveRequestsCollection() {
        saveEngine();
    }

    /**
     * Duplicate data checkers
     */

    @Override
    public boolean isRowerSerialNumberFree(String serial) {
        return this.rowers.isSerialNumberAvailable(serial);
    }


    /**************************************************************************************************************/

    @Override
    public boolean isBoatSerialNumberFree(String serial) {
        return this.boats.isBoatSerialNumberAvailable(serial);
    }

    /**
     * Login functions
     */


    @Override
    public Pair<Boolean, String> verifyLoginDetails(String email, String password) {
        Rower rower = this.rowers.findRower(email, password);
        if (rower == null) {
            return new Pair<>(false, "Login failed because the user doesn't exist");
        }

        return new Pair<>(true, null);
    }

    @Override
    public String login(String email, String password, String sessionId) {
        Rower rower = this.rowers.findRower(email, password);
        this.sessionsUsersMap.put(sessionId, rower.getSerialNumber());

        return rower.getSerialNumber();
    }

    @Override
    public void logout(String sessionId){
        this.sessionsUsersMap.remove(sessionId);
    }

    /**************************************************************************************************************/

    @Override
    public boolean canBoatBeRemoved(Boat boat) {
        this.rowingActivities.cloneBoatsForPastActivities();

        if (boat != null) {
            return this.rowingActivities.filter(rowingActivity -> !rowingActivity.isOver() && rowingActivity.getBoat().equals(boat)).isEmpty();
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public boolean canRowerBeRemoved(Rower rower) {
        if (rower != null) {
            this.requests.removeRowersFromPastActivities(rower);

            return this.requests.toArrayList().stream()
                    .filter(request -> request.getOtherRowersList().contains(rower) ||
                            request.getMainRower().equals(rower) ||
                            request.getRequestCreator().equals(rower))
                    .noneMatch(request -> !request.isOver() || request.isApproved());
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public boolean addObject(Object objToAdd) {
        if (objToAdd instanceof Rower) {
            return this.rowers.add((Rower) objToAdd);
        } else if (objToAdd instanceof Boat) {
            return this.boats.add((Boat) objToAdd);
        } else if (objToAdd instanceof WeeklyActivity) {
            return this.weeklyActivities.add((WeeklyActivity) objToAdd);
        } else if (objToAdd instanceof Request) {
            return this.requests.add((Request) objToAdd);
        } else if (objToAdd instanceof RowingActivity) {
            return this.rowingActivities.add((RowingActivity) objToAdd);
        }

        return false;
    }

    @Override
    public boolean removeObject(Object objToRemove) {
        if (objToRemove instanceof Rower) {
            return this.rowers.remove((Rower) objToRemove);
        } else if (objToRemove instanceof Boat) {
            return this.boats.remove((Boat) objToRemove);
        } else if (objToRemove instanceof WeeklyActivity) {
            return this.weeklyActivities.remove((WeeklyActivity) objToRemove);
        } else if (objToRemove instanceof Request) {
            return this.requests.remove((Request) objToRemove);
        } else if (objToRemove instanceof RowingActivity) {
            return this.rowingActivities.remove((RowingActivity) objToRemove);
        }

        return false;
    }

    private void createAdminUser() {
        try {
            this.rowers.shallowAdd(new Rower("AdminUserID", "Admin", (short) 25,
                    Rower.eRowerRank.PRO, "123456", true, "admin@gmail.com", "054-0000000"));
            this.rowers.shallowAdd(new Rower("notAdminId", "notAdmin", (short) 25,
                    Rower.eRowerRank.PRO, "123456", false, "notAdmin@gmail.com", "054-0000000"));
           this.boats.add(new Boat("boat1", "boatSerial1", Boat.eBoatType.DUE_SINGLE_OAR_WITH_COXWAIN, false, true, false));
           this.boats.add(new Boat("boat2", "boatSerial2", Boat.eBoatType.DUE_SINGLE_OAR_WITH_COXWAIN, true, true, true));
           this.boats.add(new Boat("boat3", "boatSerial3", Boat.eBoatType.DUE_SINGLE_OAR_WITH_COXWAIN, true, false, false));
           this.boats.add(new Boat("boat4", "boatSerial4", Boat.eBoatType.DUE_SINGLE_OAR_WITH_COXWAIN, true, true, false));
           this.boats.add(new Boat("boat5", "boatSerial5", Boat.eBoatType.DUE_SINGLE_OAR_WITH_COXWAIN, true, false, false));
            //TODO - delete not admin user
        } catch (Exception ex) {

        }
    }

    public Object find(Object objectToFind) {
        if (objectToFind instanceof Rower) {
            return this.rowers.find((Rower) objectToFind);
        } else if (objectToFind instanceof Boat) {
            return this.boats.find((Boat) objectToFind);
        } else if (objectToFind instanceof WeeklyActivity) {
            return this.weeklyActivities.find((WeeklyActivity) objectToFind);
        } else if (objectToFind instanceof Request) {
            return this.requests.find((Request) objectToFind);
        } else if (objectToFind instanceof RowingActivity) {
            return this.rowingActivities.find((RowingActivity) objectToFind);
        }

        return null;
    }
}