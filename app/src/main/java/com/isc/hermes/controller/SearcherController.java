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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SearcherController {
    private static final long SEARCH_DELAY_MS = 100; // Throttling delay in milliseconds

    private ScrollView resultsContainer;
    private LinearLayout searchResultsLayout;
    private SearchView searchView;
    private final Searcher searcher;
    private Handler searchHandler;
    private Runnable searchRunnable;

    public SearcherController(Searcher searcherModel, ScrollView resultsContainer, SearchView searchView) {
        this.searcher = searcherModel;
        resultsContainer.setVisibility(View.INVISIBLE);
        this.resultsContainer = resultsContainer;
        this.searchResultsLayout = resultsContainer.findViewById(R.id.searchResultsLayout);
        this.searchView = searchView;
        this.searchHandler = new Handler();
        this.searchRunnable = null;
    }

    public void runSearcher() {
        manageTextFieldSearcherBehaviour();
    }

    private void manageTextFieldSearcherBehaviour() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search submission if needed
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Cancel any pending search requests
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                // Schedule a new search request with throttling
                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if(!newText.isEmpty()){
                            resultsContainer.setVisibility(View.VISIBLE);
                            searchResultsLayout.removeAllViews();
                            performSearch(newText);
                        }
                        else {
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

    private void performSearch(String query) {
        // Clear previous search results
        searchResultsLayout.removeAllViews();
//        addSearchResults(searcher.getSuggestionsFeatures(query));
        // Make the API call asynchronously
        new AsyncTask<Void, Void, List<CarmenFeature>>() {
            @Override
            protected List<CarmenFeature> doInBackground(Void... voids) {
                return searcher.getSuggestionsFeatures(query);
            }

            @Override
            protected void onPostExecute(List<CarmenFeature> carmenFeatures) {
                // Update the search results UI
//                searchResultsLayout.removeAllViews();
                addSearchResults(carmenFeatures);
            }
        }.execute();
    }

    private void addSearchResults(List<CarmenFeature> results) {
//        addSearchResult("\n");
        for (CarmenFeature feature : results) {
            addSearchResult(Objects.requireNonNull(feature.placeName()));
        }
    }

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
