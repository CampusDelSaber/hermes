package com.isc.hermes.database;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.isc.hermes.database.collection.Incidents;

public class CollectionConnection {

    @Test
    public void getIncidentsCollection() {
        assertNotNull(Incidents.getInstance().getCollection());
    }

}
