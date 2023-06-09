package com.isc.hermes.database.user;

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
            jsonTree.insertOne(userData.export());
        }catch(MongoException e){
            e.printStackTrace();
        }
    }

    public UserData get(int userID){
        MongoCursor<Document> jsonTree = this.jsonTree.find().iterator();
        while (jsonTree.hasNext()){
            Document node = jsonTree.next();
            if (!node.containsKey(UserData.UNIQUE_ID)){
                continue;
            }

            if (node.getInteger(UserData.UNIQUE_ID) == userID){
                return UserData.transform(node);
            }
        }
        return null;
    }
}
