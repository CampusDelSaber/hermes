package com.isc.hermes.model.User;
/**
 * Enum representing the type of user.
 */
public enum TypeUser {

    /**
     * Administrator user type.
     */
    ADMINISTRATOR("Administrator"),

    /**
     * General user type.
     */
    GENERAL("General");

    private final String typeUser;

    /**
     * Constructs a new TypeUser enum with the specified user type.
     *
     * @param typeUser The user type.
     */
    private TypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    /**
     * Gets the user type.
     *
     * @return The user type.
     */
    public String getTypeUser() {
        return typeUser;
    }

    /**
     * Gets an array of all user types as strings.
     *
     * @return An array of user types.
     */
    public static String[] getArrayTypeUsers() {
        String[] attributes = new String[TypeUser.values().length];
        int index = 0;
        for (TypeUser element : TypeUser.values()) {
            attributes[index] = element.getTypeUser();
            index++;
        } return attributes;
    }
}
