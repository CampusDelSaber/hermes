package com.isc.hermes.utils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isc.hermes.R;
import com.isc.hermes.model.WayPoint;

import java.util.Locale;

public class SearcherViewHolder extends RecyclerView.ViewHolder {
    TextView placeName;
    TextView placeLocation;
    WayPointClickListener wayPointClickListener;

    SearcherViewHolder(@NonNull View itemView, WayPointClickListener wayPointClickListener) {
        super(itemView);
        this.wayPointClickListener = wayPointClickListener;
        placeName = itemView.findViewById(R.id.placeName);
        placeLocation = itemView.findViewById(R.id.placeLocation);
    }
    void bind(final WayPoint wayPoint) {
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