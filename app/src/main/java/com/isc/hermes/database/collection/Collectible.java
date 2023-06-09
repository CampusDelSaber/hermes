package com.isc.hermes.database.collection;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Interface to implement the classes that represent a collection of the hermes database.
 */
public interface Collectible {

    /**
     * Method to obtain the connection with the collection to perform operations with it.
     *
     * @return collection connection.
     */
    public MongoCollection<Document> getCollection();

}
