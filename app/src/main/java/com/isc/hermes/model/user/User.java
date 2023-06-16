package com.isc.hermes.model.user;

public class User {

    private final Payload payload;
    private UserRoles role;

    public User(Payload payload){
        this.payload = payload;
    }

    public String getFullName(){
        return String.format("%s %s", payload.getGivenName(), payload.getFamilyName());
    }

    public String getUserName(){
        return payload.getGivenName();
    }

    public String getEmail(){
        return payload.getEmailAddress();
    }

    public String getPathImageUser(){
        return payload.getPhotoURL();
    }

    public void setRole(UserRoles role){
        this.role = role;
    }

    public UserRoles getRole() {
        return role;
    }
}
