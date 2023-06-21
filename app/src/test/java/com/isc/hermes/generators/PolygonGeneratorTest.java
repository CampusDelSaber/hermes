package com.isc.hermes.generators;

import static org.junit.Assert.assertNotNull;

import com.isc.hermes.model.Radium;

import org.junit.Before;
import org.junit.Test;

public class PolygonGeneratorTest {

    private PolygonGenerator generator;
    private Double[] referencePoint;

    @Before
    public void initGenerator() {
        generator = new PolygonGenerator();
        referencePoint = new Double[]{-66.17558339723521, -17.366337924269107};
    }

    @Test
    public void generatePolygon() {
        assertNotNull(generator.generate(referencePoint, Radium.ONE_KILOMETER, 0));
    }
}
