package com.isc.hermes.generators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.isc.hermes.requests.geocoders.Geocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

public class GeocodingTest {

    private Geocoding geocoding;

    @Before
    public void initGeocoding() {
        geocoding = new Geocoding();
    }

    @Test
    public void geocodingResponse() {
        assertNotNull(geocoding.getPlaceInformation("jalasoft"));
    }

    @Test
    public void placeAddress() {
        CarmenFeature response = geocoding.getPlaceInformation("jalasoft");
        System.out.println(response);
        String addressExpect = "Melchor Perez";
        String addressResult = response.properties().get("address").getAsString();

        assertEquals(addressExpect, addressResult);
    }

    @Test
    public void placeCoordinates() {
        CarmenFeature response = geocoding.getPlaceInformation("jalasoft");

        Double longitudeExpected = -66.175615;
        Double latitudeExpected = -17.366102;

        List<Double> coordinateResponse = Objects.requireNonNull(response.center()).coordinates();
        Double longitudeResult = coordinateResponse.get(0);
        Double latitudeResult = coordinateResponse.get(1);

        assertEquals(longitudeExpected, longitudeResult);
        assertEquals(latitudeExpected, latitudeResult);
    }


}
