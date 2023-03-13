package com.example.workout.activaties;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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
import java.util.Objects;
import java.util.TimerTask;

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

    // 년월 텍스트 뷰
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
//                    Toast.makeText(getApplicationContext(), "저장이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
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

                    //레이아웃 설정 (열 7개)
                    RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 7);

                    recyclerView.setLayoutManager(manager);

                    recyclerView.setAdapter(adapter);


                }
            } else if (response.code() == 400) {
                Toast.makeText(getApplicationContext(), "Token이 만료 되었습니다 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "권한이 없습니다. APP을 다시 실행 해주세요.", Toast.LENGTH_SHORT).show();

                finish();
            }
//            Toast.makeText(MainActivity.this, "인터넷 연결 오류", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "저장이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                    int iThisMonth = result.getThis_month();
                    int iLastMonth = result.getLast_month();
                    int iLastMonthDay = result.getLast_month_day();

                    int iThisMonthHour = iThisMonth / 3600;
                    int iThisMonthMinute = iThisMonth / 60;
                    int iThisMonthSecond = iThisMonth % 60;

                    String strThisMonth = "";
                    if (iThisMonthHour > 0) strThisMonth += iThisMonthHour + "시간 ";
                    if (iThisMonthMinute > 0) strThisMonth += iThisMonthMinute + "분 ";
                    if (iThisMonthSecond > 0) strThisMonth += iThisMonthSecond + "초";

                    int iLastMonthHour = iLastMonth / 3600;
                    int iLastMonthMinute = iLastMonth / 60;
                    int iLastMonthSecond = iLastMonth % 60;

                    String strLastMonth = "";
                    if (iLastMonthHour > 0) strLastMonth += iLastMonthHour + "시간 ";
                    if (iLastMonthMinute > 0) strLastMonth += iLastMonthMinute + "분 ";
                    if (iLastMonthSecond > 0) strLastMonth += iLastMonthSecond + "초";


                    int iLastDayMonthHour = iLastMonthDay / 3600;
                    int iLastDayMonthMinute = iLastMonthDay / 60;
                    int iLastDayMonthSecond = iLastMonthDay % 60;

                    String strLastDayMonth = "";
                    if (iLastDayMonthHour > 0) strLastDayMonth += iLastDayMonthHour + "시간 ";
                    if (iLastDayMonthMinute > 0) strLastDayMonth += iLastDayMonthMinute + "분 ";
                    if (iLastDayMonthSecond > 0) strLastDayMonth += iLastDayMonthSecond + "초";


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
                    Toast.makeText(getApplicationContext(), "통신 에러", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "400 통신 에러", Toast.LENGTH_SHORT).show();
                }
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "권한이 없습니다. APP을 다시 실행 해주세요.", Toast.LENGTH_SHORT).show();
                finish();

            }
//            Toast.makeText(MainActivity.this, "인터넷 연결 오류", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "저장이 완료 되었습니다.", Toast.LENGTH_SHORT).show();

                    String dayOfFirstMonth = yearMonthDayFormDate(true);
                    String dayOfLastMonth = yearMonthDayFormDate(false);

                    serverApiService.exerciseRecodeList(preferenceHelper.getUserId(), dayOfFirstMonth, dayOfLastMonth).enqueue(exerciseRecodeListCall);

                }
            } else if (response.code() == 400) {
                ExerciseRecodeModel result = response.body();
                if (result != null) {
                    Toast.makeText(getApplicationContext(), "통신 에러", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "400 통신 에러", Toast.LENGTH_SHORT).show();
                }
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "권한이 없습니다. APP을 다시 실행 해주세요.", Toast.LENGTH_SHORT).show();
                finish();
            }
//            Toast.makeText(MainActivity.this, "인터넷 연결 오류", Toast.LENGTH_SHORT).show();
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
                    for (int i = 0; i < result.size(); i++) {
                        ExerciseAreaModel exerciseAreaModel = result.get(i);
                        strExerciseArea.add(exerciseAreaModel.getExercise_area_name());
                        exerciseArea.put(exerciseAreaModel.getExercise_area_name(), exerciseAreaModel.getExercise_id());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_exercise_area, strExerciseArea);

                    adapter.setDropDownViewResource(R.layout.spinner_item_exercise_area);

                    spinner.setAdapter(adapter);

                    spinner.setSelection(CalendarUtil.iExerciseAreaItem);


//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, item);


                }
            } else if (response.code() == 400) {
//                List<ExerciseAreaModel> result = response.body();

                Toast.makeText(getApplicationContext(), "Token이 만료 되었습니다 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(getApplicationContext(), "Email 또는 패스워드가 틀립니다 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
//            Toast.makeText(MainActivity.this, "인터넷 연결 오류", Toast.LENGTH_SHORT).show();
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


        //xml 초기화
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


        //변수 초기화
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

                if (mHour == 0 && mMin == 0 && mSec == 0) {

                    sendMessageToService("startTimer");
                    btnTimer.setText(getString(R.string.txt_select_time_insert_timer_end));
                    spinner.setEnabled(false);
//                    handler.postDelayed(runnable, 100);
                } else {
                    //서비스
                    btnTimer.setText(getString(R.string.txt_select_time_insert_timer_start));
                    txtTimer.setText("0분 0초");
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

    
    //서비스 시작
    private void setStartService(){
        startService(new Intent(MainActivity.this, TimerService.class));
        bindService(new Intent(this, TimerService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }


    // 서비스 정지
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

            txtTimer.setText(CalendarUtil.exerciseTimeModel.getmMin() + "분 " + CalendarUtil.exerciseTimeModel.getmSec() + "초");
            handler.postDelayed(runnable, 100);

        }
    };

    private String monthYearFromDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        String monthYear = month + "월 " + year;

        return monthYear;
    }

    private String yearMonthFromDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        String yearMonth = year + "년 " + month + "월";
        return yearMonth;
    }

    private String yearMonthDayFromDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        String yearMonthDay = year + "년 " + month + "월 " + day + "일";

        return yearMonthDay;
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

        //1일로 세팅
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        //요일 가져와서 -1 일요일:1, 월요일:2
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        //날짜 세팅(-5일전)
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        //42전 까지 반복
        while (dayList.size() < 42) {
            CalendarStructureModel calendarStructureModel = new CalendarStructureModel();

            calendarStructureModel.setDate(monthCalendar.getTime());
            //리스트에 날짜 등록
            dayList.add(calendarStructureModel);

            //1일씩 늘린 날짜로 변경 1일->2일->3일
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
}