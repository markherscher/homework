package com.target.dealbrowserpoc.dealbrowser.api;

import android.support.annotation.Nullable;

public class DealApi {
    @Nullable
    public String title;

    @Nullable
    public String description;

    @Nullable
    public String image;

    @Nullable
    public String salePrice;

    @Nullable
    public String price;

    @Nullable
    public String guid;

    @Nullable
    public String aisle;

    @Nullable
    public String _id;

    public int index;

    @Override
    public String toString() {
        return title;
    }
}
