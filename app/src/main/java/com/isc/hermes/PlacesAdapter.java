package com.isc.hermes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isc.hermes.model.Place;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private List<Place> places;

    public PlacesAdapter(List<Place> places) {
        this.places = places;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = places.get(position);
        holder.placeName.setText(place.getPlaceName());
        holder.placeLocation.setText(place.getPlaceLocation());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView placeName;
        TextView placeLocation;

        PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.placeName);
            placeLocation = itemView.findViewById(R.id.placeLocation);
        }
    }
}
