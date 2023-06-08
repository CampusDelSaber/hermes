package com.isc.hermes.database;

import com.mongodb.client.MongoDatabase;

/**
 * This class is responsible for generating the connection to the hermes database.
 */
public class Hermesdb {

    private Connection connection;
    private MongoDatabase database;
    private static Hermesdb hermesdb;
    private final String DATABASE_NAME = "hermesmapdb";

    /**
     * It is a singleton method to have a single instance of the hermes database connection.
     *
     * @return only connection created.
     */
    public static Hermesdb getInstance() {
        if (hermesdb == null)
            hermesdb = new Hermesdb();

        return hermesdb;
    }

    /**
     * This is a constructor method to initialize their variables.
     */
    private Hermesdb() {
        this.connection = Connection.getConnection();
    }

    /**
     * This is a method returns the hermes database obtained.
     *
     * @return only hermes database connection.
     */
    public MongoDatabase getHermesDatabase() {
        if (database == null)
            database = connection.getDatabase().getDatabase(DATABASE_NAME);

        return database;
    }

    /**
     * This method gets the database name
     * @return the database name
     */
    public String getDATABASE_NAME() {
        return DATABASE_NAME;
    }
}
