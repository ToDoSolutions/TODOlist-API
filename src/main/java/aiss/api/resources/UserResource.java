package aiss.api.resources;


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
                           @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                           @QueryParam("fieldsUser") String fieldsUser, @QueryParam("fieldsTask") String fieldsTask,
                           @QueryParam("name") String name, @QueryParam("surname") String surname, @QueryParam("email") String email,
                           @QueryParam("location") String location, @QueryParam("taskCompleted") String taskCompleted) {
        List<User> result = new ArrayList<>(), users = new ArrayList<>(repository.getAllUser()); // No se puede utilizar .toList() porque eso es a partir de Java 16.
        if (order != null)
            Order.sequenceUser(users, order);
        int start = offset == null ? 0 : offset - 1; // Donde va a comenzar.
        int end = limit == null || limit > users.size() ? users.size() : start + limit; // Donde va a terminar.

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

        // Comprobamos si se encuentra el objeto en la base de datos.
        Response response = Message.userNotFound(user, userId);
        if (response != null) return response;

        return Response.ok(user.getFields((fieldsUser == null ? User.ALL_ATTRIBUTES : fieldsUser), fieldsTask)).build();
    }

    @POST
    @Consumes("application/json")
    public Response addUser(@Context UriInfo uriInfo, User user) {
        // Comprobamos aquellos campos obligatorios.
        Response response = Message.requiredForUser(user);
        if (response != null) return response;

        // Comprobamos si algún campo no es correcto.
        response = Message.checkUser(user);
        if (response != null) return response;

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
        // Comprobamos que nos ha dado una id.
        Response response = Message.userIdRequired(user);
        if (response != null) return response;

        User oldUser = repository.getUser(user.getIdUser());

        // Comprobamos si se encuentra el objeto en la base de datos.
        response = Message.userNotFound(oldUser, user.getIdUser());
        if (response != null) return response;

        // Comprobamos si algún campo no es correcto.
        response = Message.checkUser(user);
        if (response != null) return response;

        Update.userFromOther(user, oldUser); // Actualiza los atributos del modelo.
        repository.updateUser(oldUser);  // No está en la práctica 7, pero deduzco que falta, ya que de la otra manera no se actualiza en la base de datos.

        return Response.noContent().build();
    }


    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") String userId) {
        User toBeRemoved = repository.getUser(userId); // Obtiene el modelo a eliminar de la base de datos chapucera.

        // Comprobamos si se encuentra el usuario en la base de datos.
        Response response = Message.userNotFound(toBeRemoved, userId);
        if (response != null) return response;

        // Elimina el usuario de la base de datos chapucera.
        repository.deleteUser(userId);

        return Response.noContent().build();
    }

    @POST
    @Path("/{userId}/{taskId}")
    public Response addTaskToUser(@Context UriInfo uriInfo, @PathParam("userId") String userId, @PathParam("taskId") String taskId) {
        User user = repository.getUser(userId);
        Task task = repository.getTask(taskId);

        // Comprobamos si el usuario tiene la tarea asignada.
        Response response = Message.isTaskInUser(user, task, userId, taskId);
        if (response != null) return response;

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

        // Comprobamos que existe un usuario con la id dada.
        Response response = Message.userNotFound(user, userId);
        if (response != null) return response;

        // Comprobamos que existe una tarea con la id dada.
        response = Message.taskNotFound(task, taskId);
        if (response != null) return response;

        repository.deleteTaskToOrder(userId, taskId);

        return Response.noContent().build();
    }
}

