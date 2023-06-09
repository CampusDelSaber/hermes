package com.isc.hermes.database.user;

import com.isc.hermes.database.collection.Collectible;
import org.bson.Document;
import com.isc.hermes.database.Hermesdb;
import com.mongodb.client.MongoCollection;

/**
 * User
 */
public class UsersCollection implements Collectible{
    private Hermesdb hermesdb;
    private static Collectible collection;
    public static final String COLLECTION_NAME = "users";
    private MongoCollection<Document> users;


    public static Collectible getInstance() {
        if (collection == null)
            collection = new UsersCollection();

        return collection;
    }

    private UsersCollection(){
        hermesdb = Hermesdb.getInstance();
    }

    /**
     * This method gets the connection to the users collection using the hermes database.
     */
    private void initCollection() {
        users = hermesdb.getHermesDatabase().getCollection(COLLECTION_NAME);
    }

    @Override
    public MongoCollection<Document> getCollection(){
        if (users == null)
            initCollection();

        return users;
    }
}
