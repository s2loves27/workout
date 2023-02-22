package com.example.workout.applications;


import android.app.Application;

import com.example.workout.BuildConfig;
import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 네이티브 앱 키로 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY);
    }
}
