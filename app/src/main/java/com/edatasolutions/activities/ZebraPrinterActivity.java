package com.edatasolutions.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.edatasolutions.R;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.edatasolutions.database.DatabaseAccess;
import com.edatasolutions.utils.SessionManager;
import com.edatasolutions.zebraPrinterUtils.DemoSleeper;
import com.edatasolutions.zebraPrinterUtils.SettingsHelper;
import com.edatasolutions.zebraPrinterUtils.UIHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
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

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;


public class ZebraPrinterActivity extends AppCompatActivity {

    private Connection connection;

    private RadioButton btRadioButton;
    private EditText macAddressEditText;
    private EditText ipAddressEditText;
    private EditText portNumberEditText;
    private static final String bluetoothAddressKey = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";
    private static final String tcpAddressKey = "ZEBRA_DEMO_TCP_ADDRESS";
    private static final String tcpPortKey = "ZEBRA_DEMO_TCP_PORT";
    private static final String PREFS_NAME = "OurSavedAddress";

    private DatabaseAccess databaseAccess;
    private Button testButton, print_data;
    String macAddress="";
    boolean connected = false;
    int timeout=17;
    int count=0;
    private int linecount = 0;
    private int total_height = 0;
    private static final int line_height = 48;
    private ZebraPrinter printer;
    //private TextView statusField;
    private LinearLayout ll_print_layout;

    //-----------------
    private UIHelper helper = new UIHelper(this);

    @Override
    public void onBackPressed() {
        sessionManager.ClearDataEntery();
        sessionManager.clearSession();
        super.onBackPressed();
    }

    private TextView owner_responsibility,traffic,ped,court_code,lea;
    private TextView driver_firstname, driver_middlename, driver_lastname, driver_suffix, driver_address1, driver_address2, driver_city, driver_state, driver_zipcode;
    private TextView driver_license_no, driver_statee, driver_dob, driver_sex, driver_hair, driver_eyes, driver_height, driver_weight, license_type, driver_race, driver_ethncity, comm_drivers_license;
    private TextView pre_owner_firstname, pre_owner_middlename, pre_owner_lastname, pre_owner_suffix, pre_owner_address1, pre_owner_address2, pre_owner_city, pre_owner_state, pre_owner_zipcode;
    private TextView pre_vehyear, pre_vehmake, pre_vehmodel, pre_vehbody, pre_vehcolor, pre_vehtype, pre_commercial, pre_vehno, pre_vehstate, pre_hazardous, pre_overload, pre_policyno;
    private TextView pre_vioA, pre_vioB, pre_vioC, pre_vioD, pre_vioE, pre_vioF, pre_vioG, pre_vioH, pre_vca, pre_vcb, pre_vcc, pre_vcd, pre_vce, pre_vcf, pre_vcg, pre_vch;
    private TextView pre_apprspeed, pre_pfspeed, pre_vehlimit, pre_safespeed, pre_animal1, pre_animal2, pre_animal3, pre_animal4, pre_animal5, pre_animal6, pre_animal7, pre_animal8;

    private TextView school_zone,violationcity,violationst,violationsttyp,violationcst,violationcsttyp,offbadgeno,offlname,offarea,division,detail,catobenotified,cacitenotsignedbydriver;
    private TextView citation_no_txt;
    private TextView appeardate_txt, courttime_txt, nightcourt_txt, issuetime_txt, issuedate_txt;
    private SessionManager sessionManager;
    private ImageView clear_page;
    private SharedPreferences settings;
    private LinearLayout ll_ctlout,ll_owner_responsibility,ll_traffic,ll_ped,ll_court_code,ll_lea;

    private LinearLayout ll_driver_first_name, ll_driver_middlename, ll_driver_lastname, ll_driver_suffix, ll_driver_address1, ll_driver_address2, ll_driver_city, ll_driver_state, ll_driver_zipcode, ll_driver_license_no,ll_driver_statee,ll_driver_dob,ll_driver_gender,ll_driver_hair,ll_driver_eyes,ll_driver_height,ll_driver_weight,ll_driver_license_type,ll_driver_race,ll_driver_ethnicity,ll_driver_common_driver_license;
    private LinearLayout ll_owner_first_name, ll_owner_middlename, ll_owner_lastname, ll_owner_suffix, ll_owner_address1, ll_owner_address2, ll_owner_city, ll_owner_state, ll_owner_zipcode,ll_vehyear,ll_vehmake,ll_vehmodel,ll_vehbody,ll_vehcolor,ll_vehtype,ll_vehcommercial,ll_veh_licno,ll_veh_state,ll_hazardous,ll_overload,ll_insurance_policy_no;
    private LinearLayout ll_vioA, ll_vioB, ll_vioC, ll_vioD, ll_vioE, ll_vioF, ll_vioG, ll_vioH, ll_vca, ll_vcb, ll_vcc, ll_vcd, ll_vce, ll_vcf, ll_vcg, ll_vch;
    private LinearLayout ll_animal1, ll_animal2, ll_animal3, ll_animal4, ll_animal5, ll_animal6, ll_animal7, ll_animal8;
    private LinearLayout ll_vehlimit,ll_safe_speed,ll_appr_speed,ll_pf_speed,ll_issue_date,ll_issue_time,ll_appear_date,ll_court_time,ll_night_court;

