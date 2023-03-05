package com.example.workout.models;

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
}
