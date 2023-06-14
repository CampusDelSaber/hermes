package com.isc.hermes.controller;

import android.widget.SearchView;

import com.isc.hermes.utils.SearcherAdapterUpdater;
import com.isc.hermes.model.Searcher;
import com.isc.hermes.model.WayPoint;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The SearcherController class is responsible for managing the search functionality
 * and updating the search results UI.
 */
public class SearcherController {
    private final Searcher searcher;
    private final SearcherAdapterUpdater adapterUpdater;
    private final ExecutorService executorService;

    public SearcherController(Searcher searcher, SearcherAdapterUpdater adapterUpdater) {
        this.searcher = searcher;
        this.adapterUpdater = adapterUpdater;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public SearchView.OnQueryTextListener getOnQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                executorService.execute(() -> {
                    List<WayPoint> newWayPoints = searcher.getSearcherSuggestionsPlacesInfo(newText);
                    adapterUpdater.updateWayPoints(newWayPoints);
                });
                return false;
            }
        };
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
