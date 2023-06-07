package com.isc.hermes.database;

import com.mongodb.client.MongoDatabase;

public class Hermesdb {

    private Connection connection;
    private MongoDatabase database;
    private static Hermesdb hermesdb;

    public static Hermesdb getInstance() {
        if (hermesdb == null)
            hermesdb = new Hermesdb();

        return hermesdb;
    }

    private Hermesdb() {
        this.connection = Connection.getConnection();
    }

    public MongoDatabase getHermesDatabase() {
        if (database == null)
            database = connection.getDatabase().getDatabase("hermesmapdb");

        return database;
    }
}
