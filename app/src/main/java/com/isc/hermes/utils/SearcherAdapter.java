package com.isc.hermes.utils;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.isc.hermes.R;
import com.isc.hermes.model.WayPoint;
import java.util.List;

/**
 * Adapter for RecyclerView to display a list of WayPoints.
 * This adapter communicates with a WayPointClickListener for handling item click events.
 */
public class SearcherAdapter extends RecyclerView.Adapter<SearcherViewHolder> {

    private List<WayPoint> wayPoints;
    private WayPointClickListener wayPointClickListener;

    /**
     * Constructor of SearcherAdapter.
     *
     * @param wayPoints the initial list of WayPoints to display.
     * @param wayPointClickListener the listener to handle item click events.
     */
    public SearcherAdapter(List<WayPoint> wayPoints, WayPointClickListener wayPointClickListener) {
        this.wayPoints = wayPoints;
        this.wayPointClickListener = wayPointClickListener;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent the ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType the view type of the new View.
     * @return a new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public SearcherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new SearcherViewHolder(view, wayPointClickListener);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder the ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position the position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull SearcherViewHolder holder, int position) {
        holder.bind(wayPoints.get(position));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return the total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return wayPoints.size();
    }

    /**
     * Adds a WayPoint to the list of items and notify any registered observers that an item has been added.
     *
     * @param wayPoint the WayPoint to add.
     */
    public void addWayPoint(WayPoint wayPoint) {
        this.wayPoints.add(wayPoint);
        notifyItemInserted(wayPoints.size() - 1);
    }

    /**
     * Clear the list of WayPoints and notify any registered observers that the data set has changed.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void clearWayPoints() {
        this.wayPoints.clear();
        notifyDataSetChanged();
    }
}

