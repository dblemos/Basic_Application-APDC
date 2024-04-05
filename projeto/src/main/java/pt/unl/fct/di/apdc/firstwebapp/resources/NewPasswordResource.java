package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Transaction;

import pt.unl.fct.di.apdc.firstwebapp.util.NewPasswordData;
import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Entity;

@Path("/new")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class NewPasswordResource {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    
    /**
     * Logger Object
     */
    private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
    
    public NewPasswordResource() { } // Nothing to be done here

    @POST
    @Path("/password")
    @JsonCreator
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newPassword(NewPasswordData data) {
        LOG.fine("User removal attempt by user: " + data.username);
        Transaction txn = datastore.newTransaction();
        Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
        Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.username);

        try {
            if(!data.validRequest())
                return Response.status(Response.Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();

            Entity user = txn.get(userKey);

            if(user == null)
                return Response.status(Response.Status.FORBIDDEN).entity("User does not exist.").build();

            Entity token = txn.get(tokenKey);

            if(token == null || token.getLong("expirationData") < System.currentTimeMillis())
                return Response.status(Response.Status.FORBIDDEN).entity("User is not logged in.").build();

            if(!data.token.tokenID.equals(token.getString("tokenID")))
                return Response.status(Response.Status.FORBIDDEN).entity("Invalid token.").build();

            if(!user.getString("password").equals(DigestUtils.sha256Hex(data.oldPassword)))
                return Response.status(Response.Status.FORBIDDEN).entity("Invalid password.").build();
            
            user = Entity.newBuilder(userKey, user)
                .set("password", DigestUtils.sha256Hex(data.newPassword))
                .build();

            txn.update(user);
            txn.commit();
            return Response.status(Response.Status.OK).entity("{}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Something went wrong. Please try again.").build();
        
        } finally {
            if(txn.isActive())
                txn.rollback();
        }
    }
}
