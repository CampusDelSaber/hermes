package com.isc.hermes.controller;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;
import com.isc.hermes.MainActivity;
import com.isc.hermes.R;
import com.isc.hermes.requests.geocoders.StreetValidator;
import com.isc.hermes.utils.CameraAnimator;
import com.isc.hermes.utils.KeyBoardManager;
import com.isc.hermes.view.FiltersView;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import java.util.List;

/**
 * Class to represent the filters controller to manage the ui components and perform the filters correctly
 */
public class FilterController {
    private final MainActivity mainActivity;
    private final MapboxMap mapboxMap;
    private FiltersView filtersView;
    private CameraAnimator cameraAnimator;
    private StreetValidator streetValidator;
    private GeocodeFiltersManager geocodingManager;
    private LatLng currentLatLng;


    /**
     * Constructor method to initialize the mapbox and main Activity attributes
     * @param mapBox mapbox instanced in main activity
     * @param mainActivity the actual window set on the emulator
     */
    public FilterController(MapboxMap mapBox, MainActivity mainActivity) {
        this.mapboxMap = mapBox;
        this.mainActivity = mainActivity;
        this.filtersView = new FiltersView(mainActivity);
        this.cameraAnimator = new CameraAnimator(mapboxMap);
        this.streetValidator = new StreetValidator();
        this.geocodingManager = new GeocodeFiltersManager(mainActivity);
        FilterCategoriesController filterCategoriesController = new FilterCategoriesController(mainActivity);
    }

    /**
     * Method to init all Filters component manager's behaviours
     */
    public void initComponents() {
        initFiltersButtons();
    }

    /**
     * Method tu init filters' buttons inside the filters' container
     */
    private void initFiltersButtons() {
        manageFiltersButtonsBehaviour(filtersView.getStartGeocodeButton());
        filtersView.getChooseCityButton().setOnClickListener(view -> {
            KeyBoardManager.getInstance().closeKeyBoard(view, mainActivity);
            geocodingManager.showCityListMenu(filtersView.getSearchInput().getText().toString(), this::updateFiltersComponentsToMap);
        });
    }

    /**
     * Method to manage the behaviour of the start geocoding button in ui to send a query and receive a response
     * @param startGeocodeButton the start filter button when latitude and longitude are filled
     */
    private void manageFiltersButtonsBehaviour(Button startGeocodeButton) {
        startGeocodeButton.setOnClickListener(view -> {
            KeyBoardManager.getInstance().closeKeyBoard(view, mainActivity);
            if (TextUtils.isEmpty(filtersView.getLatEditText().getText().toString())) filtersView.getLatEditText().setError(mainActivity.getString(R.string.fill_in_a_value));
            else if (TextUtils.isEmpty(filtersView.getLongEditText().getText().toString())) filtersView.getLongEditText().setError(mainActivity.getString(R.string.fill_in_a_value));
            else manageResponseForLocationDataInserted();
        });
    }

    /**
     * Method to manage the response of the query sent to render on ui creating a geocoding search with the values inputted into the EditTexts
     */
    private void manageResponseForLocationDataInserted() {
        double latitude = Double.parseDouble(filtersView.getLatEditText().getText().toString());
        double longitude = Double.parseDouble(filtersView.getLongEditText().getText().toString());

        if (streetValidator.isPointStreet(latitude, longitude)) {
            LatLng latLng = new LatLng(latitude, longitude);
            geocodingManager.makeGeocodeSearch(latLng, this::setCameraToResponsePosition);
        } // Make a geocoding search with the values inputted into the EditTexts
        else Toast.makeText(mainActivity, R.string.make_valid_lat, Toast.LENGTH_LONG).show();

    }

    /**
     * Sets the coordinate EditTexts with the latitude and longitude values from the given LatLng object.
     *
     * @param latLng The LatLng object containing the latitude and longitude values.
     */
    private void setCoordinateEditTexts(LatLng latLng) {
        filtersView.getLatEditText().setText(String.valueOf(latLng.getLatitude()));
        filtersView.getLongEditText().setText(String.valueOf(latLng.getLongitude()));
    }

    /**
     * Updates the filter components on the map based on the given list of CarmenFeature results.
     *
     * @param results The list of CarmenFeature results.
     */
    private void updateFiltersComponentsToMap(List<CarmenFeature> results) {
        Point point = results.get(0).center();
        assert point != null;
        currentLatLng = new LatLng(point.latitude(), point.longitude());
        setCoordinateEditTexts(currentLatLng);
        cameraAnimator.animateCameraToNewPosition(currentLatLng,11, 1500);
        geocodingManager.makeGeocodeSearch(currentLatLng, this::setCameraToResponsePosition);
    }

    /**
     * Sets the camera position to the response position obtained from the geocoding API call.
     * If there are no results, displays a toast message indicating no results were found.
     * @param results The list of CarmenFeature results.
     */
    private void setCameraToResponsePosition(List<CarmenFeature> results) {
        if (results.size() > 0) {
            cameraAnimator.animateCameraToNewPosition(currentLatLng,11, 1500);
        } else Toast.makeText(mainActivity, R.string.no_results, Toast.LENGTH_SHORT).show();
    }
}
