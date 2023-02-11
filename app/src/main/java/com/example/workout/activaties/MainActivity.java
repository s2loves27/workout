package com.example.workout.activaty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.workout.R;
import com.example.workout.adapters.CalendarAdapter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // 년월 텍스트 뷰
    TextView monthYearText;

    // 년월 변수
    LocalDate selectedDate;


    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //초기화
        monthYearText = findViewById(R.id.monthYearText);
        ImageButton preBtn = findViewById(R.id.pre_btn);
        ImageButton nextBtn = findViewById(R.id.next_btn);
        recyclerView = findViewById(R.id.recyclerView);


        //현재 날짜
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = LocalDate.now();
        }

        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    selectedDate = selectedDate.minusMonths(1);
                }
                setMonthView();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    selectedDate = selectedDate.plusMonths(1);
                }
                setMonthView();
            }
        });


    }

    private String monthYearFromDate(LocalDate date) {
        String formatter = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = date.format(DateTimeFormatter.ofPattern("yyyy년 MM월"));

        }
        return formatter;
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> dayList = new ArrayList<>();


        int lastDay = 0;
        int dayOfWeek = 1;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            YearMonth yearMonth = YearMonth.from(date);

            //해당 월 마지막 날짜 가져오기(예: 28,29,30,31)
            lastDay = yearMonth.lengthOfMonth();

            //해당 월의 첫번째 날 가져오기(예: 4월 1일)
            LocalDate firstDay = selectedDate.withDayOfMonth(1);

            //첫번째 날 요일 가져오기(월 1, 일:7)
            dayOfWeek = firstDay.getDayOfWeek().getValue();
        }


        for (int i = 1; i < 42; i++) {
            if (i <= dayOfWeek || i > lastDay + dayOfWeek) {
                dayList.add("");

            } else {
                dayList.add(String.valueOf(i - dayOfWeek));
            }
        }
        return dayList;
    }


    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));

        ArrayList<String> dayList = daysInMonthArray(selectedDate);

        CalendarAdapter adapter = new CalendarAdapter(dayList);

        //레이아웃 설정 (열 7개)
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 7);

        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);
    }
}