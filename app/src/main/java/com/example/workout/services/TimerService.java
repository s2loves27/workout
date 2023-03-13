package com.example.workout.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.health.TimerStat;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.workout.models.ExerciseTimeModel;
import com.example.workout.utils.CalendarUtil;

import java.util.Timer;
import java.util.TimerTask;


public class TimerService extends Service {


    public static final int MSG_REGISTER_CLIENT = 1;

    public static final int MSG_SEND_TO_SERVICE = 3;
    public static final int MSG_SEND_TO_ACTIVITY = 4;
    Timer mTimer;
    TimerTask mTimerTask;

    private Messenger mClient = null;   // Activity 에서 가져온 Messenger

    private Handler handler;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }


    private int mMsec = 0;


    @Override
    public void onCreate() {
        super.onCreate();

        CalendarUtil.exerciseTimeModel = new ExerciseTimeModel(0, 0, 0);

        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }

//        timerStart();
        mTimer = new Timer();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Log.i("TEST", "시 : 분 : 초 " + "   " + CalendarUtil.exerciseTimeModel.getmHour() + " : " +
                    CalendarUtil.exerciseTimeModel.getmMin() + " : " +
                    CalendarUtil.exerciseTimeModel.getmSec());
            CalendarUtil.exerciseTimeModel.setmSec(CalendarUtil.exerciseTimeModel.getmSec() + 1);
            if (CalendarUtil.exerciseTimeModel.getmSec() == 60) {
                CalendarUtil.exerciseTimeModel.setmSec(0);
                CalendarUtil.exerciseTimeModel.setmMin(CalendarUtil.exerciseTimeModel.getmMin() + 1);
            }
            if (CalendarUtil.exerciseTimeModel.getmMin() == 60) {
                CalendarUtil.exerciseTimeModel.setmMin(0);
                CalendarUtil.exerciseTimeModel.setmHour(CalendarUtil.exerciseTimeModel.getmHour() + 1);
            }

            handler.postDelayed(runnable, 1000);

        }
    };

    private void timerStart() {
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return Service.START_STICKY; // 서비스가 종료 되었을 때도 다시 자동으로 실행 함
        } else {
            processCommand(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void processCommand(Intent intent) {
        String command = intent.getStringExtra("command");
        String name = intent.getStringExtra("name");

//        if(command.equals("startTime")){
//            timerStart();
//        }else if(command.equals("endTime")){
//            timerStop();
//        }

    }

    private void timerStop() {
        handler.removeMessages(0);

//        mTimer.cancel();


        CalendarUtil.exerciseTimeModel = new ExerciseTimeModel(0, 0, 0);

    }

    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.w("test", "ControlService - message what : " + msg.what + " , msg.obj " + msg.obj);
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClient = msg.replyTo;  // activity로부터 가져온
                    break;
                case MSG_SEND_TO_SERVICE:
                    if (msg.obj != null) {
                        if (msg.obj.toString().equals("startTimer")) {
                            Log.i("TEST", "startTimer");

                            timerStart();
                        } else if (msg.obj.toString().equals("endTimer")) {
                            Log.i("TEST", "stopTimer");

                            timerStop();
                        }
                    }
                    break;

            }
            return false;
        }
    }));

    private void sendMsgToActivity(int sendValue) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("fromService", sendValue);
            bundle.putString("test", "abcdefg");
            Message msg = Message.obtain(null, MSG_SEND_TO_ACTIVITY);
            msg.setData(bundle);
            mClient.send(msg);      // msg 보내기
        } catch (RemoteException e) {
            Log.e("TEST", e.getMessage());
        }
    }
}
