package com.edatasolutions.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edatasolutions.R;
import com.edatasolutions.database.DatabaseAccess;
import com.edatasolutions.interfaces.RetrofitInterface;
import com.edatasolutions.utils.ConstantCodes;
import com.edatasolutions.utils.MD5;
import com.edatasolutions.utils.NetworkConectivity;
import com.edatasolutions.utils.RSA;
import com.edatasolutions.utils.SessionManager;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityLogin extends AppCompatActivity {

    private EditText login_password, login_username;
    private Button login_btn;
    private ArrayList<String> roleList = new ArrayList<>();
    private String selected_roleid="";
    private String device_no = "";
    private String platform = "android";
    private String app_version = "1.0";
    private SessionManager sessionManager;

    private FrameLayout selecttype_lay;
    private TextView selecttype_txt;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        device_no = Settings.Secure.getString(ActivityLogin.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("device_no",device_no);
        sessionManager = new SessionManager(ActivityLogin.this);
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_btn);
        selecttype_lay = findViewById(R.id.selecttype_lay);
        selecttype_txt = findViewById(R.id.selecttype_txt);
        //readFromLocalStorage();

        selectRole();

        if (roleList.isEmpty()){
            selected_roleid = "1";
            selecttype_lay.setVisibility(View.GONE);
            selecttype_txt.setVisibility(View.GONE);
        }else {
            selecttype_txt.setVisibility(View.VISIBLE);
            selecttype_lay.setVisibility(View.VISIBLE);
        }
        Spinner login_role = findViewById(R.id.login_role);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, roleList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        login_role.setAdapter(arrayAdapter);
        login_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String rolename = parent.getItemAtPosition(position).toString();
                Log.e("rolename",rolename);
                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityLogin.this);
                databaseAccess.open();

                selected_roleid = databaseAccess.getroleId(rolename);
                databaseAccess.close();

                //Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityLogin.this);
                databaseAccess.open();

                String email = login_username.getText().toString().trim();
                String pass = databaseAccess.getPassword(email);
                String userRoleId = databaseAccess.getUserRoleId(email);
                String actual_pass = login_password.getText().toString().trim();
                String encryptedText = MD5.encrypt(email + actual_pass);
                Log.e("checkk", userRoleId);


