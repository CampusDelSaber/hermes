package com.isc.hermes.controller;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.isc.hermes.R;
import com.isc.hermes.SpacingItemDecoration;
import com.isc.hermes.model.CategoryFilter;
import com.isc.hermes.model.Searcher;
import com.isc.hermes.utils.CategoryFilterAdapter;
import com.isc.hermes.utils.CategoryFilterClickListener;
import com.isc.hermes.utils.MarkerManager;
import com.isc.hermes.utils.PlaceByTypeSearch;
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

    private LinearLayout tagsContainer;
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
        markerManager = MarkerManager.getInstance(activity);
        createItemsUI();
        addTags();
    }

    /**
     * Creates the UI elements for the controller.
     */
    private void createItemsUI() {
        tagsContainer = activity.findViewById(R.id.tagsContainer);
        mapView = activity.findViewById(R.id.mapView);
        recyclerViewConfig();
        adapterConfiguration();
    }

    private void recyclerViewConfig() {
        locationCategoriesRecyclerView = activity.findViewById(R.id.recyclerView);
        locationCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        addSpaceBetweenCategories();
    }

    private void addSpaceBetweenCategories() {
        int spacingInPixels = activity.getResources().getDimensionPixelSize(R.dimen.spacing);
        locationCategoriesRecyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
    }

    private void adapterConfiguration() {
        List<CategoryFilter> categories = generateLocationCategories();
        locationCategoryAdapter = new CategoryFilterAdapter(categories, this);
        locationCategoriesRecyclerView.setAdapter(locationCategoryAdapter);
    }

    private List<CategoryFilter> generateLocationCategories() {
        List<CategoryFilter> categories = new ArrayList<>();

        // Example categories
        categories.add(new CategoryFilter(R.drawable.ic_pin, "Restaurant"));
        categories.add(new CategoryFilter(R.drawable.ic_pin, "Restaurant"));
        categories.add(new CategoryFilter(R.drawable.ic_pin, "Restaurant"));
        categories.add(new CategoryFilter(R.drawable.ic_pin, "Restaurant"));
        categories.add(new CategoryFilter(R.drawable.ic_pin, "Restaurant"));
        categories.add(new CategoryFilter(R.drawable.ic_pin, "Restaurant"));
        categories.add(new CategoryFilter(R.drawable.ic_pin, "Restaurant"));
        categories.add(new CategoryFilter(R.drawable.ic_pin, "Restaurant"));
        return categories;
    }

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

    /**
     * Adds a tag to the UI and sets up the click listener.
     *
     * @param tag the tag to add
     */
    private void addTag(final String tag) {
        TextView tagTextView = new TextView(activity);
        tagTextView.setText(tag);
        tagTextView.setPadding(10, 5, 10, 5);
        tagTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPlacesByTag(tag);
            }
        });

        tagsContainer.addView(tagTextView);
    }

    /**
     * Adds tags to the UI based on the available PlacesType values.
     */
    private void addTags(){
        for (PlacesType tag: PlacesType.values()) {
            addTag(tag.getDisplayName());
        }
    }
}
