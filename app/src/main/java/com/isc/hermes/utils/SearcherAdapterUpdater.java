package com.isc.hermes.utils;

import android.app.Activity;

import com.isc.hermes.model.WayPoint;

import java.util.List;

/**
 * A helper class to update the adapter data of a SearcherAdapter from a UI thread.
 */
public class SearcherAdapterUpdater {
    private final Activity activity;
    private final SearcherAdapter adapter;

    /**
     * Constructor of SearcherAdapterUpdater.
     *
     * @param activity the activity in which UI thread the adapter will be updated.
     * @param adapter the SearcherAdapter whose data is to be updated.
     */
    public SearcherAdapterUpdater(Activity activity, SearcherAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    /**
     * Clears the current list of WayPoints and adds new WayPoints to the adapter in the UI thread.
     *
     * @param newWayPoints the new list of WayPoints to display in the adapter.
     */
    public void updateWayPoints(List<WayPoint> newWayPoints) {
        activity.runOnUiThread(() -> {
            adapter.clearWayPoints();
            for (WayPoint wayPoint : newWayPoints) {
                adapter.addWayPoint(wayPoint);
            }
        });
    }
}