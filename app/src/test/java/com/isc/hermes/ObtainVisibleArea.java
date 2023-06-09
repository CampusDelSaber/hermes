package com.isc.hermes;

import static org.junit.Assert.assertNotNull;

import com.isc.hermes.model.CoordinatesGenerator;
import com.isc.hermes.model.IncidentsGenerator;

import org.junit.Test;

public class ObtainVisibleArea {
    @Test
    public void coordinates() {
        assertNotNull(CoordinatesGenerator.getVariousCoordinates(-17.387072, -66.089125, 5));
    }

    @Test
    public void polygon() {
        assertNotNull(IncidentsGenerator.getRandomPolygon(-17.387072, -66.089125, 5));
    }
}
