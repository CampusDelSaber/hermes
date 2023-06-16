package com.isc.hermes.model.user;

/**
 * The User class holds a user data.
 */
public class User {

    private final Payload payload;
    private UserRoles role;

    public User(Payload payload){
        this.payload = payload;
    }

    /**
     * Retrieves the givenName and the familyName to merge it as the fullName.
     *
     * @return The fullName of the user.
     */
    public String getFullName(){
        return String.format("%s %s", payload.getGivenName(), payload.getFamilyName());
    }

    /**
     * Retrieves the givenName.
     *
     * @return the givenName as userName
     */
    public String getUserName(){
        return payload.getGivenName();
    }

    /**
     * Retrieves the emailAddress.
     *
     * @return the email address of the user
     */
    public String getEmail(){
        return payload.getEmailAddress();
    }

    /**
     * Retrieves the URL path of the user as string.
     *
     * @return the URL path to the image of the user
     */
    public String getPathImageUser(){
        return payload.getPhotoURL();
    }

    /**
     * Sets the user's role.
     */
    public void setRole(UserRoles role){
        this.role = role;
    }

    /**
     * Retrieves the user's role.
     *
     * @return the role of the user
     */
    public UserRoles getRole() {
        return role;
    }
}
