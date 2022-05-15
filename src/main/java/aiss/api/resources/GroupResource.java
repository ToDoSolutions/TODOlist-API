package aiss.api.resources;

import aiss.model.Group;
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

@Path("/groups")
public class GroupResource {

    protected static GroupResource instance = null; // La instancia inicialmente no existe, se crea al ejecutar .getInstance().
    final Repository repository; // Para poder trabajar con los datos

    private GroupResource() {
        repository = MapRepository.getInstance();
    }

    public static GroupResource getInstance() {
        // Creamos una instancia si no existe.
        instance = (instance == null) ? new GroupResource() : instance;
        return instance;
    }

    @GET
    @Produces("application/json")
    public Response getAll(@QueryParam("order") String order,
                           @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                           @QueryParam("fieldsGroup") String fieldsGroup, @QueryParam("fieldsUser") String fieldsUser,
                           @QueryParam("fieldsTask") String fieldsTask, @QueryParam("name") String name,
                           @QueryParam("description") String description, @QueryParam("numTasks") String numTasks,
                           @QueryParam("createdDate") String createdDate) {
        List<Group> result = new ArrayList<>(), groups = new ArrayList<>(repository.getAllGroup()); // No se puede utilizar .toList() porque eso es a partir de Java 16.
        if (order != null)
            Order.sequenceGroup(groups, order);
        int start = offset == null ? 0 : offset - 1; // Donde va a comenzar.
        int end = limit == null || limit > groups.size() ? groups.size() : start + limit; // Donde va a terminar.

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
    @Produces("application/json")
    public Response getGroup(@PathParam("groupId") String groupId, @QueryParam("fieldsGroup") String fieldsGroup,
                             @QueryParam("fieldsUser") String fieldsUser, @QueryParam("fieldsTask") String fieldsTask) {
        Group group = repository.getGroup(groupId);

        // Comprobamos si se encuentra el objeto en la base de datos.
        Response response = Message.groupNotFound(group, groupId);
        if (response != null) return response;

        return Response.ok(group.getFields((fieldsGroup == null ? Group.ALL_ATTRIBUTES : fieldsGroup), fieldsUser, fieldsTask)).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addGroup(@Context UriInfo uriInfo, Group group) {
        // Comprobamos aquellos campos obligatorios.
        Response response = Message.requiredForGroup(group);
        if (response != null) return response;

        // Comprobamos si algún campo no es correcto.
        response = Message.checkGroup(group);
        if (response != null) return response;

        repository.addGroup(group); // Añadimos el usuario a la base de datos.

        // Builds the response. Returns the user the has just been added.
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "getUser");
        URI uri = ub.build(group.getIdGroup());
        ResponseBuilder resp = Response.created(uri);
        resp.entity(group);
        return resp.build();
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateGroup(Group group) {
        // Comprobamos que nos ha dado una id.
        Response response = Message.groupIdRequired(group);
        if (response != null) return response;

        Group oldGroup = repository.getGroup(group.getIdGroup());

        // Comprobamos si se encuentra el objeto en la base de datos.
        response = Message.groupNotFound(oldGroup, group.getIdGroup());
        if (response != null) return response;

        // Comprobamos si algún campo no es correcto.
        response = Message.checkGroup(group);
        if (response != null) return response;

        Update.groupFromOther(group, oldGroup); // Actualiza los atributos del modelo.
        repository.updateGroup(oldGroup);  // No está en la práctica 7, pero deduzco que falta, ya que de la otra manera no se actualiza en la base de datos.

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{groupId}")
    @Produces("application/json")
    public Response deleteGroup(@PathParam("groupId") String groupId) {
        Group toBeRemoved = repository.getGroup(groupId); // Obtiene el modelo a eliminar de la base de datos chapucera.

        // Comprobamos si se encuentra el grupo en la base de datos.
        Response response = Message.groupNotFound(toBeRemoved, groupId);
        if (response != null) return response;

        // Elimina el usuario de la base de datos chapucera.
        repository.deleteGroup(groupId);

        return Response.noContent().build();
    }

    @POST
    @Path("/{groupId}/{userId}")
    @Produces("application/json")
    public Response addUserToGroup(@Context UriInfo uriInfo, @PathParam("userId") String userId, @PathParam("groupId") String groupId) {
        Group group = repository.getGroup(groupId);
        User user = repository.getUser(userId);

        // Comprobamos si el grupo tiene al usuario dado.
        Response response = Message.isUserInGroup(group, user, groupId, userId);
        if (response != null) return response;

        repository.addTaskToUser(groupId, userId);

        // Builds the response
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "getGroup");
        URI uri = ub.build(groupId);
        ResponseBuilder resp = Response.created(uri);
        resp.entity(group);
        return resp.build();
    }

    @DELETE
    @Path("/{groupId}/{userId}")
    @Produces("application/json")
    public Response deleteUserToGroup(@PathParam("groupId") String groupId, @PathParam("userId") String userId) {
        Group group = repository.getGroup(groupId);
        User user = repository.getUser(userId);

        // Comprobamos que existe un grupo con la id dada.
        Response response = Message.groupNotFound(group, groupId);
        if (response != null) return response;

        // Comprobamos que existe un usuario con la id dada.
        response = Message.userNotFound(user, userId);
        if (response != null) return response;

        repository.deleteUserToGroup(groupId, userId);

        return Response.noContent().build();
    }

    @POST
    @Path("/{groupId}/{taskId}")
    @Produces("application/json")
    public Response addTaskToGroup(@Context UriInfo uriInfo, @PathParam("taskId") String taskId, @PathParam("groupId") String groupId) {
        Group group = repository.getGroup(groupId);
        Task task = repository.getTask(taskId);

        // Comprobamos que existe un grupo con la id dada.
        Response response = Message.groupNotFound(group, groupId);
        if (response != null) return response;

        // Comprobamos que existe una tarea con la id dada.
        response = Message.taskNotFound(task, taskId);
        if (response != null) return response;

        // >Comprobamos que existe una tarea con la id dada.
        response = Message.taskNotFound(task, taskId);
        if (response != null) return response;

        repository.addTaskToGroup(groupId, taskId);

        // Builds the response
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "getGroup");
        URI uri = ub.build(groupId);
        ResponseBuilder resp = Response.created(uri);
        resp.entity(group);
        return resp.build();
    }

    @DELETE
    @Path("/{groupId}/{taskId}")
    @Produces("application/json")
    public Response deleteTaskToGroup(@PathParam("groupId") String groupId, @PathParam("taskId") String taskId) {
        Group group = repository.getGroup(groupId);
        Task task = repository.getTask(taskId);

        // Comprobamos que existe un grupo con la id dada.
        Response response = Message.groupNotFound(group, groupId);
        if (response != null) return response;

        // Comprobamos que existe una tarea con la id dada.
        response = Message.taskNotFound(task, taskId);
        if (response != null) return response;

        // >Comprobamos que existe una tarea con la id dada.
        response = Message.taskNotFound(task, taskId);
        if (response != null) return response;

        repository.deleteTaskToGroup(groupId, taskId);

        return Response.noContent().build();
    }

}
