package server.servlet.json.template;

import java.util.List;

public class Response {
    public final boolean isSuccess;
    public final List<Object> values;

    public Response(boolean isSuccess, List<Object> values) {
        this.isSuccess = isSuccess;
        this.values = values;
    }
}
