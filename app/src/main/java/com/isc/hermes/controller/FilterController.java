package com.isc.hermes.controller;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class FilterController extends AppCompatActivity {

    private MapboxMap mapboxMap;
    private Button chooseCityButton;
    private EditText latEditText;
    private EditText longEditText;
    private EditText searchInput;


    private void initTextViews() {
        latEditText = findViewById(R.id.geocode_latitude_editText);
        longEditText = findViewById(R.id.geocode_longitude_editText);
        searchInput = findViewById(R.id.searchInput);
    }

    private void initButtons() {
        Button startGeocodeButton = findViewById(R.id.start_geocode_button);
        chooseCityButton = findViewById(R.id.searchButton);

        startGeocodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make sure the EditTexts aren't empty
                if (TextUtils.isEmpty(latEditText.getText().toString())) {
                    latEditText.setError(getString(R.string.fill_in_a_value));
                } else if (TextUtils.isEmpty(longEditText.getText().toString())) {
                    longEditText.setError(getString(R.string.fill_in_a_value));
                } else {
                    if (latCoordinateIsValid(Double.valueOf(latEditText.getText().toString()))
                            && longCoordinateIsValid(Double.valueOf(longEditText.getText().toString()))) {
                        // Make a geocoding search with the values inputted into the EditTexts
                        makeGeocodeSearch(new LatLng(Double.valueOf(latEditText.getText().toString()),
                                Double.valueOf(longEditText.getText().toString())));
                    } else {
                        Toast.makeText(FilterController.this, R.string.make_valid_lat, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        chooseCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCityListMenu();
            }
        });
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
                .accessToken(getString(R.string.access_token))
                .query(searchInput.getText().toString())
                .build();


        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CarmenFeature> results = response.body().features();
                    Log.d(TAG, "onResponse: " + searchInput.getText().toString());
                    if (results.size() > 0) {
                        // Log the first results Point.

                        Log.d(TAG, "onResponse: " +  results.get(0).toString());
                        Point point = results.get(0).center();
                        LatLng cityLatLng = new LatLng(point.latitude(), point.longitude());
                        setCoordinateEditTexts(cityLatLng);
                        animateCameraToNewPosition(cityLatLng);
                        makeGeocodeSearch(cityLatLng);
                        Log.d(TAG, "onResponse: " + point.toString());
                    } else {
                        // No results for your request were found.
                        Log.d(TAG, "onResponse: No result found");
                    }
                } else {

                    // No result for your request were found.
                    Log.d(TAG, "onResponse: No result found");

                }

            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });

    }

    private void makeGeocodeSearch(final LatLng latLng) {
        try {
            // Build a Mapbox geocoding request
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.access_token))
                    .query(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                    .mode(GeocodingCriteria.MODE_PLACES)
                    .build();

            client.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call,
                                       Response<GeocodingResponse> response) {
                    if (response.body() != null) {
                        List<CarmenFeature> results = response.body().features();
                        if (results.size() > 0) {

                            // Get the first Feature from the successful geocoding response
                            CarmenFeature feature = results.get(0);
                            animateCameraToNewPosition(latLng);
                        } else {
                            Toast.makeText(FilterController.this, R.string.no_results,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                    Timber.e("Geocoding Failure: " + throwable.getMessage());
                }
            });
        } catch (ServicesException servicesException) {
            Timber.e("Error geocoding: " + servicesException.toString());
            servicesException.printStackTrace();
        }
    }

    private void animateCameraToNewPosition(LatLng latLng) {
        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(11)
                        .build()), 1500);
    }
}
