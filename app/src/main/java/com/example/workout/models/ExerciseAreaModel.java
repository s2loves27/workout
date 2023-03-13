package com.example.workout.models;

import com.google.gson.annotations.SerializedName;

public class ExerciseAreaModel {

    @SerializedName("exercise_area_name")
    private String exercise_area_name;

    @SerializedName("exercise_area_name_en")
    private String exercise_area_name_en;


    @SerializedName("exercise_id")
    private String exercise_id;

    @SerializedName("detail")
    private String detail;

    @SerializedName("code")
    private String code;

    @SerializedName("messages")
    private TokenMessageModel messages;

    public String getExercise_area_name() {
        return exercise_area_name;
    }

    public String getExercise_id() {
        return exercise_id;
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

    public String getExercise_area_name_en() {
        return exercise_area_name_en;
    }
}
