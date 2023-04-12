package com.edatasolutions.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.edatasolutions.R;
import com.edatasolutions.database.DatabaseAccess;
import com.edatasolutions.utils.SessionManager;
import com.edatasolutions.utils.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class ActivityViolationMisc extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView header_text,violationcity_txt,division_txt;
    private Button preview_btn;
    private ImageView img_menu, img_back;
    private SessionManager sessionManager;
    private RadioGroup schoolzone,night_court,ca_tobenotified,ca_citenotsignedbydriver;
    private RadioButton r_schoolzone, r_night_court,r_ca_tobenotified,r_ca_citenotsignedbydriver;
    private String seleced_appeardate="",seleced_time="",selected_issuedate="",selected_issuetime="";
    private TextView issuedate, time, courttime,appeardate,ampm_txt;
    private EditText offbadgeno, offlname,violationst,violationcst;
    private Spinner violationcity, violationsttyp,ampm,areacode,violationcsttyp,detail,division;
    private String s_schoolzone="N",s_night_court="N",s_ca_tobenotified="N",s_ca_citenotsignedbydriver="N", s_violationcity="",s_violationcityid="",s_violationsttyp="",s_violationsttypid="",s_violationcsttypid="",s_violationst="",s_violationcst="",s_violationcsttyp="";
    private String s_issuedate="", s_time="", s_ampm="",s_offbadgeno="", s_offlname="",s_areacode="",s_areacodeid="", s_division="",s_divisionid="", s_detail="", s_appeardate="",s_courttime="";

    private ArrayList<String> violationcityList = new ArrayList<>();
    private ArrayList<String> violationsttypList = new ArrayList<>();
    private ArrayList<String> violationcsttypList = new ArrayList<>();
    private ArrayList<String> areacodeList = new ArrayList<>();
    private ArrayList<String> divisionList = new ArrayList<>();
    private LinearLayout ca_citenotsignedbydriver_lay,appeardate_lay;
    private int mYear;
    private int mMonth;
    private int mDay;
    private DatePicker s_datepicker;
    private String selected = "";
    private ScrollView violationmisc_scroll;
    private RadioButton rb_ca_cite_yes, rb_ca_cite_no, rb_ca_notified_yes, rb_ca_notified_no,rb_nightcourt_yes,rb_nightcourt_no,rb_schoolzone_yes,rb_schoolzone_no;
    private ArrayAdapter<String> adapter;
    private DatePickerDialog mDatePicker;
    private ImageView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_violation_misc);

        sessionManager = new SessionManager(ActivityViolationMisc.this);


        init();

        if (getIntent().hasExtra("selected")){
            selected = getIntent().getStringExtra("selected");
        }


        getAllListData();
        getSpinnerData();
        getBadgeNumber();

        appeardate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance(TimeZone.getTimeZone("GMT-07:00"));
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                mDatePicker = DatePickerDialog.newInstance((DatePickerDialog.OnDateSetListener) ActivityViolationMisc.this,mYear,mMonth,mDay);

                // Setting Min Date to today date
                Calendar min_date_c = Calendar.getInstance();
                mDatePicker.setMinDate(min_date_c);



                // Setting Max Date to next 5 years
                Calendar max_date_c = Calendar.getInstance();
                max_date_c.set(Calendar.YEAR, mYear+5);
                mDatePicker.setMaxDate(max_date_c);

                //Disable all SUNDAYS and SATURDAYS between Min and Max Dates
                for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
                    int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek == Calendar.SUNDAY) {
                        Calendar[] disabledDays =  new Calendar[1];
                        disabledDays[0] = loopdate;
                        mDatePicker.setDisabledDays(disabledDays);
                    }
                }
                mDatePicker.show(getSupportFragmentManager(), "");
            }
        });

        /*appeardate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance(TimeZone.getTimeZone("GMT-07:00"));
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                mDatePicker = DatePickerDialog.newInstance((DatePickerDialog.OnDateSetListener) ActivityViolationMisc.this,mYear,mMonth,mDay);

                // Setting Min Date to today date
                Calendar min_date_c = Calendar.getInstance();
                mDatePicker.setMinDate(min_date_c);



                // Setting Max Date to next 5 years
                Calendar max_date_c = Calendar.getInstance();
                max_date_c.set(Calendar.YEAR, mYear+5);
                mDatePicker.setMaxDate(max_date_c);

                //Disable all SUNDAYS and SATURDAYS between Min and Max Dates
                for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
                    int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek == Calendar.SUNDAY) {
                        Calendar[] disabledDays =  new Calendar[1];
                        disabledDays[0] = loopdate;
                        mDatePicker.setDisabledDays(disabledDays);
                    }
                }
                mDatePicker.show(getSupportFragmentManager(), "");
            }
        });*/
        /*appeardate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance(TimeZone.getTimeZone("GMT-07:00"));
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                //      mcurrentDate.add(Calendar.DAY_OF_MONTH, mDay+1);


                DatePickerDialog mDatePicker = new DatePickerDialog(ActivityViolationMisc.this, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        s_datepicker = new DatePicker(ActivityViolationMisc.this);
                        Calendar myCalendar = Calendar.getInstance();


                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);


                        Date today = new Date();
                        String myFormat = "MM-dd-yyyy"; //Change as you need
                        String myFormat2 = "MMddyy";
                        String myFormat3 = "EEE";
                        String myFormat4 = "dd";
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf3 = new SimpleDateFormat(myFormat3);
                        SimpleDateFormat sdf4 = new SimpleDateFormat(myFormat4, Locale.getDefault());


                        String selectedDate="" ;
                        if (selectedday<10){
                            selectedDate = "0"+String.valueOf(selectedday);
                        }else {
                            selectedDate = String.valueOf(selectedday);
                        }


                        if (sdf4.format(today).equals(selectedDate)){

                            appeardate.setText("APPEARDATE");
                            Toast.makeText(ActivityViolationMisc.this,"Please select future date",Toast.LENGTH_SHORT).show();

                        }else if (sdf3.format(myCalendar.getTime()).equals("Sun")||sdf3.format(myCalendar.getTime()).equals("Sat")){

                            //seleced_appeardate = sdf.format(myCalendar.getTime());
                            //appeardate.setText(sdf.format(myCalendar.getTime()));
                            appeardate.setText("APPEARDATE");
                            //appeardate.setTextColor(Color.parseColor("#444444"));
//                            appeardate.setText("APPEARDATE");
                            Toast.makeText(ActivityViolationMisc.this,"Its a weekend",Toast.LENGTH_SHORT).show();

                        }else {
                            //Change by Rohit from sdf2 to sdf

                            seleced_appeardate = sdf.format(myCalendar.getTime());
                            appeardate.setText(sdf.format(myCalendar.getTime()));
                            appeardate.setTextColor(Color.parseColor("#444444"));
                        }

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);

                if (s_datepicker != null) {
                    mDatePicker.updateDate(s_datepicker.getYear(), s_datepicker.getMonth() - 1, s_datepicker.getDayOfMonth());

                }
                //mDatePicker.setTitle("Select date");
                long millis = mcurrentDate.getTimeInMillis();
                mDatePicker.getDatePicker().setMinDate(millis);
                mDatePicker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                mDatePicker.show();

            }
        });*/

        courttime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance(TimeZone.getTimeZone("GMT-07:00"));
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActivityViolationMisc.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String hour ="";
                        String minutes ="";


                        if (selectedHour<10){
                            hour = "0"+String.valueOf(selectedHour);
                        }else {
                            hour = String.valueOf(selectedHour);
                        }

                        if (selectedMinute <10){
                            minutes = "0"+String.valueOf(selectedMinute);
                        }else {
                            minutes = String.valueOf(selectedMinute);
                        }
                        String time = hour+":"+minutes;
                        seleced_time = hour+minutes;
