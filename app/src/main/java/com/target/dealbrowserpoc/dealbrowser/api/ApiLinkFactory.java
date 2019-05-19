package com.target.dealbrowserpoc.dealbrowser.api;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Helper class to create an instance of the {@code ApiLink} interface for use by Retrofit.
 */
public class ApiLinkFactory {
    private ApiLink apiLink;

    public ApiLinkFactory(String rootUrl) {
        this(rootUrl, true);
    }

    public ApiLinkFactory(String rootUrl, boolean useLogging) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(createHttpOk(useLogging))
                .baseUrl(rootUrl)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        apiLink = retrofit.create(ApiLink.class);
    }

    public ApiLink getInstance() {
        return apiLink;
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
