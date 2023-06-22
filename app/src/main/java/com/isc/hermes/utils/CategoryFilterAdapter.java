package com.isc.hermes.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isc.hermes.R;
import com.isc.hermes.model.CategoryFilter;

import java.util.List;

public class CategoryFilterAdapter extends RecyclerView.Adapter<CategoryFilterViewHolder> {

    private List<CategoryFilter> categories;
    private CategoryFilterClickListener categoryFilterClickListener;

    public CategoryFilterAdapter(List<CategoryFilter> categories, CategoryFilterClickListener listener) {
        this.categories = categories;
        this.categoryFilterClickListener = listener;
    }

    @NonNull
    @Override
    public CategoryFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_category, parent, false);
        return new CategoryFilterViewHolder(view, categoryFilterClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryFilterViewHolder holder, int position) {
        CategoryFilter category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }
}
