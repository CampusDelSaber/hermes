package com.isc.hermes.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isc.hermes.R;
import com.isc.hermes.model.CategoryFilter;

import java.util.List;

/**
 * The CategoryFilterAdapter class is responsible for managing the category filter functionality
 */
public class CategoryFilterAdapter extends RecyclerView.Adapter<CategoryFilterViewHolder> {
    private List<CategoryFilter> categories;
    private CategoryFilterClickListener categoryFilterClickListener;

    /**
     * Constructs a new CategoryFilterAdapter.
     * @param categories the categories that will be used to filter the data.
     * @param listener the listener that will be used to listen for category filter clicks.
     */
    public CategoryFilterAdapter(List<CategoryFilter> categories, CategoryFilterClickListener listener) {
        this.categories = categories;
        this.categoryFilterClickListener = listener;
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public CategoryFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_category, parent, false);
        return new CategoryFilterViewHolder(view, categoryFilterClickListener);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CategoryFilterViewHolder holder, int position) {
        CategoryFilter category = categories.get(position);
        holder.bind(category);
    }

    /**
     * Returns the total number of categories in the data set held by the adapter.
     * @return the total number of categories in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }
}
