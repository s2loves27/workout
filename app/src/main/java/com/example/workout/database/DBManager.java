package com.example.workout.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.work.Data;

import com.example.workout.models.ExerciseRecodeListItemModel;
import com.example.workout.models.ExerciseRecodeStatisticsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBManager {

    private DatabaseHelper dbHelper;

    private final Context context;

    private SQLiteDatabase database;


    public DBManager(Context context) {
        this.context = context;
    }

    private static final String SQL_UPDATE_EXERCISE_RECODE =
            "UPDATE " + DatabaseHelper.EXERCISE_RECODE_TABLE  + " SET ";


    private static final String SQL_INSERT_REPLACE_EXERCISE_RECODE =
            "INSERT OR REPLACE INTO " + DatabaseHelper.EXERCISE_RECODE_TABLE;

    private static final String SQL_INSERT_EXERCISE_RECODE =
            "INSERT INTO " + DatabaseHelper.EXERCISE_RECODE_TABLE;



    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public void insert(int exerciseRecodeTime, String exerciseRecodeDate,
                       String exerciseAreaId, String exerciseAreaName , String deleteYN, int updateCount){




        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());

        String query = "select count(*) as count, " + DatabaseHelper.EXERCISE_RECODE_TIME +" as time from " + DatabaseHelper.EXERCISE_RECODE_TABLE +
        " WHERE " + DatabaseHelper.EXERCISE_AREA_ID + "='" + exerciseAreaId +
                "' AND " + DatabaseHelper.EXERCISE_RECODE_DATE + "='" + exerciseRecodeDate +
                "' AND " + DatabaseHelper.DELETE_YN + "='N'";


        Cursor cursor = database.rawQuery(query, null);
        int count = 0;
        int time = 0;
        if(cursor.moveToNext()) {
            count = cursor.getInt(0);
            time = cursor.getInt(1);
        }


        query = SQL_INSERT_REPLACE_EXERCISE_RECODE
            + " VALUES (" +
                (time + exerciseRecodeTime) + ", '" +
            exerciseRecodeDate + "', '" +
            strDate + "', '" +
            strDate + "', '" +
            exerciseAreaId + "', '" +
            exerciseAreaName + "', '" +
            deleteYN + "', " +
            updateCount +
            ")";




//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(DatabaseHelper.EXERCISE_RECODE_TIME, exerciseRecodeTime);
//        contentValues.put(DatabaseHelper.EXERCISE_RECODE_DATE, exerciseRecodeDate);
//        contentValues.put(DatabaseHelper.CREATE_DATE, strDate);
//        contentValues.put(DatabaseHelper.UPDATED_DATE, strDate);
//        contentValues.put(DatabaseHelper.EXERCISE_AREA_ID, exerciseAreaId);
//        contentValues.put(DatabaseHelper.EXERCISE_AREA_NAME, exerciseAreaName);
//        contentValues.put(DatabaseHelper.EXERCISE_AREA_NAME_EN, exerciseAreaNameEn);
//        contentValues.put(DatabaseHelper.DELETE_YN, deleteYN);
//        contentValues.put(DatabaseHelper.UPDATED_COUNT, updateCount);
//        database.insert(DatabaseHelper.EXERCISE_RECODE_TABLE, null, contentValues);

        database.execSQL(query);


        close();
    }

    public ArrayList<ExerciseRecodeListItemModel> fetch(int updated_count){
        ArrayList<ExerciseRecodeListItemModel> list = new ArrayList<>();

        String[] columns = new String[]{
                DatabaseHelper.EXERCISE_RECODE_TIME, DatabaseHelper.EXERCISE_RECODE_DATE,
                DatabaseHelper.CREATE_DATE, DatabaseHelper.UPDATED_DATE, DatabaseHelper.EXERCISE_AREA_ID,
                DatabaseHelper.DELETE_YN, DatabaseHelper.UPDATED_COUNT, DatabaseHelper.EXERCISE_AREA_NAME,


        };
        String[] selectionArgs = new String[]{String.valueOf(updated_count), "N"};

        Cursor cursor = database.query(DatabaseHelper.EXERCISE_RECODE_TABLE, columns, DatabaseHelper.UPDATED_COUNT + " > ? AND " + DatabaseHelper.DELETE_YN + " = ?" , selectionArgs, null, null, null);

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                ExerciseRecodeListItemModel exerciseRecodeListItemModel =
                        new ExerciseRecodeListItemModel(cursor.getInt(0), cursor.getString(1), cursor.getString(4),
                                cursor.getString(7), cursor.getString(5),
                                cursor.getInt(6));

                list.add(exerciseRecodeListItemModel);
                cursor.moveToNext();

            }

        }

        close();
        return list;

    }

    public ArrayList<ExerciseRecodeListItemModel> fetch_update_local(int updated_count){
        ArrayList<ExerciseRecodeListItemModel> list = new ArrayList<>();

        String[] columns = new String[]{
                DatabaseHelper.EXERCISE_RECODE_TIME, DatabaseHelper.EXERCISE_RECODE_DATE,
                DatabaseHelper.CREATE_DATE, DatabaseHelper.UPDATED_DATE, DatabaseHelper.EXERCISE_AREA_ID,
                DatabaseHelper.DELETE_YN, DatabaseHelper.UPDATED_COUNT, DatabaseHelper.EXERCISE_AREA_NAME,


        };
        String[] selectionArgs = new String[]{String.valueOf(updated_count)};

        Cursor cursor = database.query(DatabaseHelper.EXERCISE_RECODE_TABLE, columns, DatabaseHelper.UPDATED_COUNT + " > ? " , selectionArgs, null, null, null);

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                ExerciseRecodeListItemModel exerciseRecodeListItemModel =
                        new ExerciseRecodeListItemModel(cursor.getInt(0), cursor.getString(1), cursor.getString(4),
                                cursor.getString(7), cursor.getString(5),
                                cursor.getInt(6));

                list.add(exerciseRecodeListItemModel);
                cursor.moveToNext();

            }

        }

        close();
        return list;

    }

    public ExerciseRecodeStatisticsModel statistics(String strThisMonthToday, String strThisMonthFirstDay,
                                                               String strThisMonthLastDay, String strLastMonthToday,
                                                               String strLastMonthFirstDay, String strLastMonthLastDay){


        String query = "select sum(" +  DatabaseHelper.EXERCISE_RECODE_TIME + ") as time from "
                + DatabaseHelper.EXERCISE_RECODE_TABLE +
                " WHERE DATE(" + DatabaseHelper.EXERCISE_RECODE_DATE +  ") BETWEEN DATE('" + strThisMonthFirstDay +
                "') AND DATE('" + strThisMonthToday + "')" +
                " AND " + DatabaseHelper.DELETE_YN + "='N'";


        Cursor cursor = database.rawQuery(query, null);
        int ThisMonthSum = 0;
        if(cursor.moveToNext()) {
            ThisMonthSum = cursor.getInt(0);
        }

        query = "select sum(" +  DatabaseHelper.EXERCISE_RECODE_TIME + ") as time from "
                + DatabaseHelper.EXERCISE_RECODE_TABLE +
                " WHERE DATE(" + DatabaseHelper.EXERCISE_RECODE_DATE +  ") BETWEEN DATE('" + strLastMonthFirstDay +
                "') AND DATE('" + strLastMonthToday + "')" +
                " AND " + DatabaseHelper.DELETE_YN + "='N'";


        cursor = database.rawQuery(query, null);
        int LastMonthSum = 0;
        if(cursor.moveToNext()) {
            LastMonthSum = cursor.getInt(0);
        }

        query = "select sum(" +  DatabaseHelper.EXERCISE_RECODE_TIME + ") as time from "
                + DatabaseHelper.EXERCISE_RECODE_TABLE +
                " WHERE DATE(" + DatabaseHelper.EXERCISE_RECODE_DATE +  ") BETWEEN DATE('" + strLastMonthFirstDay +
                "') AND DATE('" + strLastMonthLastDay + "')" +
                " AND " + DatabaseHelper.DELETE_YN + "='N'";


        cursor = database.rawQuery(query, null);
        int LastMonthTotalSum = 0;
        if(cursor.moveToNext()) {
            LastMonthTotalSum = cursor.getInt(0);
        }



        ExerciseRecodeStatisticsModel exerciseRecodeStatisticsModel = new ExerciseRecodeStatisticsModel(ThisMonthSum
        , LastMonthSum, LastMonthTotalSum);

        return exerciseRecodeStatisticsModel;
    }

    public ArrayList<ExerciseRecodeListItemModel> fetch_date(String updated_date){
        ArrayList<ExerciseRecodeListItemModel> list = new ArrayList<>();

        String[] columns = new String[]{
                DatabaseHelper.EXERCISE_RECODE_TIME, DatabaseHelper.EXERCISE_RECODE_DATE,
                DatabaseHelper.CREATE_DATE, DatabaseHelper.UPDATED_DATE, DatabaseHelper.EXERCISE_AREA_ID,
                DatabaseHelper.DELETE_YN, DatabaseHelper.UPDATED_COUNT, DatabaseHelper.EXERCISE_AREA_NAME,
        };
        String[] selectionArgs = new String[]{String.valueOf(updated_date), "N"};

        Cursor cursor = database.query(DatabaseHelper.EXERCISE_RECODE_TABLE, columns, DatabaseHelper.UPDATED_DATE + " > ? AND " + DatabaseHelper.DELETE_YN + " = ?" , selectionArgs, null, null, DatabaseHelper.UPDATED_COUNT + " ASC");

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                ExerciseRecodeListItemModel exerciseRecodeListItemModel =
                        new ExerciseRecodeListItemModel(cursor.getInt(0), cursor.getString(1), cursor.getString(4),
                                cursor.getString(7), cursor.getString(5),
                                cursor.getInt(6));

                list.add(exerciseRecodeListItemModel);
                cursor.moveToNext();

            }

        }

        close();
        return list;

    }


    public int getUpdateCount() {
        String query = "select max(" + DatabaseHelper.UPDATED_COUNT + ") as max_updated_count from " + DatabaseHelper.EXERCISE_RECODE_TABLE;

        Cursor cursor = database.rawQuery(query, null);
        int max_updated_count = 0;
        if (cursor.moveToNext()) {

            max_updated_count = cursor.getInt(0);
        }


        cursor.close();
        close();

        return max_updated_count;

    }

