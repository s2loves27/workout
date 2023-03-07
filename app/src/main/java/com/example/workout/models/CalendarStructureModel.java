package com.example.workout.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class CalendarStructureModel {

    Date date;

    List<ExerciseRecodeListItemModel> exerciseRecodeListItemModel;

    public Date getDate() {
        return date;
    }

    public List<ExerciseRecodeListItemModel> getExerciseRecodeListItemModel() {
        return exerciseRecodeListItemModel;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setExerciseRecodeListItemModel(List<ExerciseRecodeListItemModel> exerciseRecodeListItemModel) {
        this.exerciseRecodeListItemModel = exerciseRecodeListItemModel;
    }
}
