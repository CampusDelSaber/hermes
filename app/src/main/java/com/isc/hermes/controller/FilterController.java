package com.isc.hermes.controller;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


/**
 * Class to represent the filters controller to manage the ui components and perform the filters correctly
 */
public class FilterController {
    private final MainActivity mainActivity;
    private final MapboxMap mapboxMap;
    private EditText latEditText;
    private EditText longEditText;
    private EditText searchInput;


    /**
     * Constructor method to initialize the mapbox and main Activity attributes
     * @param mapBox mapbox instanced in main activity
     * @param mainActivity the actual window set on the emulator
     */
    public FilterController(MapboxMap mapBox, MainActivity mainActivity) {
        this.mapboxMap = mapBox;
        this.mainActivity = mainActivity;
    }

    /**
     * Method to init all Filters component manager's behaviours
     */
    public void initComponents() {
        initFilterOptionsButton();
        initTextViews();
        initFiltersButtons();
    }

    /**
     * Method to manage the behaviour of the button that is displayed on header to set the filters container
     */
    private void initFilterOptionsButton() {
        CircleImageView filtersButton = mainActivity.findViewById(R.id.filtersButton);
        CardView filtersContainer = mainActivity.findViewById(R.id.filtersContainer);
        filtersContainer.setVisibility(View.INVISIBLE);
        filtersButton.setOnClickListener(v -> {
            if (filtersContainer.getVisibility() == View.INVISIBLE)
                filtersContainer.setVisibility(View.VISIBLE);
            else {
                filtersContainer.setVisibility(View.INVISIBLE);
                closeKeyBoard(v);
            }
        });
    }

    /**
     * Method to init the text labels views in ui for the filters
     */
    private void initTextViews() {
        latEditText = mainActivity.findViewById(R.id.geocode_latitude_editText);
        longEditText = mainActivity.findViewById(R.id.geocode_longitude_editText);
        searchInput = mainActivity.findViewById(R.id.searchInput);
    }

    /**
     * Method tu init filters' buttons inside the filters' container
     */
    private void initFiltersButtons() {
        Button startGeocodeButton = mainActivity.findViewById(R.id.start_geocode_button);
        Button chooseCityButton = mainActivity.findViewById(R.id.searchButton);
        manageFiltersButtonsBehaviour(startGeocodeButton);
        chooseCityButton.setOnClickListener(view -> {
            closeKeyBoard(view);
            showCityListMenu();
        });
    }

    /**
     * Method to manage the behaviour of the start geocoding button in ui to send a query and receive a response
     * @param startGeocodeButton the start filter button when latitude and longitude are filled
     */
    private void manageFiltersButtonsBehaviour(Button startGeocodeButton) {
        startGeocodeButton.setOnClickListener(view -> {
            closeKeyBoard(view);
            if (TextUtils.isEmpty(latEditText.getText().toString())) latEditText.setError(mainActivity.getString(R.string.fill_in_a_value));
            else if (TextUtils.isEmpty(longEditText.getText().toString())) longEditText.setError(mainActivity.getString(R.string.fill_in_a_value));
            else manageResponseForLocationDataInserted();
        });
    }

    /**
     * Method to manage the response of the query sent to render on ui creating a geocoding search with the values inputted into the EditTexts
     */
    private void manageResponseForLocationDataInserted() {
        if (latCoordinateIsValid(Double.parseDouble(latEditText.getText().toString()))
                && longCoordinateIsValid(Double.parseDouble(longEditText.getText().toString())))
            makeGeocodeSearch(new LatLng(Double.parseDouble(latEditText.getText().toString()),
                    Double.parseDouble(longEditText.getText().toString())));
        else Toast.makeText(mainActivity, mainActivity.getString(R.string.make_valid_lat), Toast.LENGTH_LONG).show();

    }
    /**
     * Checks if the latitude coordinate value is within the valid range.
     *
     * @param value The latitude coordinate value to check.
     * @return {@code true} if the latitude coordinate value is within the valid range of -90 to 90, {@code false} otherwise.
     */
    private boolean latCoordinateIsValid(double value) {
        return value >= -90 && value <= 90;
    }

