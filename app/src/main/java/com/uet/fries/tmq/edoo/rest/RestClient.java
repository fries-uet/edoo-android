package com.uet.fries.tmq.edoo.rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uet.fries.tmq.edoo.rest.models.ItemTypeAdapterFactory;
import com.uet.fries.tmq.edoo.rest.services.ApiService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tmq on 12/04/2017.
 */

public class RestClient {
    private static final String URL_HOST = "https://edoo.vn/api/";

    private ApiService apiService;
    private Context mContext;

    public RestClient(Context context, String field) {
        this.mContext = context;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .setLenient()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory(field))
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }

}
