package server.utils;

public class Utils {

    public static String standardJsonResponse(Boolean result) {
        return standardJsonResponse(result, "");
    }

    public static String standardJsonResponse(Boolean result, String error) {
        return "{\"result\": " + result.toString().toLowerCase() + ", " +
                "\"error\": \"" + error + "\"}";
    }
}