//    public void

    public void update_time(int time){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());

        String query = SQL_INSERT_REPLACE_EXERCISE_RECODE +
                "( " + DatabaseHelper.EXERCISE_RECODE_TIME + ", " + DatabaseHelper.UPDATED_DATE + ") VALUES (" +
                time+ ", " + strDate + ")";

        database.execSQL(query);
        close();
    }

    public void delete(int _id){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());

        String query = SQL_UPDATE_EXERCISE_RECODE +
                DatabaseHelper.DELETE_YN + "='" + "Y" + "', " +
                DatabaseHelper.UPDATED_DATE + "=" + strDate + " " +
                "WHERE " + DatabaseHelper._ID + "=" + _id;

        database.execSQL(query);
        close();

    }

    public void delete(String exercise_area_id, String exercise_recode_date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());

        String query = SQL_UPDATE_EXERCISE_RECODE +
                DatabaseHelper.DELETE_YN + "='" + "Y" + "', " +
                DatabaseHelper.UPDATED_DATE + "='" + strDate + "' " +
                "WHERE " + DatabaseHelper.EXERCISE_AREA_ID + "='" + exercise_area_id +
                "' AND " + DatabaseHelper.EXERCISE_RECODE_DATE + "='" + exercise_recode_date +"'";

        database.execSQL(query);
        close();

    }

    public void delete(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());

        String query = SQL_UPDATE_EXERCISE_RECODE +
                DatabaseHelper.DELETE_YN + "='" + "Y" + "', " +
        DatabaseHelper.UPDATED_DATE + "=" + strDate + " ";

        database.execSQL(query);
        close();


    }








}
