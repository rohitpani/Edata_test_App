package com.edatasolutions.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.edatasolutions.R;
import com.edatasolutions.utils.DBRDriverLicenseUtil;
import com.edatasolutions.models.DriverLicense;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.microblink.blinkbarcode.entities.recognizers.RecognizerBundle;
import com.microblink.blinkbarcode.entities.recognizers.blinkbarcode.barcode.BarcodeRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.edatasolutions.utils.DBRDriverLicenseUtil.BIRTH_DATE;
import static com.edatasolutions.utils.DBRDriverLicenseUtil.CITY;
import static com.edatasolutions.utils.DBRDriverLicenseUtil.FIRST_NAME;
import static com.edatasolutions.utils.DBRDriverLicenseUtil.GENDER;
import static com.edatasolutions.utils.DBRDriverLicenseUtil.LAST_NAME;
import static com.edatasolutions.utils.DBRDriverLicenseUtil.LICENSE_NUMBER;
import static com.edatasolutions.utils.DBRDriverLicenseUtil.MIDDLE_NAME;
import static com.edatasolutions.utils.DBRDriverLicenseUtil.STATE;
import static com.edatasolutions.utils.DBRDriverLicenseUtil.STREET;
import static com.edatasolutions.utils.DBRDriverLicenseUtil.ZIP;

public class ActivityScanCode extends AppCompatActivity {


    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";

    private BarcodeRecognizer mBarcodeRecognizer;
    private RecognizerBundle mRecognizerBundle;

    HashMap<String, String> myData = new HashMap<String, String>();

    public final String Customer_Family_Name = "DCS", Customer_Given_Name = "DCT", Name_Suffix = "DCU",
            Street_Address_1 = "DAG", City = "DAI", Jurisdction_Code = "DAJ", Postal_Code = "DAK",
            Country_Identification = "DCG", Customer_Id_Number = "DAQ", Class = "DCA", Restrictions = "DCB",
            Endorsements = "DCD", Document_Discriminator = "DCF", Vehicle_Code = "DCH", Expiration_Date = "DBA",
            Date_Of_Birth = "DBB", Sex = "DBC", Issue_Date = "DBD", Height = "DAU", Weight = "DCE", Eye_Color = "DAY",
            Control_Number = "ZWA", WA_SPECIFIC_ENDORSMENT = "ZWB", Transaction_Types = "ZWC", Under_18_Until = "ZWD",
            Under_21_Until = "ZWE", Revision_Date = "ZWF", Customer_Full_Name = "DAA", Customer_First_Name = "DAC",
            Customer_Middle_Name = "DAD", Street_Address_2 = "DAH", Street_Address_1_optional = "DAL",
            Street_Address_2_optional = "DAM";

    ArrayList<String> allKeys = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

