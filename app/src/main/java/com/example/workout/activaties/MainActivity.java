package com.example.workout.activaties;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workout.R;
import com.example.workout.adapters.CalendarAdapter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimerTask;

import com.example.workout.dialogs.BatteryPermissionDialog;
import com.example.workout.dialogs.RecodeAreaListDialog;
import com.example.workout.dialogs.SelectTimerInsertDialog;
import com.example.workout.managers.PreferenceHelper;
import com.example.workout.models.CalendarStructureModel;
import com.example.workout.models.ExerciseAreaModel;
import com.example.workout.models.ExerciseRecodeListItemModel;
import com.example.workout.models.ExerciseRecodeModel;
import com.example.workout.models.ExerciseRecodeStatisticsModel;
import com.example.workout.models.TokenModel;
import com.example.workout.restapi.ServerApiService;
import com.example.workout.restapi.ServiceGenerator;
import com.example.workout.services.TimerService;
import com.example.workout.utils.CalendarUtil;
import com.example.workout.utils.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    // ?????? ????????? ???
    TextView monthYearText;


    RecyclerView recyclerView;

    TextView btnTimer;
    TextView txtTimer;

    Spinner spinner;

    TextView txtThisMonth;
    TextView txtLastMonth;
    TextView txtLastMonthAll;

    private Handler handler;


//    private SelectTimerInsertDialog selectTimerInsertDialog;

    private RecodeAreaListDialog recodeAreaListDialog;
    PreferenceHelper preferenceHelper;
    ServerApiService serverApiService;

    //    ArrayList<Date>
    ArrayList<CalendarStructureModel> dayList;

    HashMap<String, String> exerciseArea;


    private final Callback<List<ExerciseRecodeListItemModel>> exerciseRecodeListCall = new Callback<List<ExerciseRecodeListItemModel>>() {
        @Override
        public void onResponse(Call<List<ExerciseRecodeListItemModel>> call, Response<List<ExerciseRecodeListItemModel>> response) {
            if (response.isSuccessful()) {
                List<ExerciseRecodeListItemModel> result = response.body();
                if (result != null) {

                    for (int i = 0; i < dayList.size(); i++) {
                        dayList.get(i).getExerciseRecodeListItemModel().clear();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    Toast.makeText(getApplicationContext(), "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < result.size(); i++) {
                        CalendarStructureModel structureModel = new CalendarStructureModel();
                        ExerciseRecodeListItemModel exerciseRecodeListItemModel = result.get(i);
                        String exerciseRecodeDate = exerciseRecodeListItemModel.getExercies_recode_date();

                        for (int j = 0; j < dayList.size(); j++) {
                            String date = sdf.format(dayList.get(j).getDate());

                            if (date.equals(exerciseRecodeDate)) {
                                dayList.get(j).getExerciseRecodeListItemModel().add(exerciseRecodeListItemModel);
                            }
                        }
//                        exerciseRecodeDate.equals()
                    }

                    CalendarAdapter adapter = new CalendarAdapter(dayList, getApplicationContext(), MainActivity.this);

                    //???????????? ?????? (??? 7???)
                    RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 7);

                    recyclerView.setLayoutManager(manager);

                    recyclerView.setAdapter(adapter);


                }
            } else if (response.code() == 400) {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_join_error_server), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_main_token_error), Toast.LENGTH_SHORT).show();

                finish();
            }
