package pt.unl.fct.di.apdc.firstwebapp.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ModifyAttributesData {
    public String targetUsername, phoneNumber, password, email, name, profile, ocupation, workplace, address, zipCode, taxNumber;
    public AuthToken token;

    public ModifyAttributesData() {}

    @JsonCreator
    public ModifyAttributesData(@JsonProperty("targetUsername") String targetUsername, @JsonProperty("email") String email, @JsonProperty("name") String name, 
    @JsonProperty("phoneNumber") String phoneNumber, @JsonProperty("password") String password, @JsonProperty("profile") String profile, @JsonProperty("ocupation") String ocupation,
    @JsonProperty("workplace") String workplace, @JsonProperty("address") String address, @JsonProperty("zipCode") String zipCode,
    @JsonProperty("taxNumber") String taxNumber, @JsonProperty("token") AuthToken token) {
        this.targetUsername = targetUsername;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.profile = profile;
        this.ocupation = ocupation;
        this.workplace = workplace;
        this.address = address;
        this.zipCode = zipCode;
        this.taxNumber = taxNumber;
        this.token = token;
    }

    public boolean validRequest() {
        return targetUsername != null && token != null;
    }
}
