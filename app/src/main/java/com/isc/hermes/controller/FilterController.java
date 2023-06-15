package com.isc.hermes.controller;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.isc.hermes.MainActivity;
import com.isc.hermes.R;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;



public class FilterController {

    private final MainActivity mainActivity;
    private final MapboxMap mapboxMap;
    private EditText latEditText;
    private EditText longEditText;
    private EditText searchInput;


    public FilterController(MapboxMap mapBox, MainActivity mainActivity) {
        this.mapboxMap = mapBox;
        this.mainActivity = mainActivity;
    }


    public void initComponents() {
        initTextViews();
        initButtons();
    }

    private void initTextViews() {
        latEditText = mainActivity.findViewById(R.id.geocode_latitude_editText);
        longEditText = mainActivity.findViewById(R.id.geocode_longitude_editText);
        searchInput = mainActivity.findViewById(R.id.searchInput);
    }

    private void initButtons() {
        Button startGeocodeButton = mainActivity.findViewById(R.id.start_geocode_button);
        Button chooseCityButton = mainActivity.findViewById(R.id.searchButton);
        manageFiltersButtonsBehaviour(startGeocodeButton);
        chooseCityButton.setOnClickListener(view -> showCityListMenu());
    }

    private void manageFiltersButtonsBehaviour(Button startGeocodeButton) {
        startGeocodeButton.setOnClickListener(view -> {
            if (TextUtils.isEmpty(latEditText.getText().toString())) latEditText.setError(mainActivity.getString(R.string.fill_in_a_value));
            else if (TextUtils.isEmpty(longEditText.getText().toString())) longEditText.setError(mainActivity.getString(R.string.fill_in_a_value));
            else manageResponseForLocationDataInserted();
        });
    }


    private void manageResponseForLocationDataInserted() {
        if (latCoordinateIsValid(Double.parseDouble(latEditText.getText().toString()))
                && longCoordinateIsValid(Double.parseDouble(longEditText.getText().toString())))
            makeGeocodeSearch(new LatLng(Double.parseDouble(latEditText.getText().toString()),
                    Double.parseDouble(longEditText.getText().toString()))); // Make a geocoding search with the values inputted into the EditTexts
        else Toast.makeText(mainActivity, R.string.make_valid_lat, Toast.LENGTH_LONG).show();

    }

    private boolean latCoordinateIsValid(double value) {
        return value >= -90 && value <= 90;
    }

    private boolean longCoordinateIsValid(double value) {
        return value >= -180 && value <= 180;
    }

    private void setCoordinateEditTexts(LatLng latLng) {
        latEditText.setText(String.valueOf(latLng.getLatitude()));
        longEditText.setText(String.valueOf(latLng.getLongitude()));
    }

    private void showCityListMenu() {
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(mainActivity.getString(R.string.access_token))
                .query(searchInput.getText().toString())
                .build();
        manageGeoCodingResponse(mapboxGeocoding);
    }

    private void manageGeoCodingResponse(MapboxGeocoding mapboxGeocoding) {
        mapboxGeocoding.enqueueCall(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GeocodingResponse> call, @NonNull Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                    manageResponseFeatures(response);
                else Timber.tag(TAG).d("onResponse: No result found");
            }

            @Override
            public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private void manageResponseFeatures(Response<GeocodingResponse> response) {
        assert response.body() != null;
        List<CarmenFeature> results = response.body().features();
        Timber.tag(TAG).d("onResponse: %s", searchInput.getText().toString());
        if (results.size() > 0) {
            // Log the first results Point.
            Timber.tag(TAG).d("onResponse: %s", results.get(0).toString());
            updateFiltersComponentsToMap(results);

        } else Timber.tag(TAG).d("onResponse: No result found");
    }


    private void updateFiltersComponentsToMap(List<CarmenFeature> results) {
        Point point = results.get(0).center();
        assert point != null;
        LatLng cityLatLng = new LatLng(point.latitude(), point.longitude());
        setCoordinateEditTexts(cityLatLng);
        animateCameraToNewPosition(cityLatLng);
        makeGeocodeSearch(cityLatLng);
        Timber.tag(TAG).d("onResponse: %s", point.toString());
    }

    private void makeGeocodeSearch(final LatLng latLng) {
        try {
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(mainActivity.getString(R.string.access_token))
                    .query(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                    .mode(GeocodingCriteria.MODE_PLACES)
                    .build();
            sendQueryCall(client, latLng);

        } catch (ServicesException servicesException) {
            Timber.e("Error geocoding: %s", servicesException.toString());
            servicesException.printStackTrace();
        }
    }

    private void sendQueryCall(MapboxGeocoding client, LatLng latLng) {
        client.enqueueCall(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GeocodingResponse> call,
                                   @NonNull Response<GeocodingResponse> response) {
                if (response.body() != null) setCameraToResponsePosition(response, latLng);
            }

            @Override
            public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable throwable) {
                Timber.e("Geocoding Failure: %s", throwable.getMessage());
            }
        });
    }

    private void setCameraToResponsePosition(Response<GeocodingResponse> response, LatLng latLng) {
        assert response.body() != null;
        List<CarmenFeature> results = response.body().features();
        if (results.size() > 0) {
            animateCameraToNewPosition(latLng);
        } else Toast.makeText(mainActivity, R.string.no_results,
                    Toast.LENGTH_SHORT).show();
    }


    private void animateCameraToNewPosition(LatLng latLng) {
        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(11)
                        .build()), 1500);
    }
}
