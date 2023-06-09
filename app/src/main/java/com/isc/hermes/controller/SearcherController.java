package com.isc.hermes.controller;

import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.isc.hermes.R;
import com.isc.hermes.model.searcher.Searcher;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import java.util.List;
import java.util.Objects;

/**
 * The SearcherController class is responsible for managing the search functionality
 * and updating the search results UI.
 */
public class SearcherController {
    private static final long SEARCH_DELAY_MS = 100; // Throttling delay in milliseconds

    private ScrollView resultsContainer;
    private LinearLayout searchResultsLayout;
    private SearchView searchView;
    private final Searcher searcher;
    private Handler searchHandler;
    private Runnable searchRunnable;

    /**
     * Constructs a new SearcherController.
     *
     * @param searcherModel    The Searcher model used for performing the search.
     * @param resultsContainer The ScrollView that contains the search results UI.
     * @param searchView       The SearchView widget for inputting search queries.
     */
    public SearcherController(Searcher searcherModel, ScrollView resultsContainer, SearchView searchView) {
        this.searcher = searcherModel;
        resultsContainer.setVisibility(View.INVISIBLE);
        this.resultsContainer = resultsContainer;
        this.searchResultsLayout = resultsContainer.findViewById(R.id.searchResultsLayout);
        this.searchView = searchView;
        this.searchHandler = new Handler();
        this.searchRunnable = null;
    }

    /**
     * Runs the searcher and sets up the search functionality.
     */
    public void runSearcher() {
        manageTextFieldSearcherBehaviour();
    }

    /**
     * Manages the behavior of the search field.
     */
    private void manageTextFieldSearcherBehaviour() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Add a point on the map to start the navigation
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (!newText.isEmpty()) {
                            resultsContainer.setVisibility(View.VISIBLE);
                            searchResultsLayout.removeAllViews();
                            performSearch(newText);
                        } else {
                            searchResultsLayout.removeAllViews();
                            resultsContainer.setVisibility(View.INVISIBLE);
                        }
                    }
                };
                searchHandler.postDelayed(searchRunnable, SEARCH_DELAY_MS);

                return true;
            }
        });
    }

    /**
     * Performs the search using the provided query.
     *
     * @param query The search query.
     */
    private void performSearch(String query) {
        searchResultsLayout.removeAllViews();

        new AsyncTask<Void, Void, List<CarmenFeature>>() {
            @Override
            protected List<CarmenFeature> doInBackground(Void... voids) {
                return searcher.getSuggestionsFeatures(query);
            }

            @Override
            protected void onPostExecute(List<CarmenFeature> carmenFeatures) {
                addSearchResults(carmenFeatures);
            }
        }.execute();
    }

    /**
     * Adds the search results to the search results layout.
     *
     * @param results The list of search results.
     */
    private void addSearchResults(List<CarmenFeature> results) {
        for (CarmenFeature feature : results) {
            addSearchResult(Objects.requireNonNull(feature.placeName()));
        }
    }

    /**
     * Adds a single search result to the search results layout.
     *
     * @param result The search result to add.
     */
    private void addSearchResult(String result) {
        TextView textView = new TextView(resultsContainer.getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        textView.setText("- " + result);

        searchResultsLayout.addView(textView);
    }
}
