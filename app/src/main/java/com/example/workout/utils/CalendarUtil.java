package com.example.workout.utils;

import com.example.workout.models.ExerciseAreaModel;
import com.example.workout.models.ExerciseTimeModel;

import java.time.LocalDate;
import java.util.Calendar;

public class CalendarUtil {

    public static Calendar selectedDate; //년월 변수

    public static ExerciseTimeModel exerciseTimeModel = new ExerciseTimeModel(0, 0, 0);

    public static int iExerciseAreaItem;

    public static int today;

}
