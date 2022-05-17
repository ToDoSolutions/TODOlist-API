package aiss.api.resources;

import aiss.model.Task;
import aiss.model.repository.MapRepository;
import aiss.model.repository.Repository;
import aiss.utilities.Pair;
import aiss.utilities.Parse;
import aiss.utilities.messages.Message;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/pokemon")
@Produces("application/json")
public class PokemonResource {

    protected static PokemonResource instance = null; // La instancia inicialmente no existe, se crea al ejecutar .getInstance().
    final Repository repository; // Para poder trabajar con los datos

    private PokemonResource() {
        repository = MapRepository.getInstance();
    }

    public static PokemonResource getInstance() {
        instance = (instance == null) ? new PokemonResource() : instance; // Creamos una instancia si no existe.
        return instance;
    }

    @GET
    @Path("/{name}")
    public Response getPokemon(@PathParam("name") String name, @QueryParam("status") String status,
                               @QueryParam("finishedDate") String finishedDate, @QueryParam("priority") String priority) {
        Task task;
        try {
            task = Parse.taskFromPokemon(repository.getPokemon(name), status, finishedDate, priority);
        } catch (Exception e) {
            return Message.send(Response.Status.NOT_FOUND, Pair.of("status: ", "404"),
                    Pair.of("message: ", "The pokemon with the name " + e + " was not found"));
        }
        return Response.ok(task).build();
    }

    @POST
    @Path("/{name}")
    public Response addPokemon(@PathParam("name") String name, @QueryParam("status") String status,
                               @QueryParam("finishedDate") String finishedDate, @QueryParam("priority") String priority) {
        Task task;
        try {
            task = Parse.taskFromPokemon(repository.getPokemon(name), status, finishedDate, priority);
        } catch (Exception e) {
            return Message.send(Response.Status.NOT_FOUND, Pair.of("status: ", "404"),
                    Pair.of("message: ", "The pokemon with the name " + name + " was not found"));
        }
        repository.addTask(task);
        return Response.ok(task).build();
    }
}
