package com.target.dealbrowserpoc.dealbrowser.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.target.dealbrowserpoc.dealbrowser.R;

public class SearchTextView extends FrameLayout {
    public SearchTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_search_text, this);
    }
}
