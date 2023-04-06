package com.edatasolutions.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.edatasolutions.BuildConfig;
import com.edatasolutions.R;

import com.edatasolutions.database.DatabaseAccess;
import com.edatasolutions.interfaces.RetrofitInterface;
import com.edatasolutions.models.sync_db;
import com.edatasolutions.utils.ConstantCodes;
import com.edatasolutions.utils.SessionManager;
import com.edatasolutions.utils.Utils;
import com.google.gson.JsonObject;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onPause() {
        super.onPause();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(myLocalBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("my.own.broadcast");
//        LocalBroadcastManager.getInstance(this).registerReceiver(myLocalBroadcastReceiver, intentFilter);
    }

    private String LangId = "", TOKEN_VALUE = "", USER_ID = "", UniqueId = "";
    private SessionManager sessionManager;
    private ImageView sync_btn;
    private LinearLayout ll_sync_btn;
    private ImageView img_menu, img_back;
    private String code_type = "";
    private String field_type = "";
    private String device_no = "";
    private TextView deviceno_txt, pleasewait_txt, sync_percent;
    private Animation rotation;
    private TextView last_sync_time, last_sync_date;
    private LinearLayout lastsync_lay;

    private int CurrentProgress = 0;
    private ProgressBar progressBar;

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

    public static final String MESSAGE_STATUS = "message_status";

    private FrameLayout mainpage_lay;


    private void getAllDBdata() {

        DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
        databaseAccess.open();

        usersList = databaseAccess.getUsersData();
        roleList = databaseAccess.getRolesData();
        commoncodesList = databaseAccess.getCommonCodes();
        commonfieldsList = databaseAccess.getCommonFields();
        division_officer_areaList = databaseAccess.getDivisionOfficerAreas();
        nameList = databaseAccess.getNameMaster();
        cityList = databaseAccess.getCityMaster();
        stateList = databaseAccess.getStateMaster();
        vehicleMakeList = databaseAccess.getVehicleMakeList();
        vehicleModelList = databaseAccess.getVehicleModelList();
        violationList = databaseAccess.getViolationList();
        zipCityStatesList = databaseAccess.getZipCityStateList();

        databaseAccess.close();
    }

    public void ShowGif() {
        //ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.sync).into(sync_btn);
    }

    public void HideGif() {
        //ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.sync).into(sync_btn);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("InterruptedException22", event.toString());
        return true; // Splash screen is consuming all touch events on it.
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"HardwareIds", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        getAllDBdata();
        sessionManager = new SessionManager(MainActivity.this);
        mainpage_lay = findViewById(R.id.mainpage_lay);
        img_menu = findViewById(R.id.img_menu);
        img_back = findViewById(R.id.img_back);
        deviceno_txt = findViewById(R.id.deviceno_txt);
        pleasewait_txt = findViewById(R.id.pleasewait_txt);
        sync_percent = findViewById(R.id.sync_percent);
        last_sync_time = findViewById(R.id.last_sync_time);
        last_sync_date = findViewById(R.id.last_sync_date);
        lastsync_lay = findViewById(R.id.lastsync_lay);
        progressBar = findViewById(R.id.progressBar);


        if (sessionManager.getLastAdminSyncSession().get(SessionManager.LAST_SYNC_DATE).equals("")) {
            lastsync_lay.setVisibility(View.GONE);
        } else {

            last_sync_time.setText(sessionManager.getLastAdminSyncSession().get(SessionManager.LAST_SYNC_TIME));
            String last_sync = sessionManager.getLastAdminSyncSession().get(SessionManager.LAST_SYNC_DATE);

            last_sync_date.setText(Utils.getDateInFormat("yyyy-MM-dd", "MM-dd-yyyy", last_sync));
        }


        LangId = ConstantCodes.LANG_ID;
        UniqueId = UUID.randomUUID().toString();
        TOKEN_VALUE = sessionManager.getAdminLogin().get(SessionManager.TOKEN);
        USER_ID = sessionManager.getAdminLogin().get(SessionManager.USER_ID);
        device_no = Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceno_txt.setText("Device Number - " + device_no);

        Log.e("token", TOKEN_VALUE);


        deviceno_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(deviceno_txt.getText());
                Toast.makeText(MainActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        sync_btn = findViewById(R.id.sync_btn);
        ll_sync_btn= findViewById(R.id.ll_sync_btn);
        rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
        rotation.setRepeatCount(Animation.INFINITE);


        String versionName = BuildConfig.VERSION_NAME;
        String platform = "android";
        checkVersion(UniqueId, platform, versionName);
//        DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
//        databaseAccess.open();
//        String datetime="";
//        if (databaseAccess.getLastSyncDateTime(USER_ID).isEmpty()){
//            datetime = "0000-00-00 00:00:00";
//        }else {
//            datetime = databaseAccess.getLastSyncDateTime(USER_ID);
//        }
//        databaseAccess.close();
//        Log.e("datetime",datetime);


//        mainpage_lay.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.e("InterruptedException23",event.toString());
//                return false;
//            }
//        });

        ll_sync_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_sync_btn.setEnabled(false);
//                CurrentProgress=0;
//                progressBar.setProgress(CurrentProgress);
//                sync_percent.setText(CurrentProgress+"%");


//                mainpage_lay.setMotionEventSplittingEnabled(false);
//                mainpage_lay.setClickable(false);
//                mainpage_lay.setEnabled(false);
//                mainpage_lay.setFocusableInTouchMode(false);
                try {
                    Thread t = new Thread() {
                        public void run() {
                            String is_new_user_userlist = "";


                            if (usersList.isEmpty() || usersList.equals("")) {
                                is_new_user_userlist = "Y";
                                callUserList(LangId, UniqueId, is_new_user_userlist);
                            } else {
                                is_new_user_userlist = "N";
                                callUserList(LangId, UniqueId, is_new_user_userlist);
                            }
                        }
                    };
                    t.start();
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        });


        img_back.setVisibility(View.INVISIBLE);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                sessionManager.logoutUser();
                                final Intent mainIntent = new Intent(MainActivity.this, ActivityLogin.class);
                                startActivity(mainIntent);
                                finish();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want to Logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }

        });

    }


    public void startServiceMain() {

//        Intent serviceIntent = new Intent(this, SendDataService.class);
//        serviceIntent.putExtra("inputExtra", "Edata is working in background");
//
//        SendDataService.enqueueWork(this, serviceIntent);

//        Intent serviceIntent = new Intent(this, SendDataService.class);
//        serviceIntent.putExtra("inputExtra", "Edata is working in background");
//
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
////            startForegroundService(serviceIntent);
////        } else {
////            //lower then Oreo, just start the service.
////            startService(serviceIntent);
////        }
//
//        MainActivity.this.bindService(serviceIntent, mServerConn, Context.BIND_AUTO_CREATE);
//        MainActivity.this.startService(serviceIntent);

//        Intent number5 = new Intent(getBaseContext(), MyForeGroundService.class);
//        number5.putExtra("times", 5);

        // finish();
        //    ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopServiceMain() {
//        Intent serviceIntent = new Intent(this, SendDataService.class);
//        serviceIntent.putExtra("inputExtra", "stop");
//
//        SendDataService.enqueueWork(this, serviceIntent);

//        Intent serviceIntent = new Intent(this, SendDataService.class);
//        serviceIntent.putExtra("inputExtra", "stop");

//        MainActivity.this.stopService(new Intent(MainActivity.this, SendDataService.class));
//        MainActivity.this.unbindService(mServerConn);

    }

    protected ServiceConnection mServerConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d("LOG_TAG", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("LOG_TAG", "onServiceDisconnected");
        }
    };


    private void callUserList(final String id, final String uuid, final String is_new_user) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        sync_db sync_db = new sync_db(id, uuid, device_no, is_new_user);
        Call<JsonObject> call = api.getUserList(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log1", totaldata.toString());
                    //Toast.makeText(MainActivity.this,totaldata.toString(),Toast.LENGTH_LONG).show();
                    if (totaldata.getString("success").equals("1")) {

                        startServiceMain();

                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

//                        CurrentProgress = 0 ;
//                        progressBar.setProgress(CurrentProgress);
//                        sync_percent.setText(CurrentProgress+"%");
//                        sync_btn.startAnimation(rotation);
//
//                        pleasewait_txt.setTextColor(getResources().getColor(R.color.light_pink));
//                        pleasewait_txt.setText("Please Wait.. Data is Syncing");
//                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String name = userdata.getString("name");
                            String badge_no = userdata.getString("badge_no");
                            String email = userdata.getString("email");
                            String password = userdata.getString("password");
                            String role_id = userdata.getString("role_id");
                            String citation_series_from = userdata.getString("citation_series_from");
                            String citation_series_to = userdata.getString("citation_series_to");
                            String access_token = "";
                            if (userdata.has("access_token")) {
                                if (userdata.getString("access_token").isEmpty()) {
                                    access_token = "";
                                } else {
                                    access_token = userdata.getString("access_token");
                                }
                            }

                            String mkey = "";
                            if (userdata.has("mkey")) {
                                if (userdata.getString("mkey").isEmpty()) {
                                    mkey = "";
                                } else {
                                    mkey = userdata.getString("mkey");
                                }
                            }

                            String msalt = "";
                            if (userdata.has("msalt")) {
                                if (userdata.getString("msalt").isEmpty()) {
                                    msalt = "";
                                } else {
                                    msalt = userdata.getString("msalt");
                                }
                            }

                            String last_sync_datetime = "";

                            if (userdata.has("last_sync_datetime")) {
                                if (userdata.getString("last_sync_datetime").isEmpty()) {
                                    last_sync_datetime = "0000-00-00 00:00:00";
                                } else {
                                    last_sync_datetime = userdata.getString("last_sync_datetime");
                                }
                            }

                            String status = userdata.getString("status");
                            String last_login = userdata.getString("last_login");
                            String fpwd_flag = userdata.getString("fpwd_flag");

                            String last_pwd_update_time = "";

                            if (userdata.has("last_pwd_update_time")) {
                                if (userdata.getString("last_pwd_update_time").isEmpty()) {
                                    last_pwd_update_time = "0000-00-00 00:00:00";
                                } else {
                                    last_pwd_update_time = userdata.getString("last_pwd_update_time");
                                }
                            }

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
                                sync_btn.startAnimation(rotation);
                                Log.d("MainActivity", "update_userlist");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoUsers(id, name, badge_no, email, password, role_id, citation_series_from, citation_series_to, access_token, mkey, msalt, last_sync_datetime, status, last_login, fpwd_flag, last_pwd_update_time, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                sync_btn.startAnimation(rotation);
                                Log.d("MainActivity", "insert_userlist");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoUsers(id, name, badge_no, email, password, role_id, citation_series_from, citation_series_to, access_token, mkey, msalt, last_sync_datetime, status, last_login, fpwd_flag, last_pwd_update_time, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(MainActivity.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }
                        CurrentProgress = 0;

                        CurrentProgress = CurrentProgress + 8;
                        ;
                        progressBar.setProgress(CurrentProgress);
                        sync_percent.setText(CurrentProgress + "%");
                        sync_btn.startAnimation(rotation);

                        pleasewait_txt.setTextColor(getResources().getColor(R.color.light_pink));
                        pleasewait_txt.setText("Please Wait.. Data is Syncing");


//                        sync_btn.startAnimation(rotation);
//                        callAllAPIs();

                        if (commoncodesList.isEmpty() || commoncodesList.equals("")) {
                            String is_new_user = "Y";
                            callCommonCodes(LangId, code_type, UniqueId, is_new_user);
                        } else {
                            String is_new_user = "N";
                            callCommonCodes(LangId, code_type, UniqueId, is_new_user);
                        }
                    } else if (totaldata.getString("success").equals("0")) {


                        if (totaldata.getString("message").equals("device not registered")) {

                            Toast.makeText(MainActivity.this, "Your device is not registered.", Toast.LENGTH_LONG).show();

                        }
//                        else {
//
//                            CurrentProgress = 0 ;
//                            progressBar.setProgress(CurrentProgress);
//                            sync_percent.setText(CurrentProgress+"%");
//                            sync_btn.startAnimation(rotation);
//
//                            pleasewait_txt.setTextColor(getResources().getColor(R.color.light_pink));
//                            pleasewait_txt.setText("Please Wait.. Data is Syncing");
////                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
////                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
// //                           callAllAPIs();
//                        }

                        if (totaldata.getString("empty_list_flag").equals("true")) {

                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            startServiceMain();
                            CurrentProgress = 0;
                            CurrentProgress = CurrentProgress + 8;
                            ;
                            progressBar.setProgress(CurrentProgress);
                            sync_percent.setText(CurrentProgress + "%");
                            sync_btn.startAnimation(rotation);

                            pleasewait_txt.setTextColor(getResources().getColor(R.color.light_pink));
                            pleasewait_txt.setText("Please Wait.. Data is Syncing");

                            if (commoncodesList.isEmpty() || commoncodesList.equals("")) {
                                String is_new_user = "Y";
                                callCommonCodes(LangId, code_type, UniqueId, is_new_user);
                            } else {
                                String is_new_user = "N";
                                callCommonCodes(LangId, code_type, UniqueId, is_new_user);
                            }

//                            callAllAPIs();

                        }

                    } else if (totaldata.getString("success").equals("4")) {

                        sessionManager.logoutUser();
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                        final Intent mainIntent = new Intent(MainActivity.this, ActivityLogin.class);
                        startActivity(mainIntent);
                        finish();

                    } else {
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
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


    private void callCommonCodes(final String id, final String code_type, final String uuid, final String is_new_user) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        Call<JsonObject> call = api.getCommonCodes(id, device_no, code_type, uuid, is_new_user);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log3", totaldata.toString());
                    //Toast.makeText(MainActivity.this,totaldata.toString(),Toast.LENGTH_LONG);
                    if (totaldata.getString("success").equals("1")) {

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String code_type = userdata.getString("code_type");
                            String code_value = userdata.getString("code_value");

                            if (code_value.contains("'")) {
                                code_value = code_value.replaceAll("'", "''");
                            }

                            String code_description = userdata.getString("code_description");

                            if (code_description.contains("'")) {
                                code_description = code_description.replaceAll("'", "''");
                            }

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
                                Log.d("MainActivity", "update_commoncodes");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoCommonCodes(id, code_type, code_value, code_description, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                Log.d("MainActivity", "insert_commoncodes");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoCommonCodes(id, code_type, code_value, code_description, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(MainActivity.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }
                        CurrentProgress = CurrentProgress + 8;
                        progressBar.setProgress(CurrentProgress);
                        sync_percent.setText(CurrentProgress + "%");
                        if (commonfieldsList.isEmpty() || commonfieldsList.equals("")) {
                            String is_new_user = "Y";
                            callCommonFields(LangId, field_type, UniqueId, is_new_user);
                        } else {
                            String is_new_user = "N";
                            callCommonFields(LangId, field_type, UniqueId, is_new_user);
                        }

                    } else if (totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")) {
                            CurrentProgress = CurrentProgress + 8;
                            progressBar.setProgress(CurrentProgress);
                            sync_percent.setText(CurrentProgress + "%");
                            if (commonfieldsList.isEmpty() || commonfieldsList.equals("")) {
                                String is_new_user = "Y";
                                callCommonFields(LangId, field_type, UniqueId, is_new_user);
                            } else {
                                String is_new_user = "N";
                                callCommonFields(LangId, field_type, UniqueId, is_new_user);
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void callCommonFields(final String id, final String field_type, final String uuid, final String is_new_user) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        Call<JsonObject> call = api.getCommonFields(id, device_no, field_type, uuid, is_new_user);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log4", totaldata.toString());
                    if (totaldata.getString("success").equals("1")) {

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String field_type = userdata.getString("field_type");
                            String field_value = userdata.getString("field_value");
                            if (field_value.contains("'")) {
                                field_value = field_value.replaceAll("'", "''");
                            }
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
                                Log.d("MainActivity", "update_commonfields");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoCommonFields(id, field_type, field_value, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                Log.d("MainActivity", "insert_commonfields");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoCommonFields(id, field_type, field_value, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(MainActivity.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }
                        }
                        CurrentProgress = CurrentProgress + 8;
                        progressBar.setProgress(CurrentProgress);
                        sync_percent.setText(CurrentProgress + "%");
                        if (division_officer_areaList.isEmpty() || division_officer_areaList.equals("")) {
                            String is_new_user = "Y";
                            callDivisionOfficerAreas(LangId, UniqueId, is_new_user);
                        } else {
                            String is_new_user = "N";
                            callDivisionOfficerAreas(LangId, UniqueId, is_new_user);
                        }


                    } else if (totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")) {
                            CurrentProgress = CurrentProgress + 8;
                            progressBar.setProgress(CurrentProgress);
                            sync_percent.setText(CurrentProgress + "%");
                            if (division_officer_areaList.isEmpty() || division_officer_areaList.equals("")) {
                                String is_new_user = "Y";
                                callDivisionOfficerAreas(LangId, UniqueId, is_new_user);
                            } else {
                                String is_new_user = "N";
                                callDivisionOfficerAreas(LangId, UniqueId, is_new_user);
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
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
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        sync_db sync_db = new sync_db(id, uuid, device_no, is_new_user);
        Call<JsonObject> call = api.getDivisionOfficerAreas(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {


                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log5", totaldata.toString());
                    if (totaldata.getString("success").equals("1")) {

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String division_code = userdata.getString("division_code");
                            String division_desc = userdata.getString("division_desc");
                            if (division_desc.contains("'")) {
                                division_desc = division_desc.replaceAll("'", "''");
                            }
                            String content = userdata.getString("content");
                            if (content.contains("'")) {
                                content = content.replaceAll("'", "''");
                            }
                            String office_area = userdata.getString("office_area");
                            if (office_area.contains("'")) {
                                office_area = office_area.replaceAll("'", "''");
                            }

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
                                Log.d("MainActivity", "update_DivisionOfficerAreas");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoDivisionOfficerAreas(id, division_code, division_desc, content, office_area, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                Log.d("MainActivity", "insert_DivisionOfficerAreas");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoDivisionOfficerAreas(id, division_code, division_desc, content, office_area, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(MainActivity.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }
                        CurrentProgress = CurrentProgress + 8;
                        progressBar.setProgress(CurrentProgress);
                        sync_percent.setText(CurrentProgress + "%");

                        if (nameList.isEmpty() || nameList.equals("")) {
                            String is_new_user = "Y";
                            callNameMaster(LangId, UniqueId, is_new_user);
                        } else {
                            String is_new_user = "N";
                            callNameMaster(LangId, UniqueId, is_new_user);
                        }


                    } else if (totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")) {
                            CurrentProgress = CurrentProgress + 8;
                            progressBar.setProgress(CurrentProgress);
                            sync_percent.setText(CurrentProgress + "%");
                            if (nameList.isEmpty() || nameList.equals("")) {
                                String is_new_user = "Y";
                                callNameMaster(LangId, UniqueId, is_new_user);
                            } else {
                                String is_new_user = "N";
                                callNameMaster(LangId, UniqueId, is_new_user);
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
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


//        sync_percent.setText("45%");
//        sync_btn.startAnimation(rotation);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        sync_db sync_db = new sync_db(id, uuid, device_no, is_new_user);
        Call<JsonObject> call = api.getNameMaster(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log6", totaldata.toString());
                    if (totaldata.getString("success").equals("1")) {

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String value = userdata.getString("value");
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

//                            sync_percent.setText("45%");
//                            sync_btn.startAnimation(rotation);

                            if (operation.equals("update")) {
                                Log.d("MainActivity", "update_nameMaster");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoNameMaster(id, value, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                Log.d("MainActivity", "insert_nameMasters");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoNameMaster(id, value, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(MainActivity.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }
                        CurrentProgress = CurrentProgress + 8;
                        progressBar.setProgress(CurrentProgress);
                        sync_percent.setText(CurrentProgress + "%");

                        if (vehicleMakeList.isEmpty() || vehicleMakeList.equals("")) {
                            String is_new_user = "Y";
                            callVehicleMake(LangId, UniqueId, is_new_user);
                        } else {
                            String is_new_user = "N";
                            callVehicleMake(LangId, UniqueId, is_new_user);
                        }


                    } else if (totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")) {
                            CurrentProgress = CurrentProgress + 8;
                            progressBar.setProgress(CurrentProgress);
                            sync_percent.setText(CurrentProgress + "%");

                            if (vehicleMakeList.isEmpty() || vehicleMakeList.equals("")) {
                                String is_new_user = "Y";
                                callVehicleMake(LangId, UniqueId, is_new_user);
                            } else {
                                String is_new_user = "N";
                                callVehicleMake(LangId, UniqueId, is_new_user);
                            }


                        }
                    } else {
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
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

//        startService();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        sync_db sync_db = new sync_db(id, uuid, device_no, is_new_user);
        Call<JsonObject> call = api.getVehicleMakes(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {


                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log7", totaldata.toString());
                    if (totaldata.getString("success").equals("1")) {

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
                                Log.d("MainActivity", "update_vehicleMake");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoVehicleMakes(id, make_code, make_description, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                Log.d("MainActivity", "insert_vehicleMake");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoVehicleMakes(id, make_code, make_description, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(MainActivity.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }
                        CurrentProgress = CurrentProgress + 8;
                        progressBar.setProgress(CurrentProgress);
                        sync_percent.setText(CurrentProgress + "%");

                        if (vehicleModelList.isEmpty() || vehicleModelList.equals("")) {
                            String is_new_user = "Y";
                            callVehicleModels(LangId, UniqueId, is_new_user);
                        } else {
                            String is_new_user = "N";
                            callVehicleModels(LangId, UniqueId, is_new_user);
                        }

                    } else if (totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")) {
                            CurrentProgress = CurrentProgress + 8;
                            progressBar.setProgress(CurrentProgress);
                            sync_percent.setText(CurrentProgress + "%");

                            if (vehicleModelList.isEmpty() || vehicleModelList.equals("")) {
                                String is_new_user = "Y";
                                callVehicleModels(LangId, UniqueId, is_new_user);
                            } else {
                                String is_new_user = "N";
                                callVehicleModels(LangId, UniqueId, is_new_user);
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
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

//        startService();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        sync_db sync_db = new sync_db(id, uuid, device_no, is_new_user);
        Call<JsonObject> call = api.getVehicleModels(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {


                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log8", totaldata.toString());
                    if (totaldata.getString("success").equals("1")) {

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
                                Log.d("MainActivity", "update_vehicleModel");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoVehicleModels(id, value, description, vehicle_make_id, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                Log.d("MainActivity", "insert_vehicleModel");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoVehicleModels(id, value, description, vehicle_make_id, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(MainActivity.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }
                        CurrentProgress = CurrentProgress + 8;
                        progressBar.setProgress(CurrentProgress);
                        sync_percent.setText(CurrentProgress + "%");

                        if (violationList.isEmpty() || violationList.equals("")) {
                            String is_new_user = "Y";
                            callViolations(LangId, UniqueId, is_new_user);
                        } else {
                            String is_new_user = "N";
                            callViolations(LangId, UniqueId, is_new_user);
                        }

                    } else if (totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")) {
                            CurrentProgress = CurrentProgress + 8;
                            progressBar.setProgress(CurrentProgress);
                            sync_percent.setText(CurrentProgress + "%");

                            if (violationList.isEmpty() || violationList.equals("")) {
                                String is_new_user = "Y";
                                callViolations(LangId, UniqueId, is_new_user);
                            } else {
                                String is_new_user = "N";
                                callViolations(LangId, UniqueId, is_new_user);
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
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

//        startService();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        sync_db sync_db = new sync_db(id, uuid, device_no, is_new_user);
        Call<JsonObject> call = api.getViolations(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log9", totaldata.toString());
                    if (totaldata.getString("success").equals("1")) {


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
                                Log.d("MainActivity", "update_violations");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoViolations(id, value, description, is_speeding, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                Log.d("MainActivity", "insert_violations");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoViolations(id, value, description, is_speeding, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(MainActivity.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }
                        CurrentProgress = CurrentProgress + 8;
                        progressBar.setProgress(CurrentProgress);
                        sync_percent.setText(CurrentProgress + "%");

                        if (stateList.isEmpty() || stateList.equals("") || sessionManager.getLastAdminSyncSession().get(SessionManager.LAST_SYNC_DATE).equals("") || usersList.isEmpty()) {
                            String is_new_user = "Y";
                            callStateMaster(LangId, UniqueId, is_new_user);
                        } else {
                            String is_new_user = "N";
                            callStateMaster(LangId, UniqueId, is_new_user);
                        }

                    } else if (totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")) {
                            CurrentProgress = CurrentProgress + 8;
                            progressBar.setProgress(CurrentProgress);
                            sync_percent.setText(CurrentProgress + "%");

                            if (stateList.isEmpty() || stateList.equals("") || sessionManager.getLastAdminSyncSession().get(SessionManager.LAST_SYNC_DATE).equals("") || usersList.isEmpty()) {
                                String is_new_user = "Y";
                                callStateMaster(LangId, UniqueId, is_new_user);
                            } else {
                                String is_new_user = "N";
                                callStateMaster(LangId, UniqueId, is_new_user);
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
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
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        sync_db sync_db = new sync_db(id, uuid, device_no, is_new_user);
        Call<JsonObject> call = api.getStateMaster(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log10", totaldata.toString());
                    if (totaldata.getString("success").equals("1")) {

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
                                Log.d("MainActivity", "update_stateMasters");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoStateMaster(id, value, description, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                Log.d("MainActivity", "insert_stateMasters");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoStateMaster(id, value, description, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(MainActivity.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }
                        CurrentProgress = CurrentProgress + 8;
                        progressBar.setProgress(CurrentProgress);
                        sync_percent.setText(CurrentProgress + "%");

                        if (cityList.isEmpty() || cityList.equals("") || sessionManager.getLastAdminSyncSession().get(SessionManager.LAST_SYNC_DATE).equals("") || usersList.isEmpty()) {
                            String is_new_user = "Y";
                            callCityMaster(LangId, UniqueId, is_new_user);
                        } else {
                            String is_new_user = "N";
                            callCityMaster(LangId, UniqueId, is_new_user);
                        }

                    } else if (totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")) {
                            CurrentProgress = CurrentProgress + 8;
                            progressBar.setProgress(CurrentProgress);
                            sync_percent.setText(CurrentProgress + "%");

                            if (cityList.isEmpty() || cityList.equals("") || sessionManager.getLastAdminSyncSession().get(SessionManager.LAST_SYNC_DATE).equals("") || usersList.isEmpty()) {
                                String is_new_user = "Y";
                                callCityMaster(LangId, UniqueId, is_new_user);
                            } else {
                                String is_new_user = "N";
                                callCityMaster(LangId, UniqueId, is_new_user);
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
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


//        sync_percent.setText("65%");
//        sync_btn.startAnimation(rotation);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        sync_db sync_db = new sync_db(id, uuid, device_no, is_new_user);
        Call<JsonObject> call = api.getCityMaster(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log11", totaldata.toString());
                    if (totaldata.getString("success").equals("1")) {

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
                                Log.d("MainActivity", "update_cityMasters");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoCityMaster(id, value, description, state_id, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                Log.d("MainActivity", "insert_cityMasters");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoCityMaster(id, value, description, state_id, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(MainActivity.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }
                        CurrentProgress = CurrentProgress + 8;
                        progressBar.setProgress(CurrentProgress);
                        sync_percent.setText(CurrentProgress + "%");

                        if (zipCityStatesList.isEmpty() || zipCityStatesList.equals("") || sessionManager.getLastAdminSyncSession().get(SessionManager.LAST_SYNC_DATE).equals("") || usersList.isEmpty()) {
                            String is_new_user = "Y";
                            callZipCityStates(LangId, UniqueId, is_new_user);
                        } else {
                            String is_new_user = "N";
                            callZipCityStates(LangId, UniqueId, is_new_user);
                        }

                    } else if (totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")) {
                            CurrentProgress = CurrentProgress + 8;
                            progressBar.setProgress(CurrentProgress);
                            sync_percent.setText(CurrentProgress + "%");

                            if (zipCityStatesList.isEmpty() || zipCityStatesList.equals("") || sessionManager.getLastAdminSyncSession().get(SessionManager.LAST_SYNC_DATE).equals("") || usersList.isEmpty()) {
                                String is_new_user = "Y";
                                callZipCityStates(LangId, UniqueId, is_new_user);
                            } else {
                                String is_new_user = "N";
                                callZipCityStates(LangId, UniqueId, is_new_user);
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
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

        //       startService();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        sync_db sync_db = new sync_db(id, uuid, device_no, is_new_user);
        Call<JsonObject> call = api.getZipCityStates(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {


                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log12", totaldata.toString());
                    if (totaldata.getString("success").equals("1")) {

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
                                Log.d("MainActivity", "update_zipcitystate");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoZipCityStates(id, name, zip, city_id, state_id, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                Log.d("MainActivity", "insert_zipcitystate");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoZipCityStates(id, name, zip, city_id, state_id, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(MainActivity.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }
                        }

                        CurrentProgress = CurrentProgress + 8;
                        progressBar.setProgress(CurrentProgress);
                        sync_percent.setText(CurrentProgress + "%");

                        if (roleList.isEmpty() || roleList.equals("")) {
                            String is_new_user = "Y";
                            callRoleList(LangId, UniqueId, is_new_user);
                        } else {
                            String is_new_user = "N";
                            callRoleList(LangId, UniqueId, is_new_user);
                        }


                    } else if (totaldata.getString("success").equals("0")) {

                        if (totaldata.getString("empty_list_flag").equals("true")) {

                            CurrentProgress = CurrentProgress + 8;
                            progressBar.setProgress(CurrentProgress);
                            sync_percent.setText(CurrentProgress + "%");
                            if (roleList.isEmpty() || roleList.equals("")) {
                                String is_new_user = "Y";
                                callRoleList(LangId, UniqueId, is_new_user);
                            } else {
                                String is_new_user = "N";
                                callRoleList(LangId, UniqueId, is_new_user);
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void callRoleList(final String id, final String uuid, final String is_new_user) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        sync_db sync_db = new sync_db(id, uuid, device_no, is_new_user);
        Call<JsonObject> call = api.getRoleList(sync_db);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                assert response.body() != null;


                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log2", totaldata.toString());

                    if (totaldata.getString("success").equals("1")) {

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject userdata = jsonArray.getJSONObject(i);


                            String id = userdata.getString("id");
                            String user_role = userdata.getString("user_role");
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
                                Log.d("MainActivity", "update_rolelist");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.UpdateDatatoRoles(id, user_role, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else if (operation.equals("insert")) {
                                Log.d("MainActivity", "insert_rolelist");
                                DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                                databaseAccess.open();
                                databaseAccess.InsertDatatoRoles(id, user_role, status, created_by, updated_by, created_at, updated_at);
                                databaseAccess.close();
                            } else {
                                Toast.makeText(MainActivity.this, "No operation to perform", Toast.LENGTH_SHORT).show();
                            }


                        }
                        CurrentProgress = CurrentProgress + 8;
                        progressBar.setProgress(CurrentProgress);
                        sync_percent.setText(CurrentProgress + "%");
                        callUpdateSyncTime(LangId, UniqueId);


                    } else if (totaldata.getString("success").equals("0")) {


                        if (totaldata.getString("empty_list_flag").equals("true")) {
                            CurrentProgress = CurrentProgress + 8;
                            progressBar.setProgress(CurrentProgress);
                            sync_percent.setText(CurrentProgress + "%");
                            callUpdateSyncTime(LangId, UniqueId);

                        }
                    } else {
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
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
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        Call<JsonObject> call = api.getUpdateSyncTime(id, device_no, uuid);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                assert response.body() != null;
                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log13", totaldata.toString());

                    if (totaldata.getString("success").equals("1")) {


                        DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                        databaseAccess.open();
                        String userid = databaseAccess.getUserId();
                        ArrayList<String> dataPassesList = new ArrayList<>();
                        dataPassesList = databaseAccess.getDataPasses();
                        databaseAccess.close();

                        if (dataPassesList.size() == 0 || dataPassesList.isEmpty()) {
                            getLastCitationNo(userid);
                        }


                        lastsync_lay.setVisibility(View.VISIBLE);

                        progressBar.setProgress(100);
                        sync_percent.setText("100%");
                        pleasewait_txt.setTextColor(getResources().getColor(R.color.light_green));
                        pleasewait_txt.setText("Data Synced Successfully");
                        sync_btn.clearAnimation();

//                    pleasewait_txt.setTextColor(getResources().getColor(R.color.light_green));
//                        pleasewait_txt.setText("Data Synced Successfully");
                        // Toast.makeText(MainActivity.this, "Data Synced Successfully", Toast.LENGTH_SHORT).show();

                        //       stopServiceMain();
//                        mWorkManager.cancelAllWork();
                        String timestamp = totaldata.getString("last_admin_sync_datetime");


                        StringTokenizer tk = new StringTokenizer(timestamp);

                        String dateString = tk.nextToken();  // <---  yyyy-mm-dd
                        String timeString = tk.nextToken();  // <---  hh:mm:ss

                        Log.d("radhika_updatedsynctime", dateString + "  " + timeString);
                        sessionManager.createLastAdminSyncSession(dateString, timeString);

                        last_sync_date.setText("");
                        last_sync_time.setText("");
                        if (dateString != null && !(dateString.equalsIgnoreCase(""))) {
                            last_sync_date.setText(Utils.getDateInFormat("yyyy-MM-dd", "MM-dd-yyyy", dateString));
                        }
                        last_sync_time.setText(timeString);

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        ll_sync_btn.setEnabled(true);

//                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                        sync_btn.setEnabled(true);
//
//                        stopServiceMain();
                    } else {
                        Toast.makeText(MainActivity.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void checkVersion(final String uuid, final String platform, final String version) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
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

        Call<JsonObject> call = api.checkVersion(ConstantCodes.LANG_ID, uuid, platform, version);

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
                        builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
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

    private void getLastCitationNo(String user_id) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH, ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN, TOKEN_VALUE)
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

        Call<JsonObject> call = api.getLastCitationNo(ConstantCodes.LANG_ID, device_no);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                assert response.body() != null;
                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("radhika_log_citation", totaldata.toString());
                    //Toast.makeText(MainActivity.this,totaldata.toString(),Toast.LENGTH_SHORT).show();
                    if (totaldata.getString("success").equals("1")) {
                        String lastcitation = totaldata.getString("lastCitationNumber");
                        if (!lastcitation.equals("")) {
                            int i = Integer.parseInt(lastcitation);
                            int updatedCitation = i + 1;
                            String new_no = String.valueOf(updatedCitation);
                            DatabaseAccess databaseAccess = new DatabaseAccess(MainActivity.this);
                            databaseAccess.open();

                            databaseAccess.UpdateCitationInUsersData(new_no, user_id);
                            databaseAccess.close();
                        }
                    } else {
                        Log.d("radhika_log_citation", totaldata.getString("message"));
                    }

                } catch (Exception ex) {
                    Log.d("radhika_log_citation123", "" + ex);
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