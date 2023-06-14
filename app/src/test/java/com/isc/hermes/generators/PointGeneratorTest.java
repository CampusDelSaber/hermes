package com.isc.hermes.generators;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.isc.hermes.requests.geocoders.StreetValidator;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


public class PointGeneratorTest {

    private PointGenerator generator;
    private StreetValidator streetValidator;
    private Double[] pointCoordinate, referencePoint;

    @Before
    public void initGenerator() {
        generator = new PointGenerator();
        streetValidator = new StreetValidator();
        referencePoint = new Double[]{-66.17558339723521, -17.366337924269107};
    }

    @Test
    public void validPointGenerated() {
        pointCoordinate = generator.getStreetPoint();
        assertNotNull(pointCoordinate);

        Double longitude = pointCoordinate[0];
        Double latitude = pointCoordinate[1];
        assertTrue(streetValidator.isPointStreet(longitude, latitude));
    }


    @Test
    public void validMultiPointGenerated() {
        int amountPoints = 15;
        List<Double[]> multiPoints = generator.getPointCoordinates(
                referencePoint, Radium.FIFTY_METERS, amountPoints);

        assertNotNull(multiPoints);
        assertEquals(amountPoints, multiPoints.size());

    }

}
