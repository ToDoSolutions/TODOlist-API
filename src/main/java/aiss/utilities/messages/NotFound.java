package aiss.utilities.messages;

import aiss.model.Group;
import aiss.model.Task;
import aiss.model.User;
import aiss.utilities.Pair;

import javax.ws.rs.core.Response;

public class NotFound {

    public static void isTaskFound(Task task, String taskId, ControllerResponse controller) {
        if (task == null)
            controller.addError(Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The task with id=" + taskId + " was not found")));
    }

    public static void isUserFound(User user, String userId, ControllerResponse controller) {
        if (user == null)
            controller.addError(Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The user with id=" + userId + " was not found")));
    }

    public static void isGroupFound(Group group, String groupId, ControllerResponse controller) {
        if (group == null)
            controller.addError(Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The group with id=" + groupId + " was not found")));
    }

    private NotFound() {
    }
}
