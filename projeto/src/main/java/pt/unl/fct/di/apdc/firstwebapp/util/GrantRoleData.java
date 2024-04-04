package pt.unl.fct.di.apdc.firstwebapp.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GrantRoleData {
    
    public String username;
    public String role;
    public String target_username;
    public AuthToken token;

    public GrantRoleData() { }

    @JsonCreator
    public GrantRoleData(@JsonProperty("username") String username, @JsonProperty("target_username") String target_username, 
    @JsonProperty("role") String role, @JsonProperty("token") AuthToken token) {
        this.username = username;
        this.target_username = target_username;
        this.role = role;
        this.token = token;
    }

    public boolean validGrant() {
        return username != null && role != null && target_username != null && token != null;
    }

    public boolean validRole() {
        return role.toUpperCase().equals(Roles.USER.toString()) || role.equals(Roles.GBO.toString()) ||
        role.equals(Roles.GA.toString()) || role.equals(Roles.SU.toString());
    }
    
}
