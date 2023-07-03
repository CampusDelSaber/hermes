package com.isc.hermes.generators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.isc.hermes.model.Radium;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class IncidentGeneratorTest {


    private IncidentGenerator incidentGenerator;
    private Double[] coordinateReference;

    @Before
    public void initSetup() {
        incidentGenerator = new IncidentGenerator();
        coordinateReference = new Double[]{-66.17558339723521, -17.366337924269107};
    }

    @Test
    public void timeoutTest() {
        assertTrue(incidentGenerator.withoutTimeout());
        incidentGenerator.getIncidentsRandomly(coordinateReference, Radium.FIFTY_METERS, 2);
        assertFalse(incidentGenerator.withoutTimeout());
    }

    @Test
    public void timeToWaitTest() {
        incidentGenerator.getIncidentsRandomly(coordinateReference, Radium.FIFTY_METERS, 2);
        assertTrue(incidentGenerator.getMinutesBetweenTimeout() > 0);
        assertTrue(incidentGenerator.getSecondsBetweenTimeout() > 0);
    }

    @Test
    public void multiPointTest() {
        PointGenerator pointGenerator = new PointGenerator();
        pointGenerator.getMultiPoint(coordinateReference, Radium.ONE_KILOMETER, 3).forEach(incident ->
                System.out.println(Arrays.toString(incident)));
    }

    @Test
    public void polygonGeneratorTest() {
        PolygonGenerator polygonGenerator = new PolygonGenerator();
        List<Double[]> polygon1 = polygonGenerator.generate(new Double[]{-66.17597619693515, -17.366472604202826}, Radium.ONE_HUNDRED_METERS, 5);
        List<Double[]> polygon2 = polygonGenerator.generate(new Double[]{-66.17500318590098, -17.36683304740737}, Radium.FIFTY_METERS, 7);
        List<Double[]> polygon3 = polygonGenerator.generate(new Double[]{-66.17549150939814, -17.36621852918228}, Radium.ONE_HUNDRED_METERS, 10);

        assertNotEquals(polygon1.get(0)[0], polygon2.get(0)[0], 0.0);
        assertNotEquals(polygon2.get(0)[0], polygon3.get(0)[0], 0.0);
        assertNotEquals(polygon1.get(0)[0], polygon3.get(0)[0], 0.0);
    }
}
