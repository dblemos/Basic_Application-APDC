package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.digest.DigestUtils;

import pt.unl.fct.di.apdc.firstwebapp.util.ModifyAttributesData;
import pt.unl.fct.di.apdc.firstwebapp.util.Roles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.Entity;

@Path("/modify")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ModifyAttributesResource {
    
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    
    /**
     * Logger Object
     */
    private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

    public ModifyAttributesResource() { } // Nothing to be done here

    @POST
    @Path("/attributes")
    @JsonCreator
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyAttributes(ModifyAttributesData data) {
        LOG.info("Attempt to modify attributes: " + data.targetUsername);

        if (!data.validRequest())
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();

        Transaction txn = datastore.newTransaction();
        Key targetKey = datastore.newKeyFactory().setKind("User").newKey(data.targetUsername);
        Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.token.username);
        Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.token.username);

        try {
            Entity user = txn.get(userKey);
            Entity targetUser = txn.get(targetKey);
            
            if (user == null || targetUser == null)
                return Response.status(Response.Status.FORBIDDEN).entity("User does not exist.").build();

            Entity token = txn.get(tokenKey);

            if (token == null || token.getLong("expirationData") < System.currentTimeMillis())
                return Response.status(Response.Status.FORBIDDEN).entity("User is not logged in.").build();

            if(!canModify(user, targetUser, data))
                return Response.status(Response.Status.FORBIDDEN).entity("Non authorized operation.").build();

            targetUser = Entity.newBuilder(targetKey, targetUser)
                .set("email", data.email != "" ? data.email : targetUser.getString("email"))
                .set("name", data.name != "" ? data.name : targetUser.getString("name"))
                .set("phoneNumber", data.phoneNumber != "" ? data.phoneNumber : targetUser.getString("phoneNumber"))
                .set("password", data.password != "" ? DigestUtils.sha256Hex(data.password) : targetUser.getString("password"))
                .set("profile", data.profile != "" ? data.profile.toUpperCase() : targetUser.getString("profile"))
                .set("ocupation", data.ocupation != "" ? data.ocupation : targetUser.getString("ocupation"))
                .set("workplace", data.workplace != "" ? data.workplace : targetUser.getString("workplace"))
                .set("address", data.address != "" ? data.address : targetUser.getString("address"))
                .set("zipCode", data.zipCode != "" ? data.zipCode : targetUser.getString("zipCode"))
                .set("taxNumber", data.taxNumber != "" ? data.taxNumber : targetUser.getString("taxNumber"))
                .build();

            txn.update(targetUser);
            txn.commit();
            return Response.status(Response.Status.OK).entity("{}").build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Something went wrong.").build();

        } finally {
            if (txn.isActive())
                txn.rollback();
        }
    }

    private boolean canModify(Entity user, Entity targetUser, ModifyAttributesData data) {
        if(user.getString("role").equals(Roles.USER.toString())) {
            return (user.getString("username").equals(data.targetUsername) && !(data.email != null || data.name != null));
        } else if(user.getString("role").equals(Roles.GBO.toString()))
            return targetUser.getString("role").equals(Roles.USER.toString());
        else
            return Roles.valueOf(user.getString("role")).getAutority() > Roles.valueOf(targetUser.getString("role")).getAutority();
    }
}
