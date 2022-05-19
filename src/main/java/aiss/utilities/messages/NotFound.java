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

	public static void isTaskFoundInUser(User user, Task task, ControllerResponse controller) {
        if (controller.hasError()) {} 
        else {
        	if (!user.getTasks().contains(task))
        		controller.addError(Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The task with id=" + task.getIdTask() + " was not found in the user with id=" + user.getIdUser())));
        }
	}

	public static void isUserFoundInGroup(Group group, User user, ControllerResponse controller) {
		if (controller.hasError()) {} 
		else if (!group.getUsers().contains(user))
        	controller.addError(Message.send(Response.Status.NOT_FOUND,
                   Pair.of("status", "404"),
                   Pair.of("message", "The user with id=" + user.getIdUser() + " was not found in the group with id=" + group.getIdGroup())));
	}

	public static void isTaskFoundInGroup(Group group, Task task, ControllerResponse controller) {
		if (controller.hasError()) {} 
		else if (group.getUsers().stream().noneMatch(user -> user.getTasks().contains(task))) 
        	controller.addError(Message.send(Response.Status.NOT_FOUND,
                   Pair.of("status", "404"),
                   Pair.of("message", "The task with id=" + task.getIdTask() + " was not found in any user of the group with id=" + group.getIdGroup())));
	}
}
