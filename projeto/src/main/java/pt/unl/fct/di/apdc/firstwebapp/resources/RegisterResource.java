package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;

import com.google.gson.Gson;

import org.apache.commons.codec.digest.DigestUtils;
import pt.unl.fct.di.apdc.firstwebapp.resources.util.RegisterData;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RegisterResource {
    /**
     * Logger Object
     */
    private static final Logger LOG = Logger.getLogger(ComputationResource.class.getName());

    private final Gson g = new Gson();
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    public RegisterResource(){}

    @POST
    @Path("/v1")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doRegistrationV1(RegisterData data){
        LOG.fine("Attempt to register user:" + data.username);
        if(!data.validRegistration())
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();
        Transaction txn = datastore.newTransaction();
        try{
            //Creates an entity user form the data. THe key is username
            Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
            Entity user = txn.get(userKey);
            if(user != null){
                txn.rollback();
                return Response.status(Response.Status.BAD_REQUEST).entity("User already eists.").build();
            }else{
                user = Entity.newBuilder(userKey)
                        .set("user_name", data.name)
                        .set("user_pwd", DigestUtils.sha512Hex(data.password))
                        .set("user_email", data.email)
                        .set("user_creation_time", Timestamp.now())
                        .build();

                txn.add(user);
                LOG.info("User registered "+ data.username);
                txn.commit();
                return Response.ok("{}").build();
            }

        }finally {
            if(txn.isActive()){
                txn.rollback();
            }
        }

    }

    @GET
    @Path("/{username}")
    public Response doLogin(@PathParam("username") String username){
        if(username.trim().equals("jleitao")){
            return Response.ok().entity(g.toJson(false)).build();
        }else{
            return Response.ok().entity(g.toJson(true)).build();
        }

    }

}
