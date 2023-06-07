package com.isc.hermes.database;


import static org.junit.Assert.assertNotNull;

import com.isc.hermes.database.collection.Incidents;

import org.junit.Test;

public class CollectionConnection {

    @Test
    public void getIncidentsCollection() {
        assertNotNull(Incidents.getInstance().getCollection());
    }

}
