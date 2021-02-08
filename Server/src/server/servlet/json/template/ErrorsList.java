package server.servlet.json.template;

import java.util.List;

public class ErrorsList {
    public Boolean result;
    public List<String> error;

    public ErrorsList(Boolean result, List<String> error) {
        this.result = result;
        this.error = error;
    }
}
