package pt.unl.fct.di.apdc.firstwebapp.resources.util;

public class RegisterData {

    public String username, password, email, name;


    public RegisterData(){

    }

    public RegisterData(String username, String password, String email, String name){
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;

    }
    public boolean validRegistration(){
        return !(username == null && password == null);
    }

}
