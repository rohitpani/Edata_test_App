package com.edatasolutions.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edatasolutions.R;
import com.edatasolutions.database.DatabaseAccess;
import com.edatasolutions.models.DriverLicense;
import com.edatasolutions.utils.SessionManager;
import com.edatasolutions.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityDriver extends AppCompatActivity {

    private Button driver_next_btn;
    private ImageView img_scanner,img_menu,img_back;
    private TextView header_text,dob,state_txt,driverstate_txt,city_txt;
    private Spinner ethncity, race, hair, eyes,driverlicensetype,driver_suffix,height,statee;
    private ArrayList<String> ethncityList = new ArrayList<>();
    private ArrayList<String> raceList = new ArrayList<>();
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayList<String> stateList = new ArrayList<>();
    private ArrayList<String> suffixList = new ArrayList<>();
    private ArrayList<String> hairList = new ArrayList<>();
    private ArrayList<String> eyesList = new ArrayList<>();
    private ArrayList<String> heightList = new ArrayList<>();
    private ArrayList<String> driverstateList = new ArrayList<>();
    private ArrayList<String> licensetypeList = new ArrayList<>();
    private EditText driver_firstname, driver_middlename, driver_lastname, driver_address1,driver_address2,driver_zipcode;
    private EditText driver_license_number, weight;
    private String s_driver_firstname="", s_driver_middlename="", s_driver_lastname="", s_driver_suffix="",s_driver_suffixid="", s_driver_address1="",s_driver_address2="",s_driver_city="",s_driver_cityid="",s_driver_state="",s_driver_stateid="",s_driver_zipcode="";
    private String s_driver_license_number="", s_driver_statee="",s_driver_stateeid="", s_dob="", s_sex="M", s_hair="",s_hairid="", s_eyes="",s_eyesid="", s_height="", s_weight="",s_driver_license_type="",s_driver_license_typeid="", s_comm_drivers_license="N",s_ethncity="",s_ethncityid="",s_race="",s_raceid="";
    private SessionManager sessionManager;
    private LinearLayout firstname_lay,lastname_lay,address_lay,dob_lay,sex_lay,weight_lay,driver_license_no_lay,zip_lay,city_lay,state_lay;
    private FrameLayout hair_lay,eye_lay,height_lay;
    private RadioGroup sex,comm_drivers_license;
    private RadioButton r_sex,r_comm_drivers_license;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String selected = "";
    private ScrollView driver_scroll;
    private RadioButton rb_sex_m, rb_sex_f, rb_cdl_yes, rb_cdl_no;
    private String s_scannedheight="";
    private String L_city="", L_state="",L_haircolor="",L_eyecolor="", L_driverState="", L_height="",L_suffix="",L_driver_license_type="";

    private ArrayAdapter<String> adapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        sessionManager = new SessionManager(ActivityDriver.this);

        init();

        if (getIntent().hasExtra("selected")){
            selected = getIntent().getStringExtra("selected");
        }

        getAllListData();
        checkLicenseData();



        String session_driverstateid = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE_ID);
        if (s_driver_state.equals("")){
            if (session_driverstateid.equals("")){
                getcity();

            }else {
                getcitywithstateid(session_driverstateid);
            }
        }else {
            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
            databaseAccess.open();
            String selected_id = databaseAccess.getSelectedOnlyStateId(s_driver_state);
            databaseAccess.close();
            getcitywithstateid(selected_id);
        }


        getSpinnerData();


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        driver_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();

            }
        });

        driver_license_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (driver_license_number.getText().toString().trim().equals("")){
                    statee.setEnabled(false);
                    statee.setSelection(0);
                }else {
                    statee.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        weight.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {

                        String weight_txt = weight.getText().toString();

                        if (weight_txt.length()==2){
                            weight.setText("0"+weight_txt);
                        }else if (weight_txt.length()==1){
                            weight.setText("00"+weight_txt);
                        }else {
                            weight.setText(weight_txt);
                        }

                        Utils.hideKeyboard(ActivityDriver.this);
                        v.setCursorVisible(false);
                        return true; // consume.
                    }
                }

                return false;
            }
        });

        img_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(ActivityCompat.checkSelfPermission(ActivityDriver.this , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(ActivityDriver.this , new String[]{Manifest.permission.CAMERA} , REQUEST_CAMERA_PERMISSION);
                }
                Intent i = new Intent(ActivityDriver.this, ActivityScanCode.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter,R.anim.exit);
              //  finish();

            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(ActivityDriver.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "MM-dd-yyyy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        dob.setText(sdf.format(myCalendar.getTime()));
                        dob.setTextColor(Color.parseColor("#444444"));


                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                //mDatePicker.setTitle("Select date");
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });

        driverstate_txt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (driver_license_number.getText().toString().equals("")){
                    driverstate_txt.setClickable(false);
                }else {
                    driverstate_txt.setClickable(true);
                }

                return false;
            }
        });

        driverstate_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(ActivityDriver.this);
                LayoutInflater inflater = (LayoutInflater) ActivityDriver.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityDriver.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, driverstateList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityDriver.this.adapter.getFilter().filter(cs);

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                  int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String selected_value = parent.getItemAtPosition(position).toString();
                        autoCompleteTextView1.setText(selected_value);
                        driverstate_txt.setText(selected_value);

                        String driverstate = driverstate_txt.getText().toString().trim();

                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                        databaseAccess.open();
                        s_driver_statee = databaseAccess.getSelectedStateValue(driverstate);
                        s_driver_stateeid = databaseAccess.getSelectedStateId(driverstate, s_driver_statee);
                        databaseAccess.close();

                        dialog.dismiss();
                    }
                });


                dialog.setContentView(post);
                dialog.setTitle("Post");
                dialog.show();

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(layoutParams);

            }
        });

        state_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Dialog dialog = new Dialog(ActivityDriver.this);
                LayoutInflater inflater = (LayoutInflater) ActivityDriver.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityDriver.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, stateList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityDriver.this.adapter.getFilter().filter(cs);

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                  int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String selected_value = parent.getItemAtPosition(position).toString();
                        autoCompleteTextView1.setText(selected_value);
                        state_txt.setText(selected_value);

                        String state = state_txt.getText().toString().trim();

                        city_txt.setText("");
                        city_txt.setHint(getResources().getString(R.string.city));
                        s_driver_city="";
                        s_driver_cityid="";

                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                        databaseAccess.open();
                        s_driver_state = databaseAccess.getSelectedStateValue(state);
                        s_driver_stateid = databaseAccess.getSelectedStateId(state,s_driver_state);
                        databaseAccess.close();

                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });


                dialog.setContentView(post);
                dialog.setTitle("Post");
                dialog.show();

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(layoutParams);

            }
        });

        city_txt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (s_driver_stateid.equals("")) {
                    city_txt.setClickable(false);
                }else {
                    city_txt.setClickable(true);
                }
                return false;
            }
        });

        city_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getcitywithstateid(s_driver_stateid);

                final Dialog dialog = new Dialog(ActivityDriver.this);
                LayoutInflater inflater = (LayoutInflater) ActivityDriver.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityDriver.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, cityList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityDriver.this.adapter.getFilter().filter(cs);

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                  int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String selected_value = parent.getItemAtPosition(position).toString();
                        autoCompleteTextView1.setText(selected_value);
                        city_txt.setText(selected_value);


                        String city = city_txt.getText().toString().trim();

                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                        databaseAccess.open();
                        s_driver_city = databaseAccess.getSelectedCityValue(city,s_driver_stateid);
                        s_driver_cityid = databaseAccess.getSelectedCityIdwithStateId(city,s_driver_city, s_driver_stateid);

                        databaseAccess.close();

                        dialog.dismiss();
                    }
                });


                dialog.setContentView(post);
                dialog.setTitle("Post");
                dialog.show();

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(layoutParams);

            }
        });
    }


    private void init(){
        driver_next_btn = findViewById(R.id.driver_next_btn);
        img_scanner = findViewById(R.id.img_scanner);
        img_scanner.setVisibility(View.VISIBLE);
        header_text = findViewById(R.id.header_text);
        img_menu = findViewById(R.id.img_menu);
        ethncity = findViewById(R.id.ethncity);
        race = findViewById(R.id.race);
        sex = findViewById(R.id.sex);
        img_back = findViewById(R.id.img_back);
        state_txt = findViewById(R.id.state_txt);
        driverstate_txt = findViewById(R.id.driverstate_txt);
        city_txt = findViewById(R.id.city_txt);

        driver_scroll = findViewById(R.id.driver_scroll);

        img_menu.setVisibility(View.GONE);
        header_text.setText(R.string.driver);

        driver_firstname = findViewById(R.id.driver_firstname);
        driver_middlename = findViewById(R.id.driver_middlename);
        driver_lastname = findViewById(R.id.driver_lastname);

        driver_firstname.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        driver_middlename.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        driver_lastname.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        driver_suffix = findViewById(R.id.driver_suffix);
        driver_address1 = findViewById(R.id.driver_address1);
        driver_address2 = findViewById(R.id.driver_address2);
        driver_zipcode = findViewById(R.id.driver_zipcode);
        driver_license_number = findViewById(R.id.driver_license_number);
        statee = findViewById(R.id.statee);
        dob = findViewById(R.id.dob);
        hair = findViewById(R.id.hair);
        eyes = findViewById(R.id.eyes);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        driverlicensetype = findViewById(R.id.driverlicensetype);
        comm_drivers_license = findViewById(R.id.comm_drivers_license);
        rb_sex_m = findViewById(R.id.rb_sex_m);
        rb_sex_f = findViewById(R.id.rb_sex_f);
        rb_cdl_yes = findViewById(R.id.rb_cdl_yes);
        rb_cdl_no = findViewById(R.id.rb_cdl_no);

        firstname_lay = findViewById(R.id.firstname_lay);
        lastname_lay = findViewById(R.id.lastname_lay);
        address_lay = findViewById(R.id.address_lay);
        dob_lay = findViewById(R.id.dob_lay);
        sex_lay = findViewById(R.id.sex_lay);
        weight_lay = findViewById(R.id.weight_lay);
        city_lay = findViewById(R.id.city_lay);
        state_lay = findViewById(R.id.state_lay);
        hair_lay = findViewById(R.id.hair_lay);
        eye_lay = findViewById(R.id.eye_lay);
        height_lay = findViewById(R.id.height_lay);
        driver_license_no_lay = findViewById(R.id.driver_license_no_lay);
        zip_lay = findViewById(R.id.zip_lay);


        String session_firstname = sessionManager.getDriverSession().get(SessionManager.DRIVER_FIRST_NAME);
        String session_middlename = sessionManager.getDriverSession().get(SessionManager.DRIVER_MIDDLE_NAME);
        String session_lastname = sessionManager.getDriverSession().get(SessionManager.DRIVER_LAST_NAME);
        String session_address1 = sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS1);
        String session_address2 = sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS2);
        String session_zipcode = sessionManager.getDriverSession().get(SessionManager.DRIVER_ZIPCODE);
        String session_licenseno = sessionManager.getDriverSession().get(SessionManager.DRIVER_LICENSE_NO);
