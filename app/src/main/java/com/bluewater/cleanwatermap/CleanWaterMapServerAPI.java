package com.bluewater.cleanwatermap;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface CleanWaterMapServerAPI {
    @GET("waterProvider")
    Call<List<WaterProvider>> getWaterProviders();

    @GET("waterProvider/{id}")
    Call<WaterProvider> getOneWaterProvider(@Path("id") String id);

    @POST("waterProvider")
    Call<WaterProvider> createWaterProvider(@Body WaterProvider waterProvider);
}