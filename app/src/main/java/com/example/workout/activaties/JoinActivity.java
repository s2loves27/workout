package com.example.workout.activaties;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workout.R;
import com.example.workout.models.UserModel;
import com.example.workout.restapi.ServerApiService;
import com.example.workout.restapi.ServiceGenerator;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class JoinActivity extends AppCompatActivity {
    Button btnJoinDatePicker;
    TextInputEditText et_join_email;
    TextInputEditText et_join_name;
    RadioButton rb_man;
    RadioButton rb_woman;
    TextInputEditText et_join_password;
    TextInputEditText et_join_password2;
    CheckBox cb_join_agree_all;
    CheckBox cb_join_agree_1;
    CheckBox cb_join_agree_2;
    CheckBox cb_join_agree_3;
    Button btn_join;

    String selectedDateStr = "";

    ServerApiService serverApiService;
    Date curDate = new Date(); // 현재
    final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
    String result = dataFormat.format(curDate);

    private final Callback<UserModel> registerCallback = new Callback<UserModel>() {
        @Override
        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
            if (response.isSuccessful()) {
                UserModel result = response.body();
                if (result != null) {
                    int code = result.getCode();

                    if(code == 1) {

                        Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT).show();

                    }

                }

            } else{
                Toast.makeText(getApplicationContext(), "회원 가입이 되지 않았습니다. 확인해주세요.", Toast.LENGTH_SHORT).show();
            }

//            Toast.makeText(MainActivity.this, "인터넷 연결 오류", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<UserModel> call, Throwable t) {
            Toast.makeText(JoinActivity.this, getString(R.string.txt_error_internet), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
//            Util.dbInset(getApplicationContext(), t);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        btnJoinDatePicker = findViewById(R.id.btn_join_date_picker);
        et_join_email = findViewById(R.id.et_join_email);
        et_join_name = findViewById(R.id.et_join_name);
        rb_man = findViewById(R.id.rb_man);
        rb_woman = findViewById(R.id.rb_woman);
        et_join_password = findViewById(R.id.et_join_password);
        et_join_password2 = findViewById(R.id.et_join_password2);
        cb_join_agree_all = findViewById(R.id.cb_join_agree_all);
        cb_join_agree_1 = findViewById(R.id.cb_join_agree_1);
        cb_join_agree_2 = findViewById(R.id.cb_join_agree_2);
        cb_join_agree_3  = findViewById(R.id.cb_join_agree_3);
        btn_join = findViewById(R.id.btn_join);


        btnJoinDatePicker.setText(result);

        serverApiService = ServiceGenerator.createService(ServerApiService.class, "");

//        selectedDateStr = result;

        btnJoinDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        cb_join_agree_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb_join_agree_all.isChecked()){
                    cb_join_agree_1.setChecked(true);
                    cb_join_agree_2.setChecked(true);
                    cb_join_agree_3.setChecked(true);

                }else{
                    cb_join_agree_1.setChecked(false);
                    cb_join_agree_2.setChecked(false);
                    cb_join_agree_3.setChecked(false);
                }
            }
        });


        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pattern emailPattern = Patterns.EMAIL_ADDRESS;
                Pattern passwordPatten = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
                String email = et_join_email.getText().toString();
                if(email.equals("")){
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!emailPattern.matcher(email).matches()){
                    Toast.makeText(getApplicationContext(), "이메일 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String username = et_join_name.getText().toString();

                if(username.equals("")){
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(username.length() >= 10) {
                    Toast.makeText(getApplicationContext(), "이름의 최대 길이는 10자 입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean man = rb_man.isChecked();
                boolean woman = rb_woman.isChecked();
                String sex = "";
                if(man){
                    sex = "M";
                }
                if(woman){
                    sex = "W";
                }
                if(sex.equals("")){
                    Toast.makeText(getApplicationContext(), "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String password = et_join_password.getText().toString();
                if(password.equals("")){
                    Toast.makeText(getApplicationContext(), "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!passwordPatten.matcher(password).find()) {
                    Toast.makeText(getApplicationContext(), "패스워드 영문+특수문자+숫자 8자로 구성되어야 합니다..", Toast.LENGTH_SHORT).show();
                    return;
                }
                String password2 = et_join_password2.getText().toString();

                if(password2.equals("")){
                    Toast.makeText(getApplicationContext(), "패스워드 확인을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(password2)){
                    Toast.makeText(getApplicationContext(), "패스워드와 패스워드 확인이 다릅니다.", Toast.LENGTH_SHORT).show();
                    return;

                }


                if(selectedDateStr.equals("")){
                    Toast.makeText(getApplicationContext(), "생년월일을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;

                }

                if(!cb_join_agree_1.isChecked()){
                    Toast.makeText(getApplicationContext(), "개인 정보 수집 및 이용동의 항목을 체크해주세요.", Toast.LENGTH_SHORT).show();
                    return;

                }


                if(!cb_join_agree_2.isChecked()){
                    Toast.makeText(getApplicationContext(), "서비스 이용 약관 확인 및 동의를 체크해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                serverApiService.register(email, selectedDateStr, password, password2, sex, username).enqueue(registerCallback);




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
            selectedDateStr = dataFormat.format(curDate);
            btnJoinDatePicker.setText(selectedDateStr); // 버튼의 텍스트 수정
        }
    };
}
