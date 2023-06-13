package com.isc.hermes.database;


/**
 * This class is responsible for creating the connection to the database.
 */
public class Connection {

    private static Connection connection;

    /**
     * It is a singleton method to have a single instance of the connection.
     *
     * @return only connection created.
     */
    public static Connection getConnection() {
        if (connection == null) {
            connection = new Connection();
        }
        return connection;
    }

}