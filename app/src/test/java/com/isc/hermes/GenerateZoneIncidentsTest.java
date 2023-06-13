package com.isc.hermes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.isc.hermes.model.CoordinatesGenerator;
import com.isc.hermes.model.IncidentsGenerator;
import com.mapbox.geojson.Polygon;

import org.junit.Test;

public class GenerateZoneIncidentsTest {
    @Test
    public void coordinates() {
        assertNotNull(CoordinatesGenerator.getVariousCoordinates(-17.387072, -66.089125, 5));
    }

    @Test
    public void polygon() {
        String a = "A";
        assertEquals("com.mapbox.geojson.Polygon", IncidentsGenerator.getRandomPolygon(-17.387072, -66.089125, 3).getClass().getName());
        assertNotNull(IncidentsGenerator.getRandomPolygon(-17.387072, -66.089125, 5));
    }
}
