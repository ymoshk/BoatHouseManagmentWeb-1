package server.utils;

import com.google.gson.Gson;
import engine.api.EngineContext;
import server.constant.Constants;
import server.constant.ePages;
import server.servlet.json.template.ErrorsList;
import server.servlet.json.template.Response;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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

    public static boolean isSessionInvalid(HttpSession session) {
        return !EngineContext.getInstance().isUseAlreadyLoggedIn(session.getId()) ||
                sessionExpired(session);
    }

    private static Map<String, LocalDateTime> getSessionExpMap(HttpSession session) {
        Map<String, LocalDateTime> res = (Map<String, LocalDateTime>) session.getServletContext()
                .getAttribute(Constants.sessionExpMap);

        return res;
    }

    public static void updateSession(HttpSession session) {
        getSessionExpMap(session).put(session.getId(), Constants.getNewSessionExpiredDate());
    }

    public static void renderLayout(HttpServletRequest req, HttpServletResponse resp, String htmlToInject, ePages activePage) throws ServletException, IOException {
        req.getServletContext().setAttribute(Constants.currentActivePagAttr, activePage);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/public/html/layoutHeader.html");
        dispatcher.include(req, resp);
        dispatcher = req.getRequestDispatcher(htmlToInject);
        dispatcher.include(req, resp);
        dispatcher = req.getRequestDispatcher("/public/html/layoutFooter.html");
        dispatcher.include(req, resp);
    }

    public static HashMap<String, String> parsePostData(HttpServletRequest req) throws IOException {
        HashMap<String, String> result;
        Gson gson = new Gson();
        try (BufferedReader reader = req.getReader()) {
            result = gson.fromJson(reader, HashMap.class);
        }
        return result;
    }

    public static String getErrorListJson(List<String> error) {
        return new Gson().toJson(new ErrorsList(false, error));
    }

    public static String getErrorJson(List<Object> data) {
        return new Gson().toJson(new Response(false, data));
    }

    public static String getSuccessJson(List<Object> data) {
        return new Gson().toJson(new Response(true, data));
    }

    public static String getSuccessJson(Boolean result) {
        return new Gson().toJson(new Response(result));
    }
}
