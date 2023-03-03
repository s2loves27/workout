package com.example.workout.restapi;

import com.example.workout.models.CheckUserModel;
import com.example.workout.models.TokenCheckModel;
import com.example.workout.models.TokenModel;
import com.example.workout.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServerApiService {

    public static final String BASE_URL = "http://softsoti.com:18000/api/";

    @FormUrlEncoded
    @POST("v1/accounts/register/")
    Call<UserModel> register(@Field("email") String email, @Field("date_of_birth") String date_of_birth, @Field("password") String password, @Field("password2") String password2, @Field("sex") String sex,
    @Field("username") String username);

    @FormUrlEncoded
    @POST("v1/accounts/login/")
    Call<TokenModel> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("v1/accounts/token-check/")
    Call<TokenCheckModel> tokenCheck(@Field("token") String token);

    @FormUrlEncoded
    @POST("v1/accounts/token-refresh/")
    Call<TokenCheckModel> tokenRefresh(@Field("refresh") String refresh);


    @FormUrlEncoded
    @POST("v1/accounts/check-user/")
    Call<CheckUserModel> checkUser(@Field("email") String email);
}
