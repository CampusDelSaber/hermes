package com.isc.hermes.database.collection;

import com.mongodb.client.MongoCollection;

import org.bson.Document;

public interface Collectible {

    public MongoCollection<Document> getCollection();

}
