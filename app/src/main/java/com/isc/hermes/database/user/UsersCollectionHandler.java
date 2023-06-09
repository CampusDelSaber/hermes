package com.isc.hermes.database.user;

import com.isc.hermes.model.User;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import org.bson.Document;

/**
 * Handler
 */
public class UsersCollectionHandler {

    private final MongoCollection<Document> jsonTree;

    public UsersCollectionHandler(){
        jsonTree = UsersCollection.getInstance().getCollection();
    }

    public void save(UserData userData){
        try{
            jsonTree.insertOne(userData.exportToDocument());
        }catch(MongoException e){
            e.printStackTrace();
        }
    }

    public void save(User user){
        try{
            jsonTree.insertOne(UserData.transform(user).exportToDocument());
        }catch(MongoException e){
            e.printStackTrace();
        }
    }

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
