package com.soti.workout.activaties;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.soti.workout.R;
import com.soti.workout.managers.PreferenceHelper;
import com.soti.workout.models.TokenCheckModel;
import com.soti.workout.restapi.ServerApiService;
import com.soti.workout.restapi.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {


    private final Callback<TokenCheckModel> TokenCheckCall = new Callback<TokenCheckModel>() {
        @Override
        public void onResponse(Call<TokenCheckModel> call, Response<TokenCheckModel> response) {
            if (response.isSuccessful()) {
                TokenCheckModel result = response.body();
                if (result != null) {
                    int code = result.getCode();

                    if(code == 1){
                        preferenceHelper.setToken(result.getAccess());
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(code == 2){
                        Toast.makeText(getApplicationContext(), getString(R.string.txt_splash_token_error), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(code == 3){
                        Toast.makeText(getApplicationContext(), getString(R.string.txt_splash_session_error), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), getString(R.string.txt_splash_db_error), Toast.LENGTH_SHORT).show();
                    }


                }
            } else if (response.code() == 400) {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_server_error_400), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            } else if (response.code() == 401) {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_server_error_401), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_splash_db_error), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<TokenCheckModel> call, Throwable t) {
            Toast.makeText(SplashActivity.this, getString(R.string.txt_error_internet), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    };


    PreferenceHelper preferenceHelper;
    ServerApiService serverApiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash);


        preferenceHelper = new PreferenceHelper(getApplicationContext());
        serverApiService = ServiceGenerator.createService(ServerApiService.class, "");
        serverApiService.tokenCheck(preferenceHelper.getToken()).enqueue(TokenCheckCall);



    }
}
