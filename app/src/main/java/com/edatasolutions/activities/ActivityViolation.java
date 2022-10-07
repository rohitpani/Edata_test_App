package com.edatasolutions.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.edatasolutions.database.DatabaseAccess;
import com.edatasolutions.utils.SessionManager;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class ActivityViolation extends AppCompatActivity {

    private TextView header_text;
    private Button violation_next_btn;
    private ImageView img_menu,img_back;
//    private Spinner violationA,violationB,violationC,violationD,violationE,violationF,violationG,violationH;
    private ArrayList<String> violationList = new ArrayList<>();

    private RadioGroup vca_group,vcb_group,vcc_group,vcd_group,vce_group,vcf_group,vcg_group,vch_group;
    private EditText apprspeed, pfspeed,vehlimit, safespeed, animal1, animal2, animal3, animal4, animal5, animal6, animal7, animal8;
    private String vio_a="",vio_a_id="", vio_b="", vio_b_id="", vio_c="",vio_c_id="", vio_d="",vio_d_id="", vio_e="",vio_e_id="", vio_f="",vio_f_id="", vio_g="",vio_g_id="", vio_h="",vio_h_id="";
    private String s_vca="N", s_vcb="N", s_vcc="N", s_vcd="N", s_vce="N", s_vcf="N", s_vcg="N", s_vch="N";
    private String  s_apprspeed="", s_pfspeed="",s_vehlimit="", s_safespeed="", s_animal1="", s_animal2="", s_animal3="", s_animal4="", s_animal5="", s_animal6="", s_animal7="", s_animal8="";
    private RadioButton r_vca, r_vcb, r_vcc, r_vcd, r_vce, r_vcf, r_vcg, r_vch;
    private SessionManager sessionManager;
    private String selected = "";
    private RadioButton rb_vca_yes,rb_vca_no,rb_vcb_yes,rb_vcb_no,rb_vcc_yes,rb_vcc_no,rb_vcd_yes,rb_vcd_no,rb_vce_yes,rb_vce_no,rb_vcf_yes,rb_vcf_no,rb_vcg_yes,rb_vcg_no,rb_vch_yes,rb_vch_no;
    private LinearLayout vioa_lay,apprspeed_lay;
    private ScrollView violation_scroll;
    private TextView violation_a,violation_b,violation_c,violation_d,violation_e,violation_f,violation_g,violation_h;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_violation);

        sessionManager = new SessionManager(ActivityViolation.this);
        init();


        if (getIntent().hasExtra("selected")){
            selected = getIntent().getStringExtra("selected");
        }


        getSessionData();
        selectViolation();

 //       getSpinnerData();


        violation_next_btn.setOnClickListener(new View.OnClickListener() {
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

        apprspeed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                String apprspeed_txt = apprspeed.getText().toString();

                if (apprspeed_txt.length()==2){
                    apprspeed.setText("0"+apprspeed_txt);
                }else if (apprspeed_txt.length()==1){
                    apprspeed.setText("00"+apprspeed_txt);
                }else {
                    apprspeed.setText(apprspeed_txt);
                }
            }
        });

        pfspeed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                String pfspeed_txt = pfspeed.getText().toString();

                if (pfspeed_txt.length()==1){
                    pfspeed.setText("0"+pfspeed_txt);
                }else {
                    pfspeed.setText(pfspeed_txt);
                }
            }
        });

        violation_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ActivityViolation.this);
                LayoutInflater inflater = (LayoutInflater) ActivityViolation.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityViolation.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, violationList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityViolation.this.adapter.getFilter().filter(cs);

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
                        violation_a.setText(selected_value);
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

        violation_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ActivityViolation.this);
                LayoutInflater inflater = (LayoutInflater) ActivityViolation.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityViolation.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, violationList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityViolation.this.adapter.getFilter().filter(cs);

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
                        violation_b.setText(selected_value);
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

        violation_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ActivityViolation.this);
                LayoutInflater inflater = (LayoutInflater) ActivityViolation.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityViolation.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, violationList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityViolation.this.adapter.getFilter().filter(cs);

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
                        violation_c.setText(selected_value);
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

        violation_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ActivityViolation.this);
                LayoutInflater inflater = (LayoutInflater) ActivityViolation.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityViolation.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, violationList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityViolation.this.adapter.getFilter().filter(cs);

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
                        violation_d.setText(selected_value);
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

        violation_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ActivityViolation.this);
                LayoutInflater inflater = (LayoutInflater) ActivityViolation.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityViolation.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, violationList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityViolation.this.adapter.getFilter().filter(cs);

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
                        violation_e.setText(selected_value);
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

        violation_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ActivityViolation.this);
                LayoutInflater inflater = (LayoutInflater) ActivityViolation.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityViolation.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, violationList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityViolation.this.adapter.getFilter().filter(cs);

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
                        violation_f.setText(selected_value);
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

        violation_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ActivityViolation.this);
                LayoutInflater inflater = (LayoutInflater) ActivityViolation.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityViolation.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, violationList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityViolation.this.adapter.getFilter().filter(cs);

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
                        violation_g.setText(selected_value);
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

        violation_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ActivityViolation.this);
                LayoutInflater inflater = (LayoutInflater) ActivityViolation.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View post = inflater.inflate(R.layout.violation_search_lay, null);
                EditText autoCompleteTextView1 = (EditText) post.findViewById((R.id.autoCompleteTextView1));
                ListView listView = (ListView) post.findViewById((R.id.listview));

                adapter = new ArrayAdapter<String>(ActivityViolation.this, R.layout.autocomplete_spinner_item, R.id.dropdown_txt, violationList);
                listView.setAdapter(adapter);

                autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        ActivityViolation.this.adapter.getFilter().filter(cs);

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
                        violation_h.setText(selected_value);
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


    private void selectViolation(){

        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolation.this);
        databaseAccess.open();
        violationList = databaseAccess.getVIOLATIONList();
        databaseAccess.close();
    }

    private void init(){

        violation_next_btn = findViewById(R.id.violation_next_btn);
        header_text = findViewById(R.id.header_text);
        img_menu = findViewById(R.id.img_menu);
        img_back = findViewById(R.id.img_back);
//        violationA = findViewById(R.id.violationA);
        violation_a = findViewById(R.id.violation_a);
        violation_b=findViewById(R.id.violation_b);
        violation_c=findViewById(R.id.violation_c);
        violation_d = findViewById(R.id.violation_d);
        violation_e=findViewById(R.id.violation_e);
        violation_f=findViewById(R.id.violation_f);
        violation_g = findViewById(R.id.violation_g);
        violation_h=findViewById(R.id.violation_h);

//        violationB = findViewById(R.id.violationB);
//        violationC = findViewById(R.id.violationC);
//        violationD = findViewById(R.id.violationD);
//        violationE = findViewById(R.id.violationE);
//        violationF = findViewById(R.id.violationF);
//        violationG = findViewById(R.id.violationG);
//        violationH = findViewById(R.id.violationH);
        img_menu.setVisibility(View.INVISIBLE);
        header_text.setText(R.string.violation);


        animal1 = findViewById(R.id.animal1);
        animal2 = findViewById(R.id.animal2);
        animal3 = findViewById(R.id.animal3);
        animal4 = findViewById(R.id.animal4);
        animal5 = findViewById(R.id.animal5);
        animal6 = findViewById(R.id.animal6);
        animal7 = findViewById(R.id.animal7);
        animal8 = findViewById(R.id.animal8);

        vca_group = findViewById(R.id.vca_group);
        vcb_group = findViewById(R.id.vcb_group);
        vcc_group = findViewById(R.id.vcc_group);
        vcd_group = findViewById(R.id.vcd_group);
        vce_group = findViewById(R.id.vce_group);
        vcf_group = findViewById(R.id.vcf_group);
        vcg_group = findViewById(R.id.vcg_group);
        vch_group = findViewById(R.id.vch_group);

        vehlimit = findViewById(R.id.vehlimit);
        safespeed = findViewById(R.id.safespeed);

        rb_vca_yes = findViewById(R.id.rb_vca_yes);
        rb_vca_no = findViewById(R.id.rb_vca_no);
        rb_vcb_no = findViewById(R.id.rb_vcb_no);
        rb_vcc_yes = findViewById(R.id.rb_vcc_yes);
        rb_vcb_yes = findViewById(R.id.rb_vcb_yes);
        rb_vcc_no = findViewById(R.id.rb_vcc_no);
        rb_vcd_yes = findViewById(R.id.rb_vcd_yes);
        rb_vcd_no = findViewById(R.id.rb_vcd_no);
        rb_vce_yes = findViewById(R.id.rb_vce_yes);
        rb_vce_no = findViewById(R.id.rb_vce_no);
        rb_vcf_yes = findViewById(R.id.rb_vcf_yes);
        rb_vcf_no = findViewById(R.id.rb_vcf_no);
        rb_vcg_yes = findViewById(R.id.rb_vcg_yes);
        rb_vcg_no = findViewById(R.id.rb_vcg_no);
        rb_vch_yes = findViewById(R.id.rb_vch_yes);
        rb_vch_no = findViewById(R.id.rb_vch_no);
        apprspeed = findViewById(R.id.apprspeed);
        pfspeed = findViewById(R.id.pfspeed);

        apprspeed_lay = findViewById(R.id.apprspeed_lay);
        vioa_lay = findViewById(R.id.vioa_lay);

        violation_scroll = findViewById(R.id.violation_scroll);

        String session_vca = sessionManager.getViolationSession().get(SessionManager.VCA);
        String session_vcb = sessionManager.getViolationSession().get(SessionManager.VCB);
        String session_vcc = sessionManager.getViolationSession().get(SessionManager.VCC);
        String session_vcd = sessionManager.getViolationSession().get(SessionManager.VCD);
        String session_vce = sessionManager.getViolationSession().get(SessionManager.VCE);
        String session_vcf = sessionManager.getViolationSession().get(SessionManager.VCF);
        String session_vcg = sessionManager.getViolationSession().get(SessionManager.VCG);
        String session_vch = sessionManager.getViolationSession().get(SessionManager.VCH);
        String session_vehlimit = sessionManager.getViolationSession().get(SessionManager.VEHLIMIT);
        String session_safespeed = sessionManager.getViolationSession().get(SessionManager.SAFE_SPEED);
        String session_appspeed = sessionManager.getViolationSession().get(SessionManager.APPRSPEED);
        String session_pfspeed = sessionManager.getViolationSession().get(SessionManager.PFSPEED);
        String session_animal1 = sessionManager.getViolationSession().get(SessionManager.ANIMAL1);
        String session_animal2 = sessionManager.getViolationSession().get(SessionManager.ANIMAL2);
        String session_animal3 = sessionManager.getViolationSession().get(SessionManager.ANIMAL3);
        String session_animal4 = sessionManager.getViolationSession().get(SessionManager.ANIMAL4);
        String session_animal5 = sessionManager.getViolationSession().get(SessionManager.ANIMAL5);
        String session_animal6 = sessionManager.getViolationSession().get(SessionManager.ANIMAL6);
        String session_animal7 = sessionManager.getViolationSession().get(SessionManager.ANIMAL7);
        String session_animal8 = sessionManager.getViolationSession().get(SessionManager.ANIMAL8);


        if (session_appspeed!=null){
            apprspeed.setText(session_appspeed);
        }
        if (session_pfspeed!=null){
            if (session_pfspeed.equals("00")){
                pfspeed.setText("");
                pfspeed.setHint(getResources().getString(R.string.pfspeed));
            }else {
                pfspeed.setText(session_pfspeed);
            }
        }
        if (session_vca!=null){
            if (session_vca.equals("Y")){
                rb_vca_yes.setChecked(true);
                rb_vca_no.setChecked(false);
            }else if (session_vca.equals("N")){
                rb_vca_yes.setChecked(false);
                rb_vca_no.setChecked(true);
            }else {
                rb_vca_yes.setChecked(false);
                rb_vca_no.setChecked(false);
            }
        }
        if (session_vcb!=null){
            if (session_vcb.equals("Y")){
                rb_vcb_yes.setChecked(true);
                rb_vcb_no.setChecked(false);
            }else if (session_vcb.equals("N")){
                rb_vcb_yes.setChecked(false);
                rb_vcb_no.setChecked(true);
            }else {
                rb_vcb_yes.setChecked(false);
                rb_vcb_no.setChecked(false);
            }
        }
        if (session_vcc!=null){
            if (session_vcc.equals("Y")){
                rb_vcc_yes.setChecked(true);
                rb_vcc_no.setChecked(false);
            }else if (session_vcc.equals("N")){
                rb_vcc_yes.setChecked(false);
                rb_vcc_no.setChecked(true);
            }else {
                rb_vcc_yes.setChecked(false);
                rb_vcc_no.setChecked(false);
            }
        }

        if (session_vcd!=null){
            if (session_vcd.equals("Y")){
                rb_vcd_yes.setChecked(true);
                rb_vcd_no.setChecked(false);
            }else if (session_vcd.equals("N")){
                rb_vcd_yes.setChecked(false);
                rb_vcd_no.setChecked(true);
            }else {
                rb_vcd_yes.setChecked(false);
                rb_vcd_no.setChecked(false);
            }
        }
        if (session_vce!=null){
            if (session_vce.equals("Y")){
                rb_vce_yes.setChecked(true);
                rb_vce_no.setChecked(false);
            }else if (session_vce.equals("N")){
                rb_vce_yes.setChecked(false);
                rb_vce_no.setChecked(true);
            }else {
                rb_vce_yes.setChecked(false);
                rb_vce_no.setChecked(false);
            }
        }
        if (session_vcf!=null){
            if (session_vcf.equals("Y")){
                rb_vcf_yes.setChecked(true);
                rb_vcf_no.setChecked(false);
            }else if (session_vcf.equals("N")){
                rb_vcf_yes.setChecked(false);
                rb_vcf_no.setChecked(true);
            }else {
                rb_vcf_yes.setChecked(false);
                rb_vcf_no.setChecked(false);
            }
        }
        if (session_vcg!=null){
            if (session_vcg.equals("Y")){
                rb_vcg_yes.setChecked(true);
                rb_vcg_no.setChecked(false);
            }else if (session_vcg.equals("N")){
                rb_vcg_yes.setChecked(false);
                rb_vcg_no.setChecked(true);
            }else {
                rb_vcg_yes.setChecked(false);
                rb_vcg_no.setChecked(false);
            }
        }
        if (session_vch!=null){
            if (session_vch.equals("Y")){
                rb_vch_yes.setChecked(true);
                rb_vch_no.setChecked(false);
            }else if (session_vch.equals("N")){
                rb_vch_yes.setChecked(false);
                rb_vch_no.setChecked(true);
            }else {
                rb_vch_yes.setChecked(false);
                rb_vch_no.setChecked(false);
            }
        }

        if (session_safespeed!=null){
            if (session_safespeed.equals("00")){
                safespeed.setText("");
                safespeed.setHint(getResources().getString(R.string.safespeed));
            }else {
                safespeed.setText(session_safespeed);
            }
        }
        if (session_vehlimit!=null){
            if (session_vehlimit.equals("00")){
                vehlimit.setText("");
                vehlimit.setHint(getResources().getString(R.string.vehlimit));
            }else {
                vehlimit.setText(session_vehlimit);
            }
        }
        if (session_animal1!=null){
            animal1.setText(session_animal1);
        }
        if (session_animal2!=null){
            animal2.setText(session_animal2);
        }
        if (session_animal3!=null){
            animal3.setText(session_animal3);
        }
        if (session_animal4!=null){
            animal4.setText(session_animal4);
        }
        if (session_animal5!=null){
            animal5.setText(session_animal5);
        }
        if (session_animal6!=null){
            animal6.setText(session_animal6);
        }
        if (session_animal7!=null){
            animal7.setText(session_animal7);
        }
        if (session_animal8!=null){
            animal8.setText(session_animal8);
        }
    }

    private void checkValidation(){

        s_apprspeed= apprspeed.getText().toString().trim();
        s_pfspeed = pfspeed.getText().toString().trim();
        s_vehlimit = vehlimit.getText().toString().trim();
        s_safespeed = safespeed.getText().toString().trim();
        s_animal1 = animal1.getText().toString().trim();
        s_animal2 = animal2.getText().toString().trim();
        s_animal3 = animal3.getText().toString().trim();
        s_animal4 = animal4.getText().toString().trim();
        s_animal5 = animal5.getText().toString().trim();
        s_animal6 = animal6.getText().toString().trim();
        s_animal7 = animal7.getText().toString().trim();
        s_animal8 = animal8.getText().toString().trim();

        if (vca_group.getCheckedRadioButtonId() == -1)
        {
            s_vca ="N";
        }
        else
        {
            int comm_selectedId = vca_group.getCheckedRadioButtonId();
            r_vca = (RadioButton) findViewById(comm_selectedId);
            if (r_vca.getText().toString().equals("Yes")){
                s_vca ="Y";
            }else {
                s_vca ="N";
            }
        }

        if (vcb_group.getCheckedRadioButtonId() == -1)
        {
            s_vcb ="N";
        }
        else
        {
            int comm_selectedId = vcb_group.getCheckedRadioButtonId();
            r_vcb = (RadioButton) findViewById(comm_selectedId);
            if (r_vcb.getText().toString().equals("Yes")){
                s_vcb ="Y";
            }else {
                s_vcb ="N";
            }
        }

        if (vcc_group.getCheckedRadioButtonId() == -1)
        {
            s_vcc ="N";
        }
        else
        {
            int comm_selectedId = vcc_group.getCheckedRadioButtonId();
            r_vcc = (RadioButton) findViewById(comm_selectedId);
            if (r_vcc.getText().toString().equals("Yes")){
                s_vcc ="Y";
            }else {
                s_vcc ="N";
            }
        }

        if (vcd_group.getCheckedRadioButtonId() == -1)
        {
            s_vcd ="N";
        }
        else
        {
            int comm_selectedId = vcd_group.getCheckedRadioButtonId();
            r_vcd = (RadioButton) findViewById(comm_selectedId);
            if (r_vcd.getText().toString().equals("Yes")){
                s_vcd ="Y";
            }else {
                s_vcd ="N";
            }
        }

        if (vce_group.getCheckedRadioButtonId() == -1)
        {
            s_vce ="N";
        }
        else
        {
            int comm_selectedId = vce_group.getCheckedRadioButtonId();
            r_vce = (RadioButton) findViewById(comm_selectedId);
            if (r_vce.getText().toString().equals("Yes")){
                s_vce ="Y";
            }else {
                s_vce ="N";
            }
        }

        if (vcf_group.getCheckedRadioButtonId() == -1)
        {
            s_vcf ="N";
        }
        else
        {
            int comm_selectedId = vcf_group.getCheckedRadioButtonId();
            r_vcf = (RadioButton) findViewById(comm_selectedId);
            if (r_vcf.getText().toString().equals("Yes")){
                s_vcf ="Y";
            }else {
                s_vcf ="N";
            }
        }

        if (vcg_group.getCheckedRadioButtonId() == -1)
        {
            s_vcg ="N";
        }
        else
        {
            int comm_selectedId = vcg_group.getCheckedRadioButtonId();
            r_vcg = (RadioButton) findViewById(comm_selectedId);
            if (r_vcg.getText().toString().equals("Yes")){
                s_vcg ="Y";
            }else {
                s_vcg ="N";
            }
        }

        if (vch_group.getCheckedRadioButtonId() == -1)
        {
            s_vch ="N";
        }
        else
        {
            int comm_selectedId = vch_group.getCheckedRadioButtonId();
            r_vch = (RadioButton) findViewById(comm_selectedId);
            if (r_vch.getText().toString().equals("Yes")){
                s_vch ="Y";
            }else {
                s_vch ="N";
            }
        }


        String selected_a = violation_a.getText().toString();
        String selected_b = violation_b.getText().toString();
        String selected_c = violation_c.getText().toString();
        String selected_d = violation_d.getText().toString();
        String selected_e = violation_e.getText().toString();
        String selected_f = violation_f.getText().toString();
        String selected_g = violation_g.getText().toString();
        String selected_h = violation_h.getText().toString();


        DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolation.this);
        databaseAccess.open();


        String a_selected_value = databaseAccess.getSelectedVIOLATION(selected_a);
        String a_selected_id=databaseAccess.getSelectedVIOLATIONid(a_selected_value,selected_a);
        vio_a = a_selected_value;
        vio_a_id = a_selected_id;

        String b_selected_value = databaseAccess.getSelectedVIOLATION(selected_b);
        String b_selected_id=databaseAccess.getSelectedVIOLATIONid(b_selected_value,selected_b);
        vio_b = b_selected_value;
        vio_b_id = b_selected_id;

        String c_selected_value = databaseAccess.getSelectedVIOLATION(selected_c);
        String c_selected_id=databaseAccess.getSelectedVIOLATIONid(c_selected_value,selected_c);
        vio_c = c_selected_value;
        vio_c_id = c_selected_id;

        String d_selected_value = databaseAccess.getSelectedVIOLATION(selected_d);
        String d_selected_id=databaseAccess.getSelectedVIOLATIONid(d_selected_value,selected_d);
        vio_d = d_selected_value;
        vio_d_id = d_selected_id;

        String e_selected_value = databaseAccess.getSelectedVIOLATION(selected_e);
        String e_selected_id=databaseAccess.getSelectedVIOLATIONid(e_selected_value,selected_e);
        vio_e = e_selected_value;
        vio_e_id = e_selected_id;

        String f_selected_value = databaseAccess.getSelectedVIOLATION(selected_f);
        String f_selected_id=databaseAccess.getSelectedVIOLATIONid(f_selected_value,selected_f);
        vio_f = f_selected_value;
        vio_f_id = f_selected_id;

        String g_selected_value = databaseAccess.getSelectedVIOLATION(selected_g);
        String g_selected_id=databaseAccess.getSelectedVIOLATIONid(g_selected_value,selected_g);
        vio_g = g_selected_value;
        vio_g_id = g_selected_id;

        String h_selected_value = databaseAccess.getSelectedVIOLATION(selected_h);
        String h_selected_id=databaseAccess.getSelectedVIOLATIONid(h_selected_value,selected_h);
        vio_h = h_selected_value;
        vio_h_id = h_selected_id;

        databaseAccess.close();



        if (vio_a.equals("")) {
            violation_scroll.fullScroll(View.FOCUS_UP);
            vioa_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
        }else{

            DatabaseAccess databaseAccess1 = new DatabaseAccess(ActivityViolation.this);
            databaseAccess1.open();
            String violation_A = databaseAccess1.getSelectedIsSpeeding(vio_a,vio_a_id);
            String violation_B = databaseAccess1.getSelectedIsSpeeding(vio_b,vio_b_id);
            String violation_C = databaseAccess1.getSelectedIsSpeeding(vio_c,vio_c_id);
            String violation_D = databaseAccess1.getSelectedIsSpeeding(vio_d,vio_d_id);
            String violation_E = databaseAccess1.getSelectedIsSpeeding(vio_e,vio_e_id);
            String violation_F = databaseAccess1.getSelectedIsSpeeding(vio_f,vio_f_id);
            String violation_G = databaseAccess1.getSelectedIsSpeeding(vio_g,vio_g_id);
            String violation_H = databaseAccess1.getSelectedIsSpeeding(vio_h,vio_h_id);
            databaseAccess1.close();

            if (violation_A.equals("Y")||violation_B.equals("Y")||violation_C.equals("Y")||violation_D.equals("Y")||
                    violation_E.equals("Y")||violation_F.equals("Y")||violation_G.equals("Y")||violation_H.equals("Y")){

                if (!s_apprspeed.isEmpty()){

                    if (s_safespeed.isEmpty()&&s_pfspeed.isEmpty()&&s_vehlimit.isEmpty()){
                        Toast.makeText(ActivityViolation.this,"Please select one more speed type",Toast.LENGTH_SHORT).show();
                    }else {

                        if (s_safespeed.isEmpty())
                            s_safespeed = "100";
                        if (s_pfspeed.isEmpty())
                            s_pfspeed = "100";
                        if (s_vehlimit.isEmpty())
                            s_vehlimit = "100";


                        if((parseInt(s_apprspeed)>parseInt(s_safespeed))||(parseInt(s_apprspeed)>parseInt(s_pfspeed))||(parseInt(s_apprspeed)>parseInt(s_vehlimit))){

                            if (s_safespeed.equals("100"))
                                s_safespeed = "00";
                            if (s_pfspeed.equals("100"))
                                s_pfspeed = "00";
                            if (s_vehlimit.equals("100"))
                                s_vehlimit = "00";

                            apprspeed_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);

                            if (s_safespeed.equals("00"))
                                s_safespeed="";
                            if (s_pfspeed.equals("00"))
                                s_pfspeed="";
                            if (s_vehlimit.equals("00"))
                                s_vehlimit="";

                            vioa_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
                            sessionManager.saveViolation("Y");
                            sessionManager.ClearViolationEntery();
                            sessionManager.createViolationSession(vio_a,vio_a_id, vio_b,vio_b_id, vio_c,vio_c_id, vio_d,vio_d_id, vio_e,vio_e_id, vio_f,vio_f_id, vio_g,vio_g_id, vio_h,vio_h_id,s_vca, s_vcb, s_vcc, s_vcd, s_vce, s_vcf, s_vcg, s_vch,s_vehlimit, s_safespeed,s_apprspeed, s_pfspeed, s_animal1, s_animal2, s_animal3, s_animal4, s_animal5, s_animal6, s_animal7, s_animal8);
                            Intent i = new Intent(ActivityViolation.this, ActivityViolationMisc.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.enter,R.anim.exit);

                        }else{
                            apprspeed_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                        }

                    }
                }else {
                    apprspeed_lay.setBackgroundResource(R.drawable.round_corner_red_btn_4dp);
                }
            }else {
                apprspeed_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
                vioa_lay.setBackgroundResource(R.drawable.round_corner_blue_btn_4dp);
                sessionManager.saveViolation("Y");
                sessionManager.ClearViolationEntery();
                sessionManager.createViolationSession(vio_a,vio_a_id, vio_b,vio_b_id, vio_c,vio_c_id, vio_d,vio_d_id, vio_e,vio_e_id, vio_f,vio_f_id, vio_g,vio_g_id, vio_h,vio_h_id,s_vca, s_vcb, s_vcc, s_vcd, s_vce, s_vcf, s_vcg, s_vch,s_vehlimit, s_safespeed,s_apprspeed, s_pfspeed, s_animal1, s_animal2, s_animal3, s_animal4, s_animal5, s_animal6, s_animal7, s_animal8);
                Intent i = new Intent(ActivityViolation.this, ActivityViolationMisc.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter,R.anim.exit);
            }



        }
    }

    @Override
    public void onBackPressed() {

        if (selected.equals("violation")){
            Intent i = new Intent(ActivityViolation.this, ActivityHome.class);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        }


    }


    private void getSessionData(){

        String session_violationA = sessionManager.getViolationSession().get(SessionManager.VIOLATION_A);
        String session_violationAid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_A_ID);

        assert session_violationA != null;
        if (!session_violationA.equals("")){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolation.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationA,session_violationAid);
            databaseAccess.close();

            violation_a.setText(selected_value);
        }


        String session_violationB = sessionManager.getViolationSession().get(SessionManager.VIOLATION_B);
        String session_violationBid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_B_ID);
        assert session_violationB != null;
        if (!session_violationB.equals("")){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolation.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationB,session_violationBid);
            databaseAccess.close();

            violation_b.setText(selected_value);
        }

        String session_violationC = sessionManager.getViolationSession().get(SessionManager.VIOLATION_C);
        String session_violationCid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_C_ID);
        assert session_violationC != null;
        if (!session_violationC.equals("")){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolation.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationC,session_violationCid);
            databaseAccess.close();

            violation_c.setText(selected_value);
        }

        String session_violationD = sessionManager.getViolationSession().get(SessionManager.VIOLATION_D);
        String session_violationDid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_D_ID);
        assert session_violationD != null;
        if (!session_violationD.equals("")){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolation.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationD,session_violationDid);
            databaseAccess.close();

            violation_d.setText(selected_value);
        }

        String session_violationE = sessionManager.getViolationSession().get(SessionManager.VIOLATION_E);
        String session_violationEid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_E_ID);
        assert session_violationE != null;
        if (!session_violationE.equals("")){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolation.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationE,session_violationEid);
            databaseAccess.close();

            violation_e.setText(selected_value);
        }

        String session_violationF = sessionManager.getViolationSession().get(SessionManager.VIOLATION_F);
        String session_violationFid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_F_ID);
        assert session_violationF != null;
        if (!session_violationF.equals("")){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolation.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationF,session_violationFid);
            databaseAccess.close();

            violation_f.setText(selected_value);
        }

        String session_violationG = sessionManager.getViolationSession().get(SessionManager.VIOLATION_G);
        String session_violationGid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_G_ID);

        assert session_violationG != null;
        if (!session_violationG.equals("")){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolation.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationG,session_violationGid);
            databaseAccess.close();

            violation_g.setText(selected_value);
        }


        String session_violationH = sessionManager.getViolationSession().get(SessionManager.VIOLATION_H);
        String session_violationHid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_H_ID);

        assert session_violationH != null;
        if (!session_violationH.equals("")){

            DatabaseAccess databaseAccess = new DatabaseAccess(ActivityViolation.this);
            databaseAccess.open();
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationH,session_violationHid);
            databaseAccess.close();

            violation_h.setText(selected_value);
        }

    }



}