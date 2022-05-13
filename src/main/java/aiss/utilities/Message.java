package aiss.utilities;

import aiss.model.Difficulty;
import aiss.model.Status;
import aiss.model.Task;
import aiss.model.User;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class Message {

    public static Response send(Response.Status status, Pair... params) {
        Map<String, String> map = new HashMap<>();
        for (Pair param : params) map.put(param.getA(), param.getB());
        return Response.status(status).entity(map).build();
    }

    public static Response checkTask(Task task) {
        if (task.getTitle() != null && task.getTitle().length() > 50)
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The title is too long"));
        if (task.getDescription() != null && task.getDescription().length() > 50)
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The description is too long"));
        if (task.getStatus() != null && task.getStatus() != Status.DRAFT && task.getStatus() != Status.IN_PROGRESS &&
                task.getStatus() != Status.DONE && task.getStatus() != Status.IN_REVISION && task.getStatus() != Status.CANCELLED)
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The status is not valid, it must be one of the following: DRAFT, IN_PROGRESS, DONE, IN_REVISION, CANCELLED"));
        if (task.getFinishedDate() != null && task.getStartDate() != null && task.getDuration() < 0)
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The duration is not valid, it must be a positive number"));
        if (task.getAnnotation() != null && task.getAnnotation().length() > 50)
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The annotation is too long"));
        if (task.getPriority() != null && (task.getPriority() < 0 || task.getPriority() > 5))
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The priority is not valid, it must be a number between 0 and 5"));
        if (task.getDifficulty() != null && task.getDifficulty() != Difficulty.EASY &&
                task.getDifficulty() != Difficulty.MEDIUM && task.getDifficulty() != Difficulty.HARD &&
                task.getDifficulty() != Difficulty.HARDCORE && task.getDifficulty() != Difficulty.I_WANT_TO_DIE)
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The difficulty is not valid, it must be one of the following: EASY, MEDIUM, HARD, HARDCORE, I_WANT_TO_DIE"));
        return null;
    }
    public static Response checkUser(User user) {
        if (user.getEmail() != null && !EmailValidator.getInstance().isValid(user.getEmail()))
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The email of the user is not valid"));
        if (user.getName() != null && user.getName().length() > 50)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The name of the user is not valid"));
        if (user.getSurname() != null && user.getSurname().length() > 50)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The surname of the user is not valid"));
        if (user.getAvatar() != null && !UrlValidator.getInstance().isValid(user.getAvatar()))
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The avatar of the user is not valid"));
        return null;
    }

    public static Response isTaskInUser(User user, Task task, String userId, String taskId) {
        if (user == null)
            return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The user with id=" + userId + " was not found"));

        if (task == null)
            return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The task with id=" + taskId + " was not found"));

        if (user.getTask(taskId) != null)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The task with id=" + taskId + " is already assigned to the user with id=" + userId));
        return null;
    }
}
