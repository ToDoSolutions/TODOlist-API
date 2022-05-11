package aiss.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task {


    // Para el getFields.
    public static final String ALL_ATTRIBUTES = "idTask,title,description,status,finishedDate,startedDate,annotation,priority,difficulty,duration";
    // Atributos de la clase.
    private String idTask, title, description, annotation;
    private Status status;
    private Date finishedDate, startedDate;
    private Integer priority;
    private Difficulty difficulty;

    // Constructor, crear nueva clase para disminuir parámetros (ni idea).
    private Task(String title, String description, Status status, Date startedDate, Date finishedDate, String annotation, Integer priority, Difficulty difficulty) {
        this.idTask = null;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startedDate = startedDate;
        this.finishedDate = finishedDate;
        this.annotation = annotation;
        this.priority = priority;
        this.difficulty = difficulty;
    }

    private Task() {
    }

    // Método de factoría para crear un objeto Task.
    public static Task of(String title, String description, Status status, Date startedDate, Date finishedDate, String annotation, Integer priority, Difficulty difficulty) {
        return new Task(title, description, status, startedDate, finishedDate, annotation, priority, difficulty);
    }

    // Métodos derivados
    public Long getDuration() {
        return (finishedDate.getTime() - startedDate.getTime()) / (1000 * 60 * 60 * 24);
    }

    public String getIdTask() {
        return idTask;
    }

    // Getters y setters
    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public Date getReleaseDate() {
        return startedDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.startedDate = releaseDate;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Date getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    @Override
    public String toString() {
        return "Task{" + "idTask=" + idTask + ", description=" + description + ", status=" + status + ", finishedDate=" + finishedDate + ", startedDate=" + startedDate + ", annotation=" + annotation + ", priority=" + priority + ", difficulty=" + difficulty + '}';
    }

    public Map<String, Object> getFields(String fields) {
        List<String> attributes = Stream.of(fields.split(",")).map(String::trim).collect(Collectors.toList());
        Map<String, Object> map = new TreeMap<>();
        for (String attribute : attributes) {
            if (Objects.equals(attribute, "idTask"))
                map.put(attribute, getIdTask());
            else if (Objects.equals(attribute, "title"))
                map.put(attribute, getTitle());
            else if (Objects.equals(attribute, "description"))
                map.put(attribute, getDescription());
            else if (Objects.equals(attribute, "status"))
                map.put(attribute, getStatus());
            else if (Objects.equals(attribute, "finishedDate"))
                map.put(attribute, getFinishedDate());
            else if (Objects.equals(attribute, "startedDate"))
                map.put(attribute, getReleaseDate());
            else if (Objects.equals(attribute, "annotation"))
                map.put(attribute, getAnnotation());
            else if (Objects.equals(attribute, "priority"))
                map.put(attribute, getPriority());
            else if (Objects.equals(attribute, "difficulty"))
                map.put(attribute, getDifficulty());
            else if (Objects.equals(attribute, "duration"))
                map.put(attribute, getDuration());
        }
        return map;
    }
}