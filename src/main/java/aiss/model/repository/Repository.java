package aiss.model.repository;

import aiss.model.Task;
import aiss.model.User;

import java.util.Collection;

public interface Repository {


    // Para task.
    Collection<Task> getAllTask();

    Task getTask(String idTask);

    void addTask(Task task);

    void updateTask(Task task);

    void deleteTask(String idTask);

    // Para user.
    Collection<User> getAllUser();

    User getUser(String idUser);

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(String idUser);

    Collection<Task> getAllTask(String idUser);

    void addTask(String idUser, String idTask);

    void deleteTask(String idUser, String idTask);
}
