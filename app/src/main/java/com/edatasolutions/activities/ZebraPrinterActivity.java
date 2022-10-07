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

    private Button testButton, print_data;
    private ZebraPrinter printer;
    private TextView statusField;
    private LinearLayout ll_print_layout;

    //-----------------
    private UIHelper helper = new UIHelper(this);

    @Override
    public void onBackPressed() {
        sessionManager.ClearDataEntery();
        sessionManager.clearSession();
        super.onBackPressed();
    }

    private TextView driver_firstname, driver_middlename, driver_lastname, driver_suffix, driver_address1, driver_address2, driver_city, driver_state, driver_zipcode;
    private TextView driver_license_no, driver_statee, driver_dob, driver_sex, driver_hair, driver_eyes, driver_height, driver_weight, license_type, driver_race, driver_ethncity, comm_drivers_license;
    private TextView pre_owner_firstname, pre_owner_middlename, pre_owner_lastname, pre_owner_suffix, pre_owner_address1, pre_owner_address2, pre_owner_city, pre_owner_state, pre_owner_zipcode;
    private TextView pre_vehyear, pre_vehmake, pre_vehmodel, pre_vehbody, pre_vehcolor, pre_vehtype, pre_commercial, pre_vehno, pre_vehstate, pre_hazardous, pre_overload, pre_policyno;
    private TextView pre_vioA, pre_vioB, pre_vioC, pre_vioD, pre_vioE, pre_vioF, pre_vioG, pre_vioH, pre_vca, pre_vcb, pre_vcc, pre_vcd, pre_vce, pre_vcf, pre_vcg, pre_vch;
    private TextView pre_apprspeed, pre_pfspeed, pre_vehlimit, pre_safespeed, pre_animal1, pre_animal2, pre_animal3, pre_animal4, pre_animal5, pre_animal6, pre_animal7, pre_animal8;
    private TextView citation_no_txt;
    private TextView appeardate_txt, courttime_txt, nightcourt_txt, issuetime_txt, issuedate_txt;
    private SessionManager sessionManager;
    private ImageView clear_page;
    private SharedPreferences settings;
    private LinearLayout ll_owner_first_name, ll_owner_middlename, ll_owner_lastname, ll_owner_suffix, ll_owner_address1, ll_owner_address2, ll_owner_city, ll_owner_state, ll_owner_zipcode;
    private LinearLayout ll_vioA, ll_vioB, ll_vioC, ll_vioD, ll_vioE, ll_vioF, ll_vioG, ll_vioH, ll_vca, ll_vcb, ll_vcc, ll_vcd, ll_vce, ll_vcf, ll_vcg, ll_vch;
    private LinearLayout ll_animal1, ll_animal2, ll_animal3, ll_animal4, ll_animal5, ll_animal6, ll_animal7, ll_animal8;


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

        statusField = (TextView) this.findViewById(R.id.statusText);


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

        appeardate_txt = findViewById(R.id.appeardate_txt);
        courttime_txt = findViewById(R.id.courttime_txt);
        nightcourt_txt = findViewById(R.id.nightcourt_txt);
        issuedate_txt = findViewById(R.id.issuedate_txt);
        issuetime_txt = findViewById(R.id.issuetime_txt);

        citation_no_txt = findViewById(R.id.citation_no_txt);
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

        ll_owner_first_name = findViewById(R.id.ll_owner_first_name);
        ll_owner_middlename = findViewById(R.id.ll_owner_middlename);
        ll_owner_lastname = findViewById(R.id.ll_owner_lastname);
        ll_owner_suffix = findViewById(R.id.ll_owner_suffix);
        ll_owner_address1 = findViewById(R.id.ll_owner_address1);
        ll_owner_address2 = findViewById(R.id.ll_owner_address2);
        ll_owner_city = findViewById(R.id.ll_owner_city);
        ll_owner_state = findViewById(R.id.ll_owner_state);
        ll_owner_zipcode = findViewById(R.id.ll_owner_zipcode);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @SuppressLint({"ServiceCast", "HardwareIds"})
    public ZebraPrinter connect() {
        setStatus("Connecting...", Color.YELLOW);
        connection = null;
        if (isBluetoothSelected()) {
            try {
                BluetoothDiscoverer.findPrinters(this, new DiscoveryHandler() {

                    public void foundPrinter(DiscoveredPrinter printer) {
                        String macAddress = printer.address;
                        connection = new BluetoothConnection(macAddress);
                        SettingsHelper.saveBluetoothAddress(ZebraPrinterActivity.this, macAddress);

                        //I found a printer! I can use the properties of a Discovered printer (address) to make a Bluetooth Connection
                    }

                    public void discoveryFinished() {
                        //        Toast.makeText(ZebraPrinterActivity.this,"Discovery is done ",Toast.LENGTH_SHORT).show();
                        //Discovery is done
                    }

                    public void discoveryError(String message) {
                        Toast.makeText(ZebraPrinterActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        //Error during discovery
                    }
                });
            } catch (ConnectionException e) {
                e.printStackTrace();
            }

            connection = new BluetoothConnection(getMacAddressFieldText());
            SettingsHelper.saveBluetoothAddress(this, getMacAddressFieldText());
        } else {
            try {
                int port = Integer.parseInt(getTcpPortNumber());
                connection = new TcpConnection(getTcpAddress(), port);
                SettingsHelper.saveIp(this, getTcpAddress());
                SettingsHelper.savePort(this, getTcpPortNumber());
            } catch (NumberFormatException e) {
                setStatus("Port Number Is Invalid", Color.RED);
                return null;
            }
        }

        try {
            connection.open();
            setStatus("Connected", Color.GREEN);
        } catch (ConnectionException e) {
            Toast.makeText(ZebraPrinterActivity.this, "ConnectionException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            setStatus("Comm Error! Disconnecting", Color.RED);
            DemoSleeper.sleep(1000);
            disconnect();
        }

        ZebraPrinter printer = null;

        if (connection.isConnected()) {
            try {
                printer = ZebraPrinterFactory.getInstance(connection);
                setStatus("Determining Printer Language", Color.YELLOW);
                String pl = SGD.GET("device.languages", connection);
                setStatus("Printer Language " + pl, Color.BLUE);
            } catch (ConnectionException e) {
                setStatus("Unknown Printer Language", Color.RED);
                printer = null;
                DemoSleeper.sleep(1000);
                disconnect();
            } catch (ZebraPrinterLanguageUnknownException e) {
                setStatus("Unknown Printer Language", Color.RED);
                printer = null;
                DemoSleeper.sleep(1000);
                disconnect();
            }
        }

        return printer;
    }

    public void disconnect() {
        try {
            setStatus("Disconnecting", Color.RED);
            if (connection != null) {
                connection.close();
            }
            setStatus("Not Connected", Color.RED);
        } catch (ConnectionException e) {
            setStatus("COMM Error! Disconnected", Color.RED);
        } finally {
            enableTestButton(true);
        }
    }

    private void setStatus(final String statusMessage, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                statusField.setBackgroundColor(color);
                statusField.setText(statusMessage);
            }
        });
        DemoSleeper.sleep(1000);
    }


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
                setStatus("Sending Data", Color.BLUE);
            } else if (printerStatus.isHeadOpen) {
                setStatus("Printer Head Open", Color.RED);
            } else if (printerStatus.isPaused) {
                setStatus("Printer is Paused", Color.RED);
            } else if (printerStatus.isPaperOut) {
                setStatus("Printer Media Out", Color.RED);
            }
            DemoSleeper.sleep(1500);
            if (connection instanceof BluetoothConnection) {
                String friendlyName = ((BluetoothConnection) connection).getFriendlyName();
                setStatus(friendlyName, Color.MAGENTA);
                DemoSleeper.sleep(500);
            }
        } catch (ConnectionException e) {
            setStatus(e.getMessage(), Color.RED);
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


    private void doConnectionTest() {

        if (!(ActivityCompat.checkSelfPermission(ZebraPrinterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(ZebraPrinterActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        printer = connect();

        if (printer != null) {
            sendTestLabel();
        } else {
            disconnect();
        }
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


        String session_citation_no = sessionManager.getHeaderSession().get(SessionManager.CITATION_NUMBER);


        citation_no_txt.setText(session_citation_no);

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

        //COURT DETAILS

        String session_appeardate = sessionManager.getViolationMiscSession().get(SessionManager.APPEAR_DATE);
        String session_courttime = sessionManager.getViolationMiscSession().get(SessionManager.COURT_TIME);
        String session_nightcourt = sessionManager.getViolationMiscSession().get(SessionManager.NIGHT_COURT);

        String session_issuedate = sessionManager.getViolationMiscSession().get(SessionManager.ISSUE_DATE);
        String session_issuetime = sessionManager.getViolationMiscSession().get(SessionManager.TIME);
        String session_ampm = sessionManager.getViolationMiscSession().get(SessionManager.AMPM);

        assert session_issuedate != null;
        if (!session_issuedate.equals("")) {
            issuedate_txt.setText(session_issuedate);
        } else {
            issuedate_txt.setText(getResources().getString(R.string.dash));
        }

        assert session_issuetime != null;
        assert session_ampm != null;
        if (!session_issuetime.equals("") && !session_ampm.equals("")) {
            issuetime_txt.setText(session_issuetime + " " + session_ampm);
        } else {
            issuetime_txt.setText(getResources().getString(R.string.dash));
        }

        assert session_appeardate != null;
        if (!session_appeardate.equals("")) {
            appeardate_txt.setText(session_appeardate);
        } else {
            appeardate_txt.setText(getResources().getString(R.string.dash));
        }

        assert session_courttime != null;
        if (!session_courttime.equals("")) {
            courttime_txt.setText(session_courttime);
        } else {
            courttime_txt.setText(getResources().getString(R.string.dash));
        }

        assert session_nightcourt != null;
        if (!session_nightcourt.equals("")) {
            nightcourt_txt.setText(session_nightcourt);
        } else {
            nightcourt_txt.setText(getResources().getString(R.string.dash));
        }

        databaseAccess.close();
    }

    //------------- //
    private Bitmap loadBitmapFromView(View v) {
        Bitmap b = null;
        try {
            b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            Drawable bgDrawable = v.getBackground();
            if (bgDrawable != null)
                bgDrawable.draw(c);
            else
                c.drawColor(Color.WHITE);
            v.draw(c);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }
    //---------- //

    public void performTestWithManyJobs() {
        if (!(ActivityCompat.checkSelfPermission(ZebraPrinterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(ZebraPrinterActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        executeTest(true);
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

    }

    private void connectAndSendLabel(final boolean withManyJobs) {
        if (isBluetoothSelected()) {
            try {
                BluetoothDiscoverer.findPrinters(this, new DiscoveryHandler() {

                    public void foundPrinter(DiscoveredPrinter printer) {
                        String macAddress = printer.address;
                        connection = new BluetoothConnection(macAddress);
                        SettingsHelper.saveBluetoothAddress(ZebraPrinterActivity.this, macAddress);

                        //I found a printer! I can use the properties of a Discovered printer (address) to make a Bluetooth Connection
                    }

                    public void discoveryFinished() {
                        //        Toast.makeText(ZebraPrinterActivity.this,"Discovery is done ",Toast.LENGTH_SHORT).show();
                        //Discovery is done
                    }

                    public void discoveryError(String message) {
                        Toast.makeText(ZebraPrinterActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        //Error during discovery
                    }
                });
            } catch (ConnectionException e) {
                e.printStackTrace();
            }

            connection = new BluetoothConnection(getMacAddressFieldText());
            SettingsHelper.saveBluetoothAddress(this, getMacAddressFieldText());
        }
       /* if (!isBluetoothSelected()) {
            try {
                connection = new TcpConnection(getTcpAddress(), Integer.valueOf(getTcpPortNumber()));
            } catch (NumberFormatException e) {
                helper.showErrorDialogOnGuiThread("Port number is invalid");
                return;
            }
        } else {
            connection = new BluetoothConnection(getMacAddressFieldText());
        }*/
        try {
            helper.showLoadingDialog("Connecting...");
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
        } catch (ZebraPrinterLanguageUnknownException e) {
            helper.showErrorDialogOnGuiThread("Could not detect printer language");
        } finally {
            helper.dismissLoadingDialog();
        }
    }

    private void sendTestLabelWithManyJobs(Connection printerConnection) {
        try {
            sendZplReceipt(printerConnection);
        } catch (ConnectionException e) {
            helper.showErrorDialogOnGuiThread(e.getMessage());
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

        String citation = "CITATION NUMBER : " + citation_no_txt.getText().toString().trim() + "\n";
        String firstName = "FIRST NAME : " + driver_firstname.getText().toString().trim() + "\n";
        String middleName = "MIDDLE NAME : " + driver_middlename.getText().toString().trim() + "\n";
        String lastName = "LAST NAME : " + driver_lastname.getText().toString().trim() + "\n";
        String suffix = "SUFFIX : " + driver_suffix.getText().toString().trim() + "\n";
        String addLine1 = "ADDRESS 1 : " + driver_address1.getText().toString().trim() + "\n";
        String addLine2 = "ADDRESS 2 : " + driver_address2.getText().toString().trim() + "\n";
        String city = "CITY : " + driver_city.getText().toString().trim() + "\n";
        String state = "STATE : " + driver_state.getText().toString().trim() + "\n";
        String zipcode = "ZIPCODE : " + driver_zipcode.getText().toString().trim() + "\n";
        String licNumber = "LICENCE : " + driver_license_no.getText().toString().trim() + "\n";
        String drState = "DRIVER STATE : " + driver_statee.getText().toString().trim() + "\n";
        String drDOB = "DOB : " + driver_dob.getText().toString().trim() + "\n";
        String drSex = "SEX : " + driver_sex.getText().toString().trim() + "\n";
        String drHair = "HAIR : " + driver_hair.getText().toString().trim() + "\n";
        String drEyes = "EYES : " + driver_eyes.getText().toString().trim() + "\n";
        String drHeight = "HEIGHT (in) : " + driver_height.getText().toString().trim() + "\n";
        String drWeight = "WEIGHT (lbs) : " + driver_weight.getText().toString().trim() + "\n";
        String lcType = "LICENSE TYPE/CLASS : " + license_type.getText().toString().trim() + "\n";
        String race = "DESCENT/RACE : " + driver_race.getText().toString().trim() + "\n";
        String ethncity = "ETHNCITY : " + driver_ethncity.getText().toString().trim() + "\n";
        String commDrLicence = "COMM DR LICENSE : " + comm_drivers_license.getText().toString().trim() + "\n";
        String ownerFname = ll_owner_first_name.getVisibility() == View.VISIBLE ? "OWNER FIRST NAME : " + pre_owner_firstname.getText().toString().trim() + "\n" : "";
        String ownerMname = ll_owner_middlename.getVisibility() == View.VISIBLE ? "OWNER MIDDLE NAME : " + pre_owner_middlename.getText().toString().trim() + "\n" : "";
        String ownerLname = ll_owner_lastname.getVisibility() == View.VISIBLE ? "OWNER LAST NAME : " + pre_owner_lastname.getText().toString().trim() + "\n" : "";
        String ownerSuffix = ll_owner_suffix.getVisibility() == View.VISIBLE ? "OWNER SUFFIX : " + pre_owner_suffix.getText().toString().trim() + "\n" : "";
        String ownerAdd1 = ll_owner_address1.getVisibility() == View.VISIBLE ? "OWNER ADDRESS 1 : " + pre_owner_address1.getText().toString().trim() + "\n" : "";
        String ownerAdd2 = ll_owner_address2.getVisibility() == View.VISIBLE ? "OWNER ADDRESS 2 : " + pre_owner_address2.getText().toString().trim() + "\n" : "";
        String ownerCity = ll_owner_city.getVisibility() == View.VISIBLE ? "OWNER CITY : " + pre_owner_city.getText().toString().trim() + "\n" : "";
        String ownerState = ll_owner_state.getVisibility() == View.VISIBLE ? "OWNER STATE : " + pre_owner_state.getText().toString().trim() + "\n" : "";
        String ownerZcode = ll_owner_zipcode.getVisibility() == View.VISIBLE ? "OWNER ZIPCODE : " + pre_owner_zipcode.getText().toString().trim() + "\n" : "";
        String vehYear = "VEHYEAR : " + pre_vehyear.getText().toString().trim() + "\n";
        String vehMake = "VEHMAKE : " + pre_vehmake.getText().toString().trim() + "\n";
        String vehModel = "VEHMODEL : " + pre_vehmodel.getText().toString().trim() + "\n";
        String vehBody = "VEHBODY : " + pre_vehbody.getText().toString().trim() + "\n";
        String vehColor = "VEHCOLOR : " + pre_vehcolor.getText().toString().trim() + "\n";
        String vehType = "VEHTYPE : " + pre_vehtype.getText().toString().trim() + "\n";
        String commercial = "COMMERCIAL : " + pre_commercial.getText().toString().trim() + "\n";
        String vehlicNo = "VEHLICNO : " + pre_vehno.getText().toString().trim() + "\n";
        String vehicleState = "VEHICLE STATE : " + pre_vehstate.getText().toString().trim() + "\n";
        String hazardous = "HAZARDOUS : " + pre_hazardous.getText().toString().trim() + "\n";
        String overload = "OVERLOAD : " + pre_overload.getText().toString().trim() + "\n";
        String policy_no = "POLICY NUMBER : " + pre_policyno.getText().toString().trim() + "\n";
        String violationA = ll_vioA.getVisibility() == View.VISIBLE ? "VIOLATION A : " + pre_vioA.getText().toString().trim() + "\n" : "";
        String violationB = ll_vioB.getVisibility() == View.VISIBLE ? "VIOLATION B : " + pre_vioB.getText().toString().trim() + "\n" : "";
        String violationC = ll_vioC.getVisibility() == View.VISIBLE ? "VIOLATION C : " + pre_vioC.getText().toString().trim() + "\n" : "";
        String violationD = ll_vioD.getVisibility() == View.VISIBLE ? "VIOLATION D : " + pre_vioD.getText().toString().trim() + "\n" : "";
        String violationE = ll_vioE.getVisibility() == View.VISIBLE ? "VIOLATION E : " + pre_vioE.getText().toString().trim() + "\n" : "";
        String violationF = ll_vioF.getVisibility() == View.VISIBLE ? "VIOLATION F : " + pre_vioF.getText().toString().trim() + "\n" : "";
        String violationG = ll_vioG.getVisibility() == View.VISIBLE ? "VIOLATION G : " + pre_vioG.getText().toString().trim() + "\n" : "";
        String violationH = ll_vioH.getVisibility() == View.VISIBLE ? "VIOLATION H : " + pre_vioH.getText().toString().trim() + "\n" : "";
        String vca = ll_vca.getVisibility() == View.VISIBLE ? "VCA : " + pre_vca.getText().toString().trim() + "\n" : "";
        String vcb = ll_vcb.getVisibility() == View.VISIBLE ? "VCB : " + pre_vcb.getText().toString().trim() + "\n" : "";
        String vcc = ll_vcc.getVisibility() == View.VISIBLE ? "VCC : " + pre_vcc.getText().toString().trim() + "\n" : "";
        String vcd = ll_vcd.getVisibility() == View.VISIBLE ? "VCD : " + pre_vcd.getText().toString().trim() + "\n" : "";
        String vce = ll_vce.getVisibility() == View.VISIBLE ? "VCE : " + pre_vce.getText().toString().trim() + "\n" : "";
        String vcf = ll_vcf.getVisibility() == View.VISIBLE ? "VCF : " + pre_vcf.getText().toString().trim() + "\n" : "";
        String vcg = ll_vcg.getVisibility() == View.VISIBLE ? "VCG : " + pre_vcg.getText().toString().trim() + "\n" : "";
        String vch = ll_vch.getVisibility() == View.VISIBLE ? "VCH : " + pre_vch.getText().toString().trim() + "\n" : "";
        String vehlimit = "VEHLIMIT : " + pre_vehlimit.getText().toString().trim() + "\n";
        String safespeed = "SAFE SPEED : " + pre_safespeed.getText().toString().trim() + "\n";
        String apprspeed = "APPRSPEED : " + pre_apprspeed.getText().toString().trim() + "\n";
        String pfspeed = "PFSPEED : " + pre_pfspeed.getText().toString().trim() + "\n";
        String animal1 = ll_animal1.getVisibility() == View.VISIBLE ? "ANIMAL1 : " + pre_animal1.getText().toString().trim() + "\n" : "";
        String animal2 = ll_animal2.getVisibility() == View.VISIBLE ? "ANIMAL2 : " + pre_animal2.getText().toString().trim() + "\n" : "";
        String animal3 = ll_animal3.getVisibility() == View.VISIBLE ? "ANIMAL3 : " + pre_animal3.getText().toString().trim() + "\n" : "";
        String animal4 = ll_animal4.getVisibility() == View.VISIBLE ? "ANIMAL4 : " + pre_animal4.getText().toString().trim() + "\n" : "";
        String animal5 = ll_animal5.getVisibility() == View.VISIBLE ? "ANIMAL5 : " + pre_animal5.getText().toString().trim() + "\n" : "";
        String animal6 = ll_animal6.getVisibility() == View.VISIBLE ? "ANIMAL6 : " + pre_animal6.getText().toString().trim() + "\n" : "";
        String animal7 = ll_animal7.getVisibility() == View.VISIBLE ? "ANIMAL7 : " + pre_animal7.getText().toString().trim() + "\n" : "";
        String animal8 = ll_animal8.getVisibility() == View.VISIBLE ? "ANIMAL8 : " + pre_animal8.getText().toString().trim() + "\n" : "";
        String issueDate = "ISSUE DATE : " + issuedate_txt.getText().toString().trim() + "\n";
        String issueTime = "ISSUE TIME : " + issuetime_txt.getText().toString().trim() + "\n";
        String appearDate = "APPEAR DATE : " + appeardate_txt.getText().toString().trim() + "\n";
        String courtTime = "COURT TIME : " + courttime_txt.getText().toString().trim() + "\n";
        String nightCourt = "NIGHT COURT : " + nightcourt_txt.getText().toString().trim() + "\n";

       /* StringBuffer sb = new StringBuffer("CITATION NUMBER : ");
        sb.append(citation_no_txt.getText().toString().trim()).append("\n")
                .append("FIRST NAME : ").append(driver_firstname.getText().toString().trim()).append("\n");*/
        String footer = String.format("! 0 200 200 3910 1\n" +
                "ML 47\n" +
                "T 7 0 10 20 " +
                citation +
                firstName +
                middleName +
                lastName +
                suffix +
                addLine1 +
                addLine2 +
                city +
                state +
                zipcode +
                licNumber +
                drState +
                drDOB +
                drSex +
                drHair +
                drEyes +
                drHeight +
                drWeight +
                lcType +
                race +
                ethncity +
                commDrLicence +
                ownerFname +
                ownerMname +
                ownerLname +
                ownerSuffix +
                ownerAdd1 +
                ownerAdd2 +
                ownerCity +
                ownerState +
                ownerZcode +
                vehYear +
                vehMake +
                vehModel +
                vehBody +
                vehColor +
                vehType +
                commercial +
                vehlicNo +
                vehicleState +
                hazardous +
                overload +
                policy_no +
                violationA +
                violationB +
                violationC +
                violationD +
                violationE +
                violationF +
                violationG +
                violationH +
                vca +
                vcb +
                vcc +
                vcd +
                vce +
                vcf +
                vcg +
                vch +
                vehlimit +
                safespeed +
                apprspeed +
                pfspeed +
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
                appearDate +
                courtTime +
                nightCourt +
                "--------------------------------------------------\n" +
                "ENDML\n" +
                "FORM\n" +
                "PRINT\n");

        printerConnection.write(footer.getBytes());

    }

    private void printBitmap(final Bitmap bitmap, Connection connection) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Looper.prepare();
                  /*  helper.showLoadingDialog("Sending image to printer");
                    connection.open();*/
                    ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
                    printer.printImage(new ZebraImageAndroid(bitmap), 0, 0, 550, 412, false);
                    /*connection.close();*/

                } catch (ConnectionException | ZebraPrinterLanguageUnknownException e) {
                    helper.showErrorDialogOnGuiThread(e.getMessage());
                } finally {
                    bitmap.recycle();
                    //  helper.dismissLoadingDialog();
                    Looper.myLooper().quit();
                }
            }
        }).start();

    }

}
