package pt.unl.fct.di.apdc.firstwebapp.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LogoutData {
    public AuthToken token;

    public LogoutData() {}

    @JsonCreator
    public LogoutData(@JsonProperty("token") AuthToken token) {
        this.token = token;
    }

    public boolean validAttempt() {
        return token != null;
    }
}
