package aiss.utilities.messages;

import javax.ws.rs.core.Response;

public class ControllerResponse {

    private Response fulfilled;
    private Response rejected;

    public ControllerResponse(Response fulfilled, Response rejected) {
        this.fulfilled = fulfilled;
        this.rejected = rejected;
    }

    public static ControllerResponse create() {
        return new ControllerResponse(null, null);
    }

    public Response getMessage() {
        return rejected != null ? rejected : fulfilled;
    }

    public void addError(Response... error) {
        for (int i = 0; i < error.length && rejected != null; i++) {
            if (error[i] == null) continue;
            rejected = error[i];
        }

    }

    public void addError(Response error) {
        if (rejected == null && error != null)
            rejected = error;
    }

    public void addMessage(Response message) {
        if (fulfilled == null && message != null)
            fulfilled = message;
    }

    public Boolean hasError() {
        return rejected != null;
    }

    private ControllerResponse() {
    }
}
