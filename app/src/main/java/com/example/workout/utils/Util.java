package com.example.workout.utils;

import android.content.Context;

import com.example.workout.database.DBManager;

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

//    public static void dbInset(Context context){
////        StringBuilder err = new StringBuilder();
////        for (int i = 0; i < t.getStackTrace().length; i++) {
////            err.append(t.getStackTrace()[i].toString()).append("\n");
////        }
////        err.append(t.getMessage()).append("\n");
////        err.append(t.toString()).append("\n");
//
////        err.append(getActivityName(context)).append("\n");
//
//
//        new DBManager(context).open().insert();
//    }


}
