package com.isc.hermes.utils.searcher;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import java.util.List;

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