package com.isc.hermes.database;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.isc.hermes.database.collection.Incidents;

import org.junit.jupiter.api.Test;

public class CollectionConnection {

    @Test
    public void getIncidentsCollection() {
        assertNotNull(Incidents.getInstance().getCollection());
    }

}
