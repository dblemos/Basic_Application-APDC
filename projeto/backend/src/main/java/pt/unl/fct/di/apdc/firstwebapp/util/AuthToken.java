package pt.unl.fct.di.apdc.firstwebapp.util;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthToken {

    public static final long EXPIRATION_TIME = 1000*60*60*2; //2h
    public String username;
    public String tokenID;
    public long creationData;
    public long expirationData;


    public AuthToken(String username) {
        this.username = username;
        this.tokenID = UUID.randomUUID().toString();
        this.creationData = System.currentTimeMillis();
        this.expirationData = this.creationData + AuthToken.EXPIRATION_TIME;
    }

    @JsonCreator
    public AuthToken(@JsonProperty("username") String username, @JsonProperty("tokenID") String tokenID, 
    @JsonProperty("creationData") long creationData, @JsonProperty("expirationData") long expirationData) {
        this.username = username;
        this.tokenID = tokenID;
        this.creationData = creationData;
        this.expirationData = expirationData;
    }
}