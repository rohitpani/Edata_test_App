package com.edatasolutions.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edatasolutions.R;
import com.edatasolutions.database.DatabaseAccess;
import com.edatasolutions.utils.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityPreview extends AppCompatActivity {

    private TextView header_text;
    private ImageView img_menu, img_back;
    private Button signature_btn;
    private SessionManager sessionManager;
    private TextView pre_or, pre_traffic, pre_nontraffic, pre_ped, pre_courtcode, pre_lea;
    private TextView driver_firstname, driver_middlename, driver_lastname, driver_suffix, driver_address1, driver_address2, driver_city, driver_state, driver_zipcode;
    private TextView driver_license_no, driver_statee, driver_dob, driver_sex, driver_hair, driver_eyes, driver_height, driver_weight, license_type, driver_race, driver_ethncity, comm_drivers_license;
    private TextView pre_owner_firstname, pre_owner_middlename, pre_owner_lastname, pre_owner_suffix, pre_owner_address1, pre_owner_address2, pre_owner_city, pre_owner_state, pre_owner_zipcode;
    private TextView pre_vehyear, pre_vehmake, pre_vehmodel, pre_vehbody, pre_vehcolor, pre_vehtype, pre_commercial, pre_vehno, pre_vehstate, pre_hazardous, pre_overload, pre_policyno;
    private TextView pre_vioA, pre_vioB, pre_vioC, pre_vioD, pre_vioE, pre_vioF, pre_vioG, pre_vioH, pre_vca, pre_vcb, pre_vcc, pre_vcd, pre_vce, pre_vcf, pre_vcg, pre_vch;
    private TextView txt_vioA, txt_vioB, txt_vioC, txt_vioD, txt_vioE, txt_vioF, txt_vioG, txt_vioH, txt_vca, txt_vcb, txt_vcc, txt_vcd, txt_vce, txt_vcf, txt_vcg, txt_vch;
    private TextView pre_vehlimit, pre_safespeed, pre_animal1, pre_animal2, pre_animal3, pre_animal4, pre_animal5, pre_animal6, pre_animal7, pre_animal8;
    private TextView pre_issuedate, pre_time, pre_ampm, pre_apprspeed, pre_pfspeed, pre_schoolzone, pre_violationcity, pre_violationst, pre_violationsttyp, pre_violationcst, pre_violationscttyp;
    private TextView pre_courttime, pre_appeardate, pre_offbadgeno, pre_offlname, pre_areacode, pre_division, pre_detail, pre_nightcourt, pre_ca_tobenotified, pre_ca_citenotsignedbydriver;

    private String CitationNumber, OwnersResponsibility, Traffic, NonTraffic, PED, FirstName, MiddleName, LastName, Suffix, Address, AddressLine2, City, State, ZipCode, DLN, DriversState, DOB, Sex, HairColor, Eyes, Height, Weight, DriversLicenseType, Race, EthnicityCode, OwnerFirstName, OwnerMiddleName, OwnerLastName, OwnerSuffix, OwnerAddress, OwnerAddressLine2, OwnerCity, OwnerState, OwnerZipCode, VehYear, VehMake, VehModel, VehBodyStyle, VehColor, VehicleType, IsCommercial, VLN, VLS, IsHazardous, Overload, CAFinRespProof, ViolCode1, ViolCode2, ViolCode3, ViolCode4, ViolCode5, ViolCode6, ViolCode7, ViolCode8, ViolCode1Correctable, ViolCode2Correctable, ViolCode3Correctable, ViolCode4Correctable, ViolCode5Correctable, ViolCode6Correctable, ViolCode7Correctable, ViolCode8Correctable, IssueDate, Time, AMPM, AppSpeed, MaxSpeed, SchoolZone, ViolCity, ViolStreet, StreetType, ViolCrossStreet, ViolCrossStreetType, AppearDate, AppearanceTime, OfficerBadge, OfficerLastName, Area, Division, Detail, CAToBeNotified, CACiteNotSignedByDriver, CourtCode, LEA, VehLimit, SafeSpeed, Animal1, Animal2, Animal3, Animal4, Animal5, Animal6, Animal7, Animal8, NightCourt, CommDriversLicense, created_at, created_by, updated_by, updated_at;
    private String user_id = "";
    private DatabaseAccess databaseAccess;
    private TextView txt_owner_firstname, txt_owner_middlename, txt_owner_lastname, txt_owner_suffix, txt_owner_address1, txt_owner_address2, txt_owner_city, txt_owner_state, txt_owner_zipcode;
    private View line_owner_firstname, line_owner_middlename, line_owner_lastname, line_owner_suffix, line_owner_address1, line_owner_address2, line_owner_city, line_owner_state, line_owner_zipcode;
    private View line_vioA, line_vioB, line_vioC, line_vioD, line_vioE, line_vioF, line_vioG, line_vioH, line_vca, line_vcb, line_vcc, line_vcd, line_vce, line_vcf, line_vcg, line_vch;
    private TextView txt_animal1, txt_animal2, txt_animal3, txt_animal4, txt_animal5, txt_animal6, txt_animal7, txt_animal8;
    private View line_animal1, line_animal2, line_animal3, line_animal4, line_animal5, line_animal6, line_animal7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        sessionManager = new SessionManager(ActivityPreview.this);

        init();
        setAllData();
        // getAllDatatoString();


        signature_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityPreview.this, ActivitySignature.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void init() {

        user_id = sessionManager.getAdminLogin().get(SessionManager.USER_ID);

        img_back = findViewById(R.id.img_back);
        header_text = findViewById(R.id.header_text);
        img_menu = findViewById(R.id.img_menu);
        signature_btn = findViewById(R.id.signature_btn);
        header_text.setText(R.string.please_confirm);
        img_menu.setVisibility(View.INVISIBLE);

        pre_or = findViewById(R.id.pre_or);
        pre_traffic = findViewById(R.id.pre_traffic);
        pre_nontraffic = findViewById(R.id.pre_nontraffic);
        pre_ped = findViewById(R.id.pre_ped);
        pre_courtcode = findViewById(R.id.pre_courtcode);
        pre_lea = findViewById(R.id.pre_lea);

        driver_firstname = findViewById(R.id.driver_firstname);
        driver_middlename = findViewById(R.id.driver_middlename);
        driver_lastname = findViewById(R.id.driver_lastname);
        driver_suffix = findViewById(R.id.driver_suffix);
        driver_address1 = findViewById(R.id.driver_address1);
        driver_address2 = findViewById(R.id.driver_address2);
        driver_city = findViewById(R.id.driver_city);
        driver_state = findViewById(R.id.driver_state);
        driver_zipcode = findViewById(R.id.driver_zipcode);
        driver_license_no = findViewById(R.id.driver_license_no);
        driver_statee = findViewById(R.id.driver_statee);
        driver_dob = findViewById(R.id.driver_dob);
        driver_sex = findViewById(R.id.driver_sex);
        driver_hair = findViewById(R.id.driver_hair);
        driver_eyes = findViewById(R.id.driver_eyes);
        driver_height = findViewById(R.id.driver_height);
        driver_weight = findViewById(R.id.driver_weight);
        license_type = findViewById(R.id.license_type);
        driver_race = findViewById(R.id.driver_race);
        driver_ethncity = findViewById(R.id.driver_ethncity);
        comm_drivers_license = findViewById(R.id.comm_drivers_license);

        pre_owner_firstname = findViewById(R.id.pre_owner_firstname);
        pre_owner_middlename = findViewById(R.id.pre_owner_middlename);
        pre_owner_lastname = findViewById(R.id.pre_owner_lastname);
        pre_owner_suffix = findViewById(R.id.pre_owner_suffix);
        pre_owner_address1 = findViewById(R.id.pre_owner_address1);
        pre_owner_address2 = findViewById(R.id.pre_owner_address2);
        pre_owner_city = findViewById(R.id.pre_owner_city);
        pre_owner_state = findViewById(R.id.pre_owner_state);
        pre_owner_zipcode = findViewById(R.id.pre_owner_zipcode);
        pre_vehyear = findViewById(R.id.pre_vehyear);
        pre_vehmake = findViewById(R.id.pre_vehmake);
        pre_vehmodel = findViewById(R.id.pre_vehmodel);
        pre_vehbody = findViewById(R.id.pre_vehbody);
        pre_vehcolor = findViewById(R.id.pre_vehcolor);
        pre_vehtype = findViewById(R.id.pre_vehtype);
        pre_commercial = findViewById(R.id.pre_commercial);
        pre_vehno = findViewById(R.id.pre_vehno);
        pre_vehstate = findViewById(R.id.pre_vehstate);
        pre_hazardous = findViewById(R.id.pre_hazardous);
        pre_overload = findViewById(R.id.pre_overload);
        pre_policyno = findViewById(R.id.pre_policyno);

        pre_vioA = findViewById(R.id.pre_vioA);
        pre_vioB = findViewById(R.id.pre_vioB);
        pre_vioC = findViewById(R.id.pre_vioC);
        pre_vioD = findViewById(R.id.pre_vioD);
        pre_vioE = findViewById(R.id.pre_vioE);
        pre_vioF = findViewById(R.id.pre_vioF);
        pre_vioG = findViewById(R.id.pre_vioG);
        pre_vioH = findViewById(R.id.pre_vioH);
        pre_vca = findViewById(R.id.pre_vca);
        pre_vcb = findViewById(R.id.pre_vcb);
        pre_vcc = findViewById(R.id.pre_vcc);
        pre_vcd = findViewById(R.id.pre_vcd);
        pre_vce = findViewById(R.id.pre_vce);
        pre_vcf = findViewById(R.id.pre_vcf);
        pre_vcg = findViewById(R.id.pre_vcg);
        pre_vch = findViewById(R.id.pre_vch);
        pre_vehlimit = findViewById(R.id.pre_vehlimit);
        pre_safespeed = findViewById(R.id.pre_safespeed);
        pre_animal1 = findViewById(R.id.pre_animal1);
        pre_animal2 = findViewById(R.id.pre_animal2);
        pre_animal3 = findViewById(R.id.pre_animal3);
        pre_animal4 = findViewById(R.id.pre_animal4);
        pre_animal5 = findViewById(R.id.pre_animal5);
        pre_animal6 = findViewById(R.id.pre_animal6);
        pre_animal7 = findViewById(R.id.pre_animal7);
        pre_animal8 = findViewById(R.id.pre_animal8);


        pre_issuedate = findViewById(R.id.pre_issuedate);
        pre_time = findViewById(R.id.pre_time);
//        pre_ampm = findViewById(R.id.pre_ampm);
        pre_apprspeed = findViewById(R.id.pre_apprspeed);
        pre_pfspeed = findViewById(R.id.pre_pfspeed);
        pre_schoolzone = findViewById(R.id.pre_schoolzone);
        pre_violationcity = findViewById(R.id.pre_violationcity);
        pre_violationst = findViewById(R.id.pre_violationst);
        pre_violationsttyp = findViewById(R.id.pre_violationsttyp);
        pre_violationcst = findViewById(R.id.pre_violationcst);
        pre_violationscttyp = findViewById(R.id.pre_violationscttyp);
        pre_appeardate = findViewById(R.id.pre_appeardate);
        pre_courttime = findViewById(R.id.pre_courttime);
        pre_offbadgeno = findViewById(R.id.pre_offbadgeno);
        pre_offlname = findViewById(R.id.pre_offlname);
        pre_areacode = findViewById(R.id.pre_areacode);
        pre_division = findViewById(R.id.pre_division);
        pre_detail = findViewById(R.id.pre_detail);
        pre_nightcourt = findViewById(R.id.pre_nightcourt);
        pre_ca_tobenotified = findViewById(R.id.pre_ca_tobenotified);
        pre_ca_citenotsignedbydriver = findViewById(R.id.pre_ca_citenotsignedbydriver);

        txt_owner_firstname = findViewById(R.id.txt_owner_firstname);
        txt_owner_middlename = findViewById(R.id.txt_owner_middlename);
        txt_owner_lastname = findViewById(R.id.txt_owner_lastname);
        txt_owner_suffix = findViewById(R.id.txt_owner_suffix);
        txt_owner_address1 = findViewById(R.id.txt_owner_address1);
        txt_owner_address2 = findViewById(R.id.txt_owner_address2);
        txt_owner_city = findViewById(R.id.txt_owner_city);
        txt_owner_state = findViewById(R.id.txt_owner_state);
        txt_owner_zipcode = findViewById(R.id.txt_owner_zipcode);

        line_owner_firstname = findViewById(R.id.line_owner_firstname);
        line_owner_middlename = findViewById(R.id.line_owner_middlename);
        line_owner_lastname = findViewById(R.id.line_owner_lastname);
        line_owner_suffix = findViewById(R.id.line_owner_suffix);
        line_owner_address1 = findViewById(R.id.line_owner_address1);
        line_owner_address2 = findViewById(R.id.line_owner_address2);
        line_owner_city = findViewById(R.id.line_owner_city);
        line_owner_state = findViewById(R.id.line_owner_state);
        line_owner_zipcode = findViewById(R.id.line_owner_zipcode);

        txt_vioA = findViewById(R.id.txt_vioA);
        txt_vioB = findViewById(R.id.txt_vioB);
        txt_vioC = findViewById(R.id.txt_vioC);
        txt_vioD = findViewById(R.id.txt_vioD);
        txt_vioE = findViewById(R.id.txt_vioE);
        txt_vioF = findViewById(R.id.txt_vioF);
        txt_vioG = findViewById(R.id.txt_vioG);
        txt_vioH = findViewById(R.id.txt_vioH);

        txt_vca = findViewById(R.id.txt_vca);
        txt_vcb = findViewById(R.id.txt_vcb);
        txt_vcc = findViewById(R.id.txt_vcc);
        txt_vcd = findViewById(R.id.txt_vcd);
        txt_vce = findViewById(R.id.txt_vce);
        txt_vcf = findViewById(R.id.txt_vcf);
        txt_vcg = findViewById(R.id.txt_vcg);
        txt_vch = findViewById(R.id.txt_vch);

        line_vioA = findViewById(R.id.line_vioA);
        line_vioB = findViewById(R.id.line_vioB);
        line_vioC = findViewById(R.id.line_vioC);
        line_vioD = findViewById(R.id.line_vioD);
        line_vioE = findViewById(R.id.line_vioE);
        line_vioF = findViewById(R.id.line_vioF);
        line_vioG = findViewById(R.id.line_vioG);
        line_vioH = findViewById(R.id.line_vioH);

        line_vca = findViewById(R.id.line_vca);
        line_vcb = findViewById(R.id.line_vcb);
        line_vcc = findViewById(R.id.line_vcc);
        line_vcd = findViewById(R.id.line_vcd);
        line_vce = findViewById(R.id.line_vce);
        line_vcf = findViewById(R.id.line_vcf);
        line_vcg = findViewById(R.id.line_vcg);
        line_vch = findViewById(R.id.line_vch);

        txt_animal1 = findViewById(R.id.txt_animal1);
        txt_animal2 = findViewById(R.id.txt_animal2);
        txt_animal3 = findViewById(R.id.txt_animal3);
        txt_animal4 = findViewById(R.id.txt_animal4);
        txt_animal5 = findViewById(R.id.txt_animal5);
        txt_animal6 = findViewById(R.id.txt_animal6);
        txt_animal7 = findViewById(R.id.txt_animal7);
        txt_animal8 = findViewById(R.id.txt_animal8);

        line_animal1 = findViewById(R.id.line_animal1);
        line_animal2 = findViewById(R.id.line_animal2);
        line_animal3 = findViewById(R.id.line_animal3);
        line_animal4 = findViewById(R.id.line_animal4);
        line_animal5 = findViewById(R.id.line_animal5);
        line_animal6 = findViewById(R.id.line_animal6);
        line_animal7 = findViewById(R.id.line_animal7);
    }

    private void setAllData() {


        //VIOLATION MISC

        String session_pre_appeardate = sessionManager.getViolationMiscSession().get(SessionManager.APPEAR_DATE);
        String session_pre_courttime = sessionManager.getViolationMiscSession().get(SessionManager.COURT_TIME);
        String session_pre_offbadgeno = sessionManager.getViolationMiscSession().get(SessionManager.OFFBADGENO);
        String session_pre_offlname = sessionManager.getViolationMiscSession().get(SessionManager.OFFLNAME);
        String session_pre_division = sessionManager.getViolationMiscSession().get(SessionManager.DIVISION);
        String session_pre_detail = sessionManager.getViolationMiscSession().get(SessionManager.DETAIL);
        String session_pre_issuedate = sessionManager.getViolationMiscSession().get(SessionManager.ISSUE_DATE);
        String session_pre_time = sessionManager.getViolationMiscSession().get(SessionManager.TIME);
        String session_pre_violationst = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONST);
        String session_pre_violationcst = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCST);
        String session_pre_violationscttyp = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCSTTYP);

        assert session_pre_issuedate != null;
        if (!session_pre_issuedate.equals("")) {
            pre_issuedate.setText(session_pre_issuedate);
        } else {
            pre_issuedate.setText(getResources().getString(R.string.dash));
        }


        assert session_pre_time != null;
        if (!session_pre_time.equals("")) {
            pre_time.setText(session_pre_time);
        } else {
            pre_time.setText(getResources().getString(R.string.dash));
        }


        assert session_pre_violationst != null;
        if (!session_pre_violationst.equals("")) {
            pre_violationst.setText(session_pre_violationst);
        } else {
            pre_violationst.setText(getResources().getString(R.string.dash));
        }


        assert session_pre_violationcst != null;
        if (!session_pre_violationcst.equals("")) {
            pre_violationcst.setText(session_pre_violationcst);
        } else {
            pre_violationcst.setText(getResources().getString(R.string.dash));
        }


        assert session_pre_appeardate != null;
        if (!session_pre_appeardate.equals("")) {
            pre_appeardate.setText(session_pre_appeardate);
        } else {
            pre_appeardate.setText(getResources().getString(R.string.dash));
        }


        assert session_pre_courttime != null;
        if (!session_pre_courttime.equals("")) {
            pre_courttime.setText(session_pre_courttime);
        } else {
            pre_courttime.setText(getResources().getString(R.string.dash));
        }


        assert session_pre_offbadgeno != null;
        if (!session_pre_offbadgeno.equals("")) {
            pre_offbadgeno.setText(session_pre_offbadgeno);
        } else {
            pre_offbadgeno.setText(getResources().getString(R.string.dash));
        }


        assert session_pre_offlname != null;
        if (!session_pre_offlname.equals("")) {
            pre_offlname.setText(session_pre_offlname);
        } else {
            pre_offlname.setText(getResources().getString(R.string.dash));
        }


        assert session_pre_division != null;
        if (!session_pre_division.equals("")) {
            pre_division.setText(session_pre_division);
        } else {
            pre_division.setText(getResources().getString(R.string.dash));
        }

        assert session_pre_detail != null;
        if (!session_pre_detail.equals("")) {
            pre_detail.setText(session_pre_detail);
        } else {
            pre_detail.setText(getResources().getString(R.string.dash));
        }

        //DRIVER


        String session_driver_firstname = sessionManager.getDriverSession().get(SessionManager.DRIVER_FIRST_NAME);
        String session_driver_middlename = sessionManager.getDriverSession().get(SessionManager.DRIVER_MIDDLE_NAME);
        String session_driver_lastname = sessionManager.getDriverSession().get(SessionManager.DRIVER_LAST_NAME);
        String session_driver_suffix = sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX);
        String session_driver_address1 = sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS1);
        String session_driver_address2 = sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS2);
        String session_driver_zipcode = sessionManager.getDriverSession().get(SessionManager.DRIVER_ZIPCODE);
        String session_driver_license_no = sessionManager.getDriverSession().get(SessionManager.DRIVER_LICENSE_NO);
        String session_driver_dob = sessionManager.getDriverSession().get(SessionManager.DOB);
        String session_driver_height = sessionManager.getDriverSession().get(SessionManager.HEIGHT);
        String session_driver_weight = sessionManager.getDriverSession().get(SessionManager.WEIGHT);


        assert session_driver_firstname != null;
        if (!session_driver_firstname.equals("")) {
            driver_firstname.setText(session_driver_firstname);
        } else {
            driver_firstname.setText(getResources().getString(R.string.dash));
        }


        assert session_driver_middlename != null;
        if (!session_driver_middlename.equals("")) {
            driver_middlename.setText(session_driver_middlename);
        } else {
            driver_middlename.setText(getResources().getString(R.string.dash));
        }


        assert session_driver_lastname != null;
        if (!session_driver_lastname.equals("")) {
            driver_lastname.setText(session_driver_lastname);
        } else {
            driver_lastname.setText(getResources().getString(R.string.dash));
        }


        assert session_driver_suffix != null;
        if (!session_driver_suffix.equals("")) {
            driver_suffix.setText(session_driver_suffix);
        } else {
            driver_suffix.setText(getResources().getString(R.string.dash));
        }


        assert session_driver_address1 != null;
        if (!session_driver_address1.equals("")) {
            driver_address1.setText(session_driver_address1);
        } else {
            driver_address1.setText(getResources().getString(R.string.dash));
        }


        assert session_driver_address2 != null;
        if (!session_driver_address2.equals("")) {
            driver_address2.setText(session_driver_address2);
        } else {
            driver_address2.setText(getResources().getString(R.string.dash));
        }


        assert session_driver_zipcode != null;
        if (!session_driver_zipcode.equals("")) {
            driver_zipcode.setText(session_driver_zipcode);
        } else {
            driver_zipcode.setText(getResources().getString(R.string.dash));
        }


        assert session_driver_license_no != null;
        if (!session_driver_license_no.equals("")) {
            driver_license_no.setText(session_driver_license_no);
        } else {
            driver_license_no.setText(getResources().getString(R.string.dash));
        }

        assert session_driver_dob != null;
        if (!session_driver_dob.equals("")) {
            driver_dob.setText(session_driver_dob);
        } else {
            driver_dob.setText(getResources().getString(R.string.dash));
        }

        assert session_driver_weight != null;
        if (!session_driver_weight.equals("")) {
            driver_weight.setText(session_driver_weight);
        } else {
            driver_weight.setText(getResources().getString(R.string.dash));
        }


        //VIOLATION
