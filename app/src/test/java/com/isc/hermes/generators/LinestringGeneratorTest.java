package com.isc.hermes.generators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.isc.hermes.model.Radium;
import com.isc.hermes.requests.geocoders.StreetValidator;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class LinestringGeneratorTest {

    private LinestringGenerator generator;
    private Double[] referencePoint;
    private List<Double[]> linestring;
    private StreetValidator streetValidator;


    @Before
    public void initGeneratorTest() {
        generator = new LinestringGenerator();
        streetValidator = new StreetValidator();
        referencePoint = new Double[]{-66.17558339723521, -17.366337924269107};
    }

    @Before
    public void linestringSize() {
        int amountPoints = 5;
        linestring = generator.generate(referencePoint, Radium.FIFTY_METERS, amountPoints);

        assertEquals(amountPoints, linestring.size());
    }

    @Test
    public void streetLinestring() {
        for (Double[] point : linestring) {
            assertTrue(streetValidator.isPointStreet(point[0], point[1]));
        }
    }
}
