package com.isc.hermes.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isc.hermes.R;
import com.isc.hermes.model.CategoryFilter;

/**
 * The CategoryFilterViewHolder class is responsible for managing the category filter view holder
 */
public class CategoryFilterViewHolder extends RecyclerView.ViewHolder{
    private ImageView locationImage;
    private TextView locationText;
    private CategoryFilterClickListener categoryFilterClickListener;

    /**
     * Constructs a new CategoryFilterViewHolder.
     * @param itemView The view that will be used to display the data.
     * @param listener The listener that will be used to listen for category filter clicks.
     */
    public CategoryFilterViewHolder(@NonNull View itemView, CategoryFilterClickListener listener) {
        super(itemView);
        locationImage = itemView.findViewById(R.id.locationImage);
        locationText = itemView.findViewById(R.id.locationText);
        this.categoryFilterClickListener = listener;
    }

    /**
     * Binds the category filter data to the view.
     * @param category The category filter that will be used to bind the data.
     */
    public void bind(CategoryFilter category) {
        locationImage.setImageResource(category.getImageCategory());
        locationText.setText(category.getNameCategory());
        itemView.setOnClickListener(v -> categoryFilterClickListener.onLocationCategoryClick(category));
    }
}
