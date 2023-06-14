package com.isc.hermes.controller;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import com.isc.hermes.R;
import com.isc.hermes.model.Searcher;
import com.isc.hermes.model.WayPoint;
import com.isc.hermes.utils.PlacesAdapter;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The SearcherController class is responsible for managing the search functionality
 * and updating the search results UI.
 */
public class SearcherController {
    private final Searcher searcher;
    private final PlacesAdapter adapter;
    private final ExecutorService executorService;

    public SearcherController(Searcher searcher, PlacesAdapter adapter) {
        this.searcher = searcher;
        this.adapter = adapter;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public SearchView.OnQueryTextListener getOnQueryTextListener(Activity activity) {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                executorService.execute(() -> {
                    List<WayPoint> newWayPoints = searcher.getSearcherSuggestionsPlacesInfo(newText);
                    activity.runOnUiThread(() -> {
                        adapter.clearWayPoints();
                        for (WayPoint wayPoint : newWayPoints) {
                            adapter.addWayPoint(wayPoint);
                        }
                    });
                });
                return false;
            }
        };
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
