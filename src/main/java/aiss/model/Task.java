package aiss.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task {


    // Por favor en private, no en public (para algo están los setters, constructores y métodos de factoría).
    private String idTask;
    private String description;
    private Status status;
    private enum Status {
        DRAFT,
        IN_PROGRESS,
        IN_REVISION,
        DONE,
        CANCELED
    }
    private String finishedDate;
    private String startedDate;

    private String annotation;
    private Integer priority;
    private Difficulty difficulty;
    private enum Difficulty {
        SLEEP,
        EASY,
        MEDIUM,
        HARD,
        HARDCORE,
        I_WANT_TO_DIE
    }



    // Por mí se quita este constructor, pero viene en las prácticas.
    // Además, sugiero que se utilicen constructores en condiciones y métodos de factoría.
    private Task(String idTask, String description, Status status, String finishedDate, String startedDate, String annotation, Integer priority, Difficulty difficulty) {
        this.idTask = idTask;
        this.description = description;
        this.status = status;
        this.finishedDate = finishedDate;
        this.startedDate = startedDate;
        this.annotation = annotation;
        this.priority = priority;
        this.difficulty = difficulty;
    }

    // Método de factoría para crear un objeto Task.
    public static Task of(String idTask, String description, Status status, String finishedDate, String startedDate, String annotation, Integer priority, Difficulty difficulty) {
        return new Task(idTask, description, status, finishedDate, startedDate, annotation, priority, difficulty);
    }

    public static Task of(String description, Status status, String finishedDate, String startedDate, String annotation, Integer priority, Difficulty difficulty) {
        return new Task(null, description, status, finishedDate, startedDate, annotation, priority, difficulty);
    }

    // Crear un constructor en condiciones y un método de factoría, please.

    // Por desgracia es necesario.
    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public String getIdTask() {
        return idTask;
    }

    // Poner getters y setters
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

    public String getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(String finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getReleaseDate() {
        return startedDate;
    }

    public void setReleaseDate(String releaseDate) {
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

    @Override
    public String toString() {
        return "Task{" + "idTask=" + idTask + ", description=" + description + ", status=" + status + ", finishedDate=" + finishedDate + ", startedDate=" + startedDate + ", annotation=" + annotation + ", priority=" + priority + ", difficulty=" + difficulty + '}';
    }

    public Map<String, String> getFields(String fields) {
        List<String> attributes = Stream.of(fields.split(",")).map(String::trim).collect(Collectors.toList());
        Map<String, String> map = new HashMap<>();
        for (String attribute : attributes) {
            if (Objects.equals(attribute, "id"))
                map.put(attribute, getIdTask());
            else if (Objects.equals(attribute, "description"))
                map.put(attribute, getDescription());
            else if (Objects.equals(attribute, "status"))
                map.put(attribute, getStatus().toString());
            else if (Objects.equals(attribute, "finishedDate"))
                map.put(attribute, getFinishedDate());
            else if (Objects.equals(attribute, "startedDate"))
                map.put(attribute, getReleaseDate());
            else if (Objects.equals(attribute, "annotation"))
                map.put(attribute, getAnnotation());
            else if (Objects.equals(attribute, "priority"))
                map.put(attribute, getPriority().toString());
            else if (Objects.equals(attribute, "difficulty"))
                map.put(attribute, getDifficulty().toString());
        }
        return map;
    }

    private Task() {
    }
}