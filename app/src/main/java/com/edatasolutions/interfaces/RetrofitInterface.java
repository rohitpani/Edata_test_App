package com.edatasolutions.interfaces;

import com.edatasolutions.utils.ConstantCodes;
import com.edatasolutions.models.officer_token;
import com.edatasolutions.models.save_data_entry;
import com.edatasolutions.models.sync_db;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitInterface {

    String BASE_URL = "http://skyonliners.com/demo/edata/api/";

    @POST(ConstantCodes.CHECK_VERSION)
    @FormUrlEncoded
    Call<JsonObject> checkVersion(@Field("langId") String langId,@Field("uuid") String uuid,@Field("platform") String platform,@Field("version") String version);


    @POST(ConstantCodes.ADMIN_LOGIN)
    @FormUrlEncoded
    Call<JsonObject> adminLogin(@Field("langId") String langId, @Field("device_no") String device_no, @Field("username") String username,@Field("uuid") String uuid,@Field("password") String password,@Field("role_id") String role_id);

    @POST(ConstantCodes.USER_LIST)
    Call<JsonObject> getUserList(@Body sync_db sync_db);

    @POST(ConstantCodes.ROLE_LIST)
    Call<JsonObject> getRoleList(@Body sync_db sync_db);

    @POST(ConstantCodes.COMMON_CODES)
    @FormUrlEncoded
    Call<JsonObject> getCommonCodes(@Field("langId") String langId, @Field("device_no") String device_no, @Field("code_type") String code_type,  @Field("uuid") String uuid, @Field("is_new_user") String is_new_user);


    @POST(ConstantCodes.COMMON_FIELDS)
    @FormUrlEncoded
    Call<JsonObject> getCommonFields(@Field("langId") String langId, @Field("device_no") String device_no, @Field("field_type") String field_type, @Field("uuid") String uuid, @Field("is_new_user") String is_new_user);


    @POST(ConstantCodes.DIVISION_OFFICER_AREAS)
    Call<JsonObject> getDivisionOfficerAreas(@Body sync_db sync_db);

    @POST(ConstantCodes.NAME_MASTER)
    Call<JsonObject> getNameMaster(@Body sync_db sync_db);

    @POST(ConstantCodes.CITY_MASTER)
    Call<JsonObject> getCityMaster(@Body sync_db sync_db);

    @POST(ConstantCodes.STATE_MASTER)
    Call<JsonObject> getStateMaster(@Body sync_db sync_db);

    @POST(ConstantCodes.VEHICLE_MODELS)
    Call<JsonObject> getVehicleModels(@Body sync_db sync_db);

    @POST(ConstantCodes.VEHICLE_MAKE)
    Call<JsonObject> getVehicleMakes(@Body sync_db sync_db);

    @POST(ConstantCodes.VIOLATIONS)
    Call<JsonObject> getViolations(@Body sync_db sync_db);

    @POST(ConstantCodes.ZIP_CITY_STATE)
    Call<JsonObject> getZipCityStates(@Body sync_db sync_db);

    @POST(ConstantCodes.UPDATE_SYNC_TIME)
    @FormUrlEncoded
    Call<JsonObject> getUpdateSyncTime(@Field("langId") String langId, @Field("device_no") String device_no,  @Field("uuid") String uuid);

    @POST(ConstantCodes.SAVA_DATA_ENTRY)
    Call<JsonObject> setDataEntry(@Body save_data_entry save_data_entry);

    @POST(ConstantCodes.GET_TOKEN)
    Call<JsonObject> getToken(@Body officer_token officer_token);

    @POST(ConstantCodes.LAST_CITATION_NUM)
    @FormUrlEncoded
    Call<JsonObject> getLastCitationNo(@Field("langId") String langId, @Field("device_no") String device_no);
}