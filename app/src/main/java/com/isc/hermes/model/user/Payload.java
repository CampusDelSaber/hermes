package com.isc.hermes.model.user;

/**
 * The Payload Class holds a bunch of information about related to a
 * person.
 */
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

    /**
     * Retrieves the email address field.
     *
     * @return An email address.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Retrieves the photoURL field.
     *
     * @return A photoURL.
     */
    public String getPhotoURL() {
        return photoURL;
    }

    /**
     * Retrieves the givenName field.
     *
     * @return A givenName.
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Retrieves the familyName field.
     *
     * @return A familyName.
     */
    public String getFamilyName() {
        return familyName;
    }
}
