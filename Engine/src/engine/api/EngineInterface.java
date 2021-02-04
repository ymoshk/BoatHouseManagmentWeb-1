package engine.api;


import engine.database.collection.*;
import engine.model.Model;
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
import engine.utils.data.structure.Triple;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface EngineInterface {
    /**
     * @param email    user email address (string)
     * @param password user password (string)
     * @return true if a user with the given email and password is found within the system users list
     */
    Pair<Boolean, String> verifyLoginDetails(String email, String password);

    /**
     * @param email    user email address (string)
     * @param password user password (string)
     */
    String login(String email, String password, String SessionId);

    /**
     * @param sessionId user serial number (string)
     */
    void logout(String sessionId);


    String importCollectionFromXml(String xmlAsString, Boolean cleanAll, Model.eImportExportModelType type);

    String exportCollectionToXml(String path, Model.eImportExportModelType type);

    /**
     * @return the current logged in user.
     */
    Rower getLoggedInUser(String sessionId);


    /**
     * @return a BoatsCollectionManager from the system.
     */
    BoatsCollectionManager getBoatsCollectionManager();

    /**
     * @return a RowersCollectionManager from the system.
     */
    RowersCollectionManager getRowersCollectionManager();


    /**
     * @return a SchedulerCollectionManager from the system.
     */
    RowingActivitiesCollectionManager getRowingActivitiesCollectionManager();

    /**
     * @return a RequestsCollectionManager from the system.
     */
    RequestsCollectionManager getRequestsCollectionManager();


    /**
     * @param boat to check if it can be removed
     * @return true if the given boat can be removed from the system.
     * False can be returned if the given boat takes a part in the club activities.
     */
    boolean canBoatBeRemoved(Boat boat);

    /**
     * @param rower to check if it can be removed
     * @return true if the given rower can be removed from the system.
     * False can be returned if the given rower takes a part in the club activities.
     */
    boolean canRowerBeRemoved(Rower rower);


    /**
     * @param objToAdd Add a new generic object to the system.
     */
    boolean addObject(Object objToAdd);

    /**
     * @param objToRemove remove a generic object from the system.
     */
    boolean removeObject(Object objToRemove);

    /**
     * @param serial the serial number string to check.
     * @return true if the given string can be used as a boat serial number.
     */
    boolean isBoatSerialNumberFree(String serial);

    /**
     * Saves the engine instance to a binary file.
     *
     * @throws IOException will be thrown if the data file wasn't found or couldn't be opened for writing.
     */
    void saveEngine() throws IOException;


    /**
     * @param serial the serial number string to check.
     * @return true if the given string can be used as a rower serial number.
     */
    boolean isRowerSerialNumberFree(String serial);


    /**
     * @return a WeeklyActivitiesCollectionManager from the system.
     */
    WeeklyActivityCollectionManager getWeeklyActivitiesCollectionManager();


    /**
     * @param boatToEdit a reference to a boat that we want to edit.
     * @param callBack   a function that receive string and return nothing to invoke after editing any data.
     *                   The call back function will receive an information about successful change or about an error.
     * @return BoatModifier object that has access to Boat class's setters.
     */
    BoatModifier getBoatModifier(Boat boatToEdit, Consumer<String> callBack);

    /**
     * @param rowerToEdit a reference to a rower that we want to edit.
     * @param callBack    a function that receive string and return nothing to invoke after editing any data.
     *                    The call back function will receive an information about successful change or about an error.
     * @return BoatModifier object that has access to Rower class's setters.
     */
    RowerModifier getRowerModifier(Rower rowerToEdit, Consumer<String> callBack);

    /**
     * @param weeklyActivityToEdit a reference to a weekly activity that we want to edit.
     * @param callBack             a function that receive string and return nothing to invoke after editing any data.
     *                             The call back function will receive an information about successful change or about an error.
     * @return WeeklyActivityModifier object that has access to Rower class's setters.
     */
    WeeklyActivityModifier getWeeklyActivityModifier(WeeklyActivity weeklyActivityToEdit, Consumer<String> callBack);

    /**
     * @param requestToEdit a reference to a request that we want to edit.
     * @param callBack      a function that receive string and return nothing to invoke after editing any data.
     *                      The call back function will receive an information about successful change or about an error.
     * @return RequestModifier object that has access to Rower class's setters.
     */
    RequestModifier getRequestModifier(Request requestToEdit, Consumer<String> callBack);

    /**
     * @param rowingActivityToEdit a reference to a request that we want to edit.
     * @param callBack             a function that receive string and return nothing to invoke after editing any data.
     *                             The call back function will receive an information about successful change or about an error.
     * @return SchedulerModifier object that has access to Rower class's setters.
     */
    RowingActivityModifier getRowingActivityModifier(RowingActivity rowingActivityToEdit, Consumer<String> callBack);


    /**
     * Save the engine boats collection to a XML file.
     */
    void saveBoatsCollection();

    /**
     * Save the engine rowers collection to a XML file.
     */
    void saveRowersCollection();

    /**
     * Save the engine schedulers collection to a XML file.
     */
    void saveRowingActivitiesCollection();

    /**
     * Save the engine weekly activities collection to a XML file.
     */
    void saveWeeklyActivitiesCollection();

    /**
     * Save the engine requests collection to a XML file.
     */
    void saveRequestsCollection();

    Object find(Object objectToFind);

    Triple<String, Object, Object> updateObject(Object arg, Object objectToUpdate); // For client side use only

    Triple<String, Object, Object> updateObject(String methodName, Object arg, Object objectToUpdate);

    List<Rower> getCurrentLoggedInUsers();

    boolean isUseAlreadyLoggedIn(String sessionId);
}
