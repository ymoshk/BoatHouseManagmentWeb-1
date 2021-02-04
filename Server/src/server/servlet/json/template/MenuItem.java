package server.servlet.json.template;

public class MenuItem {
    public boolean isActive;
    public String id;
    public String iconName;
    public String text;

    public MenuItem(boolean isActive, String id, String iconName, String text) {
        this.isActive = isActive;
        this.id = id;
        this.iconName = iconName;
        this.text = text;
    }
}
