package server.servlet.json.template;


public class Response {
    public final boolean isSuccess;
    public final Object data;

    public Response(boolean isSuccess, Object data) {
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public Response(boolean isSuccess) {
        this.isSuccess = isSuccess;
        data = isSuccess;
    }
}
