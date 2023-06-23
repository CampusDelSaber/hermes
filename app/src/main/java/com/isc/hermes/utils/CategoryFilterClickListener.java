package com.isc.hermes.utils;

import com.isc.hermes.model.CategoryFilter;

/**
 * Interface for listening to category filter clicks.
 */
public interface CategoryFilterClickListener {
    /**
     * Called when a category filter is clicked.
     * @param categoryFilter The category filter that was clicked.
     */
    void onLocationCategoryClick(CategoryFilter categoryFilter);
}
