package com.isc.hermes.database.collection;

import com.mongodb.client.MongoCollection;

import org.bson.Document;

/**
 * This class that represents the collection of incidents from the hermes database.
 */
public class Incidents implements Collectible {

    private static Collectible collection;
    private final String COLLECTION_NAME = "incidents";

    /**
     * It is a singleton method to have a single instance of the incident collection.
     *
     * @return incident collection.
     */
    public static Collectible getInstance() {
        if (collection == null)
            collection = new Incidents();

        return collection;
    }

    /**
     * This is a constructor method to initialize their variables.
     */
    private Incidents() {
    }

    /**
     * This method gets the connection to the incident connection using the hermes database.
     */
    private void initCollection() {
    }
}
