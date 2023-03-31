package com.example.workout.models;

import com.google.gson.annotations.SerializedName;

public class ExerciseRecodeUpdateCount {

    @SerializedName("max_updated_count")
    private int max_updated_count;


    public int getMax_updated_count() {
        return max_updated_count;
    }
}
