package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;

import pt.unl.fct.di.apdc.firstwebapp.util.AuthToken;
import pt.unl.fct.di.apdc.firstwebapp.util.LoginData;
import pt.unl.fct.di.apdc.firstwebapp.util.States;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.Entity;
import com.google.gson.Gson;



@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LoginResource {

    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    
    Gson g = new Gson();
    
    /**
     * Logger Object
     */
    private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

    public LoginResource() { } // Nothing to be done here 

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginData data) {
        Transaction txn = datastore.newTransaction();
        Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.username);
        Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);

        try {
            LOG.fine("Login attempt by user: " + data.username);

            if(!data.validRegistration())
                return Response.status(Response.Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();

            Entity user = datastore.get(userKey);

            if(user == null)
                return Response.status(Response.Status.FORBIDDEN).entity("User does not exist.").build();

            if(!user.getString("password").equals(DigestUtils.sha256Hex(data.password)))
                return Response.status(Response.Status.FORBIDDEN).entity("Incorrect password.").build();

            if(user.getString("state").equals(States.INACTIVE.toString()))
                return Response.status(Response.Status.FORBIDDEN).entity("User is not active.").build();

            AuthToken token;
            Entity tokenEntity = txn.get(tokenKey);

            if(tokenEntity == null || tokenEntity.getLong("expirationData") < System.currentTimeMillis()) {
                token = new AuthToken(data.username);
                tokenEntity = Entity.newBuilder(tokenKey)
                .set("tokenID", token.tokenID)
                .set("creationData", token.creationData)
                .set("expirationData", token.expirationData)
                .build();
                txn.put(tokenEntity);
                txn.commit();
            }
            else {
                token = new AuthToken(data.username, tokenEntity.getString("tokenID"), tokenEntity.getLong("creationData"),
                tokenEntity.getLong("expirationData"));
            }

            LOG.info("User " + data.username + " logged in.");
            return Response.ok(g.toJson(token)).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Something went wrong. Try again later.").build();
        
        } finally {
            if(txn.isActive())
                txn.rollback();
        }
    }

}