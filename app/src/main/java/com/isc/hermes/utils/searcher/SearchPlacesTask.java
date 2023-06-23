package com.isc.hermes.utils.searcher;

import android.os.AsyncTask;

import com.isc.hermes.utils.PlaceByTypeSearch;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * AsyncTask for performing the place search in the background.
 */
public class SearchPlacesTask extends AsyncTask<String, Void, List<CarmenFeature>> {
    private static final String MAPBOX_ACCESS_TOKEN = "sk.eyJ1IjoiaGVybWVzLW1hcHMiLCJhIjoiY2xpamxmbnQxMDg2aDNybGc0YmUzcHloaCJ9.__1WydgkE41IAuYtsob0jA";

    private SearchPlacesListener listener;

    /**
     * This is the constructor method for instance this class
     *
     * @param listener for execute the searching
     */
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

    /**
     * Performs the geocoding request to retrieve places of the specified type.
     *
     * @param placeType the type of places to search for
     * @param latitude  the latitude of the search location
     * @param longitude the longitude of the search location
     * @return a list of places matching the specified type
     * @throws IOException if an error occurs during the geocoding request
     */
    private static List<CarmenFeature> performGeocodingRequest(String placeType, double latitude, double longitude) throws IOException {
        MapboxGeocoding geocoding = MapboxGeocoding.builder()
                .accessToken(MAPBOX_ACCESS_TOKEN)
                .query(placeType)
                .proximity(Point.fromLngLat(longitude, latitude))
                .limit(8)
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

