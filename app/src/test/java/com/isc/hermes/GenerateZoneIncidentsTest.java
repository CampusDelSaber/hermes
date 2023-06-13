package com.isc.hermes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.isc.hermes.utils.CoordinatesGenerator;
import com.isc.hermes.utils.IncidentsGenerator;

import org.junit.Test;

public class GenerateZoneIncidentsTest {
    @Test
    public void coordinates() {
        assertNotNull(CoordinatesGenerator.getVariousFarAwayCoordinates(-17.387072, -66.089125, 5));
    }

    @Test
    public void polygon() {
        String a = "A";
        assertEquals("com.mapbox.geojson.Polygon", IncidentsGenerator.getRandomPolygon(-17.387072, -66.089125, 3).getClass().getName());
        assertNotNull(IncidentsGenerator.getRandomPolygon(-17.387072, -66.089125, 5));
    }
}
