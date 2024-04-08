package pt.unl.fct.di.apdc.firstwebapp.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NewPasswordData {
    public String oldPassword;
    public String newPassword;
    public String confirmation;
    public AuthToken token;

    public NewPasswordData() {}

    @JsonCreator
    public NewPasswordData(@JsonProperty("oldPassword") String oldPassword, 
    @JsonProperty("newPassword") String newPassword, @JsonProperty("confirmation") String confirmation, 
    @JsonProperty("token") AuthToken token) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmation = confirmation;
        this.token = token;
    }

    public boolean validRequest() {
        return oldPassword != null && newPassword != null && confirmation != null && 
        token != null && newPassword.equals(confirmation) && !oldPassword.equals(newPassword);
    }
}
