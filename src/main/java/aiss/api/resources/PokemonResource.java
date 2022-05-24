package aiss.api.resources;

import aiss.model.Status;
import aiss.model.Task;

import aiss.model.repository.MapRepository;
import aiss.model.repository.Repository;
import aiss.utilities.Pair;
import aiss.utilities.Parse;
import aiss.utilities.messages.Checker;
import aiss.utilities.messages.ControllerResponse;
import aiss.utilities.messages.Message;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

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
    public Response getPokemon() {
        List<Map<String, Object>> tasks;
        try {
            tasks = Arrays.asList(repository.getAllPokemons())
            		.stream()
            		.map(pokemon -> Parse.taskFromPokemon(pokemon, null, null, null).getFields(Task.ALL_ATTRIBUTES))
            		.collect(Collectors.toList());
        } catch (Exception e) {
            return Message.send(Response.Status.NOT_FOUND, Pair.of("status: ", "404"),
                    Pair.of("message: ", "The pokemon with the name was not found"));
        }
        return Response.ok(tasks).build();
    }
    

    @GET
    @Path("/{name}")
    public Response getPokemon(@PathParam("name") String name, @QueryParam("status") String status,
                               @QueryParam("finishedDate") String finishedDate, @QueryParam("priority") String priority) {
        Task task;
        ControllerResponse controller = ControllerResponse.create();
        Date auxFinishedDate = Checker.isDateCorrect(finishedDate, controller);
        Checker.isAfter(Date.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))), auxFinishedDate, controller);
        Status auxStatus = Checker.isStatusCorrect(status, controller);
        Integer auxPriority = Checker.isNumberCorrect(priority, controller);
        Checker.isPriorityCorrect(auxPriority, controller);
        if (controller.hasError()) return controller.getMessage();
        try {
            task = Parse.taskFromPokemon(repository.getPokemon(name), auxStatus, finishedDate, priority);
        } catch (Exception e) {
            return Message.send(Response.Status.NOT_FOUND, Pair.of("status: ", "404"),
                    Pair.of("message: ", "The pokemon with the name " + name + " was not found"));
        }
        return Response.ok(task.getFields(task.ALL_ATTRIBUTES)).build();
    }

    @POST
    @Path("/{name}")
    public Response addPokemon(@PathParam("name") String name, @QueryParam("status") String status,
                               @QueryParam("finishedDate") String finishedDate, @QueryParam("priority") String priority) {
        Task task;
        ControllerResponse controller = ControllerResponse.create();
        Date auxFinishedDate = Checker.isDateCorrect(finishedDate, controller);
        Checker.isAfter(Date.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))), auxFinishedDate, controller);
        Status auxStatus = Checker.isStatusCorrect(status, controller);
        Integer auxPriority = Checker.isNumberCorrect(priority, controller);
        Checker.isPriorityCorrect(auxPriority, controller);
        if (controller.hasError()) return controller.getMessage();
        if (controller.hasError()) return controller.getMessage();
        try {
            task = Parse.taskFromPokemon(repository.getPokemon(name), auxStatus, finishedDate, priority);
        } catch (Exception e) {
            return Message.send(Response.Status.NOT_FOUND, Pair.of("status: ", "404"),
                    Pair.of("message: ", "The pokemon with the name " + name + " was not found"));
        }
        repository.addTask(task);
        return Response.ok(task).build();
    }
}
