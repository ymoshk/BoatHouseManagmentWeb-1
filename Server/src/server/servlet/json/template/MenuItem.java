package server.servlet.json.template;

public class MenuItem {
    public boolean isActive;
    public String id;
    public String iconName;
    public String text;
    public String href;

    public MenuItem(boolean isActive, String id, String iconName, String text, String href) {
        this.isActive = isActive;
        this.id = id;
        this.iconName = iconName;
        this.text = text;
        this.href = href;
    }
}
