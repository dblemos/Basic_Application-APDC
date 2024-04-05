package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;

import pt.unl.fct.di.apdc.firstwebapp.util.TokenData;
import pt.unl.fct.di.apdc.firstwebapp.util.ControlData;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.Entity;
import com.google.gson.Gson;

@Path("/show")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ShowControlAttribResource {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    Gson g = new Gson();

    private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());

    public ShowControlAttribResource() { } // Nothing to be done here

    @POST
    @Path("/controlAttributes")
    @JsonCreator
    @Consumes(MediaType.APPLICATION_JSON)
    public Response showControlAttributes(TokenData data) {
        LOG.fine("Show control Attributes attempt by user: " + data.token.username);

        if(!data.validAttempt())
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();

        Transaction txn = datastore.newTransaction();
        Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.token.username);
        Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.token.username);

        try {

            Entity token = txn.get(tokenKey);
       
            if(token == null || token.getLong("expirationData") < System.currentTimeMillis())
                return Response.status(Response.Status.FORBIDDEN).entity("User is not logged in.").build();

            if(!data.token.tokenID.equals(token.getString("tokenID")))
                return Response.status(Response.Status.FORBIDDEN).entity("Invalid token.").build();

            Entity user = txn.get(userKey);

            if(user == null)
                return Response.status(Response.Status.FORBIDDEN).entity("User does not exist.").build();

            ControlData control = new ControlData(data.token, user.getString("role"), user.getString("state"));

            return Response.ok(g.toJson(control)).build();

        }   catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception.").build();

        } finally {
            if (txn.isActive())
                txn.rollback();
        }
    }
}
