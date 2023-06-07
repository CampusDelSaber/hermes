package com.isc.hermes.database.collection;

import com.isc.hermes.database.Hermesdb;
import com.mongodb.client.MongoCollection;

import org.bson.Document;

public class Incidents implements Collectible {

    private Hermesdb hermesdb;
    private final String COLLECTION_NAME;
    private MongoCollection<Document> incidents;
    private static Collectible collection;


    public static Collectible getInstance() {
        if (collection == null)
            collection = new Incidents();

        return collection;
    }

    private Incidents() {
        this.COLLECTION_NAME = "incidents";
        this.hermesdb = Hermesdb.getInstance();
    }

    public MongoCollection<Document> getCollection() {
        if (incidents == null)
            iniCollection();

        return incidents;
    }

    private void iniCollection() {
        incidents = hermesdb.getHermesDatabase().getCollection(COLLECTION_NAME);
    }
}
