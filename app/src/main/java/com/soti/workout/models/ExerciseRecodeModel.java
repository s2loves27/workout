package com.soti.workout.models;

import com.google.gson.annotations.SerializedName;

public class ExerciseRecodeModel {

    @SerializedName("exercise_recode_time")
    private String exercise_recode_time;

    @SerializedName("exercise_area_id")
    private String exercise_area_id;

    @SerializedName("exercise_recode_date")
    private String exercise_recode_date;

    @SerializedName("exercise_user_id")
    private String exercise_user_id;

    @SerializedName("code")
    private String code;

    @SerializedName("detail")
    private String detail;


    @SerializedName("messages")
    private TokenMessageModel messages;

    public String getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }

    public String getExercise_area_id() {
        return exercise_area_id;
    }

    public String getExercise_recode_date() {
        return exercise_recode_date;
    }

    public String getExercise_recode_time() {
        return exercise_recode_time;
    }

    public String getExercise_user_id() {
        return exercise_user_id;
    }

    public TokenMessageModel getMessages() {
        return messages;
    }
}
