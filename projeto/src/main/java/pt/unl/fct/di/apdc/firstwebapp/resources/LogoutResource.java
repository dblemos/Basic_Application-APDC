package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;

import pt.unl.fct.di.apdc.firstwebapp.util.LogoutData;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.Entity;

@Path("/logout")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LogoutResource {

    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());

    public LogoutResource() { } // Nothing to be done here

    @POST
    @Path("/user")
    @JsonCreator
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logoutUser(LogoutData data) {
        LOG.fine("Logout attempt by user: " + data.token.username);

        Transaction txn = datastore.newTransaction();
        Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.token.username);

        try {
            if(!data.validAttempt())
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();

            Entity token = txn.get(tokenKey);

            if(token == null || token.getLong("expirationData") < System.currentTimeMillis())
                return Response.status(Response.Status.FORBIDDEN).entity("User is not logged in.").build();

            if(!data.token.tokenID.equals(token.getString("tokenID")))
                return Response.status(Response.Status.FORBIDDEN).entity("Invalid token.").build();
                
            txn.delete(tokenKey);
            txn.commit();
            return Response.ok("{}").build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred.").build();

        } finally {
            if (txn.isActive()) {
                txn.rollback();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred.").build();
            }
        }
    }
}
