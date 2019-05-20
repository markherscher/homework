package com.target.dealbrowserpoc.dealbrowser.service;

import android.app.IntentService;
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

import io.realm.Realm;
import retrofit2.Response;
import timber.log.Timber;

public class DealApiService extends IntentService {
    private Realm realm;
    private ApiLink apiLink;

    public DealApiService() {
        super(DealApiService.class.getName());
        apiLink = DealsApplication.getInstance().getApiLink();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        realm = Realm.getDefaultInstance();
        Response<DataListApi<DealApi>> response = null;

        try {
            response = apiLink.getDeals().execute();
        } catch (IOException e) {
            Timber.e(e, "Error getting deals list");
        } catch (JsonParseException e) {
            Timber.e(e, "Error getting deals list");
        }

        if (response != null) {
            if (isStatusCodeValid(response.code())) {
                copyApiListToRealm(response.body());
            } else {
                Timber.e("HTTP error %d on getting deals list", response.code());
            }
        } else {
            Timber.e("Failed getting deals");
        }

        realm.close();
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
                            r.copyToRealmOrUpdate(new Deal(
                                    dealApi.guid,
                                    dealApi.title,
                                    dealApi.description,
                                    dealApi.image,
                                    dealApi.salePrice,
                                    dealApi.price,
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
}
