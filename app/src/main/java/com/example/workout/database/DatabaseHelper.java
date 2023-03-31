package com.example.workout.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {



    //database
    public static final String DATABASE_NAME = "exercise.db";
    public static final int DATABASE_VERSION = 1;


    // table
    public static final String _ID = "_id";
    public static final String EXERCISE_RECODE_TIME = "exercise_recode_time";
    public static final String EXERCISE_RECODE_DATE = "exercise_recode_date";
    public static final String CREATE_DATE = "created_date";
    public static final String UPDATED_DATE = "updated_date";
    public static final String EXERCISE_AREA_ID = "exercise_area_id";

    public static final String EXERCISE_AREA_NAME = "exercise_area_name";

//    public static final String EXERCISE_AREA_NAME_EN = "exercise_area_name_en";
    public static final String DELETE_YN = "delete_yn";
    public static final String UPDATED_COUNT = "updated_count";





    public static final String EXERCISE_RECODE_TABLE = "exerciserecode";

    private static final String CREATE_TABLE = "CREATE TABLE " + EXERCISE_RECODE_TABLE + " (" +
            EXERCISE_RECODE_TIME + " INTEGER, " +
            EXERCISE_RECODE_DATE + " TEXT NOT NULL, " +
            CREATE_DATE + " TEXT, " +
            UPDATED_DATE + " TEXT, " +
            EXERCISE_AREA_ID + " TEXT NOT NULL, " +
            EXERCISE_AREA_NAME + " TEXT, " +
            DELETE_YN + " TEXT, " +
            UPDATED_COUNT + " INTEGER ," +
            "PRIMARY KEY " + "(" + EXERCISE_RECODE_DATE + ", " + EXERCISE_AREA_ID + ")"+
            ")";




    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + EXERCISE_RECODE_TABLE);
        onCreate(db);
    }
}
