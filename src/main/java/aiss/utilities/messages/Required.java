package aiss.utilities.messages;

import aiss.model.Group;
import aiss.model.Task;
import aiss.model.User;
import aiss.utilities.Pair;

import javax.ws.rs.core.Response;

public class Required {

    public static void forTask(Task task, ControllerResponse controller) {
        if (Boolean.TRUE.equals(controller.hasError())) {
        } else if (task.getTitle() == null || "".equals(task.getTitle()))
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "Title is required")));
    }

    public static void forUser(User user, ControllerResponse controller) {
        if (Boolean.TRUE.equals(controller.hasError())) {
        } else if (user.getName() == null || "".equals(user.getName()))
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The name of the user is required")));
        else if (user.getSurname() == null || "".equals(user.getSurname()))
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The surname of the user is required")));
        else if (user.getEmail() == null || "".equals(user.getEmail()))
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The email of the user is required")));
        else if (user.getTasks() != null)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The tasks of the user are not allowed")));
    }

    public static void forGroup(Group group, ControllerResponse controller) {
        if (Boolean.TRUE.equals(controller.hasError())) {
        } else if (group.getName() == null || "".equals(group.getName()))
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The name of the group is required")));
        else if (group.getUsers() != null)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The users of the group are not allowed")));
    }

    public static void haveTaskId(Task task, ControllerResponse controller) {
        if (!Boolean.TRUE.equals(controller.hasError()) && task.getIdTask() == null)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The id of the task is required")));
    }

    public static void haveUserId(User user, ControllerResponse controller) {
        if (!Boolean.TRUE.equals(controller.hasError()) && user.getIdUser() == null)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The id of the user is required")));
    }

    public static void haveGroupId(Group group, ControllerResponse controller) {
        if (!Boolean.TRUE.equals(controller.hasError()) && group.getIdGroup() == null)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The id of the group is required")));
    }

    private Required() {
    }
}
