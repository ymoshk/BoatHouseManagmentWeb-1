package server.utils;

import engine.api.EngineContext;
import server.constant.Constants;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;

public class Utils {

    public static String standardJsonResponse(Boolean result) {
        return standardJsonResponse(result, "");
    }

    public static String standardJsonResponse(Boolean result, String error) {
        return "{\"result\": " + result.toString().toLowerCase() + ", " +
                "\"error\": \"" + error + "\"}";
    }

    private static boolean sessionExpired(HttpSession session) {
        Map<String, LocalDateTime> map = getSessionExpMap(session);
        boolean isExpired = map.get(session.getId()) == null || map.get(session.getId())
                .isBefore(LocalDateTime.now());

        if (isExpired) {
            removeSession(session);
        }

        return isExpired;
    }

    private static void removeSession(HttpSession session) {
        getSessionExpMap(session).remove(session.getId());
        String sessionId = session.getId();
        EngineContext.getInstance().logout(sessionId);
        //session.invalidate();
    }

    public static boolean isSessionInvalid(HttpSession session){
        return !EngineContext.getInstance().isUseAlreadyLoggedIn(session.getId()) ||
                sessionExpired(session);
    }

    private static Map<String, LocalDateTime> getSessionExpMap(HttpSession session) {
        Map<String, LocalDateTime> res =  (Map<String, LocalDateTime>) session.getServletContext()
                .getAttribute(Constants.sessionExpMap);

        return res;
    }

    public static void updateSession(HttpSession session) {
        getSessionExpMap(session).put(session.getId(), Constants.getNewSessionExpiredDate());
    }
}