//        pre_vioA.setText(sessionManager.getViolationSession().get(SessionManager.VIOLATION_A));
//        pre_vioB.setText(sessionManager.getViolationSession().get(SessionManager.VIOLATION_B));
//        pre_vioC.setText(sessionManager.getViolationSession().get(SessionManager.VIOLATION_C));
//        pre_vioD.setText(sessionManager.getViolationSession().get(SessionManager.VIOLATION_D));
//        pre_vioE.setText(sessionManager.getViolationSession().get(SessionManager.VIOLATION_E));
//        pre_vioF.setText(sessionManager.getViolationSession().get(SessionManager.VIOLATION_F));
//        pre_vioG.setText(sessionManager.getViolationSession().get(SessionManager.VIOLATION_G));
//        pre_vioH.setText(sessionManager.getViolationSession().get(SessionManager.VIOLATION_H));


        String session_vehlimit = sessionManager.getViolationSession().get(SessionManager.VEHLIMIT);
        String session_safespeed = sessionManager.getViolationSession().get(SessionManager.SAFE_SPEED);
        String session_apprspeed = sessionManager.getViolationSession().get(SessionManager.APPRSPEED);
        String session_pfspeed = sessionManager.getViolationSession().get(SessionManager.PFSPEED);
        String session_animal1 = sessionManager.getViolationSession().get(SessionManager.ANIMAL1);
        String session_animal2 = sessionManager.getViolationSession().get(SessionManager.ANIMAL2);
        String session_animal3 = sessionManager.getViolationSession().get(SessionManager.ANIMAL3);
        String session_animal4 = sessionManager.getViolationSession().get(SessionManager.ANIMAL4);
        String session_animal5 = sessionManager.getViolationSession().get(SessionManager.ANIMAL5);
        String session_animal6 = sessionManager.getViolationSession().get(SessionManager.ANIMAL6);
        String session_animal7 = sessionManager.getViolationSession().get(SessionManager.ANIMAL7);
        String session_animal8 = sessionManager.getViolationSession().get(SessionManager.ANIMAL8);

        assert session_vehlimit != null;
        if (!session_vehlimit.equals("")) {
            pre_vehlimit.setText(session_vehlimit);
        } else {
            pre_vehlimit.setText(getResources().getString(R.string.dash));
        }


        assert session_safespeed != null;
        if (!session_safespeed.equals("")) {
            pre_safespeed.setText(session_safespeed);
        } else {
            pre_safespeed.setText(getResources().getString(R.string.dash));
        }


        assert session_apprspeed != null;
        if (!session_apprspeed.equals("")) {
            pre_apprspeed.setText(session_apprspeed);
        } else {
            pre_apprspeed.setText(getResources().getString(R.string.dash));
        }


        assert session_pfspeed != null;
        if (!session_pfspeed.equals("")) {
            pre_pfspeed.setText(session_pfspeed);
        } else {
            pre_pfspeed.setText(getResources().getString(R.string.dash));
        }


        assert session_animal1 != null;
        if (!session_animal1.equals("")) {
            pre_animal1.setVisibility(View.VISIBLE);
            txt_animal1.setVisibility(View.VISIBLE);
            line_animal1.setVisibility(View.VISIBLE);
            pre_animal1.setText(session_animal1);
        } else {
            pre_animal1.setText(getResources().getString(R.string.dash));
            pre_animal1.setVisibility(View.GONE);
            txt_animal1.setVisibility(View.GONE);
            line_animal1.setVisibility(View.GONE);
        }


        assert session_animal2 != null;
        if (!session_animal2.equals("")) {
            pre_animal2.setVisibility(View.VISIBLE);
            txt_animal2.setVisibility(View.VISIBLE);
            line_animal2.setVisibility(View.VISIBLE);
            pre_animal2.setText(session_animal2);
        } else {
            pre_animal2.setText(getResources().getString(R.string.dash));
            pre_animal2.setVisibility(View.GONE);
            txt_animal2.setVisibility(View.GONE);
            line_animal2.setVisibility(View.GONE);
        }

        assert session_animal3 != null;
        if (!session_animal3.equals("")) {
            pre_animal3.setVisibility(View.VISIBLE);
            txt_animal3.setVisibility(View.VISIBLE);
            line_animal3.setVisibility(View.VISIBLE);
            pre_animal3.setText(session_animal3);
        } else {
            pre_animal3.setVisibility(View.GONE);
            txt_animal3.setVisibility(View.GONE);
            line_animal3.setVisibility(View.GONE);
            pre_animal3.setText(getResources().getString(R.string.dash));
        }


        assert session_animal4 != null;
        if (!session_animal4.equals("")) {
            pre_animal4.setVisibility(View.VISIBLE);
            txt_animal4.setVisibility(View.VISIBLE);
            line_animal4.setVisibility(View.VISIBLE);
            pre_animal4.setText(session_animal4);
        } else {
            pre_animal4.setText(getResources().getString(R.string.dash));
            pre_animal4.setVisibility(View.GONE);
            txt_animal4.setVisibility(View.GONE);
            line_animal4.setVisibility(View.GONE);
        }


        assert session_animal5 != null;
        if (!session_animal5.equals("")) {
            pre_animal5.setVisibility(View.VISIBLE);
            txt_animal5.setVisibility(View.VISIBLE);
            line_animal5.setVisibility(View.VISIBLE);
            pre_animal5.setText(session_animal5);
        } else {
            pre_animal5.setText(getResources().getString(R.string.dash));
            pre_animal5.setVisibility(View.GONE);
            txt_animal5.setVisibility(View.GONE);
            line_animal5.setVisibility(View.GONE);
        }


        assert session_animal6 != null;
        if (!session_animal6.equals("")) {
            pre_animal6.setVisibility(View.VISIBLE);
            txt_animal6.setVisibility(View.VISIBLE);
            line_animal6.setVisibility(View.VISIBLE);
            pre_animal6.setText(session_animal6);
        } else {
            pre_animal6.setText(getResources().getString(R.string.dash));
            pre_animal6.setVisibility(View.GONE);
            txt_animal6.setVisibility(View.GONE);
            line_animal6.setVisibility(View.GONE);
        }

        assert session_animal7 != null;
        if (!session_animal7.equals("")) {
            pre_animal7.setVisibility(View.VISIBLE);
            txt_animal7.setVisibility(View.VISIBLE);
            line_animal7.setVisibility(View.VISIBLE);
            pre_animal7.setText(session_animal7);
        } else {
            pre_animal7.setText(getResources().getString(R.string.dash));
            pre_animal7.setVisibility(View.GONE);
            txt_animal7.setVisibility(View.GONE);
            line_animal7.setVisibility(View.GONE);
        }

        assert session_animal8 != null;
        if (!session_animal8.equals("")) {
            pre_animal8.setVisibility(View.VISIBLE);
            txt_animal8.setVisibility(View.VISIBLE);
            pre_animal8.setText(session_animal8);
        } else {
            pre_animal8.setText(getResources().getString(R.string.dash));
            pre_animal8.setVisibility(View.GONE);
            txt_animal8.setVisibility(View.GONE);
        }


        databaseAccess = new DatabaseAccess(ActivityPreview.this);
        databaseAccess.open();

        String session_violationsttypid = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONSTTYP_ID);
        String session_violationcsttypid = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCSTTYP_ID);


        String session_ethncity = sessionManager.getDriverSession().get(SessionManager.ETHNCITY);
        String session_ethncityid = sessionManager.getDriverSession().get(SessionManager.ETHNCITY_ID);
        String session_race = sessionManager.getDriverSession().get(SessionManager.RACE);
        String session_raceid = sessionManager.getDriverSession().get(SessionManager.RACE_ID);
        String session_state = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE);
        String session_stateid = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE_ID);
        String session_city = sessionManager.getDriverSession().get(SessionManager.DRIVER_CITY);
        String session_cityid = sessionManager.getDriverSession().get(SessionManager.DRIVER_CITY_ID);
        String session_statee = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATEE);
        String session_stateeid = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATEE_ID);
        String session_eyes = sessionManager.getDriverSession().get(SessionManager.EYES);
        String session_eyesid = sessionManager.getDriverSession().get(SessionManager.EYES_ID);
        String session_hair = sessionManager.getDriverSession().get(SessionManager.HAIR);
        String session_hairid = sessionManager.getDriverSession().get(SessionManager.HAIR_ID);
        String session_licensetype = sessionManager.getDriverSession().get(SessionManager.LICENSE_TYPE);
        String session_licensetypeid = sessionManager.getDriverSession().get(SessionManager.LICENSE_TYPE_ID);
        String session_courtcode = sessionManager.getHeaderSession().get(SessionManager.COURT_CODE);
        String session_courtcodeid = sessionManager.getHeaderSession().get(SessionManager.COURT_CODE_ID);

        String session_lea = sessionManager.getHeaderSession().get(SessionManager.LEA);
        String session_leaid = sessionManager.getHeaderSession().get(SessionManager.LEA_ID);

        String session_ownercity = sessionManager.getVehicleSession().get(SessionManager.OWNER_CITY);
        String session_ownercityid = sessionManager.getVehicleSession().get(SessionManager.OWNER_CITY_ID);
        String session_ownersuffix = sessionManager.getVehicleSession().get(SessionManager.OWNER_SUFFIX);
        String session_ownersuffixid = sessionManager.getVehicleSession().get(SessionManager.OWNER_SUFFIX_ID);
        String session_vehbody = sessionManager.getVehicleSession().get(SessionManager.VEHBODY);
        String session_vehbodyid = sessionManager.getVehicleSession().get(SessionManager.VEHBODY_ID);
        String session_vehcolor = sessionManager.getVehicleSession().get(SessionManager.VEHCOLOR);
        String session_vehcolorid = sessionManager.getVehicleSession().get(SessionManager.VEHCOLOR_ID);
        String session_vehtype = sessionManager.getVehicleSession().get(SessionManager.VEHTYPE);
        String session_vehtypeid = sessionManager.getVehicleSession().get(SessionManager.VEHTYPE_ID);
        String session_vehmake = sessionManager.getVehicleSession().get(SessionManager.VEHMAKE);
        String session_vehmakeid = sessionManager.getVehicleSession().get(SessionManager.VEHMAKE_ID);
        String session_vehmodel = sessionManager.getVehicleSession().get(SessionManager.VEHMODEL);
        String session_vehmodelid = sessionManager.getVehicleSession().get(SessionManager.VEHMODEL_ID);
        String session_ownerstate = sessionManager.getVehicleSession().get(SessionManager.OWNER_STATE);
        String session_ownerstateid = sessionManager.getVehicleSession().get(SessionManager.OWNER_STATE_ID);
        String session_violationA = sessionManager.getViolationSession().get(SessionManager.VIOLATION_A);
        String session_violationB = sessionManager.getViolationSession().get(SessionManager.VIOLATION_B);
        String session_violationC = sessionManager.getViolationSession().get(SessionManager.VIOLATION_C);
        String session_violationD = sessionManager.getViolationSession().get(SessionManager.VIOLATION_D);
        String session_violationE = sessionManager.getViolationSession().get(SessionManager.VIOLATION_E);
        String session_violationF = sessionManager.getViolationSession().get(SessionManager.VIOLATION_F);
        String session_violationG = sessionManager.getViolationSession().get(SessionManager.VIOLATION_G);
        String session_violationH = sessionManager.getViolationSession().get(SessionManager.VIOLATION_H);
        String session_violationAid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_A_ID);
        String session_violationBid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_B_ID);
        String session_violationCid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_C_ID);
        String session_violationDid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_D_ID);
        String session_violationEid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_E_ID);
        String session_violationFid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_F_ID);
        String session_violationGid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_G_ID);
        String session_violationHid = sessionManager.getViolationSession().get(SessionManager.VIOLATION_H_ID);
        String session_violationcity = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCITY);
        String session_violationcityid = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCITY_ID);
        String session_areacode = sessionManager.getViolationMiscSession().get(SessionManager.AREACODE);
        String session_areacodeid = sessionManager.getViolationMiscSession().get(SessionManager.AREACODE_ID);
        String session_height = sessionManager.getDriverSession().get(SessionManager.HEIGHT);

        String session_or = sessionManager.getHeaderSession().get(SessionManager.OWNER_RESPONSIBILITY);
        String session_ped = sessionManager.getHeaderSession().get(SessionManager.PED);

        String session_sex = sessionManager.getDriverSession().get(SessionManager.SEX);
        String session_commdriverlicense = sessionManager.getDriverSession().get(SessionManager.COMM_DRIVER_LICENSE);

        String session_commercial = sessionManager.getVehicleSession().get(SessionManager.COMMERCIAL);
        String session_hazardous = sessionManager.getVehicleSession().get(SessionManager.HAZARDOUS);

        String session_vca = sessionManager.getViolationSession().get(SessionManager.VCA);
        String session_vcb = sessionManager.getViolationSession().get(SessionManager.VCB);
        String session_vcc = sessionManager.getViolationSession().get(SessionManager.VCC);
        String session_vcd = sessionManager.getViolationSession().get(SessionManager.VCD);
        String session_vce = sessionManager.getViolationSession().get(SessionManager.VCE);
        String session_vcf = sessionManager.getViolationSession().get(SessionManager.VCF);
        String session_vcg = sessionManager.getViolationSession().get(SessionManager.VCG);
        String session_vch = sessionManager.getViolationSession().get(SessionManager.VCH);

        String session_schoolzone = sessionManager.getViolationMiscSession().get(SessionManager.SCHOOL_ZONE);
        String session_nightcourt = sessionManager.getViolationMiscSession().get(SessionManager.NIGHT_COURT);
        String session_ca_tobenotified = sessionManager.getViolationMiscSession().get(SessionManager.CA_TOBENOTIFIED);
        String session_ca_citenotsignedbydriver = sessionManager.getViolationMiscSession().get(SessionManager.CA_CITENOTSIGNEDBYDRIVER);

        String session_traffic = sessionManager.getHeaderSession().get(SessionManager.TRAFFIC);
        String session_nontraffic = sessionManager.getHeaderSession().get(SessionManager.NON_TRAFFIC);

        String session_vehiclestate = sessionManager.getVehicleSession().get(SessionManager.VEHSTATE);
        String session_vehiclestateid = sessionManager.getVehicleSession().get(SessionManager.VEHSTATE_ID);

        String session_violationsttyp = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONSTTYP);
        String session_violationcsttyp = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCSTTYP);

        //VEHICLE


