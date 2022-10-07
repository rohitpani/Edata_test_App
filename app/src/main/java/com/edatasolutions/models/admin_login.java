package com.edatasolutions.models;

import com.google.gson.annotations.SerializedName;

public class admin_login {

    @SerializedName("langId")
    String langId;

    @SerializedName("username")
    String username;

    @SerializedName("password")
    String password;


    @SerializedName("uuid")
    String uuid;


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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public admin_login(String langId, String username,String uuid, String password) {
        this.langId = langId;
        this.username = username;
        this.uuid = uuid;
        this.password = password;
    }


}
