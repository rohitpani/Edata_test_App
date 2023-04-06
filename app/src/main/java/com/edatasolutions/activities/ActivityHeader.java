package com.edatasolutions.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.edatasolutions.adapters.CourtCodeAdapter;
import com.edatasolutions.database.DatabaseAccess;
import com.edatasolutions.models.CourtCodeModel;
import com.edatasolutions.utils.Constants;
import com.edatasolutions.utils.SessionManager;

import java.util.ArrayList;

public class ActivityHeader extends AppCompatActivity {

    private Button header_next_btn;
    private TextView header_text, or_txt, citation_no_txt, lea_txt;
    private ImageView img_menu, img_back;
    private Spinner  courtcode;
    private ArrayList<String> courtCodeValueList = new ArrayList<>();
    private ArrayList<String> courtCodeDescList = new ArrayList<>();
    private ArrayList<String> leaList = new ArrayList<>();
    private RadioGroup owner_responsibility, ped, traffic_radiogroup;
    private RadioButton r_owner_responsibility, r_ped, r_traffic;
    private SessionManager sessionManager;
    private String s_citation_no, s_owner_responsibility = "N", s_traffic = "0", s_nontraffic = "0", s_courtcode = "", s_lea = "", s_leaid = "", s_ped = "N", s_courtcode_id = "",s_courtcode_pos="";
    private String user_id;
    private String selected = "";
    private RadioButton rb_or_yes, rb_or_no, rb_ped_yes, rb_ped_no, rb_traffic_yes, rb_traffic_no;
    private LinearLayout traffic_lay;
    private FrameLayout courtcode_lay;
    private ArrayAdapter<String> adapter;
    private LinearLayout lea_lay;
    private ScrollView header_scroll;
    ArrayList<CourtCodeModel> courtCodeArrayList;
    CourtCodeAdapter courtCodeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);
        sessionManager = new SessionManager(ActivityHeader.this);


        init();

        if (getIntent().hasExtra("selected")) {
            selected = getIntent().getStringExtra("selected");
        }

        getLea();
        courtcodeList();
        getSpinnerData();
        getCitationNum();


        header_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                // overridePendingTransition(R.animator.exit,R.animator.enter);
            }
        });


        lea_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ActivityHeader.this);
                LayoutInflater inflater = (LayoutInflater) ActivityHeader.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityHeader.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, leaList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityHeader.this.adapter.getFilter().filter(cs);

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
                        lea_txt.setText(selected_value);
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

    private void courtcodeList() {

        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHeader.this);
        databaseAccess.open();
        courtCodeDescList = databaseAccess.getCourtCodeDesc();
        courtCodeValueList = databaseAccess.getCourtCodeValue();
        databaseAccess.close();

        courtCodeArrayList = new ArrayList<>();

        for (int i = 0; i < courtCodeDescList.size(); i++) {
            CourtCodeModel courtCodeModel = new CourtCodeModel();
            courtCodeModel.setCourt_description(courtCodeDescList.get(i));
            courtCodeModel.setCourt_code_value(courtCodeValueList.get(i));

            courtCodeArrayList.add(courtCodeModel);
        }

    }

    private void init() {

        user_id = sessionManager.getAdminLogin().get(SessionManager.USER_ID);

        citation_no_txt = findViewById(R.id.citation_no_txt);
        header_next_btn = findViewById(R.id.header_next_btn);
        header_text = findViewById(R.id.header_text);
        img_menu = findViewById(R.id.img_menu);
        img_back = findViewById(R.id.img_back);
        courtcode = findViewById(R.id.courtcode);
        owner_responsibility = findViewById(R.id.owner_responsibility);
        ped = findViewById(R.id.ped);
        img_menu.setVisibility(View.INVISIBLE);
        header_text.setText(R.string.header);

        header_scroll= findViewById(R.id.header_scroll);

        rb_or_yes = findViewById(R.id.rb_or_yes);
        rb_or_no = findViewById(R.id.rb_or_no);
        rb_ped_yes = findViewById(R.id.rb_ped_yes);
        rb_ped_no = findViewById(R.id.rb_ped_no);
        rb_traffic_yes = findViewById(R.id.rb_traffic_yes);
        rb_traffic_no = findViewById(R.id.rb_traffic_no);
        traffic_radiogroup = findViewById(R.id.traffic_radiogroup);
        lea_txt = findViewById(R.id.lea_txt);

        traffic_lay = findViewById(R.id.traffic_lay);
        courtcode_lay = findViewById(R.id.courtcode_lay);
        lea_lay = findViewById(R.id.lea_lay);

        String session_or = sessionManager.getHeaderSession().get(SessionManager.OWNER_RESPONSIBILITY);
        String session_ped = sessionManager.getHeaderSession().get(SessionManager.PED);
        String session_traffic = sessionManager.getHeaderSession().get(SessionManager.TRAFFIC);
        String session_nontraffic = sessionManager.getHeaderSession().get(SessionManager.NON_TRAFFIC);

        String session_lea = sessionManager.getHeaderSession().get(SessionManager.LEA);
        String session_leaid = sessionManager.getHeaderSession().get(SessionManager.LEA_ID);

        assert session_lea != null;
        if (!session_lea.equals("")) {

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHeader.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getLeaName(session_leaid);
            databaseAccess.close();

            lea_txt.setText(selected_value);
        }

        if (session_nontraffic != null) {
            if (session_nontraffic.equals("0")) {
                rb_traffic_no.setChecked(false);
            } else if (session_nontraffic.equals("1")) {
                rb_traffic_no.setChecked(true);
            } else {
                rb_traffic_no.setChecked(false);
            }
        }

        if (session_traffic != null) {
            if (session_traffic.equals("0")) {
                rb_traffic_yes.setChecked(false);
            } else if (session_traffic.equals("1")) {
                rb_traffic_yes.setChecked(true);
            } else {
                rb_traffic_yes.setChecked(false);
            }
        }

        if (session_or != null) {
            if (session_or.equals("Y")) {
                rb_or_yes.setChecked(true);
                rb_or_no.setChecked(false);
            } else if (session_or.equals("N")) {
                rb_or_no.setChecked(true);
                rb_or_yes.setChecked(false);
            } else {
                rb_or_yes.setChecked(false);
                rb_or_no.setChecked(false);
            }
        }

        if (session_ped != null) {
            if (session_ped.equals("Y")) {
                rb_ped_yes.setChecked(true);
                rb_ped_no.setChecked(false);
            } else if (session_ped.equals("N")) {
                rb_ped_yes.setChecked(false);
                rb_ped_no.setChecked(true);
            } else {
                rb_ped_yes.setChecked(false);
                rb_ped_no.setChecked(false);
            }
        }
    }

    private void checkValidation() {


        s_lea = lea_txt.getText().toString();


        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHeader.this);
        databaseAccess.open();
        s_leaid = databaseAccess.getLeaId(s_lea);
        databaseAccess.close();

        if (traffic_radiogroup.getCheckedRadioButtonId() == -1) {
            s_traffic = "";
            s_nontraffic = "";
        } else {
            int comm_selectedId = traffic_radiogroup.getCheckedRadioButtonId();
            r_traffic = (RadioButton) findViewById(comm_selectedId);

            if (r_traffic.getText().toString().equals("Yes")) {
                s_traffic = "1";
                s_nontraffic = "0";
            } else if (r_traffic.getText().toString().equals("No")) {
                s_traffic = "0";
                s_nontraffic = "1";
            } else {
                s_traffic = "0";
                s_nontraffic = "0";
            }
        }


        if (owner_responsibility.getCheckedRadioButtonId() == -1) {
            s_owner_responsibility = "N";
        } else {
            int comm_selectedId = owner_responsibility.getCheckedRadioButtonId();
            r_owner_responsibility = (RadioButton) findViewById(comm_selectedId);

            if (r_owner_responsibility.getText().toString().equals("Yes")) {
                s_owner_responsibility = "Y";
            } else {
                s_owner_responsibility = "N";
            }
        }

        if (ped.getCheckedRadioButtonId() == -1) {
            s_ped = "N";
        } else {
            int comm_selectedId = ped.getCheckedRadioButtonId();
            r_ped = (RadioButton) findViewById(comm_selectedId);
            if (r_ped.getText().toString().equals("Yes")) {
                s_ped = "Y";
            } else {
                s_ped = "N";
            }
        }

        boolean valid = true;
        if (TextUtils.isEmpty(s_traffic) && TextUtils.isEmpty(s_nontraffic)) {
            traffic_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            traffic_lay.getParent().requestChildFocus(traffic_lay,traffic_lay);
            valid = false;
        } else {
            traffic_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
        }

        if (TextUtils.isEmpty(s_courtcode)) {
            courtcode_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            courtcode_lay.getParent().requestChildFocus(courtcode_lay,courtcode_lay);
            valid = false;
        } else {
            courtcode_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
        }

        if (TextUtils.isEmpty(s_lea)) {
            lea_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
            lea_lay.getParent().requestChildFocus(lea_lay,lea_lay);
            valid = false;
        } else {
            lea_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
        }

        if(TextUtils.isEmpty(s_traffic) && TextUtils.isEmpty(s_nontraffic) && TextUtils.isEmpty(s_courtcode) && TextUtils.isEmpty(s_lea)){
            header_scroll.fullScroll(View.FOCUS_UP);
        }

        if (valid) {
            sessionManager.saveHeader("Y");
            sessionManager.ClearHeaderEntery();
            sessionManager.createHeaderSession(s_citation_no, s_owner_responsibility, s_traffic, s_nontraffic, s_ped, s_courtcode, s_courtcode_id,s_courtcode_pos, s_lea, s_leaid);
            Intent i = new Intent(ActivityHeader.this, ActivityDriver.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }


    }

    @Override
    public void onBackPressed() {


        Intent i = new Intent(ActivityHeader.this, ActivityHome.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);


    }

    private void getSpinnerData() {


        String session_courtcode = sessionManager.getHeaderSession().get(SessionManager.COURT_CODE);
        String session_courtcodeid = sessionManager.getHeaderSession().get(SessionManager.COURT_CODE_ID);
        String session_courtcodepos = sessionManager.getHeaderSession().get(SessionManager.COURT_CODE_POSITION);

        courtCodeAdapter = new CourtCodeAdapter(ActivityHeader.this, courtCodeArrayList);
        courtCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courtcode.setAdapter(courtCodeAdapter);

        assert session_courtcodepos != null;
        if (!session_courtcodepos.equals("")) {

//            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHeader.this);
//            databaseAccess.open();
//            String selected_desc = databaseAccess.getSelectedCourtCodeName(session_courtcode, session_courtcodeid);
//            databaseAccess.close();
//
//            String courtcode1 = selected_desc + " " + session_courtcode;


            int i = Integer.parseInt(session_courtcodepos);
            courtcode.setSelection(i);
        }
        courtcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CourtCodeModel clickedItem = (CourtCodeModel) parent.getItemAtPosition(position);
                String selected_description = clickedItem.getCourt_description();
                String selected_value = clickedItem.getCourt_code_value();

                DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHeader.this);
                databaseAccess.open();
                String ssid = databaseAccess.getSelectedCourtCodeId(selected_description, selected_value);
                databaseAccess.close();

                if (selected_description.equals("COURT")&&selected_value.equals("CODE")) {
                    s_courtcode = "";
                    s_courtcode_id = "";
                    s_courtcode_pos="";
                } else {
                    s_courtcode = selected_value;
                    s_courtcode_id = ssid;
                    s_courtcode_pos=String.valueOf(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }



    private void getLea() {
        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHeader.this);
        databaseAccess.open();
        leaList = databaseAccess.getLea();
        databaseAccess.close();
    }

    private void getCitationNum() {

        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityHeader.this);
        databaseAccess.open();

        String starting_no = databaseAccess.getStartingCitationNumber(user_id);
        String last_no = databaseAccess.getLastCitationNumber(user_id);
        String lastupdated_no = databaseAccess.getLastUpdatedCitationNumber(user_id);

        int start_ = Integer.parseInt(starting_no);
        int last_ = Integer.parseInt(last_no);

        if (lastupdated_no.equals("null")) {

            citation_no_txt.setText(starting_no);
            s_citation_no = starting_no;
        } else {
            int lastupdate_ = Integer.parseInt(lastupdated_no);
            int new_ = lastupdate_ + 1;
            String new_citationNo = String.valueOf(new_);
            if (Integer.parseInt(new_citationNo) > last_) {
                citation_no_txt.setText("");
                header_next_btn.setVisibility(View.GONE);
                Toast.makeText(ActivityHeader.this, "Your citation limit has been over", Toast.LENGTH_SHORT).show();
            } else {
                header_next_btn.setVisibility(View.VISIBLE);
                citation_no_txt.setText(new_citationNo);
                s_citation_no = new_citationNo;
            }
        }

        databaseAccess.close();

    }
}