//                        seleced_time = time;
                        courttime.setText(time);


                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        preview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkValidation();

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        violationcity_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Dialog dialog = new Dialog(ActivityViolationMisc.this);
                LayoutInflater inflater = (LayoutInflater) ActivityViolationMisc.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityViolationMisc.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, violationcityList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityViolationMisc.this.adapter.getFilter().filter(cs);

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
                        violationcity_txt.setText(selected_value);

                        String violation_city = violationcity_txt.getText().toString().trim();


                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolationMisc.this);
                        databaseAccess.open();
                        s_violationcity = databaseAccess.getSelectedViolationCity(violation_city);
                        s_violationcityid = databaseAccess.getSelectedViolationCityId(s_violationcity,violation_city);
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

        division_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Dialog dialog = new Dialog(ActivityViolationMisc.this);
                LayoutInflater inflater = (LayoutInflater) ActivityViolationMisc.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityViolationMisc.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, divisionList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityViolationMisc.this.adapter.getFilter().filter(cs);

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
                        division_txt.setText(selected_value);

                        String division = division_txt.getText().toString().trim();

                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolationMisc.this);
                        databaseAccess.open();
                        s_divisionid = databaseAccess.getDivisionAreaCodeId(division);
                        s_division = databaseAccess.getDivisionAreaCodecodevalue(division,s_divisionid);

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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appeardate.setText("");
                cancel.setVisibility(View.GONE);
            }
        });
    }


    private void init(){

        preview_btn = findViewById(R.id.preview_btn);
        header_text = findViewById(R.id.header_text);
        img_menu = findViewById(R.id.img_menu);
        img_menu.setVisibility(View.INVISIBLE);
        header_text.setText(R.string.violation_misc);
        img_back = findViewById(R.id.img_back);
        violationcity = findViewById(R.id.violationcity);
        violationst = findViewById(R.id.violationst);
        violationsttyp = findViewById(R.id.violationsttyp);
        violationcst = findViewById(R.id.violationcst);
        violationcsttyp = findViewById(R.id.violationcsttyp);
        schoolzone = findViewById(R.id.schoolzone);
        night_court = findViewById(R.id.night_court);
        ca_tobenotified = findViewById(R.id.ca_tobenotified);
        ca_citenotsignedbydriver = findViewById(R.id.ca_citenotsignedbydriver);
        issuedate = findViewById(R.id.issuedate);
        time = findViewById(R.id.time);
        ampm = findViewById(R.id.ampm);
        offbadgeno = findViewById(R.id.offbadgeno);
        offlname = findViewById(R.id.offlname);
        areacode = findViewById(R.id.areacode);
        division = findViewById(R.id.division);
        detail = findViewById(R.id.detail);
        appeardate = findViewById(R.id.appeardate);
        courttime = findViewById(R.id.courttime);
        rb_ca_cite_yes = findViewById(R.id.rb_ca_cite_yes);
        rb_ca_cite_no = findViewById(R.id.rb_ca_cite_no);
        rb_ca_notified_yes = findViewById(R.id.rb_ca_notified_yes);
        rb_ca_notified_no = findViewById(R.id.rb_ca_notified_no);
        rb_nightcourt_yes = findViewById(R.id.rb_nightcourt_yes);
        rb_nightcourt_no = findViewById(R.id.rb_nightcourt_no);
        rb_schoolzone_yes = findViewById(R.id.rb_schoolzone_yes);
        rb_schoolzone_no = findViewById(R.id.rb_schoolzone_no);
        ca_citenotsignedbydriver_lay = findViewById(R.id.ca_citenotsignedbydriver_lay);
        appeardate_lay = findViewById(R.id.appeardate_lay);
        violationmisc_scroll = findViewById(R.id.violationmisc_scroll);
        violationcity_txt = findViewById(R.id.violationcity_txt);
        division_txt = findViewById(R.id.division_txt);
        ampm_txt = findViewById(R.id.ampm_txt);
        cancel = findViewById(R.id.cancel_btn);

        String session_schoolzone = sessionManager.getViolationMiscSession().get(SessionManager.SCHOOL_ZONE);
        String session_cacite = sessionManager.getViolationMiscSession().get(SessionManager.CA_CITENOTSIGNEDBYDRIVER);
        String session_canotified = sessionManager.getViolationMiscSession().get(SessionManager.CA_TOBENOTIFIED);
        String session_nightcourt = sessionManager.getViolationMiscSession().get(SessionManager.NIGHT_COURT);

        String session_detail = sessionManager.getViolationMiscSession().get(SessionManager.DETAIL);
        String session_division = sessionManager.getViolationMiscSession().get(SessionManager.DIVISION);
        String session_offlname = sessionManager.getViolationMiscSession().get(SessionManager.OFFLNAME);
        String session_badgeno = sessionManager.getViolationMiscSession().get(SessionManager.OFFBADGENO);
        String session_courttime = sessionManager.getViolationMiscSession().get(SessionManager.COURT_TIME);
        String session_appeardate = sessionManager.getViolationMiscSession().get(SessionManager.APPEAR_DATE);
        String session_violationcsttyp = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCSTTYP);
        String session_violationcst = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCST);
        String session_violationst = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONST);

        //String session_issuetime = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONST);

        String session_violationcity = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCITY);
        String session_violationcityid = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCITY_ID);

        assert session_violationcity != null;
        if (!session_violationcity.equals("")){

            s_violationcity = session_violationcity;
            s_violationcityid = session_violationcityid;

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolationMisc.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedViolationCityValue(session_violationcity,session_violationcityid);
            databaseAccess.close();

            violationcity_txt.setText(selected_value);

        }

        String session_divisioncode = sessionManager.getViolationMiscSession().get(SessionManager.DIVISION);
        String session_divisioncodeid = sessionManager.getViolationMiscSession().get(SessionManager.DIVISION_ID);

        assert session_divisioncode != null;
        if (!session_divisioncode.equals("")){

            if (session_divisioncode.equals("00")){
                s_division = "00";
                s_divisionid = "";
                division_txt.setHint(getResources().getString(R.string.division));
            }else {
                s_division = session_divisioncode;
                s_divisionid = session_divisioncodeid;

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolationMisc.this);
                databaseAccess.open();
                String selected_value = databaseAccess.getDivisionAreaCodeValue(session_divisioncodeid,session_divisioncode);
                databaseAccess.close();

                division_txt.setText(selected_value);
            }

        }


        if (session_violationcst!=null){
            violationcst.setText(session_violationcst);
        }
        if (session_violationst!=null){
            violationst.setText(session_violationst);
        }
