package pt.unl.fct.di.apdc.firstwebapp.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GrantStateData {
    
    public String username;
    public String state;
    public String targetUsername;
    public AuthToken token;

    public GrantStateData() {}

    @JsonCreator
    public GrantStateData(@JsonProperty("username") String username, @JsonProperty("targetUsername") String targetUsername, 
    @JsonProperty("state") String state, @JsonProperty("token") AuthToken token) {
        this.username = username;
        this.targetUsername = targetUsername;
        this.state = state;
        this.token = token;
    }

    public boolean validGrant() {
        return username != null && state != null && targetUsername != null && token != null
        && username != targetUsername;
    }

    public boolean validState() {
        return state.toUpperCase().equals(States.ACTIVE.toString()) || state.equals(States.INACTIVE.toString());
    }
    
}
