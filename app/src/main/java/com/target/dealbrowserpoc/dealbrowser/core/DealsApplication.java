package com.target.dealbrowserpoc.dealbrowser.core;

import android.app.Application;

import com.target.dealbrowserpoc.dealbrowser.api.ApiLink;
import com.target.dealbrowserpoc.dealbrowser.api.ApiLinkFactory;

import io.realm.Realm;

public class DealsApplication extends Application {
    private static final String ROOT_URL = "http://target-deals.herokuapp.com/";
    private static DealsApplication instance;

    private ApiLink apiLink;

    public static DealsApplication getInstance() {
        return instance;
    }

    public ApiLink getApiLink() {
        return apiLink;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Some setup stuff
        Realm.init(getBaseContext());
        instance = this;
        apiLink = new ApiLinkFactory(ROOT_URL).getInstance();
    }
}
