package com.isc.hermes.controller;

import android.widget.SearchView;
import android.widget.Toast;
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

    /**
     * Constructs a new SearcherController.
     *
     * @param searcher the searcher that will be used to search for data.
     * @param adapterUpdater the adapterUpdater that will be used to update the adapter with new data.
     */
    public SearcherController(Searcher searcher, SearcherAdapterUpdater adapterUpdater) {
        this.searcher = searcher;
        this.adapterUpdater = adapterUpdater;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Returns a listener for text changes in the search view.
     * When the text changes, it triggers a search and updates the adapter with the new results.
     *
     * @return a listener for text changes in the search view.
     */
    public SearchView.OnQueryTextListener getOnQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(searcher.getSearcherSuggestionsPlacesInfo(query).isEmpty()){
                    Toast.makeText(searcher.getContext(), "No results found", Toast.LENGTH_SHORT).show();
                }
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

    /**
     * Shuts down the executor service.
     * This should be called when the controller is no longer needed, to free up resources.
     */
    public void shutdown() {
        executorService.shutdown();
    }
}
