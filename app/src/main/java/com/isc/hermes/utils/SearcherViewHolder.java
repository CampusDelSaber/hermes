package com.isc.hermes.utils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isc.hermes.R;
import com.isc.hermes.model.WayPoint;

import java.util.Locale;

/**
 * ViewHolder class for the SearcherAdapter. It holds and binds the view elements of a single WayPoint in the RecyclerView.
 */
public class SearcherViewHolder extends RecyclerView.ViewHolder {
    private TextView placeName;
    private TextView placeLocation;
    private WayPointClickListener wayPointClickListener;

    /**
     * Constructor of SearcherViewHolder.
     *
     * @param itemView the view of a single item in the RecyclerView.
     * @param wayPointClickListener a listener to handle item click events.
     */
    public SearcherViewHolder(@NonNull View itemView, WayPointClickListener wayPointClickListener) {
        super(itemView);
        this.wayPointClickListener = wayPointClickListener;
        placeName = itemView.findViewById(R.id.placeName);
        placeLocation = itemView.findViewById(R.id.placeLocation);
    }

    /**
     * Binds the WayPoint data to the views in the ViewHolder.
     * This method sets the text views with the WayPoint data and sets an OnClickListener to notify the WayPointClickListener.
     *
     * @param wayPoint the WayPoint object whose data will be displayed in the ViewHolder.
     */
    public void bind(final WayPoint wayPoint) {
        placeName.setText(wayPoint.getPlaceName());
        placeLocation.setText(String.format(Locale.getDefault(), "%.2f, %.2f",
                wayPoint.getLatitude(), wayPoint.getLongitude()));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wayPointClickListener.onItemClick(wayPoint);
            }
        });
    }
}