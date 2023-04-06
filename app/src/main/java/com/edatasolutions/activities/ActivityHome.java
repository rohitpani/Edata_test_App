package com.edatasolutions.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edatasolutions.BuildConfig;
import com.edatasolutions.R;
import com.edatasolutions.database.DatabaseAccess;
import com.edatasolutions.interfaces.RetrofitInterface;
import com.edatasolutions.models.officer_token;
import com.edatasolutions.models.save_data_entry;
import com.edatasolutions.models.sync_db;
import com.edatasolutions.utils.ConstantCodes;
import com.edatasolutions.utils.NetworkConectivity;
import com.edatasolutions.utils.SessionManager;
import com.edatasolutions.zebraPrinterUtils.DemoSleeper;
import com.edatasolutions.zebraPrinterUtils.SettingsHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.SGD;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.ZebraPrinterLinkOs;
import com.zebra.sdk.printer.discovery.BluetoothDiscoverer;
import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
import com.zebra.sdk.printer.discovery.DiscoveryHandler;

import androidx.core.app.ActivityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityHome extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView img_menu,img_back;
    private LinearLayout header_lay,driver_lay,vehicle_lay,violation_lay,violationmisc_lay;
    private SessionManager sessionManager;
    private JsonArray resultSet = new JsonArray();
    private String uniqueId = "", user_id="";
    private String device_no = "";
    private Button save_btn,clear_btn;
    private ImageView header_check_img,driver_check_img,vehicle_check_img,violation_check_img,violationmisc_check_img;
    private String header_checkmark= "N",driver_checkmark= "N",vehicle_checkmark= "N",violation_checkmark= "N",violationmisc_checkmark= "N";

    private ArrayList<String> usersList = new ArrayList<>();
    private ArrayList<String> roleList = new ArrayList<>();
    private ArrayList<String> commoncodesList = new ArrayList<>();
    private ArrayList<String> commonfieldsList = new ArrayList<>();
    private ArrayList<String> division_officer_areaList = new ArrayList<>();
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayList<String> stateList = new ArrayList<>();
    private ArrayList<String> vehicleMakeList = new ArrayList<>();
    private ArrayList<String> vehicleModelList = new ArrayList<>();
    private ArrayList<String> violationList = new ArrayList<>();
    private ArrayList<String> zipCityStatesList = new ArrayList<>();

    private String LangId = "", TOKEN_VALUE="",UniqueId ="";
    private String code_type = "";
    private String field_type = "";

    private ZebraPrinter printer;
    private Connection connection;
    private static final String bluetoothAddressKey = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";
    private static final String tcpAddressKey = "ZEBRA_DEMO_TCP_ADDRESS";
    private static final String tcpPortKey = "ZEBRA_DEMO_TCP_PORT";
    private static final String PREFS_NAME = "OurSavedAddress";
    private SharedPreferences settings;

    @Override
    public void onAttachedToWindow() {
    //    super.onAttachedToWindow();
//        Window wind;
//        wind = ActivityHome.this.getWindow();
//        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }



    @Override
    public void onBackPressed() {
        finish();
      //  super.onBackPressed();
    }

    private void getAllDBdata(){

        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
        databaseAccess.open();

        usersList = databaseAccess.getUsersData();
        roleList = databaseAccess.getRolesData();
        commoncodesList = databaseAccess.getCommonCodes();
        commonfieldsList= databaseAccess.getCommonFields();
        division_officer_areaList = databaseAccess.getDivisionOfficerAreas();
        nameList = databaseAccess.getNameMaster();
        cityList = databaseAccess.getCityMaster();
        stateList= databaseAccess.getStateMaster();
        vehicleMakeList = databaseAccess.getVehicleMakeList();
        vehicleModelList = databaseAccess.getVehicleModelList();
        violationList = databaseAccess.getViolationList();
        zipCityStatesList = databaseAccess.getZipCityStateList();

        databaseAccess.close();
    }


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        sessionManager = new SessionManager(ActivityHome.this);


        init();
        getAllDBdata();
        getAllCitationNumber();


        if (NetworkConectivity.checkConnectivity(ActivityHome.this)){

            LangId = ConstantCodes.LANG_ID;
            UniqueId = UUID.randomUUID().toString();
            String versionName = BuildConfig.VERSION_NAME;
            String platform="android";
            checkVersion(UniqueId,platform,versionName);
            callGetToken();

        }


    }

    @SuppressLint("HardwareIds")
    private void init(){

        clear_btn = findViewById(R.id.clear_btn);
        img_menu = findViewById(R.id.img_menu);
//        img_back = findViewById(R.id.img_back);
        header_lay = findViewById(R.id.header_lay);
        driver_lay = findViewById(R.id.driver_lay);
        vehicle_lay = findViewById(R.id.vehicle_lay);
        violation_lay = findViewById(R.id.violation_lay);
        violationmisc_lay = findViewById(R.id.violationmisc_lay);
        save_btn = findViewById(R.id.save_btn);
        header_check_img = findViewById(R.id.header_check_img);
        driver_check_img = findViewById(R.id.driver_check_img);
        vehicle_check_img = findViewById(R.id.vehicle_check_img);
        violation_check_img = findViewById(R.id.violation_check_img);
        violationmisc_check_img = findViewById(R.id.violationmisc_check_img);
        settings = getSharedPreferences(PREFS_NAME, 0);

        header_checkmark = sessionManager.getHeader().get(SessionManager.HEADER_SECTION);
        driver_checkmark = sessionManager.getDriver().get(SessionManager.DRIVER_SECTION);
        vehicle_checkmark = sessionManager.getVehicle().get(SessionManager.VEHICLE_SECTION);
        violation_checkmark = sessionManager.getViolation().get(SessionManager.VIOLATION_SECTION);
        violationmisc_checkmark = sessionManager.getViolationMisc().get(SessionManager.VIOLATIONMISC_SECTION);

        if (header_checkmark.equals("Y")){
            header_check_img.setVisibility(View.VISIBLE);
            clear_btn.setVisibility(View.VISIBLE);
        }else {
            header_check_img.setVisibility(View.GONE);
            clear_btn.setVisibility(View.GONE);
        }

        if (driver_checkmark.equals("Y")){
            driver_check_img.setVisibility(View.VISIBLE);
        }else {
            driver_check_img.setVisibility(View.GONE);
        }

        if (vehicle_checkmark.equals("Y")){
            vehicle_check_img.setVisibility(View.VISIBLE);
        }else {
            vehicle_check_img.setVisibility(View.GONE);
        }

        if (violation_checkmark.equals("Y")){
            violation_check_img.setVisibility(View.VISIBLE);
        }else {
            violation_check_img.setVisibility(View.GONE);
        }

        if (violationmisc_checkmark.equals("Y")){
            violationmisc_check_img.setVisibility(View.VISIBLE);
        }else {
            violationmisc_check_img.setVisibility(View.GONE);
        }

      //  uniqueId = ;
        device_no = Settings.Secure.getString(ActivityHome.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        user_id = sessionManager.getAdminLogin().get(SessionManager.USER_ID);

//        img_back.setVisibility(View.INVISIBLE);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:


                                sessionManager.logoutUser();
                                sessionManager.ClearHeaderEntery();
                                sessionManager.ClearDriverEntery();
                                sessionManager.ClearVehicleEntery();
                                sessionManager.ClearViolationEntery();
                                sessionManager.ClearViolationMiscEntery();
                                final Intent mainIntent = new Intent(ActivityHome.this, ActivityLogin.class);
                                startActivity(mainIntent);
                                finish();


                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHome.this);
                builder.setMessage("Do you want to Logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }

        });


        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){

                            case DialogInterface.BUTTON_POSITIVE:

                                final ProgressDialog progressdialog = new ProgressDialog(ActivityHome.this,
                                        AlertDialog.THEME_HOLO_LIGHT);
                                progressdialog.setMessage("Please wait ...");
                                progressdialog.show();

                                sessionManager.ClearDataEntery();
                                sessionManager.ClearHeaderEntery();
                                sessionManager.ClearDriverEntery();
                                sessionManager.ClearVehicleEntery();
                                sessionManager.ClearViolationEntery();
                                sessionManager.ClearViolationMiscEntery();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        final Intent mainIntent = new Intent(ActivityHome.this, ActivityHome.class);
                                        startActivity(mainIntent);
                                        finish();

                                    }

                                }, 1000);

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHome.this);
                builder.setMessage("Do you want to clear this form?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }

        });

        header_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getCitationNum();
            }
        });

        driver_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (header_checkmark.equals("Y")){
                    Intent i = new Intent(ActivityHome.this, ActivityDriver.class);
                    i.putExtra("selected","driver");
                    startActivity(i);
                   // finish();
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                }

            }
        });

        vehicle_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (driver_checkmark.equals("Y")){
                    Intent i = new Intent(ActivityHome.this, ActivityVehicle.class);
                    i.putExtra("selected","vehicle");
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                }

            }
        });

        violation_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vehicle_checkmark.equals("Y")){
                    Intent i = new Intent(ActivityHome.this, ActivityViolation.class);
                    i.putExtra("selected","violation");
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                }

            }
        });

        violationmisc_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (violation_checkmark.equals("Y")){
                    Intent i = new Intent(ActivityHome.this, ActivityViolationMisc.class);
                    i.putExtra("selected","violationmisc");
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                }
            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (violationmisc_checkmark.equals("Y")){

                    if (Objects.equals(sessionManager.getHeaderSession().get(SessionManager.OWNER_RESPONSIBILITY), "N")){

                        String owner_firstname=sessionManager.getDriverSession().get(SessionManager.DRIVER_FIRST_NAME);
                        String owner_middlename=sessionManager.getDriverSession().get(SessionManager.DRIVER_MIDDLE_NAME);
                        String owner_lastname=sessionManager.getDriverSession().get(SessionManager.DRIVER_LAST_NAME);
                        String owner_address1=sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS1);
                        String owner_address2=sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS2);
                        String owner_zipcode=sessionManager.getDriverSession().get(SessionManager.DRIVER_ZIPCODE);
                        String suffix = sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX);
                        String suffixid = sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX_ID);
                        String city = sessionManager.getDriverSession().get(SessionManager.DRIVER_CITY);
                        String city_id = sessionManager.getDriverSession().get(SessionManager.DRIVER_CITY_ID);
                        String state = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE);
                        String state_id = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE_ID);

                        sessionManager.createOwnerVehicleSession(owner_firstname,owner_middlename,owner_lastname,suffix,suffixid,owner_address1,owner_address2,city,city_id,state,state_id,owner_zipcode);

                        Intent i = new Intent(ActivityHome.this, ActivityPreview.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter,R.anim.exit);

                    }else {

                        boolean res;
                        res = (sessionManager.getOwnerVehicleSession().get(SessionManager.OWNER_FIRST_NAME) == null);
                        if (res){
                            Toast.makeText(ActivityHome.this,"Please fill owner details",Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(ActivityHome.this, ActivityVehicle.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.enter,R.anim.exit);
                        }else {

                            String owner_firstname = sessionManager.getVehicleSession().get(SessionManager.OWNER_FIRST_NAME);
                            String owner_middlename = sessionManager.getVehicleSession().get(SessionManager.OWNER_MIDDLE_NAME);
                            String owner_lastname = sessionManager.getVehicleSession().get(SessionManager.OWNER_LAST_NAME);
                            String owner_address1 = sessionManager.getVehicleSession().get(SessionManager.OWNER_ADDRESS1);
                            String owner_address2 = sessionManager.getVehicleSession().get(SessionManager.OWNER_ADDRESS2);
                            String owner_zipcode = sessionManager.getVehicleSession().get(SessionManager.OWNER_ZIPCODE);
                            String suffix = sessionManager.getVehicleSession().get(SessionManager.OWNER_SUFFIX);
                            String suffixid = sessionManager.getVehicleSession().get(SessionManager.OWNER_SUFFIX_ID);
                            String city = sessionManager.getVehicleSession().get(SessionManager.OWNER_CITY);
                            String city_id = sessionManager.getVehicleSession().get(SessionManager.OWNER_CITY_ID);
                            String state = sessionManager.getVehicleSession().get(SessionManager.OWNER_STATE);
                            String state_id = sessionManager.getVehicleSession().get(SessionManager.OWNER_STATE_ID);


                            sessionManager.createOwnerVehicleSession(owner_firstname,owner_middlename,owner_lastname,suffix,suffixid,owner_address1,owner_address2,city,city_id,state,state_id,owner_zipcode);


                        Intent i = new Intent(ActivityHome.this, ActivityPreview.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter,R.anim.exit);
                        }


                    }


                }
            }
        });
    }


    private void getAllCitationNumber(){

        String user_id = sessionManager.getAdminLogin().get(SessionManager.USER_ID);

        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
        databaseAccess.open();

        resultSet = databaseAccess.getResults(ActivityHome.this,user_id);
        Log.d("TAG_NAME123",""+resultSet);
        databaseAccess.close();


    }

    private void callUpdateSyncDB() {

//        final ProgressDialog progressdialog = new ProgressDialog(ActivityHome.this);
//        progressdialog.setMessage("Loading....");
//        progressdialog.show();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        save_data_entry save_data_entry = new save_data_entry(ConstantCodes.LANG_ID,UUID.randomUUID().toString(),device_no,resultSet);
        Call<JsonObject> call = api.setDataEntry(save_data_entry);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

//                progressdialog.dismiss();
                //String s = null;
                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));


                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));
                        JSONObject userdata = jsonArray.getJSONObject(0);

                        String synced_citation_no= userdata.getString("synced_citation_no");
                        String check = synced_citation_no .replace("[", "(").replace("]", ")");

                        //Toast.makeText(getApplicationContext(),check,Toast.LENGTH_LONG).show();

                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                        databaseAccess.open();
                        databaseAccess.UpdateDatatoDataPasses(check);
                        databaseAccess.close();


                        if (usersList.isEmpty()||usersList.equals("")){
                            String is_new_user = "Y";
                            callUserList(LangId, UniqueId,is_new_user);
                        }else {
                            String is_new_user = "N";
                            callUserList(LangId, UniqueId,is_new_user);
                        }



                    }else {
                        if (usersList.isEmpty()||usersList.equals("")){
                            String is_new_user = "Y";
                            callUserList(LangId, UniqueId,is_new_user);
                        }else {
                            String is_new_user = "N";
                            callUserList(LangId, UniqueId,is_new_user);
                        }
                    }




                } catch (Exception ex) {
//                    progressdialog.dismiss();
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                progressdialog.dismiss();
           //     Toast.makeText(ActivityHome.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }

    private void callGetToken() {

//        final ProgressDialog progressdialog = new ProgressDialog(ActivityHome.this);
//        progressdialog.setMessage("Loading....");
//        progressdialog.show();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        officer_token officer_token = new officer_token(ConstantCodes.LANG_ID,user_id,device_no);
        Call<JsonObject> call = api.getToken(officer_token);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

//                progressdialog.dismiss();
                //String s = null;
                Log.e("status", response.toString());
                assert response.body() != null;

                Log.e("data", "true" + response);

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));

                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));
                        JSONObject userdata = jsonArray.getJSONObject(0);

                        TOKEN_VALUE = userdata.getString("token");
                        Log.e("token",TOKEN_VALUE);

                        if (resultSet.size()!=0){
                            callUpdateSyncDB();
                        }else {
                            if (usersList.isEmpty()||usersList.equals("")){
                                String is_new_user = "Y";
                                callUserList(LangId,UniqueId,is_new_user);
                            }else {
                                String is_new_user = "N";
                                callUserList(LangId,UniqueId,is_new_user);
                            }
                        }

                    }else {
                        Log.d("data t", "Something is wrong");
                        //Toast.makeText(ActivityHome.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                    }




                } catch (Exception ex) {
//                    progressdialog.dismiss();
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                progressdialog.dismiss();
              //  Toast.makeText(ActivityHome.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }


    private void callUserList(final  String id, final  String uuid, final String is_new_user) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        sync_db sync_db = new sync_db(id,uuid,device_no,is_new_user);

        Call<JsonObject> call = api.getUserList(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));

                    Log.d("radhika_log1",totaldata.toString());
                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);

                            Log.e("check3", "" + userdata.getString("name"));

                            String id = userdata.getString("id");
                            String name = userdata.getString("name");
                            String badge_no =userdata.getString("badge_no");
                            String email =userdata.getString("email");
                            String password =userdata.getString("password");
                            String role_id =userdata.getString("role_id");
                            String citation_series_from =userdata.getString("citation_series_from");
                            String citation_series_to =userdata.getString("citation_series_to");
                            String access_token="";
                            if (userdata.has("access_token")){
                                if (userdata.getString("access_token").isEmpty()){
                                    access_token = "";
                                }else {
                                    access_token = userdata.getString("access_token");
                                }
                            }

                            String mkey = "";
                            if (userdata.has("mkey")){
                                if (userdata.getString("mkey").isEmpty()){
                                    mkey = "";
                                }else {
                                    mkey = userdata.getString("mkey");
                                }
                            }

                            String msalt = "";
                            if (userdata.has("msalt")){
                                if (userdata.getString("msalt").isEmpty()){
                                    msalt = "";
                                }else {
                                    msalt = userdata.getString("msalt");
                                }
                            }

                            String last_sync_datetime ="";

                            if (userdata.has("last_sync_datetime")){
                                if (userdata.getString("last_sync_datetime").isEmpty()){
                                    last_sync_datetime = "0000-00-00 00:00:00";
                                }else {
                                    last_sync_datetime = userdata.getString("last_sync_datetime");
                                }
                            }

                            String status = userdata.getString("status");
                            String last_login = userdata.getString("last_login");
                            String fpwd_flag = userdata.getString("fpwd_flag");

                            String last_pwd_update_time = "";

                            if (userdata.has("last_pwd_update_time")){
                                if (userdata.getString("last_pwd_update_time").isEmpty()){
                                    last_pwd_update_time = "0000-00-00 00:00:00";
                                }else {
                                    last_pwd_update_time = userdata.getString("last_pwd_update_time");
                                }
                            }

                            String created_by = "";

                            if (userdata.has("created_by")){
                                if (userdata.getString("created_by").isEmpty()){
                                    created_by = "0";
                                }else {
                                    created_by = userdata.getString("created_by");
                                }
                            }

                            String updated_by = "";

                            if (userdata.has("updated_by")){
                                if (userdata.getString("updated_by").isEmpty()){
                                    updated_by = "0";
                                }else {
                                    updated_by = userdata.getString("updated_by");
                                }
                            }

                            String created_at = "";

                            if (userdata.has("created_at")){
                                if (userdata.getString("created_at").isEmpty()){
                                    created_at = "0000-00-00 00:00:00";
                                }else {
                                    created_at = userdata.getString("created_at");
                                }
                            }

                            String updated_at = "";

                            if (userdata.has("updated_at")){
                                if (userdata.getString("updated_at").isEmpty()){
                                    updated_at = "0000-00-00 00:00:00";
                                }else {
                                    updated_at = userdata.getString("updated_at");
                                }
                            }


                            String operation = userdata.getString("operation");


                            DatabaseAccess databaseAccess1 = new DatabaseAccess(ActivityHome.this);
                            databaseAccess1.open();

                            String starting_no = databaseAccess1.getStartingCitationNumber(user_id);
                            databaseAccess1.close();
                            String new_citation="";
                            if (Integer.parseInt(citation_series_from)>Integer.parseInt(starting_no)){
                                 new_citation=citation_series_from;
                            }else {
                                new_citation=starting_no;
                            }
                            Log.e("checkcitation",new_citation);
                            if(operation.equals("update")){
                                Log.d("MainActivity","update_userlist");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoUsers(id,name,badge_no,email,password,role_id,new_citation, citation_series_to,  access_token, mkey,  msalt,  last_sync_datetime,  status,  last_login, fpwd_flag,  last_pwd_update_time,  created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else if(operation.equals("insert")) {
                                Log.d("MainActivity","insert_userlist");

                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoUsers(id,name,badge_no,email,password,role_id,new_citation, citation_series_to,  access_token, mkey,  msalt,  last_sync_datetime,  status,  last_login, fpwd_flag,  last_pwd_update_time,  created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else {
                                Toast.makeText(ActivityHome.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }


                        if (roleList.isEmpty()||roleList.equals("")){
                            String is_new_user = "Y";
                            callRoleList(LangId,UniqueId,is_new_user);
                        }else {
                            String is_new_user = "N";
                            callRoleList(LangId,UniqueId,is_new_user);
                        }


                        // callCommonCodes(LangId, code_type,UniqueId);

                    }
                    if(totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")){

                            if (roleList.isEmpty()||roleList.equals("")){
                                String is_new_user = "Y";
                                callRoleList(LangId,UniqueId,is_new_user);
                            }else {
                                String is_new_user = "N";
                                callRoleList(LangId,UniqueId,is_new_user);
                            }

                            //callCommonCodes(LangId, code_type,UniqueId);
                        }

                    }else if (totaldata.getString("success").equals("4")) {

                        Log.d("msgg",totaldata.getString("message"));
//                        sessionManager.logoutUser();
//                        final Intent mainIntent = new Intent(ActivityHome.this, ActivityLogin.class);
//                        startActivity(mainIntent);
//                        finish();

                    }else {
                        Log.d("msgg",totaldata.getString("message"));
 //                       Toast.makeText(ActivityHome.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }




                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                //  Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }


    private void callRoleList( final  String id, final  String uuid, final String is_new_user) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        sync_db sync_db = new sync_db(id,uuid,device_no,is_new_user);
        Call<JsonObject> call = api.getRoleList(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log2",totaldata.toString());
                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String user_role = userdata.getString("user_role");
                            String status = userdata.getString("status");

                            String created_by = "";

                            if (userdata.has("created_by")){
                                if (userdata.getString("created_by").isEmpty()){
                                    created_by = "0";
                                }else {
                                    created_by = userdata.getString("created_by");
                                }
                            }

                            String updated_by = "";

                            if (userdata.has("updated_by")){
                                if (userdata.getString("updated_by").isEmpty()){
                                    updated_by = "0";
                                }else {
                                    updated_by = userdata.getString("updated_by");
                                }
                            }

                            String created_at = "";

                            if (userdata.has("created_at")){
                                if (userdata.getString("created_at").isEmpty()){
                                    created_at = "0000-00-00 00:00:00";
                                }else {
                                    created_at = userdata.getString("created_at");
                                }
                            }

                            String updated_at = "";

                            if (userdata.has("updated_at")){
                                if (userdata.getString("updated_at").isEmpty()){
                                    updated_at = "0000-00-00 00:00:00";
                                }else {
                                    updated_at = userdata.getString("updated_at");
                                }
                            }


                            String operation = userdata.getString("operation");

                            if(operation.equals("update")){
                                Log.d("MainActivity","update_rolelist");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoRoles(id,user_role,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else if(operation.equals("insert")) {
                                Log.d("MainActivity","insert_rolelist");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoRoles(id,user_role,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else {
                                Toast.makeText(ActivityHome.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }



                        }

                        if (commoncodesList.isEmpty()||commoncodesList.equals("")){
                            String is_new_user = "Y";
                            callCommonCodes(LangId, code_type,UniqueId,is_new_user);
                        }else {
                            String is_new_user = "N";
                            callCommonCodes(LangId, code_type,UniqueId,is_new_user);
                        }


                    }else if(totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")){


                            if (commoncodesList.isEmpty()||commoncodesList.equals("")){
                                String is_new_user = "Y";
                                callCommonCodes(LangId, code_type,UniqueId,is_new_user);
                            }else {
                                String is_new_user = "N";
                                callCommonCodes(LangId, code_type,UniqueId,is_new_user);
                            }
                        }
                    }else {
   //                     Toast.makeText(ActivityHome.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }




                } catch (Exception ex) {

                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }

    private void callCommonCodes(final String id, final String code_type, final  String uuid, final String is_new_user) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        Call<JsonObject> call = api.getCommonCodes(id,device_no,code_type,uuid,is_new_user);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log3",totaldata.toString());
                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String code_type = userdata.getString("code_type");
                            String code_value = userdata.getString("code_value");
                            String code_description = userdata.getString("code_description");
                            String status = userdata.getString("status");

                            String created_by = "";

                            if (userdata.has("created_by")){
                                if (userdata.getString("created_by").isEmpty()){
                                    created_by = "0";
                                }else {
                                    created_by = userdata.getString("created_by");
                                }
                            }

                            String updated_by = "";

                            if (userdata.has("updated_by")){
                                if (userdata.getString("updated_by").isEmpty()){
                                    updated_by = "0";
                                }else {
                                    updated_by = userdata.getString("updated_by");
                                }
                            }

                            String created_at = "";

                            if (userdata.has("created_at")){
                                if (userdata.getString("created_at").isEmpty()){
                                    created_at = "0000-00-00 00:00:00";
                                }else {
                                    created_at = userdata.getString("created_at");
                                }
                            }

                            String updated_at = "";

                            if (userdata.has("updated_at")){
                                if (userdata.getString("updated_at").isEmpty()){
                                    updated_at = "0000-00-00 00:00:00";
                                }else {
                                    updated_at = userdata.getString("updated_at");
                                }
                            }


                            String operation = userdata.getString("operation");

                            if(operation.equals("update")){
                                Log.d("MainActivity","update_commoncodes");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoCommonCodes(id,code_type,code_value,code_description,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else if(operation.equals("insert")) {
                                Log.d("MainActivity","insert_commoncodes");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoCommonCodes(id,code_type,code_value,code_description,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else {
                                Toast.makeText(ActivityHome.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }



                        }

                        if (commonfieldsList.isEmpty()||commonfieldsList.equals("")){
                            String is_new_user = "Y";
                            callCommonFields(LangId,field_type,UniqueId,is_new_user);
                        }else {
                            String is_new_user = "N";
                            callCommonFields(LangId,field_type,UniqueId,is_new_user);
                        }

                    }else if(totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")){

                            if (commonfieldsList.isEmpty()||commonfieldsList.equals("")){
                                String is_new_user = "Y";
                                callCommonFields(LangId,field_type,UniqueId,is_new_user);
                            }else {
                                String is_new_user = "N";
                                callCommonFields(LangId,field_type,UniqueId,is_new_user);
                            }
                        }
                    }else {
 //                       Toast.makeText(ActivityHome.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }




                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }

    private void callCommonFields(final String id,final String field_type, final String uuid,  final String is_new_user) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        Call<JsonObject> call = api.getCommonFields(id,device_no,field_type, uuid,is_new_user);


        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log4",totaldata.toString());
                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String field_type = userdata.getString("field_type");
                            String field_value = userdata.getString("field_value");
                            String status = userdata.getString("status");

                            String created_by = "";

                            if (userdata.has("created_by")) {
                                if (userdata.getString("created_by").isEmpty()) {
                                    created_by = "0";
                                } else {
                                    created_by = userdata.getString("created_by");
                                }
                            }

                            String updated_by = "";

                            if (userdata.has("updated_by")) {
                                if (userdata.getString("updated_by").isEmpty()) {
                                    updated_by = "0";
                                } else {
                                    updated_by = userdata.getString("updated_by");
                                }
                            }

                            String created_at = "";

                            if (userdata.has("created_at")) {
                                if (userdata.getString("created_at").isEmpty()) {
                                    created_at = "0000-00-00 00:00:00";
                                } else {
                                    created_at = userdata.getString("created_at");
                                }
                            }

                            String updated_at = "";

                            if (userdata.has("updated_at")) {
                                if (userdata.getString("updated_at").isEmpty()) {
                                    updated_at = "0000-00-00 00:00:00";
                                } else {
                                    updated_at = userdata.getString("updated_at");
                                }
                            }


                            String operation = userdata.getString("operation");

                            if (operation.equals("update")) {
                                Log.d("MainActivity","update_commonfields");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoCommonFields(id, field_type, field_value, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                Log.d("MainActivity","insert_commonfields");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoCommonFields(id, field_type, field_value, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(ActivityHome.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (division_officer_areaList.isEmpty()||division_officer_areaList.equals("")){
                            String is_new_user = "Y";
                            callDivisionOfficerAreas(LangId,UniqueId,is_new_user);
                        }else {
                            String is_new_user = "N";
                            callDivisionOfficerAreas(LangId,UniqueId,is_new_user);
                        }


                    }else if(totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")){

                            if (division_officer_areaList.isEmpty()||division_officer_areaList.equals("")){
                                String is_new_user = "Y";
                                callDivisionOfficerAreas(LangId,UniqueId,is_new_user);
                            }else {
                                String is_new_user = "N";
                                callDivisionOfficerAreas(LangId,UniqueId,is_new_user);
                            }
                        }
                    }else {
//                        Toast.makeText(ActivityHome.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }




                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }

    private void callDivisionOfficerAreas(final String id, final String uuid, final String is_new_user) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        sync_db sync_db = new sync_db(id, uuid,device_no,is_new_user);
        Call<JsonObject> call = api.getDivisionOfficerAreas(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log5",totaldata.toString());
                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String division_code = userdata.getString("division_code");
                            String division_desc = userdata.getString("division_desc");
                            String content = userdata.getString("content");
                            String office_area = userdata.getString("office_area");
                            String status = userdata.getString("status");

                            String created_by = "";

                            if (userdata.has("created_by")){
                                if (userdata.getString("created_by").isEmpty()){
                                    created_by = "0";
                                }else {
                                    created_by = userdata.getString("created_by");
                                }
                            }

                            String updated_by = "";

                            if (userdata.has("updated_by")){
                                if (userdata.getString("updated_by").isEmpty()){
                                    updated_by = "0";
                                }else {
                                    updated_by = userdata.getString("updated_by");
                                }
                            }

                            String created_at = "";

                            if (userdata.has("created_at")){
                                if (userdata.getString("created_at").isEmpty()){
                                    created_at = "0000-00-00 00:00:00";
                                }else {
                                    created_at = userdata.getString("created_at");
                                }
                            }

                            String updated_at = "";

                            if (userdata.has("updated_at")){
                                if (userdata.getString("updated_at").isEmpty()){
                                    updated_at = "0000-00-00 00:00:00";
                                }else {
                                    updated_at = userdata.getString("updated_at");
                                }
                            }


                            String operation = userdata.getString("operation");

                            if(operation.equals("update")){
                                Log.d("MainActivity","update_DivisionOfficerAreas");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoDivisionOfficerAreas(id,division_code,division_desc,content,office_area,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else if(operation.equals("insert")) {
                                Log.d("MainActivity","insert_DivisionOfficerAreas");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoDivisionOfficerAreas(id,division_code,division_desc,content,office_area,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else {
                                Toast.makeText(ActivityHome.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }

                        if (nameList.isEmpty()||nameList.equals("")){
                            String is_new_user = "Y";
                            callNameMaster(LangId,UniqueId,is_new_user);
                        }else {
                            String is_new_user = "N";
                            callNameMaster(LangId,UniqueId,is_new_user);
                        }


                    }else if(totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")){

                            if (nameList.isEmpty()||nameList.equals("")){
                                String is_new_user = "Y";
                                callNameMaster(LangId,UniqueId,is_new_user);
                            }else {
                                String is_new_user = "N";
                                callNameMaster(LangId,UniqueId,is_new_user);
                            }
                        }
                    }else {
//                        Toast.makeText(ActivityHome.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }




                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }

    private void callNameMaster(final String id, final String uuid, final String is_new_user) {



        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        sync_db sync_db = new sync_db(id,uuid,device_no,is_new_user);
        Call<JsonObject> call = api.getNameMaster(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {


                assert response.body() != null;


                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log6",totaldata.toString());
                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String value = userdata.getString("value");
                            String status = userdata.getString("status");

                            String created_by = "";

                            if (userdata.has("created_by")){
                                if (userdata.getString("created_by").isEmpty()){
                                    created_by = "0";
                                }else {
                                    created_by = userdata.getString("created_by");
                                }
                            }

                            String updated_by = "";

                            if (userdata.has("updated_by")){
                                if (userdata.getString("updated_by").isEmpty()){
                                    updated_by = "0";
                                }else {
                                    updated_by = userdata.getString("updated_by");
                                }
                            }

                            String created_at = "";

                            if (userdata.has("created_at")){
                                if (userdata.getString("created_at").isEmpty()){
                                    created_at = "0000-00-00 00:00:00";
                                }else {
                                    created_at = userdata.getString("created_at");
                                }
                            }

                            String updated_at = "";

                            if (userdata.has("updated_at")){
                                if (userdata.getString("updated_at").isEmpty()){
                                    updated_at = "0000-00-00 00:00:00";
                                }else {
                                    updated_at = userdata.getString("updated_at");
                                }
                            }


                            String operation = userdata.getString("operation");


                            if(operation.equals("update")){
                                Log.d("MainActivity","update_nameMaster");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoNameMaster(id,value,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else if(operation.equals("insert")) {
                                Log.d("MainActivity","insert_nameMasters");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoNameMaster(id,value,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else {
                                Toast.makeText(ActivityHome.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }


                        if (stateList.isEmpty()||stateList.equals("")){
                            String is_new_user = "Y";
                            callStateMaster(LangId,UniqueId,is_new_user);
                        }else {
                            String is_new_user = "N";
                            callStateMaster(LangId,UniqueId,is_new_user);
                        }

                    }else if(totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")){

                            if (stateList.isEmpty()||stateList.equals("")){
                                String is_new_user = "Y";
                                callStateMaster(LangId,UniqueId,is_new_user);
                            }else {
                                String is_new_user = "N";
                                callStateMaster(LangId,UniqueId,is_new_user);
                            }
                        }
                    }else {
//                        Toast.makeText(ActivityHome.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }




                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }

    private void callStateMaster(final String id, final String uuid, final String is_new_user) {



        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        sync_db sync_db = new sync_db(id,uuid,device_no,is_new_user);
        Call<JsonObject> call = api.getStateMaster(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {


                assert response.body() != null;


                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log7",totaldata.toString());
                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String value = userdata.getString("value");
                            String description = userdata.getString("description");

                            if (description.contains("'")) {
                                description = description.replaceAll("'", "''");
                            }

                            String status = userdata.getString("status");

                            String created_by = "";

                            if (userdata.has("created_by")){
                                if (userdata.getString("created_by").isEmpty()){
                                    created_by = "0";
                                }else {
                                    created_by = userdata.getString("created_by");
                                }
                            }

                            String updated_by = "";

                            if (userdata.has("updated_by")){
                                if (userdata.getString("updated_by").isEmpty()){
                                    updated_by = "0";
                                }else {
                                    updated_by = userdata.getString("updated_by");
                                }
                            }

                            String created_at = "";

                            if (userdata.has("created_at")){
                                if (userdata.getString("created_at").isEmpty()){
                                    created_at = "0000-00-00 00:00:00";
                                }else {
                                    created_at = userdata.getString("created_at");
                                }
                            }

                            String updated_at = "";

                            if (userdata.has("updated_at")){
                                if (userdata.getString("updated_at").isEmpty()){
                                    updated_at = "0000-00-00 00:00:00";
                                }else {
                                    updated_at = userdata.getString("updated_at");
                                }
                            }


                            String operation = userdata.getString("operation");


                            if(operation.equals("update")){
                                Log.d("MainActivity","update_stateMasters");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoStateMaster(id,value,description,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else if(operation.equals("insert")) {
                                Log.d("MainActivity","insert_stateMasters");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoStateMaster(id,value,description,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else {
                                Toast.makeText(ActivityHome.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }


                        if (cityList.isEmpty()||cityList.equals("")){
                            String is_new_user = "Y";
                            callCityMaster(LangId,UniqueId,is_new_user);
                        }else {
                            String is_new_user = "N";
                            callCityMaster(LangId,UniqueId,is_new_user);
                        }

                    }else if(totaldata.getString("success").equals("0")) {


                        if (totaldata.getString("empty_list_flag").equals("true")){


                            if (cityList.isEmpty()||cityList.equals("")){
                                String is_new_user = "Y";
                                callCityMaster(LangId,UniqueId,is_new_user);
                            }else {
                                String is_new_user = "N";
                                callCityMaster(LangId,UniqueId,is_new_user);
                            }
                        }
                    }else {
//                        Toast.makeText(ActivityHome.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }




                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }

    private void callCityMaster(final String id, final String uuid, final String is_new_user) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        sync_db sync_db = new sync_db(id,uuid,device_no,is_new_user);
        Call<JsonObject> call = api.getCityMaster(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log8",totaldata.toString());
                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String value = userdata.getString("value");
                            String description = userdata.getString("description");
                            String state_id = userdata.getString("state_id");

                            if (description.contains("'")) {
                                description = description.replaceAll("'", "''");
                            }

                            String status = userdata.getString("status");

                            String created_by = "";

                            if (userdata.has("created_by")){
                                if (userdata.getString("created_by").isEmpty()){
                                    created_by = "0";
                                }else {
                                    created_by = userdata.getString("created_by");
                                }
                            }

                            String updated_by = "";

                            if (userdata.has("updated_by")){
                                if (userdata.getString("updated_by").isEmpty()){
                                    updated_by = "0";
                                }else {
                                    updated_by = userdata.getString("updated_by");
                                }
                            }

                            String created_at = "";

                            if (userdata.has("created_at")){
                                if (userdata.getString("created_at").isEmpty()){
                                    created_at = "0000-00-00 00:00:00";
                                }else {
                                    created_at = userdata.getString("created_at");
                                }
                            }

                            String updated_at = "";

                            if (userdata.has("updated_at")){
                                if (userdata.getString("updated_at").isEmpty()){
                                    updated_at = "0000-00-00 00:00:00";
                                }else {
                                    updated_at = userdata.getString("updated_at");
                                }
                            }


                            String operation = userdata.getString("operation");

                            if(operation.equals("update")){
                                Log.d("MainActivity","update_cityMasters");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoCityMaster(id,value,description,state_id,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else if(operation.equals("insert")) {
                                Log.d("MainActivity","insert_cityMasters");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoCityMaster(id,value,description,state_id,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else {
                                Toast.makeText(ActivityHome.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }


                        if (vehicleMakeList.isEmpty()||vehicleMakeList.equals("")){
                            String is_new_user = "Y";
                            callVehicleMake(LangId,UniqueId,is_new_user);
                        }else {
                            String is_new_user = "N";
                            callVehicleMake(LangId,UniqueId,is_new_user);
                        }


                    }else if(totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")){
                            if (vehicleMakeList.isEmpty()||vehicleMakeList.equals("")){
                                String is_new_user = "Y";
                                callVehicleMake(LangId,UniqueId,is_new_user);
                            }else {
                                String is_new_user = "N";
                                callVehicleMake(LangId,UniqueId,is_new_user);
                            }
                        }
                    }else {
 //                       Toast.makeText(ActivityHome.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }




                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }



    private void callVehicleMake(final String id, final String uuid, final String is_new_user) {



        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        sync_db sync_db = new sync_db(id,uuid,device_no,is_new_user);
        Call<JsonObject> call = api.getVehicleMakes(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {


                assert response.body() != null;


                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log9",totaldata.toString());
                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String make_code = userdata.getString("make_code");
                            String make_description = userdata.getString("make_description");

                            if (make_description.contains("'")) {
                                make_description = make_description.replaceAll("'", "''");
                            }

                            String status = userdata.getString("status");

                            String created_by = "";

                            if (userdata.has("created_by")){
                                if (userdata.getString("created_by").isEmpty()){
                                    created_by = "0";
                                }else {
                                    created_by = userdata.getString("created_by");
                                }
                            }

                            String updated_by = "";

                            if (userdata.has("updated_by")){
                                if (userdata.getString("updated_by").isEmpty()){
                                    updated_by = "0";
                                }else {
                                    updated_by = userdata.getString("updated_by");
                                }
                            }

                            String created_at = "";

                            if (userdata.has("created_at")){
                                if (userdata.getString("created_at").isEmpty()){
                                    created_at = "0000-00-00 00:00:00";
                                }else {
                                    created_at = userdata.getString("created_at");
                                }
                            }

                            String updated_at = "";

                            if (userdata.has("updated_at")){
                                if (userdata.getString("updated_at").isEmpty()){
                                    updated_at = "0000-00-00 00:00:00";
                                }else {
                                    updated_at = userdata.getString("updated_at");
                                }
                            }


                            String operation = userdata.getString("operation");

                            if(operation.equals("update")){
                                Log.d("MainActivity","update_vehicleMake");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoVehicleMakes(id,make_code,make_description,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else if(operation.equals("insert")) {
                                Log.d("MainActivity","insert_vehicleMake");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoVehicleMakes(id,make_code,make_description,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else {
                                Toast.makeText(ActivityHome.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }


                        if (vehicleModelList.isEmpty()||vehicleModelList.equals("")){
                            String is_new_user = "Y";
                            callVehicleModels(LangId,UniqueId,is_new_user);
                        }else {
                            String is_new_user = "N";
                            callVehicleModels(LangId,UniqueId,is_new_user);
                        }

                    }else if(totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")){


                            if (vehicleModelList.isEmpty()||vehicleModelList.equals("")){
                                String is_new_user = "Y";
                                callVehicleModels(LangId,UniqueId,is_new_user);
                            }else {
                                String is_new_user = "N";
                                callVehicleModels(LangId,UniqueId,is_new_user);
                            }
                        }
                    }else {
 //                       Toast.makeText(ActivityHome.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }



                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }


    private void callVehicleModels(final String id, final String uuid, final String is_new_user) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        sync_db sync_db = new sync_db(id,uuid,device_no,is_new_user);
        Call<JsonObject> call = api.getVehicleModels(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {


                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log10",totaldata.toString());
                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String value = userdata.getString("value");
                            String description = userdata.getString("description");
                            String vehicle_make_id = userdata.getString("vehicle_make_id");

                            if (description.contains("'")) {
                                description = description.replaceAll("'", "''");
                            }

                            String status = userdata.getString("status");

                            String created_by = "";

                            if (userdata.has("created_by")){
                                if (userdata.getString("created_by").isEmpty()){
                                    created_by = "0";
                                }else {
                                    created_by = userdata.getString("created_by");
                                }
                            }

                            String updated_by = "";

                            if (userdata.has("updated_by")){
                                if (userdata.getString("updated_by").isEmpty()){
                                    updated_by = "0";
                                }else {
                                    updated_by = userdata.getString("updated_by");
                                }
                            }

                            String created_at = "";

                            if (userdata.has("created_at")){
                                if (userdata.getString("created_at").isEmpty()){
                                    created_at = "0000-00-00 00:00:00";
                                }else {
                                    created_at = userdata.getString("created_at");
                                }
                            }

                            String updated_at = "";

                            if (userdata.has("updated_at")){
                                if (userdata.getString("updated_at").isEmpty()){
                                    updated_at = "0000-00-00 00:00:00";
                                }else {
                                    updated_at = userdata.getString("updated_at");
                                }
                            }


                            String operation = userdata.getString("operation");

                            if(operation.equals("update")){
                                Log.d("MainActivity","update_vehicleModel");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoVehicleModels(id,value,description,vehicle_make_id,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else if(operation.equals("insert")) {
                                Log.d("MainActivity","insert_vehicleModel");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoVehicleModels(id,value,description,vehicle_make_id,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else {
                                Toast.makeText(ActivityHome.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }


                        Log.e("checkkk",""+violationList.size());
                        if (violationList.isEmpty()||violationList.equals("")){
                            String is_new_user = "Y";
                            callViolations(LangId,UniqueId,is_new_user);
                        }else {
                            String is_new_user = "N";
                            callViolations(LangId,UniqueId,is_new_user);
                        }

                    }else if(totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")){

                            if (violationList.isEmpty()||violationList.equals("")){
                                String is_new_user = "Y";
                                callViolations(LangId,UniqueId,is_new_user);
                            }else {
                                String is_new_user = "N";
                                callViolations(LangId,UniqueId,is_new_user);
                            }
                        }
                    }else {
//                        Toast.makeText(ActivityHome.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }



                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }

    private void callViolations(final String id, final String uuid, final String is_new_user) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        sync_db sync_db = new sync_db(id,uuid,device_no,is_new_user);
        Call<JsonObject> call = api.getViolations(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {


                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log11",totaldata.toString());
                    if (totaldata.getString("success").equals("1")){


                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String value = userdata.getString("value");
                            String description = userdata.getString("description");
                            String is_speeding = userdata.getString("is_speeding");

                            if (description.contains("'")) {
                                description = description.replaceAll("'", "''");
                            }

                            String status = userdata.getString("status");

                            String created_by = "";

                            if (userdata.has("created_by")){
                                if (userdata.getString("created_by").isEmpty()){
                                    created_by = "0";
                                }else {
                                    created_by = userdata.getString("created_by");
                                }
                            }

                            String updated_by = "";

                            if (userdata.has("updated_by")){
                                if (userdata.getString("updated_by").isEmpty()){
                                    updated_by = "0";
                                }else {
                                    updated_by = userdata.getString("updated_by");
                                }
                            }

                            String created_at = "";

                            if (userdata.has("created_at")){
                                if (userdata.getString("created_at").isEmpty()){
                                    created_at = "0000-00-00 00:00:00";
                                }else {
                                    created_at = userdata.getString("created_at");
                                }
                            }

                            String updated_at = "";

                            if (userdata.has("updated_at")){
                                if (userdata.getString("updated_at").isEmpty()){
                                    updated_at = "0000-00-00 00:00:00";
                                }else {
                                    updated_at = userdata.getString("updated_at");
                                }
                            }


                            String operation = userdata.getString("operation");

                            if(operation.equals("update")){
                                Log.d("MainActivity","update_violations");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoViolations(id,value,description,is_speeding,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else if(operation.equals("insert")) {
                                Log.d("MainActivity","insert_violations");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoViolations(id,value,description,is_speeding,status, created_by,  updated_by,  created_at,  updated_at);
                                databaseAccess.close();
                            }else {
                                Toast.makeText(ActivityHome.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }


                        if (zipCityStatesList.isEmpty()||zipCityStatesList.equals("")){
                            String is_new_user = "Y";
                            callZipCityStates(LangId,UniqueId,is_new_user);
                        }else {
                            String is_new_user = "N";
                            callZipCityStates(LangId,UniqueId,is_new_user);
                        }

                    }else if(totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")){


                            if (zipCityStatesList.isEmpty()||zipCityStatesList.equals("")){
                                String is_new_user = "Y";
                                callZipCityStates(LangId,UniqueId,is_new_user);
                            }else {
                                String is_new_user = "N";
                                callZipCityStates(LangId,UniqueId,is_new_user);
                            }
                        }
                    }else {
//                        Toast.makeText(ActivityHome.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }

    private void callZipCityStates(final String id, final String uuid, final String is_new_user) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        sync_db sync_db = new sync_db(id,uuid,device_no,is_new_user);
        Call<JsonObject> call = api.getZipCityStates(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log12",totaldata.toString());
                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String name = userdata.getString("name");
                            String zip = userdata.getString("zip");
                            String city_id = userdata.getString("city_id");
                            String state_id = userdata.getString("state_id");

                            String status = userdata.getString("status");

                            String created_by = "";

                            if (userdata.has("created_by")) {
                                if (userdata.getString("created_by").isEmpty()) {
                                    created_by = "0";
                                } else {
                                    created_by = userdata.getString("created_by");
                                }
                            }

                            String updated_by = "";

                            if (userdata.has("updated_by")) {
                                if (userdata.getString("updated_by").isEmpty()) {
                                    updated_by = "0";
                                } else {
                                    updated_by = userdata.getString("updated_by");
                                }
                            }

                            String created_at = "";

                            if (userdata.has("created_at")) {
                                if (userdata.getString("created_at").isEmpty()) {
                                    created_at = "0000-00-00 00:00:00";
                                } else {
                                    created_at = userdata.getString("created_at");
                                }
                            }

                            String updated_at = "";

                            if (userdata.has("updated_at")) {
                                if (userdata.getString("updated_at").isEmpty()) {
                                    updated_at = "0000-00-00 00:00:00";
                                } else {
                                    updated_at = userdata.getString("updated_at");
                                }
                            }


                            String operation = userdata.getString("operation");

                            if (operation.equals("update")) {
                                Log.d("MainActivity","update_zipcitystate");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoZipCityStates(id, name, zip,city_id,state_id, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                Log.d("MainActivity","insert_zipcitystate");
                                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoZipCityStates(id, name, zip,city_id,state_id, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(ActivityHome.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }
                        }


                        //  Toast.makeText(MainActivity.this, "Data Synced Successfully", Toast.LENGTH_SHORT).show();

                        callUpdateSyncTime(LangId,UniqueId);

                    }else if(totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")){

                            callUpdateSyncTime(LangId,UniqueId);
                        }
                    }else {
//                        Toast.makeText(ActivityHome.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }

    private void callUpdateSyncTime(final String id, final String uuid) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        Call<JsonObject> call = api.getUpdateSyncTime(id,device_no,uuid);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {


                assert response.body() != null;
                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log13",totaldata.toString());
                    if (totaldata.getString("success").equals("1")){


                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                        String timestamp =totaldata.getString("last_admin_sync_datetime");


                        StringTokenizer tk = new StringTokenizer(timestamp);

                        String dateString = tk.nextToken();  // <---  yyyy-mm-dd
                        String timeString = tk.nextToken();  // <---  hh:mm:ss

                        sessionManager.createLastAdminSyncSession(dateString,timeString);



                    }else {
//                        Toast.makeText(ActivityHome.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
//                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }

    private void getCitationNum(){

        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHome.this);
        databaseAccess.open();

        String starting_no = databaseAccess.getStartingCitationNumber(user_id);
        String last_no = databaseAccess.getLastCitationNumber(user_id);
        String lastupdated_no = databaseAccess.getLastUpdatedCitationNumber(user_id);

        int start_ = Integer.parseInt(starting_no);
        int last_ = Integer.parseInt(last_no);

        databaseAccess.close();

        if (!lastupdated_no.equals("null")&&!lastupdated_no.equals("")){
            int lastupdate_ = Integer.parseInt(lastupdated_no);

            int new_ = lastupdate_+1;
            String new_citationNo = String.valueOf(new_);
            if (Integer.parseInt(new_citationNo)>last_){
                Toast.makeText(ActivityHome.this, "Your citation limit has been over", Toast.LENGTH_SHORT).show();
            }else {
                Intent i = new Intent(ActivityHome.this, ActivityHeader.class);
                i.putExtra("selected","header");
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.enter,R.anim.exit);
            }

        }else {

//            int new_ = Integer.parseInt(starting_no);
//            String new_citationNo = String.valueOf(new_);
            Intent i = new Intent(ActivityHome.this, ActivityHeader.class);
            i.putExtra("selected","header");
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.enter,R.anim.exit);

//            Toast.makeText(ActivityHome.this, "Please check your data with admin team", Toast.LENGTH_SHORT).show();
        }

    }


    private void checkVersion(final String uuid,final String platform, final String version) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        Call<JsonObject> call = api.checkVersion(ConstantCodes.LANG_ID, uuid, platform,version);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                assert response.body() != null;
                try {
                    if (response.code() == 200) {
                        // Toast.makeText(DrawerActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("checkVersion", "onResponse: " + response.message());
                    } else if (response.code() == 400) {
                        androidx.appcompat.app.AlertDialog.Builder builder;
                            builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityHome.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                        builder.setTitle(R.string.app_name)
                                .setMessage("New Update available! Would you like to update app?");
                        builder.setPositiveButton("YES", (dialog, which) -> {
                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                            dialog.dismiss();
                        });
                        builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss()).show();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                //  Toast.makeText(ActivityLogin.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }
}