package aiss.api.resources;

import aiss.model.Difficulty;
import aiss.model.Status;
import aiss.model.Task;
import aiss.model.User;
import aiss.model.repository.MapRepository;
import aiss.model.repository.Repository;
import aiss.utilities.Pair;
import aiss.utilities.Parse;
import aiss.utilities.messages.Checker;
import aiss.utilities.messages.ControllerResponse;
import aiss.utilities.messages.Message;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/github")
@Produces("application/json")
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
    @Path("repos/{account}")
    public Response getAllTask(@PathParam("account") String account) {
    	List<Map<String, Object>> tasks;
        try {
            tasks = Arrays.asList(repository.getAllRepos(account))
            		.stream()
            		.map(repo -> Parse.taskFromGitHub(repo, null, null, null, null).getFields(Task.ALL_ATTRIBUTES))
            		.collect(Collectors.toList());
        } catch (Exception e) {
        	return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status: ", "404"),
                    Pair.of("message: ", "The user with the name " + e + " was not found"));
        }
        return Response.ok(tasks).build();
    }


    @GET
    @Path("/{account}/{repo}")
    public Response getTask(@PathParam("account") String account, @PathParam("repo") String repo,
                            @QueryParam("status") String status, @QueryParam("finishedDate") String finishedDate, @QueryParam("priority") Integer priority,
                            @QueryParam("difficulty") String difficulty) {
        Task task;
        ControllerResponse controller = ControllerResponse.create();
        Status auxStatus = Checker.isStatusCorrect(status, controller);
        Difficulty auxDifficulty = Checker.isDifficultyCorrect(difficulty, controller);
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();
        try {
            task = Parse.taskFromGitHub(repository.getRepo(account, repo), auxStatus, finishedDate, priority, auxDifficulty);
        } catch (Exception e) {
            return Message.send(Response.Status.NOT_FOUND, Pair.of("status: ", "404"),
                    Pair.of("message: ", "The repository with the name " + repo + " was not found"));
        }

        Checker.isRepoCorrect(task, controller); // Comprobamos si el repo es correcto.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        return Response.ok(task.getFields(task.ALL_ATTRIBUTES)).build();


    }

    @POST
    @Path("/{account}/{repo}")
    public Response addTask(@Context UriInfo uriInfo, @PathParam("account") String account, @PathParam("repo") String repo,
                            @QueryParam("status") String status, @QueryParam("finishedDate") String finishedDate, @QueryParam("priority") Integer priority,
                            @QueryParam("difficulty") String difficulty) {
        Task task;
        ControllerResponse controller = ControllerResponse.create();
        Status auxStatus = Checker.isStatusCorrect(status, controller);
        Difficulty auxDifficulty = Checker.isDifficultyCorrect(difficulty, controller);
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();
        try {
            task = Parse.taskFromGitHub(repository.getRepo(account, repo), auxStatus, finishedDate, priority, auxDifficulty);
        } catch (Exception e) {
            return Message.send(Response.Status.NOT_FOUND,
                    Pair.of("status: ", "404"),
                    Pair.of("message: ", "The repository with the name " + repo + " was not found"));
        }
        Checker.isRepoCorrect(task, controller); // Comprobamos si el repo es correcto
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();


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
