package com.isc.hermes.database;


import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DatabaseConnection {

    @Test
    public void initSuccessConnection() {
        Connection.getConnection().initDatabaseConnection();
        assertNotNull(Connection.getConnection().getDatabase());
    }

    @Test
    public void getSuccessfulConnection() {
        assertNotNull(Connection.getConnection().getDatabase());
    }

    @Test
    public void getSuccessHermesConnection() {
        assertNotNull(Hermesdb.getInstance().getHermesDatabase());
    }
}
