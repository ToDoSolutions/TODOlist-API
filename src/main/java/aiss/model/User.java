package aiss.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class User {

    // Para el getFields.
    public static final String ALL_ATTRIBUTES = "idUser,name,surname,email,avatar,bio,location,taskCompleted,tasks";

    // Atributos de la clase.
    private String idUser, name, surname, email, avatar, bio, location;
    private List<Task> tasks;

    // Constructor, crear nueva clase para disminuir parámetros (por ejemplo persona que contenga name, surname, bio y location).
    private User(String name, String surname, String email, String avatar, String bio, String location) {
        this.idUser = null;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.avatar = avatar;
        this.bio = bio;
        this.location = location;
        this.tasks = new ArrayList<>();
    }

    private User() {
    }

    public static User of(String name, String surname, String email, String avatar, String bio, String location) {
        return new User(name, surname, email, avatar, bio, location);
    }

    // Propiedades derivadas.
    public Long getTaskCompleted() {
        if (getTasks() == null)
            setTasks(new ArrayList<>());
        return getTasks().stream().filter(task -> task.getStatus().equals(Status.DONE)).count();
    }

    // Getter y setters.
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
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

    @Override
    public String toString() {
        return "User{" + "idUser=" + idUser + ", name=" + name + ", surname=" + surname + ", email=" + email + ", avatar=" + avatar + ", bio=" + bio + ", location=" + location + ", taskCompleted=" + getTaskCompleted() + ", tasks=" + tasks + '}';
    }

    public Map<String, Object> getFields(String fieldsUser, String fieldsTask) {
        List<String> attributesShown = Stream.of(fieldsUser.split(",")).map(String::trim).collect(Collectors.toList());
        Map<String, Object> map = new TreeMap<>();
        for (String attribute : attributesShown) {
            if (Objects.equals(attribute, "idUser"))
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
                map.put(attribute, getTaskCompleted());
            else if (Objects.equals(attribute, "tasks"))
                map.put(attribute, getTasks().stream().map(task -> task.getFields(fieldsTask == null ? Task.ALL_ATTRIBUTES : fieldsTask)).collect(Collectors.toList()));
        }
        return map;
    }
}
