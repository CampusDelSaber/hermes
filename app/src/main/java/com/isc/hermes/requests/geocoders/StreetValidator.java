package com.isc.hermes.requests.geocoders;

import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LONGITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LONGITUDE;

import android.graphics.PointF;

import com.isc.hermes.utils.MapClickEventsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

/**
 * This class has the responsibility to validate a coordinates to know if are belong a
 * street using the reverse geocoding of mapbox.
 */
public class StreetValidator {

    private MapboxMap mapboxMap;

    /**
     * This is the constructor method to initialize the reverse geocoding.
     */
    public StreetValidator() {
        this.mapboxMap = MapClickEventsManager.getInstance().getMapboxMap();
    }

    /**
     * This method validate if a point coordinate is a street.
     *
     * @param longitude is the longitude point.
     * @param latitude  is the latitude point.
     * @return boolean to know if the point coordinate is a street.
     */
    public boolean isPointStreet(double longitude, double latitude) {
        return hasStreetContext(new LatLng(longitude, latitude));
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
     * @param point is the point to know if is a street point.
     * @return boolean to know if the place has a street context.
     */
    public boolean hasStreetContext(LatLng point) {
        if (mapboxMap != null) {
            PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
            List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
            return (!features.isEmpty() &&
                    (features.get(0).geometry().type().equals("MultiLineString") ||
                            features.get(0).geometry().type().equals("LineString")));
        }

        return true;
    }
}
