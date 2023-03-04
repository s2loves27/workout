package com.example.workout.models;

import com.google.gson.annotations.SerializedName;

public class ExerciseAreaModel {

    @SerializedName("exercise_area_name")
    private String exercise_area_name;

    @SerializedName("detail")
    private String detail;

    @SerializedName("code")
    private String code;

    @SerializedName("messages")
    private TokenMessageModel messages;

    public String getExercise_area_name() {
        return exercise_area_name;
    }


    public String getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }

    public TokenMessageModel getMessages() {
        return messages;
    }
}