//            Toast.makeText(MainActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<List<ExerciseRecodeListItemModel>> call, Throwable t) {
            Toast.makeText(MainActivity.this, getString(R.string.txt_error_internet), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    };

    private final Callback<ExerciseRecodeStatisticsModel> exerciseRecodeStatisticsCall = new Callback<ExerciseRecodeStatisticsModel>() {
        @Override
        public void onResponse(Call<ExerciseRecodeStatisticsModel> call, Response<ExerciseRecodeStatisticsModel> response) {
            if (response.isSuccessful()) {
                ExerciseRecodeStatisticsModel result = response.body();
                if (result != null) {
//                    Toast.makeText(getApplicationContext(), "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                    int iThisMonth = result.getThis_month();
                    int iLastMonth = result.getLast_month();
                    int iLastMonthDay = result.getLast_month_day();

                    int iThisMonthHour = iThisMonth / 3600;
                    int iThisMonthMinute = iThisMonth / 60;
                    int iThisMonthSecond = iThisMonth % 60;

                    String strThisMonth = "";
                    if (iThisMonthHour > 0) strThisMonth += iThisMonthHour + "h ";
                    if (iThisMonthMinute > 0) strThisMonth += iThisMonthMinute + "m ";
                    if (iThisMonthSecond > 0) strThisMonth += iThisMonthSecond + "s";

                    int iLastMonthHour = iLastMonth / 3600;
                    int iLastMonthMinute = iLastMonth / 60;
                    int iLastMonthSecond = iLastMonth % 60;

                    String strLastMonth = "";
                    if (iLastMonthHour > 0) strLastMonth += iLastMonthHour + "h ";
                    if (iLastMonthMinute > 0) strLastMonth += iLastMonthMinute + "m ";
                    if (iLastMonthSecond > 0) strLastMonth += iLastMonthSecond + "s";


                    int iLastDayMonthHour = iLastMonthDay / 3600;
                    int iLastDayMonthMinute = iLastMonthDay / 60;
                    int iLastDayMonthSecond = iLastMonthDay % 60;

                    String strLastDayMonth = "";
                    if (iLastDayMonthHour > 0) strLastDayMonth += iLastDayMonthHour + "h ";
                    if (iLastDayMonthMinute > 0) strLastDayMonth += iLastDayMonthMinute + "m ";
                    if (iLastDayMonthSecond > 0) strLastDayMonth += iLastDayMonthSecond + "s";


                    txtThisMonth.setText(strThisMonth);
                    txtLastMonth.setText(strLastDayMonth);
                    txtLastMonthAll.setText(strLastMonth);


                    String dayOfFirstMonth = yearMonthDayFormDate(true);
                    String dayOfLastMonth = yearMonthDayFormDate(false);

                    serverApiService.exerciseRecodeList(preferenceHelper.getUserId(), dayOfFirstMonth, dayOfLastMonth).enqueue(exerciseRecodeListCall);

                }
            } else if (response.code() == 400) {
                ExerciseRecodeStatisticsModel result = response.body();
                if (result != null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_join_error_server), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_join_error_400), Toast.LENGTH_SHORT).show();
                }
                finish();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_main_token_error), Toast.LENGTH_SHORT).show();
                finish();

            }
//            Toast.makeText(MainActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<ExerciseRecodeStatisticsModel> call, Throwable t) {
            Toast.makeText(MainActivity.this, getString(R.string.txt_error_internet), Toast.LENGTH_SHORT).show();
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

                    String dayOfFirstMonth = yearMonthDayFormDate(true);
                    String dayOfLastMonth = yearMonthDayFormDate(false);

                    serverApiService.exerciseRecodeList(preferenceHelper.getUserId(), dayOfFirstMonth, dayOfLastMonth).enqueue(exerciseRecodeListCall);

                }
            } else if (response.code() == 400) {
                ExerciseRecodeModel result = response.body();
                if (result != null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_join_error_server), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_join_error_400), Toast.LENGTH_SHORT).show();
                }
                finish();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_main_token_error), Toast.LENGTH_SHORT).show();
                finish();
            }
//            Toast.makeText(MainActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<ExerciseRecodeModel> call, Throwable t) {
            Toast.makeText(MainActivity.this, getString(R.string.txt_error_internet), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    };


    private final Callback<List<ExerciseAreaModel>> exerciseAreaListCall = new Callback<List<ExerciseAreaModel>>() {
        @Override
        public void onResponse(Call<List<ExerciseAreaModel>> call, Response<List<ExerciseAreaModel>> response) {
            if (response.isSuccessful()) {
                List<ExerciseAreaModel> result = response.body();
                if (result != null) {
                    exerciseArea = new HashMap<>();
                    ArrayList<String> strExerciseArea = new ArrayList<>();
                    String locale = Util.getLocale(getApplicationContext());

                        for (int i = 0; i < result.size(); i++) {

                            ExerciseAreaModel exerciseAreaModel = result.get(i);
                            if(locale.equals("ko")) {
                                strExerciseArea.add(exerciseAreaModel.getExercise_area_name());
                                exerciseArea.put(exerciseAreaModel.getExercise_area_name(), exerciseAreaModel.getExercise_id());

                            }else {
                                strExerciseArea.add(exerciseAreaModel.getExercise_area_name_en());
                                exerciseArea.put(exerciseAreaModel.getExercise_area_name_en(), exerciseAreaModel.getExercise_id());
                            }
                        }



                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_exercise_area, strExerciseArea);

                    adapter.setDropDownViewResource(R.layout.spinner_item_exercise_area);

                    spinner.setAdapter(adapter);

                    spinner.setSelection(CalendarUtil.iExerciseAreaItem);


//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, item);


                }
            } else if (response.code() == 400) {
//                List<ExerciseAreaModel> result = response.body();

                Toast.makeText(getApplicationContext(), getString(R.string.txt_main_token_error), Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_join_error_server), Toast.LENGTH_SHORT).show();
            }
