package com.isc.hermes.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String fullName;
    private String email;
    private String userName;
    private String pathImageUser;
    private String typeUser;
    private String id;

    public User(String email, String pathImageUser, String id) {
        this.email = email;
        this.pathImageUser = pathImageUser;
        this.id = id;
    }

    protected User(Parcel in) {
        fullName = in.readString();
        email = in.readString();
        userName = in.readString();
        pathImageUser = in.readString();
        typeUser = in.readString();
        id = in.readString();
    }

    private String getFirstWord(String words) {
        String[] wordsArray = words.split(" ");
        String expectedWord = "";

        if (wordsArray.length > 0) expectedWord = wordsArray[0];
        return expectedWord;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeString(userName);
        dest.writeString(pathImageUser);
        dest.writeString(typeUser);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public String getFullName() {
        return fullName;
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

    public String getId() {
        return id;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public void setUserName(String userName) {
        this.userName = getFirstWord(userName);
    }

    public void setFullName(String name, String lastName){
        this.fullName = getFirstWord(name) + " " + getFirstWord(lastName);
        System.out.println(fullName);
    }
}