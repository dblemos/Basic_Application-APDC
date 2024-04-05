package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.annotation.JsonCreator;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;

import pt.unl.fct.di.apdc.firstwebapp.util.UserRemovalData;
import pt.unl.fct.di.apdc.firstwebapp.util.Roles;

@Path("/remove")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class UserRemovalResource {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    
    /**
     * Logger Object
     */
    private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
    
    public UserRemovalResource() { } // Nothing to be done here

    @POST
    @Path("/user")
    @JsonCreator
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeUser(UserRemovalData data) {
        LOG.fine("User removal attempt by user: " + data.token.username);
        Transaction txn = datastore.newTransaction();
        Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.token.username);
        Key targetKey = datastore.newKeyFactory().setKind("User").newKey(data.targetUsername);
        Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.token.username);

        try {
            if(!data.validRequest())
                return Response.status(Response.Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();
            
            Entity user = txn.get(userKey);
            Entity targetUser = txn.get(targetKey);

            if(user == null || targetUser == null)
                return Response.status(Response.Status.FORBIDDEN).entity("User does not exist.").build();

            Entity token = txn.get(tokenKey);

            if(token == null || token.getLong("expirationData") < System.currentTimeMillis())
                return Response.status(Response.Status.FORBIDDEN).entity("User is not logged in.").build();

            if(!data.token.tokenID.equals(token.getString("tokenID")))
                return Response.status(Response.Status.FORBIDDEN).entity("Invalid token.").build();

            if(data.token.username.equals(data.targetUsername) && (user.getString("role").equals(Roles.GA.toString()) ||
                user.getString("role").equals(Roles.GBO.toString())))
                return Response.status(Response.Status.FORBIDDEN).entity("Non authorized operation.").build();
            
            if(!data.token.username.equals(data.targetUsername) && user.getString("role").equals(Roles.USER.toString()))
                return Response.status(Response.Status.FORBIDDEN).entity("Non authorized operation.").build();

            if(!Roles.canRemoveUser(user.getString("role"), targetUser.getString("role")))
                return Response.status(Response.Status.FORBIDDEN).entity("Non authorized operation.").build();

            Key targetTokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.targetUsername);
            txn.delete(targetTokenKey);

            txn.delete(targetKey);
            txn.commit();
            return Response.ok().entity("{}").build();

        }   catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Something went wrong.").build();

        }   finally {
            if(txn.isActive())
                txn.rollback();
        }
    }
}