//            Toast.makeText(MainActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<List<ExerciseAreaModel>> call, Throwable t) {
            Toast.makeText(MainActivity.this, getString(R.string.txt_error_internet), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();


        //xml ?????????
        monthYearText = findViewById(R.id.monthYearText);
        ImageButton preBtn = findViewById(R.id.pre_btn);
        ImageButton nextBtn = findViewById(R.id.next_btn);
        recyclerView = findViewById(R.id.recyclerView);
        btnTimer = findViewById(R.id.btn_timer);
        txtTimer = findViewById(R.id.txt_timer);


        txtThisMonth = findViewById(R.id.txt_this_month);
        txtLastMonth = findViewById(R.id.txt_last_month);
        txtLastMonthAll = findViewById(R.id.txt_last_month_all);

        spinner = findViewById(R.id.spinner);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (powerManager.isIgnoringBatteryOptimizations(getPackageName()) == false) {
                // ????????? ????????? ?????? ??????.
                FragmentManager fm = getSupportFragmentManager();

                BatteryPermissionDialog fragment = new BatteryPermissionDialog();

                fragment.show(fm, "dialog");

            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CalendarUtil.iExerciseAreaItem = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                CalendarUtil.iExerciseAreaItem = 0;

            }
        });


        //?????? ?????????
        CalendarUtil.selectedDate = Calendar.getInstance();


        setStartService();


        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }

        handler.postDelayed(runnable, 100);


        preferenceHelper = new PreferenceHelper(getApplicationContext());

        serverApiService = ServiceGenerator.createService(ServerApiService.class, preferenceHelper.getToken());


        setMonthView();


        int mHour = CalendarUtil.exerciseTimeModel.getmHour();
        int mMin = CalendarUtil.exerciseTimeModel.getmMin();
        int mSec = CalendarUtil.exerciseTimeModel.getmSec();

        if (mHour == 0 && mMin == 0 && mSec == 0) {
            btnTimer.setText(getString(R.string.txt_select_time_insert_timer_start));
        } else {
            btnTimer.setText(getString(R.string.txt_select_time_insert_timer_end));

        }


        String dayOfFirstMonth = yearMonthDayFormDate(true);
        String dayOfLastMonth = yearMonthDayFormDate(false);

        Log.i("TEST", "dayOfFirstMonth : " + dayOfFirstMonth);
        Log.i("TEST", "dayOfLastMonth : " + dayOfLastMonth);

//        serverApiService.exerciseRecodeList(preferenceHelper.getUserId(), dayOfFirstMonth , dayOfLastMonth).enqueue(exerciseRecodeListCall);

        serverApiService.exerciseArea().enqueue(exerciseAreaListCall);
        serverApiService.exerciseRecodeStatistics(preferenceHelper.getUserId()).enqueue(exerciseRecodeStatisticsCall);


        Log.i("TEST", "USERID" + preferenceHelper.getUserId());


        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CalendarUtil.selectedDate.add(Calendar.MONTH, -1);
                }
                setMonthView();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CalendarUtil.selectedDate.add(Calendar.MONTH, 1);

                }
                setMonthView();
            }
        });

        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int mHour = CalendarUtil.exerciseTimeModel.getmHour();
                int mMin = CalendarUtil.exerciseTimeModel.getmMin();
                int mSec = CalendarUtil.exerciseTimeModel.getmSec();

                if (!checkPermission()) {

                    if (mHour == 0 && mMin == 0 && mSec == 0) {

                        sendMessageToService("startTimer");
                        btnTimer.setText(getString(R.string.txt_select_time_insert_timer_end));
                        spinner.setEnabled(false);


//                    handler.postDelayed(runnable, 100);
                    } else {
                        //?????????
                        btnTimer.setText(getString(R.string.txt_select_time_insert_timer_start));
                        txtTimer.setText("0h 0m 0s");
                        spinner.setEnabled(true);
                        sendMessageToService("endTimer");

                        int time = mHour * 60 * 60 + mMin * 60 + mSec;


                        long now = System.currentTimeMillis();
                        Date date = new Date(now);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String getDate = sdf.format(date);

                        serverApiService.exerciseRecode(preferenceHelper.getUserId(), exerciseArea.get((String) spinner.getSelectedItem()), getDate, time).enqueue(exerciseRecodeCall);


                    }
                }
            }
        });




//        selectTimerInsertDialog = new SelectTimerInsertDialog(this, new SelectTimerInsertDialog.SelectTimerInsertDialogClickListener() {
//            @Override
//            public void onTimerClick() {
//
//            }
//
//            @Override
//            public void onInputClick() {
//
//            }
//        });

        recodeAreaListDialog = new RecodeAreaListDialog(this);


