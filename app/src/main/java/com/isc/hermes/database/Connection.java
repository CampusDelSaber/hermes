package com.isc.hermes.database;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;

/**
 * This class is responsible for creating the connection to the database.
 */
public class Connection {

    private static Connection connection;
    private static MongoClient mongodb;

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

    /**
     * This is a method returns the connection to the database.
     *
     * @return only database connection.
     */
    public MongoClient getDatabase() {
        if (mongodb == null)
            initDatabaseConnection();

        return mongodb;
    }

    /**
     * This method creates the connection to the database using settings and the mongodb uri.
     */
    public void initDatabaseConnection() {
        try {
            mongodb = MongoClients.create(getConnectionSettings(getConnectionURI()));
        } catch (MongoException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method returns the Uniform Resource Identifier of our database.
     *
     * @return connection URI
     */
    private ConnectionString getConnectionURI() {
        return new ConnectionString("mongodb+srv://hermesmapapp:jzFAZXVdzEyCfHwh@hermes-cluster.qqt9zti.mongodb.net/hermesmapdb?retryWrites=true&w=majority");
    }

    /**
     * This method returns the settings for the connection to the database.
     *
     * @param connectionURI it's the connection URI.
     * @return connection settings.
     */
    private MongoClientSettings getConnectionSettings(ConnectionString connectionURI) {
        return MongoClientSettings.builder()
                .applyConnectionString(connectionURI)
                .serverApi(ServerApi.builder().version(ServerApiVersion.V1).build())
                .build();
    }
}