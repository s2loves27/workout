package com.soti.workout.models;

import com.google.gson.annotations.SerializedName;

public class TokenModel {

    @SerializedName("refresh")
    private String refresh;

    @SerializedName("access")
    private String access;

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int code;

    @SerializedName("user")
    private String user;

    @SerializedName("user_id")
    private String userId;



    public String getAccess() {
        return access;
    }

    public String getRefresh() {
        return refresh;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public String getUser() {
        return user;
    }

    public String getUserId() {
        return userId;
    }
}
