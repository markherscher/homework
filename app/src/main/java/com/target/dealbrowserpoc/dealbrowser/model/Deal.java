package com.target.dealbrowserpoc.dealbrowser.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Deal extends RealmObject {
    public final static String GUID = "guid";
    public final static String TITLE = "title";
    public final static String DESCRIPTION = "description";
    public final static String IMAGE_URL = "imageUrl";
    public final static String SALES_PRICE = "salePrice";
    public final static String PRICE = "price";
    public final static String AISLEE = "aisle";
    public final static String ORDER = "order";

    @PrimaryKey
    private String guid;
    private String title;
    private String description;
    private String imageUrl;
    private String salePrice;
    private String price;
    private String aisle;
    private int order;

    public Deal() {
        // Empty constructor required by Realm
    }

    public Deal(@NonNull String guid,
                @Nullable String title,
                @Nullable String description,
                @Nullable String imageUrl,
                @Nullable String salePrice,
                @Nullable String price,
                @Nullable String aisle,
                int order) {
        this.guid = guid;
        this.title = Objects.toString(title, "");
        this.description = Objects.toString(description, "");
        this.imageUrl = Objects.toString(imageUrl, "");
        this.salePrice = Objects.toString(salePrice, "");
        this.price = Objects.toString(price, "");
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

    @NonNull
    public String getSalePrice() {
        return salePrice;
    }

    @NonNull
    public String getPrice() {
        return price;
    }

    @NonNull
    public String getAisle() {
        return aisle;
    }

    @NonNull
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
                    TextUtils.equals(salePrice, other.salePrice) &&
                    TextUtils.equals(price, other.price) &&
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
