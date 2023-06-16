package com.isc.hermes.requests.geocoders;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import java.util.Objects;

/**
 * This class is responsible for using mapbox geocoding.
 */
public class Geocoding extends Geocoder {

    /**
     * This method get place information as response of geocoding about place.
     *
     * @param placeName is the place name to look up information.
     * @return place information response.
     */
    public CarmenFeature getPlaceInformation(String placeName) {
        return getInfoResponse(builtGeocoding(placeName));
    }

    /**
     * This method build a mapbox geocoding using a place name as a query.
     *
     * @param placeName query to build the geocoding.
     * @return mapbox geocoding.
     */
    private MapboxGeocoding builtGeocoding(String placeName) {
        return MapboxGeocoding.builder()
                    .accessToken(Objects.requireNonNull(dotenv.get("MAPBOX_TOKEN")))
                    .query(placeName)
                    .build();

    }

}
