package com.isc.hermes.utils;

import android.app.Activity;

import com.isc.hermes.model.WayPoint;

import java.util.List;

public class SearcherAdapterUpdater {
    private final Activity activity;
    private final SearcherAdapter adapter;

    public SearcherAdapterUpdater(Activity activity, SearcherAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void updateWayPoints(List<WayPoint> newWayPoints) {
        activity.runOnUiThread(() -> {
            adapter.clearWayPoints();
            for (WayPoint wayPoint : newWayPoints) {
                adapter.addWayPoint(wayPoint);
            }
        });
    }
}
