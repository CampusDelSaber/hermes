package com.isc.hermes.validators;

import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.geojson.Point;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;

import retrofit2.Response;

public class ReverseGeocoding {

    public boolean isStreet() {
/*        double longitude = coordinate[0];
        double latitude = coordinate[1];*/

/*
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken("sk.eyJ1IjoiaGVybWVzLW1hcHMiLCJhIjoiY2xpamxmbnQxMDg2aDNybGc0YmUzcHloaCJ9.__1WydgkE41IAuYtsob0jA")
                .query(Point.fromLngLat(longitude, latitude))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build();
*/

        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken("sk.eyJ1IjoiaGVybWVzLW1hcHMiLCJhIjoiY2xpamxmbnQxMDg2aDNybGc0YmUzcHloaCJ9.__1WydgkE41IAuYtsob0jA")
                .query(Point.fromLngLat(-66.199711199814, -17.39245060108115))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build();

        try {
            Response<GeocodingResponse> response = mapboxGeocoding.executeCall();
            if (response.isSuccessful()) {
                GeocodingResponse geocodingResponse = response.body();
                if (geocodingResponse != null && !geocodingResponse.features().isEmpty()) {
                    CarmenFeature feature = geocodingResponse.features().get(0);
                    String placeType = feature.placeType().get(0);
                    System.out.println(feature);
                    return placeType.equals("street");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
