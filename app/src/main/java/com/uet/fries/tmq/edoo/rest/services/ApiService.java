package com.uet.fries.tmq.edoo.rest.services;

import com.uet.fries.tmq.edoo.rest.models.ItemClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by tmq on 09/04/2017.
 */

public interface ApiService {

    @GET("classes")
    Call<List<ItemClass>> listClasses(@Header("Authorization") String token);

}
