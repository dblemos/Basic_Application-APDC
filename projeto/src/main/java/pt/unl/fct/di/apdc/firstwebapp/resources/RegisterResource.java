package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;
import pt.unl.fct.di.apdc.firstwebapp.util.Roles;
import pt.unl.fct.di.apdc.firstwebapp.util.States;
import pt.unl.fct.di.apdc.firstwebapp.util.Profile;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;

import pt.unl.fct.di.apdc.firstwebapp.util.RegisterData;
import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.Entity;


@Path("/register")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RegisterResource {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());

    public RegisterResource() { } // Nothing to be done here


    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(RegisterData data) {
        LOG.fine("Register attempt by user: " + data.username);

        
        if(!data.validRegistration()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();
        }
        else if(!data.validConfirmation()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Passwords do not match.").build();
        }

        Transaction txn = datastore.newTransaction();

        try {

            Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
            Entity user = txn.get(userKey);

            // Check if the user already exists
            if( user != null ) {
                txn.rollback();
                return Response.status(Response.Status.BAD_REQUEST).entity("User already exists.").build();
            } else {

                user = Entity.newBuilder(userKey)
                .set("username", data.username)
                .set("email", data.email)
                .set("name", data.name)
                .set("phoneNumber", data.phoneNumber)
                .set("password", DigestUtils.sha256Hex(data.password))
                .set("role", Roles.USER.toString())
                .set("state", States.INACTIVE.toString())
                .set("profile", data.profile == null ? Profile.PRIVATE.toString() : data.profile.toUpperCase())
                .set("ocupation", data.ocupation == null ? "" : data.ocupation)
                .set("workplace", data.workplace == null ? "" : data.workplace)
                .set("address", data.address == null ? "" : data.address)
                .set("zipCode", data.zipCode == null ? "" : data.zipCode)
                .set("taxNumber", data.taxNumber == null ? "" : data.taxNumber)
                .build();

                txn.put(user);
                LOG.info("User registered: " + data.username);
                txn.commit();
                return Response.ok("{}").build();
            }
        } finally {
            if(txn.isActive()) {
                txn.rollback();
            }
        }
    }

    @POST
    @Path("/su")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerSU(RegisterData data) {
        Transaction txn = datastore.newTransaction();

        try {

            Key userKey = datastore.newKeyFactory().setKind("User").newKey("root");
            Entity user = txn.get(userKey);

            // Check if the user already exists
            if( user != null ) {
                txn.rollback();
                return Response.status(Response.Status.BAD_REQUEST).entity("SU already exists.").build();
            } else {

                user = Entity.newBuilder(userKey)
                .set("username", "root")
                .set("email", "")
                .set("name", "")
                .set("phoneNumber", "")
                .set("password", DigestUtils.sha256Hex("root"))
                .set("role", Roles.SU.toString())
                .set("state", States.ACTIVE.toString())
                .set("profile", Profile.PRIVATE.toString())
                .set("ocupation", "")
                .set("workplace", "")
                .set("address", "")
                .set("zipCode", "")
                .set("taxNumber", "")
                .build();

                txn.put(user);
                LOG.info("SU registered");
                txn.commit();
                return Response.ok("{}").build();
            }
        } finally {
            if(txn.isActive()) {
                txn.rollback();
            }
        }
    }
}
