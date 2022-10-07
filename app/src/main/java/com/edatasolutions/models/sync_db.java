package com.edatasolutions.models;

import com.google.gson.annotations.SerializedName;

public class sync_db {

    @SerializedName("langId")
    String langId;

    @SerializedName("uuid")
    String uuid;

    @SerializedName("device_no")
    String device_no;

    @SerializedName("is_new_user")
    String  is_new_user;


    public String getIs_new_user() {
        return is_new_user;
    }

    public void setIs_new_user(String is_new_user) {
        this.is_new_user = is_new_user;
    }



    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }


    public sync_db(String langId,String uuid, String device_no, String is_new_user) {
        this.langId = langId;
        this.uuid = uuid;
        this.device_no = device_no;
        this.is_new_user = is_new_user;
    }


}
