package com.example.workout.models;

import com.google.gson.annotations.SerializedName;

public class TokenMessageModel {


    @SerializedName("token_class")
    int tokenClass;

    @SerializedName("token_type")
    String tokenType;

    @SerializedName("message")
    String message;


    public int getTokenClass() {
        return tokenClass;
    }

    public String getMessage() {
        return message;
    }

    public String getTokenType() {
        return tokenType;
    }
}
