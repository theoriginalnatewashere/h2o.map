package com.bluewater.cleanwatermap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CleanWaterMapServerAPISingleton {

    // lazy init
    private static volatile CleanWaterMapServerAPISingleton instance = null;


    private final static String API_ADDRESS = "https://clean-water-map-server.herokuapp.com/";

    private CleanWaterMapServerAPI jsonPlaceHolderApi;

    //private constructor.
    private CleanWaterMapServerAPISingleton() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(CleanWaterMapServerAPI.class);
    }

    private static CleanWaterMapServerAPISingleton getSingleton() {
        if (instance == null) {
            instance = new CleanWaterMapServerAPISingleton();
        }
        return instance;
    }

    public static CleanWaterMapServerAPI API() {
        return CleanWaterMapServerAPISingleton.getSingleton().jsonPlaceHolderApi;
    }
}
