package com.isc.hermes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.isc.hermes.validators.Geocoder;
import com.isc.hermes.validators.ReverseGeocoding;

import org.junit.Test;

public class Geocoding {

    @Test
    public void buildGeocoder() {
        assertNotNull(Geocoder.getInstance().getGeocoder());
    }

    @Test
    public void reverseGeocoding() {
        assertTrue(new ReverseGeocoding().isStreet());
    }

}
