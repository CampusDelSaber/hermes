package com.isc.hermes.generators;

import static org.junit.Assert.assertTrue;

import com.mapbox.geojson.constants.GeoJsonConstants;

import org.junit.Before;
import org.junit.Test;

public class CoordinateGenTest {

    private CoordinateGen coordinateGen;

    @Before
    public void initGenerator() {
        coordinateGen = new CoordinateGen();
    }

    @Test
    public void pointGenerated() {
        double[] point = coordinateGen.genPoint();

        assertTrue(point[1] >= GeoJsonConstants.MIN_LATITUDE &&
                point[1] <= GeoJsonConstants.MAX_LATITUDE);

        assertTrue(point[0] >= GeoJsonConstants.MIN_LONGITUDE &&
                point[0] <= GeoJsonConstants.MAX_LONGITUDE);
    }
}