        initViews();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
    }


    private void initViews() {
    txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
    surfaceView = findViewById(R.id.surfaceView);
    btnAction = findViewById(R.id.btnAction);
    btnAction.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //onBackPressed();
//                if (intentData.length() > 0) {
//                    startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(intentData)));
//                }
        }
    });
}

    private void initialiseDetectorsAndSources() {

        //  Toast.makeText(getApplicationContext() , "Barcode scanner started" , Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.PDF417)
                .build();

        cameraSource = new CameraSource.Builder(this , barcodeDetector)
                .setRequestedPreviewSize(1920 , 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ActivityScanCode.this , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ActivityScanCode.this , new String[]{Manifest.permission.CAMERA} , REQUEST_CAMERA_PERMISSION);

                        if (ActivityCompat.checkSelfPermission(ActivityScanCode.this , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

//                            BarcodeUISettings uiSettings = new BarcodeUISettings(mRecognizerBundle);
//                            uiSettings.setBeepSoundResourceID(R.raw.beep);
//                            ActivityRunner.startActivityForResult(ActivityScanCode.this, 1000, uiSettings);
                            cameraSource.start(surfaceView.getHolder());
                        }else {
                            onBackPressed();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder , int format , int width , int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });



        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
               // Toast.makeText(getApplicationContext() , "To prevent memory leaks barcode scanner has been stopped" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {


                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
//                Barcode thisCode = barcodes.valueAt(0);


                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                            btnAction.setText("GOT IT");



                            for (int i=0; i<barcodes.size(); i++) {
                                int key = barcodes.keyAt(i);
                                final Barcode barcode = barcodes.get(key);


                                String keyData=barcode.rawValue;
                                //Toast.makeText(getApplicationContext(),keyData,Toast.LENGTH_LONG).show();
                                String[] separated = keyData.split("\n");
                                List<String> nameList = new ArrayList<>(Arrays.asList(separated));

                                if (DBRDriverLicenseUtil.ifDriverLicense(keyData)) {

                                    HashMap<String, String> resultMaps = DBRDriverLicenseUtil.readUSDriverLicense(keyData);
                                    for(String k: resultMaps.keySet()){
                                        //Toast.makeText(getApplicationContext(),k +": "+resultMaps.get(k),Toast.LENGTH_SHORT).show();
                                    }
                                    Intent intent = new Intent(ActivityScanCode.this, ActivityDriver.class);
                                    DriverLicense driverLicense = new DriverLicense();
                                    driverLicense.documentType = "DL";
                                    Log.e("heyyyy",resultMaps.toString());
                                    boolean res_fname;
                                    res_fname = (resultMaps.get(FIRST_NAME) == null);
                                    if (res_fname){
                                        driverLicense.firstName = "";
                                    }else {
                                        driverLicense.firstName = Objects.requireNonNull(resultMaps.get(FIRST_NAME)).trim();
                                    }

                                    boolean res_lname;
                                    res_lname = (resultMaps.get(LAST_NAME) == null);
                                    if (res_lname){
                                        driverLicense.lastName = "";
                                    }else {
                                        driverLicense.lastName = Objects.requireNonNull(resultMaps.get(LAST_NAME)).trim();
                                    }

                                    boolean res_mname;
                                    res_mname = (resultMaps.get(MIDDLE_NAME) == null);
                                    if (res_mname){
                                        driverLicense.middleName = "";
                                    }else {
                                        driverLicense.middleName = Objects.requireNonNull(resultMaps.get(MIDDLE_NAME)).trim();
                                    }

                                    boolean res_street;
                                    res_street = (resultMaps.get(STREET) == null);
                                    if (res_street){
                                        driverLicense.addressStreet = "";
                                    }else {
                                        driverLicense.addressStreet = Objects.requireNonNull(resultMaps.get(STREET)).trim();
                                    }

                                    boolean res_state;
                                    res_state = (resultMaps.get(STATE) == null);
                                    if (res_state){
                                        driverLicense.addressState = "";
                                    }else {
                                        driverLicense.addressState = Objects.requireNonNull(resultMaps.get(STATE)).trim();
                                    }

                                    boolean res_city;
                                    res_city = (resultMaps.get(CITY) == null);
                                    if (res_city){
                                        driverLicense.addressCity = "";
                                    }else {
                                        driverLicense.addressCity = Objects.requireNonNull(resultMaps.get(CITY)).trim();
                                    }

                                    Log.e("checkkk",driverLicense.addressCity+"djfnjd"+driverLicense.addressState);
                                    boolean res_zip;
                                    res_zip = (resultMaps.get(ZIP) == null);
                                    if (res_zip){
                                        driverLicense.addressZip = "";
                                    }else {
                                        driverLicense.addressZip = Objects.requireNonNull(resultMaps.get(ZIP)).trim();
                                    }

                                    boolean res_licenseNumber;
                                    res_licenseNumber = (resultMaps.get(LICENSE_NUMBER) == null);
                                    if (res_licenseNumber){
                                        driverLicense.licenseNumber = "";
                                    }else {
                                        driverLicense.licenseNumber = Objects.requireNonNull(resultMaps.get(LICENSE_NUMBER)).trim();
                                    }

                                    boolean res_driverState;
                                    res_driverState = (resultMaps.get(STATE) == null);
                                    if (res_driverState){
                                        driverLicense.driverState = "";
                                    }else {
                                        driverLicense.driverState = Objects.requireNonNull(resultMaps.get(STATE)).trim();
                                    }

                                    boolean res_birthDate;
                                    res_birthDate = (resultMaps.get(BIRTH_DATE) == null);
                                    if (res_birthDate){
                                        driverLicense.birthDate = "";
                                    }else {
                                        driverLicense.birthDate = Objects.requireNonNull(resultMaps.get(BIRTH_DATE)).trim();
                                    }

                                    boolean res_gender;
                                    res_gender = (resultMaps.get(GENDER) == null);
                                    if (res_gender){
                                        driverLicense.gender = "";
                                    }else {
                                        driverLicense.gender = Objects.requireNonNull(resultMaps.get(GENDER)).trim();
                                    }

                                    boolean res_hairColor;
                                    res_hairColor = (resultMaps.get(DBRDriverLicenseUtil.DAZ) == null);
                                    if (res_hairColor){
                                        driverLicense.hairColor = "";
                                    }else {
                                        driverLicense.hairColor = Objects.requireNonNull(resultMaps.get(DBRDriverLicenseUtil.DAZ)).trim();
                                    }

                                    boolean res_eyeColor;
                                    res_eyeColor = (resultMaps.get(DBRDriverLicenseUtil.DAY) == null);
                                    if (res_eyeColor){
                                        driverLicense.eyeColor = "";
                                    }else {
                                        driverLicense.eyeColor = Objects.requireNonNull(resultMaps.get(DBRDriverLicenseUtil.DAY)).trim();
                                    }

                                    boolean res_weight;
                                    res_weight = (resultMaps.get(DBRDriverLicenseUtil.DAW) == null);
                                    if (res_weight){
                                        driverLicense.weight = "";
                                    }else {
                                        driverLicense.weight = Objects.requireNonNull(resultMaps.get(DBRDriverLicenseUtil.DAW)).trim();
                                    }
//                                    driverLicense.firstName = Objects.requireNonNull(resultMaps.get(FIRST_NAME)).trim();
//                                    driverLicense.middleName = Objects.requireNonNull(resultMaps.get(MIDDLE_NAME)).trim();
//                                    driverLicense.lastName = Objects.requireNonNull(resultMaps.get(LAST_NAME)).trim();
//                                    driverLicense.address1 = Objects.requireNonNull(resultMaps.get(DBRDriverLicenseUtil.DAL)).trim();
//                                    driverLicense.address2 = Objects.requireNonNull(resultMaps.get(DBRDriverLicenseUtil.DAM)).trim();
//                                    driverLicense.addressStreet = Objects.requireNonNull(resultMaps.get(STREET)).trim();
//                                    driverLicense.addressCity = Objects.requireNonNull(resultMaps.get(CITY)).trim();
//                                    driverLicense.addressState = Objects.requireNonNull(resultMaps.get(STATE)).trim();
//                                    driverLicense.addressZip = Objects.requireNonNull(resultMaps.get(ZIP)).trim();
//                                    driverLicense.licenseNumber = Objects.requireNonNull(resultMaps.get(LICENSE_NUMBER)).trim();
//                                    driverLicense.driverState = Objects.requireNonNull(resultMaps.get(STATE)).trim();
//                                    driverLicense.birthDate = Objects.requireNonNull(resultMaps.get(BIRTH_DATE)).trim();
//                                    driverLicense.gender = Objects.requireNonNull(resultMaps.get(GENDER)).trim();
//                                    driverLicense.hairColor = Objects.requireNonNull(resultMaps.get(DBRDriverLicenseUtil.DAZ)).trim();
//                                    driverLicense.eyeColor = Objects.requireNonNull(resultMaps.get(DBRDriverLicenseUtil.DAY)).trim();
//                                    driverLicense.weight = Objects.requireNonNull(resultMaps.get(DBRDriverLicenseUtil.DAW)).trim();

                                    boolean res;
                                    res = (resultMaps.get(DBRDriverLicenseUtil.DCU) == null);
                                    if (res){
                                        driverLicense.suffix = "";
                                    }else {
                                        driverLicense.suffix = Objects.requireNonNull(resultMaps.get(DBRDriverLicenseUtil.DCU)).trim();
                                    }

                                    boolean res_class;
                                    res_class = (resultMaps.get(DBRDriverLicenseUtil.DCA) == null);
                                    if (res_class){
                                        driverLicense.driverlicenseType = "";
                                    }else {
                                        driverLicense.driverlicenseType = Objects.requireNonNull(resultMaps.get(DBRDriverLicenseUtil.DCA)).trim();
                                    }

                                    boolean res_height;
                                    res_height = (resultMaps.get(DBRDriverLicenseUtil.DAU) == null);

                                    if (!res_height){
                                        if (Objects.requireNonNull(resultMaps.get(DBRDriverLicenseUtil.DAU)).contains("-")){
                                            driverLicense.height = Objects.requireNonNull(resultMaps.get(DBRDriverLicenseUtil.DAU)).trim();
                                        }else {
                                            driverLicense.height = Objects.requireNonNull(resultMaps.get(DBRDriverLicenseUtil.DAU)).substring(0,3);
                                        }
                                    }



//

                                    intent.putExtra("DriverLicense", driverLicense);
//                                    intent.putExtra("selected","driver_license");
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                            }

                        }
                    });
                }else {
                    btnAction.setText("No Barcode found");
                }
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mRecognizerBundle.loadFromIntent(data);
        BarcodeRecognizer.Result result =  mBarcodeRecognizer.getResult();

        Log.e("checkdatabarcode33",result.toString());
