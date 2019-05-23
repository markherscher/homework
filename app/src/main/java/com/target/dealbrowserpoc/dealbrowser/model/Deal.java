package com.target.dealbrowserpoc.dealbrowser.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Deal extends RealmObject {
    // Field name definitions, for use by Realm queries
    public final static String GUID = "guid";
    public final static String TITLE = "title";
    public final static String DESCRIPTION = "description";
    public final static String IMAGE_URL = "imageUrl";
    public final static String ACTUAL_PRICE = "actualPrice";
    public final static String REGULAR_PRICE = "regularPrice";
    public final static String AISLEE = "aisle";
    public final static String ORDER = "order";

    @PrimaryKey
    private String guid;
    private String title;
    private String description;
    private String imageUrl;
    private int actualPrice;
    private int regularPrice;
    private String aisle;
    private int order;

    public Deal() {
        // Empty constructor required by Realm
    }

    public Deal(@NonNull String guid,
                @Nullable String title,
                @Nullable String description,
                @Nullable String imageUrl,
                int actualPrice,
                int regularPrice,
                @Nullable String aisle,
                int order) {
        this.guid = guid;
        this.title = Objects.toString(title, "");
        this.description = Objects.toString(description, "");
        this.imageUrl = Objects.toString(imageUrl, "");
        this.actualPrice = actualPrice;
        this.regularPrice = regularPrice;
        this.aisle = Objects.toString(aisle, "");
        this.order = order;
    }

    @NonNull
    public String getGuid() {
        return guid;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Gets the actual price. Might be the same as regular price.
     * @return the price, in cents
     */
    public int getActualPrice() {
        return actualPrice;
    }

    /**
     * Gets the regular price. Might be the same as actual price.
     * @return the price, in cents
     */
    public int getRegularPrice() {
        return regularPrice;
    }

    @NonNull
    public String getAisle() {
        return aisle;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Deal) {
            Deal other = (Deal) o;

            return TextUtils.equals(guid, other.guid) &&
                    TextUtils.equals(title, other.title) &&
                    TextUtils.equals(description, other.description) &&
                    TextUtils.equals(imageUrl, other.imageUrl) &&
                    actualPrice == other.actualPrice &&
                    regularPrice == other.regularPrice &&
                    TextUtils.equals(aisle, other.aisle) &&
                    order == other.order;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return guid.hashCode();
    }
}