    private LinearLayout ll_schoolzone,ll_violationcity,ll_violationst,ll_violationsttyp,ll_violationcst,ll_violationcsttyp,ll_offbadgeno,ll_offlname,ll_offarea,ll_division,ll_detail,ll_catobenotified,ll_cacitenotsignedbydriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zebra_printer);

        init();
        setAllData();
        settings = getSharedPreferences(PREFS_NAME, 0);
        ipAddressEditText = (EditText) this.findViewById(R.id.ipAddressInput);

        portNumberEditText = (EditText) this.findViewById(R.id.portInput);

        macAddressEditText = (EditText) this.findViewById(R.id.macInput);

        TextView t2 = (TextView) findViewById(R.id.launchpad_link);
        t2.setMovementMethod(LinkMovementMethod.getInstance());

        //statusField = (TextView) this.findViewById(R.id.statusText);


        btRadioButton = (RadioButton) this.findViewById(R.id.bluetoothRadio);


        RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.bluetoothRadio) {
                    toggleEditField(macAddressEditText, true);
                    toggleEditField(portNumberEditText, false);
                    toggleEditField(ipAddressEditText, false);
                } else {
                    toggleEditField(portNumberEditText, true);
                    toggleEditField(ipAddressEditText, true);
                    toggleEditField(macAddressEditText, false);
                }
            }
        });
        testButton = (Button) this.findViewById(R.id.testButton);
        print_data = (Button) this.findViewById(R.id.print_data);
        print_data.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                /*new Thread(new Runnable() {
                    public void run() {
                        enableTestButton(false);
                        Looper.prepare();
                        doConnectionTest();
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                }).start();*/
                //enablePrintButton(false);
                total_height = 0;
                linecount = 0;
                performTestWithManyJobs();
            }
        });

        clear_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.ClearDataEntery();
                sessionManager.clearSession();
                Intent i = new Intent(ZebraPrinterActivity.this, ActivityHome.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

    }
    private void init() {
        sessionManager = new SessionManager(ZebraPrinterActivity.this);
        clear_page = findViewById(R.id.clear_page);

        owner_responsibility = findViewById(R.id.owner_responsibility);
        traffic = findViewById(R.id.traffic);
        ped = findViewById(R.id.ped);
        court_code = findViewById(R.id.court_code);
        lea = findViewById(R.id.lea);

        citation_no_txt = findViewById(R.id.citation_no_txt);
        owner_responsibility = findViewById(R.id.owner_responsibility);

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
        pre_apprspeed = findViewById(R.id.pre_apprspeed);
        pre_pfspeed = findViewById(R.id.pre_pfspeed);
        pre_safespeed = findViewById(R.id.pre_safespeed);
        pre_animal1 = findViewById(R.id.pre_animal1);
        pre_animal2 = findViewById(R.id.pre_animal2);
        pre_animal3 = findViewById(R.id.pre_animal3);
        pre_animal4 = findViewById(R.id.pre_animal4);
        pre_animal5 = findViewById(R.id.pre_animal5);
        pre_animal6 = findViewById(R.id.pre_animal6);
        pre_animal7 = findViewById(R.id.pre_animal7);
        pre_animal8 = findViewById(R.id.pre_animal8);

        ll_ctlout = findViewById(R.id.citation_lout);
        ll_owner_responsibility = findViewById(R.id.ll_owner_responsibility);
        ll_traffic = findViewById(R.id.ll_traffic);
        ll_ped = findViewById(R.id.ll_PED);
        ll_court_code = findViewById(R.id.ll_court_code);
        ll_lea = findViewById(R.id.ll_LEA);


        ll_driver_first_name = findViewById(R.id.ll_driver_first_name);
        ll_driver_middlename = findViewById(R.id.ll_driver_middlename);
        ll_driver_lastname = findViewById(R.id.ll_driver_lastname);
        ll_driver_suffix = findViewById(R.id.ll_driver_suffix);
        ll_driver_address1 = findViewById(R.id.ll_driver_address1);
        ll_driver_address2 = findViewById(R.id.ll_driver_address2);
        ll_driver_city = findViewById(R.id.ll_driver_city);
        ll_driver_state = findViewById(R.id.ll_driver_state);
        ll_driver_zipcode = findViewById(R.id.ll_driver_zipcode);
        ll_driver_license_no = findViewById(R.id.ll_driver_license_no);
        ll_driver_statee = findViewById(R.id.ll_driver_statee);
        ll_driver_dob = findViewById(R.id.ll_driver_dob);
        ll_driver_gender = findViewById(R.id.ll_driver_gender);
        ll_driver_hair = findViewById(R.id.ll_driver_hair);
        ll_driver_eyes = findViewById(R.id.ll_driver_eyes);
        ll_driver_height = findViewById(R.id.ll_driver_height);
        ll_driver_weight = findViewById(R.id.ll_driver_weight);
        ll_driver_license_type = findViewById(R.id.ll_driver_licensetype);
        ll_driver_race = findViewById(R.id.ll_driver_race);
        ll_driver_ethnicity = findViewById(R.id.ll_driver_ethnicity);
        ll_driver_common_driver_license = findViewById(R.id.ll_driver_common_driver_license);


        ll_owner_first_name = findViewById(R.id.ll_owner_first_name);
        ll_owner_middlename = findViewById(R.id.ll_owner_middlename);
        ll_owner_lastname = findViewById(R.id.ll_owner_lastname);
        ll_owner_suffix = findViewById(R.id.ll_owner_suffix);
        ll_owner_address1 = findViewById(R.id.ll_owner_address1);
        ll_owner_address2 = findViewById(R.id.ll_owner_address2);
        ll_owner_city = findViewById(R.id.ll_owner_city);
        ll_owner_state = findViewById(R.id.ll_owner_state);
        ll_owner_zipcode = findViewById(R.id.ll_owner_zipcode);
        ll_vehyear = findViewById(R.id.ll_vehyear);
        ll_vehmake = findViewById(R.id.ll_vehmake);
        ll_vehmodel = findViewById(R.id.ll_vehmodel);
        ll_vehbody = findViewById(R.id.ll_vehbody);
        ll_vehcolor = findViewById(R.id.ll_vehcolor);
        ll_vehtype = findViewById(R.id.ll_vehtype);
        ll_vehcommercial = findViewById(R.id.ll_vehcommercial);
        ll_veh_licno = findViewById(R.id.ll_vehlicno);
        ll_veh_state = findViewById(R.id.ll_veh_state);
        ll_hazardous = findViewById(R.id.ll_hazardous);
        ll_overload = findViewById(R.id.ll_overload);
        ll_insurance_policy_no = findViewById(R.id.ll_insurance_policy_no);

        ll_vehlimit = findViewById(R.id.ll_vehlimit);
        ll_safe_speed = findViewById(R.id.ll_safe_speed);
        ll_appr_speed = findViewById(R.id.ll_appr_speed);
        ll_pf_speed = findViewById(R.id.ll_pfspeed);
        ll_issue_date = findViewById(R.id.ll_issue_date);
        ll_issue_time = findViewById(R.id.ll_issue_time);
        ll_appear_date = findViewById(R.id.ll_appear_date);
        ll_court_time = findViewById(R.id.ll_court_time);
        ll_night_court = findViewById(R.id.ll_night_court);

        ll_vioA = findViewById(R.id.ll_vioA);
        ll_vioB = findViewById(R.id.ll_vioB);
        ll_vioC = findViewById(R.id.ll_vioC);
        ll_vioD = findViewById(R.id.ll_vioD);
        ll_vioE = findViewById(R.id.ll_vioE);
        ll_vioF = findViewById(R.id.ll_vioF);
        ll_vioG = findViewById(R.id.ll_vioG);
        ll_vioH = findViewById(R.id.ll_vioH);

        ll_vca = findViewById(R.id.ll_vca);
        ll_vcb = findViewById(R.id.ll_vcb);
        ll_vcc = findViewById(R.id.ll_vcc);
        ll_vcd = findViewById(R.id.ll_vcd);
        ll_vce = findViewById(R.id.ll_vce);
        ll_vcf = findViewById(R.id.ll_vcf);
        ll_vcg = findViewById(R.id.ll_vcg);
        ll_vch = findViewById(R.id.ll_vch);

        ll_animal1 = findViewById(R.id.ll_animal1);
        ll_animal2 = findViewById(R.id.ll_animal2);
        ll_animal3 = findViewById(R.id.ll_animal3);
        ll_animal4 = findViewById(R.id.ll_animal4);
        ll_animal5 = findViewById(R.id.ll_animal5);
        ll_animal6 = findViewById(R.id.ll_animal6);
        ll_animal7 = findViewById(R.id.ll_animal7);
        ll_animal8 = findViewById(R.id.ll_animal8);

        ll_print_layout = findViewById(R.id.ll_print_layout);

        appeardate_txt = findViewById(R.id.appeardate_txt);
        courttime_txt = findViewById(R.id.courttime_txt);
        nightcourt_txt = findViewById(R.id.nightcourt_txt);
        issuedate_txt = findViewById(R.id.issuedate_txt);
        issuetime_txt = findViewById(R.id.issuetime_txt);

        school_zone = findViewById(R.id.schoolzone_txt);
        violationcity = findViewById(R.id.violationcity_txt);
        violationst = findViewById(R.id.violationst_txt);
        violationsttyp = findViewById(R.id.violation_st_typ_txt);
        violationcst = findViewById(R.id.violationcst_txt);
        violationcsttyp = findViewById(R.id.violation_cst_typ_txt);
        offbadgeno = findViewById(R.id.offbadgeno_txt);
        offlname = findViewById(R.id.offlname_txt);
        offarea = findViewById(R.id.officer_area_txt);
        division = findViewById(R.id.division_txt);
        detail = findViewById(R.id.detail_txt);
        catobenotified= findViewById(R.id.ca_tobenotified_txt);
        cacitenotsignedbydriver = findViewById(R.id.ca_citenotsignedbydriver_txt);

        ll_schoolzone = findViewById(R.id.ll_school_zone);
        ll_violationcity = findViewById(R.id.ll_violation_city);
        ll_violationst = findViewById(R.id.ll_violation_st);
        ll_violationsttyp = findViewById(R.id.ll_violation_st_typ);
        ll_violationcst = findViewById(R.id.ll_violation_cst);
        ll_violationcsttyp = findViewById(R.id.ll_violation_cst_typ);
        ll_offbadgeno = findViewById(R.id.ll_offbadgeno);
        ll_offlname = findViewById(R.id.ll_offlname);
        ll_offarea = findViewById(R.id.ll_officer_area);
        ll_division = findViewById(R.id.ll_division);
        ll_detail = findViewById(R.id.ll_detail);
        ll_catobenotified= findViewById(R.id.ll_ca_tobenotified);
        ll_cacitenotsignedbydriver = findViewById(R.id.ll_ca_citenotsignedbydriver);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void disconnect() {
        try {
            //setStatus("Disconnecting", Color.RED);
            Log.e("TAG", "Disconnecting" );
            if (connection != null) {
                connection.close();
            }
            // setStatus("Not Connected", Color.RED);
            Log.e("TAG", "Not Connected" );
        } catch (ConnectionException e) {
            //setStatus("COMM Error! Disconnected", Color.RED);
            Log.e("TAG", "disconnect: COMM Error! Disconnected" );
        } finally {
            enableTestButton(true);
        }
    }

   /* private void setStatus(final String statusMessage, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                statusField.setBackgroundColor(color);
                statusField.setText(statusMessage);
            }
        });
        DemoSleeper.sleep(1000);
    }*/

    //    private void enablePrintButton(final boolean enabled) {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                print_data.setEnabled(enabled);
//            }
//        });
//    }
    private void sendTestLabel() {
        try {
            ZebraPrinterLinkOs linkOsPrinter = ZebraPrinterFactory.createLinkOsPrinter(printer);

            PrinterStatus printerStatus = (linkOsPrinter != null) ? linkOsPrinter.getCurrentStatus() : printer.getCurrentStatus();

            if (printerStatus.isReadyToPrint) {
                byte[] configLabel = getConfigLabel();
                connection.write(configLabel);
                //----------------------//
               /* Bitmap bitmapToPrint = loadBitmapFromView(ll_print_layout);
                ZebraImageAndroid zebraImageToPrint = new ZebraImageAndroid(bitmapToPrint);
                //printer.printImage(zebraImageToPrint, 0, 0, -1, -1, false);
                printer.printImage(zebraImageToPrint, 0, 0, 970, 11809, false);*/
                //-----------------------//
                //setStatus("Sending Data", Color.BLUE);
            } else if (printerStatus.isHeadOpen) {
                // setStatus("Printer Head Open", Color.RED);
                Log.e("sendTestLabel", "sendTestLabel: Printer Head Open" );
            } else if (printerStatus.isPaused) {
                // setStatus("Printer is Paused", Color.RED);
                Log.e("sendTestLabel", "sendTestLabel: Printer is Paused" );
            } else if (printerStatus.isPaperOut) {
                // setStatus("Printer Media Out", Color.RED);
                Log.e("sendTestLabel", "sendTestLabel: Printer Media Out" );
            }
            DemoSleeper.sleep(1500);
            if (connection instanceof BluetoothConnection) {
                String friendlyName = ((BluetoothConnection) connection).getFriendlyName();
                // setStatus(friendlyName, Color.MAGENTA);
                DemoSleeper.sleep(500);
            }
        } catch (ConnectionException e) {
            // setStatus(e.getMessage(), Color.RED);
        } finally {
            disconnect();
        }
    }
    private void enableTestButton(final boolean enabled) {
        runOnUiThread(new Runnable() {
            public void run() {
                testButton.setEnabled(enabled);
            }
        });
    }

    /*
     * Returns the command for a test label depending on the printer control language
     * The test label is a box with the word "TEST" inside of it
     *
     * _________________________
     * |                       |
     * |                       |
     * |        TEST           |
     * |                       |
     * |                       |
     * |_______________________|
     *
     *
     */
    private byte[] getConfigLabel() {
        byte[] configLabel = null;
        try {
            PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
            SGD.SET("device.languages", "zpl", connection);
            //SGD.SET("device.languages", "CPCL", connection);

            if (printerLanguage == PrinterLanguage.ZPL) {
                configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
            } else if (printerLanguage == PrinterLanguage.CPCL) {
                String cpclConfigLabel = "! 0 200 200 406 1\r\n" + "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n" + "T 0 6 137 177 TEST\r\n" + "PRINT\r\n";
                configLabel = cpclConfigLabel.getBytes();
            }
        } catch (ConnectionException e) {

        }
        return configLabel;
    }

    private void toggleEditField(EditText editText, boolean set) {
        /*
         * Note: Disabled EditText fields may still get focus by some other means, and allow text input.
         *       See http://code.google.com/p/android/issues/detail?id=2771
         */
        editText.setEnabled(set);
        editText.setFocusable(set);
        editText.setFocusableInTouchMode(set);
    }

    private boolean isBluetoothSelected() {
        return btRadioButton.isChecked();
    }

    private String getMacAddressFieldText() {
        return settings.getString(bluetoothAddressKey, "");
    }

    private String getTcpAddress() {
        return settings.getString(tcpAddressKey, "");
    }

    private String getTcpPortNumber() {
        return settings.getString(tcpPortKey, "");
    }


    private void setAllData() {

        databaseAccess = new DatabaseAccess(ZebraPrinterActivity.this);
        databaseAccess.open();
        String session_citation_no = sessionManager.getHeaderSession().get(SessionManager.CITATION_NUMBER);


        citation_no_txt.setText(session_citation_no);


        //HEADER
        String session_owner_responsibility = sessionManager.getHeaderSession().get(SessionManager.OWNER_RESPONSIBILITY);
        String session_traffic = sessionManager.getHeaderSession().get(SessionManager.TRAFFIC);
        String session_nontraffic = sessionManager.getHeaderSession().get(SessionManager.NON_TRAFFIC);
        String session_ped = sessionManager.getHeaderSession().get(SessionManager.PED);
        String session_courtcode_id = sessionManager.getHeaderSession().get(SessionManager.COURT_CODE_ID);
        String session_court_code_value = sessionManager.getHeaderSession().get(SessionManager.COURT_CODE);
        String session_lea = sessionManager.getHeaderSession().get(SessionManager.LEA);

        assert session_owner_responsibility != null;
        if (session_owner_responsibility.equals("Y")) {
            owner_responsibility.setText("Yes");
        } else {
            owner_responsibility.setText(getResources().getString(R.string.dash));
            ll_owner_responsibility.setVisibility(View.GONE);
        }

        assert session_traffic != null && session_nontraffic != null;
        if (!session_traffic.equals("") || !session_nontraffic.equals("")) {

            if(session_traffic.equals("1")){
                traffic.setText("Yes");
            }
            else{
                traffic.setText(getResources().getString(R.string.dash));
                ll_traffic.setVisibility(View.GONE);
            }

        } else {
            traffic.setText(getResources().getString(R.string.dash));
            ll_traffic.setVisibility(View.GONE);
        }

        assert session_ped != null;
        if (session_ped.equals("Y")) {
            ped.setText("Yes");
        } else {
            ped.setText(getResources().getString(R.string.dash));
            ll_ped.setVisibility(View.GONE);
        }

        assert session_court_code_value != null && session_courtcode_id != null;
        if (!session_court_code_value.equals("")) {
            String selected_value = databaseAccess.getSelectedCourtCodeName(session_court_code_value, session_courtcode_id);
            court_code.setText(selected_value + " " + session_court_code_value);
        } else {
            court_code.setText(getResources().getString(R.string.dash));
            ll_court_code.setVisibility(View.GONE);
        }

        assert session_lea != null;
        if (!session_lea.equals("")) {
            lea.setText(session_lea);
        } else {
            lea.setText(getResources().getString(R.string.dash));
            ll_lea.setVisibility(View.GONE);
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
            ll_driver_first_name.setVisibility(View.GONE);
        }


        assert session_driver_middlename != null;
        if (!session_driver_middlename.equals("")) {
            driver_middlename.setText(session_driver_middlename);
        } else {
            driver_middlename.setText(getResources().getString(R.string.dash));
            ll_driver_middlename.setVisibility(View.GONE);
        }


        assert session_driver_lastname != null;
        if (!session_driver_lastname.equals("")) {
            driver_lastname.setText(session_driver_lastname);
        } else {
            driver_lastname.setText(getResources().getString(R.string.dash));
            ll_driver_lastname.setVisibility(View.GONE);
        }


        assert session_driver_suffix != null;
        if (!session_driver_suffix.equals("")) {
            driver_suffix.setText(session_driver_suffix);
        } else {
            driver_suffix.setText(getResources().getString(R.string.dash));
            ll_driver_suffix.setVisibility(View.GONE);
        }


        assert session_driver_address1 != null;
        if (!session_driver_address1.equals("")) {
            driver_address1.setText(session_driver_address1);
        } else {
            driver_address1.setText(getResources().getString(R.string.dash));
            ll_driver_address1.setVisibility(View.GONE);
        }


        assert session_driver_address2 != null;
        if (!session_driver_address2.equals("")) {
            driver_address2.setText(session_driver_address2);
        } else {
            driver_address2.setText(getResources().getString(R.string.dash));
            ll_driver_address2.setVisibility(View.GONE);
        }


        assert session_driver_zipcode != null;
        if (!session_driver_zipcode.equals("")) {
            driver_zipcode.setText(session_driver_zipcode);
        } else {
            driver_zipcode.setText(getResources().getString(R.string.dash));
            ll_driver_zipcode.setVisibility(View.GONE);
        }


        assert session_driver_license_no != null;
        if (!session_driver_license_no.equals("")) {
            driver_license_no.setText(session_driver_license_no);
        } else {
            driver_license_no.setText(getResources().getString(R.string.dash));
            ll_driver_license_no.setVisibility(View.GONE);
        }

        assert session_driver_dob != null;
        if (!session_driver_dob.equals("")) {
            driver_dob.setText(session_driver_dob);
        } else {
            driver_dob.setText(getResources().getString(R.string.dash));
            ll_driver_dob.setVisibility(View.GONE);
        }

        assert session_driver_weight != null;
        if (!session_driver_weight.equals("")) {
            driver_weight.setText(session_driver_weight);
        } else {
            driver_weight.setText(getResources().getString(R.string.dash));
            ll_driver_weight.setVisibility(View.GONE);
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
            ll_vehlimit.setVisibility(View.GONE);
        }


        assert session_safespeed != null;
        if (!session_safespeed.equals("")) {
            pre_safespeed.setText(session_safespeed);
        } else {
            pre_safespeed.setText(getResources().getString(R.string.dash));
            ll_safe_speed.setVisibility(View.GONE);
        }


        assert session_apprspeed != null;
        if (!session_apprspeed.equals("")) {
            pre_apprspeed.setText(session_apprspeed);
        } else {
            pre_apprspeed.setText(getResources().getString(R.string.dash));
            ll_appr_speed.setVisibility(View.GONE);
        }


        assert session_pfspeed != null;
        if (!session_pfspeed.equals("")) {
            pre_pfspeed.setText(session_pfspeed);
        } else {
            pre_pfspeed.setText(getResources().getString(R.string.dash));
            ll_pf_speed.setVisibility(View.GONE);
        }


        assert session_animal1 != null;
        if (!session_animal1.equals("")) {
            ll_animal1.setVisibility(View.VISIBLE);
            pre_animal1.setText(session_animal1);
        } else {
            pre_animal1.setText(getResources().getString(R.string.dash));
            ll_animal1.setVisibility(View.GONE);
        }


        assert session_animal2 != null;
        if (!session_animal2.equals("")) {
            ll_animal2.setVisibility(View.VISIBLE);
            pre_animal2.setText(session_animal2);
        } else {
            pre_animal2.setText(getResources().getString(R.string.dash));
            ll_animal2.setVisibility(View.GONE);
        }

        assert session_animal3 != null;
        if (!session_animal3.equals("")) {
            ll_animal3.setVisibility(View.VISIBLE);
            pre_animal3.setText(session_animal3);
        } else {
            pre_animal3.setText(getResources().getString(R.string.dash));
            ll_animal3.setVisibility(View.GONE);
        }


        assert session_animal4 != null;
        if (!session_animal4.equals("")) {
            ll_animal4.setVisibility(View.VISIBLE);
            pre_animal4.setText(session_animal4);
        } else {
            pre_animal4.setText(getResources().getString(R.string.dash));
            ll_animal4.setVisibility(View.GONE);
        }


        assert session_animal5 != null;
        if (!session_animal5.equals("")) {
            ll_animal5.setVisibility(View.VISIBLE);
            pre_animal5.setText(session_animal5);
        } else {
            pre_animal5.setText(getResources().getString(R.string.dash));
            ll_animal5.setVisibility(View.GONE);
        }


        assert session_animal6 != null;
        if (!session_animal6.equals("")) {
            ll_animal6.setVisibility(View.VISIBLE);
            pre_animal6.setText(session_animal6);
        } else {
            pre_animal6.setText(getResources().getString(R.string.dash));
            ll_animal6.setVisibility(View.GONE);
        }

        assert session_animal7 != null;
        if (!session_animal7.equals("")) {
            ll_animal7.setVisibility(View.VISIBLE);
            pre_animal7.setText(session_animal7);
        } else {
            pre_animal7.setText(getResources().getString(R.string.dash));
            ll_animal7.setVisibility(View.GONE);
        }

        assert session_animal8 != null;
        if (!session_animal8.equals("")) {
            ll_animal8.setVisibility(View.VISIBLE);
            pre_animal8.setText(session_animal8);
        } else {
            pre_animal8.setText(getResources().getString(R.string.dash));
            ll_animal8.setVisibility(View.GONE);
        }

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


        String session_overload = sessionManager.getVehicleSession().get(SessionManager.OVERLOAD);
        String session_policyno = sessionManager.getVehicleSession().get(SessionManager.POLICYNO);
        String session_vehno = sessionManager.getVehicleSession().get(SessionManager.VEHNO);
        String session_vehyear = sessionManager.getVehicleSession().get(SessionManager.VEHYEAR);


        assert session_overload != null;
        if (!session_overload.equals("")) {
            pre_overload.setText(session_overload);
        } else {
            pre_overload.setText(getResources().getString(R.string.dash));
            ll_overload.setVisibility(View.GONE);
        }


        assert session_policyno != null;
        if (!session_policyno.equals("")) {
            pre_policyno.setText(session_policyno);
        } else {
            pre_policyno.setText(getResources().getString(R.string.dash));
            ll_insurance_policy_no.setVisibility(View.GONE);
        }

        assert session_vehyear != null;
        if (!session_vehyear.equals("")) {
            pre_vehyear.setText(session_vehyear);
        } else {
            pre_vehyear.setText(getResources().getString(R.string.dash));
            ll_vehyear.setVisibility(View.GONE);
        }


        assert session_vehno != null;
        if (!session_vehno.equals("")) {
            pre_vehno.setText(session_vehno);
        } else {
            pre_vehno.setText(getResources().getString(R.string.dash));
            ll_veh_licno.setVisibility(View.GONE);
        }


        DatabaseAccess databaseAccess = new DatabaseAccess(ZebraPrinterActivity.this);
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
        //String session_ped = sessionManager.getHeaderSession().get(SessionManager.PED);

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
        String session_vehiclestate = sessionManager.getVehicleSession().get(SessionManager.VEHSTATE);
        String session_vehiclestateid = sessionManager.getVehicleSession().get(SessionManager.VEHSTATE_ID);


        String session_ownerfirstname = sessionManager.getVehicleSession().get(SessionManager.OWNER_FIRST_NAME);
        String session_ownermiddlename = sessionManager.getVehicleSession().get(SessionManager.OWNER_MIDDLE_NAME);
        String session_ownerlastname = sessionManager.getVehicleSession().get(SessionManager.OWNER_LAST_NAME);
        String session_owneraddress1 = sessionManager.getVehicleSession().get(SessionManager.OWNER_ADDRESS1);
        String session_owneraddress2 = sessionManager.getVehicleSession().get(SessionManager.OWNER_ADDRESS2);
        String session_zipcode = sessionManager.getVehicleSession().get(SessionManager.OWNER_ZIPCODE);

        assert session_or != null;
        if (session_or.equals("Y")) {
            ll_owner_first_name.setVisibility(View.VISIBLE);
            ll_owner_middlename.setVisibility(View.VISIBLE);
            ll_owner_lastname.setVisibility(View.VISIBLE);
            ll_owner_suffix.setVisibility(View.VISIBLE);
            ll_owner_address1.setVisibility(View.VISIBLE);
            ll_owner_address2.setVisibility(View.VISIBLE);
            ll_owner_city.setVisibility(View.VISIBLE);
            ll_owner_state.setVisibility(View.VISIBLE);
            ll_owner_zipcode.setVisibility(View.VISIBLE);

            assert session_ownerfirstname != null;
            if (!session_ownerfirstname.equals("")) {
                pre_owner_firstname.setText(session_ownerfirstname);
            } else {
                pre_owner_firstname.setText(getResources().getString(R.string.dash));
                ll_owner_first_name.setVisibility(View.GONE);
            }

            assert session_ownermiddlename != null;
            if (!session_ownermiddlename.equals("")) {
                pre_owner_middlename.setText(session_ownermiddlename);
            } else {
                pre_owner_middlename.setText(getResources().getString(R.string.dash));
                ll_owner_middlename.setVisibility(View.GONE);
            }

            assert session_ownerlastname != null;
            if (!session_ownerlastname.equals("")) {
                pre_owner_lastname.setText(session_ownerlastname);
            } else {
                pre_owner_lastname.setText(getResources().getString(R.string.dash));
                ll_owner_lastname.setVisibility(View.GONE);
            }

            assert session_owneraddress1 != null;
            if (!session_owneraddress1.equals("")) {
                pre_owner_address1.setText(session_owneraddress1);
            } else {
                pre_owner_address1.setText(getResources().getString(R.string.dash));
                ll_owner_address1.setVisibility(View.GONE);
            }


            assert session_owneraddress2 != null;
            if (!session_owneraddress2.equals("")) {
                pre_owner_address2.setText(session_owneraddress2);
            } else {
                pre_owner_address2.setText(getResources().getString(R.string.dash));
                ll_owner_address2.setVisibility(View.GONE);
            }


            assert session_zipcode != null;
            if (!session_zipcode.equals("")) {
                pre_owner_zipcode.setText(session_zipcode);
            } else {
                pre_owner_zipcode.setText(getResources().getString(R.string.dash));
                ll_owner_zipcode.setVisibility(View.GONE);
            }


            assert session_ownercity != null;
            if (!session_ownercity.equals("")) {
                String selected_value = databaseAccess.getSelectedCityName(session_ownercity, session_ownercityid);
                pre_owner_city.setText(selected_value);
            } else {
                pre_owner_city.setText(getResources().getString(R.string.dash));
                ll_owner_city.setVisibility(View.GONE);
            }

            assert session_ownersuffix != null;
            if (!session_ownersuffix.equals("")) {
                String selected_value = databaseAccess.getSuffixName(session_ownersuffixid);
                pre_owner_suffix.setText(selected_value);
            } else {
                pre_owner_suffix.setText(getResources().getString(R.string.dash));
                ll_owner_suffix.setVisibility(View.GONE);
            }

            assert session_ownerstate != null;
            if (!session_ownerstate.equals("")) {
                String selected_value = databaseAccess.getSelectedStateName(session_ownerstate, session_ownerstateid);
                pre_owner_state.setText(selected_value);
            } else {
                pre_owner_state.setText(getResources().getString(R.string.dash));
                ll_owner_state.setVisibility(View.GONE);
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

            ll_owner_first_name.setVisibility(View.GONE);
            ll_owner_middlename.setVisibility(View.GONE);
            ll_owner_lastname.setVisibility(View.GONE);
            ll_owner_suffix.setVisibility(View.GONE);
            ll_owner_address1.setVisibility(View.GONE);
            ll_owner_address2.setVisibility(View.GONE);
            ll_owner_city.setVisibility(View.GONE);
            ll_owner_state.setVisibility(View.GONE);
            ll_owner_zipcode.setVisibility(View.GONE);
        }


        if (session_vce != null) {
            if (session_vce.equals("Y")) {
                ll_vce.setVisibility(View.VISIBLE);
                pre_vce.setText(getResources().getString(R.string.yes));
            } else {
                pre_vce.setText(getResources().getString(R.string.no));
                ll_vce.setVisibility(View.GONE);
            }
        }
        if (session_vcf != null) {
            if (session_vcf.equals("Y")) {
                ll_vcf.setVisibility(View.VISIBLE);
                pre_vcf.setText(getResources().getString(R.string.yes));
            } else {
                pre_vcf.setText(getResources().getString(R.string.no));
                ll_vcf.setVisibility(View.GONE);
            }
        }
        if (session_vcg != null) {
            if (session_vcg.equals("Y")) {
                ll_vcg.setVisibility(View.VISIBLE);
                pre_vcg.setText(getResources().getString(R.string.yes));
            } else {
                pre_vcg.setText(getResources().getString(R.string.no));
                ll_vcg.setVisibility(View.GONE);
            }
        }
        if (session_vch != null) {
            if (session_vch.equals("Y")) {
                ll_vch.setVisibility(View.VISIBLE);
                pre_vch.setText(getResources().getString(R.string.yes));
            } else {
                pre_vch.setText(getResources().getString(R.string.no));
                ll_vch.setVisibility(View.GONE);
            }
        }


        if (session_vca != null) {
            if (session_vca.equals("Y")) {
                ll_vca.setVisibility(View.VISIBLE);
                pre_vca.setText(getResources().getString(R.string.yes));
            } else {
                pre_vca.setText(getResources().getString(R.string.no));
                ll_vca.setVisibility(View.GONE);
            }
        }
        if (session_vcb != null) {
            if (session_vcb.equals("Y")) {
                ll_vcb.setVisibility(View.VISIBLE);
                pre_vcb.setText(getResources().getString(R.string.yes));
            } else {
                pre_vcb.setText(getResources().getString(R.string.no));
                ll_vcb.setVisibility(View.GONE);
            }
        }
        if (session_vcc != null) {
            if (session_vcc.equals("Y")) {
                ll_vcc.setVisibility(View.VISIBLE);
                pre_vcc.setText(getResources().getString(R.string.yes));
            } else {
                pre_vcc.setText(getResources().getString(R.string.no));
                ll_vcc.setVisibility(View.GONE);
            }
        }
        if (session_vcd != null) {
            if (session_vcd.equals("Y")) {
                ll_vcd.setVisibility(View.VISIBLE);
                pre_vcd.setText(getResources().getString(R.string.yes));
            } else {
                pre_vcd.setText(getResources().getString(R.string.no));
                ll_vcd.setVisibility(View.GONE);
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


        assert session_vehiclestate != null;
        if (!session_vehiclestate.equals("")) {
            String selected_value = databaseAccess.getSelectedStateName(session_vehiclestate, session_vehiclestateid);
            pre_vehstate.setText(selected_value);
        } else {
            pre_vehstate.setText(getResources().getString(R.string.dash));
            ll_veh_state.setVisibility(View.GONE);
        }

        assert session_city != null;
        if (!session_city.equals("")) {
            String selected_value = databaseAccess.getSelectedCityName(session_city, session_cityid);
            driver_city.setText(selected_value);
        } else {
            driver_city.setText(getResources().getString(R.string.dash));
            ll_driver_city.setVisibility(View.GONE);
        }


        assert session_height != null;
        if (!session_height.equals("")) {
            String selected_value = databaseAccess.getSelectedHeight(session_height);
            driver_height.setText(selected_value);
        } else {
            driver_height.setText(getResources().getString(R.string.dash));
            ll_driver_height.setVisibility(View.GONE);
        }

        assert session_violationH != null;
        if (!session_violationH.equals("")) {
            ll_vioH.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationH, session_violationHid);
            pre_vioH.setText(selected_value);
        } else {
            pre_vioH.setText(getResources().getString(R.string.dash));
            ll_vioH.setVisibility(View.GONE);
        }


        assert session_violationG != null;
        if (!session_violationG.equals("")) {
            ll_vioG.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationG, session_violationGid);
            pre_vioG.setText(selected_value);
        } else {
            pre_vioG.setText(getResources().getString(R.string.dash));
            ll_vioG.setVisibility(View.GONE);
        }


        assert session_violationF != null;
        if (!session_violationF.equals("")) {
            ll_vioF.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationF, session_violationFid);
            pre_vioF.setText(selected_value);
        } else {
            pre_vioF.setText(getResources().getString(R.string.dash));
            ll_vioF.setVisibility(View.GONE);
        }


        assert session_violationE != null;
        if (!session_violationE.equals("")) {
            ll_vioE.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationE, session_violationEid);
            pre_vioE.setText(selected_value);
        } else {
            pre_vioE.setText(getResources().getString(R.string.dash));
            ll_vioE.setVisibility(View.GONE);
        }


        assert session_violationD != null;
        if (!session_violationD.equals("")) {
            ll_vioD.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationD, session_violationDid);
            pre_vioD.setText(selected_value);
        } else {
            pre_vioD.setText(getResources().getString(R.string.dash));
            ll_vioD.setVisibility(View.GONE);
        }

        assert session_violationC != null;
        if (!session_violationC.equals("")) {
            ll_vioC.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationC, session_violationCid);
            pre_vioC.setText(selected_value);
        } else {
            pre_vioC.setText(getResources().getString(R.string.dash));
            ll_vioC.setVisibility(View.GONE);
        }

        assert session_violationB != null;
        if (!session_violationB.equals("")) {
            ll_vioB.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationB, session_violationBid);
            pre_vioB.setText(selected_value);
        } else {
            pre_vioB.setText(getResources().getString(R.string.dash));
            ll_vioB.setVisibility(View.GONE);
        }

        assert session_violationA != null;
        if (!session_violationA.equals("")) {
            ll_vioA.setVisibility(View.VISIBLE);
            String selected_value = databaseAccess.getSelectedVIOLATIONvalue(session_violationA, session_violationAid);
            pre_vioA.setText(selected_value);
        } else {
            pre_vioA.setText(getResources().getString(R.string.dash));
            ll_vioA.setVisibility(View.GONE);
        }


        assert session_vehmodel != null;
        if (!session_vehmodel.equals("")) {
            String selected_value = databaseAccess.getSelectedVehicleModelName(session_vehmodel, session_vehmodelid);
            pre_vehmodel.setText(selected_value);
        } else {
            pre_vehmodel.setText(getResources().getString(R.string.dash));
            ll_vehmodel.setVisibility(View.GONE);
        }


        assert session_vehmake != null;
        if (!session_vehmake.equals("")) {
            String selected_value = databaseAccess.getSelectedVehicleMakeValue(session_vehmake, session_vehmakeid);
            pre_vehmake.setText(selected_value);
        } else {
            pre_vehmake.setText(getResources().getString(R.string.dash));
            ll_vehmake.setVisibility(View.GONE);
        }


        assert session_vehtype != null;
        if (!session_vehtype.equals("")) {
            String selected_value = databaseAccess.getVehTypeName(session_vehtype, session_vehtypeid);
            pre_vehtype.setText(selected_value);
        } else {
            pre_vehtype.setText(getResources().getString(R.string.dash));
            ll_vehtype.setVisibility(View.GONE);
        }


        assert session_vehcolor != null;
        if (!session_vehcolor.equals("")) {
            String selected_value = databaseAccess.getSelectedVehColorName(session_vehcolor, session_vehcolorid);
            pre_vehcolor.setText(selected_value);
        } else {
            pre_vehcolor.setText(getResources().getString(R.string.dash));
            ll_vehcolor.setVisibility(View.GONE);
        }


        assert session_vehbody != null;
        if (!session_vehbody.equals("")) {
            String selected_value = databaseAccess.getSelectedVehBodyName(session_vehbody, session_vehbodyid);
            pre_vehbody.setText(selected_value);
        } else {
            pre_vehbody.setText(getResources().getString(R.string.dash));
            ll_vehbody.setVisibility(View.GONE);
        }


        assert session_licensetype != null;
        if (!session_licensetype.equals("")) {
            String selected_value = databaseAccess.getSelectedDriversLicenseTypeName(session_licensetype, session_licensetypeid);
            license_type.setText(selected_value);
        } else {
            license_type.setText(getResources().getString(R.string.dash));
            ll_driver_license_type.setVisibility(View.GONE);
        }

        assert session_hair != null;
        if (!session_hair.equals("")) {
            String selected_value = databaseAccess.getSelectedHairName(session_hair, session_hairid);
            driver_hair.setText(selected_value);
        } else {
            driver_hair.setText(getResources().getString(R.string.dash));
            ll_driver_hair.setVisibility(View.GONE);
        }

        assert session_eyes != null;
        if (!session_eyes.equals("")) {
            String selected_value = databaseAccess.getSelectedEyesName(session_eyes, session_eyesid);
            driver_eyes.setText(selected_value);
        } else {
            driver_eyes.setText(getResources().getString(R.string.dash));
            ll_driver_eyes.setVisibility(View.GONE);
        }

        assert session_statee != null;
        if (!session_statee.equals("")) {
            String selected_value = databaseAccess.getSelectedStateName(session_statee, session_stateeid);
            driver_statee.setText(selected_value);
        } else {
            driver_statee.setText(getResources().getString(R.string.dash));
            ll_driver_statee.setVisibility(View.GONE);
        }

        assert session_state != null;
        if (!session_state.equals("")) {
            String selected_value = databaseAccess.getSelectedStateName(session_state, session_stateid);
            driver_state.setText(selected_value);
        } else {
            driver_state.setText(getResources().getString(R.string.dash));
            ll_driver_state.setVisibility(View.GONE);
        }


        assert session_ethncity != null;
        if (!session_ethncity.equals("")) {
            String selected_value = databaseAccess.getSelectedEthnicityName(session_ethncity, session_ethncityid);
            driver_ethncity.setText(selected_value);
        } else {
            driver_ethncity.setText(getResources().getString(R.string.dash));
            ll_driver_ethnicity.setVisibility(View.GONE);
        }


        assert session_race != null;
        if (!session_race.equals("")) {
            String selected_value = databaseAccess.getSelectedRaceName(session_race, session_raceid);
            driver_race.setText(selected_value);
        } else {
            driver_race.setText(getResources().getString(R.string.dash));
            ll_driver_race.setVisibility(View.GONE);
        }

        //COURT DETAILS

        String session_appeardate = sessionManager.getViolationMiscSession().get(SessionManager.APPEAR_DATE);
        String session_courttime = sessionManager.getViolationMiscSession().get(SessionManager.COURT_TIME);
        String session_nightcourt = sessionManager.getViolationMiscSession().get(SessionManager.NIGHT_COURT);
        String session_issuedate = sessionManager.getViolationMiscSession().get(SessionManager.ISSUE_DATE);
        String session_issuetime = sessionManager.getViolationMiscSession().get(SessionManager.TIME);
        String session_ampm = sessionManager.getViolationMiscSession().get(SessionManager.AMPM);
        String session_schoolzone = sessionManager.getViolationMiscSession().get(SessionManager.SCHOOL_ZONE);
        String session_violationst = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONST);
        String session_violationsttyp = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONSTTYP);
        String session_violationcst = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCST);
        String session_violationcsttyp = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCSTTYP);
        String session_offbadgeno = sessionManager.getViolationMiscSession().get(SessionManager.OFFBADGENO);
        String session_offlname = sessionManager.getViolationMiscSession().get(SessionManager.OFFLNAME);
        String session_division = sessionManager.getViolationMiscSession().get(SessionManager.DIVISION);
        String session_division_codeid = sessionManager.getViolationMiscSession().get(SessionManager.DIVISION_ID);
        String session_detail = sessionManager.getViolationMiscSession().get(SessionManager.DETAIL);
        String session_catobenotified = sessionManager.getViolationMiscSession().get(SessionManager.CA_TOBENOTIFIED);
        String session_ca_citenotsignedbydriver = sessionManager.getViolationMiscSession().get(SessionManager.CA_CITENOTSIGNEDBYDRIVER);

        assert session_issuedate != null;
        if (!session_issuedate.equals("")) {
            issuedate_txt.setText(session_issuedate);
        } else {
            issuedate_txt.setText(getResources().getString(R.string.dash));
            ll_issue_date.setVisibility(View.GONE);
        }

        assert session_issuetime != null;
        assert session_ampm != null;
        if (!session_issuetime.equals("") && !session_ampm.equals("")) {
            String am_or_pm = (session_ampm == "A")? "AM" : "PM";
            issuetime_txt.setText(session_issuetime + " " + am_or_pm);
        } else {
            issuetime_txt.setText(getResources().getString(R.string.dash));
            ll_issue_time.setVisibility(View.GONE);
        }

        assert session_schoolzone != null;
        if (session_schoolzone.equals("Y")) {
            school_zone.setText("Yes");
        } else {
            school_zone.setText(getResources().getString(R.string.dash));
            ll_schoolzone.setVisibility(View.GONE);
        }

        assert session_violationcity != null;
        if (!session_violationcity.equals("")) {
            String selected_city = databaseAccess.getSelectedViolationCityValue(session_violationcity, session_violationcityid);
            violationcity.setText(selected_city);
        } else {
            violationcity.setText(getResources().getString(R.string.dash));
            ll_violationcity.setVisibility(View.GONE);
        }

        assert session_violationst != null;
        if (!session_violationst.equals("")) {
            violationst.setText(session_violationst);
        } else {
            violationst.setText(getResources().getString(R.string.dash));
            ll_violationst.setVisibility(View.GONE);
        }

        assert session_violationsttyp != null;
        if (!session_violationsttyp.equals("")) {
            violationsttyp.setText(session_violationsttyp);
        } else {
            violationsttyp.setText(getResources().getString(R.string.dash));
            ll_violationsttyp.setVisibility(View.GONE);
        }

        assert session_violationcst != null;
        if (!session_violationcst.equals("")) {
            violationcst.setText(session_violationcst);
        } else {
            violationcst.setText(getResources().getString(R.string.dash));
            ll_violationcst.setVisibility(View.GONE);
        }

        assert session_violationcsttyp != null;
        if (!session_violationcsttyp.equals("")) {
            violationcsttyp.setText(session_violationcsttyp);
        } else {
            violationcsttyp.setText(getResources().getString(R.string.dash));
            ll_violationcsttyp.setVisibility(View.GONE);
        }

        assert session_appeardate != null;
        if (!session_appeardate.equals("")) {
            appeardate_txt.setText(session_appeardate);
        } else {
            appeardate_txt.setText(getResources().getString(R.string.dash));
            ll_appear_date.setVisibility(View.GONE);
        }

        assert session_courttime != null;
        if (!session_courttime.equals("")) {
            courttime_txt.setText(session_courttime);
        } else {
            courttime_txt.setText(getResources().getString(R.string.dash));
            ll_court_time.setVisibility(View.GONE);
        }

        assert session_offbadgeno != null;
        if (!session_offbadgeno.equals("")) {
            offbadgeno.setText(session_offbadgeno);
        } else {
            offbadgeno.setText(getResources().getString(R.string.dash));
            ll_offbadgeno.setVisibility(View.GONE);
        }

        assert session_offlname != null;
        if (!session_offlname.equals("")) {
            offlname.setText(session_offlname);
        } else {
            offlname.setText(getResources().getString(R.string.dash));
            ll_offlname.setVisibility(View.GONE);
        }

        assert session_areacode != null;
        if (!session_areacode.equals("")) {
            String selected_value = databaseAccess.getSelectedAreaCodeValue(session_areacode, session_areacodeid);
            offarea.setText(selected_value);
        } else {
            offarea.setText(getResources().getString(R.string.dash));
            ll_offarea.setVisibility(View.GONE);
        }

        assert session_division != null;
        if (!session_division.equals("")) {
            String selected_value = databaseAccess.getDivisionAreaCodeValue(session_division_codeid,session_division);
            division.setText(selected_value);
        } else {
            division.setText(getResources().getString(R.string.dash));
            ll_division.setVisibility(View.GONE);
        }

        assert session_detail != null;
        if (!session_detail.equals("")) {
            detail.setText(session_detail);
        } else {
            detail.setText(getResources().getString(R.string.dash));
            ll_detail.setVisibility(View.GONE);
        }

        assert session_nightcourt != null;
        if (!session_nightcourt.equals("")) {
            if(session_nightcourt.equals("Y")){
                nightcourt_txt.setText("Yes");
            }else{
                nightcourt_txt.setText("No");
            }
        } else {
            nightcourt_txt.setText(getResources().getString(R.string.dash));
            ll_night_court.setVisibility(View.GONE);
        }

        assert session_catobenotified != null;
        if (session_catobenotified.equals("Y")) {
            catobenotified.setText("Yes");
        } else {
            catobenotified.setText(getResources().getString(R.string.dash));
            ll_catobenotified.setVisibility(View.GONE);
        }

        assert session_ca_citenotsignedbydriver != null;
        if (session_ca_citenotsignedbydriver.equals("Y")) {
            cacitenotsignedbydriver.setText("Yes");
        } else {
            cacitenotsignedbydriver.setText(getResources().getString(R.string.dash));
            ll_cacitenotsignedbydriver.setVisibility(View.GONE);
        }

        databaseAccess.close();
    }

    //------------- //
    public void performTestWithManyJobs() {
//        if (!(ActivityCompat.checkSelfPermission(ZebraPrinterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(ZebraPrinterActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        executeTest(true);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public void executeTest(final boolean withManyJobs) {
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                connectAndSendLabel(withManyJobs);
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();
        //executeAsyncTask();
    }

    private void connectAndSendLabel(final boolean withManyJobs) {
        if (isBluetoothSelected()) {
            while(count < timeout){
                try{
                    if(count == 0){
                        helper.showLoadingDialog("Connecting...");
                        establishConnection();
                    }
                    //Toast.makeText(getApplicationContext(),""+count+": MAC:"+macAddress,Toast.LENGTH_SHORT).show();
                    Thread.sleep(1000);
                    count++;

                    /*if((count == 10) && macAddress == ""){
                        if(count == 10){
                            establishConnection();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),""+count+": MAC Id:"+macAddress,Toast.LENGTH_SHORT).show();
                        }
                    }*/

                    if(macAddress != ""){
                        connected = true;
                        count=0;
                        helper.updateLoadingDialog("Printing...");
                        startPrinting(true);
                        break;
                    }
                    else if(count == timeout-1){
                        //Toast.makeText(ZebraPrinterActivity.this,"MAC ADDRESS:"+macAddress,Toast.LENGTH_LONG).show();
                        helper.dismissLoadingDialog();
                        count=0;
                        //enablePrintButton(true);
                        break;
                    }
                } catch (InterruptedException e) {
                    //throw new RuntimeException(e);
                    //Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("connectAndSendLabel",e.getMessage());
                    helper.dismissLoadingDialog();
                    count=0;
                    //enablePrintButton(true);
                }

            }

            //connection = new BluetoothConnection(getMacAddressFieldText());
            //SettingsHelper.saveBluetoothAddress(this, getMacAddressFieldText());
        }
    }

    private void establishConnection() {
        try {
            macAddress = "";
            //Toast.makeText(ZebraPrinterActivity.this,"Inside establishConnection()",Toast.LENGTH_SHORT).show();
            BluetoothDiscoverer.findPrinters(this, new DiscoveryHandler() {
                public void foundPrinter(DiscoveredPrinter printer) {
                    macAddress = printer.address;
                    //Toast.makeText(ZebraPrinterActivity.this,"Inside foundPrinter()",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(ZebraPrinterActivity.this,"MAC ADD:"+macAddress,Toast.LENGTH_SHORT).show();
                    connection = new BluetoothConnection(macAddress);
                    SettingsHelper.saveBluetoothAddress(ZebraPrinterActivity.this, macAddress);
                    //Toast.makeText(ZebraPrinterActivity.this,"IsConnected ? : "+connection.isConnected(),Toast.LENGTH_LONG).show();
                    //I found a printer! I can use the properties of a Discovered printer (address) to make a Bluetooth Connection
                }

                public void discoveryFinished() {
                    //Toast.makeText(ZebraPrinterActivity.this,"Discovery is done ",Toast.LENGTH_LONG).show();
                    //Discovery is done
                }

                public void discoveryError(String message) {
                    //Toast.makeText(ZebraPrinterActivity.this, "Discovery Error: " + message, Toast.LENGTH_LONG).show();
                    //Error during discovery
                }
            });
        } catch (ConnectionException e) {
            e.printStackTrace();
            helper.dismissLoadingDialog();
            //enablePrintButton(true);
            //Toast.makeText(ZebraPrinterActivity.this, "Inside catch: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void startPrinting(boolean withManyJobs) {
        try {
            connection.open();

            ZebraPrinter printer = null;

            if (connection.isConnected()) {
                printer = ZebraPrinterFactory.getInstance(connection);

                if (printer != null) {
                    PrinterLanguage pl = printer.getPrinterControlLanguage();
                    if (pl != PrinterLanguage.LINE_PRINT) {
                        helper.showErrorDialogOnGuiThread("THIS PRINTER ARE NOT CPCL MODE!!");
                    } else {
                        // [self.connectivityViewController setStatus:@"Building receipt in ZPL..." withColor:[UIColor
                        // cyanColor]];
                        if (withManyJobs) {
                            sendTestLabelWithManyJobs(connection);
                        } else {
                            sendTestLabel();
                        }
                    }
                    connection.close();
                }
            }
        } catch (ConnectionException e) {
            helper.showErrorDialogOnGuiThread(e.getMessage());
            //enablePrintButton(true);
        } catch (ZebraPrinterLanguageUnknownException e) {
            helper.showErrorDialogOnGuiThread("Could not detect printer language");
            //enablePrintButton(true);
        } finally {
            helper.dismissLoadingDialog();
            //enablePrintButton(true);
        }
    }

    private void sendTestLabelWithManyJobs(Connection printerConnection) {
        try {
            sendZplReceipt(printerConnection);
        } catch (ConnectionException e) {
            helper.showErrorDialogOnGuiThread(e.getMessage());
            //enablePrintButton(true);
        }
    }

    private void sendZplReceipt(Connection printerConnection) throws ConnectionException {

       /* String footer = String.format("! 0 200 200 406 1\n" +
                "PW 575\n" +
                "TONE 0\n" +
                "SPEED 2\n" +
                "ON-FEED IGNORE\n" +
                "NO-PACE\n" +
                "BAR-SENSE\n" +
                "BT 7 0 6\n" +
                "B 128 3 30 81 88 233 123456789012\n" +
                "T 4 0 57 55 THIS ZEBRA TESTING \n" +
                "T 4 0 57 149          PRINTING \n" +
                "PRINT\n");*/
       /* String str = getString(R.string.citation_number_caps) + " : " + citation_no_txt.getText().toString().trim() + "\n"
                + getString(R.string.first_name) + " : " + driver_firstname.getText().toString().trim() + "\n"
                + getString(R.string.last_name) + " : " + driver_lastname.getText().toString().trim() + "\n"
                + getString(R.string.comm_drivers_license) + " : " + comm_drivers_license.getText().toString().trim() + "\n"
                + getString(R.string.vehyear) + " : " + pre_vehyear.getText().toString().trim() + "\n"
                + getString(R.string.vehmake) + " : " + pre_vehmake.getText().toString().trim() + "\n"
                + getString(R.string.vehmodel) + " : " + pre_vehmodel.getText().toString().trim() + "\n"
                + getString(R.string.vehmodel) + " : " + pre_vehmodel.getText().toString().trim() + "\n"
                + getString(R.string.violation_a) + " : " + pre_vioA.getText().toString().trim() + "\n"
                + getString(R.string.issue_date) + " : " + issuedate_txt.getText().toString().trim() + "\n"
                + getString(R.string.issue_time) + " : " + issuetime_txt.getText().toString().trim() + "\n"
                + getString(R.string.appeardate) + " : " + appeardate_txt.getText().toString().trim() + "\n"
                + getString(R.string.courttime) + " : " + courttime_txt.getText().toString().trim() + "\n"
                + getString(R.string.night_court) + " : " + nightcourt_txt.getText().toString().trim() + "\n";*/


        /*String citation = ll_ctlout.getVisibility() == View.VISIBLE ? "CITATION NUMBER : " + citation_no_txt.getText().toString().trim() + "\n" : "";
        String firstName = ll_driver_first_name.getVisibility() == View.VISIBLE ? "FIRST NAME : " + driver_firstname.getText().toString().trim() + "\n" : "";
        String middleName = ll_driver_middlename.getVisibility() == View.VISIBLE ? "MIDDLE NAME : " + driver_middlename.getText().toString().trim() + "\n" : "";
        String lastName = ll_driver_lastname.getVisibility() == View.VISIBLE ? "LAST NAME : " + driver_lastname.getText().toString().trim() + "\n" : "";
        String suffix = ll_driver_suffix.getVisibility() == View.VISIBLE ? "SUFFIX : " + driver_suffix.getText().toString().trim() + "\n" : "";
        String addLine1 = ll_driver_address1.getVisibility() == View.VISIBLE ? "ADDRESS 1 : " + driver_address1.getText().toString().trim() + "\n" : "";
        String addLine2 = ll_driver_address2.getVisibility() == View.VISIBLE ? "ADDRESS 2 : " + driver_address2.getText().toString().trim() + "\n" : "";
        String city = ll_driver_city.getVisibility() == View.VISIBLE ? "CITY : " + driver_city.getText().toString().trim() + "\n" : "";
        String state = ll_driver_state.getVisibility() == View.VISIBLE ? "STATE : " + driver_state.getText().toString().trim() + "\n" : "";
        String zipcode = ll_driver_zipcode.getVisibility() == View.VISIBLE ? "ZIPCODE : " + driver_zipcode.getText().toString().trim() + "\n" : "";
        String licNumber = ll_driver_license_no.getVisibility() == View.VISIBLE ? "LICENCE : " + driver_license_no.getText().toString().trim() + "\n" : "";
        String drState = ll_driver_statee.getVisibility() == View.VISIBLE ? "DRIVER STATE : " + driver_statee.getText().toString().trim() + "\n" : "";
        String drDOB = ll_driver_dob.getVisibility() == View.VISIBLE ? "DOB : " + driver_dob.getText().toString().trim() + "\n" : "";
        String drSex = ll_driver_gender.getVisibility() == View.VISIBLE ? "SEX : " + driver_sex.getText().toString().trim() + "\n" : "";
        String drHair = ll_driver_hair.getVisibility() == View.VISIBLE ? "HAIR : " + driver_hair.getText().toString().trim() + "\n" : "";
        String drEyes = ll_driver_eyes.getVisibility() == View.VISIBLE ? "EYES : " + driver_eyes.getText().toString().trim() + "\n" : "";
        String drHeight = ll_driver_height.getVisibility() == View.VISIBLE ? "HEIGHT (in) : " + driver_height.getText().toString().trim() + "\n" : "";
        String drWeight = ll_driver_weight.getVisibility() == View.VISIBLE ? "WEIGHT (lbs) : " + driver_weight.getText().toString().trim() + "\n" : "";
        String lcType = ll_driver_license_type.getVisibility() == View.VISIBLE ? "LICENSE TYPE/CLASS : " + license_type.getText().toString().trim() + "\n" : "";
        String race = ll_driver_race.getVisibility() == View.VISIBLE ? "DESCENT/RACE : " + driver_race.getText().toString().trim() + "\n" : "";
        String ethncity = ll_driver_ethnicity.getVisibility() == View.VISIBLE ? "ETHNCITY : " + driver_ethncity.getText().toString().trim() + "\n" : "";
        String commDrLicence = ll_driver_common_driver_license.getVisibility() == View.VISIBLE ? "COMM DR LICENSE : " + comm_drivers_license.getText().toString().trim() + "\n" : "";
        String ownerFname = ll_owner_first_name.getVisibility() == View.VISIBLE ? "OWNER FIRST NAME : " + pre_owner_firstname.getText().toString().trim() + "\n" : "";
        String ownerMname = ll_owner_middlename.getVisibility() == View.VISIBLE ? "OWNER MIDDLE NAME : " + pre_owner_middlename.getText().toString().trim() + "\n" : "";
        String ownerLname = ll_owner_lastname.getVisibility() == View.VISIBLE ? "OWNER LAST NAME : " + pre_owner_lastname.getText().toString().trim() + "\n" : "";
        String ownerSuffix = ll_owner_suffix.getVisibility() == View.VISIBLE ? "OWNER SUFFIX : " + pre_owner_suffix.getText().toString().trim() + "\n" : "";
        String ownerAdd1 = ll_owner_address1.getVisibility() == View.VISIBLE ? "OWNER ADDRESS 1 : " + pre_owner_address1.getText().toString().trim() + "\n" : "";
        String ownerAdd2 = ll_owner_address2.getVisibility() == View.VISIBLE ? "OWNER ADDRESS 2 : " + pre_owner_address2.getText().toString().trim() + "\n" : "";
        String ownerCity = ll_owner_city.getVisibility() == View.VISIBLE ? "OWNER CITY : " + pre_owner_city.getText().toString().trim() + "\n" : "";
        String ownerState = ll_owner_state.getVisibility() == View.VISIBLE ? "OWNER STATE : " + pre_owner_state.getText().toString().trim() + "\n" : "";
        String ownerZcode = ll_owner_zipcode.getVisibility() == View.VISIBLE ? "OWNER ZIPCODE : " + pre_owner_zipcode.getText().toString().trim() + "\n" : "";
        String vehYear = ll_vehyear.getVisibility() == View.VISIBLE ? "VEHYEAR : " + pre_vehyear.getText().toString().trim() + "\n" : "";
        String vehMake = ll_vehmake.getVisibility() == View.VISIBLE ? "VEHMAKE : " + pre_vehmake.getText().toString().trim() + "\n" : "";
        String vehModel = ll_vehmodel.getVisibility() == View.VISIBLE ? "VEHMODEL : " + pre_vehmodel.getText().toString().trim() + "\n" : "";
        String vehBody = ll_vehbody.getVisibility() == View.VISIBLE ? "VEHBODY : " + pre_vehbody.getText().toString().trim() + "\n" : "";
        String vehColor = ll_vehcolor.getVisibility() == View.VISIBLE ? "VEHCOLOR : " + pre_vehcolor.getText().toString().trim() + "\n" : "";
        String vehType = ll_vehtype.getVisibility() == View.VISIBLE ? "VEHTYPE : " + pre_vehtype.getText().toString().trim() + "\n" : "";
        String commercial = ll_vehcommercial.getVisibility() == View.VISIBLE ? "COMMERCIAL : " + pre_commercial.getText().toString().trim() + "\n" : "";
        String vehlicNo = ll_veh_licno.getVisibility() == View.VISIBLE ? "VEHLICNO : " + pre_vehno.getText().toString().trim() + "\n" : "";
        String vehicleState = ll_veh_state.getVisibility() == View.VISIBLE ? "VEHICLE STATE : " + pre_vehstate.getText().toString().trim() + "\n" : "";
        String hazardous = ll_hazardous.getVisibility() == View.VISIBLE ? "HAZARDOUS : " + pre_hazardous.getText().toString().trim() + "\n" : "";
        String overload = ll_overload.getVisibility() == View.VISIBLE ? "OVERLOAD : " + pre_overload.getText().toString().trim() + "\n" : "";
        String policy_no = ll_insurance_policy_no.getVisibility() == View.VISIBLE ? "POLICY NUMBER : " + pre_policyno.getText().toString().trim() + "\n" : "";
        String violationA1 = "";
        String violationA2 = "";
        if (pre_vioA.getText().toString().trim().length() > 32) {
            violationA1 = ll_vioA.getVisibility() == View.VISIBLE ? "VIOLATION A : " + pre_vioA.getText().toString().trim().substring(0, 32) + "\n" : "";
            violationA2 = ll_vioA.getVisibility() == View.VISIBLE ? "              " + pre_vioA.getText().toString().trim().substring(32) + "\n" : "";
        } else {
            violationA1 = ll_vioA.getVisibility() == View.VISIBLE ? "VIOLATION A : " + pre_vioA.getText().toString().trim() + "\n" : "";
        }

        String violationB1 = "";
        String violationB2 = "";
        if (pre_vioB.getText().toString().trim().length() > 32) {
            violationB1 = ll_vioB.getVisibility() == View.VISIBLE ? "VIOLATION B : " + pre_vioB.getText().toString().trim().substring(0, 32) + "\n" : "";
            violationB2 = ll_vioB.getVisibility() == View.VISIBLE ? "              " + pre_vioB.getText().toString().trim().substring(32) + "\n" : "";
        } else {
            violationB1 = ll_vioB.getVisibility() == View.VISIBLE ? "VIOLATION B : " + pre_vioB.getText().toString().trim() + "\n" : "";
        }

        String violationC1 = "";
        String violationC2 = "";
        if (pre_vioC.getText().toString().trim().length() > 32) {
            violationC1 = ll_vioC.getVisibility() == View.VISIBLE ? "VIOLATION C : " + pre_vioC.getText().toString().trim().substring(0, 32) + "\n" : "";
            violationC2 = ll_vioC.getVisibility() == View.VISIBLE ? "              " + pre_vioC.getText().toString().trim().substring(32) + "\n" : "";
        } else {
            violationC1 = ll_vioC.getVisibility() == View.VISIBLE ? "VIOLATION C : " + pre_vioC.getText().toString().trim() + "\n" : "";
        }

        String violationD1 = "";
        String violationD2 = "";
        if (pre_vioD.getText().toString().trim().length() > 32) {
            violationD1 = ll_vioD.getVisibility() == View.VISIBLE ? "VIOLATION D : " + pre_vioD.getText().toString().trim().substring(0, 32) + "\n" : "";
            violationD2 = ll_vioD.getVisibility() == View.VISIBLE ? "              " + pre_vioD.getText().toString().trim().substring(32) + "\n" : "";
        } else {
            violationD1 = ll_vioD.getVisibility() == View.VISIBLE ? "VIOLATION D : " + pre_vioD.getText().toString().trim() + "\n" : "";
        }

        String violationE1 = "";
        String violationE2 = "";
        if (pre_vioE.getText().toString().trim().length() > 32) {
            violationE1 = ll_vioE.getVisibility() == View.VISIBLE ? "VIOLATION E : " + pre_vioE.getText().toString().trim().substring(0, 32) + "\n" : "";
            violationE2 = ll_vioE.getVisibility() == View.VISIBLE ? "              " + pre_vioE.getText().toString().trim().substring(32) + "\n" : "";
        } else {
            violationE1 = ll_vioE.getVisibility() == View.VISIBLE ? "VIOLATION E : " + pre_vioE.getText().toString().trim() + "\n" : "";
        }
        String violationF1 = "";
        String violationF2 = "";
        if (pre_vioF.getText().toString().trim().length() > 32) {
            violationF1 = ll_vioF.getVisibility() == View.VISIBLE ? "VIOLATION F : " + pre_vioF.getText().toString().trim().substring(0, 32) + "\n" : "";
            violationF2 = ll_vioF.getVisibility() == View.VISIBLE ? "              " + pre_vioF.getText().toString().trim().substring(32) + "\n" : "";
        } else {
            violationF1 = ll_vioF.getVisibility() == View.VISIBLE ? "VIOLATION F : " + pre_vioF.getText().toString().trim() + "\n" : "";
        }

        String violationG1 = "";
        String violationG2 = "";
        if (pre_vioG.getText().toString().trim().length() > 32) {
            violationG1 = ll_vioG.getVisibility() == View.VISIBLE ? "VIOLATION G : " + pre_vioG.getText().toString().trim().substring(0, 32) + "\n" : "";
            violationG2 = ll_vioG.getVisibility() == View.VISIBLE ? "              " + pre_vioG.getText().toString().trim().substring(32) + "\n" : "";
        } else {
            violationG1 = ll_vioG.getVisibility() == View.VISIBLE ? "VIOLATION G : " + pre_vioG.getText().toString().trim() + "\n" : "";
        }

        String violationH1 = "";
        String violationH2 = "";
        if (pre_vioH.getText().toString().trim().length() > 32) {
            violationH1 = ll_vioH.getVisibility() == View.VISIBLE ? "VIOLATION H : " + pre_vioH.getText().toString().trim().substring(0, 32) + "\n" : "";
            violationH2 = ll_vioH.getVisibility() == View.VISIBLE ? "              " + pre_vioH.getText().toString().trim().substring(32) + "\n" : "";
        } else {
            violationH1 = ll_vioH.getVisibility() == View.VISIBLE ? "VIOLATION H : " + pre_vioH.getText().toString().trim() + "\n" : "";
        }
        String vca = ll_vca.getVisibility() == View.VISIBLE ? "VCA : " + pre_vca.getText().toString().trim() + "\n" : "";
        String vcb = ll_vcb.getVisibility() == View.VISIBLE ? "VCB : " + pre_vcb.getText().toString().trim() + "\n" : "";
        String vcc = ll_vcc.getVisibility() == View.VISIBLE ? "VCC : " + pre_vcc.getText().toString().trim() + "\n" : "";
        String vcd = ll_vcd.getVisibility() == View.VISIBLE ? "VCD : " + pre_vcd.getText().toString().trim() + "\n" : "";
        String vce = ll_vce.getVisibility() == View.VISIBLE ? "VCE : " + pre_vce.getText().toString().trim() + "\n" : "";
        String vcf = ll_vcf.getVisibility() == View.VISIBLE ? "VCF : " + pre_vcf.getText().toString().trim() + "\n" : "";
        String vcg = ll_vcg.getVisibility() == View.VISIBLE ? "VCG : " + pre_vcg.getText().toString().trim() + "\n" : "";
        String vch = ll_vch.getVisibility() == View.VISIBLE ? "VCH : " + pre_vch.getText().toString().trim() + "\n" : "";
        String vehlimit = ll_vehlimit.getVisibility() == View.VISIBLE ? "VEHLIMIT : " + pre_vehlimit.getText().toString().trim() + "\n" : "";
        String safespeed = ll_safe_speed.getVisibility() == View.VISIBLE ? "SAFE SPEED : " + pre_safespeed.getText().toString().trim() + "\n" : "";
        String apprspeed = ll_appr_speed.getVisibility() == View.VISIBLE ? "APPRSPEED : " + pre_apprspeed.getText().toString().trim() + "\n" : "";
        String pfspeed = ll_pf_speed.getVisibility() == View.VISIBLE ? "PFSPEED : " + pre_pfspeed.getText().toString().trim() + "\n" : "";
        String animal1 = ll_animal1.getVisibility() == View.VISIBLE ? "ANIMAL1 : " + pre_animal1.getText().toString().trim() + "\n" : "";
        String animal2 = ll_animal2.getVisibility() == View.VISIBLE ? "ANIMAL2 : " + pre_animal2.getText().toString().trim() + "\n" : "";
        String animal3 = ll_animal3.getVisibility() == View.VISIBLE ? "ANIMAL3 : " + pre_animal3.getText().toString().trim() + "\n" : "";
        String animal4 = ll_animal4.getVisibility() == View.VISIBLE ? "ANIMAL4 : " + pre_animal4.getText().toString().trim() + "\n" : "";
        String animal5 = ll_animal5.getVisibility() == View.VISIBLE ? "ANIMAL5 : " + pre_animal5.getText().toString().trim() + "\n" : "";
        String animal6 = ll_animal6.getVisibility() == View.VISIBLE ? "ANIMAL6 : " + pre_animal6.getText().toString().trim() + "\n" : "";
        String animal7 = ll_animal7.getVisibility() == View.VISIBLE ? "ANIMAL7 : " + pre_animal7.getText().toString().trim() + "\n" : "";
        String animal8 = ll_animal8.getVisibility() == View.VISIBLE ? "ANIMAL8 : " + pre_animal8.getText().toString().trim() + "\n" : "";
        String issueDate = ll_issue_date.getVisibility() == View.VISIBLE ? "ISSUE DATE : " + issuedate_txt.getText().toString().trim() + "\n" : "";
        String issueTime = ll_issue_time.getVisibility() == View.VISIBLE ? "ISSUE TIME : " + issuetime_txt.getText().toString().trim() + "\n" : "";
        String appearDate = ll_appear_date.getVisibility() == View.VISIBLE ? "APPEAR DATE : " + appeardate_txt.getText().toString().trim() + "\n" : "";
        String courtTime = ll_court_time.getVisibility() == View.VISIBLE ? "COURT TIME : " + courttime_txt.getText().toString().trim() + "\n" : "";
        String nightCourt = ll_night_court.getVisibility() == View.VISIBLE ? "NIGHT COURT : " + nightcourt_txt.getText().toString().trim() + "\n" : "";*/
        String citation;
        if(ll_ctlout.getVisibility() == View.VISIBLE){
            citation = "CITATIONR NUMBER : " + citation_no_txt.getText().toString().trim() + "\n";
            linecount+=1;
        }else{
            citation="";
        }

        String owner_responsibility_isyes;
        if(ll_owner_responsibility.getVisibility() == View.VISIBLE){
            owner_responsibility_isyes = "OWNER RESPONSIBILITY : " + owner_responsibility.getText().toString().trim() + "\n";
            linecount+=1;
        }else{
            owner_responsibility_isyes="";
        }

        String traffic_isyes;
        if(ll_traffic.getVisibility() == View.VISIBLE){
            traffic_isyes = "TRAFFIC : "+traffic.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            traffic_isyes="";
        }

        String ped_isyes;
        if(ll_ped.getVisibility() == View.VISIBLE){
            ped_isyes = "PED : "+ped.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            ped_isyes="";
        }

        String court_code_value;
        if(ll_court_code.getVisibility() == View.VISIBLE){
            court_code_value = "COURT CODE : "+court_code.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            court_code_value="";
        }

        String lea_value;
        if(ll_lea.getVisibility() == View.VISIBLE){
            lea_value = "LEA : "+lea.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            lea_value="";
        }

        String firstName;
        if(ll_driver_first_name.getVisibility() == View.VISIBLE){
            firstName =  "FIRST NAME : " + driver_firstname.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            firstName="";
        }

        String  middleName;
        if(ll_driver_middlename.getVisibility() == View.VISIBLE){
            middleName =  "MIDDLE NAME : " + driver_middlename.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            middleName="";
        }

        String lastName;
        if(ll_driver_lastname.getVisibility() == View.VISIBLE){
            lastName =  "LAST NAME : " + driver_lastname.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            lastName="";
        }

        String suffix;
        if(ll_driver_suffix.getVisibility() == View.VISIBLE){
            suffix =  "SUFFIX : " + driver_suffix.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            suffix="";
        }

//        String addLine1_1 = "";
//        String addLine1_2 = "";
//        if(driver_address1.getText().toString().trim().length() > 34){
//            if(ll_driver_address1.getVisibility() == View.VISIBLE){
//                addLine1_1 = "ADDRESS 1 : " + driver_address1.getText().toString().trim().substring(0,34) + "\n";
//                addLine1_2 = "            " + driver_address1.getText().toString().trim().substring(34) + "\n";
//                linecount+=2;
//            }
//            else{
//                addLine1_1="";
//                addLine1_2="";
//            }
//        }
//        else{
//            if(ll_driver_address1.getVisibility() == View.VISIBLE){
//                addLine1_1 = "ADDRESS 1 : " + driver_address1.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                addLine1_1="";
//            }
//        }


        String addLine1 = "";
        if(driver_address1.getText().toString().trim().length() > 34){
            if(ll_driver_address1.getVisibility() == View.VISIBLE){
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(driver_address1.getText().toString().trim().length() / 34.0);
                //Toast.makeText(ZebraPrinterActivity.this,""+count,Toast.LENGTH_SHORT).show();
                total_no_lines = (int)Math.ceil(count);
                //Toast.makeText(ZebraPrinterActivity.this,""+linecount,Toast.LENGTH_SHORT).show();
                for(int i = 1;i<=total_no_lines;i++){
                    //Toast.makeText(ZebraPrinterActivity.this,"i = "+i,Toast.LENGTH_SHORT).show();
                    if(i==1){
                        //Toast.makeText(ZebraPrinterActivity.this,"Inside first part",Toast.LENGTH_SHORT).show();
                        addLine1 = "ADDRESS 1 : " + driver_address1.getText().toString().trim().substring(0,34*i) + "\n";
                        linecount+=1;
                        //Toast.makeText(ZebraPrinterActivity.this,""+driver_address1.getText().toString().trim().substring(0,34*i),Toast.LENGTH_SHORT).show();
                    }
                    else if(i > 1 && i < total_no_lines){
                        //Toast.makeText(ZebraPrinterActivity.this,"Inside 2 part",Toast.LENGTH_SHORT).show();
                        addLine1 = addLine1+"            " + driver_address1.getText().toString().trim().substring(34*(i-1),34*i) + "\n";
                        linecount+=1;
                        //Toast.makeText(ZebraPrinterActivity.this,""+driver_address1.getText().toString().trim().substring(34*(i-1),34*i),Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //Toast.makeText(ZebraPrinterActivity.this,"Inside else part",Toast.LENGTH_SHORT).show();
                        addLine1 = addLine1+"            " + driver_address1.getText().toString().trim().substring(34*(i-1)) + "\n";
                        linecount+=1;
                        // Toast.makeText(ZebraPrinterActivity.this,""+driver_address1.getText().toString().trim().substring(34*(i-1)),Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else{
                addLine1 = "";
            }
        }
        else{
            if(ll_driver_address1.getVisibility() == View.VISIBLE){
                addLine1 = "ADDRESS 1 : " + driver_address1.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                addLine1="";
            }
        }


//        String addLine2_1 = "";
//        String addLine2_2 = "";
//        if(driver_address2.getText().toString().trim().length() > 34){
//            if(ll_driver_address2.getVisibility() == View.VISIBLE){
//                addLine2_1 = "ADDRESS 2 : " + driver_address2.getText().toString().trim().substring(0,34) + "\n";
//                addLine2_2 = "            " + driver_address2.getText().toString().trim().substring(34) + "\n";
//                linecount+=2;
//            }
//            else{
//                addLine2_1="";
//                addLine2_2="";
//            }
//        }
//        else{
//            if(ll_driver_address2.getVisibility() == View.VISIBLE){
//                addLine2_1 = "ADDRESS 2 : " + driver_address2.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                addLine2_1="";
//            }
//        }


        String addLine2 = "";
        if(driver_address2.getText().toString().trim().length() > 34){
            if(ll_driver_address2.getVisibility() == View.VISIBLE){
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(driver_address2.getText().toString().trim().length() / 34.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        addLine2 = "ADDRESS 2 : " + driver_address2.getText().toString().trim().substring(0,34*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        addLine2 = addLine2+"            " + driver_address2.getText().toString().trim().substring(34*(i-1),34*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        addLine2 = addLine2+"            " + driver_address2.getText().toString().trim().substring(34*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                addLine2 = "";
            }
        }
        else {
            if (ll_driver_address2.getVisibility() == View.VISIBLE) {
                addLine2 = "ADDRESS 2 : " + driver_address2.getText().toString().trim() + "\n";
                linecount += 1;
            } else {
                addLine2 = "";
            }
        }

        String city;
        if(ll_driver_city.getVisibility() == View.VISIBLE){
            city = "CITY : " + driver_city.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            city="";
        }

//        String state1="";
//        String state2="";
//        if (driver_state.getText().toString().trim().length() > 38) {
//            if(ll_driver_state.getVisibility() == View.VISIBLE){
//                state1 = "STATE : " + driver_state.getText().toString().trim().substring(0,38) + "\n";
//                state2 = "        " + driver_state.getText().toString().trim().substring(38) + "\n";
//                linecount+=2;
//            }
//            else{
//                state1="";
//                state2="";
//            }
//        }
//        else{
//            if(ll_driver_state.getVisibility() == View.VISIBLE){
//                state1 = "STATE : " + driver_state.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                state1="";
//            }
//        }

        String state="";
        if (driver_state.getText().toString().trim().length() > 38) {
            if(ll_driver_state.getVisibility() == View.VISIBLE){
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(driver_state.getText().toString().trim().length() / 38.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        state = "STATE : " + driver_state.getText().toString().trim().substring(0,38*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        state = state+"        " + driver_state.getText().toString().trim().substring(38*(i-1),38*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        state = state+"        " + driver_state.getText().toString().trim().substring(38*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                state="";
            }
        }
        else{
            if(ll_driver_state.getVisibility() == View.VISIBLE){
                state = "STATE : " + driver_state.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                state="";
            }
        }


        String zipcode;
        if(ll_driver_zipcode.getVisibility() == View.VISIBLE){
            zipcode = "ZIPCODE : " + driver_zipcode.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            zipcode="";
        }

        String licNumber;
        if(ll_driver_license_no.getVisibility() == View.VISIBLE){
            licNumber = "DRIVERS LICENCE NUMBER : " + driver_license_no.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            licNumber="";
        }

//        String drState1="";
//        String drState2="";
//        if (driver_statee.getText().toString().trim().length() > 31) {
//            if(ll_driver_statee.getVisibility() == View.VISIBLE){
//                drState1 = "DRIVER STATE : " + driver_statee.getText().toString().trim().substring(0,31) + "\n";
//                drState2 = "               " + driver_statee.getText().toString().trim().substring(31) + "\n";
//                linecount += 2;
//            }
//            else{
//                drState1 = "";
//                drState2 = "";
//            }
//        }
//        else{
//            if(ll_driver_statee.getVisibility() == View.VISIBLE){
//                drState1 = "DRIVER STATE : " + driver_statee.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                drState1="";
//            }
//        }

        String drState="";
        if (driver_statee.getText().toString().trim().length() > 31) {
            if(ll_driver_statee.getVisibility() == View.VISIBLE){
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(driver_statee.getText().toString().trim().length() / 31.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        drState = "DRIVER STATE : " + driver_statee.getText().toString().trim().substring(0,31*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        drState = drState+"               " + driver_statee.getText().toString().trim().substring(31*(i-1),31*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        drState = drState+"               " + driver_statee.getText().toString().trim().substring(31*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                drState = "";
            }
        }
        else{
            if(ll_driver_statee.getVisibility() == View.VISIBLE){
                drState = "DRIVER STATE : " + driver_statee.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                drState="";
            }
        }

        String drDOB;
        if(ll_driver_dob.getVisibility() == View.VISIBLE){
            drDOB = "DOB : " + driver_dob.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            drDOB="";
        }

        String drSex;
        if(ll_driver_gender.getVisibility() == View.VISIBLE){
            drSex = "SEX : " + driver_sex.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            drSex="";
        }

        String drHair;
        if(ll_driver_hair.getVisibility() == View.VISIBLE){
            drHair = "HAIR : " + driver_hair.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            drHair="";
        }

        String drEyes;
        if(ll_driver_eyes.getVisibility() == View.VISIBLE){
            drEyes = "EYES : " + driver_eyes.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            drEyes="";
        }

        String drHeight;
        if(ll_driver_height.getVisibility() == View.VISIBLE){
            drHeight = "HEIGHT (in) : " + driver_height.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            drHeight="";
        }

        String drWeight;
        if(ll_driver_weight.getVisibility() == View.VISIBLE){
            drWeight = "WEIGHT (lbs) : " + driver_weight.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            drWeight="";
        }

//        String lcType1="";
//        String lcType2="";
//        String lcType3="";
//        if(license_type.getText().toString().trim().length() > 25){
//            if(license_type.getText().toString().trim().length() > 50){
//                if(ll_driver_license_type.getVisibility() == View.VISIBLE){
//                    lcType1 = "DRIVERS LICENSE TYPE/CLASS : " + license_type.getText().toString().trim().substring(0,25) + "\n";
//                    lcType2 = "                     " + license_type.getText().toString().trim().substring(25,50) + "\n";
//                    lcType3 = "                     " + license_type.getText().toString().trim().substring(50) + "\n";
//                    linecount+=3;
//                }
//                else{
//                    lcType1="";
//                    lcType2="";
//                    lcType3="";
//                }
//            }
//            else{
//                if(ll_driver_license_type.getVisibility() == View.VISIBLE){
//                    lcType1 = "DRIVERS LICENSE TYPE/CLASS : " + license_type.getText().toString().trim().substring(0,25) + "\n";
//                    lcType2 = "                     " + license_type.getText().toString().trim().substring(25) + "\n";
//                    linecount+=2;
//                }
//                else{
//                    lcType1="";
//                    lcType2="";
//                }
//            }
//        }
//        else{
//            if(ll_driver_license_type.getVisibility() == View.VISIBLE){
//                lcType1 = "DRIVERS LICENSE TYPE/CLASS : " + license_type.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                lcType1="";
//            }
//        }

        String lcType="";
        if(license_type.getText().toString().trim().length() > 17){
            if(ll_driver_license_type.getVisibility() == View.VISIBLE){
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(license_type.getText().toString().trim().length() / 17.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        lcType = "DRIVERS LICENSE TYPE/CLASS : " + license_type.getText().toString().trim().substring(0,17*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        lcType = lcType+"                             " + license_type.getText().toString().trim().substring(17*(i-1),17*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        lcType = lcType+"                             " + license_type.getText().toString().trim().substring(17*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                lcType = "";
            }
        }
        else{
            if(ll_driver_license_type.getVisibility() == View.VISIBLE){
                lcType = "DRIVERS LICENSE TYPE/CLASS : " + license_type.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                lcType="";
            }
        }

        String race;
        if(ll_driver_race.getVisibility() == View.VISIBLE){
            race = "DESCENT/RACE : " + driver_race.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            race="";
        }

        String ethncity;
        if(ll_driver_ethnicity.getVisibility() == View.VISIBLE){
            ethncity = "ETHNCITY : " + driver_ethncity.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            ethncity="";
        }

        String commDrLicence;
        if(ll_driver_common_driver_license.getVisibility() == View.VISIBLE){
            commDrLicence = "COMM DRIVERS LICENSE : " + comm_drivers_license.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            commDrLicence="";
        }

        String ownerFname;
        if(ll_owner_first_name.getVisibility() == View.VISIBLE){
            ownerFname = "OWNER FIRST NAME : " + pre_owner_firstname.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            ownerFname="";
        }

        String ownerMname;
        if(ll_owner_middlename.getVisibility() == View.VISIBLE){
            ownerMname = "OWNER MIDDLE NAME : " + pre_owner_middlename.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            ownerMname="";
        }

        String ownerLname;
        if(ll_owner_lastname.getVisibility() == View.VISIBLE){
            ownerLname = "OWNER LAST NAME : " + pre_owner_lastname.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            ownerLname="";
        }

        String ownerSuffix;
        if(ll_owner_suffix.getVisibility() == View.VISIBLE){
            ownerSuffix = "OWNER SUFFIX : " + pre_owner_suffix.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            ownerSuffix="";
        }

//        String ownerAdd1_1="";
//        String ownerAdd1_2="";
//        if(pre_owner_address1.getText().toString().trim().length() > 28){
//            if(ll_owner_address1.getVisibility() == View.VISIBLE){
//                ownerAdd1_1 = "OWNER ADDRESS 1 : " + pre_owner_address1.getText().toString().trim().substring(0,28) + "\n";
//                ownerAdd1_2 = "                  " + pre_owner_address1.getText().toString().trim().substring(28) + "\n";
//                linecount+=2;
//            }
//            else{
//                ownerAdd1_1="";
//                ownerAdd1_2="";
//            }
//        }
//        else{
//            if(ll_owner_address1.getVisibility() == View.VISIBLE){
//                ownerAdd1_1 = "OWNER ADDRESS 1 : " + pre_owner_address1.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                ownerAdd1_1="";
//            }
//        }

        String ownerAdd1 = "";
        if(pre_owner_address1.getText().toString().trim().length() > 28){
            if(ll_owner_address1.getVisibility() == View.VISIBLE){
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_owner_address1.getText().toString().trim().length() / 28.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        ownerAdd1 = "OWNER ADDRESS 1 : " + pre_owner_address1.getText().toString().trim().substring(0,28*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        ownerAdd1 = ownerAdd1+"                  " + pre_owner_address1.getText().toString().trim().substring(28*(i-1),28*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        ownerAdd1 = ownerAdd1+"                  " + pre_owner_address1.getText().toString().trim().substring(28*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                ownerAdd1="";
            }
        }
        else{
            if(ll_owner_address1.getVisibility() == View.VISIBLE){
                ownerAdd1 = "OWNER ADDRESS 1 : " + pre_owner_address1.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                ownerAdd1="";
            }
        }

//        String ownerAdd2_1="";
//        String ownerAdd2_2="";
//        if(pre_owner_address2.getText().toString().trim().length() > 28){
//            if(ll_owner_address2.getVisibility() == View.VISIBLE){
//                ownerAdd2_1 = "OWNER ADDRESS 2 : " + pre_owner_address2.getText().toString().trim().substring(0,28) + "\n";
//                ownerAdd2_2 = "                  " + pre_owner_address2.getText().toString().trim().substring(28) + "\n";
//                linecount+=2;
//            }
//            else{
//                ownerAdd2_1="";
//                ownerAdd2_2="";
//            }
//        }
//        else{
//            if(ll_owner_address2.getVisibility() == View.VISIBLE){
//                ownerAdd2_1 = "OWNER ADDRESS 2 : " + pre_owner_address2.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                ownerAdd2_1="";
//            }
//        }


        String ownerAdd2="";
        if(pre_owner_address2.getText().toString().trim().length() > 28){
            if(ll_owner_address2.getVisibility() == View.VISIBLE){
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_owner_address2.getText().toString().trim().length() / 28.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        ownerAdd2 = "OWNER ADDRESS 2 : " + pre_owner_address2.getText().toString().trim().substring(0,28*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        ownerAdd2 = ownerAdd2+"                  " + pre_owner_address2.getText().toString().trim().substring(28*(i-1),28*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        ownerAdd2 = ownerAdd2+"                  " + pre_owner_address2.getText().toString().trim().substring(28*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                ownerAdd2="";
            }
        }
        else{
            if(ll_owner_address2.getVisibility() == View.VISIBLE){
                ownerAdd2 = "OWNER ADDRESS 2 : " + pre_owner_address2.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                ownerAdd2="";
            }
        }

        String ownerCity;
        if(ll_owner_city.getVisibility() == View.VISIBLE){
            ownerCity = "OWNER CITY : " + pre_owner_city.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            ownerCity="";
        }

//        String ownerState1="";
//        String ownerState2="";
//        if(pre_owner_state.getText().toString().trim().length() > 32){
//            if(ll_owner_state.getVisibility() == View.VISIBLE){
//                ownerState1 = "OWNER STATE : " + pre_owner_state.getText().toString().trim().substring(0,32) + "\n";
//                ownerState2 = "              " + pre_owner_state.getText().toString().trim().substring(32) + "\n";
//                linecount+=2;
//            }
//            else{
//                ownerState1="";
//                ownerState2="";
//            }
//        }
//        else{
//            if(ll_owner_state.getVisibility() == View.VISIBLE){
//                ownerState1 = "OWNER STATE : " + pre_owner_state.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                ownerState1="";
//            }
//        }

        String ownerState = "";
        if(pre_owner_state.getText().toString().trim().length() > 32){
            if(ll_owner_state.getVisibility() == View.VISIBLE){
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_owner_state.getText().toString().trim().length() / 32.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        ownerState = "OWNER STATE : " + pre_owner_state.getText().toString().trim().substring(0,32*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        ownerState = ownerState+"              " + pre_owner_state.getText().toString().trim().substring(32*(i-1),32*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        ownerState = ownerState+"              " + pre_owner_state.getText().toString().trim().substring(32*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                ownerState="";
            }
        }
        else{
            if(ll_owner_state.getVisibility() == View.VISIBLE){
                ownerState = "OWNER STATE : " + pre_owner_state.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                ownerState="";
            }
        }

        String ownerZcode;
        if(ll_owner_zipcode.getVisibility() == View.VISIBLE){
            ownerZcode = "OWNER ZIPCODE : " + pre_owner_zipcode.getText().toString().trim()+ "\n";
            linecount+=1;
        }
        else{
            ownerZcode="";
        }

        String vehYear;
        if(ll_vehyear.getVisibility() == View.VISIBLE){
            vehYear = "VEHYEAR : " + pre_vehyear.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vehYear="";
        }

        String vehMake;
        if(ll_vehmake.getVisibility() == View.VISIBLE){
            vehMake = "VEHMAKE : " + pre_vehmake.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vehMake="";
        }

        String vehModel;
        if(ll_vehmodel.getVisibility() == View.VISIBLE){
            vehModel = "VEHMODEL : " + pre_vehmodel.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vehModel="";
        }

        String vehBody;
        if(ll_vehbody.getVisibility() == View.VISIBLE){
            vehBody = "VEHBODY : " + pre_vehbody.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vehBody="";
        }

        String vehColor;
        if(ll_vehcolor.getVisibility() == View.VISIBLE){
            vehColor = "VEHCOLOR : " + pre_vehcolor.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vehColor="";
        }

//        String vehType1="";
//        String vehType2="";
//        if(pre_vehtype.getText().toString().trim().length() > 36){
//            if(ll_vehtype.getVisibility() == View.VISIBLE){
//                vehType1 = "VEHTYPE : " + pre_vehtype.getText().toString().trim().substring(0,36) + "\n";
//                vehType2 = "          " + pre_vehtype.getText().toString().trim().substring(36) + "\n";
//                linecount+=2;
//            }
//            else{
//                vehType1="";
//                vehType2="";
//            }
//        }
//        else{
//            if(ll_vehtype.getVisibility() == View.VISIBLE){
//                vehType1 = "VEHTYPE : " + pre_vehtype.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                vehType1="";
//            }
//        }


        String vehType="";
        if(pre_vehtype.getText().toString().trim().length() > 36){
            if(ll_vehtype.getVisibility() == View.VISIBLE){
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_vehtype.getText().toString().trim().length() / 36.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        vehType = "VEHTYPE : " + pre_vehtype.getText().toString().trim().substring(0,36*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        vehType = vehType+"          " + pre_vehtype.getText().toString().trim().substring(36*(i-1),36*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        vehType = vehType+"          " + pre_vehtype.getText().toString().trim().substring(36*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                vehType="";
            }
        }
        else{
            if(ll_vehtype.getVisibility() == View.VISIBLE){
                vehType = "VEHTYPE : " + pre_vehtype.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                vehType="";
            }
        }

        String commercial;
        if(ll_vehcommercial.getVisibility() == View.VISIBLE){
            commercial = "COMMERCIAL : " + pre_commercial.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            commercial="";
        }

        String vehlicNo;
        if(ll_veh_licno.getVisibility() == View.VISIBLE){
            vehlicNo = "VEHLICNO : " + pre_vehno.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vehlicNo="";
        }

//        String vehicleState1 = "";
//        String vehicleState2 = "";
//        if(pre_vehstate.getText().toString().trim().length() > 30){
//            if(ll_veh_state.getVisibility() == View.VISIBLE){
//                vehicleState1 = "VEHICLE STATE : " + pre_vehstate.getText().toString().trim().substring(0,30) + "\n";
//                vehicleState2 = "                " + pre_vehstate.getText().toString().trim().substring(30) + "\n";
//                linecount+=2;
//            }
//            else{
//                vehicleState1="";
//                vehicleState2="";
//            }
//        }
//        else{
//            if(ll_veh_state.getVisibility() == View.VISIBLE){
//                vehicleState1 = "VEHICLE STATE : " + pre_vehstate.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                vehicleState1="";
//            }
//        }

        String vehicleState = "";
        if(pre_vehstate.getText().toString().trim().length() > 30){
            if(ll_veh_state.getVisibility() == View.VISIBLE){
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_vehstate.getText().toString().trim().length() / 30.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        vehicleState = "VEHICLE STATE : " + pre_vehstate.getText().toString().trim().substring(0,30*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        vehicleState = vehicleState+"                " + pre_vehstate.getText().toString().trim().substring(30*(i-1),30*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        vehicleState = vehicleState+"                " + pre_vehstate.getText().toString().trim().substring(30*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                vehicleState="";
            }
        }
        else{
            if(ll_veh_state.getVisibility() == View.VISIBLE){
                vehicleState = "VEHICLE STATE : " + pre_vehstate.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                vehicleState="";
            }
        }

        String hazardous;
        if(ll_hazardous.getVisibility() == View.VISIBLE){
            hazardous = "HAZARDOUS : " + pre_hazardous.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            hazardous="";
        }

        String overload;
        if(ll_overload.getVisibility() == View.VISIBLE){
            overload = "OVERLOAD : " + pre_overload.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            overload="";
        }


//        String policy_no="";
//        if(ll_insurance_policy_no.getVisibility() == View.VISIBLE){
//            policy_no = "INSURANCE COMPANY POLICY NUMBER : " + pre_policyno.getText().toString().trim() + "\n";
//            linecount+=1;
//        }
//        else{
//            policy_no="";
//        }

        String policy_no="";
        if(pre_policyno.getText().toString().trim().length() > 12){
            if(ll_insurance_policy_no.getVisibility() == View.VISIBLE){
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_policyno.getText().toString().trim().length() / 12.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        policy_no = "INSURANCE COMPANY POLICY NUMBER : " + pre_policyno.getText().toString().trim().substring(0,12*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        policy_no = policy_no+"                                  " + pre_policyno.getText().toString().trim().substring(12*(i-1),12*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        policy_no = policy_no+"                                  " + pre_policyno.getText().toString().trim().substring(12*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                policy_no="";
            }
        }
        else{
            if(ll_insurance_policy_no.getVisibility() == View.VISIBLE){
                policy_no = "INSURANCE COMPANY POLICY NUMBER : " + pre_policyno.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                policy_no="";
            }
        }

//        String violationA1 = "";
//        String violationA2 = "";
//        String violationA3 = "";
//        String violationA4 = "";
//        if (pre_vioA.getText().toString().trim().length() > 32) {
//            if (pre_vioA.getText().toString().trim().length() > 64) {
//                if (pre_vioA.getText().toString().trim().length() > 96) {
//                    if(ll_vioA.getVisibility() == View.VISIBLE){
//                        violationA1 = "VIOLATION A : " + pre_vioA.getText().toString().trim().substring(0, 32) + "\n";
//                        violationA2 = "              " + pre_vioA.getText().toString().trim().substring(32,64) + "\n";
//                        violationA3 = "              " + pre_vioA.getText().toString().trim().substring(64,96) + "\n";
//                        violationA4 = "              " + pre_vioA.getText().toString().trim().substring(96) + "\n";
//                        linecount+=4;
//                    }
//                    else{
//                        violationA1 =  "";
//                        violationA2 =  "";
//                        violationA3 = "";
//                        violationA4 = "";
//                    }
//                }
//                else{
//                    if(ll_vioA.getVisibility() == View.VISIBLE){
//                        violationA1 = "VIOLATION A : " + pre_vioA.getText().toString().trim().substring(0, 32) + "\n";
//                        violationA2 = "              " + pre_vioA.getText().toString().trim().substring(32,64) + "\n";
//                        violationA3 = "              " + pre_vioA.getText().toString().trim().substring(64) + "\n";
//                        linecount+=3;
//                    }
//                    else{
//                        violationA1 =  "";
//                        violationA2 =  "";
//                        violationA3 = "";
//                    }
//                }
//            }
//            else{
//                if(ll_vioA.getVisibility() == View.VISIBLE){
//                    violationA1 = "VIOLATION A : " + pre_vioA.getText().toString().trim().substring(0, 32) + "\n";
//                    violationA2 = "              " + pre_vioA.getText().toString().trim().substring(32) + "\n";
//                    linecount+=2;
//                }
//                else{
//                    violationA1 =  "";
//                    violationA2 =  "";
//                }
//            }
//        } else {
//            if(ll_vioA.getVisibility() == View.VISIBLE){
//                violationA1 = "VIOLATION A : " + pre_vioA.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                violationA1 =  "";
//            }
//        }


        String violationA = "";
        if (pre_vioA.getText().toString().trim().length() > 32) {
            if (ll_vioA.getVisibility() == View.VISIBLE) {
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_vioA.getText().toString().trim().length() / 32.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        violationA = "VIOLATION A : " + pre_vioA.getText().toString().trim().substring(0,32*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        violationA = violationA+"              " + pre_vioA.getText().toString().trim().substring(32*(i-1),12*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        violationA = violationA+"              " + pre_vioA.getText().toString().trim().substring(32*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                violationA = "";
            }
        } else {
            if(ll_vioA.getVisibility() == View.VISIBLE){
                violationA = "VIOLATION A : " + pre_vioA.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                violationA =  "";
            }
        }

//        String violationB1 = "";
//        String violationB2 = "";
//        String violationB3 = "";
//        String violationB4 = "";
//        if (pre_vioB.getText().toString().trim().length() > 32) {
//            if(pre_vioB.getText().toString().trim().length() > 64){
//                if(pre_vioB.getText().toString().trim().length() > 96){
//                    if(ll_vioB.getVisibility() == View.VISIBLE){
//                        violationB1 = "VIOLATION B : " + pre_vioB.getText().toString().trim().substring(0, 32) + "\n";
//                        violationB2 = "              " + pre_vioB.getText().toString().trim().substring(32,64) + "\n";
//                        violationB3 = "              " + pre_vioB.getText().toString().trim().substring(64,96) + "\n";
//                        violationB4 = "              " + pre_vioB.getText().toString().trim().substring(96) + "\n";
//                        linecount+=4;
//                    }
//                    else{
//                        violationB1 =  "";
//                        violationB2 =  "";
//                        violationB3 = "";
//                        violationB4 = "";
//                    }
//                }
//                else{
//                    if(ll_vioB.getVisibility() == View.VISIBLE){
//                        violationB1 = "VIOLATION B : " + pre_vioB.getText().toString().trim().substring(0, 32) + "\n";
//                        violationB2 = "              " + pre_vioB.getText().toString().trim().substring(32,64) + "\n";
//                        violationB3 = "              " + pre_vioB.getText().toString().trim().substring(64) + "\n";
//                        linecount+=3;
//                    }
//                    else{
//                        violationB1 =  "";
//                        violationB2 =  "";
//                        violationB3 = "";
//                    }
//                }
//            }else{
//                if(ll_vioB.getVisibility() == View.VISIBLE){
//                    violationB1 = "VIOLATION B : " + pre_vioB.getText().toString().trim().substring(0, 32) + "\n";
//                    violationB2 = "              " + pre_vioB.getText().toString().trim().substring(32) + "\n";
//                    linecount+=2;
//                }
//                else{
//                    violationB1 =  "";
//                    violationB2 =  "";
//                }
//            }
//        } else {
//            if(ll_vioB.getVisibility() == View.VISIBLE){
//                violationB1 = "VIOLATION B : " + pre_vioB.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                violationB1 =  "";
//            }
//        }

        String violationB = "";
        if (pre_vioB.getText().toString().trim().length() > 32) {
            if (ll_vioB.getVisibility() == View.VISIBLE) {
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_vioB.getText().toString().trim().length() / 32.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        violationB = "VIOLATION B : " + pre_vioB.getText().toString().trim().substring(0,32*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        violationB = violationB+"              " + pre_vioB.getText().toString().trim().substring(32*(i-1),12*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        violationB = violationB+"              " + pre_vioB.getText().toString().trim().substring(32*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                violationB = "";
            }
        } else {
            if(ll_vioB.getVisibility() == View.VISIBLE){
                violationB = "VIOLATION B : " + pre_vioB.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                violationB =  "";
            }
        }

//        String violationC1 = "";
//        String violationC2 = "";
//        String violationC3 = "";
//        String violationC4 = "";
//        if (pre_vioC.getText().toString().trim().length() > 32) {
//            if (pre_vioC.getText().toString().trim().length() > 64) {
//                if (pre_vioC.getText().toString().trim().length() > 96) {
//                    if(ll_vioC.getVisibility() == View.VISIBLE){
//                        violationC1 = "VIOLATION C : " + pre_vioC.getText().toString().trim().substring(0, 32) + "\n";
//                        violationC2 =  "              " + pre_vioC.getText().toString().trim().substring(32,64) + "\n";
//                        violationC3 =  "              " + pre_vioC.getText().toString().trim().substring(64,96) + "\n";
//                        violationC4 =  "              " + pre_vioC.getText().toString().trim().substring(96) + "\n";
//                        linecount+=4;
//                    }
//                    else{
//                        violationC1 =  "";
//                        violationC2 =  "";
//                        violationC3 =  "";
//                        violationC4 =  "";
//                    }
//                }
//                else{
//                    if(ll_vioC.getVisibility() == View.VISIBLE){
//                        violationC1 = "VIOLATION C : " + pre_vioC.getText().toString().trim().substring(0, 32) + "\n";
//                        violationC2 =  "              " + pre_vioC.getText().toString().trim().substring(32,64) + "\n";
//                        violationC3 =  "              " + pre_vioC.getText().toString().trim().substring(64) + "\n";
//                        linecount+=3;
//                    }
//                    else{
//                        violationC1 =  "";
//                        violationC2 =  "";
//                        violationC3 =  "";
//                    }
//                }
//            }
//            else{
//                if(ll_vioC.getVisibility() == View.VISIBLE){
//                    violationC1 = "VIOLATION C : " + pre_vioC.getText().toString().trim().substring(0, 32) + "\n";
//                    violationC2 =  "              " + pre_vioC.getText().toString().trim().substring(32) + "\n";
//                    linecount+=2;
//                }
//                else{
//                    violationC1 =  "";
//                    violationC2 =  "";
//                }
//            }
//        } else {
//            if(ll_vioC.getVisibility() == View.VISIBLE){
//                violationC1 = "VIOLATION C : " + pre_vioC.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                violationC1 =  "";
//            }
//        }

        String violationC = "";
        if (pre_vioC.getText().toString().trim().length() > 32) {
            if (ll_vioC.getVisibility() == View.VISIBLE) {
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_vioC.getText().toString().trim().length() / 32.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        violationC = "VIOLATION C : " + pre_vioC.getText().toString().trim().substring(0,32*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        violationC = violationC+"              " + pre_vioC.getText().toString().trim().substring(32*(i-1),12*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        violationC = violationC+"              " + pre_vioC.getText().toString().trim().substring(32*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                violationC = "";
            }
        } else {
            if(ll_vioC.getVisibility() == View.VISIBLE){
                violationC = "VIOLATION C : " + pre_vioC.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                violationC =  "";
            }
        }

//        String violationD1 = "";
//        String violationD2 = "";
//        String violationD3 = "";
//        String violationD4 = "";
//        if (pre_vioD.getText().toString().trim().length() > 32) {
//            if(pre_vioD.getText().toString().trim().length() > 64){
//                if(pre_vioD.getText().toString().trim().length() > 96){
//                    if(ll_vioD.getVisibility() == View.VISIBLE){
//                        violationD1 = "VIOLATION D : " + pre_vioD.getText().toString().trim().substring(0, 32) + "\n";
//                        violationD2 =  "              " + pre_vioD.getText().toString().trim().substring(32,64) + "\n";
//                        violationD3 =  "              " + pre_vioD.getText().toString().trim().substring(64,96) + "\n";
//                        violationD4 =  "              " + pre_vioD.getText().toString().trim().substring(96) + "\n";
//                        linecount+=4;
//                    }
//                    else{
//                        violationD1 =  "";
//                        violationD2 =  "";
//                        violationD3 =  "";
//                        violationD4 =  "";
//                    }
//                }
//                else{
//                    if(ll_vioD.getVisibility() == View.VISIBLE){
//                        violationD1 = "VIOLATION D : " + pre_vioD.getText().toString().trim().substring(0, 32) + "\n";
//                        violationD2 =  "              " + pre_vioD.getText().toString().trim().substring(32,64) + "\n";
//                        violationD3 =  "              " + pre_vioD.getText().toString().trim().substring(64) + "\n";
//                        linecount+=3;
//                    }
//                    else{
//                        violationD1 =  "";
//                        violationD2 =  "";
//                        violationD3 =  "";
//                    }
//                }
//            }
//            else{
//                if(ll_vioD.getVisibility() == View.VISIBLE){
//                    violationD1 = "VIOLATION D : " + pre_vioD.getText().toString().trim().substring(0, 32) + "\n";
//                    violationD2 =  "              " + pre_vioD.getText().toString().trim().substring(32) + "\n";
//                    linecount+=2;
//                }
//                else{
//                    violationD1 =  "";
//                    violationD2 =  "";
//                }
//            }
//        } else {
//            if(ll_vioD.getVisibility() == View.VISIBLE){
//                violationD1 = "VIOLATION D : " + pre_vioD.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                violationD1 =  "";
//            }
//        }

        String violationD = "";
        if (pre_vioD.getText().toString().trim().length() > 32) {
            if (ll_vioD.getVisibility() == View.VISIBLE) {
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_vioD.getText().toString().trim().length() / 32.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        violationD = "VIOLATION D : " + pre_vioD.getText().toString().trim().substring(0,32*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        violationD = violationD+"              " + pre_vioD.getText().toString().trim().substring(32*(i-1),12*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        violationD = violationD+"              " + pre_vioD.getText().toString().trim().substring(32*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                violationD = "";
            }
        } else {
            if(ll_vioD.getVisibility() == View.VISIBLE){
                violationD = "VIOLATION D : " + pre_vioD.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                violationD =  "";
            }
        }

//        String violationE1 = "";
//        String violationE2 = "";
//        String violationE3 = "";
//        String violationE4 = "";
//        if (pre_vioE.getText().toString().trim().length() > 32) {
//            if(pre_vioE.getText().toString().trim().length() > 64){
//                if(pre_vioE.getText().toString().trim().length() > 96){
//                    if(ll_vioE.getVisibility() == View.VISIBLE){
//                        violationE1 ="VIOLATION E : " + pre_vioE.getText().toString().trim().substring(0, 32) + "\n";
//                        violationE2 = "              " + pre_vioE.getText().toString().trim().substring(32,64) + "\n";
//                        violationE3 = "              " + pre_vioE.getText().toString().trim().substring(64,96) + "\n";
//                        violationE4 = "              " + pre_vioE.getText().toString().trim().substring(96) + "\n";
//                        linecount+=4;
//                    }
//                    else{
//                        violationE1 =  "";
//                        violationE2 =  "";
//                        violationE3 =  "";
//                        violationE4 =  "";
//                    }
//                }
//                else{
//                    if(ll_vioE.getVisibility() == View.VISIBLE){
//                        violationE1 ="VIOLATION E : " + pre_vioE.getText().toString().trim().substring(0, 32) + "\n";
//                        violationE2 = "              " + pre_vioE.getText().toString().trim().substring(32,64) + "\n";
//                        violationE3 = "              " + pre_vioE.getText().toString().trim().substring(64) + "\n";
//                        linecount+=3;
//                    }
//                    else{
//                        violationE1 =  "";
//                        violationE2 =  "";
//                        violationE3 =  "";
//                    }
//                }
//            }
//            else{
//                if(ll_vioE.getVisibility() == View.VISIBLE){
//                    violationE1 ="VIOLATION E : " + pre_vioE.getText().toString().trim().substring(0, 32) + "\n";
//                    violationE2 = "              " + pre_vioE.getText().toString().trim().substring(32) + "\n";
//                    linecount+=2;
//                }
//                else{
//                    violationE1 =  "";
//                    violationE2 =  "";
//                }
//            }
//        } else {
//            if(ll_vioE.getVisibility() == View.VISIBLE){
//                violationE1 = "VIOLATION E : " + pre_vioE.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                violationE1 =  "";
//            }
//        }

        String violationE = "";
        if (pre_vioE.getText().toString().trim().length() > 32) {
            if (ll_vioE.getVisibility() == View.VISIBLE) {
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_vioE.getText().toString().trim().length() / 32.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        violationE = "VIOLATION E : " + pre_vioE.getText().toString().trim().substring(0,32*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        violationE = violationE+"              " + pre_vioE.getText().toString().trim().substring(32*(i-1),12*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        violationE = violationE+"              " + pre_vioE.getText().toString().trim().substring(32*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                violationE = "";
            }
        } else {
            if(ll_vioE.getVisibility() == View.VISIBLE){
                violationE = "VIOLATION E : " + pre_vioE.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                violationE =  "";
            }
        }

//        String violationF1 = "";
//        String violationF2 = "";
//        String violationF3 = "";
//        String violationF4 = "";
//        if (pre_vioF.getText().toString().trim().length() > 32) {
//            if(pre_vioF.getText().toString().trim().length() > 64){
//                if(pre_vioF.getText().toString().trim().length() > 96){
//                    if(ll_vioF.getVisibility() == View.VISIBLE){
//                        violationF1 = "VIOLATION F : " + pre_vioF.getText().toString().trim().substring(0, 32) + "\n";
//                        violationF2 = "              " + pre_vioF.getText().toString().trim().substring(32,64) + "\n";
//                        violationF3 = "              " + pre_vioF.getText().toString().trim().substring(64,96) + "\n";
//                        violationF4 = "              " + pre_vioF.getText().toString().trim().substring(96) + "\n";
//                        linecount+=4;
//                    }
//                    else{
//                        violationF1 =  "";
//                        violationF2 =  "";
//                        violationF3 =  "";
//                        violationF4 =  "";
//                    }
//                }
//                else{
//                    if(ll_vioF.getVisibility() == View.VISIBLE){
//                        violationF1 = "VIOLATION F : " + pre_vioF.getText().toString().trim().substring(0, 32) + "\n";
//                        violationF2 = "              " + pre_vioF.getText().toString().trim().substring(32,64) + "\n";
//                        violationF3 = "              " + pre_vioF.getText().toString().trim().substring(64) + "\n";
//                        linecount+=3;
//                    }
//                    else{
//                        violationF1 =  "";
//                        violationF2 =  "";
//                        violationF3 =  "";
//                    }
//                }
//            }
//            else{
//                if(ll_vioF.getVisibility() == View.VISIBLE){
//                    violationF1 = "VIOLATION F : " + pre_vioF.getText().toString().trim().substring(0, 32) + "\n";
//                    violationF2 = "              " + pre_vioF.getText().toString().trim().substring(32) + "\n";
//                    linecount+=2;
//                }
//                else{
//                    violationF1 =  "";
//                    violationF2 =  "";
//                }
//            }
//        } else {
//            if(ll_vioF.getVisibility() == View.VISIBLE){
//                violationF1 = "VIOLATION F : " + pre_vioF.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                violationF1 =  "";
//            }
//        }

        String violationF = "";
        if (pre_vioF.getText().toString().trim().length() > 32) {
            if (ll_vioF.getVisibility() == View.VISIBLE) {
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_vioF.getText().toString().trim().length() / 32.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        violationF = "VIOLATION F : " + pre_vioF.getText().toString().trim().substring(0,32*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        violationF = violationF+"              " + pre_vioF.getText().toString().trim().substring(32*(i-1),12*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        violationF = violationF+"              " + pre_vioF.getText().toString().trim().substring(32*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                violationF = "";
            }
        } else {
            if(ll_vioF.getVisibility() == View.VISIBLE){
                violationF = "VIOLATION F : " + pre_vioF.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                violationF =  "";
            }
        }

//        String violationG1 = "";
//        String violationG2 = "";
//        String violationG3 = "";
//        String violationG4 = "";
//        if (pre_vioG.getText().toString().trim().length() > 32) {
//            if(pre_vioG.getText().toString().trim().length() > 64){
//                if(pre_vioG.getText().toString().trim().length() > 96){
//                    if(ll_vioG.getVisibility() == View.VISIBLE){
//                        violationG1 = "VIOLATION G : " + pre_vioG.getText().toString().trim().substring(0, 32) + "\n";
//                        violationG2 = "              " + pre_vioG.getText().toString().trim().substring(32,64) + "\n";
//                        violationG3 = "              " + pre_vioG.getText().toString().trim().substring(64,96) + "\n";
//                        violationG4 = "              " + pre_vioG.getText().toString().trim().substring(96) + "\n";
//                        linecount+=4;
//                    }
//                    else{
//                        violationG1 =  "";
//                        violationG2 =  "";
//                        violationG3 =  "";
//                        violationG4 =  "";
//                    }
//                }
//                else{
//                    if(ll_vioG.getVisibility() == View.VISIBLE){
//                        violationG1 = "VIOLATION G : " + pre_vioG.getText().toString().trim().substring(0, 32) + "\n";
//                        violationG2 = "              " + pre_vioG.getText().toString().trim().substring(32,64) + "\n";
//                        violationG3 = "              " + pre_vioG.getText().toString().trim().substring(64) + "\n";
//                        linecount+=3;
//                    }
//                    else{
//                        violationG1 =  "";
//                        violationG2 =  "";
//                        violationG3 =  "";
//                    }
//                }
//            }
//            else{
//                if(ll_vioG.getVisibility() == View.VISIBLE){
//                    violationG1 = "VIOLATION G : " + pre_vioG.getText().toString().trim().substring(0, 32) + "\n";
//                    violationG2 = "              " + pre_vioG.getText().toString().trim().substring(32) + "\n";
//                    linecount+=2;
//                }
//                else{
//                    violationG1 =  "";
//                    violationG2 =  "";
//                }
//            }
//        } else {
//            if(ll_vioG.getVisibility() == View.VISIBLE){
//                violationG1 = "VIOLATION G : " + pre_vioG.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                violationG1 =  "";
//            }
//        }

        String violationG = "";
        if (pre_vioG.getText().toString().trim().length() > 32) {
            if (ll_vioG.getVisibility() == View.VISIBLE) {
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_vioG.getText().toString().trim().length() / 32.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        violationG = "VIOLATION G : " + pre_vioG.getText().toString().trim().substring(0,32*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        violationG = violationG+"              " + pre_vioG.getText().toString().trim().substring(32*(i-1),12*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        violationG = violationG+"              " + pre_vioG.getText().toString().trim().substring(32*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                violationG = "";
            }
        } else {
            if(ll_vioG.getVisibility() == View.VISIBLE){
                violationG = "VIOLATION G : " + pre_vioG.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                violationG =  "";
            }
        }

//        String violationH1 = "";
//        String violationH2 = "";
//        String violationH3 = "";
//        String violationH4 = "";
//        if (pre_vioH.getText().toString().trim().length() > 32) {
//            if(pre_vioH.getText().toString().trim().length() > 64){
//                if(pre_vioH.getText().toString().trim().length() > 96){
//                    if(ll_vioH.getVisibility() == View.VISIBLE){
//                        violationH1 = "VIOLATION H : " + pre_vioH.getText().toString().trim().substring(0, 32) + "\n";
//                        violationH2 ="              " + pre_vioH.getText().toString().trim().substring(32,64) + "\n";
//                        violationH3 ="              " + pre_vioH.getText().toString().trim().substring(64,96) + "\n";
//                        violationH4 ="              " + pre_vioH.getText().toString().trim().substring(96) + "\n";
//                        linecount+=4;
//                    }
//                    else{
//                        violationH1 =  "";
//                        violationH2 =  "";
//                        violationH3 =  "";
//                        violationH4 =  "";
//                    }
//                }
//                else{
//                    if(ll_vioH.getVisibility() == View.VISIBLE){
//                        violationH1 = "VIOLATION H : " + pre_vioH.getText().toString().trim().substring(0, 32) + "\n";
//                        violationH2 ="              " + pre_vioH.getText().toString().trim().substring(32,64) + "\n";
//                        violationH3 ="              " + pre_vioH.getText().toString().trim().substring(64) + "\n";
//                        linecount+=3;
//                    }
//                    else{
//                        violationH1 =  "";
//                        violationH2 =  "";
//                        violationH3 =  "";
//                    }
//                }
//            }
//            else{
//                if(ll_vioH.getVisibility() == View.VISIBLE){
//                    violationH1 = "VIOLATION H : " + pre_vioH.getText().toString().trim().substring(0, 32) + "\n";
//                    violationH2 ="              " + pre_vioH.getText().toString().trim().substring(32) + "\n";
//                    linecount+=2;
//                }
//                else{
//                    violationH1 =  "";
//                    violationH2 =  "";
//                }
//            }
//        } else {
//            if(ll_vioH.getVisibility() == View.VISIBLE){
//                violationH1 = "VIOLATION H : " + pre_vioH.getText().toString().trim() + "\n";
//                linecount+=1;
//            }
//            else{
//                violationH1 =  "";
//            }
//        }

        String violationH = "";
        if (pre_vioH.getText().toString().trim().length() > 32) {
            if (ll_vioH.getVisibility() == View.VISIBLE) {
                Double count = 0.0;
                int total_no_lines = 0;
                count = Double.valueOf(pre_vioH.getText().toString().trim().length() / 32.0);
                total_no_lines = (int)Math.ceil(count);
                for(int i = 1;i<=total_no_lines;i++){
                    if(i==1){
                        violationH = "VIOLATION H : " + pre_vioH.getText().toString().trim().substring(0,32*i) + "\n";
                        linecount+=1;
                    }
                    else if(i > 1 && i < total_no_lines){
                        violationH = violationH+"              " + pre_vioH.getText().toString().trim().substring(32*(i-1),12*i) + "\n";
                        linecount+=1;
                    }
                    else{
                        violationH = violationH+"              " + pre_vioH.getText().toString().trim().substring(32*(i-1)) + "\n";
                        linecount+=1;
                    }
                }
            }
            else{
                violationH = "";
            }
        } else {
            if(ll_vioH.getVisibility() == View.VISIBLE){
                violationH = "VIOLATION H : " + pre_vioH.getText().toString().trim() + "\n";
                linecount+=1;
            }
            else{
                violationH =  "";
            }
        }

        String vca;
        if(ll_vca.getVisibility() == View.VISIBLE){
            vca = "VCA : " + pre_vca.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vca="";
        }

        String vcb;
        if(ll_vcb.getVisibility() == View.VISIBLE){
            vcb = "VCB : " + pre_vcb.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vcb="";
        }

        String vcc;
        if(ll_vcc.getVisibility() == View.VISIBLE){
            vcc = "VCC : " + pre_vcc.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vcc="";
        }

        String vcd;
        if(ll_vcd.getVisibility() == View.VISIBLE){
            vcd = "VCD : " + pre_vcd.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vcd="";
        }

        String vce;
        if(ll_vce.getVisibility() == View.VISIBLE){
            vce = "VCE : " + pre_vce.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vce="";
        }

        String vcf;
        if(ll_vcf.getVisibility() == View.VISIBLE){
            vcf = "VCF : " + pre_vcf.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vcf="";
        }

        String vcg;
        if(ll_vcg.getVisibility() == View.VISIBLE){
            vcg = "VCG : " + pre_vcg.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vcg="";
        }

        String vch;
        if(ll_vch.getVisibility() == View.VISIBLE){
            vch = "VCH : " + pre_vch.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vch="";
        }

        String vehlimit;
        if(ll_vehlimit.getVisibility() == View.VISIBLE){
            vehlimit = "VEHLIMIT : " + pre_vehlimit.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            vehlimit="";
        }

        String safespeed;
        if(ll_safe_speed.getVisibility() == View.VISIBLE){
            safespeed = "SAFE SPEED : " + pre_safespeed.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            safespeed="";
        }

        String apprspeed;
        if(ll_appr_speed.getVisibility() == View.VISIBLE){
            apprspeed = "APPRSPEED : " + pre_apprspeed.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            apprspeed="";
        }

        String pfspeed;
        if(ll_pf_speed.getVisibility() == View.VISIBLE){
            pfspeed = "PFSPEED : " + pre_pfspeed.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            pfspeed="";
        }

        String animal1;
        if(ll_animal1.getVisibility() == View.VISIBLE){
            animal1 = "ANIMAL1 : " + pre_animal1.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            animal1="";
        }

        String animal2;
        if(ll_animal2.getVisibility() == View.VISIBLE){
            animal2 = "ANIMAL2 : " + pre_animal2.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            animal2="";
        }

        String animal3;
        if(ll_animal3.getVisibility() == View.VISIBLE){
            animal3 = "ANIMAL3 : " + pre_animal3.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            animal3="";
        }

        String animal4;
        if(ll_animal4.getVisibility() == View.VISIBLE){
            animal4 = "ANIMAL4 : " + pre_animal4.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            animal4="";
        }

        String animal5;
        if(ll_animal5.getVisibility() == View.VISIBLE){
            animal5 = "ANIMAL5 : " + pre_animal5.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            animal5="";
        }

        String animal6;
        if(ll_animal6.getVisibility() == View.VISIBLE){
            animal6 = "ANIMAL6 : " + pre_animal6.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            animal6="";
        }

        String animal7;
        if(ll_animal7.getVisibility() == View.VISIBLE){
            animal7 = "ANIMAL7 : " + pre_animal7.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            animal7="";
        }

        String animal8;
        if(ll_animal8.getVisibility() == View.VISIBLE){
            animal8 = "ANIMAL8 : " + pre_animal8.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            animal8="";
        }

        String issueDate;
        if(ll_issue_date.getVisibility() == View.VISIBLE){
            issueDate = "ISSUE DATE : " + issuedate_txt.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            issueDate="";
        }

        String issueTime;
        if(ll_issue_time.getVisibility() == View.VISIBLE){
            issueTime = "ISSUE TIME : " + issuetime_txt.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            issueTime="";
        }

        String school_zone_txt;
        if(ll_schoolzone.getVisibility() == View.VISIBLE){
            school_zone_txt = "SCHOOL ZONE : " + school_zone.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            school_zone_txt="";
        }

        String violation_city_txt;
        if(ll_violationcity.getVisibility() == View.VISIBLE){
            violation_city_txt = "VIOLATIONCITY : " + violationcity.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            violation_city_txt="";
        }

        String violation_st_txt;
        if(ll_violationst.getVisibility() == View.VISIBLE){
            violation_st_txt = "VIOLATIONST : " + violationst.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            violation_st_txt="";
        }

        String violation_sttyp_txt;
        if(ll_violationsttyp.getVisibility() == View.VISIBLE){
            violation_sttyp_txt = "VIOLATIONSTTYP : " + violationsttyp.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            violation_sttyp_txt="";
        }

        String violation_cst_txt;
        if(ll_violationcst.getVisibility() == View.VISIBLE){
            violation_cst_txt = "VIOLATIONCST : " + violationcst.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            violation_cst_txt="";
        }

        String violation_csttyp_txt;
        if(ll_violationcsttyp.getVisibility() == View.VISIBLE){
            violation_csttyp_txt = "VIOLATIONCSTTYP : " + violationcsttyp.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            violation_csttyp_txt="";
        }

        String appearDate;
        if(ll_appear_date.getVisibility() == View.VISIBLE){
            appearDate = "APPEAR DATE : " + appeardate_txt.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            appearDate="";
        }

        String courtTime;
        if(ll_court_time.getVisibility() == View.VISIBLE){
            courtTime = "COURT TIME : " + courttime_txt.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            courtTime="";
        }

        String offbadge_no_txt;
        if(ll_offbadgeno.getVisibility() == View.VISIBLE){
            offbadge_no_txt = "OFFBADGENO : " + offbadgeno.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            offbadge_no_txt="";
        }

        String offlname_txt;
        if(ll_offlname.getVisibility() == View.VISIBLE){
            offlname_txt = "OFFLNAME : " + offlname.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            offlname_txt="";
        }

        String offarea_txt;
        if(ll_offarea.getVisibility() == View.VISIBLE){
            offarea_txt = "OFFICER AREA : " + offarea.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            offarea_txt="";
        }

        String division_txt;
        if(ll_division.getVisibility() == View.VISIBLE){
            division_txt = "DIVISION : " + division.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            division_txt="";
        }

        String detail_txt;
        if(ll_detail.getVisibility() == View.VISIBLE){
            detail_txt = "DETAIL : " + detail.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            detail_txt="";
        }

        String nightCourt;
        if(ll_night_court.getVisibility() == View.VISIBLE){
            nightCourt = "NIGHT COURT : " + nightcourt_txt.getText().toString().trim() + "\n";
            //Log.d("Night Court:",nightCourt);
            linecount+=1;
        }
        else{
            nightCourt="";
        }

        String catobenotified_txt;
        if(ll_catobenotified.getVisibility() == View.VISIBLE){
            catobenotified_txt = "CATOBENOTIFIED : " + catobenotified.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            catobenotified_txt="";
        }

        String cacitenotsignedbydriver_txt;
        if(ll_cacitenotsignedbydriver.getVisibility() == View.VISIBLE){
            cacitenotsignedbydriver_txt = "CACiteNotSignedByDriver : " + cacitenotsignedbydriver.getText().toString().trim() + "\n";
            linecount+=1;
        }
        else{
            cacitenotsignedbydriver_txt="";
        }


       /* StringBuffer sb = new StringBuffer("CITATION NUMBER : ");
        sb.append(citation_no_txt.getText().toString().trim()).append("\n")
                .append("FIRST NAME : ").append(driver_firstname.getText().toString().trim()).append("\n");*/

        total_height = (linecount * line_height)+48; //48 is added just to add height of line break(------------------)

        String footer = String.format("! 0 200 200 "+total_height+" 1\n" +
                "ML 48\n" +
                "T 7 1 10 20 " +
                citation +
                owner_responsibility_isyes+
                traffic_isyes+
                ped_isyes+
                court_code_value+
                lea_value+
                firstName +
                middleName +
                lastName +
                suffix +
                //addLine1_1 +
                //addLine1_2 +
                addLine1+
                addLine2+
//                addLine2_1 +
//                addLine2_2 +
                city +
//                state1 +
//                state2 +
                state +
                zipcode +
                licNumber +
                drState+
//                drState1 +
//                drState2 +
                drDOB +
                drSex +
                drHair +
                drEyes +
                drHeight +
                drWeight +
                lcType+
//                lcType1 +
//                lcType2 +
//                lcType3 +
                race +
                ethncity +
                commDrLicence +
                ownerFname +
                ownerMname +
                ownerLname +
                ownerSuffix +
                ownerAdd1+
//                ownerAdd1_1 +
//                ownerAdd1_2 +
//                ownerAdd2_1 +
//                ownerAdd2_2 +
                ownerAdd2+
                ownerCity +
//                ownerState1 +
//                ownerState2 +
                ownerState+
                ownerZcode +
                vehYear +
                vehMake +
                vehModel +
                vehBody +
                vehColor +
//                vehType1 +
//                vehType2 +
                vehType+
                commercial +
                vehlicNo +
                vehicleState+
//                vehicleState1 +
//                vehicleState2 +
                hazardous +
                overload +
                policy_no +
//                violationA1 +
//                violationA2 +
//                violationA3 +
//                violationA4 +
                violationA+
                violationB+
//                violationB1 +
//                violationB2 +
//                violationB3 +
//                violationB4 +
//                violationC1 +
//                violationC2 +
//                violationC3 +
//                violationC4 +
//                violationD1 +
//                violationD2 +
//                violationD3 +
//                violationD4 +
                violationC+
                violationD+
                violationE+
                violationF+
//                violationE1 +
//                violationE2 +
//                violationE3 +
//                violationE4 +
//                violationF1 +
//                violationF2 +
//                violationF3 +
//                violationF4 +
//                violationG1 +
//                violationG2 +
//                violationG3 +
//                violationG4 +
//                violationH1 +
//                violationH2 +
//                violationH3 +
//                violationH4 +
                violationG+
                violationH+
                vca +
                vcb +
                vcc +
                vcd +
                vce +
                vcf +
                vcg +
                vch +
                apprspeed +
                pfspeed +
                vehlimit +
                safespeed +
                animal1 +
                animal2 +
                animal3 +
                animal4 +
                animal5 +
                animal6 +
                animal7 +
                animal8 +
                issueDate +
                issueTime +
                school_zone_txt+
                violation_city_txt+
                violation_st_txt+
                violation_sttyp_txt+
                violation_cst_txt+
                violation_csttyp_txt+
                appearDate +
                courtTime +
                offbadge_no_txt+
                offlname_txt+
                offarea_txt+
                division_txt+
                detail_txt+
                nightCourt +
                catobenotified_txt+
                cacitenotsignedbydriver_txt+
                "--------------------------------------------------\n" +
                "ENDML\n" +
                //"FORM\n" +
                "PRINT\n");

        printerConnection.write(footer.getBytes());

    }

}
