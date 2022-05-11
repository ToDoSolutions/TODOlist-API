package aiss.api.resources;

import aiss.model.Task;
import aiss.model.repository.MapRepository;
import aiss.model.repository.Repository;
import javassist.NotFoundException;
import org.jboss.resteasy.spi.BadRequestException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/tasks")
public class TaskResource {

    protected static final TaskResource instance = null; // La instancia inicialmente no existe, se crea al ejecutar .getInstance().
    final Repository repository; // Para poder trabajar con los datos

    private TaskResource() {
        repository = MapRepository.getInstance();
    }

    public static TaskResource getInstance() {
        // Creamos una instancia si no existe.
        return (instance == null) ? new TaskResource() : instance;
    }

    @GET
    @Produces("application/json")
    public List<Map<String, Object>> getAllTasks(@QueryParam("order") String order,
                                                 @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                                                 @QueryParam("fields") String fields) {
        {
            List<Task> result = new ArrayList<>(), tasks = new ArrayList<>(repository.getAllTask()); // No se puede utilizar .toList() porque eso es a partir de Java 16.
            int start = offset == null ? 0 : offset - 1; // Donde va a comenzar.
            int end = limit == null ? tasks.size() : start + limit; // Donde va a terminar.
            for (int i = start; i < end; i++) {
                // Hacer más compleja esta restricción, pedir para un cierto:
                // - title.
                // - status.
                // - releaseDate (mayor, menor  o igual).
                // - finishedDate.
                // - priority.
                // - difficulty
                // - duration (mayor, menor o igual).
                if (tasks.get(i) != null)
                    result.add(tasks.get(i));
            }
            if (order != null)
                orderResult(result, order);

            // fields lo hemos dado en teoría, pero no en práctica, quizás en vez de esto sea con un Response.
            return result.stream().map(task -> task.getFields(fields == null ? Task.ALL_ATTRIBUTES : fields)).collect(Collectors.toList());
        }
    }

    private void orderResult(List<Task> result, String order) {
        if (order.equals("title"))
            result.sort(Comparator.comparing(Task::getTitle));
        else if (order.equals("-title"))
            result.sort(Comparator.comparing(Task::getTitle).reversed());
        else if (order.equals("status"))
            result.sort(Comparator.comparing(Task::getStatus));
        else if (order.equals("-status"))
            result.sort(Comparator.comparing(Task::getStatus).reversed());
        else if (order.equals("releaseDate"))
            result.sort(Comparator.comparing(Task::getReleaseDate));
        else if (order.equals("-releaseDate"))
            result.sort(Comparator.comparing(Task::getReleaseDate).reversed());
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

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Task getTask(@PathParam("id") String id) throws NotFoundException {
        Task t = repository.getTask(id);

        // Comprobamos si se encuentra el objeto en la base de datos chapucera.
        if (t == null)
            throw new NotFoundException("The task with id=" + id + " was not found");

        return t;
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addTask(@Context UriInfo uriInfo, Task task) {
        isTaskCorrect(task); // Comprueba contiene algún tipo de error.

        repository.addTask(task); // Añadimos el modelo a la base de datos chapucera.

        // Builds the response. Returns the playlist the has just been added.
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
        URI uri = ub.build(task.getIdTask());
        ResponseBuilder resp = Response.created(uri);
        resp.entity(task);
        return resp.build();
    }

    private void isTaskCorrect(Task task) {
        if (task.getTitle() == null || "".equals(task.getTitle()))
            throw new BadRequestException("The title of the task must not be null");
    }

    @PUT
    @Consumes("application/json")
    public Response updateTask(Task task) throws NotFoundException {
        Task oldTask = repository.getTask(task.getIdTask());

        // Comprobamos si se encuentra el objeto en la base de datos chapucera.
        if (oldTask == null)
            throw new NotFoundException("The task with id=" + task.getIdTask() + " was not found");

        auxUpdateTask(task, oldTask); // Actualiza los atributos del modelo.

        repository.updateTask(task);  // No está en la práctica 7, pero deduzco que falta, ya que de la otra manera no se actualiza en la base de datos chapucera.

        return Response.noContent().build();
    }

    private void auxUpdateTask(Task task, Task oldTask) {
        if (task.getTitle() != null)
            oldTask.setTitle(task.getTitle());
        if (task.getDescription() != null)
            oldTask.setDescription(task.getDescription());
        if (task.getStatus() != null)
            oldTask.setStatus(task.getStatus());
        if (task.getFinishedDate() != null)
            oldTask.setFinishedDate(task.getFinishedDate());
        if (task.getReleaseDate() != null)
            oldTask.setReleaseDate(task.getReleaseDate());
        if (task.getAnnotation() != null)
            oldTask.setAnnotation(task.getAnnotation());
        if (task.getPriority() != null)
            oldTask.setPriority(task.getPriority());
        if (task.getDifficulty() != null)
            oldTask.setDifficulty(task.getDifficulty());
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") String taskId) throws NotFoundException {
        Task toBeRemoved = repository.getTask(taskId); // Obtiene el modelo a eliminar de la base de datos chapucera.

        // Comprobamos si se encuentra el objeto en la base de datos chapucera.
        if (toBeRemoved == null)
            throw new NotFoundException("The task with id=" + taskId + " was not found");
            // Si no Elimina el modelo de la base de datos chapucera.
        else
            repository.deleteTask(taskId);

        return Response.noContent().build();
    }
}



