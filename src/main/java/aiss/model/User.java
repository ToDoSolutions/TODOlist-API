package aiss.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class User {

    private final static String attributes = "isUser,id,name,surname,email,avatar,bio,location,taskCompleted,tasks";

    private String idUser;
    private String name;
    private String surname;
    private String email;
    private String avatar; // se podrá hacer con dalle.
    private String bio;
    private String location;
    private Integer taskCompleted;
    private List<Task> tasks;

    public User(String idUser, String name, String surname, String email, String avatar, String bio, String location, List<Task> tasks) {
        this.idUser = idUser;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.avatar = avatar;
        this.bio = bio;
        this.location = location;
        this.taskCompleted = 0;
        this.tasks = tasks;
    }

    public static User of(String name, String surname, String email, String avatar, String bio, String location) {
        return new User(null, name, surname, email, avatar, bio, location, new ArrayList<>());
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTaskCompleted() {
        return taskCompleted;
    }

    public void setTaskCompleted(Integer taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(String id) {
        if (tasks == null)
            return null;

        Task task = null;
        for (Task t : tasks) {
            if (t.getIdTask().equals(id)) {
                task = t;
                break;
            }
        }
        return task;
    }


    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    // Para modificar tasks.
    public void addTask(Task t) {
        if (tasks == null)
            tasks = new ArrayList<>();
        tasks.add(t);
    }

    public void deleteTask(Task t) {
        tasks.remove(t);
    }

    public void deleteTask(String id) {
        Task t = getTask(id);
        if (t != null)
            tasks.remove(t);
    }

    public static String getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "User{" + "idUser=" + idUser + ", name=" + name + ", surname=" + surname + ", email=" + email + ", avatar=" + avatar + ", bio=" + bio + ", location=" + location + ", taskCompleted=" + taskCompleted + ", tasks=" + tasks + '}';
    }

    public Map<String, String> getFields(String fields) {
        List<String> attributes = Stream.of(fields.split(",")).map(String::trim).collect(Collectors.toList());
        Map<String, String> map = new HashMap<>();
        for (String attribute : attributes) {
            if (Objects.equals(attribute, "id"))
                map.put(attribute, getIdUser());
            else if (Objects.equals(attribute, "name"))
                map.put(attribute, getName());
            else if (Objects.equals(attribute, "surname"))
                map.put(attribute, getSurname());
            else if (Objects.equals(attribute, "email"))
                map.put(attribute, getEmail());
            else if (Objects.equals(attribute, "avatar"))
                map.put(attribute, getAvatar());
            else if (Objects.equals(attribute, "bio"))
                map.put(attribute, getBio());
            else if (Objects.equals(attribute, "location"))
                map.put(attribute, getLocation());
            else if (Objects.equals(attribute, "taskCompleted"))
                map.put(attribute, getTaskCompleted().toString());
            else if (Objects.equals(attribute, "tasks"))
                map.put(attribute, getTasks().toString());
        }
        return map;
    }

    private User() {
    }
}
