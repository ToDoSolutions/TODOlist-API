package aiss.api.resources;

import aiss.model.Difficulty;
import aiss.model.Status;
import aiss.model.Task;
import aiss.model.repository.MapRepository;
import aiss.model.repository.Repository;
import aiss.utilities.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/tasks")
public class TaskResource {

    protected static TaskResource instance = null; // La instancia inicialmente no existe, se crea al ejecutar .getInstance().
    final Repository repository; // Para poder trabajar con los datos

    private TaskResource() {
        repository = MapRepository.getInstance();
    }

    public static TaskResource getInstance() {
        // Creamos una instancia si no existe.
        instance = (instance == null) ? new TaskResource() : instance;
        return instance;
    }

    @GET
    @Produces("application/json")
    public Response getAllTasks(@QueryParam("order") String order,
                                @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                                @QueryParam("fields") String fields, @QueryParam("title") String title,
                                @QueryParam("status") String status, @QueryParam("startDate") String startDate,
                                @QueryParam("finishedDate") String finishedDate, @QueryParam("priority") String priority,
                                @QueryParam("difficulty") String difficulty, @QueryParam("duration") String duration) {
        List<Task> result = new ArrayList<>(), tasks = new ArrayList<>(repository.getAllTask()); // No se puede utilizar .toList() porque eso es a partir de Java 16.
        if (order != null)
            Order.sequenceTask(result, order);
        Status auxStatus = status != null ? Status.parse(status) : null;
        Difficulty auxDifficulty = difficulty != null ? Difficulty.parse(difficulty) : null;
        int start = offset == null ? 0 : offset - 1; // Donde va a comenzar.
        int end = limit == null || limit > tasks.size() ? tasks.size() : start + limit; // Donde va a terminar.
        for (int i = start; i < end; i++) {
            Task task = tasks.get(i);
            if (task != null &&
                    (title == null || task.getTitle().contains(title)) &&
                    (auxStatus == null || task.getStatus() == auxStatus) &&
                    (startDate == null || Filter.isGEL(task.getStartDate(), startDate)) &&
                    (finishedDate == null || Filter.isGEL(task.getFinishedDate(), finishedDate)) &&
                    (priority == null || Filter.isGEL((long) task.getPriority(), priority)) &&
                    (auxDifficulty == null || task.getDifficulty() == auxDifficulty) &&
                    (duration == null || Filter.isGEL(task.getDuration(), duration)))
                result.add(task);
        }


        // fields lo hemos dado en teoría, pero no en práctica, quizás en vez de esto sea con un Response.
        return Response.ok(result.stream().map(task -> task.getFields(fields == null ? Task.ALL_ATTRIBUTES : fields)).collect(Collectors.toList())).build();

    }


    @GET
    @Path("/{taskId}")
    @Produces("application/json")
    public Response getTask(@PathParam("taskId") String taskId, @QueryParam("fields") String fields) {
        Task task = repository.getTask(taskId);

        // Comprobamos si se encuentra el objeto en la base de datos chapucera.
        if (task == null)
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "Task not found"));
        return Response.ok(task.getFields(fields == null ? Task.ALL_ATTRIBUTES : fields)).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addTask(@Context UriInfo uriInfo, Task task) {
        // Comprueba contiene algún tipo de error.
        if (task.getTitle() == null || "".equals(task.getTitle()))
            return Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "Title is required"));

        // Comprobamos si los campos son correctos.
        Response response = Message.checkTask(task);
        if (response != null) return response;

        repository.addTask(task); // Añadimos la tarea a la base de datos.

        // Builds the response. Returns the task the has just been added.
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "getTask");
        URI uri = ub.build(task.getIdTask());
        ResponseBuilder resp = Response.created(uri);
        resp.entity(task);
        return resp.build();
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateTask(Task task) {
        Task oldTask = repository.getTask(task.getIdTask());

        // Comprobamos si se encuentra el objeto en la base de datos chapucera.
        if (oldTask == null)
            return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The task with id=" + task.getIdTask() + " was not found"));

        // Comprobamos si los campos son correctos.
        Response response = Message.checkTask(task);
        if (response != null) return response;

        Update.taskFromOther(task, oldTask); // Actualiza los atributos del modelo.

        repository.updateTask(oldTask);  // No está en la práctica 7, pero deduzco que falta, ya que de la otra manera no se actualiza en la base de datos chapucera.

        return Response.noContent().build();
    }


    @DELETE
    @Path("/{taskId}")
    @Produces("application/json")
    public Response deleteTask(@PathParam("taskId") String taskId) {
        Task toBeRemoved = repository.getTask(taskId); // Obtiene la tarea a eliminar de la base de datos chapucera.

        // Comprobamos si se encuentra el objeto en la base de datos chapucera.
        if (toBeRemoved == null)
            return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The task with id=" + taskId + " was not found"));
            // Si no Elimina la tarea de la base de datos chapucera.
        else
            repository.deleteTask(taskId);

        return Response.noContent().build();
    }
}



