package com.isc.hermes.utils;

import com.isc.hermes.model.WayPoint;

/**
 * Interface for handling click events on a WayPoint item in the RecyclerView.
 */
public interface WayPointClickListener {

    /**
     * Callback method to be invoked when a WayPoint item has been clicked.
     *
     * @param wayPoint the WayPoint item that was clicked.
     */
    void onItemClick(WayPoint wayPoint);
}
