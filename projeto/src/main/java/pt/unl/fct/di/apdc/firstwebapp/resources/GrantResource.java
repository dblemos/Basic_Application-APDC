package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;

import pt.unl.fct.di.apdc.firstwebapp.util.GrantRoleData;
import pt.unl.fct.di.apdc.firstwebapp.util.GrantStateData;
import pt.unl.fct.di.apdc.firstwebapp.util.Roles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

@Path("/grant")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class GrantResource {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    
    /**
     * Logger Object
     */
    private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

    public GrantResource() { } // Nothing to be done here

    @POST
    @Path("/role")
    @JsonCreator
    @Consumes(MediaType.APPLICATION_JSON)
    public Response grantRole(GrantRoleData data) {
        Transaction txn = datastore.newTransaction();
        Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.token.username);
        Key targetKey = datastore.newKeyFactory().setKind("User").newKey(data.targetUsername);
        Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.token.username);

        try {
            LOG.fine("Role grant attempt by user: " + data.token.username);

            if(!data.validGrant())
                return Response.status(Response.Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();

            if(!data.validRole())
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid role.").build();

            Entity user = txn.get(userKey);
            Entity targetUser = txn.get(targetKey);

            if(user == null || targetUser == null)
                return Response.status(Response.Status.FORBIDDEN).entity("User does not exist.").build();
            
            Entity token = txn.get(tokenKey);

            if(token == null || token.getLong("expirationData") < System.currentTimeMillis())
                return Response.status(Response.Status.FORBIDDEN).entity("User is not logged in.").build();

            if(!data.token.tokenID.equals(token.getString("tokenID")))
                return Response.status(Response.Status.FORBIDDEN).entity("Invalid token.").build();

            if(user.getString("role").equals(Roles.GBO.toString()) || user.getString("role").equals(Roles.USER.toString()))
                return Response.status(Response.Status.FORBIDDEN).entity("User does not have permission to grant roles.").build();

            if(!Roles.canGrantRole(user.getString("role"), targetUser.getString("role"), data.role.toUpperCase()))
                return Response.status(Response.Status.FORBIDDEN).entity("User does not have permission to grant the given role.").build();

            targetUser = Entity.newBuilder(targetKey, targetUser)
            .set("role", data.role.toUpperCase())
            .build();

            txn.update(targetUser);
            txn.commit();
            return Response.ok().entity("{}").build();

        } catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Something went wrong.").build();

        } finally {
            if(txn.isActive())
                txn.rollback();
        }
    }

    @POST
    @Path("/state")
    @JsonCreator
    @Consumes(MediaType.APPLICATION_JSON)
    public Response grantState(GrantStateData data) {
        Transaction txn = datastore.newTransaction();
        Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.token.username);
        Key targetKey = datastore.newKeyFactory().setKind("User").newKey(data.targetUsername);
        Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.token.username);

        try {
            LOG.fine("State grant attempt by user: " + data.token.username);

            if(!data.validGrant())
                return Response.status(Response.Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();

            if(!data.validState())
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid state.").build();

            Entity user = txn.get(userKey);
            Entity targetUser = txn.get(targetKey);

            if(user == null || targetUser == null)
                return Response.status(Response.Status.FORBIDDEN).entity("User does not exist.").build();
            
            Entity token = txn.get(tokenKey);

            if(token == null || token.getLong("expirationData") < System.currentTimeMillis())
                return Response.status(Response.Status.FORBIDDEN).entity("User is not logged in.").build();

            if(!data.token.tokenID.equals(token.getString("tokenID")))
                return Response.status(Response.Status.FORBIDDEN).entity("Invalid token.").build();
            
            if(user.getString("role").equals(Roles.USER.toString()))
                return Response.status(Response.Status.FORBIDDEN).entity("User does not have permission to grant states.").build();

            if(!Roles.canGrantState(user.getString("role"), targetUser.getString("role")))
                return Response.status(Response.Status.FORBIDDEN).entity("User does not have permission to grant the given state to the target user.").build();

            targetUser = Entity.newBuilder(targetKey, targetUser)
            .set("state", data.state.toUpperCase())
            .build();

            txn.update(targetUser);
            txn.commit();
            return Response.ok().entity("{}").build();

        } catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Something went wrong.").build();

        } finally {
            if(txn.isActive())
                txn.rollback();
        }
    }
}
