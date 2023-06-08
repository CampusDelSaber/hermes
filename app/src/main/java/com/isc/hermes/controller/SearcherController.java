package com.isc.hermes.controller;

import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.MainActivity;
import com.isc.hermes.R;
import com.isc.hermes.model.Searcher;


public class SearcherController {
    private ScrollView resultsContainer;
    private LinearLayout searchResultsLayout;
    private Searcher searcher;

    public SearcherController(Searcher searcherModel, ScrollView resultsContainer, LinearLayout searchResultsLayout) {
        this.searcher = searcherModel;
        this.resultsContainer = resultsContainer;
        this.searchResultsLayout = searchResultsLayout;
    }

/*    public SearcherController(Searcher searcher) {
        this.searcher = searcher;
        this.resultsContainer = findViewById(R.id.searchResults);
        this.searchResultsLayout = findViewById(R.id.searchResultsLayout);
    }*/

    public void manageResultsContainerBehaviour() {
        addSearchResult("Result 1");
        addSearchResult("Result 2");
        addSearchResult("Result 3");
    }

    private void addSearchResult(String result) {
        //TextView textView = new TextView(this.resultsContainer);
        TextView textView = new TextView(resultsContainer.getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        textView.setText(result);

        searchResultsLayout.addView(textView);
    }
}
