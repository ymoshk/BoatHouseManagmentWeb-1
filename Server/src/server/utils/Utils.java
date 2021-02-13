package server.utils;

import com.google.gson.Gson;
import engine.api.EngineContext;
import server.constant.Constants;
import server.constant.ePages;
import server.servlet.json.template.Response;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;

public class Utils {


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

    public static void renderLayoutString(HttpServletRequest req, HttpServletResponse resp, String htmlToInject, ePages activePage) throws ServletException, IOException {
        req.getServletContext().setAttribute(Constants.currentActivePagAttr, activePage);

        String layoutHeader = readHtmlPage("/public/html/layoutHeader.html", req);
        String layoutFooter = readHtmlPage("/public/html/layoutFooter.html", req);
        String result = String.format("%s%n%s%n%s", layoutHeader, htmlToInject, layoutFooter);

        try (PrintWriter out = resp.getWriter()) {
            out.println(result);
        }
    }

    public static void errorPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getServletContext().setAttribute(Constants.currentActivePagAttr, ePages.HOME);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/public/html/layoutHeader.html");
        dispatcher.include(req, resp);
        dispatcher = req.getRequestDispatcher("/public/html/errorPage.html");
        dispatcher.include(req, resp);
        dispatcher = req.getRequestDispatcher("/public/html/layoutFooter.html");
        dispatcher.include(req, resp);
    }

    public static HashMap<String, String> parsePostData(HttpServletRequest req) throws IOException {
        HashMap<String, String> result = new HashMap<>();
        HashMap<String, String> temp;
        Gson gson = new Gson();
        try (BufferedReader reader = req.getReader()) {
            temp = gson.fromJson(reader, HashMap.class);
        }

        temp.forEach((key, value) -> result.put(key, value.trim()));

        return result;
    }


    // For ajax pop up results
    public static String createJsonErrorsListObject(List<String> data) {
        return new Gson().toJson(new Response(false, data));
    }


    // For ajax pop up results
    public static String createJsonErrorObject(Object data) {
        return new Gson().toJson(new Response(false, data));
    }

    // For ajax pop up results
    public static String createJsonSuccessObject(Object data) {
        return new Gson().toJson(new Response(true, data));
    }

    // For ajax pop up results
    public static String createJsonSuccessObject(Boolean result) {
        return new Gson().toJson(new Response(result));
    }

    public static String readHtmlPage(String path, HttpServletRequest req) {
        InputStream stream = req.getServletContext().getResourceAsStream(path);
        Scanner scanner = new Scanner(stream);
        StringBuilder result = new StringBuilder();
        while (scanner.hasNextLine()) {
            result.append(scanner.nextLine());
        }
        return result.toString();
    }

    public static List<String> splitsNotes(String notes) {
        if (notes.length() == 0) {
            return null;
        }
        List<String> temp = Arrays.asList(notes.split(String.valueOf('\n')).clone());
        List<String> res = new ArrayList<>();
        temp.forEach(str -> res.add(str.trim()));
        return res;
    }
}
