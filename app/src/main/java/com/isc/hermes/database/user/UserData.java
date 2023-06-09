package com.isc.hermes.database.user;

import com.isc.hermes.model.User;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * The UserData class stores the user's data.
 */
public class UserData {

    private String userName;
    private String fullName;
    private String email;
    private String profileImagePath;
    private String userID;
    private String userType;
    public static String UNIQUE_ID = "userID";

    public UserData(String userName, String email, String userID, String userType, String profileImagePath, String fullName) {
        this.userName = userName;
        this.email = email;
        this.userID = userID;
        this.userType = userType;
        this.profileImagePath = profileImagePath;
        this.fullName = fullName;
    }

    /**
     * Exports this class as a Document class.
     *
     * @return A document class populated with the UserData class fields.
     */
    public Document exportToDocument(){
        return new Document()
            .append("_id", new ObjectId())
            .append("username", userName)
            .append("fullName", fullName)
            .append("email", email)
            .append("userID", userID)
            .append("isPrivileged", userType)
            .append("profileImage", profileImagePath);
    }

    /**
     * Transforms a Document class into a UserData class.
     *
     * @param document the class to be transformed.
     * @return outputs an UserData class
     */
    public static UserData transform(Document document){
        return new UserData(
                document.getString("username"),
                document.getString("fullName"),
                document.getString("email"),
                document.getString("userID"),
                document.getString("userID"),
                document.getString("profileImage")
        );
    }

    /**
     * Transforms a User class into a UserData class.
     *
     * @param user the class to be transformed.
     * @return outputs an UserData class
     */
    public static UserData transform(User user){
        return new UserData(
                user.getUserName(),
                user.getFullName(),
                user.getEmail(),
                user.getId(),
                user.getTypeUser(),
                user.getPathImageUser()
        );
    }

    /**
     * Returns the userID class field.
     *
     * @return userID
     */
    public String getUserID() {
        return userID;
    }
}
