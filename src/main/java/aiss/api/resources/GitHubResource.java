package aiss.api.resources;

import aiss.model.Task;
import aiss.model.User;
import aiss.model.repository.MapRepository;
import aiss.model.repository.Repository;
import aiss.utilities.Message;
import aiss.utilities.Pair;
import aiss.utilities.Parse;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/github")
public class GitHubResource {

    protected static GitHubResource instance = null; // La instancia inicialmente no existe, se crea al ejecutar .getInstance().
    final Repository repository; // Para poder trabajar con los datos

    private GitHubResource() {
        repository = MapRepository.getInstance();
    }

    public static GitHubResource getInstance() {
        // Creamos una instancia si no existe.
        instance = (instance == null) ? new GitHubResource() : instance;
        return instance;
    }


    @GET
    @Path("/{account}/{repo}")
    @Produces("application/json")
    public Response getTask(@PathParam("account") String account, @PathParam("repo") String repo,
                            @QueryParam("status") String status, @QueryParam("finishedDate") String finishedDate, @QueryParam("priority") Integer priority,
                            @QueryParam("difficulty") String difficulty) {
        Task task;
        try {
            task = Parse.taskFromGitHub(repository.getRepo(account, repo), status, finishedDate, priority, difficulty);
        } catch (Exception e) {
            return Message.send(Response.Status.NOT_FOUND, Pair.of("status: ", "404"),
                    Pair.of("message: ", "The repository with the name " + repo + " was not found"));
        }

        // Comprobamos si el repo es correcto
        Response response = Message.checkRepo(task);
        if (response != null) return response;

        return Response.ok(task).build();


    }

    @POST
    @Path("/{account}/{repo}")
    @Produces("application/json")
    public Response addTask(@Context UriInfo uriInfo, @PathParam("account") String account, @PathParam("repo") String repo,
                            @QueryParam("status") String status, @QueryParam("finishedDate") String finishedDate, @QueryParam("priority") Integer priority,
                            @QueryParam("difficulty") String difficulty) {
        Task task;
        try {
            task = Parse.taskFromGitHub(repository.getRepo(account, repo), status, finishedDate, priority, difficulty);
        } catch (Exception e) {
            return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status: ", "404"),
                    Pair.of("message: ", "The repository with the name " + repo + " was not found"));
        }

        // Comprobamos si el repo es correcto
        Response response = Message.checkRepo(task);
        if (response != null) return response;


        repository.addTask(task); // Añadimos el modelo a la base de datos.
        // Builds the response. Returns the playlist the has just been added.
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(TaskResource.getInstance().getClass(), "getTask");
        URI uri = ub.build(task.getIdTask());
        ResponseBuilder resp = Response.created(uri);
        resp.entity(task);
        return resp.build();
    }

    @GET
    @Path("/{account}")
    @Produces("application/json")
    public Response getUser(@PathParam("account") String account) {
        User user;
        try {
            user = Parse.userFromGitHub(repository.getOwner(account));
        } catch (Exception e) {
            return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status: ", "404"),
                    Pair.of("message: ", "The user with the name " + account + " was not found"));
        }
        return Response.ok(user).build();
    }

    @POST
    @Path("/{account}")
    @Produces("application/json")
    public Response addUser(@Context UriInfo uriInfo, @PathParam("account") String account) {
        User user;
        try {
            user = Parse.userFromGitHub(repository.getOwner(account));
        } catch (Exception e) {
            return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status: ", "404"),
                    Pair.of("message: ", "The account with the name " + account + " was not found"));
        }

        repository.addUser(user); // Añadimos el modelo a la base de datos.
        // Builds the response. Returns the playlist the has just been added.
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(UserResource.getInstance().getClass(), "getUser");
        URI uri = ub.build(user.getIdUser());
        ResponseBuilder resp = Response.created(uri);
        resp.entity(user);
        return resp.build();
    }
}
