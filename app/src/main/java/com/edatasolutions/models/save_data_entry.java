package com.edatasolutions.models;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.ArrayList;

public class save_data_entry {

    @SerializedName("langId")
    String langId;

    @SerializedName("uuid")
    String uuuid;

    @SerializedName("device_no")
    String device_no;

    @SerializedName("data")
    JsonArray dataList;



    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }


    public JsonArray getDataList() {
        return dataList;
    }

    public void setDataList(JsonArray dataList) {
        this.dataList = dataList;
    }

    public String getUuuid() {
        return uuuid;
    }

    public void setUuuid(String uuuid) {
        this.uuuid = uuuid;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }


    public save_data_entry(String langId,String uuuid,String device_no,  JsonArray dataList) {
        this.langId = langId;
        this.uuuid = uuuid;
        this.device_no = device_no;
        this.dataList = dataList;
    }


}
