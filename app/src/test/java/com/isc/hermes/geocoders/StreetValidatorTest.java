package com.isc.hermes.geocoders;

import  static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.isc.hermes.requests.geocoders.StreetValidator;

import org.junit.Before;
import org.junit.Test;

public class StreetValidatorTest {

    private StreetValidator streetValidator;

    @Before
    public void initReverseGeocoding() {
        streetValidator = new StreetValidator();
    }


    @Test
    public void streetCoordinate() {
        double[] streetCoordinates = new double[]{-66.15502527828558, -17.395038120001512};
        assertTrue(streetValidator.isPointStreet(streetCoordinates));
    }

    @Test
    public void condominiumCoordinate() {
        double[] streetCoordinates = new double[]{-66.16515716119834, -17.40974024111149};
        assertFalse(streetValidator.isPointStreet(streetCoordinates));
    }

    @Test
    public void parkCoordinate() {
        double[] streetCoordinates = new double[]{-58.4069732157805, -34.63873125229828};
        assertFalse(streetValidator.isPointStreet(streetCoordinates));
    }

    @Test
    public void stadiumCoordinate() {
        double[] stadiumCoordinates = new double[]{-58.36472587505779, -34.63562171760705};
        assertFalse(streetValidator.isPointStreet(stadiumCoordinates));
    }

    @Test
    public void lagoonCoordinate() {
        double[] lagoonCoordinates = new double[]{-58.35784503214201, -34.60298249377615};
        assertFalse(streetValidator.isPointStreet(lagoonCoordinates));
    }

    @Test
    public void lakeCoordinate() {
        double[] lakeCoordinates = new double[]{-67.10423014120559, -18.078431727478833};
        assertFalse(streetValidator.isPointStreet(lakeCoordinates));
    }

    @Test
    public void oceanCoordinate() {
        double[] oceanCoordinates = new double[]{-15.581983793348444, -16.840618722869287};
        assertFalse(streetValidator.isPointStreet(oceanCoordinates));
    }

    @Test
    public void antarcticaCoordinate() {
        double[] antarcticaCoordinates = new double[]{12.508477351275317, -78.4620994356629};
        assertFalse(streetValidator.isPointStreet(antarcticaCoordinates));
    }

}
