package pt.unl.fct.di.apdc.firstwebapp.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GrantRoleData {
    
    public String role;
    public String targetUsername;
    public AuthToken token;

    public GrantRoleData() {}

    @JsonCreator
    public GrantRoleData(@JsonProperty("targetUsername") String targetUsername, 
    @JsonProperty("role") String role, @JsonProperty("token") AuthToken token) {
        this.targetUsername = targetUsername;
        this.role = role;
        this.token = token;
    }

    public boolean validGrant() {
        return role != null && targetUsername != null && token != null
        && token.username != targetUsername;
    }

    public boolean validRole() {
        return role.toUpperCase().equals(Roles.USER.toString()) || role.equals(Roles.GBO.toString()) ||
        role.equals(Roles.GA.toString()) || role.equals(Roles.SU.toString());
    }
    
}
