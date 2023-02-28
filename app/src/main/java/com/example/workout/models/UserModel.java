package com.example.workout.models;

import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("email")
    private String email;

    @SerializedName("date_of_birth")
    private String date_of_birth;

    @SerializedName("sex")
    private String sex;

    @SerializedName("username")
    private String username;

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int code;

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getEmail() {
        return email;
    }

    public String getSex() {
        return sex;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