    /**
     * Checks if the longitude coordinate value is within the valid range.
     *
     * @param value The longitude coordinate value to check.
     * @return {@code true} if the longitude coordinate value is within the valid range of -180 to 180, {@code false} otherwise.
     */
    private boolean longCoordinateIsValid(double value) {
        return value >= -180 && value <= 180;
    }

    /**
     * Sets the coordinate EditTexts with the latitude and longitude values from the given LatLng object.
     *
     * @param latLng The LatLng object containing the latitude and longitude values.
     */
    private void setCoordinateEditTexts(LatLng latLng) {
        latEditText.setText(String.valueOf(latLng.getLatitude()));
        longEditText.setText(String.valueOf(latLng.getLongitude()));
    }

    /**
     * Shows the city list menu by performing a geocoding search using the search input text.
     * Uses the MapboxGeocoding API for geocoding.
     */
    private void showCityListMenu() {
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(mainActivity.getString(R.string.access_token))
                .query(searchInput.getText().toString())
                .build();
        manageGeoCodingResponse(mapboxGeocoding);
    }

    /**
     * Manages the response from the MapboxGeocoding API.
     * Handles the success and failure cases of the API call.
     *
     * @param mapboxGeocoding The MapboxGeocoding object representing the geocoding API call.
     */
    private void manageGeoCodingResponse(MapboxGeocoding mapboxGeocoding) {
        mapboxGeocoding.enqueueCall(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GeocodingResponse> call, @NonNull Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                    manageResponseFeatures(response);
                else Timber.tag(TAG).d("onResponse: %s", mainActivity.getString(R.string.no_results));
            }

            @Override
            public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    /**
     * Processes the features in the geocoding response.
     *
     * @param response The response from the geocoding API call.
     */
    private void manageResponseFeatures(Response<GeocodingResponse> response) {
        assert response.body() != null;
        List<CarmenFeature> results = response.body().features();
        Timber.tag(TAG).d("onResponse: %s", searchInput.getText().toString());
        if (results.size() > 0) {
            Timber.tag(TAG).d("onResponse: %s", results.get(0).toString());
            updateFiltersComponentsToMap(results);

        } else Timber.tag(TAG).d("onResponse: %s", mainActivity.getString(R.string.no_results));
    }

    /**
     * Updates the filter components on the map based on the given list of CarmenFeature results.
     *
     * @param results The list of CarmenFeature results.
     */
    private void updateFiltersComponentsToMap(List<CarmenFeature> results) {
        Point point = results.get(0).center();
        assert point != null;
        LatLng cityLatLng = new LatLng(point.latitude(), point.longitude());
        setCoordinateEditTexts(cityLatLng);
        animateCameraToNewPosition(cityLatLng);
        makeGeocodeSearch(cityLatLng);
        Timber.tag(TAG).d("onResponse: %s", point.toString());
    }

    /**
     * Performs a geocoding search using the given LatLng object.
     * Uses the MapboxGeocoding API for geocoding.
     *
     * @param latLng The LatLng object representing the location to search.
     */
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

    /**
     * Sends the geocoding query call to the MapboxGeocoding API.
     * Handles the success and failure cases of the API call.
     *
     * @param client The MapboxGeocoding object representing the geocoding API call.
     * @param latLng The LatLng object representing the location used for the geocoding search.
     */
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

    /**
     * Sets the camera position to the response position obtained from the geocoding API call.
     * If there are no results, displays a toast message indicating no results were found.
     *
     * @param response The response from the geocoding API call.
     * @param latLng   The LatLng object representing the location used for the geocoding search.
     */
    private void setCameraToResponsePosition(Response<GeocodingResponse> response, LatLng latLng) {
        assert response.body() != null;
        List<CarmenFeature> results = response.body().features();
        if (results.size() > 0) {
            animateCameraToNewPosition(latLng);
        } else Toast.makeText(mainActivity, mainActivity.getString(R.string.no_results),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Closes the keyboard for the specified view.
     *
     * @param view The view associated with the keyboard to be closed.
     */
    private void closeKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Animates the camera to the new position represented by the given LatLng object.
     *
     * @param latLng The LatLng object representing the new camera position.
     */
    private void animateCameraToNewPosition(LatLng latLng) {
        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(11)
                        .build()), 1500);
    }
}