//        if (session_violationcsttyp!=null){
//            violationscttyp.setText(session_violationcsttyp);
//        }
        assert session_appeardate != null;
        if (session_appeardate.equals("")||session_appeardate.equals("999999")){
            appeardate.setText("");
        }else {
            appeardate.setText(session_appeardate);
        }
        assert session_courttime != null;
        if (session_courttime.equals("")){
            courttime.setText(getResources().getString(R.string.courttime));
        }else {
            courttime.setText(session_courttime);
        }
        if (session_badgeno!=null){
            offbadgeno.setText(session_badgeno);
        }
        if (session_offlname!=null){
            offlname.setText(session_offlname);
        }
//        if (session_detail!=null){
//            detail.setText(session_detail);
//        }
//        if (session_division!=null){
//            division.setText(session_division);
//        }

        if (session_cacite!=null){
            if (session_cacite.equals("Y")){
                rb_ca_cite_yes.setChecked(true);
                rb_ca_cite_no.setChecked(false);
            }else if (session_cacite.equals("N")){
                rb_ca_cite_yes.setChecked(false);
                rb_ca_cite_no.setChecked(true);
            }else {
                rb_ca_cite_yes.setChecked(false);
                rb_ca_cite_no.setChecked(false);
            }
        }
        if (session_canotified!=null){
            if (session_canotified.equals("Y")){
                rb_ca_notified_yes.setChecked(true);
                rb_ca_notified_no.setChecked(false);
            }else if (session_canotified.equals("N")){
                rb_ca_notified_yes.setChecked(false);
                rb_ca_notified_no.setChecked(true);
            }else {
                rb_ca_notified_yes.setChecked(false);
                rb_ca_notified_no.setChecked(false);
            }
        }
        if (session_nightcourt!=null){
            if (session_nightcourt.equals("Y")){
                rb_nightcourt_yes.setChecked(true);
                rb_nightcourt_no.setChecked(false);
            }else if (session_nightcourt.equals("N")){
                rb_nightcourt_yes.setChecked(false);
                rb_nightcourt_no.setChecked(true);
            }else {
                rb_nightcourt_yes.setChecked(false);
                rb_nightcourt_no.setChecked(false);
            }
        }
        if (session_schoolzone!=null){
            if (session_schoolzone.equals("Y")){
                rb_schoolzone_yes.setChecked(true);
                rb_schoolzone_no.setChecked(false);
            }else if (session_schoolzone.equals("N")){
                rb_schoolzone_yes.setChecked(false);
                rb_schoolzone_no.setChecked(true);
            }else {
                rb_schoolzone_yes.setChecked(false);
                rb_schoolzone_no.setChecked(false);
            }
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeformat1 = new SimpleDateFormat("hhmm", Locale.getDefault());
        SimpleDateFormat timeformat2 = new SimpleDateFormat("hh:mm", Locale.getDefault());
        SimpleDateFormat timeformat3 = new SimpleDateFormat("a", Locale.getDefault());

        timeformat1.setTimeZone(TimeZone.getTimeZone("GMT-08:00"));
        timeformat2.setTimeZone(TimeZone.getTimeZone("GMT-08:00"));
        timeformat3.setTimeZone(TimeZone.getTimeZone("GMT-08:00"));

        String localTime = timeformat1.format(calendar.getTime());
        String localTime2 = timeformat2.format(calendar.getTime());
        String localTime3 = timeformat3.format(calendar.getTime());

        ampm_txt.setText(localTime3);
        s_ampm = ampm_txt.getText().toString().trim();
        selected_issuetime = localTime;
        time.setText(localTime2);



        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MMddyy", Locale.getDefault());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

        dateFormat1.setTimeZone(TimeZone.getTimeZone("GMT-07:00"));
        dateFormat2.setTimeZone(TimeZone.getTimeZone("GMT-07:00"));

        String date1 = dateFormat1.format(calendar.getTime());
        String date2 = dateFormat2.format(calendar.getTime());

        s_issuedate = date1;
        issuedate.setText(date2);

    }

    private void getSpinnerData() {


        String session_violationcttyp = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONSTTYP);
        String session_violationcttypid = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONSTTYP_ID);
        ArrayAdapter<String> violationsttyp_arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, violationsttypList);
        violationsttyp_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        violationsttyp.setAdapter(violationsttyp_arrayAdapter);
        if (session_violationcttyp != null) {

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolationMisc.this);
            databaseAccess.open();
            String selected = databaseAccess.getSelectedViolationsttypValue(session_violationcttypid);
            databaseAccess.close();

            int spinnerPosition = violationsttyp_arrayAdapter.getPosition(selected);
            violationsttyp.setSelection(spinnerPosition);
        }
        violationsttyp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolationMisc.this);
                databaseAccess.open();
                String selected_id = databaseAccess.getSelectedViolationsttypId(selected);
                databaseAccess.close();

                if (selected.equals("VIOLATIONSTTYP")) {
                    s_violationsttyp = "";
                    s_violationsttypid="";
                } else {
                    s_violationsttyp = selected;
                    s_violationsttypid=selected_id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        String session_violationcsttyp = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCSTTYP);
        String session_violationcsttypid = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCSTTYP_ID);
        ArrayAdapter<String> violationcsttyp_arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, violationcsttypList);
        violationcsttyp_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        violationcsttyp.setAdapter(violationcsttyp_arrayAdapter);
        if (session_violationcsttyp != null) {

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolationMisc.this);
            databaseAccess.open();
            String selected = databaseAccess.getSelectedViolationcsttypValue(session_violationcsttypid);
            databaseAccess.close();

            int spinnerPosition = violationsttyp_arrayAdapter.getPosition(selected);
            violationcsttyp.setSelection(spinnerPosition);
        }
        violationcsttyp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolationMisc.this);
                databaseAccess.open();
                String selected_id = databaseAccess.getSelectedViolationcsttypId(selected);
                databaseAccess.close();

                if (selected.equals("VIOLATIONCSTTYP")) {
                    s_violationcsttyp = "";
                    s_violationcsttypid="";
                } else {
                    s_violationcsttyp = selected;
                    s_violationcsttypid=selected_id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        String session_areacode = sessionManager.getViolationMiscSession().get(SessionManager.AREACODE);
        String session_areacodeid = sessionManager.getViolationMiscSession().get(SessionManager.AREACODE_ID);
        ArrayAdapter<String> areacode_arrayAdapter = new ArrayAdapter<String>(ActivityViolationMisc.this, R.layout.spinner_item, areacodeList);
        areacode_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areacode.setAdapter(areacode_arrayAdapter);
        if (session_areacode!=null){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolationMisc.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedAreaCodeValue(session_areacode,session_areacodeid);
            databaseAccess.close();

            int spinnerPosition = areacode_arrayAdapter.getPosition(selected_value);
            areacode.setSelection(spinnerPosition);
        }
        areacode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolationMisc.this);
                databaseAccess.open();
                String selected_value = databaseAccess.getSelectedAreaCode(selected);
                String selected_id = databaseAccess.getSelectedAreaCodeId(selected_value,selected);
                databaseAccess.close();

                if (selected.equals("OFFICER AREA")) {
                    s_areacode = "";
                    s_areacodeid="";
                } else {
                    s_areacode = selected_value;
                    s_areacodeid=selected_id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        ArrayList<String> detailList = new ArrayList<>();
        detailList.add("DETAIL");
        detailList.add("TE");
        detailList.add("CI");
        detailList.add("PI");
        String session_detailList = sessionManager.getViolationMiscSession().get(SessionManager.DETAIL);
        ArrayAdapter<String> adapter_detailList = new ArrayAdapter<String>(ActivityViolationMisc.this, R.layout.spinner_item, detailList);
        adapter_detailList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        detail.setAdapter(adapter_detailList);
        if (session_detailList!=null){

            int spinnerPosition = adapter_detailList.getPosition(session_detailList);
            detail.setSelection(spinnerPosition);

        }
        detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = parent.getItemAtPosition(position).toString();
                if (selected.equals("DETAIL")){
                    s_detail="";
                }else {
                    s_detail=selected;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void getAllListData(){

        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolationMisc.this);
        databaseAccess.open();
        divisionList = databaseAccess.getContentDivisionAreaCodes();
        violationcityList = databaseAccess.getViolationCity();
        violationcityList = databaseAccess.getViolationCity();
        violationsttypList = databaseAccess.getViolationsttyp();
        violationcsttypList = databaseAccess.getViolationcsttyp();
        areacodeList = databaseAccess.getAreaCode();
        databaseAccess.close();
    }


    private void getBadgeNumber(){

        String  user_id = sessionManager.getAdminLogin().get(SessionManager.USER_ID);
        String BadgeNumber = sessionManager.getAdminLogin().get(SessionManager.BADGE_NO);
        String OfficerName = sessionManager.getAdminLogin().get(SessionManager.NAME);


        offbadgeno.setText(BadgeNumber);
        offbadgeno.setFocusable(false);

        offlname.setText(OfficerName);
        offlname.setFocusable(false);
    }

    private void checkValidation(){

 //       s_issuedate = issuedate.getText().toString().trim();
        s_time= selected_issuetime;


        s_offbadgeno= offbadgeno.getText().toString().trim();
        s_offlname = offlname.getText().toString().trim();

//        s_division= division.getText().toString().trim();
//        s_detail = detail.getText().toString().trim();



        s_violationst = violationst.getText().toString().trim();
        s_violationcst = violationcst.getText().toString().trim();

      //  s_courttime = courttime.getText().toString().trim();
        if(courttime.getText().toString().trim().equals("COURTTIME")){
            s_courttime="";
        }else {

            if (seleced_time.equals("")){
                s_courttime=courttime.getText().toString().trim();
            }else {
                s_courttime = seleced_time;
            }
        }


        if (schoolzone.getCheckedRadioButtonId() == -1)
        {
            s_schoolzone ="N";
        }
        else
        {
            int comm_selectedId = schoolzone.getCheckedRadioButtonId();
            r_schoolzone = (RadioButton) findViewById(comm_selectedId);
            if (r_schoolzone.getText().toString().equals("Yes")){
                s_schoolzone ="Y";
            }else {
                s_schoolzone ="N";
            }
        }

        if (night_court.getCheckedRadioButtonId() == -1)
        {
            s_night_court ="N";
        }
        else
        {
            int comm_selectedId = night_court.getCheckedRadioButtonId();
            r_night_court = (RadioButton) findViewById(comm_selectedId);
            if (r_night_court.getText().toString().equals("Yes")){
                s_night_court ="Y";
            }else {
                s_night_court ="N";
            }
        }

        if (ca_tobenotified.getCheckedRadioButtonId() == -1)
        {
            s_ca_tobenotified ="N";
        }
        else
        {
            int comm_selectedId = ca_tobenotified.getCheckedRadioButtonId();
            r_ca_tobenotified = (RadioButton) findViewById(comm_selectedId);
            if (r_ca_tobenotified.getText().toString().equals("Yes")){
                s_ca_tobenotified ="Y";
            }else {
                s_ca_tobenotified ="N";
            }
        }

        if (ca_citenotsignedbydriver.getCheckedRadioButtonId() == -1)
        {
            s_ca_citenotsignedbydriver ="";
        }
        else
        {
            int comm_selectedId = ca_citenotsignedbydriver.getCheckedRadioButtonId();
            r_ca_citenotsignedbydriver = (RadioButton) findViewById(comm_selectedId);
            if (r_ca_citenotsignedbydriver.getText().toString().equals("Yes")){
                s_ca_citenotsignedbydriver ="Y";
            }else {
                s_ca_citenotsignedbydriver ="N";
            }
        }


        if (appeardate.getText().toString().trim().equals("")){
            s_appeardate= "999999";
        }else {
            s_appeardate = seleced_appeardate;
        }

//        boolean selected_Date = Utils.isWeekend(appeardate.getText().toString().trim());

        if (s_offbadgeno.equals("")){
            Toast.makeText(ActivityViolationMisc.this, "Officer badge number can't be empty", Toast.LENGTH_SHORT).show();
        }else if (s_offlname.equals("")){
            Toast.makeText(ActivityViolationMisc.this, "Officer name can't be empty", Toast.LENGTH_SHORT).show();
        }else if (s_ca_citenotsignedbydriver.equals("")){
            violationmisc_scroll.fullScroll(View.FOCUS_DOWN);
            ca_citenotsignedbydriver_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
//            Toast.makeText(ActivityViolationMisc.this, "Please select driver signature type", Toast.LENGTH_SHORT).show();
//        }else  if (selected_Date){
//            Toast.makeText(ActivityViolationMisc.this, "Please verify your selected date", Toast.LENGTH_SHORT).show();
        }else {

            if (s_ampm.equals("am")){
                s_ampm="A";
            }else {
                s_ampm="P";
            }

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

                sessionManager.saveViolationMisc("Y");
                sessionManager.ClearViolationMiscEntery();
                sessionManager.createViolationMiscSession(s_issuedate, s_time, s_ampm, s_schoolzone,s_violationcity,s_violationcityid,s_violationst,s_violationsttyp,s_violationsttypid,s_violationcst,s_violationcsttyp,s_violationcsttypid,s_appeardate,s_courttime, s_offbadgeno,s_offlname,s_areacode,s_areacodeid,s_division,s_divisionid,s_detail,s_night_court,s_ca_tobenotified,s_ca_citenotsignedbydriver);
                Intent i =new Intent(ActivityViolationMisc.this, ActivityPreview.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter,R.anim.exit);

            }else {

                boolean res;

                res = (sessionManager.getOwnerVehicleSession().get(SessionManager.OWNER_FIRST_NAME) == null);
                if (res){
                    Toast.makeText(ActivityViolationMisc.this,"Please fill owner details",Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(ActivityViolationMisc.this, ActivityVehicle.class);
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

                    sessionManager.saveViolationMisc("Y");
                    sessionManager.ClearViolationMiscEntery();
                    sessionManager.createViolationMiscSession(s_issuedate, s_time, s_ampm, s_schoolzone, s_violationcity, s_violationcityid, s_violationst, s_violationsttyp, s_violationsttypid, s_violationcst, s_violationcsttyp, s_violationcsttypid, s_appeardate, s_courttime, s_offbadgeno, s_offlname, s_areacode, s_areacodeid, s_division, s_divisionid, s_detail, s_night_court, s_ca_tobenotified, s_ca_citenotsignedbydriver);
                    Intent i = new Intent(ActivityViolationMisc.this, ActivityPreview.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.exit);

                }
            }

        }



    }

    @Override
    public void onBackPressed() {

        if (selected.equals("violationmisc")){
            Intent i = new Intent(ActivityViolationMisc.this, ActivityHome.class);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int selectedyear, int selectedmonth, int selectedday) {

        s_datepicker = new DatePicker(ActivityViolationMisc.this);
        Calendar myCalendar = Calendar.getInstance();


        myCalendar.set(Calendar.YEAR, selectedyear);
        myCalendar.set(Calendar.MONTH, selectedmonth);
        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);

        Date today = new Date();
        String myFormat = "MM-dd-yyyy"; //Change as you need
        String myFormat2 = "MMddyy";
        String myFormat3 = "EEE";
        String myFormat4 = "dd";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf3 = new SimpleDateFormat(myFormat3);
        SimpleDateFormat sdf4 = new SimpleDateFormat(myFormat4, Locale.getDefault());


        String selectedDate="" ;
        if (selectedday<10){
            selectedDate = "0"+String.valueOf(selectedday);
        }else {
            selectedDate = String.valueOf(selectedday);
        }


        if (sdf.format(today).equals(sdf.format(myCalendar.getTime()))){

            appeardate.setText("");
            if(cancel.getVisibility() == View.VISIBLE){
                cancel.setVisibility(View.GONE);
            }
            Toast.makeText(ActivityViolationMisc.this,"Please select future date",Toast.LENGTH_SHORT).show();
        }else if (sdf3.format(myCalendar.getTime()).equals("Sat")){

            //seleced_appeardate = sdf.format(myCalendar.getTime());
            //appeardate.setText(sdf.format(myCalendar.getTime()));
            appeardate.setText("");
            //appeardate.setTextColor(Color.parseColor("#444444"));
//                            appeardate.setText("APPEARDATE");
            if(cancel.getVisibility() == View.VISIBLE){
                cancel.setVisibility(View.GONE);
            }
            Toast.makeText(ActivityViolationMisc.this,"Its a weekend",Toast.LENGTH_SHORT).show();

        }else {
            //

            seleced_appeardate = sdf2.format(myCalendar.getTime());
            appeardate.setText(sdf.format(myCalendar.getTime()));
            appeardate.setTextColor(Color.parseColor("#444444"));
            cancel.setVisibility(View.VISIBLE);
            //appeardate.setSelection(appeardate.getText().length());

        }

//        mDay = selectedday;
//        mMonth = selectedmonth;
//        mYear = selectedyear;
    }
}