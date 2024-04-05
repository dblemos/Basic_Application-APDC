package pt.unl.fct.di.apdc.firstwebapp.util;

public class RegisterData {
    public String username;
    public String email;
    public String name;
    public String phoneNumber;
    public String password;
    public String confirmation;
    public String role;
    public String state;
    public String profile;
    public String ocupation;
    public String workplace;
    public String address;
    public String zipCode;
    public String taxNumber;

    
    public RegisterData() {
    }

    public RegisterData(String username, String email, String name, String phoneNumber, String password, String confirmation, 
    String role, String state, String profile, String ocupation, String workplace, String address, String zipCode, String taxNumber) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.confirmation = confirmation;
        this.role = role;
        this.state = state;
        this.profile = profile;
        this.ocupation = ocupation;
        this.workplace = workplace;
        this.address = address;
        this.zipCode = zipCode;
        this.taxNumber = taxNumber;
    }

    public boolean validRegistration() {
        return username != null && email != null && name != null && phoneNumber != null && 
        password != null && confirmation != null;
    }

    public boolean validConfirmation() {
        return password.equals(confirmation);
    }
}
