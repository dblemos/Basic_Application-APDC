package pt.unl.fct.di.apdc.firstwebapp.util;

public class RegisterData {
    public String username;
    public String email;
    public String name;
    public String phone_number;
    public String password;
    public String confirmation;
    public String role;
    public String state;
    public String private_profile;
    public String ocupation;
    public String workplace;
    public String address;
    public String zip_code;
    public String tax_number;

    
    public RegisterData() {
    }

    public RegisterData(String username, String email, String name, String phone_number, String password, String confirmation, 
    String role, String state, String private_profile, String ocupation, String workplace, String address, String zip_code, String tax_number) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.phone_number = phone_number;
        this.password = password;
        this.confirmation = confirmation;
        this.role = role;
        this.state = state;
        this.private_profile = private_profile;
        this.ocupation = ocupation;
        this.workplace = workplace;
        this.address = address;
        this.zip_code = zip_code;
        this.tax_number = tax_number;
    }

    public boolean validRegistration() {
        return username != null && email != null && name != null && phone_number != null && password != null && confirmation != null;
    }

    public boolean validConfirmation() {
        return password.equals(confirmation);
    }
}
