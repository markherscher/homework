package com.target.dealbrowserpoc.dealbrowser.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiLink {
    @GET("/api/deals")
    Call<DataListApi<DealApi>> getDeals();
}
