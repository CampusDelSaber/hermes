package com.isc.hermes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.isc.hermes.utils.CoordinatesGenerator;
import com.isc.hermes.utils.IncidentsGenerator;
import com.mapbox.geojson.Polygon;

import org.junit.Test;

public class GenerateZoneIncidentsTest {
    @Test
    public void coordinates() {
        assertNotNull(CoordinatesGenerator.getVariousFarAwayCoordinates(-17.387072, -66.089125, 5));
    }

    @Test
    public void polygon() {
        assertEquals("com.mapbox.geojson.Polygon", IncidentsGenerator.getRandomPolygon(-17.387072, -66.089125, 3).getClass().getName());
        assertNotNull(IncidentsGenerator.getRandomPolygon(-17.387072, -66.089125, 5));
    }

    @Test
    public void polygons() {
        Polygon[] polygons = IncidentsGenerator.generateIncidentsZones(-17.387072, -66.089125, 3);
        assertNotNull(polygons);
        assertEquals("com.mapbox.geojson.Polygon", polygons[0].getClass().getName());
    }
}
