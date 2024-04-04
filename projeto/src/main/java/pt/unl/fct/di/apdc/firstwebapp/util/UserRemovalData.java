package pt.unl.fct.di.apdc.firstwebapp.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRemovalData {
    public String username;
    public String targetUsername;
    public AuthToken token;

    public UserRemovalData() {}

    @JsonCreator
    public UserRemovalData(@JsonProperty("username") String username, @JsonProperty("targetUsername") String targetUsername,
     @JsonProperty("token") AuthToken token) {
        this.username = username;
        this.targetUsername = targetUsername;
        this.token = token;
    }

    public boolean validRequest() {
        return username != null && targetUsername != null && token != null;
    }

}
