package aiss.utilities;

import aiss.model.Group;
import aiss.model.Task;
import aiss.model.User;

import java.util.Comparator;
import java.util.List;

public class Order {

    public static void sequenceTask(List<Task> result, String order) {
        if (order.equals("idTask"))
            result.sort(Comparator.comparing(Task::getIdTask));
        else if (order.equals("-idTask"))
            result.sort(Comparator.comparing(Task::getIdTask));
        else if (order.equals("title"))
            result.sort(Comparator.comparing(Task::getTitle));
        else if (order.equals("-title"))
            result.sort(Comparator.comparing(Task::getTitle).reversed());
        else if (order.equals("status"))
            result.sort(Comparator.comparing(Task::getStatus));
        else if (order.equals("-status"))
            result.sort(Comparator.comparing(Task::getStatus).reversed());
        else if (order.equals("releaseDate"))
            result.sort(Comparator.comparing(Task::getStartDate));
        else if (order.equals("-releaseDate"))
            result.sort(Comparator.comparing(Task::getStartDate).reversed());
        else if (order.equals("finishedDate"))
            result.sort(Comparator.comparing(Task::getFinishedDate));
        else if (order.equals("-finishedDate"))
            result.sort(Comparator.comparing(Task::getFinishedDate).reversed());
        else if (order.equals("priority"))
            result.sort(Comparator.comparing(Task::getPriority));
        else if (order.equals("-priority"))
            result.sort(Comparator.comparing(Task::getPriority).reversed());
        else if (order.equals("difficulty"))
            result.sort(Comparator.comparing(Task::getDifficulty));
        else if (order.equals("-difficulty"))
            result.sort(Comparator.comparing(Task::getDifficulty).reversed());
        else if (order.equals("duration"))
            result.sort(Comparator.comparing(Task::getDuration));
        else if (order.equals("-duration"))
            result.sort(Comparator.comparing(Task::getDuration).reversed());
    }

    public static void sequenceUser(List<User> result, String order) {
        if (order.equals("idUser"))
            result.sort(Comparator.comparing(User::getIdUser));
        else if (order.equals("-idUser"))
            result.sort(Comparator.comparing(User::getIdUser).reversed());
        else if (order.equals("name"))
            result.sort(Comparator.comparing(User::getName));
        else if (order.equals("-name"))
            result.sort(Comparator.comparing(User::getName).reversed());
        else if (order.equals("surname"))
            result.sort(Comparator.comparing(User::getSurname));
        else if (order.equals("-surname"))
            result.sort(Comparator.comparing(User::getSurname).reversed());
        else if (order.equals("email"))
            result.sort(Comparator.comparing(User::getEmail));
        else if (order.equals("-email"))
            result.sort(Comparator.comparing(User::getEmail).reversed());
        else if (order.equals("location"))
            result.sort(Comparator.comparing(User::getLocation));
        else if (order.equals("-location"))
            result.sort(Comparator.comparing(User::getLocation).reversed());
        else if (order.equals("taskCompleted"))
            result.sort(Comparator.comparing(User::getTaskCompleted));
        else if (order.equals("-taskCompleted"))
            result.sort(Comparator.comparing(User::getTaskCompleted).reversed());
    }

    public static void sequenceGroup(List<Group> result, String order) {
        if (order.equals("idGroup"))
            result.sort(Comparator.comparing(Group::getIdGroup));
        else if (order.equals("-idGroup"))
            result.sort(Comparator.comparing(Group::getIdGroup).reversed());
        else if (order.equals("name"))
            result.sort(Comparator.comparing(Group::getName));
        else if (order.equals("-name"))
            result.sort(Comparator.comparing(Group::getName).reversed());
        else if (order.equals("description"))
            result.sort(Comparator.comparing(Group::getDescription));
        else if (order.equals("-description"))
            result.sort(Comparator.comparing(Group::getDescription).reversed());
        else if (order.equals("numTask"))
            result.sort(Comparator.comparing(Group::getNumTasks));
        else if (order.equals("-numTask"))
            result.sort(Comparator.comparing(Group::getNumTasks).reversed());
    }

    private Order() {
    }
}
