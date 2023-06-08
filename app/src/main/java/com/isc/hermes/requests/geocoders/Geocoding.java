package com.isc.hermes.requests.geocoders;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import java.util.Objects;

public class Geocoding extends Geocoder {


    public CarmenFeature getPlaceInformation(String placeName) {
        return getInfoResponse(builtGeocoding(placeName));
    }

    private MapboxGeocoding builtGeocoding(String placeName) {
        return MapboxGeocoding.builder()
                    .accessToken(Objects.requireNonNull(dotenv.get("MAPBOX_TOKEN")))
                    .query(placeName)
                    .build();

    }

}
