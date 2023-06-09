package com.isc.hermes.controller;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import com.isc.hermes.R;
import com.isc.hermes.model.Searcher;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import java.util.List;
import java.util.Objects;


public class SearcherController {
    private ScrollView resultsContainer;
    private LinearLayout searchResultsLayout;
    private SearchView searchView;
    private final Searcher searcher;

    public SearcherController(Searcher searcherModel, ScrollView resultsContainer, SearchView searchView) {
        this.searcher = searcherModel;
        this.resultsContainer = resultsContainer;
        this.searchResultsLayout = resultsContainer.findViewById(R.id.searchResultsLayout);
        this.searchView = searchView;
    }

    public void manageResultsContainerBehaviour(String query) {
        addSearchResult("\n");
        List<CarmenFeature> carmenFeatures = searcher.getSuggestionsFeatures(query);
        for (CarmenFeature feature: carmenFeatures) {
            addSearchResult(Objects.requireNonNull(feature.placeName()));
        }
    }

    private void addSearchResult(String result) {
        TextView textView = new TextView(resultsContainer.getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        textView.setText((result.isEmpty())? result : "- "+result);

        searchResultsLayout.addView(textView);
    }

    private void manageTextFieldSearcherBehaviour() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //manageResultsContainerBehaviour(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchResultsLayout.removeAllViews();
                manageResultsContainerBehaviour(newText);
                // Handle text changes or suggestions if needed
                return false;
            }
        });
    }

    public void runSearcher() {
        manageTextFieldSearcherBehaviour();
    }
}
