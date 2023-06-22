package com.isc.hermes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.isc.hermes.model.Utils.Utils;

/**
 * The `User` class represents a user in the application. It stores information about the user, such as their full name,
 * email, username, profile image path, user type, and ID.
 */
public class User implements Parcelable {
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
     * Constructs a User object from a Parcel.
     *
     * @param in The Parcel from which to read the User object.
     */
    protected User(Parcel in) {
        fullName = in.readString();
        email = in.readString();
        userName = in.readString();
        pathImageUser = in.readString();
        typeUser = in.readString();
        id = in.readString();
    }

    /**
     * Constructs a new User object with the specified email, full name, username, and type of user.
     *
     * @param email     The email address of the user.
     * @param fullName  The full name of the user.
     * @param userName  The username of the user.
     * @param typeUser  The type of user.
     */
    public User(String email, String fullName, String userName, String typeUser) {
        this.fullName = fullName;
        this.email = email;
        this.userName = userName;
        this.typeUser = typeUser;
    }

    /**
     * Constructs a User object with the specified email, full name, username, user type, and ID.
     *
     * @param email     the email address of the user
     * @param fullName  the full name of the user
     * @param userName  the username of the user
     * @param typeUser  the type of user
     * @param id        the ID of the user
     */
    public User(String email, String fullName, String userName, String typeUser, String id) {
        this.fullName = fullName;
        this.email = email;
        this.userName = userName;
        this.typeUser = typeUser;
        this.id = id;
    }


    /**
     * Writes the User object to a Parcel.
     *
     * @param dest  The Parcel in which to write the User object.
     * @param flags Additional flags about how the object should be written.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeString(userName);
        dest.writeString(pathImageUser);
        dest.writeString(typeUser);
        dest.writeString(id);
    }

    /**
     * Describes the contents of the User object.
     *
     * @return A bitmask indicating the set of special object types marshaled by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Creator for the User class, which creates a User object from a Parcel.
     */
    public static final Creator<User> CREATOR = new Creator<>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
}
