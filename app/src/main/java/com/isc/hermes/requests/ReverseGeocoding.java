package com.isc.hermes.requests;

import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.models.CarmenContext;
import com.mapbox.geojson.Point;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;

import retrofit2.Response;

public class ReverseGeocoding {

    public boolean isStreet(double[] coordinates) {
        double longitude = coordinates[0];
        double latitude = coordinates[1];

/*
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken("sk.eyJ1IjoiaGVybWVzLW1hcHMiLCJhIjoiY2xpamxmbnQxMDg2aDNybGc0YmUzcHloaCJ9.__1WydgkE41IAuYtsob0jA")
                .query(Point.fromLngLat(longitude, latitude))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build();
*/

        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken("sk.eyJ1IjoiaGVybWVzLW1hcHMiLCJhIjoiY2xpamxmbnQxMDg2aDNybGc0YmUzcHloaCJ9.__1WydgkE41IAuYtsob0jA")
                .query(Point.fromLngLat(longitude, latitude))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build();

        try {
            Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
            if (response.isSuccessful()) {
                GeocodingResponse geocodingResponse = response.body();
                if (geocodingResponse != null && !geocodingResponse.features().isEmpty()) {
                    CarmenFeature feature = geocodingResponse.features().get(0);
                    return hasStreetContext(feature);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean hasStreetContext(CarmenFeature feature) {
        for (CarmenContext context : feature.context()) {
            if (context.id().startsWith("place.")
                    || context.id().startsWith("postcode.")
                    || context.id().startsWith("neighborhood.")
                    || context.id().startsWith("locality.")
                    || context.id().startsWith("region.")
                    || context.id().startsWith("country.")) {
                return true;
            }
        }
        return false;
    }

}
