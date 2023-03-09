package com.example.workout.restapi;

import com.example.workout.models.CheckUserModel;
import com.example.workout.models.ExerciseAreaModel;
import com.example.workout.models.ExerciseRecodeListItemModel;
import com.example.workout.models.ExerciseRecodeModel;
import com.example.workout.models.ExerciseRecodeStatisticsModel;
import com.example.workout.models.TokenCheckModel;
import com.example.workout.models.TokenModel;
import com.example.workout.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServerApiService {

    public static final String BASE_URL = "http://softsoti.com:18000/api/";


    ///// 유저 관련 API /////

    @FormUrlEncoded
    @POST("v1/accounts/register/")
    Call<UserModel> register(@Field("email") String email, @Field("date_of_birth") String date_of_birth, @Field("password") String password, @Field("password2") String password2, @Field("sex") String sex,
    @Field("username") String username);

    @FormUrlEncoded
    @POST("v1/accounts/login/")
    Call<TokenModel> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("v1/accounts/kakao-login/")
    Call<TokenModel> kakaoLogin(@Field("email") String email);

    @FormUrlEncoded
    @POST("v1/accounts/token-check/")
    Call<TokenCheckModel> tokenCheck(@Field("token") String token);

    @FormUrlEncoded
    @POST("v1/accounts/token-refresh/")
    Call<TokenCheckModel> tokenRefresh(@Field("refresh") String refresh);

    @FormUrlEncoded
    @POST("v1/accounts/check-user/")
    Call<CheckUserModel> checkUser(@Field("email") String email);

    ///// 운동 관련 API /////
    @FormUrlEncoded
    @POST("v1/exercise/area/")
    Call<ExerciseAreaModel> exerciseArea(@Field("exercise_area_name") String exerciseAreaName);

    @GET("v1/exercise/area/")
    Call<List<ExerciseAreaModel>> exerciseArea();

    @FormUrlEncoded
    @POST("v1/exercise/recode/")
    Call<ExerciseRecodeModel> exerciseRecode(
            @Field("exercise_user_id") String exercise_user_id,
            @Field("exercise_area_id") String exercise_area_id,
            @Field("exercise_recode_date") String exercise_recode_date,
            @Field("exercise_recode_time") int exercise_recode_time);

    @FormUrlEncoded
    @POST("v1/exercise/recode/list/")
    Call<List<ExerciseRecodeListItemModel>> exerciseRecodeList(
            @Field("exercise_user_id") String exercise_user_id,
            @Field("start_date") String start_date,
            @Field("end_date") String end_date
            );

    @FormUrlEncoded
    @POST("v1/exercise/recode/statistics/")
    Call<ExerciseRecodeStatisticsModel> exerciseRecodeStatistics(
            @Field("exercise_user_id") String exercise_user_id
    );




}
