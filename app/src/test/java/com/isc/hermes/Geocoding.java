package com.isc.hermes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.isc.hermes.requests.Geocoder;
import com.isc.hermes.requests.ReverseGeocoding;

import org.junit.Test;

public class Geocoding {

    @Test
    public void buildGeocoder() {
        assertNotNull(Geocoder.getInstance().getGeocoder());
    }

    @Test
    public void isStreetCoordinate() {
        double[] streetCoordinates = new double[]{-66.16099899732424, -17.378011788682027};
        assertTrue(new ReverseGeocoding().isStreet(streetCoordinates));
    }

    @Test
    public void isLagoonCoordinate() {
        double[] lagoonCoordinates = new double[]{-66.143789,-17.405456};
        assertFalse(new ReverseGeocoding().isStreet(lagoonCoordinates));
    }

    @Test
    public void isOceanCoordinate() {
        double[] oceanCoordinates = new double[]{-15.581983793348444, -16.840618722869287};
        assertFalse(new ReverseGeocoding().isStreet(oceanCoordinates));
    }

}
