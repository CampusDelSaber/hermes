package com.isc.hermes.database;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;

public class Connection {

    private static Connection connection;
    private static MongoClient mongodb;
    private Dotenv dotenv;

    public static Connection getConnection() {
        if (connection == null) {
            connection = new Connection();
        }
        return connection;
    }

    private Connection() {
        this.dotenv = Dotenv.load();
    }

    public MongoClient getDatabase() {
        if (mongodb == null)
            initDatabaseConnection();

        return mongodb;
    }

    public void initDatabaseConnection() {
        try {
            mongodb = MongoClients.create(getConnectionSettings(getConnectionURI()));
            System.out.println("success connection");
        } catch (MongoException exception) {
            System.out.println("connection failed");
        }
    }

    private ConnectionString getConnectionURI() {
        return new ConnectionString(Objects.requireNonNull(dotenv.get("MONGODB_URI")));
    }

    private MongoClientSettings getConnectionSettings(ConnectionString connectionURI) {
        return MongoClientSettings.builder()
                .applyConnectionString(connectionURI)
                .serverApi(ServerApi.builder().version(ServerApiVersion.V1).build())
                .build();
    }
}