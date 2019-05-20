package com.target.dealbrowserpoc.dealbrowser.adapter;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class LinearLayoutMarginDecorator extends RecyclerView.ItemDecoration {
    private final int margin;

    public LinearLayoutMarginDecorator(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = margin;
        }
    }
}
