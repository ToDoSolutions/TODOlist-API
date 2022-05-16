package aiss.api.resources;

import aiss.model.Difficulty;
import aiss.model.Status;
import aiss.model.Task;
import aiss.model.User;
import aiss.model.repository.MapRepository;
import aiss.model.repository.Repository;
import aiss.utilities.Filter;
import aiss.utilities.Message;
import aiss.utilities.Order;
import aiss.utilities.Update;

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
@Produces("application/json")
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
    public Response getTask(@PathParam("taskId") String taskId, @QueryParam("fields") String fields) {
        Task task = repository.getTask(taskId);

        // Comprobamos si se encuentra el objeto en la base de datos chapucera.
        Response response = Message.taskNotFound(task, taskId);
        if (response != null) return response;

        return Response.ok(task.getFields(fields == null ? Task.ALL_ATTRIBUTES : fields)).build();
    }

    @POST
    @Consumes("application/json")
    public Response addTask(@Context UriInfo uriInfo, Task task) {
        // Comprobamos aquellos campos obligatorios.
        Response response = Message.requiredForTask(task);
        if (response != null) return response;

        // Comprobamos si los campos son correctos.
        response = Message.checkTask(task);
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
    public Response updateTask(Task task) {
        // Comprobamos que nos ha dado una id.
        Response response = Message.taskIdRequired(task);
        if (response != null) return response;

        Task oldTask = repository.getTask(task.getIdTask());

        // Comprobamos si se encuentra el objeto en la base de datos chapucera.
        response = Message.taskNotFound(oldTask, task.getIdTask());
        if (response != null) return response;

        // Comprobamos si los campos son correctos.
        response = Message.checkTask(task);
        if (response != null) return response;

        Update.taskFromOther(task, oldTask); // Actualiza los atributos del modelo.

        repository.updateTask(oldTask);  // No está en la práctica 7, pero deduzco que falta, ya que de la otra manera no se actualiza en la base de datos chapucera.

        return Response.noContent().build();
    }


    @DELETE
    @Path("/{taskId}")
    public Response deleteTask(@PathParam("taskId") String taskId) {
        Task toBeRemoved = repository.getTask(taskId); // Obtiene la tarea a eliminar de la base de datos chapucera.

        // Comprobamos si se encuentra el objeto en la base de datos chapucera.
        Response response = Message.taskNotFound(toBeRemoved, taskId);
        if (response != null) return response;

        // Elimina la tarea de la base de datos chapucera.
        repository.deleteTask(taskId);

        // Elimina las tareas de los usuarios.
        for (User user : repository.getAllUser())
            user.deleteTask(toBeRemoved);

        return Response.noContent().build();
    }
}



