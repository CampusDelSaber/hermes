package com.isc.hermes.controller;

import static android.content.ContentValues.TAG;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import com.isc.hermes.R;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

        manageGeoCodingResponse(mapboxGeocoding, callback);
    }

    public void makeGeocodeSearch(LatLng latLng, Consumer<List<CarmenFeature>> callback) {
        try {
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(activity.getString(R.string.access_token))
                    .query(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                    .mode(GeocodingCriteria.MODE_PLACES)
                    .build();

            manageGeoCodingResponse(client, callback);

        } catch (ServicesException servicesException) {
            Timber.e("Error geocoding: %s", servicesException.toString());
            servicesException.printStackTrace();
        }
    }

    private void manageGeoCodingResponse(MapboxGeocoding mapboxGeocoding, Consumer<List<CarmenFeature>> callback) {
        mapboxGeocoding.enqueueCall(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GeocodingResponse> call, @NonNull Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CarmenFeature> results = response.body().features();
                    callback.accept(results);
                } else Timber.tag(TAG).d("onResponse: No result found");
            }

            @Override
            public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