//        if (requestCode == SCAN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            ArrayList<BarcodeResult> barcodes = data.getParcelableArrayListExtra(BarcodeScanActivity.RESULT_EXTRA);
//            Log.e("BARCODE RESULT ", "<<<>>" + barcodes.toString());
//            String barcodeResult = barcodes.get(0).barcodeString;
//            String lines[] = barcodeResult.split("\\r?\\n");
//            for (int i = 0; i < lines.length; i++) {
//                String str = lines[i];
//                if (str.contains("ANSI")) {
//                    str = str.substring(str.indexOf("DL"));
//                    String str1[] = str.split("DL");
//                    if (str1.length > 1) {
//                        str = str1[str1.length - 1];
//                    }
//                }
//                if (str.length() > 3) {
//                    String key = str.substring(0, 3);
//                    String value = str.substring(3);
//                    if (allKeys.contains(key)) {
//                        if (!value.equalsIgnoreCase("None")) {
//                            myData.put(allKeys.get(allKeys.indexOf(key)), value);
//                        }
//                    }
//                }
//                Log.e("RESULT ", "<<>>" + lines[i]);
//            }
//            Log.e("TAG", "SO MAY BE FINAL RESULT");
//            if (myData.containsKey(Customer_Family_Name)) {
//                Log.v("TAG", "users family name:" + myData.get(Customer_Family_Name));
//              //  lname = myData.get(Customer_Family_Name).trim();
//            }
//            if (myData.containsKey(Customer_Given_Name)) {
//                Log.v("TAG", "users Given name:" + myData.get(Customer_Given_Name));
//                try {
//                    String CustomerName[] = myData.get(Customer_Given_Name).split(" ");
//                    fname = CustomerName[0].trim();
//                    mname = CustomerName[1].substring(0, 1).trim();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (myData.containsKey(Name_Suffix)) {
//                Log.v("TAG", "Surname name:" + myData.get(Name_Suffix));
//            }
//            if (myData.containsKey(Street_Address_1)) {
//                Log.v("TAG", "Address line 1 :" + myData.get(Street_Address_1));
//                try {
//                    address = myData.get(Street_Address_1).trim();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (TextUtils.isEmpty(address)) {
//                if (myData.containsKey(Street_Address_2)) {
//                    address = myData.get(Street_Address_2).trim();
//                }
//                if (TextUtils.isEmpty(address)) {
//                    if (myData.containsKey(Street_Address_1_optional)) {
//                        address = myData.get(Street_Address_1_optional).trim();
//                    }
//                }
//                if (TextUtils.isEmpty(address)) {
//                    if (myData.containsKey(Street_Address_2_optional)) {
//                        address = myData.get(Street_Address_2_optional).trim();
//                    }
//                }
//            }
//            if (myData.containsKey(City)) {
//                Log.v("TAG", "City:" + myData.get(City));
//                city = myData.get(City).trim();
//            }
//            if (myData.containsKey(Jurisdction_Code)) {
//                Log.v("TAG", "State:" + myData.get(Jurisdction_Code));
//                State = myData.get(Jurisdction_Code).trim();
//            }
//            if (myData.containsKey(Postal_Code)) {
//                Log.v("TAG", "Pin Code:" + myData.get(Postal_Code));
//                zipcode = myData.get(Postal_Code).substring(0, 5).trim();
//            }
//            if (myData.containsKey(Date_Of_Birth)) {
//                Log.v("TAG", "Birth Date    :" + myData.get(Date_Of_Birth));
//                birthday = myData.get(Date_Of_Birth).substring(0, 2) + "/" + myData.get(Date_Of_Birth).substring(2, 4)
//                        + "/" + myData.get(Date_Of_Birth).substring(4);
//                if (isThisDateValid(birthday, "MM/dd/yyyy", myData.get(Date_Of_Birth)))
//                    Log.e("TAG", "IS VALID");
//                else
//                    Log.e("TAG", "IS NOT VALID");
//            }
//            if (myData.containsKey(Sex)) {
//                Log.v("TAG", "Sex:" + (myData.get(Sex).toString().trim().equals("1") ? "Male" : "Female"));
//            }
//            if (myData.containsKey(Customer_Full_Name)) {
//                String cName = myData.get(Customer_Full_Name);
//                int startIndexOfComma = 0;
//                int endIndexOfComma = 0;
//                startIndexOfComma = cName.indexOf(",");
//                endIndexOfComma = cName.lastIndexOf(",");
//                if (startIndexOfComma != endIndexOfComma) {
//                    String CustomerName[] = myData.get(Customer_Full_Name).split(",");
//                    lname = CustomerName[0].replace(",", "").trim();
//                    fname = CustomerName[1].trim();
//                    mname = CustomerName[2].substring(0, 1).trim();
//                } else {
//                    String CustomerName[] = myData.get(Customer_Full_Name).split(" ");
//                    lname = CustomerName[0].replace(",", "").trim();
//                    fname = CustomerName[1].trim();
//                    mname = CustomerName[2].substring(0, 1).trim();
//                }
//            }
//            if (myData.containsKey(Customer_First_Name)) {
//                fname = myData.get(Customer_First_Name).trim();
//            }
//            if (myData.containsKey(Customer_Middle_Name)) {
//                mname = myData.get(Customer_Middle_Name).substring(0, 1).trim();
//            }
//            // TODO edit here at 7/3/2014
//            if (myData.containsKey(Customer_Id_Number)) {
//                licence_number = myData.get(Customer_Id_Number).trim();
//                Log.e("TAG", "Licence Number is :" + licence_number);
//            }
//            if (myData.containsKey(Expiration_Date)) {
//                licence_expire_date = myData.get(Expiration_Date).trim();
//                licence_expire_date = myData.get(Expiration_Date).substring(0, 2) + "/"
//                        + myData.get(Expiration_Date).substring(2, 4) + "/" + myData.get(Expiration_Date).substring(4);
//                licence_expire_date = makeDateValid(licence_expire_date, "MM/dd/yyyy", myData.get(Expiration_Date));
//                Log.e("TAG", "expire date is :" + licence_expire_date);
//            }
//            etFirstName.setText(fname.trim());
//            etMiddleName.setText(mname.trim());
//            etLastName.setText(lname.trim());
//            etAddress.setText(address.trim());
//            etZipCode.setText(zipcode.trim());
//            etCity.setText(city.trim());
//            etState.setText(State.trim());
//            etDLNumber.setText(licence_number);
//            etDLExpirationDate.setText(licence_expire_date);
//            etBirthDay.setText(birthday.trim());
//        }
//    }
    }
}