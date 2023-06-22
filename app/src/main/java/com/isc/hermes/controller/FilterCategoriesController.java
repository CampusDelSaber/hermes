package com.isc.hermes.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.R;
import com.isc.hermes.model.Searcher;
import com.isc.hermes.utils.MarkerManager;
import com.isc.hermes.utils.PlaceSearch;
import com.isc.hermes.utils.PlacesType;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.api.geocoding.v5.models.*;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.maps.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterCategoriesController {

    private LinearLayout tagsContainer;
    private MarkerManager markerManager;
    private MapView mapView;
    private Activity activity;


    public FilterCategoriesController(Activity activity) {
        this.activity = activity;
        markerManager = MarkerManager.getInstance(activity);
        createItemsUI();
        addTags();
    }

    private void createItemsUI() {
        tagsContainer = activity.findViewById(R.id.tagsContainer);
        mapView = activity.findViewById(R.id.mapView);
    }

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
                    placeSearch.searchPlacesByType(tag, new PlaceSearch.SearchPlacesListener() {
                        @Override
                        public void onSearchComplete(List<CarmenFeature> places) {
                            showPlacesOnMap(places);
                        }

                        @Override
                        public void onSearchError(String errorMessage) {

                        }
                    });

                    // Handle the retrieved places
//                    showPlacesOnMap(places);
                } else {
                    // Handle API error
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                // Handle network failure
            }
        });
    }

    private void showPlacesOnMap(List<CarmenFeature> places) {
        // Iterate over the list of places and add them to the map
        for (CarmenFeature place : places) {
            // Extract the necessary information from the place object
            String name = place.text();
            double latitude = ((Point) place.geometry()).latitude();
            double longitude = ((Point) place.geometry()).longitude();

            // Add a marker or perform any other action on the map
            // using the extracted place information

            markerManager.addMarkerToMap(mapView, name, latitude, longitude, true);
            System.out.println(name);
        }
    }

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

    private void addTags(){
        for (PlacesType tag: PlacesType.values()) {
            addTag(tag.getDisplayName());
        }
    }


}