package pt.unl.fct.di.apdc.firstwebapp.util;

public enum States {
    INACTIVE(0),
    ACTIVE(1);

    int state;

    States(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
