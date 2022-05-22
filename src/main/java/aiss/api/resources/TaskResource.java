package aiss.api.resources;

import aiss.model.*;
import aiss.model.repository.MapRepository;
import aiss.model.repository.Repository;
import aiss.utilities.Filter;
import aiss.utilities.Order;
import aiss.utilities.Update;
import aiss.utilities.messages.Checker;
import aiss.utilities.messages.ControllerResponse;
import aiss.utilities.messages.NotFound;
import aiss.utilities.messages.Required;

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
        instance = (instance == null) ? new TaskResource() : instance; // Creamos una instancia si no existe.
        return instance;
    }

    @GET
    public Response getAllTasks(@QueryParam("order") String order,
                                @QueryParam("limit") String limit, @QueryParam("offset") String offset,
                                @QueryParam("fields") String fields, @QueryParam("title") String title,
                                @QueryParam("status") String status, @QueryParam("startDate") String startDate,
                                @QueryParam("finishedDate") String finishedDate, @QueryParam("priority") String priority,
                                @QueryParam("difficulty") String difficulty, @QueryParam("duration") String duration) {
        List<Task> result = new ArrayList<>(), tasks = new ArrayList<>(repository.getAllTask()); // No se puede utilizar .toList() porque eso es a partir de Java 16.
        ControllerResponse controller = ControllerResponse.create();
        Integer auxLimit = Checker.isNumberCorrect(limit, controller);
        Integer auxOffset = Checker.isNumberCorrect(offset, controller);
        Status auxStatus = Checker.isStatusCorrect(status, controller);
        Difficulty auxDifficulty = Checker.isDifficultyCorrect(difficulty, controller);
        Checker.isParamGELDate(startDate, controller); // Comprobamos formato de fecha de inicio.
        Checker.isParamGELDate(finishedDate, controller); // Comprobamos formato de fecha de fin.
        Checker.isParamGELNumber(priority, controller);
        Checker.isParamGELNumber(duration, controller);
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();
        if (order != null)
            Order.sequenceTask(result, order);
        int start = offset == null || auxOffset < 1 ? 0 : auxOffset - 1; // Donde va a comenzar.
        int end = limit == null || auxLimit > tasks.size() ? tasks.size() : start + auxLimit; // Donde va a terminar.
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
        
        return Response.ok(result.stream().map(task -> task.getFields(fields == null ? Task.ALL_ATTRIBUTES : fields)).collect(Collectors.toList())).build();

    }


    @GET
    @Path("/{taskId}")
    public Response getTask(@PathParam("taskId") String taskId, @QueryParam("fields") String fields) {
        Task task = repository.getTask(taskId);
        ControllerResponse controller = ControllerResponse.create();

        NotFound.isTaskFound(task, taskId, controller); // Comprobamos si se encuentra el objeto en la base de datos chapucera.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        return Response.ok(task.getFields(fields == null ? Task.ALL_ATTRIBUTES : fields)).build();
    }

    @POST
    @Consumes("application/json")
    public Response addTask(@Context UriInfo uriInfo, Task task) {
        ControllerResponse controller = ControllerResponse.create();

        Required.forTask(task, controller); // Comprobamos aquellos campos obligatorios.
        Checker.isTaskCorrect(task, controller); // Comprobamos si los campos son correctos.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

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
        ControllerResponse controller = ControllerResponse.create();

        Required.haveTaskId(task, controller); // Comprobamos que nos ha dado una id.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        Task oldTask = repository.getTask(task.getIdTask());

        NotFound.isTaskFound(oldTask, task.getIdTask(), controller); // Comprobamos si se encuentra el objeto en la base de datos.
        Checker.isTaskCorrect(task, controller); // Comprobamos si los campos son correctos.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        Update.taskFromOther(task, oldTask); // Actualiza los atributos del modelo.

        repository.updateTask(oldTask);  // No está en la práctica 7, pero deduzco que falta, ya que de la otra manera no se actualiza en la base de datos.

        return Response.noContent().build();
    }


    @DELETE
    @Path("/{taskId}")
    public Response deleteTask(@PathParam("taskId") String taskId) {
        Task toBeRemoved = repository.getTask(taskId); // Obtiene la tarea a eliminar de la base de datos.
        ControllerResponse controller = ControllerResponse.create();

        NotFound.isTaskFound(toBeRemoved, taskId, controller); // Comprobamos si se encuentra el objeto en la base de datos.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        repository.deleteTask(taskId); // Elimina la tarea de la base de datos.

        // Elimina la tarea de los usuarios.
        for (User user : repository.getAllUser())
            user.deleteTask(toBeRemoved);

        // Elimina la tarea de los grupos.
        for (Group group : repository.getAllGroup())
            group.deleteTask(toBeRemoved);

        return Response.noContent().build();
    }
}



