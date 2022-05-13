package aiss.api.resources;


import aiss.model.Task;
import aiss.model.User;
import aiss.model.repository.MapRepository;
import aiss.model.repository.Repository;
import aiss.utilities.Pair;
import aiss.utilities.Tool;
import javassist.NotFoundException;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
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
    @Produces("application/json")
    public Response getAll(@QueryParam("order") String order,
                           @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                           @QueryParam("fieldsUser") String fieldsUser, @QueryParam("fieldsTask") String fieldsTask,
                           @QueryParam("name") String name, @QueryParam("surname") String surname, @QueryParam("email") String email,
                           @QueryParam("location") String location, @QueryParam("taskCompleted") String taskCompleted) {

        List<User> result = new ArrayList<>(), users = new ArrayList<>(repository.getAllUser()); // No se puede utilizar .toList() porque eso es a partir de Java 16.
        if (order != null)
            orderResult(users, order);
        int start = offset == null ? 0 : offset - 1; // Donde va a comenzar.
        int end = limit == null || limit > users.size() ? users.size() : start + limit; // Donde va a terminar.


        for (int i = start; i < end; i++) {
            User user = users.get(i);
            if (user != null &&
                    (name == null || user.getName().equals(name)) &&
                    (surname == null || user.getSurname().equals(surname)) &&
                    (email == null || user.getEmail().equals(email)) &&
                    (location == null || user.getLocation().equals(location)) &&
                    (taskCompleted == null || Tool.isGEL(user.getTaskCompleted(), taskCompleted)))
                result.add(user);
        }

        // fields lo hemos dado en teoría, pero no en práctica, quizás en vez de esto sea con un Response.
        return Response.ok(result.stream().map(user -> user.getFields((fieldsUser == null ? User.ALL_ATTRIBUTES : fieldsUser), fieldsTask)).collect(Collectors.toList())).build();
    }

    private void orderResult(List<User> result, String order) {
        if (order.equals("idUser"))
            result.sort(Comparator.comparing(User::getIdUser));
        else if (order.equals("-idUser"))
            result.sort(Comparator.comparing(User::getIdUser).reversed());
        else if (order.equals("name"))
            result.sort(Comparator.comparing(User::getName));
        else if (order.equals("-name"))
            result.sort(Comparator.comparing(User::getName).reversed());
        else if (order.equals("surname"))
            result.sort(Comparator.comparing(User::getSurname));
        else if (order.equals("-surname"))
            result.sort(Comparator.comparing(User::getSurname).reversed());
        else if (order.equals("email"))
            result.sort(Comparator.comparing(User::getEmail));
        else if (order.equals("-email"))
            result.sort(Comparator.comparing(User::getEmail).reversed());
        else if (order.equals("location"))
            result.sort(Comparator.comparing(User::getLocation));
        else if (order.equals("-location"))
            result.sort(Comparator.comparing(User::getLocation).reversed());
        else if (order.equals("taskCompleted"))
            result.sort(Comparator.comparing(User::getTaskCompleted));
        else if (order.equals("-taskCompleted"))
            result.sort(Comparator.comparing(User::getTaskCompleted).reversed());
    }

    @GET
    @Path("/{userId}")
    @Produces("application/json")
    public Response getUser(@PathParam("userId") String userId, @QueryParam("fieldsUser") String fieldsUser, @QueryParam("fieldsTask") String fieldsTask) throws NotFoundException /* No debería de ser necesario este throw */ {
        User user = repository.getUser(userId);
        // Comprobamos si se encuentra el objeto en la base de datos.
        if (user == null)
            return Tool.sendMsg(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The user with id=" + userId + " was not found"));
        return Response.ok(user.getFields((fieldsUser == null ? User.ALL_ATTRIBUTES : fieldsUser), fieldsTask)).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addUser(@Context UriInfo uriInfo, User user) {
        // Comprueba contiene algún tipo de error.
        if (user.getName() == null || "".equals(user.getName()))
            return Tool.sendMsg(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The name of the user is required"));
        if (user.getSurname() == null || "".equals(user.getSurname()))
            return Tool.sendMsg(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The surname of the user is required"));
        if (user.getEmail() == null || "".equals(user.getEmail()))
            return Tool.sendMsg(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The email of the user is required"));
        if (user.getTasks() != null)
            return Tool.sendMsg(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The tasks of the user are not allowed"));

        // Comprobamos si algún campo no es correcto.
        Response response = validateUser(user);
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
    @Produces("application/json")
    public Response updateUser(User user) {
        User oldUser = repository.getUser(user.getIdUser());
        // Comprobamos si se encuentra el objeto en la base de datos.
        if (oldUser == null)
            return Tool.sendMsg(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The user with id=" + user.getIdUser() + " was not found"));

        // Comprobamos si algún campo no es correcto.
        Response response = validateUser(user);
        if (response != null) return response;

        updateUser(user, oldUser); // Actualiza los atributos del modelo.
        repository.updateUser(oldUser);  // No está en la práctica 7, pero deduzco que falta, ya que de la otra manera no se actualiza en la base de datos.

        return Response.noContent().build();
    }

    private void updateUser(User user, User oldUser) {
        // Update name.
        if (user.getName() != null)
            oldUser.setName(user.getName());
        // Update surname.
        if (user.getSurname() != null)
            oldUser.setSurname(user.getSurname());
        // Update email.
        if (user.getEmail() != null)
            oldUser.setEmail(user.getEmail());
        // Update avatar.
        if (user.getAvatar() != null)
            oldUser.setAvatar(user.getAvatar());
        // Update bio.
        if (user.getBio() != null)
            oldUser.setBio(user.getBio());
        // Update location.
        if (user.getLocation() != null)
            oldUser.setLocation(user.getLocation());
    }

    @DELETE
    @Path("/{userId}")
    @Produces("application/json")
    public Response deleteUser(@PathParam("userId") String userId) {
        User toBeRemoved = repository.getUser(userId); // Obtiene el modelo a eliminar de la base de datos chapucera.

        // Comprobamos si se encuentra el objeto en la base de datos chapucera.
        if (toBeRemoved == null)
            return Tool.sendMsg(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The user with id=" + userId + " was not found"));
        // Si no Elimina el usuario de la base de datos chapucera.
        else
            repository.deleteUser(userId);

        return Response.noContent().build();
    }

    @POST
    @Path("/{userId}/{taskId}")
    @Produces("application/json")
    public Response addTaskToUser(@Context UriInfo uriInfo, @PathParam("userId") String userId, @PathParam("taskId") String taskId) throws NotFoundException {
        User user = repository.getUser(userId);
        Task task = repository.getTask(taskId);

        if (user == null)
            return Tool.sendMsg(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The user with id=" + userId + " was not found"));

        if (task == null)
            return Tool.sendMsg(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The task with id=" + taskId + " was not found"));

        if (user.getTask(taskId) != null)
            return Tool.sendMsg(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The task with id=" + taskId + " is already assigned to the user with id=" + userId));

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
    @Produces("application/json")
    public Response deleteTaskToUser(@PathParam("userId") String userId, @PathParam("taskId") String taskId) {
        User user = repository.getUser(userId);
        Task task = repository.getTask(taskId);

        if (user == null)
            return Tool.sendMsg(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The user with id=" + userId + " was not found"));

        if (task == null)
            return Tool.sendMsg(Response.Status.NOT_FOUND,
                    Pair.of("status", "404"),
                    Pair.of("message", "The task with id=" + taskId + " was not found"));

        repository.deleteTaskToOrder(userId, taskId);

        return Response.noContent().build();
    }

    public Response validateUser(User user) {
        if (user.getEmail() != null && !EmailValidator.getInstance().isValid(user.getEmail()))
            return Tool.sendMsg(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The email of the user is not valid"));
        if (user.getName() != null && user.getName().length() > 50)
            return Tool.sendMsg(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The name of the user is not valid"));
        if (user.getSurname() != null && user.getSurname().length() > 50)
            return Tool.sendMsg(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The surname of the user is not valid"));
        if (user.getAvatar() != null && !UrlValidator.getInstance().isValid(user.getAvatar()))
            return Tool.sendMsg(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The avatar of the user is not valid"));
        return null;
    }
}

