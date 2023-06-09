package com.isc.hermes.controller;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.isc.hermes.R;
import com.isc.hermes.model.Searcher;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;


public class SearcherController {
    private ScrollView resultsContainer;
    private LinearLayout searchResultsLayout;
    private final Searcher searcher;

    public SearcherController(Searcher searcherModel, ScrollView resultsContainer) {
        this.searcher = searcherModel;
        this.resultsContainer = resultsContainer;
        this.searchResultsLayout = resultsContainer.findViewById(R.id.searchResultsLayout);
    }

    public void manageResultsContainerBehaviour() {
        addSearchResult("\n");
        List<CarmenFeature> carmenFeatures = searcher.getSuggestionsFeatures("cochabamba");
        for (CarmenFeature feature: carmenFeatures) {
            addSearchResult(feature.placeName());
        }
    }

    public void updateComponentsByQueryGiven(String query) {
    }

    private void addSearchResult(String result) {
        TextView textView = new TextView(resultsContainer.getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        textView.setText(result);

        searchResultsLayout.addView(textView);
    }
}
