package pt.unl.fct.di.apdc.firstwebapp.util;

public enum Roles {
        USER(0),
        GBO(1),
        GA(2),
        SU(3);

        int authority;

        Roles(int authority) {
            this.authority = authority;
        }

        public int getAuthority() {
            return authority;
        }        
}
