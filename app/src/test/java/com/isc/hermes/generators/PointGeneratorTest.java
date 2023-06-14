package com.isc.hermes.generators;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.isc.hermes.requests.geocoders.StreetValidator;

import org.junit.Before;
import org.junit.Test;


public class PointGeneratorTest {

    private PointGenerator generator;
    private StreetValidator streetValidator;
    private Double[] pointCoordinate;

    @Before
    public void initGenerator() {
        generator = new PointGenerator();
        streetValidator = new StreetValidator();
    }

    @Before
    public void notNullPointGenerated() {
        pointCoordinate = generator.getStreetPoint();
        assertNotNull(pointCoordinate);
    }

    @Test
    public void streetCoordinate() {
        Double longitude = pointCoordinate[0];
        Double latitude = pointCoordinate[1];
        assertTrue(streetValidator.isPointStreet(longitude, latitude));
    }

}
