package com.isc.hermes.model.User;

import com.isc.hermes.model.Utils.Utils;

/**
 * The `User` class represents a user in the application. It stores information about the user, such as their full name,
 * email, username, profile image path, user type, and ID.
 */
public class User {
    private String fullName;
    private String email;
    private String userName;
    private String pathImageUser;
    private String typeUser;
    private String id;

    /**
     * Constructs a User object with the specified email, path to the user's image, and ID.
     *
     * @param email The email of the user.
     * @param pathImageUser The path to the user's image.
     * @param id The ID of the user.
     */
    public User(String email, String pathImageUser, String id) {
        this.email = email;
        this.pathImageUser = pathImageUser;
        this.id = id;
    }

    /**
     * Constructs a new User object with the specified email, full name, username, and type of user.
     *
     * @param email     The email address of the user.
     * @param fullName  The full name of the user.
     * @param userName  The username of the user.
     * @param typeUser  The type of user.
     * @param pathImageUser the image of the user
     */
    public User(String email, String fullName, String userName, String typeUser, String pathImageUser) {
        this.fullName = fullName;
        this.email = email;
        this.userName = userName;
        this.typeUser = typeUser;
        this.pathImageUser = pathImageUser;
    }

    /**
     * Constructs a User object with the specified email, full name, username, user type, and ID.
     *
     * @param email     the email address of the user
     * @param fullName  the full name of the user
     * @param userName  the username of the user
     * @param typeUser  the type of user
     * @param id        the ID of the user
     * @param pathImageUser the image of the user
     */
    public User(String email, String fullName, String userName, String typeUser, String id, String pathImageUser) {
        this.fullName = fullName;
        this.email = email;
        this.userName = userName;
        this.typeUser = typeUser;
        this.id = id;
        this.pathImageUser = pathImageUser;
    }

    /**
     * Returns the full name of the user.
     *
     * @return The full name of the user.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns the email of the user.
     *
     * @return The email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the username of the user.
     *
     * @return The username of the user.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns the path to the user's image.
     *
     * @return The path to the user's image.
     */
    public String getPathImageUser() {
        return pathImageUser;
    }

    /**
     * Returns the ID of the user.
     *
     * @return The ID of the user.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the type of the user.
     *
     * @return The type of the user.
     */
    public String getTypeUser() {
        return typeUser;
    }

    /**
     * Sets the typeUser of the user.
     *
     * @param typeUser The typeUser of the user.
     */
    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }


    /**
     * Sets the username of the user.
     *
     * @param userName The username of the user.
     */
    public void setUserName(String userName) {
        this.userName = Utils.getFirstWord(userName);
    }

    /**
     * Sets the full name of the user.
     *
     * @param name The first name of the user.
     * @param lastName The last name of the user.
     */
    public void setFullName(String name, String lastName){
        StringBuilder str = new StringBuilder();
        str.append(Utils.getFirstWord(name));
        if (lastName != null) str.append(" ").append(Utils.getFirstWord(lastName));
        this.fullName = str.toString();
    }

    /**
     * Sets the full name of the user.
     *
     * @param fullName The full name of the user.
     */
    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    /**
     * Sets the email for the user.
     * @param email the new email for the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the image path for the user.
     *
     * @param pathImageUser the new image path for the user.
     */
    public void setPathImageUser(String pathImageUser) {
        this.pathImageUser = pathImageUser;
    }

    /**
     * This method is used to set the is of user.
     *
     * @param id The id will be use to replace.
     */
    public void setId(String id) {
        this.id = id;
    }
}
