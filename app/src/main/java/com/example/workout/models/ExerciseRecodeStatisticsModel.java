package com.example.workout.models;

import com.google.gson.annotations.SerializedName;

public class ExerciseRecodeStatisticsModel {

    @SerializedName("this_month")
    private int this_month;

    @SerializedName("last_month")
    private int last_month;

    @SerializedName("last_month_day")
    private int last_month_day;

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private String code;


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getLast_month() {
        return last_month;
    }

    public int getLast_month_day() {
        return last_month_day;
    }

    public int getThis_month() {
        return this_month;
    }
}
