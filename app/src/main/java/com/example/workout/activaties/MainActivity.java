package com.example.workout.activaties;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workout.R;
import com.example.workout.adapters.CalendarAdapter;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimerTask;

import com.example.workout.dialogs.SelectTimerInsertDialog;
import com.example.workout.managers.PreferenceHelper;
import com.example.workout.restapi.ServerApiService;
import com.example.workout.restapi.ServiceGenerator;
import com.example.workout.services.TimerService;
import com.example.workout.utils.CalendarUtil;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    // 년월 텍스트 뷰
    TextView monthYearText;


    RecyclerView recyclerView;

    TextView btnTimer;
    TextView txtTimer;

    private Handler handler;


    private SelectTimerInsertDialog selectTimerInsertDialog;

    PreferenceHelper preferenceHelper;
    ServerApiService serverApiService;
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


        //변수 초기화
        CalendarUtil.selectedDate = Calendar.getInstance();

        if(handler == null){
            handler = new Handler(Looper.getMainLooper());
        }

        preferenceHelper = new PreferenceHelper(getApplicationContext());

        serverApiService = ServiceGenerator.createService(ServerApiService.class, preferenceHelper.getToken());


//        //현재 날짜
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CalendarUtil.selectedDate = LocalDate.now();
//        }
        setMonthView();
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

                if(btnTimer.getText().equals(getString(R.string.txt_select_time_insert_timer_start))) {
                    Intent intent = new Intent(getApplicationContext(), TimerService.class);
                    intent.putExtra("command", "startTime");
                    intent.putExtra("name", "123");
                    startService(intent);
                    btnTimer.setText(getString(R.string.txt_select_time_insert_timer_end));
                    handler.postDelayed(runnable, 100);
                }else if(btnTimer.getText().equals(getString(R.string.txt_select_time_insert_timer_end))){
                    //서비스
                    btnTimer.setText(getString(R.string.txt_select_time_insert_timer_start));
                    txtTimer.setText("0분 0초");
                    Intent intent = new Intent(getApplicationContext(), TimerService.class);
                    intent.putExtra("command", "endTime");
                    intent.putExtra("name", "123");
                    startService(intent);
                    handler.removeMessages(0);
                }
            }
        });

        selectTimerInsertDialog = new SelectTimerInsertDialog(this, new SelectTimerInsertDialog.SelectTimerInsertDialogClickListener() {
            @Override
            public void onTimerClick() {

            }

            @Override
            public void onInputClick() {

            }
        });


    }

    private final Runnable runnable = new Runnable(){

        @Override
        public void run() {

//            CalendarUtil.exerciseTimeModel.getmMin()
            txtTimer.setText(CalendarUtil.exerciseTimeModel.getmMin() + "분 " +  CalendarUtil.exerciseTimeModel.getmSec() + "초");

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


    private ArrayList<Date> daysInMonthArray() {
        ArrayList<Date> dayList = new ArrayList<>();

        Calendar monthCalendar = (Calendar) CalendarUtil.selectedDate.clone();

        //1일로 세팅
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        //요일 가져와서 -1 일요일:1, 월요일:2
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) -1;

        //날짜 세팅(-5일전)
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        //42전 까지 반복
        while(dayList.size() < 42) {

            //리스트에 날짜 등록
            dayList.add(monthCalendar.getTime());

            //1일씩 늘린 날짜로 변경 1일->2일->3일
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);

        }

        return dayList;
    }


    private void setMonthView() {
        monthYearText.setText(yearMonthFromDate(CalendarUtil.selectedDate));

        ArrayList<Date> dayList = daysInMonthArray();

        CalendarAdapter adapter = new CalendarAdapter(dayList, getApplicationContext(), MainActivity.this);

        //레이아웃 설정 (열 7개)
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 7);

        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        selectTimerInsertDialog.show();

        selectTimerInsertDialog.setDate(yearMonthDayFromDate(calendar));

    }
}