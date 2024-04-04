package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import autovalue.shaded.kotlin.collections.builders.MapBuilder;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import com.google.gson.Gson;

import org.apache.commons.codec.digest.DigestUtils;
import pt.unl.fct.di.apdc.firstwebapp.resources.util.AuthToken;
import pt.unl.fct.di.apdc.firstwebapp.resources.util.LoginData;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LoginResource {
    /**
     * Logger Object
     */
    private static final Logger LOG = Logger.getLogger(ComputationResource.class.getName());

    private final Gson g = new Gson();
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");

    public LoginResource(){}

    @POST
    @Path("/v2")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response doLogin(LoginData data, @Context HttpServletRequest request, @Context HttpHeaders headers){
        LOG.fine("Attempt to login user: "+ data.username);
        //Keys should be generated outside transactions
        //Construct the key from the username
        Key userKey = userKeyFactory.newKey(data.username);
        Key ctrsKey = datastore.newKeyFactory().addAncestor(PathElement.of("User", data.username))
                .setKind("UserStats").newKey("counters");
        //Generate automatically a key
        Key logKey = datastore.allocateId(
                datastore.newKeyFactory().addAncestor(PathElement.of("User", data.username))
                        .setKind("UserLog").newKey());

        Transaction txn = datastore.newTransaction();
        try{
            Entity user = txn.get(userKey);
            if( user == null){
                //Username does not exist
                LOG.warning("Failed login attempt for username: " + data.username);
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            //We get the suer stats from the storage
            Entity stats = txn.get(ctrsKey);
            if(stats == null){
                stats = Entity.newBuilder(ctrsKey)
                        .set("user_stats_logins", 0L)
                        .set("user_stats_failed", 0L)
                        .set("user_first_login", Timestamp.now())
                        .set("user_last_login", Timestamp.now())
                        .build();
            }
            String hashedPWD = (String) user.getString("user_pwd");
            if(hashedPWD.equals(DigestUtils.sha512Hex(data.password))){
                //Password is correct
                //Construct the logs
                Entity log = Entity.newBuilder(logKey)
                        .set("user_login_ip", request.getRemoteAddr())
                        .set("user_login_host", request.getRemoteHost())
                        .set("user_login_latlon",
                                //Does not index this property value
                                StringValue.newBuilder(headers.getHeaderString("X-AppEngine-CityLatLong"))
                                        .setExcludeFromIndexes(true).build()
                                )
                                        .set("user_login_city", headers.getHeaderString("X-AppEngine-City"))
                                        .set("user_login_country", headers.getHeaderString("X-AppEngine-Country"))
                                        .set("user_login_time", Timestamp.now())
                                        .build();
                //Get the user statistics and update it
                //Copying information every time a user logins maybe is not a good solution (why?)
                Entity ustats = Entity.newBuilder(ctrsKey)
                        .set("user_stats_logins", 1L + stats.getLong("user_stats_logins"))
                        .set("user_stats_failed", 0L)
                        .set("user_first_login", stats.getTimestamp("user_first_login"))
                        .set("user_last_login", Timestamp.now())
                        .build();

                //Batch operation
                txn.put(log, ustats);
                txn.commit();

                //Return token
                AuthToken token = new AuthToken(data.username);
                LOG.info("User '" + data.username+ "' logged in successfully.");
                return Response.ok(g.toJson(token)).build();

            }else{
                //Incorrect passowrd
                //TODO: Copying here is even worse. Propose a better solution!
                Entity ustats = Entity.newBuilder(ctrsKey)
                        .set("user_stats_logins", stats.getLong("user_stats_logins"))
                        .set("user_stats_failed", 1L + stats.getLong("user_stats_failed"))
                        .set("user_first_login", stats.getTimestamp("user_first_login"))
                        .set("user_last_login", stats.getTimestamp("user_last_login"))
                        .set("user_last_attempt", Timestamp.now())
                        .build();
                txn.put(ustats);
                txn.commit();
                LOG.warning("Wrong password for username: " + data.username);
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        }catch(Exception e) {
                txn.rollback();
                LOG.severe(e.toString());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            if(txn.isActive()){
                txn.rollback();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
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



    @GET
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response queryLoginTime(LoginData data ){

        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("UserLog")
                .setFilter(
                        StructuredQuery.CompositeFilter.and(StructuredQuery.PropertyFilter.hasAncestor(
                                datastore.newKeyFactory().setKind("User").newKey(data.username)),
                                StructuredQuery.PropertyFilter.ge("user_login_time", "yesterday")
                        )
                )
                .setOrderBy(StructuredQuery.OrderBy.desc("user_login_time"))
                .setLimit(3)
                .build();
        datastore.run(query);
        return Response.ok().build();
    }


}
