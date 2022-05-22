package aiss.api.resources;


import aiss.model.Group;
import aiss.model.Task;
import aiss.model.User;
import aiss.model.repository.MapRepository;
import aiss.model.repository.Repository;
import aiss.utilities.Filter;
import aiss.utilities.Order;
import aiss.utilities.Update;
import aiss.utilities.messages.*;

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

@Path("/users")
@Produces("application/json")
public class UserResource {

    protected static UserResource instance = null; // La instancia inicialmente no existe, se crea al ejecutar .getInstance().
    final Repository repository; // Para poder trabajar con los datos

    private UserResource() {
        repository = MapRepository.getInstance();
    }

    public static UserResource getInstance() {
        // Creamos una instancia si no existe.
        instance = (instance == null) ? new UserResource() : instance;
        return instance;
    }

    @GET
    public Response getAll(@QueryParam("order") String order,
                           @QueryParam("limit") String limit, @QueryParam("offset") String offset,
                           @QueryParam("fieldsUser") String fieldsUser, @QueryParam("fieldsTask") String fieldsTask,
                           @QueryParam("name") String name, @QueryParam("surname") String surname, @QueryParam("email") String email,
                           @QueryParam("location") String location, @QueryParam("taskCompleted") String taskCompleted) {
        List<User> result = new ArrayList<>(), users = new ArrayList<>(repository.getAllUser());
        ControllerResponse controller = ControllerResponse.create();
        Integer auxLimit = Checker.isNumberCorrect(limit, controller);
        Integer auxOffset = Checker.isNumberCorrect(offset, controller);
        Checker.isParamGELNumber(taskCompleted,  controller);
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();
        
        if (order != null)
            Order.sequenceUser(users, order);
        int start = offset == null || auxOffset < 1 ? 0 : auxOffset - 1; // Donde va a comenzar.
        int end = limit == null || auxLimit > users.size() ? users.size() : start + auxLimit; // Donde va a terminar.

        

        for (int i = start; i < end; i++) {
            User user = users.get(i);
            if (user != null &&
                    (name == null || user.getName().equals(name)) &&
                    (surname == null || user.getSurname().equals(surname)) &&
                    (email == null || user.getEmail().equals(email)) &&
                    (location == null || user.getLocation().equals(location)) &&
                    (taskCompleted == null || Filter.isGEL(user.getTaskCompleted(), taskCompleted)))
                result.add(user);
        }
        return Response.ok(result.stream().map(user -> user.getFields((fieldsUser == null ? User.ALL_ATTRIBUTES : fieldsUser), fieldsTask)).collect(Collectors.toList())).build();
    }


    @GET
    @Path("/{userId}")
    public Response getUser(@PathParam("userId") String userId, @QueryParam("fieldsUser") String fieldsUser, @QueryParam("fieldsTask") String fieldsTask) {
        User user = repository.getUser(userId);
        ControllerResponse controller = ControllerResponse.create();

        NotFound.isUserFound(user, userId, controller); // Comprobamos si se encuentra el objeto en la base de datos.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        return Response.ok(user.getFields((fieldsUser == null ? User.ALL_ATTRIBUTES : fieldsUser), fieldsTask)).build();
    }

    @POST
    @Consumes("application/json")
    public Response addUser(@Context UriInfo uriInfo, User user) {
        ControllerResponse controller = ControllerResponse.create();


        Required.forUser(user, controller); // Comprobamos aquellos campos obligatorios.
        Checker.isUserCorrect(user, controller); // Comprobamos si algún campo no es correcto.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        repository.addUser(user); // Añadimos el usuario a la base de datos.

        // Builds the response. Returns the user the has just been added.
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "getUser");
        URI uri = ub.build(user.getIdUser());
        ResponseBuilder resp = Response.created(uri);
        resp.entity(user);
        return resp.build();
    }

    @PUT
    @Consumes("application/json")
    public Response updateUser(User user) {
        ControllerResponse controller = ControllerResponse.create();


        Required.haveUserId(user, controller); // Comprobamos que nos ha dado una id.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        User oldUser = repository.getUser(user.getIdUser());


        NotFound.isUserFound(oldUser, user.getIdUser(), controller); // Comprobamos si se encuentra el objeto en la base de datos.
        Checker.isUserCorrect(user, controller); // Comprobamos si algún campo no es correcto.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        Update.userFromOther(user, oldUser); // Actualiza los atributos del modelo.
        repository.updateUser(oldUser);  // Actualiza la base de datos.

        return Response.noContent().build();
    }


    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") String userId) {
        User toBeRemoved = repository.getUser(userId); // Obtiene el modelo a eliminar de la base de datos chapucera.
        ControllerResponse controller = ControllerResponse.create();

        NotFound.isUserFound(toBeRemoved, userId, controller); // Comprobamos si se encuentra el usuario en la base de datos.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        // Elimina el usuario de la base de datos chapucera.
        repository.deleteUser(userId);

        // Elimina el usuario de los grupos.
        for (Group group : repository.getAllGroup())
            group.deleteUser(toBeRemoved);

        return Response.noContent().build();
    }

    @POST
    @Path("/{userId}/{taskId}")
    public Response addTaskToUser(@Context UriInfo uriInfo, @PathParam("userId") String userId, @PathParam("taskId") String taskId) {
        User user = repository.getUser(userId);
        Task task = repository.getTask(taskId);
        ControllerResponse controller = ControllerResponse.create();

        Container.isTaskInUser(user, task, userId, taskId, controller); // Comprobamos si el usuario tiene la tarea asignada.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        repository.addTaskToUser(userId, taskId);

        // Builds the response
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "getUser");
        URI uri = ub.build(userId);
        ResponseBuilder resp = Response.created(uri);
        resp.entity(user);
        return resp.build();
    }

    @DELETE
    @Path("/{userId}/{taskId}")
    public Response deleteTaskToUser(@PathParam("userId") String userId, @PathParam("taskId") String taskId) {
        User user = repository.getUser(userId);
        Task task = repository.getTask(taskId);
        ControllerResponse controller = ControllerResponse.create();


        NotFound.isUserFound(user, userId, controller); // Comprobamos que existe un usuario con la id dada.
        NotFound.isTaskFound(task, taskId, controller); // Comprobamos que existe una tarea con la id dada.
        NotFound.isTaskFoundInUser(user, task, controller);
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        repository.deleteTaskToOrder(userId, taskId);

        return Response.noContent().build();
    }
}

