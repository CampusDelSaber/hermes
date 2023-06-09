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
    private String userType;
    public static String UNIQUE_ID = "userID";

    public UserData(String username, String email, int userID, String userType, String profileImage) {
        this.username = username;
        this.email = email;
        this.userID = userID;
        this.userType = userType;
        this.profileImage = profileImage;
    }

    public Document export(){
        return new Document()
            .append("_id", new ObjectId())
            .append("username", username)
            .append("email", email)
            .append("userID", userID)
            .append("userType", userType)
            .append("profileImage", profileImage);
    }

    public static UserData transform(Document document){
        return new UserData(
                document.getString("username"),
                document.getString("email"),
                document.getInteger("userID"),
                document.getString("userType"),
                document.getString("profileImage")
        );
    }
}
