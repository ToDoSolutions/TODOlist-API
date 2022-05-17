package aiss.utilities.messages;

import aiss.utilities.Pair;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class Message {

    public static Response send(Response.Status status, Pair... params) {
        Map<String, String> map = new HashMap<>();
        for (Pair param : params) map.put(param.getA(), param.getB());
        return Response.status(status).entity(map).build();
    }

    private Message() {
    }
}
