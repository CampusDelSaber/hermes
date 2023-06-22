package com.isc.hermes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationCategoryAdapter extends RecyclerView.Adapter<LocationCategoryAdapter.ViewHolder> {

    private List<LocationCategory> categories;

    public LocationCategoryAdapter(List<LocationCategory> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationCategory category = categories.get(position);
        holder.locationImage.setImageResource(category.image);
        holder.locationText.setText(category.name);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView locationImage;
        TextView locationText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationImage = itemView.findViewById(R.id.locationImage);
            locationText = itemView.findViewById(R.id.locationText);
        }
    }
}
