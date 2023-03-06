package com.example.workout.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.workout.models.ExerciseTimeModel;
import com.example.workout.utils.CalendarUtil;

import java.util.Timer;
import java.util.TimerTask;




public class TimerService extends Service {

    Timer mTimer;
    TimerTask mTimerTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private int mMsec = 0;


    @Override
    public void onCreate() {
        super.onCreate();

        CalendarUtil.exerciseTimeModel = new ExerciseTimeModel(0, 0, 0);

//        timerStart();

    }

    private void timerStart(){
        mTimer = new Timer();
        mTimerTask = new TimerTask(){

            @Override
            public void run() {
                mMsec++;
                if(mMsec >= 100){
                    mMsec=0;
                    CalendarUtil.exerciseTimeModel.setmSec(CalendarUtil.exerciseTimeModel.getmSec() + 1);
                }
                if(CalendarUtil.exerciseTimeModel.getmSec() == 60){
                    CalendarUtil.exerciseTimeModel.setmSec(0);
                    CalendarUtil.exerciseTimeModel.setmMin(CalendarUtil.exerciseTimeModel.getmMin() + 1);
                }
                if(CalendarUtil.exerciseTimeModel.getmMin() == 60){
                    CalendarUtil.exerciseTimeModel.setmMin(0);
                    CalendarUtil.exerciseTimeModel.setmHour(CalendarUtil.exerciseTimeModel.getmHour() + 1);
                }
//                mTimerText.set
            }
        };
        mTimer.schedule(mTimerTask, 0, 10);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null){
            return Service.START_STICKY; // 서비스가 종료 되었을 때도 다시 자동으로 실행 함
        }else{
            processCommand(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void processCommand(Intent intent){
        String command = intent.getStringExtra("command");
        String name = intent.getStringExtra("name");

        if(command.equals("startTime")){
            timerStart();
        }else if(command.equals("endTime")){
            timerStop();
        }

    }

    private void timerStop() {

        mTimer.cancel();

        CalendarUtil.exerciseTimeModel = new ExerciseTimeModel(0, 0, 0);

    }
}