//        String session_driverstate = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATEE);

        String session_dob = sessionManager.getDriverSession().get(SessionManager.DOB);
        String session_height = sessionManager.getDriverSession().get(SessionManager.HEIGHT);
        String session_weight = sessionManager.getDriverSession().get(SessionManager.WEIGHT);
        String session_sex = sessionManager.getDriverSession().get(SessionManager.SEX);
        String session_commdriverlicense = sessionManager.getDriverSession().get(SessionManager.COMM_DRIVER_LICENSE);

        String session_statee = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATEE);
        String session_stateeid = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATEE_ID);


        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
        databaseAccess.open();

        assert session_statee != null;
        if (!session_statee.equals("")){
            s_driver_statee = session_statee;
            s_driver_stateeid = session_stateeid;
            String selected_value = databaseAccess.getSelectedStateName(session_statee,session_stateeid);

            driverstate_txt.setText(selected_value);
        }


        String session_driverstate = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE);
        String session_driverstateid = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE_ID);

        assert session_driverstate != null;
        if (!session_driverstate.equals("")){

            s_driver_state = session_driverstate;
            s_driver_stateid = session_driverstateid;

            String selected_value = databaseAccess.getSelectedStateName(session_driverstate,session_driverstateid);

            state_txt.setText(selected_value);
        }else {

            String selected_value = "California";
            s_driver_state = databaseAccess.getSelectedStateValue(selected_value);
            s_driver_stateid = databaseAccess.getSelectedStateId(selected_value, s_driver_state);
            state_txt.setText(selected_value);

        }

        String  session_drivercity = sessionManager.getDriverSession().get(SessionManager.DRIVER_CITY);
        String session_drivercityid = sessionManager.getDriverSession().get(SessionManager.DRIVER_CITY_ID);

        assert session_drivercity != null;
        if (!session_drivercity.equals("")){

            s_driver_city = session_drivercity;
            s_driver_cityid = session_drivercityid;

            String selected_value = databaseAccess.getSelectedCityName(session_drivercity,session_drivercityid);

            city_txt.setText(selected_value);
        }

        databaseAccess.close();


        if (session_sex!=null){
            if (session_sex.equals("M")){
                rb_sex_m.setChecked(true);
                rb_sex_f.setChecked(false);
            }else if (session_sex.equals("F")){
                rb_sex_m.setChecked(false);
                rb_sex_f.setChecked(true);
            }else {
                rb_sex_m.setChecked(false);
                rb_sex_f.setChecked(false);
            }
        }

        if (session_commdriverlicense!=null){
            if (session_commdriverlicense.equals("Y")){
                rb_cdl_yes.setChecked(true);
                rb_cdl_no.setChecked(false);
            }else if (session_commdriverlicense.equals("N")){
                rb_cdl_yes.setChecked(false);
                rb_cdl_no.setChecked(true);
            }else {
                rb_cdl_yes.setChecked(false);
                rb_cdl_no.setChecked(false);
            }
        }

        if (session_firstname!=null){
            driver_firstname.setText(session_firstname);
        }
        if (session_middlename!=null){
            driver_middlename.setText(session_middlename);
        }
        if (session_lastname!=null){
            driver_lastname.setText(session_lastname);
        }
        if (session_address1!=null){
            driver_address1.setText(session_address1);
        }
        if (session_address2!=null){
            driver_address2.setText(session_address2);
        }
        if (session_zipcode!=null){
            driver_zipcode.setText(session_zipcode);
        }

        assert session_licenseno != null;
        if (!session_licenseno.equals("")){
            if (s_driver_license_number.length()==0){
                driver_license_number.setText(session_licenseno);
            }else {
                driver_license_number.setText(session_licenseno);
            }

        }

        if (session_dob!=null){
            dob.setText(session_dob);
        }

        if (session_weight!=null){
            weight.setText(session_weight);
        }
    }

    private void checkValidation(){

        s_driver_firstname = driver_firstname.getText().toString().trim();
        s_driver_middlename = driver_middlename.getText().toString().trim();
        s_driver_lastname = driver_lastname.getText().toString().trim();
        s_driver_address1 = driver_address1.getText().toString().trim();
        s_driver_address2 = driver_address2.getText().toString().trim();
        s_driver_zipcode = driver_zipcode.getText().toString().trim();
        s_driver_license_number = driver_license_number.getText().toString().trim();
//        s_driver_statee = driver_statee.getText().toString().trim();

        if (dob.getText().toString().equals("DOB")){
            s_dob = "";
        }else {
            s_dob = dob.getText().toString().trim();
        }

        s_weight = weight.getText().toString().trim();

        if (sex.getCheckedRadioButtonId() == -1)
        {
            s_sex ="";
        }
        else
        {
            int comm_selectedId = sex.getCheckedRadioButtonId();
            r_sex = (RadioButton) findViewById(comm_selectedId);
            if (r_sex.getText().toString().equals("Male")){
                s_sex ="M";
            }else {
                s_sex ="F";
            }
        }

        if (comm_drivers_license.getCheckedRadioButtonId() == -1)
        {
            s_comm_drivers_license ="N";
        }
        else
        {
            int comm_selectedId = comm_drivers_license.getCheckedRadioButtonId();
            r_comm_drivers_license = (RadioButton) findViewById(comm_selectedId);
            if (r_comm_drivers_license.getText().toString().equals("Yes")){
                s_comm_drivers_license ="Y";
            }else {
                s_comm_drivers_license ="N";
            }
        }

        boolean license_response = Utils.licenseValidation(s_driver_license_number);
        boolean dob_response = Utils.dobdateValidate(s_dob);

        boolean valid = true;
        if ( TextUtils.isEmpty(s_driver_firstname) ) {
            firstname_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            firstname_lay.getParent().requestChildFocus(firstname_lay,firstname_lay);
            valid = false;
        }else {
            firstname_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
        }

        if ( TextUtils.isEmpty(s_driver_lastname) ) {
            lastname_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            lastname_lay.getParent().requestChildFocus(lastname_lay,lastname_lay);
            valid = false;
        }else {
            lastname_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
        }

        if ( TextUtils.isEmpty(s_driver_address1) ) {
            address_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            address_lay.getParent().requestChildFocus(address_lay,address_lay);
            valid = false;
        }else {

            if(s_driver_address1.startsWith("0")){
                address_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                address_lay.getParent().requestChildFocus(address_lay,address_lay);
                valid = false;
            }else if (s_driver_address1.length()<=4){
                address_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                address_lay.getParent().requestChildFocus(address_lay,address_lay);
                valid = false;
            }else {
                address_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
            }

        }

        if ( TextUtils.isEmpty(s_driver_city) ) {
            city_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            city_lay.getParent().requestChildFocus(city_lay,city_lay);

            valid = false;
        }else {
            city_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
        }

        if ( TextUtils.isEmpty(s_driver_state) ) {
            state_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            state_lay.getParent().requestChildFocus(state_lay,state_lay);
            valid = false;
        }else {
            state_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
        }


        if ( TextUtils.isEmpty(s_sex) ) {
            sex_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            sex_lay.getParent().requestChildFocus(sex_lay,sex_lay);
            valid = false;
        }else {
            sex_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
        }

        if ( TextUtils.isEmpty(s_hair) ) {
            hair_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            hair_lay.getParent().requestChildFocus(hair_lay,hair_lay);
            valid = false;
        }else {
            hair_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
        }

        if ( TextUtils.isEmpty(s_eyes) ) {
            eye_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            eye_lay.getParent().requestChildFocus(eye_lay,eye_lay);
            valid = false;
        }else {
            eye_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
        }

        if ( TextUtils.isEmpty(s_height) ) {
            height_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            height_lay.getParent().requestChildFocus(height_lay,height_lay);
            valid = false;
        }else {
            height_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
        }

        if ( TextUtils.isEmpty(s_weight) ) {
            weight_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            weight_lay.getParent().requestChildFocus(weight_lay,weight_lay);
            valid = false;
        }else {
            int weight = Integer.parseInt(s_weight);
            if (isInRange(1,500,weight)){
                weight_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
            }else {
                weight_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                weight_lay.getParent().requestChildFocus(weight_lay,weight_lay);
                valid = false;
            }

        }

        if (!TextUtils.isEmpty(s_driver_zipcode)) {

            if (s_driver_zipcode.length() < 5) {
                zip_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                zip_lay.getParent().requestChildFocus(zip_lay,zip_lay);
                valid = false;
               // Toast.makeText(ActivityDriver.this, "Please enter correct zipcode", Toast.LENGTH_SHORT).show();
            } else {

                if(!s_driver_stateid.equals("")&&!s_driver_cityid.equals("")&&!s_driver_state.equals("CA")){

                    DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                    databaseAccess.open();
                    ArrayList<String> arrayList = databaseAccess.checkZipcode(s_driver_stateid,s_driver_cityid);
                    String zipcode = databaseAccess.checkSingleZipcode(s_driver_stateid,s_driver_cityid);
                    databaseAccess.close();

                    if (arrayList.contains(s_driver_zipcode)){
                        zip_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
                    }else {
                        zip_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
                        Toast.makeText(ActivityDriver.this, "Entered zip code does not match with selected city", Toast.LENGTH_SHORT).show();
                    }
                }

                if (s_driver_state.equals("CA")) {
                    int Digit = Integer.parseInt(s_driver_zipcode);
                    if (!(firstDigit(Digit) == 9)) {
                        zip_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                        zip_lay.getParent().requestChildFocus(zip_lay,zip_lay);
                        valid = false;


                    }else {

                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                        databaseAccess.open();
                        ArrayList<String> arrayList = databaseAccess.checkZipcode(s_driver_stateid,s_driver_cityid);
                        databaseAccess.close();

                        if (arrayList.contains(s_driver_zipcode)){
                            zip_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
                        }else {
                            zip_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
                            Toast.makeText(ActivityDriver.this, "Entered zip code does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        }

        if (!TextUtils.isEmpty(s_driver_license_number) ) {

            if (s_driver_statee.equals("CA")){
                if (!license_response){
                    driver_license_no_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                    driver_license_no_lay.getParent().requestChildFocus(driver_license_no_lay,driver_license_no_lay);
                    valid = false;
                }else {
                    driver_license_no_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
                }
            }else {
                driver_license_no_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
            }

        }else {
            driver_license_no_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
        }

        if (!TextUtils.isEmpty(s_dob) ) {
            if (!dob_response){
                dob_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                dob_lay.getParent().requestChildFocus(dob_lay,dob_lay);
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                try {
                    Date parseddate = sdf.parse(s_dob);
                    Calendar c2 = Calendar.getInstance();
                    c2.add(Calendar.YEAR, -13);

                    Calendar c3 = Calendar.getInstance();
                    c3.add(Calendar.YEAR, -100);
                    Date dateObj2 = new Date(System.currentTimeMillis());
                    assert parseddate != null;
                    if (parseddate.before(c2.getTime())) {
                        Toast.makeText(getApplicationContext(),"Age is above 100 year",Toast.LENGTH_SHORT).show();
                    }
                    else if(parseddate.after(c3.getTime())){
                        Toast.makeText(getApplicationContext(),"Age is under 13 year",Toast.LENGTH_SHORT).show();
                    }

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                valid = false;
            }else {
                dob_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
            }

        }else {
            dob_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            dob_lay.getParent().requestChildFocus(dob_lay,dob_lay);
            valid = false;
        }

        if(TextUtils.isEmpty(s_driver_firstname) && TextUtils.isEmpty(s_driver_lastname) && TextUtils.isEmpty(s_driver_address1) && TextUtils.isEmpty(s_driver_city) && TextUtils.isEmpty(s_sex) && TextUtils.isEmpty(s_hair) && TextUtils.isEmpty(s_eyes) && TextUtils.isEmpty(s_height) && TextUtils.isEmpty(s_weight) && TextUtils.isEmpty(s_dob)){
            driver_scroll.fullScroll(View.FOCUS_UP);
        }

            if ( valid ) {

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                databaseAccess.open();
                String first_name  = databaseAccess.checkNameWithNameMaster(s_driver_firstname);
                String last_name  = databaseAccess.checkNameWithNameMaster(s_driver_lastname);
                databaseAccess.close();


                if (first_name.equals("")&&last_name.equals("")){

                    showAlertMessage(getResources().getString(R.string.first_and_last_name_not_found));

                }else if (first_name.equals("")||last_name.equals("")){

                    if (first_name.equals("")){
                        showAlertMessage(getResources().getString(R.string.first_name_not_found));
                    }
                    if (last_name.equals("")){
                        showAlertMessage(getResources().getString(R.string.last_name_not_found));
                    }

                }else {

                    sessionManager.saveDriver("Y");
                    sessionManager.ClearDriverEntery();
                    sessionManager.createDriverSession(s_driver_firstname, s_driver_middlename, s_driver_lastname, s_driver_suffix,s_driver_suffixid, s_driver_address1,s_driver_address2,s_driver_city,s_driver_cityid,s_driver_state,s_driver_stateid,s_driver_zipcode,s_driver_license_number, s_driver_statee,s_driver_stateeid, s_dob, s_sex, s_hair,s_hairid, s_eyes,s_eyesid, s_height, s_weight,s_driver_license_type,s_driver_license_typeid, s_race,s_raceid,s_ethncity,s_ethncityid, s_comm_drivers_license);
                    Intent i = new Intent(ActivityDriver.this, ActivityVehicle.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                }

            }



    }
    public void showAlertMessage(String message) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityDriver.this);

        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                sessionManager.saveDriver("Y");
                sessionManager.ClearDriverEntery();
                sessionManager.createDriverSession(s_driver_firstname, s_driver_middlename, s_driver_lastname, s_driver_suffix,s_driver_suffixid, s_driver_address1,s_driver_address2,s_driver_city,s_driver_cityid,s_driver_state,s_driver_stateid,s_driver_zipcode,s_driver_license_number, s_driver_statee,s_driver_stateeid, s_dob, s_sex, s_hair,s_hairid, s_eyes,s_eyesid, s_height, s_weight,s_driver_license_type,s_driver_license_typeid, s_race,s_raceid,s_ethncity,s_ethncityid, s_comm_drivers_license);
                Intent i = new Intent(ActivityDriver.this, ActivityVehicle.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
        alertDialog.show();

    }


    private void  checkLicenseData(){

        String dateofbirth ="";

        Intent intent = getIntent();
        if (intent != null) {
            DriverLicense driverLicense = (DriverLicense) intent.getParcelableExtra("DriverLicense");
            if (driverLicense != null) {
                L_driver_license_type = driverLicense.driverlicenseType;
                s_driver_firstname = driverLicense.firstName;
                s_driver_middlename = driverLicense.middleName;
                s_driver_lastname = driverLicense.lastName;
                s_sex = driverLicense.gender;
                s_driver_address1 = driverLicense.addressStreet;
                s_driver_zipcode = driverLicense.addressZip;
                s_driver_license_number = driverLicense.licenseNumber;
                dateofbirth = driverLicense.birthDate;
                L_height = driverLicense.height;
                s_weight = driverLicense.weight;
                L_haircolor = driverLicense.hairColor;
                L_eyecolor = driverLicense.eyeColor;
                L_suffix = driverLicense.suffix;
                L_driverState = driverLicense.driverState;
                L_city = driverLicense.addressCity;
                s_driver_state = driverLicense.addressState;


                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                databaseAccess.open();

                if (!s_driver_state.equals("")) {
                    String selected_value = databaseAccess.getSelectedStateDescription(s_driver_state);
                    s_driver_stateid = databaseAccess.getSelectedStateId(selected_value,s_driver_state);
                    state_txt.setText(selected_value);
                }

                if (!L_driverState.equals("")) {
                    String selected_value = databaseAccess.getSelectedStateDescription(L_driverState);
                    s_driver_stateeid = databaseAccess.getSelectedStateId(selected_value,L_driverState);
                    driverstate_txt.setText(selected_value);
                }

                if (!L_city.equals("")) {
                    getcitywithstateid(s_driver_stateid);
                    if (cityList.contains(L_city)) {
                       // s_driver_city = databaseAccess.getSelectedCityValue(L_city,s_driver_stateid);
                        s_driver_city = databaseAccess.getSelectedCityValue(L_city,s_driver_stateid);
                        s_driver_cityid = databaseAccess.getSelectedCityIdwithStateId(L_city,s_driver_city, s_driver_stateid);
                        //add value desc and state_id
                        Log.e("Checkfree",""+s_driver_cityid);
//                        String selected_value = databaseAccess.getSelectedCityName(s_driver_city,s_driver_cityid);
                        city_txt.setText(L_city);
                    }else {
                        s_driver_city="";
                        s_driver_cityid="";
                        city_txt.setText("");
                        city_txt.setHint(getResources().getString(R.string.city));
                    }

                }




                if (s_sex.equals("1")){
                    rb_sex_m.setChecked(true);
                    rb_sex_f.setChecked(false);
                }else {
                    rb_sex_m.setChecked(false);
                    rb_sex_f.setChecked(true);

                }

                if (L_height.contains("-")){

                    s_scannedheight = L_height.replace("'","").replace("-","");
                    s_scannedheight = s_scannedheight.substring(0,3);


                }else {
                    int inch = Integer.parseInt(L_height)/12;
                    int ft = Integer.parseInt(L_height)%12;
                    String feet="";
                    if (ft<10){
                        feet="0"+ft;
                    } else {
                        feet=String.valueOf(ft);
                    }

                    s_scannedheight = inch+feet;
                }


                driver_firstname.setText(s_driver_firstname);
                driver_middlename.setText(s_driver_middlename);
                driver_lastname.setText(s_driver_lastname);
                driver_address1.setText(s_driver_address1);
                weight.setText(s_weight);
                driver_zipcode.setText(s_driver_zipcode);
                driver_license_number.setText(s_driver_license_number);

                DateFormat inputFormat = new SimpleDateFormat("ddMMyyyy");
                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                try {
                    assert dateofbirth != null;
                    Date date = inputFormat.parse(dateofbirth);
                    assert date != null;
                    String outputDateStr = outputFormat.format(date);
                    s_dob=outputDateStr;
                    dob.setText(outputDateStr);
                }catch (Exception ex){
                    Log.d("error","error");
                }

            }
        }

    }
    @Override
    public void onBackPressed() {

        Intent i = new Intent(ActivityDriver.this, ActivityHome.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        super.onBackPressed();
//        super.onBackPressed();
//        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
//        if (selected.equals("driver_license")){
//            Intent i = new Intent(ActivityDriver.this, ActivityHome.class);
////            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
//            finish();
//            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
////        }else if (!driver_firstname.getText().toString().equals("")){
////            Intent i = new Intent(ActivityDriver.this, ActivityHome.class);
////            startActivity(i);
////            finish();
////            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
//        }else {
//            super.onBackPressed();
//            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
//        }


    }

    private void getAllListData(){
        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
        databaseAccess.open();
        ethncityList = databaseAccess.getEthnicity();
        raceList = databaseAccess.getRace();
        stateList = databaseAccess.getState();
        driverstateList = databaseAccess.getState();
        hairList = databaseAccess.getHairs();
        heightList = databaseAccess.getHeights();
        eyesList = databaseAccess.getEyes();
        licensetypeList = databaseAccess.getDriversLicenseType();
        suffixList = databaseAccess.getSuffix();
        databaseAccess.close();
    }


    private void getcitywithstateid(String state_id){
        cityList.clear();
        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
        databaseAccess.open();
        cityList = databaseAccess.getCityWithStateId(state_id);
        databaseAccess.close();
    }

    private void getcity(){
        cityList.clear();
        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
        databaseAccess.open();
        cityList = databaseAccess.getCity();
        databaseAccess.close();
    }


    private void getSpinnerData(){

        String session_ethncity = sessionManager.getDriverSession().get(SessionManager.ETHNCITY);
        String session_ethncityid = sessionManager.getDriverSession().get(SessionManager.ETHNCITY_ID);
        ArrayAdapter<String> eth_arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, ethncityList);
        eth_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ethncity.setAdapter(eth_arrayAdapter);
        if (session_ethncity!=null){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedEthnicityName(session_ethncity,session_ethncityid);
            databaseAccess.close();

            int spinnerPosition = eth_arrayAdapter.getPosition(selected_value);
            ethncity.setSelection(spinnerPosition);
        }
        ethncity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                databaseAccess.open();
                String selected_value = databaseAccess.getSelectedEthnicity(selected);
                String selected_id = databaseAccess.getSelectedEthnicityId(selected_value,selected);
                databaseAccess.close();

                if (selected.equals("ETHNCITY")){
                    s_ethncity="";
                    s_ethncityid="";
                }else {
                    s_ethncity=selected_value;
                    s_ethncityid=selected_id;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        String session_race = sessionManager.getDriverSession().get(SessionManager.RACE);
        String session_raceid = sessionManager.getDriverSession().get(SessionManager.RACE_ID);
        ArrayAdapter<String> race_arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, raceList);
        race_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        race.setAdapter(race_arrayAdapter);
        if (session_race!=null){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedRaceName(session_race,session_raceid);
            databaseAccess.close();

            int spinnerPosition = race_arrayAdapter.getPosition(selected_value);
            race.setSelection(spinnerPosition);
        }
        race.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                databaseAccess.open();
                String selected_value = databaseAccess.getSelectedRace(selected);
                String selected_id = databaseAccess.getSelectedRaceId(selected_value,selected);
                databaseAccess.close();

                if (selected.equals("DESCENT/RACE")){
                    s_race="";
                    s_raceid="";
                }else {
                    s_race=selected_value;
                    s_raceid=selected_id;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });



        String session_suffix = sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX);
        String session_suffixid = sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX_ID);
        ArrayAdapter<String> suffix_arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, suffixList);
        suffix_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driver_suffix.setAdapter(suffix_arrayAdapter);
        assert session_suffix!=null;
        if (session_suffix.equals("")) {

            String value = "";

            if (L_suffix.equals("")){
                value = "";
            }else {
                value = L_suffix;
            }

            int spinnerPosition = 0;
            if (!value.equals("")){
                spinnerPosition = suffix_arrayAdapter.getPosition(value);
            }

            driver_suffix.setSelection(spinnerPosition);


        }else {

            String selected_value ="";
            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
            databaseAccess.open();

            if (!L_suffix.equals("")){
                selected_value = L_suffix;
            }else {
                selected_value = databaseAccess.getSuffixName(session_suffixid);
            }

            databaseAccess.close();

            int spinnerPosition = suffix_arrayAdapter.getPosition(selected_value);
            driver_suffix.setSelection(spinnerPosition);
        }
        driver_suffix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                databaseAccess.open();
                String suffix_id = databaseAccess.getSuffixId(selected);
                databaseAccess.close();

                if (selected.equals("SUFFIX")){
                    s_driver_suffix="";
                    s_driver_suffixid="";
                }else {
                    s_driver_suffix=selected;
                    s_driver_suffixid=suffix_id;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });



        String session_eyes = sessionManager.getDriverSession().get(SessionManager.EYES);
        String session_eyesid = sessionManager.getDriverSession().get(SessionManager.EYES_ID);
        ArrayAdapter<String> eyes_arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, eyesList);
        eyes_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eyes.setAdapter(eyes_arrayAdapter);
        assert session_eyes != null;
        if (session_eyes.equals("")){

            String value = "";
            if (!L_eyecolor.equals("")){
                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                databaseAccess.open();
                value = databaseAccess.getSelectedEyesNameWithoutId(L_eyecolor);
                databaseAccess.close();
            }

            int spinnerPosition = eyes_arrayAdapter.getPosition(value);
            eyes.setSelection(spinnerPosition);

        }else {

            String selected_value="";
            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
            databaseAccess.open();

            if (!L_eyecolor.equals("")){
                    selected_value = databaseAccess.getSelectedEyesNameWithoutId(L_eyecolor);
            }else {
                selected_value =  databaseAccess.getSelectedEyesName(session_eyes,session_eyesid);
            }

            databaseAccess.close();


            int spinnerPosition = eyes_arrayAdapter.getPosition(selected_value);
            eyes.setSelection(spinnerPosition);
        }
        eyes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                databaseAccess.open();
                String selected_value = databaseAccess.getSelectedEyesValue(selected);
                String selected_id = databaseAccess.getSelectedEyesId(selected_value,selected);
                databaseAccess.close();

                if (selected.equals("EYES")){
                    s_eyes="";
                    s_eyesid ="";
                }else {
                    s_eyes=selected_value;
                    s_eyesid = selected_id;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        String session_hair = sessionManager.getDriverSession().get(SessionManager.HAIR);
        String session_hairid = sessionManager.getDriverSession().get(SessionManager.HAIR_ID);
        ArrayAdapter<String> hair_arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, hairList);
        hair_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hair.setAdapter(hair_arrayAdapter);
        assert session_hair != null;
        if (session_hair.equals("")){

            String value = "";
            if (!L_haircolor.equals("")){
                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                databaseAccess.open();
                value = databaseAccess.getSelectedHairNameWithoutId(L_haircolor);
                databaseAccess.close();
            }

            int spinnerPosition = hair_arrayAdapter.getPosition(value);
            hair.setSelection(spinnerPosition);

        }else {

            String selected_value="";
            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
            databaseAccess.open();

            if (!L_haircolor.equals("")){
                    selected_value = databaseAccess.getSelectedHairNameWithoutId(L_haircolor);
            }else {
                selected_value =  databaseAccess.getSelectedHairName(session_hair,session_hairid);
            }

            databaseAccess.close();


            int spinnerPosition = hair_arrayAdapter.getPosition(selected_value);
            hair.setSelection(spinnerPosition);
        }
        hair.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                databaseAccess.open();
                String selected_value = databaseAccess.getSelectedHairValue(selected);
                String selected_id = databaseAccess.getSelectedHairId(selected_value,selected);
                databaseAccess.close();

                if (selected.equals("HAIR")){
                    s_hair="";
                    s_hairid="";
                }else {
                    s_hair=selected_value;
                    s_hairid=selected_id;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        String session_height = sessionManager.getDriverSession().get(SessionManager.HEIGHT);
        ArrayAdapter<String> height_arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, heightList);
        height_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        height.setAdapter(height_arrayAdapter);
        assert session_height != null;
        if (session_height.equals("")){
            String value = "";
            if (!L_height.equals("")){
                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                databaseAccess.open();
                value = databaseAccess.getSelectedHeight(s_scannedheight);
                databaseAccess.close();
            }

            int spinnerPosition = height_arrayAdapter.getPosition(value);
            height.setSelection(spinnerPosition);

        }else {

            String selected_value="";
            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
            databaseAccess.open();

            if (!L_height.equals("")){
                    selected_value = databaseAccess.getSelectedHeight(s_scannedheight);
            }else {
                selected_value =  databaseAccess.getSelectedHeight(session_height);
            }

            int spinnerPosition = height_arrayAdapter.getPosition(selected_value);
            height.setSelection(spinnerPosition);
        }
        height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                databaseAccess.open();
                String selected_value = databaseAccess.getSelectedHeightValue(selected);
                databaseAccess.close();

                if (selected.equals("HEIGHT")){
                    s_height="";
                }else {
                    s_height=selected_value;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


        String session_licensetype = sessionManager.getDriverSession().get(SessionManager.LICENSE_TYPE);
        String session_licensetypeid = sessionManager.getDriverSession().get(SessionManager.LICENSE_TYPE_ID);
        ArrayAdapter<String> licensetype_arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, licensetypeList);
        licensetype_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driverlicensetype.setAdapter(licensetype_arrayAdapter);
        assert session_licensetype != null;
        if (!session_licensetype.equals("")){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedDriversLicenseTypeName(session_licensetype,session_licensetypeid);
            databaseAccess.close();

            int spinnerPosition = licensetype_arrayAdapter.getPosition(selected_value);
            driverlicensetype.setSelection(spinnerPosition);
        }

        if (!L_driver_license_type.equals("")){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
            databaseAccess.open();

            String selected_value = databaseAccess.getSelectedDriversLicenseTypeCodeDesc(L_driver_license_type);

            databaseAccess.close();
            int spinnerPosition = licensetype_arrayAdapter.getPosition(selected_value);
            driverlicensetype.setSelection(spinnerPosition);
        }
        driverlicensetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityDriver.this);
                databaseAccess.open();
                String selected_value = databaseAccess.getSelectedDriversLicenseType(selected);
                String selected_id = databaseAccess.getSelectedDriversLicenseTypeId(selected_value,selected);
                databaseAccess.close();

                if (selected.equals("Drivers License Type")){
                    s_driver_license_type="";
                    s_driver_license_typeid = "";
                }else {
                    s_driver_license_type=selected_value;
                    s_driver_license_typeid=selected_id;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

    }

    public static int firstDigit(int n)
    {
        // Remove last digit from number
        // till only one digit is left
        while (n >= 10)
            n /= 10;

        // return the first digit
        return n;
    }

    public static boolean isInRange ( int a , int b , int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a ;
    }
}