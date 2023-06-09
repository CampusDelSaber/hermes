package com.isc.hermes.database.user;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Data
 */
public class UserData {

    private String username;
    private String email;
    private String profileImage;
    private int userID;
    private boolean isPrivileged;
    public static String UNIQUE_ID = "userID";

    public UserData(String username, String email, int userID, boolean isPrivileged, String profileImage) {
        this.username = username;
        this.email = email;
        this.userID = userID;
        this.isPrivileged = isPrivileged;
        this.profileImage = profileImage;
    }

    public Document export(){
        return new Document()
            .append("_id", new ObjectId())
            .append("username", username)
            .append("email", email)
            .append("userID", userID)
            .append("isPrivileged", isPrivileged)
            .append("profileImage", profileImage);
    }

    public static UserData transform(Document document){
        return new UserData(
                document.getString("username"),
                document.getString("email"),
                document.getInteger("userID"),
                document.getBoolean("isPrivileged"),
                document.getString("profileImage")
        );
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public int getUserID() {
        return userID;
    }

    public boolean getPrivileged() {
        return isPrivileged;
    }
}
