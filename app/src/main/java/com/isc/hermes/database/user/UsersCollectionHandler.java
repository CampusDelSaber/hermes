package com.isc.hermes.database.user;

import com.isc.hermes.model.User;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import org.bson.Document;

/**
 * Handles the CRUD operations with MongoDB
 */
public class UsersCollectionHandler {

    private final MongoCollection<Document> jsonTree;

    public UsersCollectionHandler(){
        jsonTree = UsersCollection.getInstance().getCollection();
    }

    /**
     * Saves an UserData object into the MongoDB
     *
     * @param userData the object to be saved
     */
    public void save(UserData userData){
        try{
            jsonTree.insertOne(userData.exportToDocument());
        }catch(MongoException e){
            e.printStackTrace();
        }
    }

    /**
     * Saves an User object into the MongoDB
     *
     * @param user the object to be saved
     */
    public void save(User user){
        try{
            jsonTree.insertOne(UserData.transform(user).exportToDocument());
        }catch(MongoException e){
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the information of a user, based on its ID
     *
     * @param userID the unique identifier of the user
     * @return an UserData object populated with the user's information.
     */
    public UserData get(String userID){
        MongoCursor<Document> jsonTree = this.jsonTree.find().iterator();
        while (jsonTree.hasNext()){
            Document node = jsonTree.next();
            if (!node.containsKey(UserData.UNIQUE_ID)){
                continue;
            }

            if (node.getString(UserData.UNIQUE_ID).equals(userID)){
                return UserData.transform(node);
            }
        }

        // TODO: when the user is not found, raise an error.
        return null;
    }
}
