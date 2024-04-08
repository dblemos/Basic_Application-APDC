package pt.unl.fct.di.apdc.firstwebapp.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserData {
    public String username, email, name, phoneNumber, profile, ocupation, workplace, address, zipCode, taxNumber, role, state;

    @JsonCreator
    public UserData(@JsonProperty("username") String username, @JsonProperty("email") String email, @JsonProperty("name") String name, 
    @JsonProperty("phoneNumber") String phoneNumber, @JsonProperty("profile") String profile, @JsonProperty("ocupation") String ocupation,
    @JsonProperty("workplace") String workplace, @JsonProperty("address") String address, @JsonProperty("zipCode") String zipCode,
    @JsonProperty("taxNumber") String taxNumber, @JsonProperty("role") String role, @JsonProperty("state") String state){
        this.username = username;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profile = profile;
        this.ocupation = ocupation;
        this.workplace = workplace;
        this.address = address;
        this.zipCode = zipCode;
        this.taxNumber = taxNumber;
        this.role = role;
        this.state = state;
    }

    @JsonCreator
    public UserData(@JsonProperty("username") String username, @JsonProperty("email") String email, @JsonProperty("name") String name) {
        this.username = username;
        this.email = email;
        this.name = name;
    }
}