//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(recodeAreaListDialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        Window window = recodeAreaListDialog.getWindow();
//        window.setAttributes(lp);

    }

    
    //????????? ??????
    private void setStartService(){
        startService(new Intent(MainActivity.this, TimerService.class));
        bindService(new Intent(this, TimerService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }


    // ????????? ??????
    private void setStopService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
        stopService(new Intent(MainActivity.this, TimerService.class));
    }




    private final Runnable runnable = new Runnable() {

        @Override
        public void run() {

            txtTimer.setText(CalendarUtil.exerciseTimeModel.getmHour() + "h " +CalendarUtil.exerciseTimeModel.getmMin() + "m " + CalendarUtil.exerciseTimeModel.getmSec() + "s");
            handler.postDelayed(runnable, 100);

        }
    };

    private String monthYearFromDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        String monthYear = month + "??? " + year;

        return monthYear;
    }

    private String yearMonthFromDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        String yearMonth = year + " - " + month;
        return yearMonth;
    }


    private String yearMonthDayFormDate(boolean flag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = "";

        if (flag) {
            day = sdf.format(dayList.get(0).getDate());
        } else {
            day = sdf.format(dayList.get(dayList.size() - 1).getDate());
        }
        return day;
    }


    private ArrayList<CalendarStructureModel> daysInMonthArray() {
        dayList = new ArrayList<>();

        Calendar monthCalendar = (Calendar) CalendarUtil.selectedDate.clone();

        //1?????? ??????
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        //?????? ???????????? -1 ?????????:1, ?????????:2
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        //?????? ??????(-5??????)
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        //42??? ?????? ??????
        while (dayList.size() < 42) {
            CalendarStructureModel calendarStructureModel = new CalendarStructureModel();

            calendarStructureModel.setDate(monthCalendar.getTime());
            //???????????? ?????? ??????
            dayList.add(calendarStructureModel);

            //1?????? ?????? ????????? ?????? 1???->2???->3???
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dayList;
    }


    private void setMonthView() {
        monthYearText.setText(yearMonthFromDate(CalendarUtil.selectedDate));

        dayList = daysInMonthArray();

        String dayOfFirstMonth = yearMonthDayFormDate(true);
        String dayOfLastMonth = yearMonthDayFormDate(false);

        Log.i("TEST", "dayOfFirstMonth : " + dayOfFirstMonth);
        Log.i("TEST", "dayOfLastMonth : " + dayOfLastMonth);


        serverApiService.exerciseRecodeList(preferenceHelper.getUserId(), dayOfFirstMonth, dayOfLastMonth).enqueue(exerciseRecodeListCall);


    }

    @Override
    public void onItemClick(CalendarStructureModel calendarStructureModel) {
        List<ExerciseRecodeListItemModel> exerciseRecodeListItemModel = calendarStructureModel.getExerciseRecodeListItemModel();
        ArrayList<ExerciseRecodeListItemModel> item = new ArrayList<>(exerciseRecodeListItemModel);
        if (item != null && !item.isEmpty()) {
            recodeAreaListDialog.setListItem(item);
            recodeAreaListDialog.show();

            Window window = recodeAreaListDialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//            recodeAreaListDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("test","onServiceConnected");
            mServiceMessenger = new Messenger(iBinder);
            try {
                Message msg = Message.obtain(null, TimerService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mServiceMessenger.send(msg);
            }
            catch (RemoteException e) {
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.i("test", "act : what " + msg.what);
            switch (msg.what) {
                case TimerService.MSG_SEND_TO_ACTIVITY:
                    int value1 = msg.getData().getInt("fromService");
                    String value2 = msg.getData().getString("test");
                    Log.i("test", "act : value1 " + value1);
                    Log.i("test", "act : value2 " + value2);
                    break;
            }
            return false;
        }
    }));
    private Messenger mServiceMessenger = null;
    private boolean mIsBound;

    private void sendMessageToService(String str) {
        if (mIsBound) {
            if (mServiceMessenger != null) {
                try {
                    Message msg = Message.obtain(null, TimerService.MSG_SEND_TO_SERVICE, str);
                    msg.replyTo = mMessenger;
                    mServiceMessenger.send(msg);
                } catch (RemoteException e) {
                }
            }
        }
    }

    private boolean checkPermission(){

        boolean granted = false;

        AppOpsManager appOps = (AppOpsManager) getApplicationContext()
                .getSystemService(Context.APP_OPS_SERVICE);

        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getApplicationContext().getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (getApplicationContext().checkCallingOrSelfPermission(
                    android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        }
        else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }

        return granted;
    }

}