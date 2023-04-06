package com.edatasolutions.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.edatasolutions.utils.SessionManager;
import com.edatasolutions.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static com.edatasolutions.activities.ActivityDriver.firstDigit;

public class ActivityVehicle extends AppCompatActivity {

    private TextView header_text,vehmake_txt,vehmodel_txt,ownercity_txt,ownerstate_txt,vehiclestate_txt;
    private Button vehicle_next_btn;
    private ImageView img_menu,img_back;
    private ArrayList<String> vehcileBodyList = new ArrayList<>();
    private ArrayList<String> vehcileColorList = new ArrayList<>();
    private ArrayList<String> vehcileTypeList = new ArrayList<>();
    private ArrayList<String> vehcileMakeList = new ArrayList<>();
    private ArrayList<String> vehcileModelList = new ArrayList<>();
    private ArrayList<String> vehcileYearList = new ArrayList<>();
    private ArrayList<String> vehiclestateList = new ArrayList<>();
    private Spinner vehbody,vehcolor,vehtype,vehmake,vehmodel,vehyear,owner_city, owner_state,owner_suffix,vehicle_state;
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayList<String> stateList = new ArrayList<>();
    private ArrayList<String> suffixList = new ArrayList<>();
    private EditText owner_firstname, owner_middlename,owner_lastname, owner_address1,owner_address2,  owner_zipcode, vehicle_no,overload,policy_no;
    private SessionManager sessionManager;
    private ScrollView vehicle_scroll;
    private String firstname ="", middlename="", lastname="", suffix="",suffixid="", address1="",address2="",city="",city_id="",state="",state_id="", zipcode="",vehicleno="",vehiclestate="",vehiclestateid="",s_overload="", policyno="" ;
    private String s_vehyear="",s_vehmake="",s_vehmakeid="", s_vehbody="",s_vehbodyid="",s_vehtype="",s_vehtypeid="",s_vehcolor="",s_vehcolorid="", s_vehmodel="",s_vehmodelid="",s_commercial="0", s_hazardous="N";

    private RadioGroup commercial_group,hazardous_group;
    private RadioButton comm_radiobtn, hazardous_radiobtn;
    private LinearLayout owner_fields;
    private RadioButton rb_hazardous_yes,rb_hazardous_no,rb_commercial_no,rb_commercial_yes;
    private String selected = "";

    private int check = 0;
    private int vehmodel_check = 0;
    private int city_spinnerPosition, vehmodel_spinnerPosition;
    private LinearLayout  zip_lay, firstname_lay,address_lay,lastname_lay,city_lay,ownerstate_lay;
    private String owner_responsibility="";
    private ArrayAdapter<String> adapter;
    private CheckBox sameasdriver_checkbox;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        sessionManager = new SessionManager(ActivityVehicle.this);
        Log.e("djffe","dhu1 "+sessionManager.getVehicleSession().get(SessionManager.OWNER_FIRST_NAME));
        init();

        if (getIntent().hasExtra("selected")){
            selected = getIntent().getStringExtra("selected");
        }

        getAllListData();
        selectVehcileMake();
        selectVehicleYear();;


        String session_stateid = sessionManager.getVehicleSession().get(SessionManager.OWNER_STATE_ID);
        assert session_stateid != null;
        if (state.equals("")){
            if (session_stateid.equals("")){
                getcity();
            }else {
                getcitywithstateid(session_stateid);
            }
        }else {
            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
            databaseAccess.open();
            String selected_id = databaseAccess.getSelectedOnlyStateId(state);
            databaseAccess.close();
            getcitywithstateid(selected_id);
        }


        String session_vehmakeid = sessionManager.getVehicleSession().get(SessionManager.VEHMAKE_ID);
        assert session_vehmakeid != null;

        if (session_vehmakeid.equals("")){
            selectVehcileMake();
        }else {
            selectVehcileModelWithVehMakeId(session_vehmakeid);
        }

//        if (s_vehmake.equals("")){
//            if (!session_vehmakeid.equals("")){
//                selectVehcileModelWithVehMakeId(session_vehmakeid);
//            }else {
//                selectVehcileMake();
//            }
//        }else {
//            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
//            databaseAccess.open();
//            String selected_id = databaseAccess.getSelectedVehicleMakeId(session_vehmakeid,session_vehmake);
//            databaseAccess.close();
//       //     selectVehcileModelWithVehMakeId(selected_id);
//        }



        getSpinnerData();

        //Log.e("getname",sessionManager.getHeaderSession().get(SessionManager.OWNER_RESPONSIBILITY));



