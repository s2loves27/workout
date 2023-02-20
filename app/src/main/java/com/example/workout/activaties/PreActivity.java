package com.example.workout.activaties;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import com.example.workout.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

public class PreActivity extends AppCompatActivity {

    TextView txtPreSignIn;
    TextView txtPreSignUp;
    ImageFilterView txtPreKakaoSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre);

        txtPreSignIn = findViewById(R.id.txt_pre_sign_in);
        txtPreSignUp = findViewById(R.id.txt_pre_sign_up);
        txtPreKakaoSignUp = findViewById(R.id.txt_pre_kakao_sign_up);


        txtPreSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });

        txtPreSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        txtPreKakaoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
