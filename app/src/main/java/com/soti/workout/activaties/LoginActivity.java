package com.soti.workout.activaties;

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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soti.workout.R;
import com.soti.workout.managers.PreferenceHelper;
import com.soti.workout.models.TokenModel;
import com.soti.workout.restapi.ServerApiService;
import com.soti.workout.restapi.ServiceGenerator;
import com.google.android.material.textfield.TextInputEditText;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

    
    // 카카오 로그인 값
    String birthYear; 
    String birthDay;
    String email;
    String ageRange;
    String gender;
    String name;
    
    private final Callback<TokenModel> kakaoLogin = new Callback<TokenModel>() {
        @Override
        public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
            if (response.isSuccessful()) {
                TokenModel result = response.body();
                if (result != null) {
                    int code = result.getCode();
                    if(code == 1) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        preferenceHelper.setToken(result.getAccess());
                        preferenceHelper.setRefresh(result.getRefresh());
                        preferenceHelper.setUserId(result.getUser());

                        startActivity(intent);
                        finish();
                        return;
                    }
                }
            }else if(response.code() == 400){
                TokenModel result = response.body();
                if(result != null){
                    Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_join_error_400), Toast.LENGTH_SHORT).show();
                }
            }else if(response.code() == 500){
                Toast.makeText(getApplicationContext(), getString(R.string.txt_login_not_member), Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), getString(R.string.txt_join_error_server) , Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(LoginActivity.this, JoinActivity.class);

            intent.putExtra("birthYear", birthYear);
            intent.putExtra("birthYear", birthDay);
            intent.putExtra("email", email);
            intent.putExtra("ageRange", ageRange);
            intent.putExtra("gender", gender);
            intent.putExtra("name", name);

            startActivity(intent);
            finish();


//            Toast.makeText(MainActivity.this, "인터넷 연결 오류", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<TokenModel> call, Throwable t) {
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

                    Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();

                    preferenceHelper.setToken(result.getAccess());
                    preferenceHelper.setRefresh(result.getRefresh());
                    preferenceHelper.setUserId(result.getUserId());

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }else if(response.code() == 400){
                TokenModel result = response.body();
                if(result != null){
                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_join_error_400), Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), getString(R.string.txt_login_not_match) , Toast.LENGTH_SHORT).show();
            }
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_join_insert_email), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!emailPattern.matcher(email).matches()){
                    Toast.makeText(getApplicationContext(),  getString(R.string.txt_join_nomatch_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.equals("")){
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_join_insert_password), Toast.LENGTH_SHORT).show();
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
                Log.e("TEST", "kakakoTalk로그인 실패 \n" + throwable);
            }else{
                Log.i("TEST", "로그인 성공(토큰)" + oAuthToken.getAccessToken());

//                preferenceHelper.setToken(oAuthToken.getAccessToken());
//                preferenceHelper.setRefresh(oAuthToken.getRefreshToken());
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
//                preferenceHelper.setToken(oAuthToken.getAccessToken());
//                preferenceHelper.setRefresh(oAuthToken.getRefreshToken());
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




                birthYear = user.getKakaoAccount().getBirthyear();
                birthDay = user.getKakaoAccount().getBirthday();

                email = user.getKakaoAccount().getEmail();



                if(user.getKakaoAccount().getAgeRange() == null){
                    ageRange = "";
                }else{
                    ageRange = user.getKakaoAccount().getAgeRange().toString();
                }

                if(user.getKakaoAccount().getGender() == null){
                    gender = "";
                }else{
                    gender = user.getKakaoAccount().getGender().toString();
                }


                name = user.getKakaoAccount().getName();

                Log.i("TEST", "birthYear : " + birthYear);
                Log.i("TEST", "email : " + email);
                Log.i("TEST", "ageRange : " + ageRange);
                Log.i("TEST", "gender : " + gender);
                Log.i("TEST", "Name : " + name);


                serverApiService.kakaoLogin(user.getKakaoAccount().getEmail()).enqueue(kakaoLogin);



                Account user1 = user.getKakaoAccount();
                Log.i("TEST", "사용자 계정" + user1);

            }
            return null;
        });
    }
}
