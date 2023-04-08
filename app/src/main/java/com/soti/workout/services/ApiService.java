package com.soti.workout.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.soti.workout.R;
import com.soti.workout.activaties.MainActivity;
import com.soti.workout.database.DBManager;
import com.soti.workout.managers.PreferenceHelper;
import com.soti.workout.models.ExerciseRecodeListItemModel;
import com.soti.workout.models.ExerciseRecodeModel;
import com.soti.workout.models.ExerciseRecodeUpdateCount;
import com.soti.workout.restapi.ServerApiService;
import com.soti.workout.restapi.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiService  extends Service {


    Handler handler;


    ServerApiService serverApiService;
    PreferenceHelper preferenceHelper;



    int apiCount = 1;


        private final Callback<List<ExerciseRecodeListItemModel>> exerciseRecodeListCall = new Callback<List<ExerciseRecodeListItemModel>>() {
        @Override
        public void onResponse(Call<List<ExerciseRecodeListItemModel>> call, Response<List<ExerciseRecodeListItemModel>> response) {
            if (response.isSuccessful()) {
                List<ExerciseRecodeListItemModel> result = response.body();
                if (result != null) {

                    DBManager dbManager = new DBManager(getApplicationContext());

                    int localCount = dbManager.open().getUpdateCount();
                    boolean updateFlag = false;
                    for(int i = 0 ; i < result.size(); i ++){
                        ExerciseRecodeListItemModel exerciseRecodeListItemModel = result.get(i);

                        int updateCount = exerciseRecodeListItemModel.getUpdated_count();

                        Log.i("TEST", "updateCount" + updateCount +"\n localCount" + localCount);
                        if(localCount <  updateCount){
                            updateFlag = true;
                            dbManager.open().insert(
                                    exerciseRecodeListItemModel.getExercies_recode_time(),
                                    exerciseRecodeListItemModel.getExercies_recode_date(),
                                    exerciseRecodeListItemModel.getExercise_area_id(),
                                    exerciseRecodeListItemModel.getExercise_area_name(),
                                    exerciseRecodeListItemModel.getDelete_yn(),
                                    exerciseRecodeListItemModel.getUpdated_count()
                            );
                        }
                    }
                    if(updateFlag){
                        Intent showIntent = new Intent(getApplicationContext(), MainActivity.class);
                        showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        showIntent.putExtra("update", "1");
                        startActivity(showIntent);


                    }



                }
            } else if (response.code() == 400) {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_join_error_server), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_main_token_error), Toast.LENGTH_SHORT).show();

            }
//            Toast.makeText(MainActivity.this, "인터넷 연결 오류", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<List<ExerciseRecodeListItemModel>> call, Throwable t) {
            Toast.makeText(getApplicationContext(), getString(R.string.txt_error_internet), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    };

    private final Callback<ExerciseRecodeModel> exerciseRecodeCall = new Callback<ExerciseRecodeModel>() {
        @Override
        public void onResponse(Call<ExerciseRecodeModel> call, Response<ExerciseRecodeModel> response) {
            if (response.isSuccessful()) {
                ExerciseRecodeModel result = response.body();
                if (result != null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_main_save_complete), Toast.LENGTH_SHORT).show();

//                    String dayOfFirstMonth = yearMonthDayFormDate(true);
//                    String dayOfLastMonth = yearMonthDayFormDate(false);

//                    serverApiService.exerciseRecodeList(preferenceHelper.getUserId(), dayOfFirstMonth, dayOfLastMonth).enqueue(exerciseRecodeListCall);
//                        getRecodeList();
                }
            } else if (response.code() == 400) {
                ExerciseRecodeModel result = response.body();
                if (result != null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_join_error_server), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_join_error_400), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_main_token_error), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ExerciseRecodeModel> call, Throwable t) {
            Toast.makeText(getApplicationContext(), getString(R.string.txt_error_internet), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    };


    private final Callback<ExerciseRecodeUpdateCount> exerciseRecodeUpdateCountCallback = new Callback<ExerciseRecodeUpdateCount>() {
        @Override
        public void onResponse(Call<ExerciseRecodeUpdateCount> call, Response<ExerciseRecodeUpdateCount> response) {
            if (response.isSuccessful()) {
                ExerciseRecodeUpdateCount result = response.body();
                if (result != null) {

                    int maxUpdatedCount = result.getMax_updated_count();


                    DBManager dbManager = new DBManager(getApplicationContext());
                    int updated_count = dbManager.open().getUpdateCount();
                    Log.i("test", "maxUpdatedCount:" + maxUpdatedCount + "\n updated_count :"+
                            updated_count );
                    if(maxUpdatedCount < updated_count){


                        ArrayList<ExerciseRecodeListItemModel> exerciseRecodeListItemModels =
                                dbManager.open().fetch_update_local(maxUpdatedCount);

                        ExerciseRecodeListItemModel exerciseRecodeListItemModel
                                = exerciseRecodeListItemModels.get(0);



                        serverApiService.exerciseRecode(preferenceHelper.getUserId(), exerciseRecodeListItemModel.getExercise_area_id(),
                                exerciseRecodeListItemModel.getExercies_recode_date(), exerciseRecodeListItemModel.getExercies_recode_time(),
                                exerciseRecodeListItemModel.getUpdated_count()).enqueue(exerciseRecodeCall);

                    }






                }
            } else if (response.code() == 400) {

                Toast.makeText(getApplicationContext(), getString(R.string.txt_main_token_error), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_join_error_server), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ExerciseRecodeUpdateCount> call, Throwable t) {
            Toast.makeText(getApplicationContext(), getString(R.string.txt_error_internet), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler(Looper.getMainLooper());



        preferenceHelper = new PreferenceHelper(getApplicationContext());

        serverApiService = ServiceGenerator.createService(ServerApiService.class, preferenceHelper.getToken());

        handler.postDelayed(runnable, 10000);



    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Log.i("test", preferenceHelper.getUserId());

            serverApiService.exerciseRecodeUpdateCount(preferenceHelper.getUserId()).enqueue(exerciseRecodeUpdateCountCallback);

            if(apiCount % 60 == 0){
                serverApiService.exerciseRecodeList(preferenceHelper.getUserId(), -1).enqueue(exerciseRecodeListCall);
            }

            apiCount += 1;


            handler.postDelayed(runnable, 10000);
        }
    };





}
