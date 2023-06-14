package com.isc.hermes.model.user;


public enum UserRoles {
    ADMINISTRATOR("Administrator"),
    GENERAL("General");

    private String role;

    UserRoles(String role) {
        this.role = role;
    }

    public String getRole(){
        return role;
    }

    public static String[] export(){
        UserRoles[] userRoles = UserRoles.values();
        String[] rolesAsString = new String[userRoles.length];

        for (int index = 0; index < userRoles.length; index++) {
            rolesAsString[index] = userRoles[index].getRole();
        }
        return rolesAsString;
    }

    public static UserRoles transform(String sRole){
        UserRoles[] roles = UserRoles.values();

        for (UserRoles role : roles) {
            if (role.getRole().equalsIgnoreCase(sRole)){
                return role;
            }
        }

        // TODO: Raise an error dialog
        return null;
    }
}
