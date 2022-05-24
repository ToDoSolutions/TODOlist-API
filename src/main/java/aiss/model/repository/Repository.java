package aiss.model.repository;

import aiss.model.Group;
import aiss.model.Task;
import aiss.model.User;
import aiss.model.github.Owner;
import aiss.model.github.TaskGitHub;
import aiss.model.pokemon.Pokemon;

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

    void addTaskToUser(String idUser, String idTask);

    void deleteTaskToOrder(String idUser, String idTask);

    // Para Group.
    Collection<Group> getAllGroup();

    Group getGroup(String idGroup);

    void addGroup(Group group);

    void updateGroup(Group group);

    void deleteGroup(String idUser);

    Collection<User> getAllUser(String idGroup);

    void addUserToGroup(String idGroup, String idUser);

    void deleteUserToGroup(String idGroup, String idUser);

    void deleteTaskToGroup(String idGroup, String idTask);

    void addTaskToGroup(String idGroup, String idTask);

    // Para GitHub.
    TaskGitHub getRepo(String account, String repo);

    Owner getOwner(String account);
    
    TaskGitHub[] getAllRepos(String account);

    // Para pokemon.
    Pokemon getPokemon(String name);

	Pokemon[] getAllPokemons();

	
}
