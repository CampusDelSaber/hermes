package com.isc.hermes.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isc.hermes.R;
import com.isc.hermes.model.CategoryFilter;

public class CategoryFilterViewHolder extends RecyclerView.ViewHolder{
    private ImageView locationImage;
    private TextView locationText;
    private CategoryFilterClickListener categoryFilterClickListener;

    public CategoryFilterViewHolder(@NonNull View itemView, CategoryFilterClickListener listener) {
        super(itemView);
        locationImage = itemView.findViewById(R.id.locationImage);
        locationText = itemView.findViewById(R.id.locationText);
        this.categoryFilterClickListener = listener;
    }

    public void bind(CategoryFilter category) {
        locationImage.setImageResource(category.getImageCategory());
        locationText.setText(category.getNameCategory());
        itemView.setOnClickListener(v -> categoryFilterClickListener.onLocationCategoryClick(category));
    }
}
