package com.target.dealbrowserpoc.dealbrowser.core;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.target.dealbrowserpoc.dealbrowser.BuildConfig;
import com.target.dealbrowserpoc.dealbrowser.api.ApiLink;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {
    @Provides
    @Singleton
    ApiLink providesApiLink() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(createHttpOk(true))
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        return retrofit.create(ApiLink.class);
    }

    private OkHttpClient createHttpOk(boolean useLogging) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(useLogging
                ? HttpLoggingInterceptor.Level.HEADERS
                : HttpLoggingInterceptor.Level.NONE);

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }
}
