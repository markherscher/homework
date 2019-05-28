package com.target.dealbrowserpoc.dealbrowser.core;

import com.target.dealbrowserpoc.dealbrowser.service.DealApiService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(DealApiService service);
}
