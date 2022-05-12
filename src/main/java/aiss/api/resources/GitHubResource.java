package aiss.api.resources;

import aiss.model.Difficulty;
import aiss.model.Status;
import aiss.model.Task;
import aiss.model.User;
import aiss.model.github.Owner;
import aiss.model.github.TaskGitHub;
import aiss.model.repository.MapRepository;
import aiss.model.repository.Repository;
import javassist.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Date;
import java.util.List;
import java.util.Map;

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

    public static String getAdditional(Map<String, Object> additional, String key) {
        Object aux = additional.get(key);
        return aux == null ? null : aux.toString();
    }

    @GET
    @Path("/{account}/{repo}")
    @Produces("application/json")
    public Task getTask(@PathParam("account") String account, @PathParam("repo") String repo,
                        @QueryParam("status") String status, @QueryParam("finishedDate") String finishedDate, @QueryParam("priority") Integer priority,
                        @QueryParam("difficulty") String difficulty) throws NotFoundException {
        return parseTaskFromGitHub(repository.getRepo(account, repo), status, finishedDate, priority, difficulty);
    }

    @POST
    @Path("/{account}/{repo}")
    @Produces("application/json")
    public Response addTask(@Context UriInfo uriInfo, @PathParam("account") String account, @PathParam("repo") String repo,
                            @QueryParam("status") String status, @QueryParam("finishedDate") String finishedDate, @QueryParam("priority") Integer priority,
                            @QueryParam("difficulty") String difficulty) {
        Task task = parseTaskFromGitHub(repository.getRepo(account, repo), status, finishedDate, priority, difficulty);

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
    public User getUser(@PathParam("account") String account) throws NotFoundException {
        return parseOrderFromGitHub(repository.getOwner(account));
    }

    @POST
    @Path("/{account}")
    @Produces("application/json")
    public Response addUser(@Context UriInfo uriInfo, @PathParam("account") String account) {
        User user = parseOrderFromGitHub(repository.getOwner(account));

        repository.addUser(user); // Añadimos el modelo a la base de datos.
        // Builds the response. Returns the playlist the has just been added.
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(UserResource.getInstance().getClass(), "getUser");
        URI uri = ub.build(user.getIdUser());
        ResponseBuilder resp = Response.created(uri);
        resp.entity(user);
        return resp.build();
    }

    // Funciones auxiliares.
    private User parseOrderFromGitHub(Owner owner) {
        Map<String, Object> additional = owner.getAdditionalProperties();
        Object auxName = additional.get("name");
        List<String> fullName = null;
        String name = null;
        String surname = null;
        if (auxName != null) {
            fullName = List.of(additional.get("name").toString().split(" "));
            name = fullName.get(0);
            surname = fullName.size() == 1 ? null : fullName.stream().skip(1).reduce("", (ac, nx) -> ac + " " + nx);
        }
        String email = getAdditional(additional, "email");
        String bio = getAdditional(additional, "bio");
        String location = getAdditional(additional, "location");
        return User.of(name, surname, email, owner.getAvatarUrl(), bio, location);
    }

    private Task parseTaskFromGitHub(TaskGitHub repo, String status, String finishedDate, Integer priority, String difficulty) {
        Status auxStatus = status == null ? null : Status.parse(status);
        Date auxFinishedDate = finishedDate == null ? null : Date.valueOf(finishedDate);
        Difficulty auxDifficulty = difficulty == null ? null : Difficulty.valueOf(difficulty);
        Object language = repo.getLanguage();
        return Task.of(
                repo.getName(),
                repo.getDescription(),
                auxStatus,
                Date.valueOf(repo.getCreatedAt().split("T")[0]),
                auxFinishedDate,
                language == null ? null: language.toString(),
                priority,
                auxDifficulty);
    }

}
