package com.example.workout.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceHelper {


    private static final String USER_PREFERENCES = "com.example.workout";
    private static final String TOKEN = "com.example.workout.token";
    private static final String REFRESH = "com.example.workout.refresh";
    private static final String USER_ID = "com.example.workout.user_id";


    private static final String LOGIN_METHOD =  "com.example.workout.login.method";

    private static final String EMAIL = "com.example.workout.email";


    private final Context mContext;

    public PreferenceHelper(Context context){
        this.mContext = context;
    }
    public static Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(USER_PREFERENCES, 0);
    }


    public void setEmail(String email) {
        Editor editor = getEditor(this.mContext);
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public String getEmail() {
        return getSharedPreferences(this.mContext).getString(EMAIL, "");
    }


    public void setToken(String token) {
        Editor editor = getEditor(this.mContext);
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return getSharedPreferences(this.mContext).getString(TOKEN, "");
    }

    public void setRefresh(String refresh) {
        Editor editor = getEditor(this.mContext);
        editor.putString(REFRESH, refresh);
        editor.apply();
    }

    public String getRefresh() {
        return getSharedPreferences(this.mContext).getString(REFRESH, "");
    }

    public void setUserId(String userId) {
        Editor editor = getEditor(this.mContext);
        editor.putString(USER_ID, userId);
        editor.apply();
    }

    public String getUserId() {
        return getSharedPreferences(this.mContext).getString(USER_ID, "");
    }




    public void setLoginMethod(String loginMethod) {
        Editor editor = getEditor(this.mContext);
        editor.putString(LOGIN_METHOD, loginMethod);
        editor.apply();
    }

    public String getLoginMethod() {
        return getSharedPreferences(this.mContext).getString(LOGIN_METHOD, "");
    }
}
