package com.isc.hermes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.isc.hermes.requests.Geocoder;
import com.isc.hermes.requests.ReverseGeocoding;

import org.junit.Test;

public class Geocoding {

    @Test
    public void buildGeocoder() {
        assertNotNull(Geocoder.getInstance().getGeocoder());
    }

    @Test
    public void isStreet() {
        double[] streetCoordinates = new double[]{-75.14713469698103, -19.436379867016758};
        assertFalse(new ReverseGeocoding().isStreet(streetCoordinates));
    }

    @Test
    public void isNotStreet() {
        double[] mountainCoordinates = new double[]{-75.14713469698103, -19.436379867016758};
        assertFalse(new ReverseGeocoding().isStreet(mountainCoordinates));
    }

}
