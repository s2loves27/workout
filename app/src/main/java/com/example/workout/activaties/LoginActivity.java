package com.example.workout.activaties;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workout.R;
import com.example.workout.managers.PreferenceHelper;
import com.example.workout.models.CheckUserModel;
import com.example.workout.models.TokenModel;
import com.example.workout.models.UserModel;
import com.example.workout.restapi.ServerApiService;
import com.example.workout.restapi.ServiceGenerator;
import com.google.android.material.textfield.TextInputEditText;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView txtLoginSignIn;
    TextView txtLoginJoin;
    ImageView ivLoginKako;

    CheckBox chk_login_status;

    TextInputEditText loginEditEmail;
    TextInputEditText loginEditPassword;

    PreferenceHelper preferenceHelper;
    ServerApiService serverApiService;

    private final Callback<CheckUserModel> checkTokenCall = new Callback<CheckUserModel>() {
        @Override
        public void onResponse(Call<CheckUserModel> call, Response<CheckUserModel> response) {
            if (response.isSuccessful()) {
                CheckUserModel result = response.body();
                if (result != null) {
                    int code = result.getCode();
                    if(code == 1) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
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
//            Toast.makeText(MainActivity.this, "인터넷 연결 오류", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<CheckUserModel> call, Throwable t) {
            Toast.makeText(LoginActivity.this, getString(R.string.txt_error_internet), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    };


    private final Callback<TokenModel> loginCall = new Callback<TokenModel>() {
        @Override
        public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
            if (response.isSuccessful()) {
                TokenModel result = response.body();
                if (result != null) {
                    int code = result.getCode();
                    if(code == 1) {

                        Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();

                        preferenceHelper.setToken(result.getAccess());
                        preferenceHelper.setRefresh(result.getRefresh());
                        preferenceHelper.setLoginMethod("JUN");

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }else if(response.code() == 400){
                TokenModel result = response.body();
                if(result != null){
                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "400 통신 에러", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Email 또는 패스워드가 틀립니다 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
//            Toast.makeText(MainActivity.this, "인터넷 연결 오류", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<TokenModel> call, Throwable t) {
            Toast.makeText(LoginActivity.this, getString(R.string.txt_error_internet), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("KeyHash", getKeyHash());

        loginEditEmail = findViewById(R.id.login_edit_email);
        loginEditPassword = findViewById(R.id.login_edit_password);
        chk_login_status = findViewById(R.id.chk_login_status);


        txtLoginSignIn = findViewById(R.id.txt_login_sign_in);
        txtLoginJoin = findViewById(R.id.txt_login_join);
        ivLoginKako = findViewById(R.id.iv_login_kako);

        preferenceHelper = new PreferenceHelper(getApplicationContext());
        serverApiService = ServiceGenerator.createService(ServerApiService.class, "");

        if(chk_login_status.isChecked()){
            loginEditEmail.setText(preferenceHelper.getEmail());
        }else{
            loginEditEmail.setText("");
        }





        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        txtLoginSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pattern emailPattern = Patterns.EMAIL_ADDRESS;

                String email = loginEditEmail.getText().toString();
                String password = loginEditPassword.getText().toString();


                if(email.equals("")){
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!emailPattern.matcher(email).matches()){
                    Toast.makeText(getApplicationContext(), "이메일 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.equals("")){
                    Toast.makeText(getApplicationContext(), "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                serverApiService.login(email, password).enqueue(loginCall);




            }
        });

        txtLoginJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });

        ivLoginKako.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                    kakaoLogin();
                }else{
                    accountLogin();
                }
            }
        });

    }

    public String getKeyHash(){
        try{
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            if(packageInfo == null) return null;
            for(Signature signature: packageInfo.signatures){
                try{
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                }catch (NoSuchAlgorithmException e){
                    Log.w("getKeyHash", "Unable to get MessageDigest. signature="+signature, e);
                }
            }
        }catch(PackageManager.NameNotFoundException e){
            Log.w("getPackageInfo", "Unable to getPackageInfo");
        }
        return null;
    }

    public void kakaoLogin(){
        UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, ((oAuthToken, throwable) -> {
            if(throwable != null){
                Log.e("TEST", "kakakoTalk로그인 실패");
            }else{
                Log.i("TEST", "로그인 성공(토큰)" + oAuthToken.getAccessToken());

                preferenceHelper.setToken(oAuthToken.getAccessToken());
                preferenceHelper.setRefresh(oAuthToken.getRefreshToken());
                preferenceHelper.setLoginMethod("KAKAO");
                getUserInfo();

            }
            return null;
        }));
    }

    public void accountLogin(){
        UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, ((oAuthToken, throwable) -> {
            if(throwable != null){
                Log.e("TEST", "kakaoAccount 로그인 실패");
            }else{
                Log.i("TEST", "kakaoAccount 로그인 성공(토큰)" + oAuthToken.getAccessToken());
                preferenceHelper.setToken(oAuthToken.getAccessToken());
                preferenceHelper.setRefresh(oAuthToken.getRefreshToken());
                preferenceHelper.setLoginMethod("KAKAO");
                getUserInfo();
            }
            return null;
        }));
    }

    public void getUserInfo(){
        UserApiClient.getInstance().me((user, meError) -> {
            if(meError != null){
                Log.e("TEST", "사용자 정보 요청 실패", meError);
            }else{
                Log.i("TEST", "로그인 완료");
                {
                    Log.i("TEST", "사용자 정보 요청 성공"+
                            "\n회원번호: " + user.getId()+
                            "\n이메일 " + user.getKakaoAccount().getEmail());
                }



                serverApiService.checkUser(user.getKakaoAccount().getEmail()).enqueue(checkTokenCall);

                user.getKakaoAccount().getBirthyear();
                user.getKakaoAccount().getEmail();
                user.getKakaoAccount().getAgeRange();
                user.getKakaoAccount().getGender();
                user.getKakaoAccount().getName();







                Account user1 = user.getKakaoAccount();
                Log.i("TEST", "사용자 계정" + user1);

            }
            return null;
        });
    }
}
