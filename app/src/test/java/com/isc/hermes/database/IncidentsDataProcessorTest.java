package com.isc.hermes.database;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IncidentsDataProcessorTest {
    private IncidentsDataProcessor incidentsDataProcessor;

    @Before
    public void setUp() {
        incidentsDataProcessor = IncidentsDataProcessor.getInstance();
    }

    @Test
    public void testGetAllIncidents() throws ExecutionException, InterruptedException {
        JSONArray incidentsArray = incidentsDataProcessor.getAllIncidents();
        assertNotNull(incidentsArray);
    }

    @Test
    public void testGetInstance() {
        IncidentsDataProcessor instance1 = IncidentsDataProcessor.getInstance();
        IncidentsDataProcessor instance2 = IncidentsDataProcessor.getInstance();

        assertEquals(instance1, instance2);
    }
}
