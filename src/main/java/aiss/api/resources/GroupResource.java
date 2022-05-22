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

@Path("/groups")
@Produces("application/json")
public class GroupResource {

    protected static GroupResource instance = null; // La instancia inicialmente no existe, se crea al ejecutar .getInstance().
    final Repository repository; // Para poder trabajar con los datos

    private GroupResource() {
        repository = MapRepository.getInstance();
    }

    public static GroupResource getInstance() {
        instance = (instance == null) ? new GroupResource() : instance; // Creamos una instancia si no existe.
        return instance;
    }

    @GET
    public Response getAll(@QueryParam("order") String order,
                           @QueryParam("limit") String limit, @QueryParam("offset") String offset,
                           @QueryParam("fieldsGroup") String fieldsGroup, @QueryParam("fieldsUser") String fieldsUser,
                           @QueryParam("fieldsTask") String fieldsTask, @QueryParam("name") String name,
                           @QueryParam("description") String description, @QueryParam("numTasks") String numTasks,
                           @QueryParam("createdDate") String createdDate) {
        List<Group> result = new ArrayList<>(), groups = new ArrayList<>(repository.getAllGroup()); // No se puede utilizar .toList() porque eso es a partir de Java 16.
        ControllerResponse controller = ControllerResponse.create();
        Integer auxLimit = Checker.isNumberCorrect(limit, controller);
        Integer auxOffset = Checker.isNumberCorrect(offset, controller);
        Checker.isParamGELDate(createdDate, controller);
        Checker.isParamGELNumber(numTasks, controller);
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();
        if (order != null)
            Order.sequenceGroup(groups, order);
        int start = offset == null || auxOffset < 1? 0 : auxOffset - 1; // Donde va a comenzar.
        int end = limit == null || auxLimit > groups.size() ? groups.size() : start + auxLimit; // Donde va a terminar.
        

        for (int i = start; i < end; i++) {
            Group group = groups.get(i);
            if (group != null &&
                    (name == null || group.getName().equals(name)) &&
                    (description == null || group.getDescription().equals(description)) &&
                    (numTasks == null || Filter.isGEL((long) group.getNumTasks(), numTasks)) &&
                    (createdDate == null || Filter.isGEL(group.getCreatedDate(), createdDate)))
                result.add(group);
        }
        return Response.ok(result.stream().map(g -> g.getFields((fieldsGroup == null ? Group.ALL_ATTRIBUTES : fieldsGroup), fieldsUser, fieldsTask)).collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{groupId}")
    public Response getGroup(@PathParam("groupId") String groupId, @QueryParam("fieldsGroup") String fieldsGroup,
                             @QueryParam("fieldsUser") String fieldsUser, @QueryParam("fieldsTask") String fieldsTask) {
        Group group = repository.getGroup(groupId);
        ControllerResponse controller = ControllerResponse.create();


        NotFound.isGroupFound(group, groupId, controller); // Comprobamos si se encuentra el objeto en la base de datos.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        return Response.ok(group.getFields((fieldsGroup == null ? Group.ALL_ATTRIBUTES : fieldsGroup), fieldsUser, fieldsTask)).build();
    }

    @POST
    @Consumes("application/json")
    public Response addGroup(@Context UriInfo uriInfo, Group group) {
        ControllerResponse controller = ControllerResponse.create();

        Required.forGroup(group, controller); // Comprobamos aquellos campos obligatorios.
        Checker.isGroupCorrect(group, controller); // Comprobamos si algún campo no es correcto.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        repository.addGroup(group); // Añadimos el usuario a la base de datos.

        // Builds the response. Returns the user the has just been added.
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "getGroup");
        URI uri = ub.build(group.getIdGroup());
        ResponseBuilder resp = Response.created(uri);
        resp.entity(group);
        return resp.build();
    }

    @PUT
    @Consumes("application/json")
    public Response updateGroup(Group group) {
        ControllerResponse controller = ControllerResponse.create();

        Required.haveGroupId(group, controller); // Comprobamos que nos ha dado una id.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        Group oldGroup = repository.getGroup(group.getIdGroup());

        NotFound.isGroupFound(oldGroup, group.getIdGroup(), controller); // Comprobamos si se encuentra el objeto en la base de datos.
        Checker.isGroupCorrect(group, controller); // Comprobamos si algún campo no es correcto.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        Update.groupFromOther(group, oldGroup); // Actualiza los atributos del modelo.
        repository.updateGroup(oldGroup); // Actualiza el objeto en la base de datos.

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{groupId}")
    public Response deleteGroup(@PathParam("groupId") String groupId) {
        Group toBeRemoved = repository.getGroup(groupId); // Obtiene el modelo a eliminar de la base de datos chapucera.
        ControllerResponse controller = ControllerResponse.create();

        NotFound.isGroupFound(toBeRemoved, groupId, controller); // Comprobamos si se encuentra el grupo en la base de datos.

        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        // Elimina el usuario de la base de datos chapucera.
        repository.deleteGroup(groupId);

        return Response.noContent().build();
    }

    @POST
    @Path("/{groupId}/user/{userId}")
    @Produces("application/json")
    public Response addUserToGroup(@Context UriInfo uriInfo, @PathParam("userId") String userId, @PathParam("groupId") String groupId) {
        Group group = repository.getGroup(groupId);
        User user = repository.getUser(userId);
        ControllerResponse controller = ControllerResponse.create();


        Container.isUserInGroup(group, user, groupId, userId, controller); // Comprobamos si el grupo tiene al usuario dado.
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        repository.addUserToGroup(groupId, userId);

        // Builds the response
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "getGroup");
        URI uri = ub.build(groupId);
        ResponseBuilder resp = Response.created(uri);
        resp.entity(group);
        return resp.build();
    }

    @DELETE
    @Path("/{groupId}/user/{userId}")
    @Produces("application/json")
    public Response deleteUserToGroup(@PathParam("groupId") String groupId, @PathParam("userId") String userId) {
        Group group = repository.getGroup(groupId);
        User user = repository.getUser(userId);
        ControllerResponse controller = ControllerResponse.create();

        NotFound.isGroupFound(group, groupId, controller); // Comprobamos que existe un grupo con la id dada.
        NotFound.isUserFound(user, userId, controller); // Comprobamos que existe un usuario con la id dada.
        NotFound.isUserFoundInGroup(group, user, controller);
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        repository.deleteUserToGroup(groupId, userId);

        return Response.noContent().build();
    }

    @POST
    @Path("/{groupId}/task/{taskId}")
    public Response addTaskToGroup(@Context UriInfo uriInfo, @PathParam("taskId") String taskId, @PathParam("groupId") String groupId) {
        Group group = repository.getGroup(groupId);
        Task task = repository.getTask(taskId);
        ControllerResponse controller = ControllerResponse.create();

        NotFound.isGroupFound(group, groupId, controller); // Comprobamos que existe un grupo con la id dada.
        NotFound.isTaskFound(task, taskId, controller); // Comprobamos que existe una tarea con la id dada.
        NotFound.isTaskFoundInGroup(group, task, controller);
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        repository.addTaskToGroup(groupId, taskId);

        // Builds the response
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "getGroup");
        URI uri = ub.build(groupId);
        ResponseBuilder resp = Response.created(uri);
        resp.entity(group);
        return resp.build();
    }

    @DELETE
    @Path("/{groupId}/task/{taskId}")
    public Response deleteTaskToGroup(@PathParam("groupId") String groupId, @PathParam("taskId") String taskId) {
        Group group = repository.getGroup(groupId);
        Task task = repository.getTask(taskId);
        ControllerResponse controller = ControllerResponse.create();

        NotFound.isGroupFound(group, groupId, controller); // Comprobamos que existe un grupo con la id dada.
        NotFound.isTaskFound(task, taskId, controller); // Comprobamos que existe una tarea con la id dada.
        NotFound.isTaskFoundInGroup(group, task, controller);
        if (Boolean.TRUE.equals(controller.hasError())) return controller.getMessage();

        repository.deleteTaskToGroup(groupId, taskId);

        return Response.noContent().build();
    }

}
