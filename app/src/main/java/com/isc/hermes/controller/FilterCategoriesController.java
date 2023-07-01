package com.isc.hermes.controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isc.hermes.R;
import com.isc.hermes.SpacingItemDecoration;
import com.isc.hermes.model.CategoryFilter;
import com.isc.hermes.utils.CategoryFilterAdapter;
import com.isc.hermes.utils.CategoryFilterClickListener;
import com.isc.hermes.utils.AndroidServicesVerification;
import com.isc.hermes.utils.MapManager;
import com.isc.hermes.utils.MarkerManager;
import com.isc.hermes.utils.PlacesType;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Controller class for filtering categories and displaying places on the map.
 */
public class FilterCategoriesController implements CategoryFilterClickListener {
    private MarkerManager markerManager;
    private MapView mapView;
    private AppCompatActivity activity;
    private RecyclerView locationCategoriesRecyclerView;
    private CategoryFilterAdapter locationCategoryAdapter;
    private String lastClickedCategory = "";
    private boolean isMarkersShown = false;
    private AndroidServicesVerification androidServicesVerification;

    /**
     * Constructs a new FilterCategoriesController with the specified activity.
     *
     * @param activity the activity associated with the controller
     */
    public FilterCategoriesController(AppCompatActivity activity) {
        this.activity = activity;
        androidServicesVerification = new AndroidServicesVerification();
    }

    /**
     * Generates the categories locations
     */
    public void initComponents() {
        markerManager = MarkerManager.getInstance(activity);
        createItemsUI();
    }

    /**
     * Creates the UI elements for the controller.
     */
    private void createItemsUI() {
        mapView = activity.findViewById(R.id.mapView);
        recyclerViewConfig();
        adapterConfiguration();
    }

    /**
     * Configures the recycler view.
     */
    private void recyclerViewConfig() {
        locationCategoriesRecyclerView = activity.findViewById(R.id.recyclerView);
        locationCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        addSpaceBetweenCategories();
    }

    /**
     * Adds space between the categories in the recycler view.
     */
    private void addSpaceBetweenCategories() {
        int spacingInPixels = activity.getResources().getDimensionPixelSize(R.dimen.spacing);
        locationCategoriesRecyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
    }

    /**
     * Configures the adapter for the recycler view.
     */
    private void adapterConfiguration() {
        List<CategoryFilter> categories = generateLocationCategories();
        locationCategoryAdapter = new CategoryFilterAdapter(categories, this);
        locationCategoriesRecyclerView.setAdapter(locationCategoryAdapter);
    }

    /**
     * Generates the categories locations
     *
     * @return the list of categories
     */
    private List<CategoryFilter> generateLocationCategories() {
        List<CategoryFilter> categories = new ArrayList<>();

        for (PlacesType place : PlacesType.values()) {
            categories.add(new CategoryFilter(place.getImageResource(), place.getDisplayName()));
        }

        return categories;
    }

    /**
     * Shows the places on the map.
     *
     * @param locationCategory the category of the places to show
     */
    @Override
    public void onLocationCategoryClick(CategoryFilter locationCategory) {
        if (androidServicesVerification.isInternetEnabled(activity)) {
            LatLng center = getLocationEnable();
            addAndRemoveMarkets(locationCategory, center);
        } else {
            showInternetDialog();
        }
    }

    /**
     * Shows or removes the places on the map.
     *
     * @param locationCategory the category of the places to show
     * @param center           the center of the map
     */
    private void addAndRemoveMarkets(CategoryFilter locationCategory, LatLng center) {
        if (isMarkersShown && lastClickedCategory.equals(locationCategory.getNameCategory())) {
            markerManager.removeAllMarkers(mapView);
            isMarkersShown = false;
        } else {
            if (isMarkersShown) {
                markerManager.removeAllMarkers(mapView);
            }
            searchPlacesByTag(locationCategory.getNameCategory(), center.getLatitude(), center.getLongitude());
            isMarkersShown = true;
            lastClickedCategory = locationCategory.getNameCategory();
        }
    }

    /**
     * Manages location of user.
     *
     * @return location of the user.
     */
    private LatLng getLocationEnable() {
        LatLng center;
        if (androidServicesVerification.isLocationEnabled(activity)) {
            center = new LatLng(
                    CurrentLocationController.getControllerInstance(activity).getCurrentLocationModel().getLatitude(),
                    CurrentLocationController.getControllerInstance(activity).getCurrentLocationModel().getLongitude()
            );
        } else center = MapManager.getInstance().getMapboxMap().getCameraPosition().target;
        return center;
    }

    /**
     * Searches for places based on the specified tag.
     *
     * @param tag the tag to search for
     */
    private void searchPlacesByTag(String tag, double latitude, double longitude) {
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(activity.getString(R.string.access_token))
                .query(tag)
                .geocodingTypes(GeocodingCriteria.TYPE_POI)
                .proximity(Point.fromLngLat(longitude, latitude))
                .limit(10)
                .build();

        mapboxGeocoding.enqueueCall(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GeocodingResponse> call, @NonNull Response<GeocodingResponse> response) {
                marketManager(response);
            }

            @Override
            public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    /**
     * Displays the retrieved places on the map.
     *
     * @param response the response from the API
     */
    private void marketManager(Response<GeocodingResponse> response) {
        if (response.body() != null) {
            List<CarmenFeature> results = response.body().features();
            if (results.size() > 0) {
                markerManager.removeAllMarkers(mapView);
                for (CarmenFeature feature : results) {
                    Point firstResultPoint = (Point) feature.geometry();
                    assert firstResultPoint != null;
                    markerManager.addMarkerToMap(mapView, feature.placeName(), firstResultPoint.latitude(), firstResultPoint.longitude(), true);
                }
            } else {
                Toast.makeText(activity, R.string.no_results_founds, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showInternetDialog() {
        new AlertDialog.Builder(activity)
                .setTitle("Internet Connection Needed")
                .setMessage("You need internet access for this function. Do you want to enable it?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    activity.startActivity(intent);
                })
                .setNegativeButton("No", null)
                .show();
    }
}
