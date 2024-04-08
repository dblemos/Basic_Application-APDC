package pt.unl.fct.di.apdc.firstwebapp.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenData {
    public AuthToken token;

    public TokenData() {}

    @JsonCreator
    public TokenData(@JsonProperty("token") AuthToken token) {
        this.token = token;
    }

    public boolean validAttempt() {
        return token != null;
    }
}
