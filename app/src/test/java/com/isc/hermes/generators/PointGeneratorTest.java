package com.isc.hermes.generators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.isc.hermes.model.Radium;
import com.isc.hermes.requests.geocoders.StreetValidator;

import org.junit.Before;
import org.junit.Test;

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
        Double[] referencePoint = {-17.366472, -66.175682};
        pointCoordinate = generator.getStreetPoint(referencePoint, Radium.FIVE_HUNDRED_METERS);
        assertNotNull(pointCoordinate);

        Double longitude = pointCoordinate[1];
        Double latitude = pointCoordinate[0];
        assertTrue(streetValidator.isPointStreet(longitude, latitude));
    }


    @Test
    public void validMultiPointGenerated() {
        int amountPoints = 15;
        List<Double[]> multiPoints = generator.getMultiPoint(
                referencePoint, Radium.FIFTY_METERS, amountPoints);

        assertNotNull(multiPoints);
        assertEquals(amountPoints, multiPoints.size());

    }

}
