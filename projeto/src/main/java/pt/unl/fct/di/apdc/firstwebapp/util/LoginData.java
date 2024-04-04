package pt.unl.fct.di.apdc.firstwebapp.util;

public class LoginData {
    public String username;
    public String password;

    public LoginData() {
    }

    public LoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean validRegistration() {
        return username != null && password != null;
    }
    
}
