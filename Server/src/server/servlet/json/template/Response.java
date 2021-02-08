package server.servlet.json.template;

import java.util.List;

public class Response {
    public final boolean isSuccess;
    public final List<Object> data;

    public Response(boolean isSuccess, List<Object> data) {
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public Response(boolean isSuccess) {
        this.isSuccess = isSuccess;
        data = null;
    }
}
