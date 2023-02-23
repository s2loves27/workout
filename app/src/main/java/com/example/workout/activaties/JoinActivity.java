package com.example.workout.activaties;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workout.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class JoinActivity extends AppCompatActivity {
    Button btnJoinDatePicker;


    Date curDate = new Date(); // 현재
    final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
    String result = dataFormat.format(curDate);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        btnJoinDatePicker = findViewById(R.id.btn_join_date_picker);

        btnJoinDatePicker.setText(result);

        btnJoinDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

    }

    private void showDateDialog(){
        Calendar calendar = Calendar.getInstance();
        try {
            curDate = dataFormat.parse(btnJoinDatePicker.getText().toString());

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Log.i("TEST" ,curDate.toString());

        calendar.setTime(curDate);


        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);

        Log.i("TEST" ,curYear +"년 " + curMonth + "월 " + curDay + "일");

        // 년,월,일 넘겨줄 변수
        DatePickerDialog dialog = new DatePickerDialog(JoinActivity.this,  birthDateSetListener,  curYear, curMonth, curDay);
        dialog.show();

    }


    final private DatePickerDialog.OnDateSetListener birthDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(Calendar.YEAR, year);
            selectedCalendar.set(Calendar.MONTH, month);
            selectedCalendar.set(Calendar.DAY_OF_MONTH, day);
            // 달력의 년월일을 버튼에서 넘겨받은 년월일로 설정

            Date curDate = selectedCalendar.getTime(); // 현재를 넘겨줌
            setSelectedDate(curDate);
        }

        private void setSelectedDate(Date curDate) {
            String selectedDateStr = dataFormat.format(curDate);
            btnJoinDatePicker.setText(selectedDateStr); // 버튼의 텍스트 수정
        }
    };
}