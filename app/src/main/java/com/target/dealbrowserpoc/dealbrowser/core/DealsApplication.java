package com.target.dealbrowserpoc.dealbrowser.core;

import android.app.Application;

import com.target.dealbrowserpoc.dealbrowser.api.ApiLink;

import io.realm.Realm;

public class DealsApplication extends Application {
    private static DealsApplication instance;

    private ApiLink apiLink;
    private ApplicationComponent component;

    public static DealsApplication getInstance() {
        return instance;
    }

    public ApiLink getApiLink() {
        return apiLink;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getBaseContext());
        instance = this;
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule())
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
