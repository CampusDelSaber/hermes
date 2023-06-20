package com.isc.hermes.controller;

import android.app.Activity;
import androidx.core.util.Consumer;
import com.isc.hermes.R;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.util.List;
import timber.log.Timber;

public class GeocodeFiltersManager {
    private Activity activity;

    public GeocodeFiltersManager(Activity activity) {
        this.activity = activity;
    }

    public void showCityListMenu(String query, Consumer<List<CarmenFeature>> callback) {
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(activity.getString(R.string.access_token))
                .query(query)
                .build();

        // TODO: implemented manageGeoCodingResponse
    }

    public void makeGeocodeSearch(LatLng latLng, Consumer<List<CarmenFeature>> callback) {
        try {
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(activity.getString(R.string.access_token))
                    .query(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                    .mode(GeocodingCriteria.MODE_PLACES)
                    .build();

            // TODO: implemented manageGeoCodingResponse

        } catch (ServicesException servicesException) {
            Timber.e("Error geocoding: %s", servicesException.toString());
            servicesException.printStackTrace();
        }
    }
}
