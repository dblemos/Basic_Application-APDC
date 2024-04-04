package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.Calendar;
import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

import javax.servlet.http.HttpServletRequest;

import pt.unl.fct.di.apdc.firstwebapp.util.AuthToken;
import pt.unl.fct.di.apdc.firstwebapp.util.LoginData;

import com.google.gson.Gson;
import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.PathElement;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.Transaction;



@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LoginResource {

    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    
    /**
     * Logger Object
     */
    private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

    private final Gson g = new Gson();

    public LoginResource() { } // Nothing to be done here 

    @POST
    @Path("/v1")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginData data) {
        LOG.fine("Login attempt by user: " + data.username);

        Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
        Entity user = datastore.get(userKey);

        if(user == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("User does not exist.").build();
        }

        if(!user.getString("password").equals(DigestUtils.sha256Hex(data.password))) {
            return Response.status(Response.Status.FORBIDDEN).entity("Incorrect password.").build();
        }

        if(!user.getString("state").equals("INACTIVE")) {
            return Response.status(Response.Status.FORBIDDEN).entity("User is not active.").build();
        }

        AuthToken at = new AuthToken(data.username);

        LOG.info("User " + data.username + " logged in.");
        return Response.ok("{}").entity(at).build();
    }

}