//        pre_owner_suffix.setText(sessionManager.getVehicleSession().get(SessionManager.OWNER_SUFFIX));
//        pre_owner_city.setText(sessionManager.getVehicleSession().get(SessionManager.OWNER_CITY));
//        pre_owner_state.setText(sessionManager.getVehicleSession().get(SessionManager.OWNER_STATE));

//        pre_vehmake.setText(sessionManager.getVehicleSession().get(SessionManager.VEHMAKE));
//        pre_vehmodel.setText(sessionManager.getVehicleSession().get(SessionManager.VEHMODEL));
//        pre_vehbody.setText(sessionManager.getVehicleSession().get(SessionManager.VEHBODY));
//        pre_vehcolor.setText(sessionManager.getVehicleSession().get(SessionManager.VEHCOLOR));
//        pre_vehtype.setText(sessionManager.getVehicleSession().get(SessionManager.VEHTYPE));

//        pre_vehstate.setText(sessionManager.getVehicleSession().get(SessionManager.VEHSTATE));


        String session_ownerfirstname = sessionManager.getVehicleSession().get(SessionManager.OWNER_FIRST_NAME);
        String session_ownermiddlename = sessionManager.getVehicleSession().get(SessionManager.OWNER_MIDDLE_NAME);
        String session_ownerlastname = sessionManager.getVehicleSession().get(SessionManager.OWNER_LAST_NAME);
        String session_owneraddress1 = sessionManager.getVehicleSession().get(SessionManager.OWNER_ADDRESS1);
        String session_owneraddress2 = sessionManager.getVehicleSession().get(SessionManager.OWNER_ADDRESS2);
        String session_zipcode = sessionManager.getVehicleSession().get(SessionManager.OWNER_ZIPCODE);
        String session_overload = sessionManager.getVehicleSession().get(SessionManager.OVERLOAD);
        String session_policyno = sessionManager.getVehicleSession().get(SessionManager.POLICYNO);
        String session_vehno = sessionManager.getVehicleSession().get(SessionManager.VEHNO);
        String session_vehyear = sessionManager.getVehicleSession().get(SessionManager.VEHYEAR);


        assert session_or != null;
        if (session_or.equals("Y")) {
            pre_owner_firstname.setVisibility(View.VISIBLE);
            pre_owner_middlename.setVisibility(View.VISIBLE);
            pre_owner_lastname.setVisibility(View.VISIBLE);
            pre_owner_address1.setVisibility(View.VISIBLE);
            pre_owner_address2.setVisibility(View.VISIBLE);
            pre_owner_zipcode.setVisibility(View.VISIBLE);
            pre_owner_suffix.setVisibility(View.VISIBLE);
            pre_owner_city.setVisibility(View.VISIBLE);
            pre_owner_state.setVisibility(View.VISIBLE);

            txt_owner_firstname.setVisibility(View.VISIBLE);
            txt_owner_middlename.setVisibility(View.VISIBLE);
            txt_owner_lastname.setVisibility(View.VISIBLE);
            txt_owner_address1.setVisibility(View.VISIBLE);
            txt_owner_address2.setVisibility(View.VISIBLE);
            txt_owner_zipcode.setVisibility(View.VISIBLE);
            txt_owner_suffix.setVisibility(View.VISIBLE);
            txt_owner_city.setVisibility(View.VISIBLE);
            txt_owner_state.setVisibility(View.VISIBLE);

            line_owner_firstname.setVisibility(View.VISIBLE);
            line_owner_middlename.setVisibility(View.VISIBLE);
            line_owner_lastname.setVisibility(View.VISIBLE);
            line_owner_address1.setVisibility(View.VISIBLE);
            line_owner_address2.setVisibility(View.VISIBLE);
            line_owner_zipcode.setVisibility(View.VISIBLE);
            line_owner_suffix.setVisibility(View.VISIBLE);
            line_owner_city.setVisibility(View.VISIBLE);
            line_owner_state.setVisibility(View.VISIBLE);

            assert session_ownerfirstname != null;
            if (!session_ownerfirstname.equals("")) {
                pre_owner_firstname.setText(session_ownerfirstname);
            } else {
                pre_owner_firstname.setText(getResources().getString(R.string.dash));
            }

            assert session_ownermiddlename != null;
            if (!session_ownermiddlename.equals("")) {
                pre_owner_middlename.setText(session_ownermiddlename);
            } else {
                pre_owner_middlename.setText(getResources().getString(R.string.dash));
            }

            assert session_ownerlastname != null;
            if (!session_ownerlastname.equals("")) {
                pre_owner_lastname.setText(session_ownerlastname);
            } else {
                pre_owner_lastname.setText(getResources().getString(R.string.dash));
            }

            assert session_owneraddress1 != null;
            if (!session_owneraddress1.equals("")) {
                pre_owner_address1.setText(session_owneraddress1);
            } else {
                pre_owner_address1.setText(getResources().getString(R.string.dash));
            }


            assert session_owneraddress2 != null;
            if (!session_owneraddress2.equals("")) {
                pre_owner_address2.setText(session_owneraddress2);
            } else {
                pre_owner_address2.setText(getResources().getString(R.string.dash));
            }


            assert session_zipcode != null;
            if (!session_zipcode.equals("")) {
                pre_owner_zipcode.setText(session_zipcode);
            } else {
                pre_owner_zipcode.setText(getResources().getString(R.string.dash));
            }


            assert session_ownercity != null;
            if (!session_ownercity.equals("")) {
                String selected_value = databaseAccess.getSelectedCityName(session_ownercity, session_ownercityid);
                pre_owner_city.setText(selected_value);
            } else {
                pre_owner_city.setText(getResources().getString(R.string.dash));
            }

            assert session_ownersuffix != null;
            if (!session_ownersuffix.equals("")) {
                String selected_value = databaseAccess.getSuffixName(session_ownersuffixid);
                pre_owner_suffix.setText(selected_value);
            } else {
                pre_owner_suffix.setText(getResources().getString(R.string.dash));
            }

            assert session_ownerstate != null;
            if (!session_ownerstate.equals("")) {
                String selected_value = databaseAccess.getSelectedStateName(session_ownerstate, session_ownerstateid);
                pre_owner_state.setText(selected_value);
            } else {
                pre_owner_state.setText(getResources().getString(R.string.dash));
            }

        } else {
            pre_owner_firstname.setText(getResources().getString(R.string.dash));
            pre_owner_middlename.setText(getResources().getString(R.string.dash));
            pre_owner_lastname.setText(getResources().getString(R.string.dash));
            pre_owner_address1.setText(getResources().getString(R.string.dash));
            pre_owner_address2.setText(getResources().getString(R.string.dash));
            pre_owner_zipcode.setText(getResources().getString(R.string.dash));
            pre_owner_suffix.setText(getResources().getString(R.string.dash));
            pre_owner_city.setText(getResources().getString(R.string.dash));
            pre_owner_state.setText(getResources().getString(R.string.dash));
            pre_owner_firstname.setVisibility(View.GONE);
            pre_owner_middlename.setVisibility(View.GONE);
            pre_owner_lastname.setVisibility(View.GONE);
            pre_owner_address1.setVisibility(View.GONE);
            pre_owner_address2.setVisibility(View.GONE);
            pre_owner_zipcode.setVisibility(View.GONE);
            pre_owner_suffix.setVisibility(View.GONE);
            pre_owner_city.setVisibility(View.GONE);
            pre_owner_state.setVisibility(View.GONE);


            txt_owner_firstname.setVisibility(View.GONE);
            txt_owner_middlename.setVisibility(View.GONE);
            txt_owner_lastname.setVisibility(View.GONE);
            txt_owner_address1.setVisibility(View.GONE);
            txt_owner_address2.setVisibility(View.GONE);
            txt_owner_zipcode.setVisibility(View.GONE);
            txt_owner_suffix.setVisibility(View.GONE);
            txt_owner_city.setVisibility(View.GONE);
            txt_owner_state.setVisibility(View.GONE);

            line_owner_firstname.setVisibility(View.GONE);
            line_owner_middlename.setVisibility(View.GONE);
            line_owner_lastname.setVisibility(View.GONE);
            line_owner_address1.setVisibility(View.GONE);
            line_owner_address2.setVisibility(View.GONE);
            line_owner_zipcode.setVisibility(View.GONE);
            line_owner_suffix.setVisibility(View.GONE);
            line_owner_city.setVisibility(View.GONE);
            line_owner_state.setVisibility(View.GONE);
        }


        assert session_overload != null;
        if (!session_overload.equals("")) {
            pre_overload.setText(session_overload);
        } else {
            pre_overload.setText(getResources().getString(R.string.dash));
        }


        assert session_policyno != null;
        if (!session_policyno.equals("")) {
            pre_policyno.setText(session_policyno);
        } else {
            pre_policyno.setText(getResources().getString(R.string.dash));
        }

        assert session_vehyear != null;
        if (!session_vehyear.equals("")) {
            pre_vehyear.setText(session_vehyear);
        } else {
            pre_vehyear.setText(getResources().getString(R.string.dash));
        }


        assert session_vehno != null;
        if (!session_vehno.equals("")) {
            pre_vehno.setText(session_vehno);
        } else {
            pre_vehno.setText(getResources().getString(R.string.dash));
        }
        assert session_violationcsttyp != null;
        if (!session_violationcsttyp.equals("")) {
            String selected_value = databaseAccess.getSelectedViolationcsttypValue(session_violationcsttypid);
            pre_violationscttyp.setText(selected_value);
        } else {
            pre_violationscttyp.setText(getResources().getString(R.string.dash));
        }

        assert session_violationsttyp != null;
        if (!session_violationsttyp.equals("")) {
            String selected_value = databaseAccess.getSelectedViolationsttypValue(session_violationsttypid);
            pre_violationsttyp.setText(selected_value);
        } else {
            pre_violationsttyp.setText(getResources().getString(R.string.yes));
        }

        assert session_traffic != null;
        if (session_traffic.equals("1")) {
            pre_traffic.setText(getResources().getString(R.string.yes));
        } else {
            assert session_nontraffic != null;
            if (session_nontraffic.equals("1")) {
                pre_traffic.setText(getResources().getString(R.string.no));
            } else {
                pre_traffic.setText(getResources().getString(R.string.dash));
            }
        }


        if (session_ca_citenotsignedbydriver != null) {
            if (session_ca_citenotsignedbydriver.equals("Y")) {
                pre_ca_citenotsignedbydriver.setText(getResources().getString(R.string.yes));
            } else {
                pre_ca_citenotsignedbydriver.setText(getResources().getString(R.string.no));
            }
        }

        if (session_ca_tobenotified != null) {
            if (session_ca_tobenotified.equals("Y")) {
                pre_ca_tobenotified.setText(getResources().getString(R.string.yes));
            } else {
                pre_ca_tobenotified.setText(getResources().getString(R.string.no));
            }
        }

        if (session_schoolzone != null) {
            if (session_schoolzone.equals("Y")) {
                pre_schoolzone.setText(getResources().getString(R.string.yes));
            } else {
                pre_schoolzone.setText(getResources().getString(R.string.no));
            }
        }

        if (session_nightcourt != null) {
            if (session_nightcourt.equals("Y")) {
                pre_nightcourt.setText(getResources().getString(R.string.yes));
            } else {
                pre_nightcourt.setText(getResources().getString(R.string.no));
            }
        }


        if (session_vce != null) {
            if (session_vce.equals("Y")) {
                pre_vce.setVisibility(View.VISIBLE);
                txt_vce.setVisibility(View.VISIBLE);
                line_vce.setVisibility(View.VISIBLE);
                pre_vce.setText(getResources().getString(R.string.yes));
            } else {
                pre_vce.setText(getResources().getString(R.string.no));
                pre_vce.setVisibility(View.GONE);
                txt_vce.setVisibility(View.GONE);
                line_vce.setVisibility(View.GONE);
            }
        }
        if (session_vcf != null) {
            if (session_vcf.equals("Y")) {
                pre_vcf.setVisibility(View.VISIBLE);
                txt_vcf.setVisibility(View.VISIBLE);
                line_vcf.setVisibility(View.VISIBLE);
                pre_vcf.setText(getResources().getString(R.string.yes));
            } else {
                pre_vcf.setText(getResources().getString(R.string.no));
                pre_vcf.setVisibility(View.GONE);
                txt_vcf.setVisibility(View.GONE);
                line_vcf.setVisibility(View.GONE);
            }
        }
        if (session_vcg != null) {
            if (session_vcg.equals("Y")) {
                pre_vcg.setVisibility(View.VISIBLE);
                txt_vcg.setVisibility(View.VISIBLE);
                line_vcg.setVisibility(View.VISIBLE);
                pre_vcg.setText(getResources().getString(R.string.yes));
            } else {
                pre_vcg.setText(getResources().getString(R.string.no));
                pre_vcg.setVisibility(View.GONE);
                txt_vcg.setVisibility(View.GONE);
                line_vcg.setVisibility(View.GONE);
            }
        }
        if (session_vch != null) {
            if (session_vch.equals("Y")) {
                pre_vch.setVisibility(View.VISIBLE);
                txt_vch.setVisibility(View.VISIBLE);
                line_vch.setVisibility(View.VISIBLE);
                pre_vch.setText(getResources().getString(R.string.yes));
            } else {
                pre_vch.setText(getResources().getString(R.string.no));
                pre_vch.setVisibility(View.GONE);
                txt_vch.setVisibility(View.GONE);
                line_vch.setVisibility(View.GONE);
            }
        }


        if (session_vca != null) {
            if (session_vca.equals("Y")) {
                pre_vca.setVisibility(View.VISIBLE);
                txt_vca.setVisibility(View.VISIBLE);
                line_vca.setVisibility(View.VISIBLE);
                pre_vca.setText(getResources().getString(R.string.yes));
            } else {
                pre_vca.setText(getResources().getString(R.string.no));
                pre_vca.setVisibility(View.GONE);
                txt_vca.setVisibility(View.GONE);
                line_vca.setVisibility(View.GONE);
            }
        }
        if (session_vcb != null) {
            if (session_vcb.equals("Y")) {
                pre_vcb.setVisibility(View.VISIBLE);
                txt_vcb.setVisibility(View.VISIBLE);
                line_vcb.setVisibility(View.VISIBLE);
                pre_vcb.setText(getResources().getString(R.string.yes));
            } else {
                pre_vcb.setText(getResources().getString(R.string.no));
                pre_vcb.setVisibility(View.GONE);
                txt_vcb.setVisibility(View.GONE);
                line_vcb.setVisibility(View.GONE);
            }
        }
        if (session_vcc != null) {
            if (session_vcc.equals("Y")) {
                pre_vcc.setVisibility(View.VISIBLE);
                txt_vcc.setVisibility(View.VISIBLE);
                line_vcc.setVisibility(View.VISIBLE);
                pre_vcc.setText(getResources().getString(R.string.yes));
            } else {
                pre_vcc.setText(getResources().getString(R.string.no));
                pre_vcc.setVisibility(View.GONE);
                line_vcc.setVisibility(View.GONE);
                txt_vcc.setVisibility(View.GONE);
            }
        }
        if (session_vcd != null) {
            if (session_vcd.equals("Y")) {
                pre_vcd.setVisibility(View.VISIBLE);
                txt_vcd.setVisibility(View.VISIBLE);
                line_vcd.setVisibility(View.VISIBLE);
                pre_vcd.setText(getResources().getString(R.string.yes));
            } else {
                pre_vcd.setVisibility(View.GONE);
                txt_vcd.setVisibility(View.GONE);
                line_vcd.setVisibility(View.GONE);
                pre_vcd.setText(getResources().getString(R.string.no));
            }
        }

        if (session_hazardous != null) {
            if (session_hazardous.equals("Y")) {
                pre_hazardous.setText(getResources().getString(R.string.yes));
            } else {
                pre_hazardous.setText(getResources().getString(R.string.no));
            }
        }

        if (session_commercial != null) {
            if (session_commercial.equals("Y")) {
                pre_commercial.setText(getResources().getString(R.string.yes));
            } else {
                pre_commercial.setText(getResources().getString(R.string.no));
            }
        }

        if (session_commdriverlicense != null) {
            if (session_commdriverlicense.equals("Y")) {
                comm_drivers_license.setText(getResources().getString(R.string.yes));
            } else {
                comm_drivers_license.setText(getResources().getString(R.string.no));
            }
        }

        if (session_sex != null) {
            if (session_sex.equals("M")) {
                driver_sex.setText(getResources().getString(R.string.male));
            } else {
                driver_sex.setText(getResources().getString(R.string.female));
            }
        }

        if (session_ped != null) {
            if (session_ped.equals("Y")) {
                pre_ped.setText(getResources().getString(R.string.yes));
            } else {
                pre_ped.setText(getResources().getString(R.string.no));
            }
        }

        if (session_or != null) {
            if (session_or.equals("Y")) {
                pre_or.setText(getResources().getString(R.string.yes));
            } else {
                pre_or.setText(getResources().getString(R.string.no));
            }
        }


        assert session_lea != null;
        if (!session_lea.equals("")) {
            String selected_value = databaseAccess.getLeaName(session_leaid);
            pre_lea.setText(selected_value);
        } else {
            pre_lea.setText(getResources().getString(R.string.dash));
        }


        assert session_vehiclestate != null;
        if (!session_vehiclestate.equals("")) {
            String selected_value = databaseAccess.getSelectedStateName(session_vehiclestate, session_vehiclestateid);
            pre_vehstate.setText(selected_value);
        } else {
            pre_vehstate.setText(getResources().getString(R.string.dash));
        }

        assert session_city != null;
        if (!session_city.equals("")) {
            String selected_value = databaseAccess.getSelectedCityName(session_city, session_cityid);
            driver_city.setText(selected_value);
        } else {
            driver_city.setText(getResources().getString(R.string.dash));
        }


        assert session_height != null;
        if (!session_height.equals("")) {
            String selected_value = databaseAccess.getSelectedHeight(session_height);
            driver_height.setText(selected_value);
        } else {
            driver_height.setText(getResources().getString(R.string.dash));
        }

        assert session_areacode != null;
        if (!session_areacode.equals("")) {
            String selected_value = databaseAccess.getSelectedAreaCodeValue(session_areacode, session_areacodeid);
            pre_areacode.setText(selected_value);
        } else {
            pre_areacode.setText(getResources().getString(R.string.dash));
        }

        assert session_violationcity != null;
        if (!session_violationcity.equals("")) {
            String selected_value = databaseAccess.getSelectedViolationCityValue(session_violationcity, session_violationcityid);
            pre_violationcity.setText(selected_value);
        } else {
            pre_violationcity.setText(getResources().getString(R.string.dash));
        }

        assert session_violationH != null;
        if (!session_violationH.equals("")) {
            pre_vioH.setVisibility(View.VISIBLE);
            txt_vioH.setVisibility(View.VISIBLE);
            line_vioH.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationH, session_violationHid);
            pre_vioH.setText(selected_value);
        } else {
            pre_vioH.setText(getResources().getString(R.string.dash));
            pre_vioH.setVisibility(View.GONE);
            txt_vioH.setVisibility(View.GONE);
            line_vioH.setVisibility(View.GONE);
        }


        assert session_violationG != null;
        if (!session_violationG.equals("")) {
            pre_vioG.setVisibility(View.VISIBLE);
            txt_vioG.setVisibility(View.VISIBLE);
            line_vioG.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationG, session_violationGid);
            pre_vioG.setText(selected_value);
        } else {
            pre_vioG.setText(getResources().getString(R.string.dash));
            pre_vioG.setVisibility(View.GONE);
            txt_vioG.setVisibility(View.GONE);
            line_vioG.setVisibility(View.GONE);
        }


        assert session_violationF != null;
        if (!session_violationF.equals("")) {
            pre_vioF.setVisibility(View.VISIBLE);
            txt_vioF.setVisibility(View.VISIBLE);
            line_vioF.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationF, session_violationFid);
            pre_vioF.setText(selected_value);
        } else {
            pre_vioF.setText(getResources().getString(R.string.dash));
            pre_vioF.setVisibility(View.GONE);
            txt_vioF.setVisibility(View.GONE);
            line_vioF.setVisibility(View.GONE);
        }


        assert session_violationE != null;
        if (!session_violationE.equals("")) {
            pre_vioE.setVisibility(View.VISIBLE);
            txt_vioE.setVisibility(View.VISIBLE);
            line_vioE.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationE, session_violationEid);
            pre_vioE.setText(selected_value);
        } else {
            pre_vioE.setText(getResources().getString(R.string.dash));
            pre_vioE.setVisibility(View.GONE);
            txt_vioE.setVisibility(View.GONE);
            line_vioE.setVisibility(View.GONE);
        }


        assert session_violationD != null;
        if (!session_violationD.equals("")) {
            pre_vioD.setVisibility(View.VISIBLE);
            txt_vioD.setVisibility(View.VISIBLE);
            line_vioD.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationD, session_violationDid);
            pre_vioD.setText(selected_value);
        } else {
            pre_vioD.setText(getResources().getString(R.string.dash));
            pre_vioD.setVisibility(View.GONE);
            txt_vioD.setVisibility(View.GONE);
            line_vioD.setVisibility(View.GONE);
        }

        assert session_violationC != null;
        if (!session_violationC.equals("")) {
            pre_vioC.setVisibility(View.VISIBLE);
            txt_vioC.setVisibility(View.VISIBLE);
            line_vioC.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationC, session_violationCid);
            pre_vioC.setText(selected_value);
        } else {
            pre_vioC.setText(getResources().getString(R.string.dash));
            pre_vioC.setVisibility(View.GONE);
            txt_vioC.setVisibility(View.GONE);
            line_vioC.setVisibility(View.GONE);
        }

        assert session_violationB != null;
        if (!session_violationB.equals("")) {
            pre_vioB.setVisibility(View.VISIBLE);
            txt_vioB.setVisibility(View.VISIBLE);
            line_vioB.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationB, session_violationBid);
            pre_vioB.setText(selected_value);
        } else {
            pre_vioB.setText(getResources().getString(R.string.dash));
            pre_vioB.setVisibility(View.GONE);
            txt_vioB.setVisibility(View.GONE);
            line_vioB.setVisibility(View.GONE);
        }

        assert session_violationA != null;
        if (!session_violationA.equals("")) {
            pre_vioA.setVisibility(View.VISIBLE);
            txt_vioA.setVisibility(View.VISIBLE);
            line_vioA.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationA, session_violationAid);
            pre_vioA.setText(selected_value);
        } else {
            pre_vioA.setText(getResources().getString(R.string.dash));
            pre_vioA.setVisibility(View.GONE);
            txt_vioA.setVisibility(View.GONE);
            line_vioA.setVisibility(View.GONE);
        }


        assert session_vehmodel != null;
        if (!session_vehmodel.equals("")) {
            String selected_value = databaseAccess.getSelectedVehicleModelName(session_vehmodel, session_vehmodelid);
            pre_vehmodel.setText(selected_value);
        } else {
            pre_vehmodel.setText(getResources().getString(R.string.dash));
        }


        assert session_vehmake != null;
        if (!session_vehmake.equals("")) {
            String selected_value = databaseAccess.getSelectedVehicleMakeValue(session_vehmake, session_vehmakeid);
            pre_vehmake.setText(selected_value);
        } else {
            pre_vehmake.setText(getResources().getString(R.string.dash));
        }


        assert session_vehtype != null;
        if (!session_vehtype.equals("")) {
            String selected_value = databaseAccess.getVehTypeName(session_vehtype, session_vehtypeid);
            pre_vehtype.setText(selected_value);
        } else {
            pre_vehtype.setText(getResources().getString(R.string.dash));
        }


        assert session_vehcolor != null;
        if (!session_vehcolor.equals("")) {
            String selected_value = databaseAccess.getSelectedVehColorName(session_vehcolor, session_vehcolorid);
            pre_vehcolor.setText(selected_value);
        } else {
            pre_vehcolor.setText(getResources().getString(R.string.dash));
        }


        assert session_vehbody != null;
        if (!session_vehbody.equals("")) {
            String selected_value = databaseAccess.getSelectedVehBodyName(session_vehbody, session_vehbodyid);
            pre_vehbody.setText(selected_value);
        } else {
            pre_vehbody.setText(getResources().getString(R.string.dash));
        }


        assert session_courtcode != null;
        if (!session_courtcode.equals("")) {
            String selected_value = databaseAccess.getSelectedCourtCodeName(session_courtcode, session_courtcodeid);
            pre_courtcode.setText(selected_value + " " + session_courtcode);
        } else {
            pre_courtcode.setText(getResources().getString(R.string.dash));
        }

        assert session_licensetype != null;
        if (!session_licensetype.equals("")) {
            String selected_value = databaseAccess.getSelectedDriversLicenseTypeName(session_licensetype, session_licensetypeid);
            license_type.setText(selected_value);
        } else {
            license_type.setText(getResources().getString(R.string.dash));
        }

        assert session_hair != null;
        if (!session_hair.equals("")) {
            String selected_value = databaseAccess.getSelectedHairName(session_hair, session_hairid);
            driver_hair.setText(selected_value);
        } else {
            driver_hair.setText(getResources().getString(R.string.dash));
        }

        assert session_eyes != null;
        if (!session_eyes.equals("")) {
            String selected_value = databaseAccess.getSelectedEyesName(session_eyes, session_eyesid);
            driver_eyes.setText(selected_value);
        } else {
            driver_eyes.setText(getResources().getString(R.string.dash));
        }

        assert session_statee != null;
        if (!session_statee.equals("")) {
            String selected_value = databaseAccess.getSelectedStateName(session_statee, session_stateeid);
            driver_statee.setText(selected_value);
        } else {
            driver_statee.setText(getResources().getString(R.string.dash));
        }

        assert session_state != null;
        if (!session_state.equals("")) {
            String selected_value = databaseAccess.getSelectedStateName(session_state, session_stateid);
            driver_state.setText(selected_value);
        } else {
            driver_state.setText(getResources().getString(R.string.dash));
        }


        assert session_ethncity != null;
        if (!session_ethncity.equals("")) {
            String selected_value = databaseAccess.getSelectedEthnicityName(session_ethncity, session_ethncityid);
            driver_ethncity.setText(selected_value);
        } else {
            driver_ethncity.setText(getResources().getString(R.string.dash));
        }


        assert session_race != null;
        if (!session_race.equals("")) {
            String selected_value = databaseAccess.getSelectedRaceName(session_race, session_raceid);
            driver_race.setText(selected_value);
        } else {
            driver_race.setText(getResources().getString(R.string.dash));
        }

        databaseAccess.close();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

    }


}