        vehicle_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkvalidation();

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        overload.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String overload_txt = overload.getText().toString();
                if (overload_txt.length()==4){
                    overload.setText("0"+overload_txt);
                }else if (overload_txt.length()==3){
                    overload.setText("00"+overload_txt);
                }else if (overload_txt.length()==2){
                    overload.setText("000"+overload_txt);
                }else if (overload_txt.length()==1){
                    overload.setText("0000"+overload_txt);
                }else {
                    overload.setText(overload_txt);
                }
            }
        });

        vehmake_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Dialog dialog = new Dialog(ActivityVehicle.this);
                LayoutInflater inflater = (LayoutInflater) ActivityVehicle.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityVehicle.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, vehcileMakeList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityVehicle.this.adapter.getFilter().filter(cs);

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
                        vehmake_txt.setText(selected_value);

                        String vehmake = vehmake_txt.getText().toString().trim();

                        vehmodel_txt.setText("");
                        vehmodel_txt.setHint(getResources().getString(R.string.vehmodel));

                        s_vehmodel="";
                        s_vehmodelid="";

                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                        databaseAccess.open();
                        s_vehmake = databaseAccess.getSelectedVehicleMake(vehmake);
                        s_vehmakeid = databaseAccess.getSelectedVehicleMakeId(s_vehmake,vehmake);
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


        vehmodel_txt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (s_vehmake.equals("")) {
                    vehmodel_txt.setClickable(false);
                }else {
                    vehmodel_txt.setClickable(true);
                }
                return false;
            }
        });

        vehmodel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!s_vehmake.equals("")) {
                    vehmodel_txt.setClickable(true);

                    selectVehcileModelWithVehMakeId(s_vehmakeid);

                    final Dialog dialog = new Dialog(ActivityVehicle.this);
                    LayoutInflater inflater = (LayoutInflater) ActivityVehicle.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View post = inflater.inflate(R.layout.violation_search_lay, null);
                    EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                    ListView listView = (ListView) post.findViewById((R.id.listview));

                    adapter = new ArrayAdapter<String>(ActivityVehicle.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, vehcileModelList);
                    listView.setAdapter(adapter);

                    autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                            // When user changed the Text
                            ActivityVehicle.this.adapter.getFilter().filter(cs);

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
                            vehmodel_txt.setText(selected_value);


                            String vehmodel = vehmodel_txt.getText().toString().trim();

                            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                            databaseAccess.open();
                            s_vehmodel = databaseAccess.getSelectedVehicleModel(vehmodel,s_vehmakeid);
                            s_vehmodelid = databaseAccess.getSelectedVehicleModelId(vehmodel,s_vehmodel,s_vehmakeid);

                            Log.e("dkjkdjf1",s_vehmodel+"  "+vehmodel+"  "+s_vehmodelid);
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
                }else {
                    vehmodel_txt.setClickable(false);
                }
            }
        });


        ownerstate_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Dialog dialog = new Dialog(ActivityVehicle.this);
                LayoutInflater inflater = (LayoutInflater) ActivityVehicle.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityVehicle.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, stateList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityVehicle.this.adapter.getFilter().filter(cs);

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
                        ownerstate_txt.setText(selected_value);

                        String ownerstate = ownerstate_txt.getText().toString().trim();

                        ownercity_txt.setText("");
                        ownercity_txt.setHint(getResources().getString(R.string.owner_city));
                        city="";
                        city_id="";

                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                        databaseAccess.open();
                        state = databaseAccess.getSelectedStateValue(ownerstate);
                        state_id = databaseAccess.getSelectedStateId(ownerstate,state);
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

        ownercity_txt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (state_id.equals("")) {
                    ownercity_txt.setClickable(false);
                }else {
                    ownercity_txt.setClickable(true);
                }
                return false;
            }
        });

        ownercity_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    getcitywithstateid(state_id);

                    final Dialog dialog = new Dialog(ActivityVehicle.this);
                    LayoutInflater inflater = (LayoutInflater) ActivityVehicle.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View post = inflater.inflate(R.layout.violation_search_lay, null);
                    EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                    ListView listView = (ListView) post.findViewById((R.id.listview));

                    adapter = new ArrayAdapter<String>(ActivityVehicle.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, cityList);
                    listView.setAdapter(adapter);

                    autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                            // When user changed the Text
                            ActivityVehicle.this.adapter.getFilter().filter(cs);

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
                            ownercity_txt.setText(selected_value);


                            String ownercity = ownercity_txt.getText().toString().trim();

                            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                            databaseAccess.open();
                            city = databaseAccess.getSelectedCityValue(ownercity,state_id);
                            city_id = databaseAccess.getSelectedCityIdwithStateId(ownercity, city,state_id);

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


        vehiclestate_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Dialog dialog = new Dialog(ActivityVehicle.this);
                LayoutInflater inflater = (LayoutInflater) ActivityVehicle.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityVehicle.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, vehiclestateList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityVehicle.this.adapter.getFilter().filter(cs);

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
                        vehiclestate_txt.setText(selected_value);

                        String vehstate = vehiclestate_txt.getText().toString().trim();

                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                        databaseAccess.open();
                        vehiclestate = databaseAccess.getSelectedStateValue(vehstate);
                        vehiclestateid = databaseAccess.getSelectedStateId(vehstate,vehiclestate);

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
    private void checkvalidation(){

        firstname =owner_firstname.getText().toString().trim();
        middlename=owner_middlename.getText().toString().trim();
        lastname=owner_lastname.getText().toString().trim();
        address1=owner_address1.getText().toString().trim();
        address2=owner_address2.getText().toString().trim();
        zipcode=owner_zipcode.getText().toString().trim();
        vehicleno=vehicle_no.getText().toString().trim();
        policyno=policy_no.getText().toString().trim();

        s_overload=overload.getText().toString().trim();

        if (commercial_group.getCheckedRadioButtonId() == -1)
        {
            s_commercial ="0";
        }
        else
        {
            // one of the radio buttons is checked
            int comm_selectedId = commercial_group.getCheckedRadioButtonId();
            comm_radiobtn = (RadioButton) findViewById(comm_selectedId);
            if (comm_radiobtn.getText().toString().equals("Yes")){
                s_commercial ="1";
            }else {
                s_commercial ="0";
            }
        }

        if (hazardous_group.getCheckedRadioButtonId() == -1)
        {
            // no radio buttons are checked
            s_hazardous ="N";
        }
        else
        {
            int h_selectedId = hazardous_group.getCheckedRadioButtonId();
            hazardous_radiobtn = (RadioButton) findViewById(h_selectedId);
            if (hazardous_radiobtn.getText().toString().equals("Yes")){
                s_hazardous ="Y";
            }else {
                s_hazardous ="N";
            }
        }


        boolean valid = true;

        owner_responsibility = sessionManager.getHeaderSession().get(SessionManager.OWNER_RESPONSIBILITY);
        assert owner_responsibility != null;
        if (owner_responsibility.equals("Y")){
            if ( TextUtils.isEmpty(firstname) ) {
                firstname_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                firstname_lay.getParent().requestChildFocus(firstname_lay,firstname_lay);
                valid = false;
            }else {
//            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
//            databaseAccess.open();
//            String name  = databaseAccess.checkNameWithNameMaster(firstname);
//            databaseAccess.close();
//            if (name.equals("")){
//                Utils.showPopUpMessage(ActivityVehicle.this,getResources().getString(R.string.first_name_not_found));
//                // Toast.makeText(ActivityDriver.this, "Name not found in database", Toast.LENGTH_SHORT).show();
//            }
                firstname_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
            }

            if ( TextUtils.isEmpty(lastname) ) {
                lastname_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                lastname_lay.getParent().requestChildFocus(lastname_lay,lastname_lay);
                valid = false;
            }else {
//            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
//            databaseAccess.open();
//            String name  = databaseAccess.checkNameWithNameMaster(lastname);
//            databaseAccess.close();
//            if (name.equals("")){
//                Utils.showPopUpMessage(ActivityVehicle.this,getResources().getString(R.string.last_name_not_found));
//                // Toast.makeText(ActivityDriver.this, "Name not found in database", Toast.LENGTH_SHORT).show();
//            }
                lastname_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
            }

            if ( TextUtils.isEmpty(address1) ) {
                address_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                address_lay.getParent().requestChildFocus(address_lay,address_lay);
                valid = false;
            }else {
                if(address1.startsWith("0")){
                    address_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                    address_lay.getParent().requestChildFocus(address_lay,address_lay);
                    valid = false;
                }else if (address1.length()<=4){
                    address_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                    address_lay.getParent().requestChildFocus(address_lay,address_lay);
                    valid = false;
                }else {
                    address_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
                }
            }

            if ( TextUtils.isEmpty(city) ) {
                city_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                city_lay.getParent().requestChildFocus(city_lay,city_lay);
                valid = false;
            }else {
                city_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
            }

            if (TextUtils.isEmpty(state)){
                ownerstate_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                ownerstate_lay.getParent().requestChildFocus(ownerstate_lay,ownerstate_lay);
                valid = false;
            }else {
                ownerstate_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
            }

            if (!TextUtils.isEmpty(zipcode)) {

                if (zipcode.length() < 5) {
                    zip_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                    zip_lay.getParent().requestChildFocus(zip_lay,zip_lay);
                    valid = false;
                    // Toast.makeText(ActivityDriver.this, "Please enter correct zipcode", Toast.LENGTH_SHORT).show();
                } else {
                    if(!state_id.equals("")&&!city_id.equals("")&&!state.equals("CA")){

                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                        databaseAccess.open();
                        ArrayList<String> arrayList = databaseAccess.checkZipcode(state_id,city_id);
                        databaseAccess.close();

                        if (arrayList.contains(zipcode)){
                            zip_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
                        }else {
                            zip_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
                            Toast.makeText(ActivityVehicle.this, "Entered zip code does not match with selected city", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (state.equals("CA")) {
                        int Digit = Integer.parseInt(zipcode);
                        if (!(firstDigit(Digit) == 9)) {
                            zip_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                            zip_lay.getParent().requestChildFocus(zip_lay,zip_lay);
                            valid = false;
                            //   Toast.makeText(ActivityDriver.this, "Please enter correct zipcode", Toast.LENGTH_SHORT).show();
                        }else {

                            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                            databaseAccess.open();

                            ArrayList<String> arrayList = databaseAccess.checkZipcode(state_id,city_id);
                            databaseAccess.close();

                            if (arrayList.contains(zipcode)){
                                zip_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
                            }else {
                                zip_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
                                if (!owner_responsibility.equals("N")){
                                    Toast.makeText(ActivityVehicle.this, "Entered zip code does not exist", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    }
                }
        }

        if(TextUtils.isEmpty(firstname) && TextUtils.isEmpty(lastname) && TextUtils.isEmpty(address1) && TextUtils.isEmpty(city)){
            vehicle_scroll.fullScroll(View.FOCUS_UP);
        }

        }


        if ( valid ) {

            String sameasdriver_check="";
            if (sameasdriver_checkbox.isChecked()){
                sameasdriver_check="yes";
            }else {
                sameasdriver_check="no";
            }

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                databaseAccess.open();
                String first_name = databaseAccess.checkNameWithNameMaster(firstname);
                String last_name = databaseAccess.checkNameWithNameMaster(lastname);
                databaseAccess.close();

            owner_responsibility = sessionManager.getHeaderSession().get(SessionManager.OWNER_RESPONSIBILITY);
            assert owner_responsibility != null;
            if (owner_responsibility.equals("Y")){
                if (first_name.equals("") && last_name.equals("")) {

                    showAlertMessage(getResources().getString(R.string.first_and_last_name_not_found));

                } else if (first_name.equals("") || last_name.equals("")) {
                    if (first_name.equals("")) {
                        showAlertMessage(getResources().getString(R.string.first_name_not_found));
                    }
                    if (last_name.equals("")) {
                        showAlertMessage(getResources().getString(R.string.last_name_not_found));
                    }

                }else {

                    sessionManager.createOwnerVehicleSession(firstname,middlename,lastname,suffix,suffixid,address1,address2,city,city_id,state,state_id,zipcode);
                    sessionManager.saveVehicle("Y");
                    sessionManager.ClearVehicleEntery();
                    sessionManager.createVehicleSession(sameasdriver_check,firstname, middlename, lastname, suffix, suffixid, address1, address2, city, city_id, state, state_id, zipcode, s_vehyear, s_vehmake, s_vehmakeid, s_vehmodel, s_vehmodelid, s_vehbody, s_vehbodyid, s_vehcolor, s_vehcolorid, s_vehtype, s_vehtypeid, s_commercial, vehicleno, vehiclestate, vehiclestateid, s_hazardous, s_overload, policyno);
                    Intent i = new Intent(ActivityVehicle.this, ActivityViolation.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            } else {
                    sessionManager.createOwnerVehicleSession(firstname,middlename,lastname,suffix,suffixid,address1,address2,city,city_id,state,state_id,zipcode);
                    sessionManager.saveVehicle("Y");
                    sessionManager.ClearVehicleEntery();
                    sessionManager.createVehicleSession(sameasdriver_check,firstname, middlename, lastname, suffix, suffixid, address1, address2, city, city_id, state, state_id, zipcode, s_vehyear, s_vehmake, s_vehmakeid, s_vehmodel, s_vehmodelid, s_vehbody, s_vehbodyid, s_vehcolor, s_vehcolorid, s_vehtype, s_vehtypeid, s_commercial, vehicleno, vehiclestate, vehiclestateid, s_hazardous, s_overload, policyno);
                    Intent i = new Intent(ActivityVehicle.this, ActivityViolation.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }

        }

    }
    public void showAlertMessage(String message) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityVehicle.this);

        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String sameasdriver_check="";

                if (sameasdriver_checkbox.isChecked()){
                    sameasdriver_check="yes";
                }else {
                    sameasdriver_check="no";
                }

                sessionManager.createOwnerVehicleSession(firstname,middlename,lastname,suffix,suffixid,address1,address2,city,city_id,state,state_id,zipcode);
                sessionManager.saveVehicle("Y");
                sessionManager.ClearVehicleEntery();
                sessionManager.createVehicleSession(sameasdriver_check,firstname, middlename, lastname, suffix, suffixid, address1, address2, city, city_id, state, state_id, zipcode, s_vehyear, s_vehmake, s_vehmakeid, s_vehmodel, s_vehmodelid, s_vehbody, s_vehbodyid, s_vehcolor, s_vehcolorid, s_vehtype, s_vehtypeid, s_commercial, vehicleno, vehiclestate, vehiclestateid, s_hazardous, s_overload, policyno);
                Intent i = new Intent(ActivityVehicle.this, ActivityViolation.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        alertDialog.show();

    }

    private void init(){

        sameasdriver_checkbox=findViewById(R.id.sameasdriver_checkbox);
        owner_fields = findViewById(R.id.owner_fields);
        img_back = findViewById(R.id.img_back);
        owner_firstname = findViewById(R.id.owner_firstname);
        owner_middlename = findViewById(R.id.owner_middlename);
        owner_lastname = findViewById(R.id.owner_lastname);

        owner_firstname.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        owner_middlename.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        owner_lastname.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        owner_suffix = findViewById(R.id.owner_suffix);
        owner_address1 = findViewById(R.id.owner_address1);
        owner_address2 = findViewById(R.id.owner_address2);
        owner_city = findViewById(R.id.owner_city);
        owner_state = findViewById(R.id.owner_state);
        owner_zipcode = findViewById(R.id.owner_zipcode);
        vehicle_no = findViewById(R.id.vehicle_no);
        vehicle_state = findViewById(R.id.vehicle_state);
        overload = findViewById(R.id.overload);
        policy_no = findViewById(R.id.policy_no);

        vehicle_scroll = findViewById(R.id.vehicle_scroll);

        vehicle_next_btn = findViewById(R.id.vehicle_next_btn);
        header_text = findViewById(R.id.header_text);
        img_menu = findViewById(R.id.img_menu);
        vehbody = findViewById(R.id.vehbody);
        vehcolor = findViewById(R.id.vehcolor);
        vehtype = findViewById(R.id.vehtype);
        vehmake = findViewById(R.id.vehmake);
        vehmodel = findViewById(R.id.vehmodel);
        vehyear = findViewById(R.id.vehyear);
        img_menu.setVisibility(View.INVISIBLE);
        header_text.setText(R.string.vehicle);
        commercial_group = findViewById(R.id.commercial_group);
        hazardous_group = findViewById(R.id.hazardous_group);
        rb_hazardous_yes = findViewById(R.id.rb_hazardous_yes);
        rb_hazardous_no = findViewById(R.id.rb_hazardous_no);
        rb_commercial_no = findViewById(R.id.rb_commercial_no);
        rb_commercial_yes = findViewById(R.id.rb_commercial_yes);


        zip_lay = findViewById(R.id.zip_lay);
        firstname_lay = findViewById(R.id.firstname_lay);
        address_lay = findViewById(R.id.address_lay);
        lastname_lay = findViewById(R.id.lastname_lay);
        city_lay = findViewById(R.id.city_lay);
        ownerstate_lay = findViewById(R.id.ownerstate_lay);


        vehmake_txt = findViewById(R.id.vehmake_txt);
        vehmodel_txt = findViewById(R.id.vehmodel_txt);
        ownercity_txt = findViewById(R.id.ownercity_txt);
        ownerstate_txt = findViewById(R.id.ownerstate_txt);
        vehiclestate_txt = findViewById(R.id.vehiclestate_txt);

        owner_responsibility = sessionManager.getHeaderSession().get(SessionManager.OWNER_RESPONSIBILITY);
        assert owner_responsibility != null;
        if (owner_responsibility.equals("N")){

            owner_firstname.setText("");
            owner_middlename.setText("");
            owner_lastname.setText("");
            owner_address1.setText("");
            owner_address2.setText("");
            owner_zipcode.setText("");
            suffix = "";
            suffixid = "";
            city = "";
            city_id = "";
            state = "";
            state_id = "";

//            owner_firstname.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_FIRST_NAME));
//            owner_middlename.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_MIDDLE_NAME));
//            owner_lastname.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_LAST_NAME));
//            owner_address1.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS1));
//            owner_address2.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS2));
//            owner_zipcode.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_ZIPCODE));
//            suffix = sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX);
//            suffixid = sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX_ID);
//            city = sessionManager.getDriverSession().get(SessionManager.DRIVER_CITY);
//            city_id = sessionManager.getDriverSession().get(SessionManager.DRIVER_CITY_ID);
//            state = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE);
//            state_id = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE_ID);

//            sessionManager.createOwnerVehicleSession(owner_firstname.getText().toString().trim(),owner_middlename.getText().toString().trim(),owner_lastname.getText().toString().trim(),suffix,suffixid,owner_address1.getText().toString().trim(),owner_address2.getText().toString().trim(),city,city_id,state,state_id,owner_zipcode.getText().toString().trim());

            owner_fields.setVisibility(View.GONE);
        }else {

            owner_fields.setVisibility(View.VISIBLE);

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
            databaseAccess.open();

            String session_ownerfirstname = sessionManager.getVehicleSession().get(SessionManager.OWNER_FIRST_NAME);
            assert session_ownerfirstname != null;
            if (!session_ownerfirstname.equals("")){

                String session_sameasdriver_check = sessionManager.getVehicleSession().get(SessionManager.SAME_AS_DRIVER_CHECK);
                String session_ownermiddlename = sessionManager.getVehicleSession().get(SessionManager.OWNER_MIDDLE_NAME);
                String session_ownerlastname = sessionManager.getVehicleSession().get(SessionManager.OWNER_LAST_NAME);
                String session_owneraddress1 = sessionManager.getVehicleSession().get(SessionManager.OWNER_ADDRESS1);
                String session_owneraddress2 = sessionManager.getVehicleSession().get(SessionManager.OWNER_ADDRESS2);
                String session_ownerzipcode = sessionManager.getVehicleSession().get(SessionManager.OWNER_ZIPCODE);


                if (session_sameasdriver_check!=null){
                    if (session_sameasdriver_check.equals("yes")){
                        sameasdriver_checkbox.setChecked(true);
                    }else {
                        sameasdriver_checkbox.setChecked(false);
                    }
                }

                if (session_ownerfirstname!=null){
                    owner_firstname.setText(session_ownerfirstname);
                }
                if (session_ownermiddlename!=null){
                    owner_middlename.setText(session_ownermiddlename);
                }
                if (session_ownerlastname!=null){
                    owner_lastname.setText(session_ownerlastname);
                }
                if (session_owneraddress1!=null){
                    owner_address1.setText(session_owneraddress1);
                }
                if (session_owneraddress2!=null){
                    owner_address2.setText(session_owneraddress2);
                }
                if (session_ownerzipcode!=null){
                    owner_zipcode.setText(session_ownerzipcode);
                }



                String session_ownerstate = sessionManager.getVehicleSession().get(SessionManager.OWNER_STATE);
                String session_ownerstateid = sessionManager.getVehicleSession().get(SessionManager.OWNER_STATE_ID);

                assert session_ownerstate != null;
                if (!session_ownerstate.equals("")){

                    state = session_ownerstate;
                    state_id = session_ownerstateid;


                    String selected_value = databaseAccess.getSelectedStateName(session_ownerstate,session_ownerstateid);


                    ownerstate_txt.setText(selected_value);
                }else {

                    String selected_value = "California";
                    state = databaseAccess.getSelectedStateValue(selected_value);
                    state_id = databaseAccess.getSelectedStateId(selected_value,state);
                    ownerstate_txt.setText(selected_value);
                }


                String  session_ownercity = sessionManager.getVehicleSession().get(SessionManager.OWNER_CITY);
                String session_ownercityid = sessionManager.getVehicleSession().get(SessionManager.OWNER_CITY_ID);

                assert session_ownercity != null;
                if (!session_ownercity.equals("")){

                    city = session_ownercity;
                    city_id = session_ownercityid;
                    String selected_value = databaseAccess.getSelectedCityName(session_ownercity,session_ownercityid);

                    ownercity_txt.setText(selected_value);
                }


            }else{


                if (sameasdriver_checkbox.isChecked()){

                    owner_firstname.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_FIRST_NAME));
                    owner_middlename.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_MIDDLE_NAME));
                    owner_lastname.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_LAST_NAME));
                    owner_address1.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS1));
                    owner_address2.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS2));
                    owner_zipcode.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_ZIPCODE));
                    suffix = sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX);
                    suffixid = sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX_ID);
                    city = sessionManager.getDriverSession().get(SessionManager.DRIVER_CITY);
                    city_id = sessionManager.getDriverSession().get(SessionManager.DRIVER_CITY_ID);
                    state = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE);
                    state_id = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE_ID);

                    assert state != null;
                    if (!state.equals("")){
                        String selected_value = databaseAccess.getSelectedStateName(state,state_id);
                        ownerstate_txt.setText(selected_value);
                    }else{
                        String selected_value = "California";
                        state = databaseAccess.getSelectedStateValue(selected_value);
                        state_id = databaseAccess.getSelectedStateId(selected_value,state);
                        ownerstate_txt.setText(selected_value);
                    }

                    assert city != null;
                    if (!city.equals("")){
                        String selected_value = databaseAccess.getSelectedCityName(city,city_id);
                        ownercity_txt.setText(selected_value);
                    }


                    owner_firstname.setFocusable(false);
                    owner_middlename.setFocusable(false);
                    owner_lastname.setFocusable(false);
                    owner_address1.setFocusable(false);
                    owner_address2.setFocusable(false);
                    owner_zipcode.setFocusable(false);
                    owner_suffix.setEnabled(false);

                    ownerstate_txt.setFocusableInTouchMode(false);
                    ownerstate_txt.setClickable(false);
                    ownercity_txt.setFocusableInTouchMode(false);
                    ownercity_txt.setClickable(false);
                    ownercity_txt.setFocusable(false);
                    ownercity_txt.setEnabled(false);


                }else {
                    owner_firstname.setFocusableInTouchMode(true);
                    owner_middlename.setFocusableInTouchMode(true);
                    owner_lastname.setFocusableInTouchMode(true);
                    owner_address1.setFocusableInTouchMode(true);
                    owner_address2.setFocusableInTouchMode(true);
                    owner_zipcode.setFocusableInTouchMode(true);

                    owner_suffix.setEnabled(true);

                    ownerstate_txt.setFocusableInTouchMode(true);
                    ownerstate_txt.setClickable(true);
                    ownercity_txt.setFocusableInTouchMode(true);
                    ownercity_txt.setClickable(true);
                    ownercity_txt.setFocusable(true);
                    ownercity_txt.setEnabled(true);

                    owner_firstname.setText("");
                    owner_middlename.setText("");
                    owner_lastname.setText("");
                    owner_address1.setText("");
                    owner_address2.setText("");
                    owner_zipcode.setText("");
                    suffix = "";
                    suffixid = "";
                    city = "";
                    city_id = "";
                    state = "";
                    state_id = "";

                    driversuffixSpinnerData(suffix,suffixid);

                    databaseAccess.open();

                    assert state != null;
                    if (!state.equals("")){
                        String selected_value = databaseAccess.getSelectedStateName(state,state_id);
                        ownerstate_txt.setText(selected_value);
                    }else {
                        String selected_value = "California";
                        state = databaseAccess.getSelectedStateValue(selected_value);
                        state_id = databaseAccess.getSelectedStateId(selected_value,state);
                        ownerstate_txt.setText(selected_value);
                    }
                    assert city != null;
                    if (!city.equals("")){
                        String selected_value = databaseAccess.getSelectedCityName(city,city_id);
                        ownercity_txt.setText(selected_value);
                    }else {
                        ownercity_txt.setText("");
                        ownercity_txt.setHint(getResources().getString(R.string.owner_city));
                    }

                }

                databaseAccess.close();
            }


            sameasdriver_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   Log.e("heyyaa",""+isChecked);

                   if (isChecked){

                       owner_firstname.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_FIRST_NAME));
                       owner_middlename.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_MIDDLE_NAME));
                       owner_lastname.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_LAST_NAME));
                       owner_address1.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS1));
                       owner_address2.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS2));
                       owner_zipcode.setText(sessionManager.getDriverSession().get(SessionManager.DRIVER_ZIPCODE));
                       suffix = sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX);
                       suffixid = sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX_ID);
                       city = sessionManager.getDriverSession().get(SessionManager.DRIVER_CITY);
                       city_id = sessionManager.getDriverSession().get(SessionManager.DRIVER_CITY_ID);
                       state = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE);
                       state_id = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE_ID);

                       driversuffixSpinnerData(suffix,suffixid);

                       DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                       databaseAccess.open();

                       assert state != null;
                       if (!state.equals("")){
                           String selected_value = databaseAccess.getSelectedStateName(state,state_id);
                           ownerstate_txt.setText(selected_value);
                       }else {
                           String selected_value = "California";
                           state = databaseAccess.getSelectedStateValue(selected_value);
                           state_id = databaseAccess.getSelectedStateId(selected_value,state);
                           ownerstate_txt.setText(selected_value);
                       }
                       assert city != null;
                       if (!city.equals("")){
                           String selected_value = databaseAccess.getSelectedCityName(city,city_id);
                           ownercity_txt.setText(selected_value);
                       }
                       databaseAccess.close();

                       owner_firstname.setFocusable(false);
                       owner_middlename.setFocusable(false);
                       owner_lastname.setFocusable(false);
                       owner_address1.setFocusable(false);
                       owner_address2.setFocusable(false);
                       owner_zipcode.setFocusable(false);
                       owner_suffix.setEnabled(false);

                       ownerstate_txt.setFocusableInTouchMode(false);
                       ownerstate_txt.setClickable(false);
                       ownercity_txt.setFocusableInTouchMode(false);
                       ownercity_txt.setClickable(false);
                       ownercity_txt.setFocusable(false);
                       ownercity_txt.setEnabled(false);

                   }else {

                       owner_firstname.setFocusableInTouchMode(true);
                       owner_middlename.setFocusableInTouchMode(true);
                       owner_lastname.setFocusableInTouchMode(true);
                       owner_address1.setFocusableInTouchMode(true);
                       owner_address2.setFocusableInTouchMode(true);
                       owner_zipcode.setFocusableInTouchMode(true);
                       owner_suffix.setEnabled(true);
                       ownerstate_txt.setFocusableInTouchMode(true);
                       ownerstate_txt.setClickable(true);
                       ownercity_txt.setFocusableInTouchMode(true);
                       ownercity_txt.setClickable(true);
                       ownercity_txt.setFocusable(true);
                       ownercity_txt.setEnabled(true);


                       owner_firstname.setText("");
                       owner_middlename.setText("");
                       owner_lastname.setText("");
                       owner_address1.setText("");
                       owner_address2.setText("");
                       owner_zipcode.setText("");
                       suffix = "";
                       suffixid = "";
                       city = "";
                       city_id = "";
                       state = "";
                       state_id = "";

                       driversuffixSpinnerData(suffix,suffixid);

                       DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                       databaseAccess.open();

                       assert state != null;
                       if (!state.equals("")){
                           String selected_value = databaseAccess.getSelectedStateName(state,state_id);
                           ownerstate_txt.setText(selected_value);
                       }else {
                           String selected_value = "California";
                           state = databaseAccess.getSelectedStateValue(selected_value);
                           state_id = databaseAccess.getSelectedStateId(selected_value,state);
                           ownerstate_txt.setText(selected_value);
                       }
                       assert city != null;
                       if (!city.equals("")){
                           String selected_value = databaseAccess.getSelectedCityName(city,city_id);
                           ownercity_txt.setText(selected_value);
                       }else {
                           ownercity_txt.setText("");
                           ownercity_txt.setHint(getResources().getString(R.string.owner_city));
                       }
                       databaseAccess.close();
                   }
                }
            });

