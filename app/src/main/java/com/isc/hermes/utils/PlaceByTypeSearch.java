package com.isc.hermes.utils;

import android.app.Activity;
import android.os.AsyncTask;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Utility class for searching places by type using Mapbox Geocoding API.
 */
public class PlaceByTypeSearch {
    private static final String MAPBOX_ACCESS_TOKEN = "sk.eyJ1IjoiaGVybWVzLW1hcHMiLCJhIjoiY2xpamxmbnQxMDg2aDNybGc0YmUzcHloaCJ9.__1WydgkE41IAuYtsob0jA";
    private Activity activity;

    /**
     * Constructs a new PlaceByTypeSearch instance.
     */
    public PlaceByTypeSearch() {
    }

    /**
     * Listener interface for handling the search results.
     */
    public interface SearchPlacesListener {
        /**
         * Called when the place search is complete and returns a list of places matching the specified type.
         *
         * @param places the list of places matching the specified type
         */
        void onSearchComplete(List<CarmenFeature> places);

        /**
         * Called when an error occurs during the place search.
         *
         * @param errorMessage the error message describing the search error
         */
        void onSearchError(String errorMessage);
    }

    /**
     * Searches for places of the specified type near the given latitude and longitude coordinates.
     *
     * @param placeType the type of places to search for
     * @param latitude  the latitude of the search location
     * @param longitude the longitude of the search location
     * @param listener  the listener to handle the search results
     */
    public void searchPlacesByType(String placeType, double latitude, double longitude, SearchPlacesListener listener) {
        new SearchPlacesTask(listener).execute(placeType, String.valueOf(latitude), String.valueOf(longitude));
    }

    /**
     * AsyncTask for performing the place search in the background.
     */
    private static class SearchPlacesTask extends AsyncTask<String, Void, List<CarmenFeature>> {
        private SearchPlacesListener listener;

        public SearchPlacesTask(SearchPlacesListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<CarmenFeature> doInBackground(String... params) {
            String placeType = params[0];
            double latitude = Double.parseDouble(params[1]);
            double longitude = Double.parseDouble(params[2]);

            try {
                return performGeocodingRequest(placeType, latitude, longitude);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<CarmenFeature> places) {
            if (places != null) {
                listener.onSearchComplete(places);
            } else {
                listener.onSearchError("An error occurred during the search.");
            }
        }
    }

    /**
     * Performs the geocoding request to retrieve places of the specified type.
     *
     * @param placeType  the type of places to search for
     * @param latitude   the latitude of the search location
     * @param longitude  the longitude of the search location
     * @return a list of places matching the specified type
     * @throws IOException if an error occurs during the geocoding request
     */
    private static List<CarmenFeature> performGeocodingRequest(String placeType, double latitude, double longitude) throws IOException {
        MapboxGeocoding geocoding = MapboxGeocoding.builder()
                .accessToken(MAPBOX_ACCESS_TOKEN)
                .query(placeType)
                .proximity(Point.fromLngLat(longitude, latitude))
                .build();

        Response<GeocodingResponse> response = geocoding.executeCall();

        if (response.isSuccessful() && response.body() != null) {
            List<CarmenFeature> results = new ArrayList<>();
            for (CarmenFeature feature : response.body().features()) {
                results.add(feature);
            }
            return results;
        } else {
            throw new IOException("An error occurred during the geocoding request.");
        }
    }
}
