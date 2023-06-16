package com.isc.hermes.model.user;

import com.isc.hermes.model.exceptions.InvalidUserRoleException;

/**
 * The UserRoles enum, holds the available user roles.
 */
public enum UserRoles {
    ADMINISTRATOR("Administrator"),
    GENERAL("General");

    private final String role;

    UserRoles(String role) {
        this.role = role;
    }

    public String getRole(){
        return role;
    }

    /**
     * Exports the values from this enum into a String array.
     *
     * @return The values of this enum as a String array.
     */
    public static String[] export(){
        UserRoles[] userRoles = UserRoles.values();
        String[] rolesAsString = new String[userRoles.length];

        for (int index = 0; index < userRoles.length; index++) {
            rolesAsString[index] = userRoles[index].getRole();
        }
        return rolesAsString;
    }

    /**
     * This method takes a role as String to then validate if it can be taken as an
     * existent role if so it returns the found role.
     *
     * @param sRole The role to be verified.
     * @return The found role.
     */
    public static UserRoles transform(String sRole){
        UserRoles[] roles = UserRoles.values();

        for (UserRoles role : roles) {
            if (role.getRole().equalsIgnoreCase(sRole)){
                return role;
            }
        }

        throw  new InvalidUserRoleException(String.format("The provided role %s could not be found.", sRole));
    }
}
