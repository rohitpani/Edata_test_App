package com.edatasolutions.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.edatasolutions.activities.ActivitySplashScreen;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Edatasolution";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String USER_EMAIL = "email";


    public static final String KEY_UPDATED = "updated";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, ActivitySplashScreen.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }



    public void clearSession(){

        //HEADER
        pref.edit().remove(CITATION_NUMBER).commit();
        pref.edit().remove(OWNER_RESPONSIBILITY).commit();
        pref.edit().remove(TRAFFIC).commit();
        pref.edit().remove(NON_TRAFFIC).commit();
        pref.edit().remove(PED).commit();
        pref.edit().remove(COURT_CODE_POSITION).commit();
        pref.edit().remove(COURT_CODE).commit();
        pref.edit().remove(COURT_CODE_ID).commit();
        pref.edit().remove(LEA).commit();
        pref.edit().remove(LEA_ID).commit();
        //VEHICLE
        pref.edit().remove(SAME_AS_DRIVER_CHECK).commit();
        pref.edit().remove(OWNER_FIRST_NAME).commit();
        pref.edit().remove(OWNER_MIDDLE_NAME).commit();
        pref.edit().remove(OWNER_LAST_NAME).commit();
        pref.edit().remove(OWNER_SUFFIX).commit();
        pref.edit().remove(OWNER_SUFFIX_ID).commit();
        pref.edit().remove(OWNER_ADDRESS1).commit();
        pref.edit().remove(OWNER_ADDRESS2).commit();
        pref.edit().remove(OWNER_CITY).commit();
        pref.edit().remove(OWNER_CITY_ID).commit();
        pref.edit().remove(OWNER_STATE).commit();
        pref.edit().remove(OWNER_STATE_ID).commit();
        pref.edit().remove(OWNER_ZIPCODE).commit();
        pref.edit().remove(VEHYEAR).commit();
        pref.edit().remove(VEHMAKE).commit();
        pref.edit().remove(VEHMAKE_ID).commit();
        pref.edit().remove(VEHMODEL).commit();
        pref.edit().remove(VEHMODEL_ID).commit();
        pref.edit().remove(VEHBODY).commit();
        pref.edit().remove(VEHBODY_ID).commit();
        pref.edit().remove(VEHCOLOR).commit();
        pref.edit().remove(VEHCOLOR_ID).commit();
        pref.edit().remove(VEHTYPE).commit();
        pref.edit().remove(VEHTYPE_ID).commit();
        pref.edit().remove(COMMERCIAL).commit();
        pref.edit().remove(VEHNO).commit();
        pref.edit().remove(VEHSTATE).commit();
        pref.edit().remove(VEHSTATE_ID).commit();
        pref.edit().remove(HAZARDOUS).commit();
        pref.edit().remove(OVERLOAD).commit();
        pref.edit().remove(POLICYNO).commit();
        //VIOLATION

        pref.edit().remove(VIOLATION_A).commit();
        pref.edit().remove(VIOLATION_A_ID).commit();
        pref.edit().remove(VIOLATION_B).commit();
        pref.edit().remove(VIOLATION_B_ID).commit();
        pref.edit().remove(VIOLATION_C).commit();
        pref.edit().remove(VIOLATION_C_ID).commit();
        pref.edit().remove(VIOLATION_D).commit();
        pref.edit().remove(VIOLATION_D_ID).commit();
        pref.edit().remove(VIOLATION_E).commit();
        pref.edit().remove(VIOLATION_E_ID).commit();
        pref.edit().remove(VIOLATION_F).commit();
        pref.edit().remove(VIOLATION_F_ID).commit();
        pref.edit().remove(VIOLATION_G).commit();
        pref.edit().remove(VIOLATION_G_ID).commit();
        pref.edit().remove(VIOLATION_H).commit();
        pref.edit().remove(VIOLATION_H_ID).commit();
        pref.edit().remove(VCA).commit();
        pref.edit().remove(VCB).commit();
        pref.edit().remove(VCC).commit();
        pref.edit().remove(VCD).commit();
        pref.edit().remove(VCE).commit();
        pref.edit().remove(VCF).commit();
        pref.edit().remove(VCG).commit();
        pref.edit().remove(VCH).commit();
        pref.edit().remove(VEHLIMIT).commit();
        pref.edit().remove(SAFE_SPEED).commit();
        pref.edit().remove(APPRSPEED).commit();
        pref.edit().remove(PFSPEED).commit();
        pref.edit().remove(ANIMAL1).commit();
        pref.edit().remove(ANIMAL2).commit();
        pref.edit().remove(ANIMAL3).commit();
        pref.edit().remove(ANIMAL4).commit();
        pref.edit().remove(ANIMAL5).commit();
        pref.edit().remove(ANIMAL6).commit();
        pref.edit().remove(ANIMAL7).commit();
        pref.edit().remove(ANIMAL8).commit();
        //VIOLATION MISC
        pref.edit().remove(ISSUE_DATE).commit();
        pref.edit().remove(TIME).commit();
        pref.edit().remove(AMPM).commit();
        pref.edit().remove(SCHOOL_ZONE).commit();
        pref.edit().remove(VIOLATIONCITY).commit();
        pref.edit().remove(VIOLATIONCITY_ID).commit();
        pref.edit().remove(VIOLATIONST).commit();
        pref.edit().remove(VIOLATIONSTTYP).commit();
        pref.edit().remove(VIOLATIONSTTYP_ID).commit();
        pref.edit().remove(VIOLATIONCST).commit();
        pref.edit().remove(VIOLATIONCSTTYP).commit();
        pref.edit().remove(VIOLATIONCSTTYP_ID).commit();
        pref.edit().remove(APPEAR_DATE).commit();
        pref.edit().remove(COURT_TIME).commit();
        pref.edit().remove(OFFBADGENO).commit();
        pref.edit().remove(OFFLNAME).commit();
        pref.edit().remove(AREACODE).commit();
        pref.edit().remove(AREACODE_ID).commit();
        pref.edit().remove(DETAIL).commit();
        pref.edit().remove(DIVISION).commit();
        pref.edit().remove(DIVISION_ID).commit();
        pref.edit().remove(NIGHT_COURT).commit();
        pref.edit().remove(CA_TOBENOTIFIED).commit();
        pref.edit().remove(CA_CITENOTSIGNEDBYDRIVER).commit();
       //DRIVER
        pref.edit().remove(DRIVER_FIRST_NAME).commit();
        pref.edit().remove(DRIVER_MIDDLE_NAME).commit();
        pref.edit().remove(DRIVER_LAST_NAME).commit();
        pref.edit().remove(DRIVER_SUFFIX).commit();
        pref.edit().remove(DRIVER_SUFFIX_ID).commit();
        pref.edit().remove(DRIVER_ADDRESS1).commit();
        pref.edit().remove(DRIVER_ADDRESS2).commit();
        pref.edit().remove(DRIVER_CITY).commit();
        pref.edit().remove(DRIVER_CITY_ID).commit();
        pref.edit().remove(DRIVER_STATE).commit();
        pref.edit().remove(DRIVER_STATE_ID).commit();
        pref.edit().remove(DRIVER_ZIPCODE).commit();
        pref.edit().remove(DRIVER_LICENSE_NO).commit();
        pref.edit().remove(DRIVER_STATEE).commit();
        pref.edit().remove(DRIVER_STATEE_ID).commit();
        pref.edit().remove(DOB).commit();
        pref.edit().remove(SEX).commit();
        pref.edit().remove(HAIR).commit();
        pref.edit().remove(HAIR_ID).commit();
        pref.edit().remove(EYES).commit();
        pref.edit().remove(EYES_ID).commit();
        pref.edit().remove(HEIGHT).commit();
        pref.edit().remove(WEIGHT).commit();
        pref.edit().remove(LICENSE_TYPE).commit();
        pref.edit().remove(LICENSE_TYPE_ID).commit();
        pref.edit().remove(RACE).commit();
        pref.edit().remove(RACE_ID).commit();
        pref.edit().remove(ETHNCITY).commit();
        pref.edit().remove(ETHNCITY_ID).commit();
        pref.edit().remove(COMM_DRIVER_LICENSE).commit();


        editor.commit();
    }


    public void isUpdated(String status) {
        editor.putString(KEY_UPDATED, status);
        editor.commit();
    }

    public HashMap<String, String> getStatus() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_UPDATED, pref.getString(KEY_UPDATED, ""));
        return user;
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    // Get Remember State
//    public boolean isRemember() {
//        return pref.getBoolean(IS_REMEMBER, false);
//    }

    public static final String SAME_AS_DRIVER_CHECK = "sameasdriver_check";
    public static final String OWNER_FIRST_NAME = "owner_first_name";
    public static final String OWNER_MIDDLE_NAME = "owner_middle_name";
    public static final String OWNER_LAST_NAME = "owner_last_name";
    public static final String OWNER_SUFFIX = "owner_suffix";
    public static final String OWNER_SUFFIX_ID = "owner_suffix_id";
    public static final String OWNER_ADDRESS1 = "owner_address1";
    public static final String OWNER_ADDRESS2 = "owner_address2";
    public static final String OWNER_CITY = "owner_city";
    public static final String OWNER_CITY_ID = "owner_city_id";
    public static final String OWNER_STATE = "owner_state";
    public static final String OWNER_STATE_ID = "owner_stateid";
    public static final String OWNER_ZIPCODE = "owner_zipcode";
    public static final String VEHYEAR = "vehyear";
    public static final String VEHMAKE = "vehmake";
    public static final String VEHMAKE_ID = "vehmake_id";
    public static final String VEHMODEL = "vehmodel";
    public static final String VEHMODEL_ID = "vehmodelid";
    public static final String VEHBODY = "vehbody";
    public static final String VEHBODY_ID = "vehbody_id";
    public static final String VEHCOLOR = "vehcolor";
    public static final String VEHCOLOR_ID = "vehcolor_id";
    public static final String VEHTYPE = "vehtype";
    public static final String VEHTYPE_ID = "vehtype_id";
    public static final String COMMERCIAL = "commercial";
    public static final String VEHNO = "vehno";
    public static final String VEHSTATE = "vehstate";
    public static final String VEHSTATE_ID = "vehstate_id";
    public static final String HAZARDOUS = "hazardous";
    public static final String OVERLOAD = "overload";
    public static final String POLICYNO = "policyno";



        public void createVehicleSession(String sameasdriver_check, String owner_first_name, String owner_middle_name, String owner_last_name, String owner_suffix, String owner_suffix_id, String owner_address1, String owner_address2, String owner_city,String owner_city_id, String owner_state,String owner_stateid, String owner_zipcode, String vehyear, String vehmake,String vehmake_id, String vehmodel, String vehmodelid, String vehbody,String vehbody_id, String vehcolor,String vehcolor_id, String vehtype,String vehtype_id, String commercial, String vehno, String vehstate,String vehstate_id, String hazardous, String overload, String policyno) {

            editor.putString(SAME_AS_DRIVER_CHECK, sameasdriver_check);
        editor.putString(OWNER_FIRST_NAME, owner_first_name);
        editor.putString(OWNER_MIDDLE_NAME, owner_middle_name);
        editor.putString(OWNER_LAST_NAME, owner_last_name);
        editor.putString(OWNER_SUFFIX, owner_suffix);
        editor.putString(OWNER_SUFFIX_ID, owner_suffix_id);
        editor.putString(OWNER_ADDRESS1, owner_address1);
        editor.putString(OWNER_ADDRESS2, owner_address2);
        editor.putString(OWNER_CITY, owner_city);
        editor.putString(OWNER_CITY_ID, owner_city_id);
        editor.putString(OWNER_STATE, owner_state);
        editor.putString(OWNER_STATE_ID, owner_stateid);
        editor.putString(OWNER_ZIPCODE, owner_zipcode);
        editor.putString(VEHYEAR, vehyear);
        editor.putString(VEHMAKE, vehmake);
        editor.putString(VEHMAKE_ID, vehmake_id);
        editor.putString(VEHMODEL, vehmodel);
        editor.putString(VEHMODEL_ID, vehmodelid);
        editor.putString(VEHBODY, vehbody);
        editor.putString(VEHBODY_ID, vehbody_id);
        editor.putString(VEHCOLOR, vehcolor);
        editor.putString(VEHCOLOR_ID, vehcolor_id);
        editor.putString(VEHTYPE, vehtype);
        editor.putString(VEHTYPE_ID, vehtype_id);
        editor.putString(COMMERCIAL, commercial);
        editor.putString(VEHNO, vehno);
        editor.putString(VEHSTATE, vehstate);
        editor.putString(VEHSTATE_ID, vehstate_id);
        editor.putString(HAZARDOUS, hazardous);
        editor.putString(OVERLOAD, overload);
        editor.putString(POLICYNO, policyno);
        editor.commit();
    }



    public HashMap<String, String> getVehicleSession() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(SAME_AS_DRIVER_CHECK, pref.getString(SAME_AS_DRIVER_CHECK, ""));
        user.put(OWNER_FIRST_NAME, pref.getString(OWNER_FIRST_NAME, ""));
        user.put(OWNER_MIDDLE_NAME, pref.getString(OWNER_MIDDLE_NAME, ""));
        user.put(OWNER_LAST_NAME, pref.getString(OWNER_LAST_NAME, ""));
        user.put(OWNER_SUFFIX, pref.getString(OWNER_SUFFIX, ""));
        user.put(OWNER_SUFFIX_ID, pref.getString(OWNER_SUFFIX_ID, ""));
        user.put(OWNER_ADDRESS1, pref.getString(OWNER_ADDRESS1, ""));
        user.put(OWNER_ADDRESS2, pref.getString(OWNER_ADDRESS2, ""));
        user.put(OWNER_CITY, pref.getString(OWNER_CITY, ""));
        user.put(OWNER_CITY_ID, pref.getString(OWNER_CITY_ID, ""));
        user.put(OWNER_STATE, pref.getString(OWNER_STATE, ""));
        user.put(OWNER_STATE_ID, pref.getString(OWNER_STATE_ID, ""));
        user.put(OWNER_ZIPCODE, pref.getString(OWNER_ZIPCODE, ""));
        user.put(VEHYEAR, pref.getString(VEHYEAR, ""));
        user.put(VEHMAKE, pref.getString(VEHMAKE, ""));
        user.put(VEHMAKE_ID, pref.getString(VEHMAKE_ID, ""));
        user.put(VEHMODEL, pref.getString(VEHMODEL, ""));
        user.put(VEHMODEL_ID, pref.getString(VEHMODEL_ID, ""));
        user.put(VEHBODY, pref.getString(VEHBODY, ""));
        user.put(VEHBODY_ID, pref.getString(VEHBODY_ID, ""));
        user.put(VEHCOLOR, pref.getString(VEHCOLOR, ""));
        user.put(VEHCOLOR_ID, pref.getString(VEHCOLOR_ID, ""));
        user.put(VEHTYPE, pref.getString(VEHTYPE, ""));
        user.put(VEHTYPE_ID, pref.getString(VEHTYPE_ID, ""));
        user.put(COMMERCIAL, pref.getString(COMMERCIAL, ""));
        user.put(VEHNO, pref.getString(VEHNO, ""));
        user.put(VEHSTATE, pref.getString(VEHSTATE, ""));
        user.put(VEHSTATE_ID, pref.getString(VEHSTATE_ID, ""));
        user.put(HAZARDOUS, pref.getString(HAZARDOUS, ""));
        user.put(OVERLOAD, pref.getString(OVERLOAD, ""));
        user.put(POLICYNO, pref.getString(POLICYNO, ""));

        return user;
    }


    public void ClearVehicleEntery() {

        pref.edit().remove(SAME_AS_DRIVER_CHECK).commit();
        pref.edit().remove(OWNER_FIRST_NAME).commit();
        pref.edit().remove(OWNER_MIDDLE_NAME).commit();
        pref.edit().remove(OWNER_LAST_NAME).commit();
        pref.edit().remove(OWNER_SUFFIX).commit();
        pref.edit().remove(OWNER_SUFFIX_ID).commit();
        pref.edit().remove(OWNER_ADDRESS1).commit();
        pref.edit().remove(OWNER_ADDRESS2).commit();
        pref.edit().remove(OWNER_CITY).commit();
        pref.edit().remove(OWNER_CITY_ID).commit();
        pref.edit().remove(OWNER_STATE).commit();
        pref.edit().remove(OWNER_STATE_ID).commit();
        pref.edit().remove(OWNER_ZIPCODE).commit();
        pref.edit().remove(VEHYEAR).commit();
        pref.edit().remove(VEHMAKE).commit();
        pref.edit().remove(VEHMAKE_ID).commit();
        pref.edit().remove(VEHMODEL).commit();
        pref.edit().remove(VEHMODEL_ID).commit();
        pref.edit().remove(VEHBODY).commit();
        pref.edit().remove(VEHBODY_ID).commit();
        pref.edit().remove(VEHCOLOR).commit();
        pref.edit().remove(VEHCOLOR_ID).commit();
        pref.edit().remove(VEHTYPE).commit();
        pref.edit().remove(VEHTYPE_ID).commit();
        pref.edit().remove(COMMERCIAL).commit();
        pref.edit().remove(VEHNO).commit();
        pref.edit().remove(VEHSTATE).commit();
        pref.edit().remove(VEHSTATE_ID).commit();
        pref.edit().remove(HAZARDOUS).commit();
        pref.edit().remove(OVERLOAD).commit();
        pref.edit().remove(POLICYNO).commit();

        editor.commit();
    }

    public void createOwnerVehicleSession(String owner_first_name, String owner_middle_name, String owner_last_name, String owner_suffix, String owner_suffix_id, String owner_address1, String owner_address2, String owner_city,String owner_city_id, String owner_state,String owner_stateid, String owner_zipcode) {

        editor.putString(OWNER_FIRST_NAME, owner_first_name);
        editor.putString(OWNER_MIDDLE_NAME, owner_middle_name);
        editor.putString(OWNER_LAST_NAME, owner_last_name);
        editor.putString(OWNER_SUFFIX, owner_suffix);
        editor.putString(OWNER_SUFFIX_ID, owner_suffix_id);
        editor.putString(OWNER_ADDRESS1, owner_address1);
        editor.putString(OWNER_ADDRESS2, owner_address2);
        editor.putString(OWNER_CITY, owner_city);
        editor.putString(OWNER_CITY_ID, owner_city_id);
        editor.putString(OWNER_STATE, owner_state);
        editor.putString(OWNER_STATE_ID, owner_stateid);
        editor.putString(OWNER_ZIPCODE, owner_zipcode);
        editor.commit();
    }

    public HashMap<String, String> getOwnerVehicleSession() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(OWNER_FIRST_NAME, pref.getString(OWNER_FIRST_NAME, ""));
        user.put(OWNER_MIDDLE_NAME, pref.getString(OWNER_MIDDLE_NAME, ""));
        user.put(OWNER_LAST_NAME, pref.getString(OWNER_LAST_NAME, ""));
        user.put(OWNER_SUFFIX, pref.getString(OWNER_SUFFIX, ""));
        user.put(OWNER_SUFFIX_ID, pref.getString(OWNER_SUFFIX_ID, ""));
        user.put(OWNER_ADDRESS1, pref.getString(OWNER_ADDRESS1, ""));
        user.put(OWNER_ADDRESS2, pref.getString(OWNER_ADDRESS2, ""));
        user.put(OWNER_CITY, pref.getString(OWNER_CITY, ""));
        user.put(OWNER_CITY_ID, pref.getString(OWNER_CITY_ID, ""));
        user.put(OWNER_STATE, pref.getString(OWNER_STATE, ""));
        user.put(OWNER_STATE_ID, pref.getString(OWNER_STATE_ID, ""));
        user.put(OWNER_ZIPCODE, pref.getString(OWNER_ZIPCODE, ""));

        return user;
    }


    public void ClearOwnerVehicleEntery() {

        pref.edit().remove(OWNER_FIRST_NAME).commit();
        pref.edit().remove(OWNER_MIDDLE_NAME).commit();
        pref.edit().remove(OWNER_LAST_NAME).commit();
        pref.edit().remove(OWNER_SUFFIX).commit();
        pref.edit().remove(OWNER_SUFFIX_ID).commit();
        pref.edit().remove(OWNER_ADDRESS1).commit();
        pref.edit().remove(OWNER_ADDRESS2).commit();
        pref.edit().remove(OWNER_CITY).commit();
        pref.edit().remove(OWNER_CITY_ID).commit();
        pref.edit().remove(OWNER_STATE).commit();
        pref.edit().remove(OWNER_STATE_ID).commit();
        pref.edit().remove(OWNER_ZIPCODE).commit();

        editor.commit();
    }

    public static final String VIOLATION_A = "violation_a";
    public static final String VIOLATION_A_ID = "violation_a_id";
    public static final String VIOLATION_B = "violation_b";
    public static final String VIOLATION_B_ID = "violation_b_id";
    public static final String VIOLATION_C = "violation_c";
    public static final String VIOLATION_C_ID = "violation_c_id";
    public static final String VIOLATION_D = "violation_d";
    public static final String VIOLATION_D_ID = "violation_d_id";
    public static final String VIOLATION_E = "violation_e";
    public static final String VIOLATION_E_ID = "violation_e_id";
    public static final String VIOLATION_F = "violation_f";
    public static final String VIOLATION_F_ID = "violation_f_id";
    public static final String VIOLATION_G = "violation_g";
    public static final String VIOLATION_G_ID = "violation_g_id";
    public static final String VIOLATION_H = "violation_h";
    public static final String VIOLATION_H_ID = "violation_h_id";
    public static final String VCA = "vca";
    public static final String VCB = "vcb";
    public static final String VCC = "vcc";
    public static final String VCD = "vcd";
    public static final String VCE = "vce";
    public static final String VCF = "vcf";
    public static final String VCG = "vcg";
    public static final String VCH = "vch";
    public static final String VEHLIMIT = "vehlimit";
    public static final String SAFE_SPEED = "safespeed";
    public static final String ANIMAL1 = "animal1";
    public static final String ANIMAL2 = "animal2";
    public static final String ANIMAL3 = "animal3";
    public static final String ANIMAL4 = "animal4";
    public static final String ANIMAL5 = "animal5";
    public static final String ANIMAL6 = "animal6";
    public static final String ANIMAL7 = "animal7";
    public static final String ANIMAL8 = "animal8";
    public static final String APPRSPEED = "apprspeed";
    public static final String PFSPEED = "pfspeed";

    public void createViolationSession(String violation_a,String violation_a_id, String violation_b,String violation_b_id, String violation_c,String violation_c_id, String violation_d,String violation_d_id, String violation_e,String violation_e_id, String violation_f,String violation_f_id, String violation_g,String violation_g_id, String violation_h,String violation_h_id, String vca, String vcb, String vcc, String vcd, String vce, String vcf, String vcg, String vch, String vehlimit, String safespeed,String apprspeed, String pfspeed, String animal1, String animal2, String animal3, String animal4, String animal5, String animal6, String animal7, String animal8) {

        editor.putString(VIOLATION_A, violation_a);
        editor.putString(VIOLATION_A_ID, violation_a_id);
        editor.putString(VIOLATION_B, violation_b);
        editor.putString(VIOLATION_B_ID, violation_b_id);
        editor.putString(VIOLATION_C, violation_c);
        editor.putString(VIOLATION_C_ID, violation_c_id);
        editor.putString(VIOLATION_D, violation_d);
        editor.putString(VIOLATION_D_ID, violation_d_id);
        editor.putString(VIOLATION_E, violation_e);
        editor.putString(VIOLATION_E_ID, violation_e_id);
        editor.putString(VIOLATION_F, violation_f);
        editor.putString(VIOLATION_F_ID, violation_f_id);
        editor.putString(VIOLATION_G, violation_g);
        editor.putString(VIOLATION_G_ID, violation_g_id);
        editor.putString(VIOLATION_H, violation_h);
        editor.putString(VIOLATION_H_ID, violation_h_id);
        editor.putString(VCA, vca);
        editor.putString(VCB, vcb);
        editor.putString(VCC, vcc);
        editor.putString(VCD, vcd);
        editor.putString(VCE, vce);
        editor.putString(VCF, vcf);
        editor.putString(VCG, vcg);
        editor.putString(VCH, vch);
        editor.putString(VEHLIMIT, vehlimit);
        editor.putString(SAFE_SPEED, safespeed);
        editor.putString(APPRSPEED, apprspeed);
        editor.putString(PFSPEED, pfspeed);
        editor.putString(ANIMAL1, animal1);
        editor.putString(ANIMAL2, animal2);
        editor.putString(ANIMAL3, animal3);
        editor.putString(ANIMAL4, animal4);
        editor.putString(ANIMAL5, animal5);
        editor.putString(ANIMAL6, animal6);
        editor.putString(ANIMAL7, animal7);
        editor.putString(ANIMAL8, animal8);
        editor.commit();
    }

    public HashMap<String, String> getViolationSession() {
        HashMap<String, String> user = new HashMap<String, String>();


        user.put(VIOLATION_A, pref.getString(VIOLATION_A, ""));
        user.put(VIOLATION_A_ID, pref.getString(VIOLATION_A_ID, ""));
        user.put(VIOLATION_B, pref.getString(VIOLATION_B, ""));
        user.put(VIOLATION_B_ID, pref.getString(VIOLATION_B_ID, ""));
        user.put(VIOLATION_C, pref.getString(VIOLATION_C, ""));
        user.put(VIOLATION_C_ID, pref.getString(VIOLATION_C_ID, ""));
        user.put(VIOLATION_D, pref.getString(VIOLATION_D, ""));
        user.put(VIOLATION_D_ID, pref.getString(VIOLATION_D_ID, ""));
        user.put(VIOLATION_E, pref.getString(VIOLATION_E, ""));
        user.put(VIOLATION_E_ID, pref.getString(VIOLATION_E_ID, ""));
        user.put(VIOLATION_F, pref.getString(VIOLATION_F, ""));
        user.put(VIOLATION_F_ID, pref.getString(VIOLATION_F_ID, ""));
        user.put(VIOLATION_G, pref.getString(VIOLATION_G, ""));
        user.put(VIOLATION_G_ID, pref.getString(VIOLATION_G_ID, ""));
        user.put(VIOLATION_H, pref.getString(VIOLATION_H, ""));
        user.put(VIOLATION_H_ID, pref.getString(VIOLATION_H_ID, ""));
        user.put(VCA, pref.getString(VCA, ""));
        user.put(VCB, pref.getString(VCB, ""));
        user.put(VCC, pref.getString(VCC, ""));
        user.put(VCD, pref.getString(VCD, ""));
        user.put(VCE, pref.getString(VCE, ""));
        user.put(VCF, pref.getString(VCF, ""));
        user.put(VCG, pref.getString(VCG, ""));
        user.put(VCH, pref.getString(VCH, ""));
        user.put(VEHLIMIT, pref.getString(VEHLIMIT, ""));
        user.put(SAFE_SPEED, pref.getString(SAFE_SPEED, ""));
        user.put(APPRSPEED, pref.getString(APPRSPEED, ""));
        user.put(PFSPEED, pref.getString(PFSPEED, ""));
        user.put(ANIMAL1, pref.getString(ANIMAL1, ""));
        user.put(ANIMAL2, pref.getString(ANIMAL2, ""));
        user.put(ANIMAL3, pref.getString(ANIMAL3, ""));
        user.put(ANIMAL4, pref.getString(ANIMAL4, ""));
        user.put(ANIMAL5, pref.getString(ANIMAL5, ""));
        user.put(ANIMAL6, pref.getString(ANIMAL6, ""));
        user.put(ANIMAL7, pref.getString(ANIMAL7, ""));
        user.put(ANIMAL8, pref.getString(ANIMAL8, ""));
        return user;
    }
    public void ClearViolationEntery() {

        pref.edit().remove(VIOLATION_A).commit();
        pref.edit().remove(VIOLATION_A_ID).commit();
        pref.edit().remove(VIOLATION_B).commit();
        pref.edit().remove(VIOLATION_B_ID).commit();
        pref.edit().remove(VIOLATION_C).commit();
        pref.edit().remove(VIOLATION_C_ID).commit();
        pref.edit().remove(VIOLATION_D).commit();
        pref.edit().remove(VIOLATION_D_ID).commit();
        pref.edit().remove(VIOLATION_E).commit();
        pref.edit().remove(VIOLATION_E_ID).commit();
        pref.edit().remove(VIOLATION_F).commit();
        pref.edit().remove(VIOLATION_F_ID).commit();
        pref.edit().remove(VIOLATION_G).commit();
        pref.edit().remove(VIOLATION_G_ID).commit();
        pref.edit().remove(VIOLATION_H).commit();
        pref.edit().remove(VIOLATION_H_ID).commit();
        pref.edit().remove(VCA).commit();
        pref.edit().remove(VCB).commit();
        pref.edit().remove(VCC).commit();
        pref.edit().remove(VCD).commit();
        pref.edit().remove(VCE).commit();
        pref.edit().remove(VCF).commit();
        pref.edit().remove(VCG).commit();
        pref.edit().remove(VCH).commit();
        pref.edit().remove(VEHLIMIT).commit();
        pref.edit().remove(SAFE_SPEED).commit();
        pref.edit().remove(APPRSPEED).commit();
        pref.edit().remove(PFSPEED).commit();
        pref.edit().remove(ANIMAL1).commit();
        pref.edit().remove(ANIMAL2).commit();
        pref.edit().remove(ANIMAL3).commit();
        pref.edit().remove(ANIMAL4).commit();
        pref.edit().remove(ANIMAL5).commit();
        pref.edit().remove(ANIMAL6).commit();
        pref.edit().remove(ANIMAL7).commit();
        pref.edit().remove(ANIMAL8).commit();

        editor.commit();
    }



    public static final String ISSUE_DATE = "issuedate";
    public static final String TIME = "time";
    public static final String AMPM = "ampm";
    public static final String OFFBADGENO = "offbadgeno";
    public static final String OFFLNAME = "offlname";
    public static final String AREACODE = "areacode";
    public static final String AREACODE_ID = "areacode_id";
    public static final String DIVISION = "division";
    public static final String DIVISION_ID = "division_id";

    public static final String DIVISION_VALUE = "division_value";
    public static final String DETAIL = "detail";
    public static final String SCHOOL_ZONE = "schoolzone";
    public static final String NIGHT_COURT = "night_court";
    public static final String CA_TOBENOTIFIED = "ca_tobenotified";
    public static final String CA_CITENOTSIGNEDBYDRIVER = "ca_citenotsignedbydriver";
    public static final String VIOLATIONCITY = "violationcity";
    public static final String VIOLATIONCITY_ID = "violationcity_id";
    public static final String VIOLATIONST = "violationst";
    public static final String VIOLATIONSTTYP = "violationsttyp";
    public static final String VIOLATIONSTTYP_ID = "violationtyp_id";
    public static final String VIOLATIONCST = "violationcst";
    public static final String VIOLATIONCSTTYP = "violationcsttyp";
    public static final String VIOLATIONCSTTYP_ID = "violationcsttyp_id";
    public static final String APPEAR_DATE = "appeardate";
    public static final String COURT_TIME = "courttime";

    public void createViolationMiscSession(String issuedate, String time, String ampm,  String schoolzome, String violationcity,String violationcity_id, String violationst,
                                           String violationsttyp,String violationsttyp_id, String violationcst, String violationcsttyp,String violationcsttyp_id, String appeardate,String courttime, String offbadgeno,
                                           String offlname, String areacode,String areacode_id,String division,String divisionvalue,String division_id, String detail, String night_court, String ca_tobenotified, String ca_citenotsignedbydriver) {

        editor.putString(ISSUE_DATE, issuedate);
        editor.putString(TIME, time);
        editor.putString(AMPM, ampm);
        editor.putString(SCHOOL_ZONE, schoolzome);
        editor.putString(VIOLATIONCITY, violationcity);
        editor.putString(VIOLATIONCITY_ID, violationcity_id);
        editor.putString(VIOLATIONST, violationst);
        editor.putString(VIOLATIONSTTYP, violationsttyp);
        editor.putString(VIOLATIONSTTYP_ID, violationsttyp_id);
        editor.putString(VIOLATIONCST, violationcst);
        editor.putString(VIOLATIONCSTTYP, violationcsttyp);
        editor.putString(VIOLATIONCSTTYP_ID, violationcsttyp_id);
        editor.putString(APPEAR_DATE, appeardate);
        editor.putString(COURT_TIME, courttime);
        editor.putString(OFFBADGENO, offbadgeno);
        editor.putString(OFFLNAME, offlname);
        editor.putString(AREACODE, areacode);
        editor.putString(AREACODE_ID, areacode_id);
        editor.putString(DIVISION, division);
        editor.putString(DIVISION_VALUE, divisionvalue);
        editor.putString(DIVISION, division);
        editor.putString(DIVISION_ID, division_id);
        editor.putString(DETAIL, detail);
        editor.putString(NIGHT_COURT, night_court);
        editor.putString(CA_TOBENOTIFIED, ca_tobenotified);
        editor.putString(CA_CITENOTSIGNEDBYDRIVER, ca_citenotsignedbydriver);

        editor.commit();
    }



    public HashMap<String, String> getViolationMiscSession() {
        HashMap<String, String> user = new HashMap<String, String>();


        user.put(ISSUE_DATE, pref.getString(ISSUE_DATE, ""));
        user.put(TIME, pref.getString(TIME, ""));
        user.put(AMPM, pref.getString(AMPM, ""));
        user.put(SCHOOL_ZONE, pref.getString(SCHOOL_ZONE, ""));
        user.put(VIOLATIONCITY, pref.getString(VIOLATIONCITY, ""));
        user.put(VIOLATIONCITY_ID, pref.getString(VIOLATIONCITY_ID, ""));
        user.put(VIOLATIONST, pref.getString(VIOLATIONST, ""));
        user.put(VIOLATIONSTTYP, pref.getString(VIOLATIONSTTYP, ""));
        user.put(VIOLATIONSTTYP_ID, pref.getString(VIOLATIONSTTYP_ID, ""));
        user.put(VIOLATIONCST, pref.getString(VIOLATIONCST, ""));
        user.put(VIOLATIONCSTTYP, pref.getString(VIOLATIONCSTTYP, ""));
        user.put(VIOLATIONCSTTYP_ID, pref.getString(VIOLATIONCSTTYP_ID, ""));
        user.put(APPEAR_DATE, pref.getString(APPEAR_DATE, ""));
        user.put(COURT_TIME, pref.getString(COURT_TIME, ""));
        user.put(OFFBADGENO, pref.getString(OFFBADGENO, ""));
        user.put(OFFLNAME, pref.getString(OFFLNAME, ""));
        user.put(AREACODE, pref.getString(AREACODE, ""));
        user.put(AREACODE_ID, pref.getString(AREACODE_ID, ""));
        user.put(DETAIL, pref.getString(DETAIL, ""));
        user.put(DIVISION, pref.getString(DIVISION, ""));
        user.put(DIVISION_ID, pref.getString(DIVISION_ID, ""));
        user.put(NIGHT_COURT, pref.getString(NIGHT_COURT, ""));
        user.put(CA_TOBENOTIFIED, pref.getString(CA_TOBENOTIFIED, ""));
        user.put(CA_CITENOTSIGNEDBYDRIVER, pref.getString(CA_CITENOTSIGNEDBYDRIVER, ""));


        return user;
    }

    public void ClearViolationMiscEntery() {

        pref.edit().remove(ISSUE_DATE).commit();
        pref.edit().remove(TIME).commit();
        pref.edit().remove(AMPM).commit();
        pref.edit().remove(SCHOOL_ZONE).commit();
        pref.edit().remove(VIOLATIONCITY).commit();
        pref.edit().remove(VIOLATIONCITY_ID).commit();
        pref.edit().remove(VIOLATIONST).commit();
        pref.edit().remove(VIOLATIONSTTYP).commit();
        pref.edit().remove(VIOLATIONSTTYP_ID).commit();
        pref.edit().remove(VIOLATIONCST).commit();
        pref.edit().remove(VIOLATIONCSTTYP).commit();
        pref.edit().remove(VIOLATIONCSTTYP_ID).commit();
        pref.edit().remove(APPEAR_DATE).commit();
        pref.edit().remove(COURT_TIME).commit();
        pref.edit().remove(OFFBADGENO).commit();
        pref.edit().remove(OFFLNAME).commit();
        pref.edit().remove(AREACODE).commit();
        pref.edit().remove(AREACODE_ID).commit();
        pref.edit().remove(DETAIL).commit();
        pref.edit().remove(DIVISION).commit();
        pref.edit().remove(DIVISION_ID).commit();
        pref.edit().remove(NIGHT_COURT).commit();
        pref.edit().remove(CA_TOBENOTIFIED).commit();
        pref.edit().remove(CA_CITENOTSIGNEDBYDRIVER).commit();

        editor.commit();
    }



    public void createCourtCodePosition(String courtcode_position){

        editor.commit();
    }
    public HashMap<String, String> getCourtCodePosition() {
        HashMap<String, String> user = new HashMap<String, String>();

        return user;
    }

    public static final String CITATION_NUMBER = "citation_number";
    public static final String OWNER_RESPONSIBILITY = "owner_responsibility";
    public static final String TRAFFIC = "traffic";
    public static final String NON_TRAFFIC = "nontraffic";
    public static final String PED = "ped";
    public static final String COURT_CODE = "courtcode";
    public static final String COURT_CODE_ID = "courtcode_id";
    public static final String LEA = "lea";
    public static final String LEA_ID = "leaid";
    public static final String COURT_CODE_POSITION = "courtcode_position";


    public void createHeaderSession(String citation_number, String owner_responsibility, String traffic, String nontraffic, String ped, String courtcode, String courtcode_id,String courtcode_position, String lea, String leaid){

        editor.putString(CITATION_NUMBER, citation_number);
        editor.putString(OWNER_RESPONSIBILITY, owner_responsibility);
        editor.putString(TRAFFIC, traffic);
        editor.putString(NON_TRAFFIC, nontraffic);
        editor.putString(PED, ped);
        editor.putString(COURT_CODE, courtcode);
        editor.putString(COURT_CODE_ID, courtcode_id);
        editor.putString(COURT_CODE_POSITION, courtcode_position);
        editor.putString(LEA, lea);
        editor.putString(LEA_ID, leaid);

        editor.commit();
    }

    public HashMap<String, String> getHeaderSession() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(CITATION_NUMBER, pref.getString(CITATION_NUMBER, ""));
        user.put(OWNER_RESPONSIBILITY, pref.getString(OWNER_RESPONSIBILITY, ""));
        user.put(TRAFFIC, pref.getString(TRAFFIC, ""));
        user.put(NON_TRAFFIC, pref.getString(NON_TRAFFIC, ""));
        user.put(PED, pref.getString(PED, ""));
        user.put(COURT_CODE, pref.getString(COURT_CODE, ""));
        user.put(COURT_CODE_ID, pref.getString(COURT_CODE_ID, ""));
        user.put(COURT_CODE_POSITION, pref.getString(COURT_CODE_POSITION, ""));
        user.put(LEA, pref.getString(LEA, ""));
        user.put(LEA_ID, pref.getString(LEA_ID, ""));

        return user;
    }


    
    public void ClearHeaderEntery() {

        pref.edit().remove(CITATION_NUMBER).commit();
        pref.edit().remove(OWNER_RESPONSIBILITY).commit();
        pref.edit().remove(TRAFFIC).commit();
        pref.edit().remove(NON_TRAFFIC).commit();
        pref.edit().remove(PED).commit();
        pref.edit().remove(COURT_CODE).commit();
        pref.edit().remove(COURT_CODE_ID).commit();
        pref.edit().remove(COURT_CODE_POSITION).commit();
        pref.edit().remove(LEA).commit();
        pref.edit().remove(LEA_ID).commit();

        editor.commit();
    }

    public static final String DRIVER_FIRST_NAME = "driver_first_name";
    public static final String DRIVER_MIDDLE_NAME = "driver_middle_name";
    public static final String DRIVER_LAST_NAME = "driver_last_name";
    public static final String DRIVER_SUFFIX = "driver_suffix";
    public static final String DRIVER_SUFFIX_ID = "s_driver_suffixid";
    public static final String DRIVER_ADDRESS1 = "driver_address1";
    public static final String DRIVER_ADDRESS2 = "driver_address2";
    public static final String DRIVER_CITY = "driver_city";
    public static final String DRIVER_CITY_ID = "driver_cityid";
    public static final String DRIVER_STATE = "driver_state";
    public static final String DRIVER_STATE_ID = "driver_stateid";
    public static final String DRIVER_ZIPCODE = "driver_zipcode";
    public static final String DRIVER_LICENSE_NO = "driver_license_no";
    public static final String DRIVER_STATEE = "driver_statee";
    public static final String DRIVER_STATEE_ID = "driver_stateeid";
    public static final String DOB = "dob";
    public static final String SEX = "sex";
    public static final String HAIR = "hair";
    public static final String HAIR_ID = "hair_id";
    public static final String EYES = "eyes";
    public static final String EYES_ID = "eyes_id";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String LICENSE_TYPE = "license_type";
    public static final String LICENSE_TYPE_ID = "license_type_id";
    public static final String RACE = "race";
    public static final String RACE_ID = "race_id";
    public static final String ETHNCITY = "ethncity";
    public static final String ETHNCITY_ID = "ethncity_id";
    public static final String COMM_DRIVER_LICENSE = "comm_driver_license";

    public void createDriverSession(String driver_first_name, String driver_middle_name, String driver_last_name, String driver_suffix,String s_driver_suffixid, String driver_address1, String driver_address2,
                                    String driver_city,String driver_cityid, String driver_state,String driver_stateid, String driver_zipcode, String driver_license_no, String driver_statee,String driver_stateeid, String dob,
                                    String sex, String hair,String hair_id, String eyes,String eyes_id, String height, String weight, String license_type,String license_type_id, String race,String race_id, String ethncity,String ethncity_id, String comm_driver_license) {

        editor.putString(DRIVER_FIRST_NAME, driver_first_name);
        editor.putString(DRIVER_MIDDLE_NAME, driver_middle_name);
        editor.putString(DRIVER_LAST_NAME, driver_last_name);
        editor.putString(DRIVER_SUFFIX, driver_suffix);
        editor.putString(DRIVER_SUFFIX_ID, s_driver_suffixid);
        editor.putString(DRIVER_ADDRESS1, driver_address1);
        editor.putString(DRIVER_ADDRESS2, driver_address2);
        editor.putString(DRIVER_CITY, driver_city);
        editor.putString(DRIVER_CITY_ID, driver_cityid);
        editor.putString(DRIVER_STATE, driver_state);
        editor.putString(DRIVER_STATE_ID, driver_stateid);
        editor.putString(DRIVER_ZIPCODE, driver_zipcode);
        editor.putString(DRIVER_LICENSE_NO, driver_license_no);
        editor.putString(DRIVER_STATEE, driver_statee);
        editor.putString(DRIVER_STATEE_ID, driver_stateeid);
        editor.putString(DOB, dob);
        editor.putString(SEX, sex);
        editor.putString(HAIR, hair);
        editor.putString(HAIR_ID, hair_id);
        editor.putString(EYES, eyes);
        editor.putString(EYES_ID, eyes_id);
        editor.putString(HEIGHT, height);
        editor.putString(WEIGHT, weight);
        editor.putString(LICENSE_TYPE, license_type);
        editor.putString(LICENSE_TYPE_ID, license_type_id);
        editor.putString(RACE, race);
        editor.putString(RACE_ID, race_id);
        editor.putString(ETHNCITY, ethncity);
        editor.putString(ETHNCITY_ID, ethncity_id);
        editor.putString(COMM_DRIVER_LICENSE, comm_driver_license);
        editor.commit();
    }

    public HashMap<String, String> getDriverSession() {
        HashMap<String, String> user = new HashMap<String, String>();


        user.put(DRIVER_FIRST_NAME, pref.getString(DRIVER_FIRST_NAME, ""));
        user.put(DRIVER_MIDDLE_NAME, pref.getString(DRIVER_MIDDLE_NAME, ""));
        user.put(DRIVER_LAST_NAME, pref.getString(DRIVER_LAST_NAME, ""));
        user.put(DRIVER_SUFFIX, pref.getString(DRIVER_SUFFIX, ""));
        user.put(DRIVER_SUFFIX_ID, pref.getString(DRIVER_SUFFIX_ID, ""));
        user.put(DRIVER_ADDRESS1, pref.getString(DRIVER_ADDRESS1, ""));
        user.put(DRIVER_ADDRESS2, pref.getString(DRIVER_ADDRESS2, ""));
        user.put(DRIVER_CITY, pref.getString(DRIVER_CITY, ""));
        user.put(DRIVER_CITY_ID, pref.getString(DRIVER_CITY_ID, ""));
        user.put(DRIVER_STATE, pref.getString(DRIVER_STATE, ""));
        user.put(DRIVER_STATE_ID, pref.getString(DRIVER_STATE_ID, ""));
        user.put(DRIVER_ZIPCODE, pref.getString(DRIVER_ZIPCODE, ""));
        user.put(DRIVER_LICENSE_NO, pref.getString(DRIVER_LICENSE_NO, ""));
        user.put(DRIVER_STATEE, pref.getString(DRIVER_STATEE, ""));
        user.put(DRIVER_STATEE_ID, pref.getString(DRIVER_STATEE_ID, ""));
        user.put(DOB, pref.getString(DOB, ""));
        user.put(SEX, pref.getString(SEX, ""));
        user.put(HAIR, pref.getString(HAIR, ""));
        user.put(HAIR_ID, pref.getString(HAIR_ID, ""));
        user.put(EYES, pref.getString(EYES, ""));
        user.put(EYES_ID, pref.getString(EYES_ID, ""));
        user.put(HEIGHT, pref.getString(HEIGHT, ""));
        user.put(WEIGHT, pref.getString(WEIGHT, ""));
        user.put(LICENSE_TYPE, pref.getString(LICENSE_TYPE, ""));
        user.put(LICENSE_TYPE_ID, pref.getString(LICENSE_TYPE_ID, ""));
        user.put(RACE, pref.getString(RACE, ""));
        user.put(RACE_ID, pref.getString(RACE_ID, ""));
        user.put(ETHNCITY, pref.getString(ETHNCITY, ""));
        user.put(ETHNCITY_ID, pref.getString(ETHNCITY_ID, ""));
        user.put(COMM_DRIVER_LICENSE, pref.getString(COMM_DRIVER_LICENSE, ""));


        return user;
    }
    public void ClearDriverEntery() {

        pref.edit().remove(DRIVER_FIRST_NAME).commit();
        pref.edit().remove(DRIVER_MIDDLE_NAME).commit();
        pref.edit().remove(DRIVER_LAST_NAME).commit();
        pref.edit().remove(DRIVER_SUFFIX).commit();
        pref.edit().remove(DRIVER_SUFFIX_ID).commit();
        pref.edit().remove(DRIVER_ADDRESS1).commit();
        pref.edit().remove(DRIVER_ADDRESS2).commit();
        pref.edit().remove(DRIVER_CITY).commit();
        pref.edit().remove(DRIVER_CITY_ID).commit();
        pref.edit().remove(DRIVER_STATE).commit();
        pref.edit().remove(DRIVER_STATE_ID).commit();
        pref.edit().remove(DRIVER_ZIPCODE).commit();
        pref.edit().remove(DRIVER_LICENSE_NO).commit();
        pref.edit().remove(DRIVER_STATEE).commit();
        pref.edit().remove(DRIVER_STATEE_ID).commit();
        pref.edit().remove(DOB).commit();
        pref.edit().remove(SEX).commit();
        pref.edit().remove(HAIR).commit();
        pref.edit().remove(HAIR_ID).commit();
        pref.edit().remove(EYES).commit();
        pref.edit().remove(EYES_ID).commit();
        pref.edit().remove(HEIGHT).commit();
        pref.edit().remove(WEIGHT).commit();
        pref.edit().remove(LICENSE_TYPE).commit();
        pref.edit().remove(LICENSE_TYPE_ID).commit();
        pref.edit().remove(RACE).commit();
        pref.edit().remove(RACE_ID).commit();
        pref.edit().remove(ETHNCITY).commit();
        pref.edit().remove(ETHNCITY_ID).commit();
        pref.edit().remove(COMM_DRIVER_LICENSE).commit();

        editor.commit();
    }

    public static final String LAST_SYNC_DATE = "last_sync_date";
    public static final String LAST_SYNC_TIME = "last_sync_time";


    public void createLastAdminSyncSession(String last_sync_date, String last_sync_time){

        editor.putString(LAST_SYNC_DATE, last_sync_date);
        editor.putString(LAST_SYNC_TIME, last_sync_time);

        editor.commit();
    }


    public HashMap<String, String> getLastAdminSyncSession() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(LAST_SYNC_DATE, pref.getString(LAST_SYNC_DATE, ""));
        user.put(LAST_SYNC_TIME, pref.getString(LAST_SYNC_TIME, ""));

        return user;
    }


    public static final String USER_ID = "user_id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String ROLE_ID = "role_id";
    public static final String BADGE_NO = "badge_no";
    public static final String USER_ROLE = "user_role";
    public static final String STATUS = "status";
    public static final String TOKEN = "token";

    public void createAdminLogin(String user_id, String name, String email, String role_id, String badge_no, String user_role, String status, String token){

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER_ID, user_id);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(ROLE_ID, role_id);
        editor.putString(BADGE_NO, badge_no);
        editor.putString(USER_ROLE, user_role);
        editor.putString(STATUS, status);
        editor.putString(TOKEN, token);
        editor.commit();
    }


    public void logoutUser() {

        editor.putBoolean(IS_LOGIN, false);
               pref.edit().remove(USER_ID).commit();
        pref.edit().remove(NAME).commit();
        pref.edit().remove(EMAIL).commit();
        pref.edit().remove(ROLE_ID).commit();
        pref.edit().remove(BADGE_NO).commit();
        pref.edit().remove(USER_ROLE).commit();
        pref.edit().remove(STATUS).commit();
        pref.edit().remove(TOKEN).commit();
        pref.edit().remove(LAST_SYNC_DATE).commit();
        pref.edit().remove(LAST_SYNC_TIME).commit();
        pref.edit().remove(HEADER_SECTION).commit();
        pref.edit().remove(DRIVER_SECTION).commit();
        pref.edit().remove(VEHICLE_SECTION).commit();
        pref.edit().remove(VIOLATION_SECTION).commit();
        pref.edit().remove(VIOLATIONMISC_SECTION).commit();
        editor.commit();
    }

    public HashMap<String, String> getAdminLogin() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(USER_ID, pref.getString(USER_ID, ""));
        user.put(NAME, pref.getString(NAME, ""));
        user.put(EMAIL, pref.getString(EMAIL, ""));
        user.put(ROLE_ID, pref.getString(ROLE_ID, ""));
        user.put(BADGE_NO, pref.getString(BADGE_NO, ""));
        user.put(USER_ROLE, pref.getString(USER_ROLE, ""));
        user.put(STATUS, pref.getString(STATUS, ""));
        user.put(TOKEN, pref.getString(TOKEN, ""));

        return user;
    }


    public static final String HEADER_SECTION = "header_section";
    public static final String DRIVER_SECTION = "driver_section";
    public static final String VEHICLE_SECTION = "vehilce_section";
    public static final String VIOLATION_SECTION = "violation_section";
    public static final String VIOLATIONMISC_SECTION = "violationmisc_section";

    public void saveHeader(String header_section){

        editor.putString(HEADER_SECTION, header_section);
        editor.commit();
    }

    public void saveDriver(String driver_section){

        editor.putString(DRIVER_SECTION, driver_section);
        editor.commit();
    }

    public void saveVehicle(String vehilce_section){

        editor.putString(VEHICLE_SECTION, vehilce_section);
        editor.commit();
    }

    public void saveViolation(String violation_section){

        editor.putString(VIOLATION_SECTION, violation_section);
        editor.commit();
    }

    public void saveViolationMisc(String violationmisc_section){

        editor.putString(VIOLATIONMISC_SECTION, violationmisc_section);
        editor.commit();
    }

    public HashMap<String, String> getHeader() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(HEADER_SECTION, pref.getString(HEADER_SECTION, ""));

        return user;
    }

    public HashMap<String, String> getDriver() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(DRIVER_SECTION, pref.getString(DRIVER_SECTION, ""));

        return user;
    }

    public HashMap<String, String> getVehicle() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(VEHICLE_SECTION, pref.getString(VEHICLE_SECTION, ""));

        return user;
    }

    public HashMap<String, String> getViolation() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(VIOLATION_SECTION, pref.getString(VIOLATION_SECTION, ""));

        return user;
    }

    public HashMap<String, String> getViolationMisc() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(VIOLATIONMISC_SECTION, pref.getString(VIOLATIONMISC_SECTION, ""));
        return user;
    }


    public void ClearDataEntery() {

        pref.edit().remove(HEADER_SECTION).commit();
        pref.edit().remove(DRIVER_SECTION).commit();
        pref.edit().remove(VEHICLE_SECTION).commit();
        pref.edit().remove(VIOLATION_SECTION).commit();
        pref.edit().remove(VIOLATIONMISC_SECTION).commit();

        editor.commit();
    }



}