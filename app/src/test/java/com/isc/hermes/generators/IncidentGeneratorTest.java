package com.isc.hermes.generators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.isc.hermes.model.Radium;

import org.junit.Before;
import org.junit.Test;

public class IncidentGeneratorTest {


    private IncidentGenerator incidentGenerator;
    private Double[] coordinateReference;

    @Before
    public void initSetup() {
        incidentGenerator = new IncidentGenerator();
        coordinateReference = new Double[]{-66.17558339723521, -17.366337924269107};
    }

    @Test
    public void incidentGeneratedTest() {
        assertNotNull(incidentGenerator
                .getIncidentsRandomly(coordinateReference, Radium.FIFTY_METERS, 2));
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
}
