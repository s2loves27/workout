package com.example.workout.models;

public class ExerciseTimeModel {

    private int mHour = 0;
    private int mMin = 0;
    private int mSec = 0;

    public ExerciseTimeModel(int mHour,int mMin, int mSec){
        this.mHour = mHour;
        this.mMin = mMin;
        this.mSec = mSec;
    }

    public void setmHour(int mHour) {
        this.mHour = mHour;
    }

    public void setmMin(int mMin) {
        this.mMin = mMin;
    }

    public void setmSec(int mSec) {
        this.mSec = mSec;
    }

    public int getmMin() {
        return mMin;
    }

    public int getmSec() {
        return mSec;
    }

    public int getmHour() {
        return mHour;
    }
}
