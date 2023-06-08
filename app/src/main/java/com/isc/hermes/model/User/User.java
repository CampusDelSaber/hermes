package com.isc.hermes.model.User;

public class User {
    private String name;
    private String lastName;
    private String email;
    private String userName;
    private String pathImageUser;
    private long id;
    private TypeUser typeUser;

    public User(String name, String lastName, String email, String userName, String pathImageUser,
                long id) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.pathImageUser = pathImageUser;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPathImageUser() {
        return pathImageUser;
    }

    public long getId() {
        return id;
    }

    public TypeUser getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(TypeUser typeUser) {
        this.typeUser = typeUser;
    }
}