//            owner_firstname.setText("");
//            owner_middlename.setText("");
//            owner_lastname.setText("");
//            owner_address1.setText("");
//            owner_address2.setText("");
//            owner_zipcode.setText("");
//            suffix = "";
//            suffixid = "";
//            city = "";
//            city_id = "";
//            state = "";
//            state_id = "";

//            sessionManager.createOwnerVehicleSession(owner_firstname.getText().toString().trim(),owner_middlename.getText().toString().trim(),owner_lastname.getText().toString().trim(),suffix,suffixid,owner_address1.getText().toString().trim(),owner_address2.getText().toString().trim(),city,city_id,state,state_id,owner_zipcode.getText().toString().trim());




        }

        String session_vehicleno = sessionManager.getVehicleSession().get(SessionManager.VEHNO);
//        String session_vehiclestate = sessionManager.getVehicleSession().get(SessionManager.VEHSTATE);
        String session_overload = sessionManager.getVehicleSession().get(SessionManager.OVERLOAD);
        String session_policyno = sessionManager.getVehicleSession().get(SessionManager.POLICYNO);
        String session_hazardous = sessionManager.getVehicleSession().get(SessionManager.HAZARDOUS);
        String session_commercial = sessionManager.getVehicleSession().get(SessionManager.COMMERCIAL);

        if (session_vehicleno!=null){
            vehicle_no.setText(session_vehicleno);
        }

        assert session_overload != null;
        if (!session_overload.equals("")){
            overload.setText(session_overload);
        }
        if (session_policyno!=null){
            policy_no.setText(session_policyno);
        }

        if (session_hazardous!=null){
            if (session_hazardous.equals("Y")){
                rb_hazardous_yes.setChecked(true);
                rb_hazardous_no.setChecked(false);
            }else if (session_hazardous.equals("N")){
                rb_hazardous_yes.setChecked(false);
                rb_hazardous_no.setChecked(true);
            }else {
                rb_hazardous_yes.setChecked(false);
                rb_hazardous_no.setChecked(false);
            }
        }
        if (session_commercial!=null){
            if (session_commercial.equals("1")){
                rb_commercial_yes.setChecked(true);
                rb_commercial_no.setChecked(false);
            }else if (session_commercial.equals("0")){
                rb_commercial_yes.setChecked(false);
                rb_commercial_no.setChecked(true);
            }else {
                rb_commercial_yes.setChecked(false);
                rb_commercial_no.setChecked(false);
            }
        }

        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
        databaseAccess.open();

        String session_vehmake = sessionManager.getVehicleSession().get(SessionManager.VEHMAKE);
        String session_vehmakeid = sessionManager.getVehicleSession().get(SessionManager.VEHMAKE_ID);

        assert session_vehmake != null;
        if (!session_vehmake.equals("")){

            s_vehmake = session_vehmake;
            s_vehmakeid = session_vehmakeid;


            String selected_value = databaseAccess.getSelectedVehicleMakeValue(session_vehmake,session_vehmakeid);
//            databaseAccess.close();

            vehmake_txt.setText(selected_value);
        }

        String session_vehmodel = sessionManager.getVehicleSession().get(SessionManager.VEHMODEL);
        String session_vehmodelid = sessionManager.getVehicleSession().get(SessionManager.VEHMODEL_ID);

        assert session_vehmodel != null;
        if (!session_vehmodel.equals("")){

            s_vehmodel = sessionManager.getVehicleSession().get(SessionManager.VEHMODEL);
            s_vehmodelid = sessionManager.getVehicleSession().get(SessionManager.VEHMODEL_ID);
//            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
//            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedVehicleModelName(session_vehmodel,session_vehmodelid);
//            databaseAccess.close();

            vehmodel_txt.setText(selected_value);
        }




        String session_vehiclestate = sessionManager.getVehicleSession().get(SessionManager.VEHSTATE);
        String session_vehiclestateid = sessionManager.getVehicleSession().get(SessionManager.VEHSTATE_ID);
        assert session_vehiclestate != null;
        if (!session_vehiclestate.equals("")){

            vehiclestate = session_vehiclestate;
            vehiclestateid = session_vehiclestateid;
//            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
//            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedStateName(session_vehiclestate,session_vehiclestateid);
//            databaseAccess.close();

            vehiclestate_txt.setText(selected_value);
        }


        databaseAccess.close();
    }
    @Override
    public void onBackPressed() {

        if (selected.equals("vehicle")){
            Intent i = new Intent(ActivityVehicle.this, ActivityHome.class);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        }

    }


    private void driversuffixSpinnerData(String driver_suffix, String driver_suffix_id){
//        driver_suffix = sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX);
//        driver_suffix_id = sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX_ID);
        ArrayAdapter<String> suffix_arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, suffixList);
        suffix_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        owner_suffix.setAdapter(suffix_arrayAdapter);
        if (driver_suffix!=null){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSuffixName(driver_suffix_id);
            databaseAccess.close();

            int spinnerPosition = suffix_arrayAdapter.getPosition(selected_value);
            owner_suffix.setSelection(spinnerPosition);
        }
        owner_suffix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                databaseAccess.open();
                String suffix_id = databaseAccess.getSuffixId(selected);
                databaseAccess.close();


                if (selected.equals("SUFFIX")){
                    suffix="";
                    suffixid="";
                }else {
                    suffix=selected;
                    suffixid=suffix_id;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


    }
    private void getSpinnerData() {

        String session_ownersuffix = sessionManager.getVehicleSession().get(SessionManager.OWNER_SUFFIX);
        String session_ownersuffixid = sessionManager.getVehicleSession().get(SessionManager.OWNER_SUFFIX_ID);
        ArrayAdapter<String> suffix_arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, suffixList);
        suffix_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        owner_suffix.setAdapter(suffix_arrayAdapter);
        if (session_ownersuffix!=null){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSuffixName(session_ownersuffixid);
            databaseAccess.close();

            int spinnerPosition = suffix_arrayAdapter.getPosition(selected_value);
            owner_suffix.setSelection(spinnerPosition);
        }
        owner_suffix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                databaseAccess.open();
                String suffix_id = databaseAccess.getSuffixId(selected);
                databaseAccess.close();


                if (selected.equals("SUFFIX")){
                    suffix="";
                    suffixid="";
                }else {
                    suffix=selected;
                    suffixid=suffix_id;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


        String session_vehbody = sessionManager.getVehicleSession().get(SessionManager.VEHBODY);
        String session_vehbodyid = sessionManager.getVehicleSession().get(SessionManager.VEHBODY_ID);
        ArrayAdapter<String> vehbody_arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, vehcileBodyList);
        vehbody_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehbody.setAdapter(vehbody_arrayAdapter);
        if (session_vehbody != null) {

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedVehBodyName(session_vehbody, session_vehbodyid);
            databaseAccess.close();

            int spinnerPosition = vehbody_arrayAdapter.getPosition(selected_value);
            vehbody.setSelection(spinnerPosition);
        }
        vehbody.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                databaseAccess.open();
                String selected_value = databaseAccess.getSelectedVehBody(selected);
                String selected_id = databaseAccess.getSelectedVehBodyId(selected_value, selected);
                databaseAccess.close();

                if (selected.equals("VEHICLE BODY")) {
                    s_vehbody = "";
                    s_vehbodyid = "";
                } else {
                    s_vehbody = selected_value;
                    s_vehbodyid = selected_id;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String session_vehcolor = sessionManager.getVehicleSession().get(SessionManager.VEHCOLOR);
        String session_vehcolorid = sessionManager.getVehicleSession().get(SessionManager.VEHCOLOR_ID);
        ArrayAdapter<String> vehcolor_arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, vehcileColorList);
        vehcolor_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehcolor.setAdapter(vehcolor_arrayAdapter);
        if (session_vehcolor != null) {

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedVehColorName(session_vehcolor, session_vehcolorid);
            databaseAccess.close();

            int spinnerPosition = vehcolor_arrayAdapter.getPosition(selected_value);
            vehcolor.setSelection(spinnerPosition);
        }
        vehcolor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                databaseAccess.open();
                String selected_value = databaseAccess.getSelectedVehColor(selected);
                String selected_id = databaseAccess.getSelectedVehColorId(selected_value, selected);
                databaseAccess.close();

                if (selected.equals("VEHICLE COLOR")) {
                    s_vehcolor = "";
                    s_vehcolorid = "";
                } else {
                    s_vehcolor = selected_value;
                    s_vehcolorid = selected_id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String session_vehtype = sessionManager.getVehicleSession().get(SessionManager.VEHTYPE);
        String session_vehtypeid = sessionManager.getVehicleSession().get(SessionManager.VEHTYPE_ID);
        ArrayAdapter<String> vehtype_arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, vehcileTypeList);
        vehtype_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehtype.setAdapter(vehtype_arrayAdapter);
        if (session_vehtype != null) {

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getVehTypeName(session_vehtype, session_vehtypeid);
            databaseAccess.close();

            int spinnerPosition = vehtype_arrayAdapter.getPosition(selected_value);
            vehtype.setSelection(spinnerPosition);
        }
        vehtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = parent.getItemAtPosition(position).toString();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
                databaseAccess.open();
                String selected_vehtype = databaseAccess.getVehType(selected);
                String selected_id = databaseAccess.getVehTypeId(selected_vehtype, selected);
                databaseAccess.close();


                if (selected.equals("VEHICLE TYPE")) {
                    s_vehtype = "";
                    s_vehtypeid = "";
                } else {
                    s_vehtype = selected_vehtype;
                    s_vehtypeid = selected_id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        String session_vehyear = sessionManager.getVehicleSession().get(SessionManager.VEHYEAR);
        ArrayAdapter<String> vehyear_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, vehcileYearList);
        vehyear_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehyear.setAdapter(vehyear_adapter);
        if (session_vehyear != null) {
            int spinnerPosition = vehyear_adapter.getPosition(session_vehyear);
            vehyear.setSelection(spinnerPosition);
        }
        vehyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = parent.getItemAtPosition(position).toString();
                if (selected.equals("VEHICLE YEAR")) {
                    s_vehyear = "";
                } else {
                    s_vehyear = selected;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void getcitywithstateid(String state_id){
        cityList.clear();
        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
        databaseAccess.open();
        cityList = databaseAccess.getCityWithStateId(state_id);
        databaseAccess.close();
    }

    private void getcity(){
        cityList.clear();
        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
        databaseAccess.open();
        cityList = databaseAccess.getCity();
        databaseAccess.close();
    }

    private void getAllListData(){
        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
        databaseAccess.open();
        suffixList = databaseAccess.getSuffix();
        stateList = databaseAccess.getState();
        vehiclestateList = databaseAccess.getState();
        vehcileBodyList = databaseAccess.getVehBody();
        vehcileColorList = databaseAccess.getVehColor();
        vehcileTypeList = databaseAccess.getVehDescription();
        databaseAccess.close();
    }



    private void selectVehcileMake(){

        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
        databaseAccess.open();
        vehcileMakeList = databaseAccess.getVehicleMake();
        databaseAccess.close();
    }

    private void selectVehcileModel(){
        vehcileModelList.clear();
        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
        databaseAccess.open();
        vehcileModelList = databaseAccess.getVehicleModel();
        databaseAccess.close();
    }

    private void selectVehcileModelWithVehMakeId(String veh_make_id){
        vehcileModelList.clear();
        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityVehicle.this);
        databaseAccess.open();
        vehcileModelList = databaseAccess.getVehicleModelWithVehMakeId(veh_make_id);
        databaseAccess.close();
    }

    private void selectVehicleYear(){

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        Calendar preyear = Calendar.getInstance();
        preyear.add(Calendar.YEAR, -99);

        int prevYearnew = preyear.get(Calendar.YEAR);
        vehcileYearList.add("VEHICLE YEAR");
        for (int i = prevYearnew; i <= thisYear; i++) {
            int s = i % 100;

            if (s>=0&&s<10){
                vehcileYearList.add("0"+s);

            }else {
                vehcileYearList.add(Integer.toString(s));
            }

        }

    }
}