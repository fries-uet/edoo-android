package com.uet.fries.tmq.edoo.rest.services;

import com.uet.fries.tmq.edoo.rest.models.ItemClass;
import com.uet.fries.tmq.edoo.rest.models.ItemLogin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by tmq on 09/04/2017.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("login")
    Call<ItemLogin> login(@Field("email") String email, @Field("password") String password);

    @GET("classes")
    Call<List<ItemClass>> listClasses(@Header("Authorization") String token);

}
