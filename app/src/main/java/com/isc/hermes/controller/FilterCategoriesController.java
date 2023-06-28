package com.isc.hermes.controller;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.isc.hermes.R;
import com.isc.hermes.SpacingItemDecoration;
import com.isc.hermes.model.CategoryFilter;
import com.isc.hermes.model.Searcher;
import com.isc.hermes.utils.CategoryFilterAdapter;
import com.isc.hermes.utils.CategoryFilterClickListener;
import com.isc.hermes.utils.MarkerManager;
import com.isc.hermes.utils.PlacesType;
import com.isc.hermes.utils.searcher.SearchPlacesListener;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
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
    private Activity activity;
    private RecyclerView locationCategoriesRecyclerView;
    private CategoryFilterAdapter locationCategoryAdapter;

    /**
     * Constructs a new FilterCategoriesController with the specified activity.
     *
     * @param activity the activity associated with the controller
     */
    public FilterCategoriesController(Activity activity) {
        this.activity = activity;
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
     * @param locationCategory the category of the places to show
     */
    @Override
    public void onLocationCategoryClick(CategoryFilter locationCategory) {
        searchPlacesByTag(locationCategory.getNameCategory());
    }

    /**
     * Searches for places based on the specified tag.
     *
     * @param tag the tag to search for
     */
    private void searchPlacesByTag(String tag) {
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(activity.getString(R.string.access_token))
                .query(tag)
                .geocodingTypes(GeocodingCriteria.TYPE_POI)
                .build();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                if (response.isSuccessful()) {
                    List<CarmenFeature> places = null;
                    Searcher placeSearch = new Searcher();
                    placeSearch.searchPlacesByType(tag, new SearchPlacesListener() {
                        @Override
                        public void onSearchComplete(List<CarmenFeature> places) {
                            markerManager.removeAllMarkers(mapView);
                            showPlacesOnMap(places);
                        }

                        @Override
                        public void onSearchError(String errorMessage) {
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    /**
     * Displays the retrieved places on the map.
     *
     * @param places the list of places to display
     */
    private void showPlacesOnMap(List<CarmenFeature> places) {
        for (CarmenFeature place : places) {
            String name = place.text();
            double latitude = ((Point) place.geometry()).latitude();
            double longitude = ((Point) place.geometry()).longitude();

            markerManager.addMarkerToMap(mapView, name, latitude, longitude, true);
        }
    }
}
