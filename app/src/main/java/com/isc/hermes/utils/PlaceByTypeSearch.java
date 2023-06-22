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

public class PlaceByTypeSearch {
    private static final String MAPBOX_ACCESS_TOKEN = "sk.eyJ1IjoiaGVybWVzLW1hcHMiLCJhIjoiY2xpamxmbnQxMDg2aDNybGc0YmUzcHloaCJ9.__1WydgkE41IAuYtsob0jA";
    private Activity activity;

    public PlaceByTypeSearch( ){
    }

    public interface SearchPlacesListener {
        void onSearchComplete(List<CarmenFeature> places);
        void onSearchError(String errorMessage);
    }

    public void searchPlacesByType(String placeType, double latitude, double longitude, SearchPlacesListener listener) {
        new SearchPlacesTask(listener).execute(placeType, String.valueOf(latitude), String.valueOf(longitude));
    }

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
