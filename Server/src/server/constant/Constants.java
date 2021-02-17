package server.constant;

import java.time.LocalDateTime;

public class Constants {

    public static final String engineAtt = "ENGINE_CONTEXT";
    public static final String deleteAllAtt = "deleteAll";
    private static final int sessionDuration = 7;
    public static String sessionExpMap = "SESSION_EXPIRED_MAP";
    public static String currentActivePagAttr = "CURRENT_PAGE";

    public static LocalDateTime getNewSessionExpiredDate() {
        return LocalDateTime.now().plusDays(sessionDuration);
    }
}
