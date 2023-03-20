package com.example.workout.models;

import com.google.gson.annotations.SerializedName;

public class ExerciseRecodeListItemModel {

    @SerializedName("exercise_recode_id")
    private String exercise_recode_id;

    @SerializedName("exercies_recode_time")
    private int exercies_recode_time;

    @SerializedName("exercies_recode_date")
    private String exercies_recode_date;

    @SerializedName("exercise_area_name")
    private String exercise_area_name;

    @SerializedName("exercise_area_name_en")
    private String exercise_area_name_en;

    @SerializedName("exercise_updated_date")
    private String exercise_updated_date;

    @SerializedName("code")
    private String code;

    @SerializedName("detail")
    private String detail;

    @SerializedName("messages")
    private TokenMessageModel messages;

    public TokenMessageModel getMessages() {
        return messages;
    }

    public String getExercise_recode_id() {
        return exercise_recode_id;
    }

    public String getDetail() {
        return detail;
    }

    public String getCode() {
        return code;
    }

    public String getExercise_area_name() {
        return exercise_area_name;
    }

    public int getExercies_recode_time() {
        return exercies_recode_time;
    }

    public String getExercies_recode_date() {
        return exercies_recode_date;
    }

    public String getExercise_updated_date() {
        return exercise_updated_date;
    }

    public String getExercise_area_name_en() {
        return exercise_area_name_en;
    }
}
