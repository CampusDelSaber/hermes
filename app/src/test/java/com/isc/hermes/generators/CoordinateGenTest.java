package com.isc.hermes.generators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.mapbox.geojson.constants.GeoJsonConstants;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CoordinateGenTest {

    private CoordinateGen coordinateGen;

    @Before
    public void initGenerator() {
        coordinateGen = new CoordinateGen();
    }

    @Test
    public void validPointGenerated() {
        double[] point = coordinateGen.generatePoint();

        assertTrue(point[1] >= GeoJsonConstants.MIN_LATITUDE &&
                point[1] <= GeoJsonConstants.MAX_LATITUDE);

        assertTrue(point[0] >= GeoJsonConstants.MIN_LONGITUDE &&
                point[0] <= GeoJsonConstants.MAX_LONGITUDE);
    }

    @Test
    public void validLineStringGenerated() {
        int amountPoints = 5;
        List<double[]> lineString = coordinateGen.generateLineString(amountPoints);

        assertEquals(amountPoints, lineString.size());

        for (double[] point : lineString) {
            assertTrue(point[1] >= GeoJsonConstants.MIN_LATITUDE && point[1] <= GeoJsonConstants.MAX_LATITUDE);
            assertTrue(point[0] >= GeoJsonConstants.MIN_LONGITUDE && point[0] <= GeoJsonConstants.MAX_LONGITUDE);

        }
    }
}
