package com.example.user.retrtofit_api;

import com.example.user.models.AdverModel;
import com.example.user.models.AdverRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AdverApi
{
    @POST("advers")
    Call<AdverModel> createPost(@Body AdverRequest adverRequest);
}
