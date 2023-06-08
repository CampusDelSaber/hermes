package com.isc.hermes.requests.geocoders;

import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LONGITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LONGITUDE;

import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.models.CarmenContext;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import java.util.Objects;

public class StreetValidator {

    private ReverseGeocoding reverseGeocoding;

    public StreetValidator() {
        this.reverseGeocoding = new ReverseGeocoding();
    }

    public boolean isPointStreet(double[] coordinates) {
        double longitude = coordinates[0];
        double latitude = coordinates[1];

        return itIsContinentalLimits(longitude, latitude) &&
                hasStreetContext(reverseGeocoding.getPointInfo(
                longitude, latitude, GeocodingCriteria.TYPE_ADDRESS));
    }

    private boolean hasStreetContext(CarmenFeature feature) {
        if (feature != null) {
            for (CarmenContext context : Objects.requireNonNull(feature.context())) {
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

    private boolean itIsContinentalLimits(double longitude, double latitude) {
        return longitude >= MIN_LONGITUDE && longitude <= MAX_LONGITUDE
                && latitude >= MIN_LATITUDE && latitude <= MAX_LATITUDE;
    }

}
