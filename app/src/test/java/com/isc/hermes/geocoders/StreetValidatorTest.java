package com.isc.hermes.geocoders;

import  static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.isc.hermes.requests.geocoders.StreetValidator;

import org.junit.Before;
import org.junit.Test;

public class StreetValidatorTest {

    private StreetValidator streetValidator;
    private double longitude, latitude;

    @Before
    public void initReverseGeocoding() {
        streetValidator = new StreetValidator();
    }

    @Test
    public void streetCoordinate() {
        double[] streetCoordinates = new double[]{-66.15502527828558, -17.395038120001512};
        longitude = streetCoordinates[0];
        latitude = streetCoordinates[1];
        assertTrue(streetValidator.isPointStreet(longitude, latitude));
    }

    @Test
    public void condominiumCoordinate() {
        double[] condominiumCoordinates = new double[]{-66.16515716119834, -17.40974024111149};
        longitude = condominiumCoordinates[0];
        latitude = condominiumCoordinates[1];
        assertFalse(streetValidator.isPointStreet(longitude, latitude));
    }

    @Test
    public void parkCoordinate() {
        double[] parkCoordinates = new double[]{-58.4069732157805, -34.63873125229828};
        longitude = parkCoordinates[0];
        latitude = parkCoordinates[1];
        assertFalse(streetValidator.isPointStreet(longitude, latitude));
    }

    @Test
    public void stadiumCoordinate() {
        double[] stadiumCoordinates = new double[]{-58.36472587505779, -34.63562171760705};
        longitude = stadiumCoordinates[0];
        latitude = stadiumCoordinates[1];
        assertFalse(streetValidator.isPointStreet(longitude, latitude));
    }

    @Test
    public void lagoonCoordinate() {
        double[] lagoonCoordinates = new double[]{-58.35784503214201, -34.60298249377615};
        longitude = lagoonCoordinates[0];
        latitude = lagoonCoordinates[1];
        assertFalse(streetValidator.isPointStreet(longitude, latitude));
    }

    @Test
    public void lakeCoordinate() {
        double[] lakeCoordinates = new double[]{-67.10423014120559, -18.078431727478833};
        longitude = lakeCoordinates[0];
        latitude = lakeCoordinates[1];
        assertFalse(streetValidator.isPointStreet(longitude, latitude));
    }

    @Test
    public void oceanCoordinate() {
        double[] oceanCoordinates = new double[]{-15.581983793348444, -16.840618722869287};
        longitude = oceanCoordinates[0];
        latitude = oceanCoordinates[1];
        assertFalse(streetValidator.isPointStreet(longitude, latitude));
    }

    @Test
    public void antarcticaCoordinate() {
        double[] antarcticaCoordinates = new double[]{12.508477351275317, -78.4620994356629};
        longitude = antarcticaCoordinates[0];
        latitude = antarcticaCoordinates[1];
        assertFalse(streetValidator.isPointStreet(longitude, latitude));
    }

}
