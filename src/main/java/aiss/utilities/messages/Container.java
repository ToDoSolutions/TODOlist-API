package aiss.utilities.messages;

import aiss.model.Group;
import aiss.model.Task;
import aiss.model.User;
import aiss.utilities.Pair;

import javax.ws.rs.core.Response;

public class Container {

    public static void isTaskInUser(User user, Task task, String userId, String taskId, ControllerResponse controller) {
        if (Boolean.TRUE.equals(controller.hasError())) {
        } else if (user == null)
            controller.addError(Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The user with id=" + userId + " was not found")));
        else if (task == null)
            controller.addError(Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The task with id=" + taskId + " was not found")));
        else if (user.getTask(taskId) != null)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The task with id=" + taskId + " is already assigned to the user with id=" + userId)));
    }

    public static void isUserInGroup(Group group, User user, String groupId, String userId, ControllerResponse controller) {
        if (Boolean.TRUE.equals(controller.hasError())) {
        } else if (group == null)
            controller.addError(Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The group with id=" + groupId + " was not found")));
        else if (user == null)
            controller.addError(Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The user with id=" + userId + " was not found")));
        else if (group.getUser(userId) != null)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The user with id=" + userId + " is already in the group with id=" + groupId)));
    }

    private Container() {
    }
}
