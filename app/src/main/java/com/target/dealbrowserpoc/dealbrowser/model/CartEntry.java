package com.target.dealbrowserpoc.dealbrowser.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CartEntry extends RealmObject {
    public static final int MAX_COUNT = 9;
    public static final String DEAL_GUID = "dealGuid";
    public static final String COUNT = "count";

    @PrimaryKey
    private String dealGuid;
    private int count;

    public CartEntry() {
        dealGuid = "";
    }

    public CartEntry(@NonNull String dealGuid, int count) {
        this.dealGuid = dealGuid;
        this.count = count;
    }

    @NonNull
    public String getDealGuid() {
        return dealGuid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = Math.min(count, MAX_COUNT);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%d %s", count, dealGuid);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CartEntry) {
            CartEntry other = (CartEntry) o;

            return TextUtils.equals(dealGuid, other.dealGuid) &&
                    count == other.count;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return dealGuid.hashCode();
    }
}
