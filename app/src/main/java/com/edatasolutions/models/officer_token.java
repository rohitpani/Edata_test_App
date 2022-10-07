package com.edatasolutions.models;

import com.google.gson.annotations.SerializedName;

public class officer_token {

    @SerializedName("langId")
    String langId;

    @SerializedName("userId")
    String userId;

    @SerializedName("device_no")
    String device_no;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }

    public officer_token(String langId,  String userId, String device_no) {
        this.langId = langId;
        this.userId = userId;
        this.device_no = device_no;
    }


}