//                    if (userRoleId.equals(selected_roleid)){
                if (selected_roleid.equals("2")) {
                    if (userRoleId.equals(selected_roleid)) {
                        if (encryptedText.equals(pass)) {
                            DatabaseAccess databaseAccess1 = new DatabaseAccess(ActivityLogin.this);
                            databaseAccess1.open();
                            String id = databaseAccess1.getOfficerId(email);
                            String badge_no = databaseAccess1.getOfficerBadgeNo(email);
                            String name = databaseAccess1.getOfficerName(email);


                            sessionManager.createAdminLogin(id, name, email, userRoleId, badge_no, "Officer", "", "");

                            Intent i = new Intent(ActivityLogin.this, ActivityHome.class);
                            startActivity(i);
                            finish();

                            databaseAccess1.close();

                        } else {
                            Toast.makeText(ActivityLogin.this, "Please enter correct information", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ActivityLogin.this, "Please enter correct information", Toast.LENGTH_SHORT).show();
                    }

                } else if (selected_roleid.equals("1")) {
                    if (userRoleId.equals(selected_roleid)) {
                        if (NetworkConectivity.checkConnectivity(ActivityLogin.this)) {

                            String uniqueId = UUID.randomUUID().toString();
                            String admin_pass = RSA.encryptData(uniqueId + actual_pass);
                            Log.e("uniqueId", uniqueId);
                            Log.e("admin_pass", admin_pass);
                            callAppVersion(uniqueId);
                            callAdminLogin(email, uniqueId, admin_pass,selected_roleid);
                        } else {
                            Toast.makeText(ActivityLogin.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                    } else if (userRoleId.equals("")) {
                        if (NetworkConectivity.checkConnectivity(ActivityLogin.this)) {

                            String uniqueId = UUID.randomUUID().toString();
                            String admin_pass = RSA.encryptData(uniqueId + actual_pass);
                            callAppVersion(uniqueId);
                            callAdminLogin(email, uniqueId, admin_pass,selected_roleid);
                        } else {
                            Toast.makeText(ActivityLogin.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                } else {
                    Toast.makeText(ActivityLogin.this, "Please enter correct information", Toast.LENGTH_SHORT).show();
                }


            } else {
                        Toast.makeText(ActivityLogin.this,"Please enter correct information",Toast.LENGTH_SHORT).show();
                    }

                databaseAccess.close();
            }
        });

    }


    private void selectRole(){

        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityLogin.this);
        databaseAccess.open();
        roleList = databaseAccess.getRole();
        Collections.reverse(roleList);
        databaseAccess.close();
    }


    private void callAdminLogin( final String username, final String uuid, final String password, final String selected_roleid) {

        final ProgressDialog progressdialog = new ProgressDialog(ActivityLogin.this);
        progressdialog.setMessage("Loading....");
        progressdialog.show();

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

       // admin_login admin_login = new admin_login(ConstantCodes.LANG_ID, username,uuid, password);
        Call<JsonObject> call = api.adminLogin(ConstantCodes.LANG_ID, device_no, username , uuid, password,selected_roleid);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                progressdialog.dismiss();
                Log.d("response", response.toString());
                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));

                    Log.d("data", totaldata.getString("message"));
                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));
                        JSONObject userdata = jsonArray.getJSONObject(0);


                        String user_id= userdata.getString("user_id");
                        String name= userdata.getString("name");
                        String email= userdata.getString("email");
                        String role_id= userdata.getString("role_id");
                        String user_role= userdata.getString("user_role");
                        String status= userdata.getString("status");
                        String token = userdata.getString("token");

                        String dateString ="";
                        String timeString ="";

                        String is_device_registered = userdata.getString("is_device_registered");

                        if (is_device_registered.equals("true")){


                            if (userdata.getString("last_admin_sync_datetime").equals("0000-00-00 00:00:00")){
                                Log.e("last_time_sync","0000-00-00_____"+dateString+"   "+timeString);
                                dateString = "";
                                timeString = "";
                                sessionManager.createLastAdminSyncSession(dateString,timeString);

                            }else if (userdata.getString("last_admin_sync_datetime").equals("-")){
                                dateString = "";
                                timeString = "";
                                sessionManager.createLastAdminSyncSession(dateString,timeString);

                            }else {

                                String timestamp = userdata.getString("last_admin_sync_datetime");
                                StringTokenizer tk = new StringTokenizer(timestamp);

                                dateString = tk.nextToken();  // <---  yyyy-mm-dd
                                timeString = tk.nextToken();  // <---  hh:mm:ss

                                sessionManager.createLastAdminSyncSession(dateString,timeString);
                            }

                        }else {

                            dateString = "";
                            timeString = "";
                            sessionManager.createLastAdminSyncSession(dateString,timeString);
                            Toast.makeText(ActivityLogin.this, "This device is not activated. Please contact with admin team", Toast.LENGTH_LONG).show();
                        }

                        Log.e("last_time_sync","timestamp"+  "  "+dateString+"   "+timeString);
                        sessionManager.createAdminLogin(user_id, name, email, role_id,"", user_role, status,token);
                        Intent i =new Intent(ActivityLogin.this,MainActivity.class);
                        startActivity(i);
                        finish();

                    }else {
                        Toast.makeText(ActivityLogin.this, totaldata.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                  //  Toast.makeText(ActivityLogin.this, "catch failure", Toast.LENGTH_SHORT).show();
                    progressdialog.dismiss();
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                progressdialog.dismiss();
               // Toast.makeText(ActivityLogin.this, "failure", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }


    private void callAppVersion(final String uuid) {

        final ProgressDialog progressdialog = new ProgressDialog(ActivityLogin.this);
        progressdialog.setMessage("Loading....");
        progressdialog.show();

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

        Call<JsonObject> call = api.checkVersion(ConstantCodes.LANG_ID, uuid, platform,app_version);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

                progressdialog.dismiss();
                //String s = null;
                Log.d("status", response.toString());
                assert response.body() != null;

                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    Log.d("data", totaldata.getString("success"));


                } catch (Exception ex) {
                    progressdialog.dismiss();
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                progressdialog.dismiss();
                //  Toast.makeText(ActivityLogin.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }
}