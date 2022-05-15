package aiss.utilities;

import aiss.model.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;

import javax.ws.rs.core.Response;
import java.sql.Date;
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
        if (task.getDescription() != null && task.getDescription().length() > 200)
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The description of the task must be less than 200 characters and it's " + task.getDescription().length()));
        if (task.getStatus() != null && task.getStatus() != Status.DRAFT && task.getStatus() != Status.IN_PROGRESS &&
                task.getStatus() != Status.DONE && task.getStatus() != Status.IN_REVISION && task.getStatus() != Status.CANCELLED)
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The status of the task must be one of the following: DRAFT, IN_PROGRESS, DONE, IN_REVISION, CANCELLED"));
        if (task.getFinishedDate() != null && task.getStartDate() != null && task.getDuration() < 0)
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The duration of the task must be a positive number and it's " + task.getDuration()));
        if (task.getAnnotation() != null && task.getAnnotation().length() > 50)
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The annotation of the task must be less than 50 characters and it's " + task.getAnnotation().length()));
        if (task.getPriority() != null && (task.getPriority() < 0 || task.getPriority() > 5))
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The priority of the task is not valid, it must be a number between 0 and 5 and it's " + task.getPriority()));
        if (task.getDifficulty() != null && task.getDifficulty() != Difficulty.EASY &&
                task.getDifficulty() != Difficulty.MEDIUM && task.getDifficulty() != Difficulty.HARD &&
                task.getDifficulty() != Difficulty.HARDCORE && task.getDifficulty() != Difficulty.I_WANT_TO_DIE)
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The difficulty of the task must be one of the following: EASY, MEDIUM, HARD, HARDCORE, I_WANT_TO_DIE"));
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
                    Pair.of("message", "The name of the user must be less than 50 characters and it's " + user.getName().length()));
        if (user.getSurname() != null && user.getSurname().length() > 50)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The surname of the user must be less than 50 characters and it's " + user.getSurname().length()));
        if (user.getAvatar() != null && !UrlValidator.getInstance().isValid(user.getAvatar()))
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The avatar of the user is not valid"));
        if (user.getBio() != null && user.getBio().length() > 500)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The bio of the user must be less than 500 characters and it's " + user.getBio().length()));
        if (user.getLocation() != null && user.getLocation().length() > 50)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The location of the user must be less than 50 characters and it's " + user.getLocation().length()));
        return null;
    }

    public static Response checkGroup(Group group) {
        if (group.getName() != null && group.getName().length() > 50)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The name of the group must be less than 50 characters and it's " + group.getName().length()));
        if (group.getDescription() != null && group.getDescription().length() > 200)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The description of the group must be less than 200 characters and it's " + group.getDescription().length()));
        if (group.getCreatedDate() != null && group.getCreatedDate().before(new Date(0)))
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The created date must be before the current date ant it's " + group.getCreatedDate()));
        return null;
    }

    public static Response checkRepo(Task repo) {
        if (repo.getFinishedDate() != null && repo.getFinishedDate().before(new Date(0)))
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The finished date must be before the current date ant it's " + repo.getFinishedDate()));
        if (repo.getPriority() != null && (repo.getPriority() < 0 || repo.getPriority() > 5))
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The priority of the task is not valid, it must be a number between 0 and 5 and it's " + repo.getPriority()));
        if (repo.getStatus() != null && repo.getStatus() != Status.DRAFT && repo.getStatus() != Status.IN_PROGRESS &&
                repo.getStatus() != Status.DONE && repo.getStatus() != Status.IN_REVISION && repo.getStatus() != Status.CANCELLED)
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The status of the task must be one of the following: DRAFT, IN_PROGRESS, DONE, IN_REVISION, CANCELLED"));
        if (repo.getDifficulty() != null && repo.getDifficulty() != Difficulty.EASY &&
                repo.getDifficulty() != Difficulty.MEDIUM && repo.getDifficulty() != Difficulty.HARD &&
                repo.getDifficulty() != Difficulty.HARDCORE && repo.getDifficulty() != Difficulty.I_WANT_TO_DIE)
            return send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The difficulty of the task must be one of the following: EASY, MEDIUM, HARD, HARDCORE, I_WANT_TO_DIE"));
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

    public static Response isUserInGroup(Group group, User user, String groupId, String userId) {
        if (group == null)
            return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The group with id=" + groupId + " was not found"));
        if (user == null)
            return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The user with id=" + userId + " was not found"));
        if (group.getUser(userId) != null)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The user with id=" + userId + " is already in the group with id=" + groupId));
        return null;
    }

    public static Response requiredForTask(Task task) {
        if (task.getTitle() == null || "".equals(task.getTitle()))
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "Title is required"));
        return null;
    }

    public static Response requiredForUser(User user) {
        if (user.getName() == null || "".equals(user.getName()))
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The name of the user is required"));
        if (user.getSurname() == null || "".equals(user.getSurname()))
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The surname of the user is required"));
        if (user.getEmail() == null || "".equals(user.getEmail()))
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The email of the user is required"));
        if (user.getTasks() != null)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The tasks of the user are not allowed"));
        return null;
    }

    public static Response requiredForGroup(Group group) {
        if (group.getName() == null || "".equals(group.getName()))
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The name of the group is required"));
        if (group.getUsers() != null)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The users of the group are not allowed"));
        return null;
    }

    public static Response taskNotFound(Task task, String taskId) {
        if (task == null)
            return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The task with id=" + taskId + " was not found"));
        return null;
    }

    public static Response userNotFound(User user, String userId) {
        if (user == null)
            return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The user with id=" + userId + " was not found"));
        return null;
    }

    public static Response groupNotFound(Group group, String groupId) {
        if (group == null)
            return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The group with id=" + groupId + " was not found"));
        return null;
    }

    public static Response taskIdRequired(Task task) {
        if (task.getIdTask() == null)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The id of the task is required"));
        return null;
    }

    public static Response userIdRequired(User user) {
        if (user.getIdUser() == null)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The id of the user is required"));
        return null;
    }

    public static Response groupIdRequired(Group group) {
        if (group.getIdGroup() == null)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The id of the group is required"));
        return null;
    }
}
