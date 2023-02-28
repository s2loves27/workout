package com.example.workout.models;

import com.google.gson.annotations.SerializedName;

public class ExerciseAreaModel {

    @SerializedName("exercise_area_name")
    private String exercise_area_name;


    public String getExercise_area_name() {
        return exercise_area_name;
    }


}
