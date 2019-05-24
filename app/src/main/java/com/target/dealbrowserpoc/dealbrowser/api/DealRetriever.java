package com.target.dealbrowserpoc.dealbrowser.api;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.target.dealbrowserpoc.dealbrowser.core.DealsApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealRetriever extends ViewModel {
    private final ApiLink apiLink;

    private final MutableLiveData<Boolean> wasSuccessful;

    private final MutableLiveData<Boolean> isRunning;

    public DealRetriever() {
        apiLink = DealsApplication.getInstance().getApiLink();
        wasSuccessful = new MutableLiveData<>();
        isRunning = new MutableLiveData<>();

        isRunning.setValue(false);
    }

    public void start() {
        Boolean isBusy = isRunning.getValue();
        if (isBusy == null || !isBusy) {
            isRunning.setValue(true);
            apiLink.getDeals().enqueue(new Callback<DataListApi<DealApi>>() {
                @Override
                public void onResponse(Call<DataListApi<DealApi>> call, Response<DataListApi<DealApi>> response) {
                    isRunning.setValue(false);
                    if (response.code() >= 200 && response.code() <= 299) {
                        wasSuccessful.setValue(true);
                        // TODO: load values into Realm
                    } else {
                        // TODO log
                    }
                }

                @Override
                public void onFailure(Call<DataListApi<DealApi>> call, Throwable t) {
                    isRunning.setValue(false);
                    wasSuccessful.setValue(false);
                    // TODO log
                }
            });
        }
    }

    public MutableLiveData<Boolean> wasSuccessful() {
        return wasSuccessful;
    }

    public MutableLiveData<Boolean> isRunning() {
        return isRunning;
    }
}
