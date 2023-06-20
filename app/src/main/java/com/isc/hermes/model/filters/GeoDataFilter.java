package com.isc.hermes.model.filters;

import android.content.Context;
import android.os.StrictMode;

import androidx.annotation.NonNull;

import com.isc.hermes.R;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.io.IOException;
import java.util.List;
import timber.log.Timber;


public class GeoDataFilter {

    public List<CarmenFeature> getWaypointData(Context context, LatLng latLng) {
        return makeGeocodeSearch(context, latLng);
    }

    /**
     * Performs a geocoding search using the given LatLng object.
     * Uses the MapboxGeocoding API for geocoding.
     *
     * @param latLng The LatLng object representing the location to search.
     */
    private List<CarmenFeature> makeGeocodeSearch(Context context, final LatLng latLng) {
        try {
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(context.getString(R.string.access_token))
                    .query(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                    .mode(GeocodingCriteria.MODE_PLACES)
                    .build();
            return sendQueryCall(client, latLng);

        } catch (ServicesException servicesException) {
            Timber.e("Error geocoding: %s", servicesException.toString());
            servicesException.printStackTrace();
        }
        return null;
    }

    /**
     * Sends the geocoding query call to the MapboxGeocoding API.
     * Handles the success and failure cases of the API call.
     *
     * @param client The MapboxGeocoding object representing the geocoding API call.
     * @param latLng The LatLng object representing the location used for the geocoding search.
     */
    private List<CarmenFeature> sendQueryCall(MapboxGeocoding client, LatLng latLng) {
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            return client.executeCall().body().features();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
