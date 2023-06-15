package com.isc.hermes.user;

public class Payload {
    private final String emailAddress;
    private final String id;
    private final String photoURL;
    private final String givenName;
    private final String familyName;

    public Payload(String emailAddress, String id, String photoURL, String givenName, String familyName) {
        this.emailAddress = emailAddress;
        this.id = id;
        this.photoURL = photoURL;
        this.givenName = givenName;
        this.familyName = familyName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getId() {
        return id;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }
}
