package pt.unl.fct.di.apdc.firstwebapp.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GrantStateData {
    
    public String state;
    public String targetUsername;
    public AuthToken token;

    public GrantStateData() {}

    @JsonCreator
    public GrantStateData(@JsonProperty("targetUsername") String targetUsername, 
    @JsonProperty("state") String state, @JsonProperty("token") AuthToken token) {
        this.targetUsername = targetUsername;
        this.state = state;
        this.token = token;
    }

    public boolean validGrant() {
        return state != null && targetUsername != null && token != null
        && token.username != targetUsername;
    }

    public boolean validState() {
        return state.toUpperCase().equals(States.ACTIVE.toString()) || state.equals(States.INACTIVE.toString());
    }
    
}
