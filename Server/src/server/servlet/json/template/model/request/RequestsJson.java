package server.servlet.json.template.model.request;

import engine.model.activity.request.Request;
import java.util.ArrayList;
import java.util.List;


public class RequestsJson {
    public List<RequestJson> requests = new ArrayList<>();

    public RequestsJson(List<Request> requests){
        for (Request req : requests) {
            this.requests.add(new RequestJson(req));
        }
    }
}
