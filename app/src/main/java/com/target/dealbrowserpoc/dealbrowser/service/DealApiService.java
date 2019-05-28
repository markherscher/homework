package com.target.dealbrowserpoc.dealbrowser.service;

import android.app.IntentService;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonParseException;
import com.target.dealbrowserpoc.dealbrowser.api.ApiLink;
import com.target.dealbrowserpoc.dealbrowser.api.DataListApi;
import com.target.dealbrowserpoc.dealbrowser.api.DealApi;
import com.target.dealbrowserpoc.dealbrowser.core.DealsApplication;
import com.target.dealbrowserpoc.dealbrowser.model.Deal;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Response;
import timber.log.Timber;

public class DealApiService extends IntentService {
    private static final State state = new State();

    @Inject
    ApiLink apiLink;
    private Realm realm;

    public static State getState() {
        return state;
    }

    public DealApiService() {
        super(DealApiService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ((DealsApplication) getApplication()).getComponent().inject(this);

        realm = Realm.getDefaultInstance();
        Response<DataListApi<DealApi>> response = null;
        boolean successful = false;

        state.isRunning.postValue(true);
        state.wasLastSuccessful.postValue(null);

        try {
            response = apiLink.getDeals().execute();
        } catch (IOException e) {
            Timber.e(e, "Error getting deals list");
        } catch (JsonParseException e) {
            Timber.e(e, "Error getting deals list");
        }

        if (response != null) {
            if (isStatusCodeValid(response.code())) {
                successful = true;
                copyApiListToRealm(response.body());
            } else {
                Timber.e("HTTP error %d on getting deals list", response.code());
            }
        } else {
            Timber.e("Failed getting deals");
        }

        realm.close();
        state.isRunning.postValue(false);
        state.wasLastSuccessful.postValue(successful);
    }

    /**
     * This is a very quick and dirty way to convert a string such as "$2.04" to 204. I am not
     * putting a lot of effort into this conversion because this is secondary to what the app
     * demonstrates, and in a real-life situation the API might give us the prices as numbers
     * anyway.
     *
     * @param dollarsString a string, such as "$2.03"
     * @return the cents value
     */
    private int dollarStringToCentsValue(String dollarsString) {
        final String currency = "$";
        final int CENTS_PER_DOLLAR = 100;
        int centsValue = 0;

        if (dollarsString != null && dollarsString.length() > 0) {
            float dollarsValue = 0f;

            try {
                dollarsValue = Float.parseFloat(dollarsString.replace(currency, ""));
            } catch (NumberFormatException e) {
                // Oh well, what can we do?
            }

            centsValue = (int) (dollarsValue * CENTS_PER_DOLLAR);
        }

        return centsValue;
    }

    private void copyApiListToRealm(final DataListApi<DealApi> dataList) {
        if (dataList != null && dataList.data != null) {
            Timber.d("Received deals list of length %d", dataList.data.size());
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm r) {
                    for (DealApi dealApi : dataList.data) {
                        // Require GUID to be set; anything else can be null
                        if (dealApi != null && dealApi.guid != null) {
                            int salesPriceCents = dollarStringToCentsValue(dealApi.salePrice);
                            int priceCents = dollarStringToCentsValue(dealApi.price);

                            // If sale price is 0 then there is no sale
                            int actualPrice = salesPriceCents == 0 ? priceCents : salesPriceCents;

                            r.copyToRealmOrUpdate(new Deal(
                                    dealApi.guid,
                                    dealApi.title,
                                    dealApi.description,
                                    dealApi.image,
                                    actualPrice,
                                    priceCents,
                                    dealApi.aisle,
                                    dealApi.index));
                        }
                    }
                }
            });
        }
    }

    private static boolean isStatusCodeValid(int code) {
        return code >= 200 && code <= 299;
    }

    /**
     * Hold the state of this service in a singleton, and use the {@code ViewModel}
     * class for this because of its built-in broadcasting and lifecycle management.
     */
    public static class State extends ViewModel {
        private final MutableLiveData<Boolean> isRunning = new MutableLiveData<>();
        private final MutableLiveData<Boolean> wasLastSuccessful = new MutableLiveData<>();

        public LiveData<Boolean> isRunning() {
            return isRunning;
        }

        public LiveData<Boolean> wasLastSuccessful() {
            return wasLastSuccessful;
        }
    }
}
