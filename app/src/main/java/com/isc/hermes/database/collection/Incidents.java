package com.isc.hermes.database.collection;

import com.isc.hermes.database.Hermesdb;
import com.mongodb.client.MongoCollection;

import org.bson.Document;

/**
 * This class that represents the collection of incidents from the hermes database.
 */
public class Incidents implements Collectible {

    private Hermesdb hermesdb;
    private MongoCollection<Document> incidents;
    private static Collectible collection;
    private final String COLLECTION_NAME = "incidents";;

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
        this.hermesdb = Hermesdb.getInstance();
    }

    /**
     * This method returns the connection to the collection of incidents created.
     *
     * @return incidents collection.
     */
    public MongoCollection<Document> getCollection() {
        if (incidents == null)
            initCollection();

        return incidents;
    }

    /**
     * This method gets the connection to the incident connection using the hermes database.
     */
    private void initCollection() {
        incidents = hermesdb.getHermesDatabase().getCollection(COLLECTION_NAME);
    }
}
