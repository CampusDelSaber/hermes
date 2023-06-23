package com.isc.hermes.utils;

import android.app.Activity;
import android.os.AsyncTask;

import com.isc.hermes.utils.searcher.SearchPlacesListener;
import com.isc.hermes.utils.searcher.SearchPlacesTask;
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

    /**
     * Constructs a new PlaceByTypeSearch instance.
     */
    public PlaceByTypeSearch() {
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
}
