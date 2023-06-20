package com.isc.hermes.requests.geocoders;

import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LONGITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LONGITUDE;

import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.models.CarmenContext;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import java.util.Objects;

/**
 * This class has the responsibility to validate a coordinates to know if are belong a
 * street using the reverse geocoding of mapbox.
 */
public class StreetValidator {

    private ReverseGeocoding reverseGeocoding;

    /**
     * This is the constructor method to initialize the reverse geocoding.
     */
    public StreetValidator() {
        this.reverseGeocoding = new ReverseGeocoding();
    }

    /**
     * This method validate if a point coordinate is a street.
     *
     * @param longitude is the longitude point.
     * @param latitude  is the latitude point.
     * @return boolean to know if the point coordinate is a street.
     */
    public boolean isPointStreet(double longitude, double latitude) {
        return hasStreetContext(reverseGeocoding.getPointInfo(
                        longitude, latitude, GeocodingCriteria.TYPE_ADDRESS));
    }

    /**
     * This method validate is the coordinates of a point are in the continental limits.
     *
     * @param longitude is the longitude point.
     * @param latitude  is the latitude point.
     * @return boolean to know is the coordinates are in the limit.
     */
    private boolean itIsContinentalLimits(double longitude, double latitude) {
        return longitude >= MIN_LONGITUDE && longitude <= MAX_LONGITUDE
                && latitude >= MIN_LATITUDE && latitude <= MAX_LATITUDE;
    }

    /**
     * This method validate if a place information has a street context.
     *
     * @param placeInformation is the response of reverse geocoding.
     * @return boolean to know if the place has a street context.
     */
    private boolean hasStreetContext(CarmenFeature placeInformation) {
        if (placeInformation != null) {
            for (CarmenContext context : Objects.requireNonNull(placeInformation.context())) {
                if (Objects.requireNonNull(context.id()).startsWith("place.")
                        || context.id().startsWith("postcode.")
                        || context.id().startsWith("neighborhood.")
                        || context.id().startsWith("locality.")
                        || context.id().startsWith("region.")
                        || context.id().startsWith("country.")
                        || context.id().startsWith("street.")
                        || context.id().startsWith("intersection.")
                        || context.id().startsWith("address."))
                    return true;
            }
        }
        return false;
    }
}
