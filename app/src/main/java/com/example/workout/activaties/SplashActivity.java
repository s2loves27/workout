package com.example.workout.activaties;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workout.R;
import com.example.workout.managers.PreferenceHelper;
import com.example.workout.models.CheckUserModel;
import com.example.workout.models.TokenCheckModel;
import com.example.workout.models.TokenModel;
import com.example.workout.restapi.ServerApiService;
import com.example.workout.restapi.ServiceGenerator;
import com.example.workout.services.TimerService;
import com.kakao.sdk.user.UserApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {


    private final Callback<TokenCheckModel> TokenRefreshCall = new Callback<TokenCheckModel>() {
        @Override
        public void onResponse(Call<TokenCheckModel> call, Response<TokenCheckModel> response) {
            if (response.isSuccessful()) {
                TokenCheckModel result = response.body();
                if (result != null) {

                    preferenceHelper.setToken(result.getAccess());
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            } else if (response.code() == 400) {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_splash_token_error), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            } else if (response.code() == 401) {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_splash_session_error), Toast.LENGTH_SHORT).show();
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


    private final Callback<TokenCheckModel> TokenCheckCall = new Callback<TokenCheckModel>() {
        @Override
        public void onResponse(Call<TokenCheckModel> call, Response<TokenCheckModel> response) {
            if (response.isSuccessful()) {
                TokenCheckModel result = response.body();
                if (result != null) {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            } else if (response.code() == 400) {
                serverApiService.tokenRefresh(preferenceHelper.getRefresh()).enqueue(TokenRefreshCall);
            } else if (response.code() == 401) {
                serverApiService.tokenRefresh(preferenceHelper.getRefresh()).enqueue(TokenRefreshCall);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.txt_splash_db_error), Toast.LENGTH_SHORT).show();
            }
//            Toast.makeText(MainActivity.this, "인터넷 연결 오류", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_splash);


        preferenceHelper = new PreferenceHelper(getApplicationContext());
        serverApiService = ServiceGenerator.createService(ServerApiService.class, "");
        serverApiService.tokenCheck(preferenceHelper.getToken()).enqueue(TokenCheckCall);



    }
}
