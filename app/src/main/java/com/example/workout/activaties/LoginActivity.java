package com.example.workout.activaties;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workout.R;
import com.google.android.material.textfield.TextInputEditText;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextView txtLoginSignIn;
    Button btnLoginJoin;
    ImageView ivLoginKako;

    CheckBox chk_login_status;

    TextInputEditText loginEditEmail;
    TextInputEditText loginEditPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("KeyHash", getKeyHash());

        txtLoginSignIn = findViewById(R.id.txt_login_sign_in);
        btnLoginJoin = findViewById(R.id.btn_login_join);
        ivLoginKako = findViewById(R.id.iv_login_kako);

        chk_login_status = findViewById(R.id.chk_login_status);

        loginEditEmail = findViewById(R.id.login_edit_email);
        loginEditPassword = findViewById(R.id.login_edit_password);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        txtLoginSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnLoginJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
                finish();
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
                Account user1 = user.getKakaoAccount();
                Log.i("TEST", "사용자 계정" + user1);

            }
            return null;
        });
    }
}
