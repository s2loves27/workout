package com.example.workout.restapi;

import com.example.workout.models.TokenModel;
import com.example.workout.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServerApiService {

    public static final String BASE_URL = "http://sotimain.com:10005/api/";

    @FormUrlEncoded
    @POST("v1/accounts/register/")
    Call<UserModel> register(@Field("email") String email, @Field("date_of_birth") String date_of_birth, @Field("password") String password, @Field("password2") String password2, @Field("sex") String sex,
    @Field("username") String username);

    @FormUrlEncoded
    @POST("v1/accounts/login/")
    Call<TokenModel> login(@Field("email") String email, @Field("password") String password);



}
