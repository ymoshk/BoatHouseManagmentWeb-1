package server.constant;

public enum ePages {
    ROWERS("Rowers"),
    HOME("Home"),
    PERSONAL_DETAILS("Personal Details"),
    BOATS("Boats"),
    WEEKLY_ACTIVITIES("Weekly Activities"),
    REQUESTS("Requests"),
    ROWING_ACTIVITY("Rowing Activities"),
    MANAGE_DATA("Manage Data");

    private final String title;

    ePages(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
