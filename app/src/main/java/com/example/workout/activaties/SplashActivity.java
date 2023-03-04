package com.example.workout.activaties;

import android.content.Intent;
import android.os.Bundle;
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
import com.kakao.sdk.user.UserApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {


    private final Callback<CheckUserModel> checkUserCall = new Callback<CheckUserModel>() {
        @Override
        public void onResponse(Call<CheckUserModel> call, Response<CheckUserModel> response) {
            if (response.isSuccessful()) {
                CheckUserModel result = response.body();
                if (result != null) {
                    int code = result.getCode();
                    if(code == 1) {
                        UserApiClient.getInstance().accessTokenInfo((accessTokenInfo, throwable) -> {
                            if (throwable != null) {
                                Toast.makeText(SplashActivity.this, "토큰 정보 보기 실패: " + throwable, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();


                            } else if (accessTokenInfo != null) {
                                Toast.makeText(SplashActivity.this, "카카오 로그인 성공: ", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            return null;
                        });
                        return;
                    }
                }
            }else if(response.code() == 400){
                CheckUserModel result = response.body();
                if(result != null){
                    Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "400 통신 에러", Toast.LENGTH_SHORT).show();
                }
            }else if(response.code() == 500){
                Toast.makeText(getApplicationContext(), "회원이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Email 또는 패스워드가 틀립니다 확인해주세요.", Toast.LENGTH_SHORT).show();
            }


            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();



//            Toast.makeText(MainActivity.this, "인터넷 연결 오류", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<CheckUserModel> call, Throwable t) {
            Toast.makeText(SplashActivity.this, getString(R.string.txt_error_internet), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    };

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
                Toast.makeText(getApplicationContext(), "발급 받은 토큰이 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            } else if (response.code() == 401) {
                Toast.makeText(getApplicationContext(), "세션이 완료 되었습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "DB에 문제가 있습니다 관리자에게 연락 부탁드립니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "DB에 문제가 있습니다 관리자에게 연락 부탁드립니다.", Toast.LENGTH_SHORT).show();
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
