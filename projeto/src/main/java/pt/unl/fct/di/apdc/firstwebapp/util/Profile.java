package pt.unl.fct.di.apdc.firstwebapp.util;

public enum Profile {
    PRIVATE(0),
    PUBLIC(1);
    
    int visibility;
    
    Profile(int visibility) {
        this.visibility = visibility;
    }
    
    public int getVisibility() {
        return visibility;
    }
}
