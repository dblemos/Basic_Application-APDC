package pt.unl.fct.di.apdc.firstwebapp.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.firstwebapp.util.Profile;
import pt.unl.fct.di.apdc.firstwebapp.util.Roles;
import pt.unl.fct.di.apdc.firstwebapp.util.States;
import pt.unl.fct.di.apdc.firstwebapp.util.TokenData;
import pt.unl.fct.di.apdc.firstwebapp.util.UserData;


@Path("/list")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ListUsersResource {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    Gson g = new Gson();
    
    public ListUsersResource() { } // Nothing to be done here

    @POST
    @Path("/users")
    @JsonCreator
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listUsers(TokenData data) {

        if (!data.validAttempt())
            return Response.status(Response.Status.FORBIDDEN).entity("Missing parameter.").build();

        Transaction txn = datastore.newTransaction();
        Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(data.token.username);
        Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.token.username);

        try {
            Entity token = txn.get(tokenKey);
            Entity user = txn.get(userKey);

            if (user == null)
                return Response.status(Response.Status.FORBIDDEN).entity("User does not exist.").build();

            if(token == null || token.getLong("expirationData") < System.currentTimeMillis())
                return Response.status(Response.Status.FORBIDDEN).entity("User is not logged in.").build();

            if(!data.token.tokenID.equals(token.getString("tokenID")))
                return Response.status(Response.Status.FORBIDDEN).entity("Invalid token.").build();

            Query<Entity> query = Query.newEntityQueryBuilder().setKind("User").build();
            QueryResults<Entity> results = datastore.run(query);

            ArrayList<UserData> users = new ArrayList<UserData>();

            if(user.getString("role").equals(Roles.USER.toString())) {
                while (results.hasNext()) {
                    Entity userEntity = results.next();
                    if(userEntity.getString("role").equals(Roles.USER.toString()) && 
                    userEntity.getString("state").equals(States.ACTIVE.toString()) &&
                    userEntity.getString("profile").equals(Profile.PUBLIC.toString())) {
                        UserData userData = new UserData(userEntity.getString("username"), 
                        userEntity.getString("email"), userEntity.getString("name"));
                        users.add(userData);
                    }
                }
            } else if(user.getString("role").equals(Roles.GBO.toString())) {
                while (results.hasNext()) {
                    Entity userEntity = results.next();
                    if(userEntity.getString("role").equals(Roles.USER.toString())) {
                        UserData userData = new UserData( userEntity.getString("username"), userEntity.getString("email"),
                        userEntity.getString("name"), userEntity.getString("phoneNumber"), userEntity.getString("profile"),
                        userEntity.getString("ocupation"), userEntity.getString("workplace"), userEntity.getString("address"),
                        userEntity.getString("zipCode"), userEntity.getString("taxNumber"), userEntity.getString("role"), userEntity.getString("state"));
                        users.add(userData);
                    }
                }
            } else {
                while (results.hasNext()) {
                    Entity userEntity = results.next();
                    if(Roles.valueOf(userEntity.getString("role")).getAutority() <= Roles.valueOf(user.getString("role")).getAutority()) {
                        UserData userData = new UserData( userEntity.getString("username"), userEntity.getString("email"),
                        userEntity.getString("name"), userEntity.getString("phoneNumber"), userEntity.getString("profile"),
                        userEntity.getString("ocupation"), userEntity.getString("workplace"), userEntity.getString("address"),
                        userEntity.getString("zipCode"), userEntity.getString("taxNumber"), userEntity.getString("role"), userEntity.getString("state"));
                        users.add(userData);
                    }
                }
            }
            
            return Response.ok(g.toJson(users)).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception.").build();
        
        } finally {
            if (txn.isActive())
                txn.rollback();
        }
    }
}
