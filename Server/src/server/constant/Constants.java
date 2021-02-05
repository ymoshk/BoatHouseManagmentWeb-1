package server.constant;

import java.time.LocalDateTime;

public class Constants {

    public static final String engineAtt  = "ENGINE_CONTEXT";
    public static String sessionExpMap = "SESSION_EXPIRED_MAP";
    private static final int sessionDuration = 7;
    public static String currentActivePage = "CURRENT_PAGE";

    public static LocalDateTime getNewSessionExpiredDate(){
        return LocalDateTime.now().plusDays(sessionDuration);
    }
}
