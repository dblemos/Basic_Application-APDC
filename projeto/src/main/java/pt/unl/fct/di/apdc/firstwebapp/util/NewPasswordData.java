package pt.unl.fct.di.apdc.firstwebapp.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NewPasswordData {
    public String username;
    public String oldPassword;
    public String newPassword;
    public String confirmation;
    public AuthToken token;

    public NewPasswordData() {}

    @JsonCreator
    public NewPasswordData(@JsonProperty("username") String username, @JsonProperty("oldPassword") String oldPassword, 
    @JsonProperty("newPassword") String newPassword, @JsonProperty("confirmation") String confirmation, 
    @JsonProperty("token") AuthToken token) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmation = confirmation;
        this.token = token;
    }

    public boolean validRequest() {
        return username != null && oldPassword != null && newPassword != null && confirmation != null && 
        token != null && newPassword.equals(confirmation) && !oldPassword.equals(newPassword);
    }
}
