package com.isc.hermes;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Item decoration for adding spacing between items in a RecyclerView.
 */
public class SpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;

    /**
     * Constructs a new SpacingItemDecoration instance with the specified spacing.
     *
     * @param spacing the spacing to be added between items
     */
    public SpacingItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    /**
     * Sets the spacing offsets for items in the RecyclerView.
     *
     * @param outRect the output rectangle that holds the spacing offsets
     * @param view    the child view being positioned
     * @param parent  the RecyclerView instance
     * @param state   the current RecyclerView state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = spacing;
        }

        outRect.right = spacing;
    }
}
