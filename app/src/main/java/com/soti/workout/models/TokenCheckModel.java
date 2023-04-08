package com.soti.workout.models;

import com.google.gson.annotations.SerializedName;

public class TokenCheckModel {

    @SerializedName("detail")
    String detail;

    @SerializedName("code")
    String code;

    @SerializedName("access")
    String access;

    public String getAccess() {
        return access;
    }

    public String getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }
}
