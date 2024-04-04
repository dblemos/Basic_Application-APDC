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

        public static boolean canGrantRole(String userRole, String targetRole, String grantedRole) {
            if(userRole.equals(GA.toString())) {
                if(Roles.valueOf(targetRole).authority <= Roles.valueOf(GBO.toString()).authority)
                    return Roles.valueOf(grantedRole).authority <= GBO.authority;
                else
                    return false;
            }
            else
                return true;
        }

        public static boolean canGrantState(String userRole, String targetRole) {
            if(userRole.equals(SU.toString()))
                return true;
            else if(userRole.equals(USER.toString()))
                return false;
            else
                return Roles.valueOf(userRole).authority > Roles.valueOf(targetRole).authority;
        }

        public static boolean canRemoveUser(String userRole, String targetRole) {
            if(userRole.equals(SU.toString()))
                return true;
            else if(userRole.equals(GBO.toString() ))
                return false;
            else if (userRole.equals(USER.toString()))
                return true;
            else
                return Roles.valueOf(userRole).authority > Roles.valueOf(targetRole).authority;
        }
}
