package com.isc.hermes.validators;

import android.telecom.Call;
import android.util.Log;

import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import java.util.List;

import javax.security.auth.callback.Callback;

import okhttp3.Response;

public class Geocoder {

    private static Geocoder geocoder;
    private final String MAPBOX_TOKEN;
    private MapboxGeocoding mapboxGeocoding;


    public static Geocoder getInstance() {
        if (geocoder == null)
            geocoder = new Geocoder();

        return geocoder;
    }

    private Geocoder() {
        this.MAPBOX_TOKEN = "sk.eyJ1IjoiaGVybWVzLW1hcHMiLCJhIjoiY2xpamxmbnQxMDg2aDNybGc0YmUzcHloaCJ9.__1WydgkE41IAuYtsob0jA";
    }

    public MapboxGeocoding getGeocoder() {
        if (mapboxGeocoding == null)
            buildGeocoder();

        return mapboxGeocoding;
    }

    public void buildGeocoder() {
        mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(MAPBOX_TOKEN)
                .query("1600 Pennsylvania Ave NW")
                .build();

        /*mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(MAPBOX_TOKEN)
                .query(Point.fromLngLat(-77.03655, 38.89770))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build();*/
    }

    public void reverseGeocoding() {
        getGeocoder().enqueueCall(new retrofit2.Callback<GeocodingResponse>() {
            @Override
            public void onResponse(retrofit2.Call<GeocodingResponse> call, retrofit2.Response<GeocodingResponse> response) {
                List<CarmenFeature> results = response.body().features();

                if (results.size() > 0) {

                    // Log the first results Point.
                    Point firstResultPoint = results.get(0).center();
                    System.out.println("onResponse: " + firstResultPoint.toString());

                } else {

                    // No result for your request were found.
                    System.out.println("onResponse: No result found");

                }
            }

            @Override
            public void onFailure(retrofit2.Call<GeocodingResponse> call, Throwable t) {
                System.out.println("geocoder failed");

            }
        });
    }

}
