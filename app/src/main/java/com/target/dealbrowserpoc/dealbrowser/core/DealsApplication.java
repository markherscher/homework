package com.target.dealbrowserpoc.dealbrowser.core;

import android.app.Application;

import io.realm.Realm;

public class DealsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Some setup stuff
        Realm.init(getBaseContext());
    }
}
