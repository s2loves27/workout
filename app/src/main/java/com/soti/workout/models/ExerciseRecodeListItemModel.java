package com.soti.workout.models;

import com.google.gson.annotations.SerializedName;

public class ExerciseRecodeListItemModel {

    public ExerciseRecodeListItemModel(int exercies_recode_time, String exercies_recode_date,
                                       String exercise_area_id,
                                       String exercise_area_name,
                                       String delete_yn, int updated_count
                                       ){

        this.exercies_recode_time = exercies_recode_time;
        this.exercies_recode_date = exercies_recode_date;
        this.exercise_area_id = exercise_area_id;
        this.exercise_area_name = exercise_area_name;
        this.delete_yn = delete_yn;
        this.updated_count = updated_count;

    }

    @SerializedName("exercise_recode_id")
    private String exercise_recode_id;

    @SerializedName("exercies_recode_time")
    private int exercies_recode_time;

    @SerializedName("exercies_recode_date")
    private String exercies_recode_date;


    @SerializedName("exercise_area_id")
    private String exercise_area_id;
    @SerializedName("exercise_area_name")
    private String exercise_area_name;

    @SerializedName("exercise_area_name_en")
    private String exercise_area_name_en;

    @SerializedName("exercise_updated_date")
    private String exercise_updated_date;

    @SerializedName("delete_yn")
    private String delete_yn;

    @SerializedName("updated_count")
    private int updated_count;

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

    public int getUpdated_count() {
        return updated_count;
    }

    public String getDelete_yn() {
        return delete_yn;
    }

    public String getExercise_area_id() {
        return exercise_area_id;
    }
}
