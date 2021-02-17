package server.utils;

import com.google.gson.Gson;
import engine.api.EngineContext;
import javafx.util.Pair;
import server.constant.Constants;
import server.constant.ePages;
import server.servlet.json.template.Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.*;
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
        String layout = readHtmlPage("/public/html/layout.html", req);
        String body = readHtmlPage(htmlToInject, req);
        layout = layout.replace("{{BODY}}", body);

        try (PrintWriter out = resp.getWriter()) {
            out.println(layout);
        }

    }

    public static void renderLayoutFromHtml(HttpServletRequest req, HttpServletResponse resp, String htmlToInject, ePages activePage) throws ServletException, IOException {
        req.getServletContext().setAttribute(Constants.currentActivePagAttr, activePage);
        String layout = readHtmlPage("/public/html/layout.html", req);
        layout = layout.replace("{{BODY}}", htmlToInject);

        try (PrintWriter out = resp.getWriter()) {
            out.println(layout);
        }
    }

    public static void errorPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderLayout(req, resp, "/public/html/errorPage.html", ePages.HOME);
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

    public static void exportHandler(HttpServletResponse resp, String xmlAsSting, String fileName) throws IOException {
        resp.setContentType("APPLICATION/OCTET-STREAM");
        resp.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xml");
        File resultFile = File.createTempFile(fileName, "xml");

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(resultFile))) {
            writer.println(xmlAsSting);
        }

        try (OutputStream outputStream = resp.getOutputStream()) {
            outputStream.write(xmlAsSting.getBytes());
            outputStream.flush();
        }
    }

    public static String readFileParts(Collection<Part> parts) throws IOException {
        StringBuilder xml = new StringBuilder();
        for (Part part : parts) {
            try (Scanner scanner = new Scanner(part.getInputStream())) {
                while (scanner.hasNextLine()) {
                    xml.append(scanner.nextLine());
                }
            }
        }
        return xml.toString();
    }

    public static Pair<Boolean, String> parsePartsRequest(HttpServletRequest req) throws IOException, ServletException {
        Collection<Part> parts = req.getParts();
        StringBuilder xml = new StringBuilder();
        Boolean deleteAll = null;

        for (Part part : parts) {
            try (Scanner scanner = new Scanner(part.getInputStream())) {
                if (part.getName().equals(Constants.deleteAllAtt)) {
                    deleteAll = Boolean.parseBoolean(scanner.nextLine());
                } else {
                    while (scanner.hasNextLine()) {
                        xml.append(scanner.nextLine());
                    }
                }
            }
        }

        return new Pair<>(deleteAll, xml.toString());
    }
}
