package com.example.workout.utils;

import android.content.Context;

public class  Util {

    public static String getMonth(int month){
        if(month > 0 && month < 10){
            return "0" + String.valueOf(month);
        }else{
            return String.valueOf(month);
        }
    }

    public static String getDay(int day){
        if(day > 0 && day < 10){
            return "0" + String.valueOf(day);
        }else{
            return String.valueOf(day);
        }
    }

    public static String getLocale(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }
}
