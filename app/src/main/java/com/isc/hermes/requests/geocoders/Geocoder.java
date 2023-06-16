package com.isc.hermes.requests.geocoders;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;

import io.github.cdimascio.dotenv.Dotenv;
import retrofit2.Response;

/**
 * This is a parent class for the geocoding and reverse geocoding child classes.
 */
public class Geocoder {

    protected Dotenv dotenv;

    /**
     * This is a constructor method to initialize dotenv getter.
     */
    public Geocoder() {
        this.dotenv = Dotenv.load();
    }

    /**
     * This method get response using a mapbox geocoding created.
     *
     * @param mapboxGeocoding to get response about geocoding or reverse geocoding.
     * @return geocoding information.
     */
    protected CarmenFeature getInfoResponse(MapboxGeocoding mapboxGeocoding) {
        try {
            Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
            GeocodingResponse geocodingResponse = response.isSuccessful() ? response.body() : null;
            if (geocodingResponse != null && !geocodingResponse.features().isEmpty()) {
                return geocodingResponse.features().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
