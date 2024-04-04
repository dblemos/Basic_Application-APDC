package pt.unl.fct.di.apdc.firstwebapp.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.annotation.JsonCreator;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;


@Path("/list")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ListUsersResource {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    
    public ListUsersResource() { } // Nothing to be done here

    @POST
    @Path("/users")
    @JsonCreator
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listUsers() {
        return Response.status(Response.Status.OK).entity("List of users").build();
    }

}
