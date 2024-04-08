package pt.unl.fct.di.apdc.firstwebapp.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ControlData {
    public String role, state;
    public AuthToken token;

    @JsonCreator
    public ControlData(@JsonProperty("token") AuthToken token,
    @JsonProperty("role") String role, @JsonProperty("state") String state) {
        this.role = role;
        this.state = state;
        this.token = token;
    }
}
