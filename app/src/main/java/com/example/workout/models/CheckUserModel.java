package com.example.workout.models;

import com.google.gson.annotations.SerializedName;

public class CheckUserModel {

    @SerializedName("code")
    int code;

    @SerializedName("email")
    String email;

    @SerializedName("message")
    String message;

    public String getEmail() {
        return email;
    }


    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
