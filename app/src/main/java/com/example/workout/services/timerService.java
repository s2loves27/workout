package com.example.workout.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class timerService extends Service {

    Timer mTimer;
    TimerTask mTimerTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private int mMin = 0;
    private int mSec = 0;
    private int mMsec = 0;


    @Override
    public void onCreate() {
        super.onCreate();


    }

    private void timerStart(){
        mTimer = new Timer();
        mTimerTask = new TimerTask(){

            @Override
            public void run() {
                mMsec++;
                if(mMsec >= 100){
                    mMsec=0;
                    mSec++;
                    Intent intent = new Intent();
                    intent.setAction("sec");
                    intent.putExtra("time", mSec);
                    sendBroadcast(intent);

                }
                if(mSec == 60){
                    mSec = 0;
                    mMin ++;
                    Intent intent = new Intent();
                    intent.setAction("min");
                    intent.putExtra("time", mMin);
                    sendBroadcast(intent);
                }
//                mTimerText.set
            }
        };
    }

    private void timerStop() {
        mTimer.cancel();
        mMin=0;
        mSec=0;
        mMsec=0;
        Intent intent = new Intent();
        intent.setAction("reset");
        intent.putExtra("0", 0);
        sendBroadcast(intent);
    }
}
