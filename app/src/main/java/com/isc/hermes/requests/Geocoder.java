package com.isc.hermes.requests;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import java.util.List;

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

}
