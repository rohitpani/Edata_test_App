package com.edatasolutions.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edatasolutions.BuildConfig;
import com.edatasolutions.R;
import com.edatasolutions.database.DatabaseAccess;
import com.edatasolutions.interfaces.RetrofitInterface;
import com.edatasolutions.models.officer_token;
import com.edatasolutions.models.save_data_entry;
import com.edatasolutions.utils.CaptureSignatureView;
import com.edatasolutions.utils.ConstantCodes;
import com.edatasolutions.utils.MD5;
import com.edatasolutions.utils.NetworkConectivity;
import com.edatasolutions.utils.SessionManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivitySignature extends AppCompatActivity {

    private TextView sign_done,sign_cancel,sign_clear;
    private EditText sign_name;
    private ImageView sign_cancell;
    private LinearLayout mLlCanvas;
    private String EncodedImage = "";
    private SessionManager sessionManager;
    private String CitationNumber, OwnersResponsibility,  Traffic, NonTraffic,  PED, FirstName,  MiddleName,  LastName,  Suffix,  Address, AddressLine2, City, State, ZipCode, DLN, DriversState, DOB, Sex, HairColor, Eyes, Height, Weight, DriversLicenseType, Race, EthnicityCode, OwnerFirstName, OwnerMiddleName, OwnerLastName, OwnerSuffix, OwnerAddress, OwnerAddressLine2, OwnerCity, OwnerState, OwnerZipCode, VehYear, VehMake, VehModel, VehBodyStyle, VehColor, VehicleType, IsCommercial, VLN, VLS, IsHazardous, Overload, CAFinRespProof, ViolCode1, ViolCode2, ViolCode3, ViolCode4, ViolCode5, ViolCode6, ViolCode7, ViolCode8, ViolCode1Correctable, ViolCode2Correctable, ViolCode3Correctable, ViolCode4Correctable, ViolCode5Correctable, ViolCode6Correctable, ViolCode7Correctable, ViolCode8Correctable, IssueDate, Time, AMPM, AppSpeed, MaxSpeed, SchoolZone, ViolCity, ViolStreet, StreetType, ViolCrossStreet, ViolCrossStreetType, AppearDate, AppearanceTime, OfficerBadge, OfficerLastName, Area, Division, Detail, CAToBeNotified, CACiteNotSignedByDriver, CourtCode, LEA, VehLimit, SafeSpeed,  Animal1,  Animal2,  Animal3,  Animal4,  Animal5,  Animal6,  Animal7,  Animal8,  NightCourt,  CommDriversLicense, created_at, created_by, updated_by, updated_at;
    private String user_id = "";
    private String status="Y";
    String filename = "";
    String filepath = "";
    String fileContent = "";
    String online_update="N";
    CaptureSignatureView mSig;
    private String LangId = "", TOKEN_VALUE="",UniqueId ="";
    private String device_no = "";
    private JsonArray resultSet = new JsonArray();
    private ProgressDialog progressdialog;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        sessionManager = new SessionManager(ActivitySignature.this);

        device_no = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        user_id = sessionManager.getAdminLogin().get(SessionManager.USER_ID);
        sign_done = findViewById(R.id.sign_done);
        sign_cancel =findViewById(R.id.sign_cancel);
        sign_clear = findViewById(R.id.sign_clear);
        sign_name = findViewById(R.id.sign_name);
        sign_cancell = findViewById(R.id.sign_cancell);

        mLlCanvas = (LinearLayout)findViewById(R.id.llCanvas);
        mSig = new CaptureSignatureView(this, null);
        mLlCanvas.addView(mSig, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        sign_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissions();
            }
        });
        sign_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_name.setText("");
            }
        });
        sign_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_name.setText("");
            }
        });
        sign_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSig.ClearCanvas();
                onBackPressed();
            }
        });


    }

    private void savingFile(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] img = bos.toByteArray();

//        ContentValues values = new ContentValues();
//        values.put("image", img);
//        SQLiteDatabase database = null;
//        try {
//            database = dbHelper.opendatabase();
//            database.insert("android_version", null, values);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            database.close();
//        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);

    }



    public String toBase64(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    private void getAllDatatoString(){

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String date = format.format(today);


        created_at=date;
        updated_at=date;
        created_by= sessionManager.getAdminLogin().get(SessionManager.USER_ID);//(id);
        updated_by=sessionManager.getAdminLogin().get(SessionManager.USER_ID);//null


        //VIOLATION MISC
        IssueDate = sessionManager.getViolationMiscSession().get(SessionManager.ISSUE_DATE);
        Time = sessionManager.getViolationMiscSession().get(SessionManager.TIME);
        AMPM = sessionManager.getViolationMiscSession().get(SessionManager.AMPM);
        SchoolZone = sessionManager.getViolationMiscSession().get(SessionManager.SCHOOL_ZONE);
        ViolCity = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCITY);
        ViolStreet = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONST);
        StreetType = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONSTTYP);
        ViolCrossStreet = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCST);
        ViolCrossStreetType = sessionManager.getViolationMiscSession().get(SessionManager.VIOLATIONCSTTYP);
        AppearDate = sessionManager.getViolationMiscSession().get(SessionManager.APPEAR_DATE);
        OfficerBadge = sessionManager.getViolationMiscSession().get(SessionManager.OFFBADGENO);
        OfficerLastName = sessionManager.getViolationMiscSession().get(SessionManager.OFFLNAME);
        Area = sessionManager.getViolationMiscSession().get(SessionManager.AREACODE);
        Division = sessionManager.getViolationMiscSession().get(SessionManager.DIVISION);
        Detail = sessionManager.getViolationMiscSession().get(SessionManager.DETAIL);
        NightCourt = sessionManager.getViolationMiscSession().get(SessionManager.NIGHT_COURT);
        CAToBeNotified = sessionManager.getViolationMiscSession().get(SessionManager.CA_TOBENOTIFIED);
        CACiteNotSignedByDriver = sessionManager.getViolationMiscSession().get(SessionManager.CA_CITENOTSIGNEDBYDRIVER);
        AppearanceTime = sessionManager.getViolationMiscSession().get(SessionManager.COURT_TIME);


        //VIOLATION
        ViolCode1 = sessionManager.getViolationSession().get(SessionManager.VIOLATION_A);
        ViolCode2 = sessionManager.getViolationSession().get(SessionManager.VIOLATION_B);
        ViolCode3 = sessionManager.getViolationSession().get(SessionManager.VIOLATION_C);
        ViolCode4 = sessionManager.getViolationSession().get(SessionManager.VIOLATION_D);
        ViolCode5 = sessionManager.getViolationSession().get(SessionManager.VIOLATION_E);
        ViolCode6 = sessionManager.getViolationSession().get(SessionManager.VIOLATION_F);
        ViolCode7 = sessionManager.getViolationSession().get(SessionManager.VIOLATION_G);
        ViolCode8 = sessionManager.getViolationSession().get(SessionManager.VIOLATION_H);
        ViolCode1Correctable = sessionManager.getViolationSession().get(SessionManager.VCA);
        ViolCode2Correctable = sessionManager.getViolationSession().get(SessionManager.VCB);
        ViolCode3Correctable = sessionManager.getViolationSession().get(SessionManager.VCC);
        ViolCode4Correctable = sessionManager.getViolationSession().get(SessionManager.VCD);
        ViolCode5Correctable = sessionManager.getViolationSession().get(SessionManager.VCE);
        ViolCode6Correctable = sessionManager.getViolationSession().get(SessionManager.VCF);
        ViolCode7Correctable = sessionManager.getViolationSession().get(SessionManager.VCG);
        ViolCode8Correctable = sessionManager.getViolationSession().get(SessionManager.VCH);
        VehLimit = sessionManager.getViolationSession().get(SessionManager.VEHLIMIT);
        SafeSpeed = sessionManager.getViolationSession().get(SessionManager.SAFE_SPEED);
        AppSpeed = sessionManager.getViolationSession().get(SessionManager.APPRSPEED);
        MaxSpeed = sessionManager.getViolationSession().get(SessionManager.PFSPEED);
        Animal1 = sessionManager.getViolationSession().get(SessionManager.ANIMAL1);
        Animal2 = sessionManager.getViolationSession().get(SessionManager.ANIMAL2);
        Animal3 = sessionManager.getViolationSession().get(SessionManager.ANIMAL3);
        Animal4 = sessionManager.getViolationSession().get(SessionManager.ANIMAL4);
        Animal5 = sessionManager.getViolationSession().get(SessionManager.ANIMAL5);
        Animal6 = sessionManager.getViolationSession().get(SessionManager.ANIMAL6);
        Animal7 = sessionManager.getViolationSession().get(SessionManager.ANIMAL7);
        Animal8 = sessionManager.getViolationSession().get(SessionManager.ANIMAL8);





        //DRIVER

        FirstName =sessionManager.getDriverSession().get(SessionManager.DRIVER_FIRST_NAME);
        MiddleName =sessionManager.getDriverSession().get(SessionManager.DRIVER_MIDDLE_NAME);
        LastName =sessionManager.getDriverSession().get(SessionManager.DRIVER_LAST_NAME);
        Suffix =sessionManager.getDriverSession().get(SessionManager.DRIVER_SUFFIX);
        Address=sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS1);
        AddressLine2=sessionManager.getDriverSession().get(SessionManager.DRIVER_ADDRESS2);
        City=sessionManager.getDriverSession().get(SessionManager.DRIVER_CITY);
        State = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATE);
        ZipCode = sessionManager.getDriverSession().get(SessionManager.DRIVER_ZIPCODE);
        DLN= sessionManager.getDriverSession().get(SessionManager.DRIVER_LICENSE_NO);
        DriversState = sessionManager.getDriverSession().get(SessionManager.DRIVER_STATEE);
        String dateofbirth = sessionManager.getDriverSession().get(SessionManager.DOB);

        try {
            String startDateString = dateofbirth;
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

            DOB=sdf2.format(sdf.parse(startDateString));

        } catch (ParseException e) {
            e.printStackTrace();
        }

//        try {
//            assert dateofbirth != null;
//            Date date_of_birth = inputFormat.parse(dateofbirth);
//            assert date_of_birth != null;
//            String outputDateStr = outputFormat.format(new Date());
//            //String outputDateStr = outputFormat.format(date_of_birth);
//            DOB=outputDateStr;
//        }catch (Exception ex){
//            Log.e("error","error");
//        }

        Sex = sessionManager.getDriverSession().get(SessionManager.SEX);
        HairColor = sessionManager.getDriverSession().get(SessionManager.HAIR);
        Eyes = sessionManager.getDriverSession().get(SessionManager.EYES);
        Height = sessionManager.getDriverSession().get(SessionManager.HEIGHT);
        Weight = sessionManager.getDriverSession().get(SessionManager.WEIGHT);
        DriversLicenseType = sessionManager.getDriverSession().get(SessionManager.LICENSE_TYPE);
        Race = sessionManager.getDriverSession().get(SessionManager.RACE);
        EthnicityCode = sessionManager.getDriverSession().get(SessionManager.ETHNCITY);
        CommDriversLicense = sessionManager.getDriverSession().get(SessionManager.COMM_DRIVER_LICENSE);


        //HEADER'
        CitationNumber=sessionManager.getHeaderSession().get(SessionManager.CITATION_NUMBER);
        OwnersResponsibility = sessionManager.getHeaderSession().get(SessionManager.OWNER_RESPONSIBILITY);
        Traffic=sessionManager.getHeaderSession().get(SessionManager.TRAFFIC);
        NonTraffic=sessionManager.getHeaderSession().get(SessionManager.NON_TRAFFIC);
        PED=sessionManager.getHeaderSession().get(SessionManager.PED);
        CourtCode = sessionManager.getHeaderSession().get(SessionManager.COURT_CODE);
        LEA = sessionManager.getHeaderSession().get(SessionManager.LEA);

        //VEHICLE

        if (OwnersResponsibility.equals("Y")){
            OwnerFirstName = sessionManager.getVehicleSession().get(SessionManager.OWNER_FIRST_NAME);
            OwnerMiddleName = sessionManager.getVehicleSession().get(SessionManager.OWNER_MIDDLE_NAME);
            OwnerLastName = sessionManager.getVehicleSession().get(SessionManager.OWNER_LAST_NAME);
            OwnerSuffix = sessionManager.getVehicleSession().get(SessionManager.OWNER_SUFFIX);
            OwnerAddress = sessionManager.getVehicleSession().get(SessionManager.OWNER_ADDRESS1);
            OwnerAddressLine2 = sessionManager.getVehicleSession().get(SessionManager.OWNER_ADDRESS2);
            OwnerCity = sessionManager.getVehicleSession().get(SessionManager.OWNER_CITY);
            OwnerState = sessionManager.getVehicleSession().get(SessionManager.OWNER_STATE);
            OwnerZipCode = sessionManager.getVehicleSession().get(SessionManager.OWNER_ZIPCODE);
        }else {
            OwnerFirstName = "";
            OwnerMiddleName = "";
            OwnerLastName = "";
            OwnerSuffix = "";
            OwnerAddress = "";
            OwnerAddressLine2 = "";
            OwnerCity ="";
            OwnerState = "";
            OwnerZipCode = "";
        }

        VehYear = sessionManager.getVehicleSession().get(SessionManager.VEHYEAR);
        VehMake = sessionManager.getVehicleSession().get(SessionManager.VEHMAKE);
        VehModel = sessionManager.getVehicleSession().get(SessionManager.VEHMODEL);
        VehBodyStyle = sessionManager.getVehicleSession().get(SessionManager.VEHBODY);
        VehColor = sessionManager.getVehicleSession().get(SessionManager.VEHCOLOR);
        VehicleType = sessionManager.getVehicleSession().get(SessionManager.VEHTYPE);
        VLN = sessionManager.getVehicleSession().get(SessionManager.VEHNO);
        VLS = sessionManager.getVehicleSession().get(SessionManager.VEHSTATE);
        Overload = sessionManager.getVehicleSession().get(SessionManager.OVERLOAD);
        IsCommercial = sessionManager.getVehicleSession().get(SessionManager.COMMERCIAL);
        IsHazardous = sessionManager.getVehicleSession().get(SessionManager.HAZARDOUS);
        CAFinRespProof = sessionManager.getVehicleSession().get(SessionManager.POLICYNO);
       // Log.e("checkkkkkk", CitationNumber+ OwnersResponsibility+  Traffic+ NonTraffic+PED, FirstName,  MiddleName,  LastName,  Suffix,  Address, AddressLine2, City, State, ZipCode, DLN, DriversState, DOB, Sex, HairColor, Eyes, Height, Weight, DriversLicenseType, Race, EthnicityCode, OwnerFirstName, OwnerMiddleName, OwnerLastName, OwnerSuffix, OwnerAddress, OwnerAddressLine2, OwnerCity, OwnerState, OwnerZipCode, VehYear, VehMake, VehModel, VehBodyStyle, VehColor, VehicleType, IsCommercial, VLN, VLS, IsHazardous, Overload, CAFinRespProof, ViolCode1, ViolCode2, ViolCode3, ViolCode4, ViolCode5, ViolCode6, ViolCode7, ViolCode8, ViolCode1Correctable, ViolCode2Correctable, ViolCode3Correctable, ViolCode4Correctable, ViolCode5Correctable, ViolCode6Correctable, ViolCode7Correctable, ViolCode8Correctable, IssueDate, Time, AMPM, AppSpeed, MaxSpeed, SchoolZone, ViolCity, ViolStreet, StreetType, ViolCrossStreet, ViolCrossStreetType, AppearDate, AppearanceTime, OfficerBadge, OfficerLastName, Area, Division, Detail, CAToBeNotified, CACiteNotSignedByDriver, CourtCode, LEA, VehLimit, SafeSpeed,  Animal1,  Animal2+ Animal3+ Animal4+ Animal5+  Animal6+  Animal7+ Animal8+  NightCourt+ CommDriversLicense+ created_at+created_by+ updated_by+ updated_at);
    }

    private void savefiletoExternalStorage(){

        String username = sessionManager.getAdminLogin().get(SessionManager.NAME);
        String specialChar ="@";
        String badge_no = sessionManager.getAdminLogin().get(SessionManager.BADGE_NO);
        String firstChar = username.substring(0, 1).toUpperCase();

        filename = badge_no+username+"_"+"CitEntry";
        filepath = "MyFileDir";


      //  fileContent = "Radhika here"+ "\n";

        File myExternalFile = new File(getExternalFilesDir(filepath), filename);
        FileOutputStream fos =null;
        try {

            fos = new FileOutputStream(myExternalFile,true);
            fos.write(fileContent.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Toast.makeText(ActivityHome.this, "Information saved to SD card.", Toast.LENGTH_SHORT).show();


    }

    private void permissions(){
        Dexter.withActivity(ActivitySignature.this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {


                            Bitmap signature = mSig.getBitmap();

                            EncodedImage = "data:image/png;base64,"+toBase64(signature);


                            getAllDatatoString();
                            DatabaseAccess databaseAccess = new DatabaseAccess(ActivitySignature.this);
                            databaseAccess.open();
                            databaseAccess.InsertDatatoDataPasses(CitationNumber, OwnersResponsibility,  Traffic, NonTraffic,  PED, FirstName,  MiddleName,  LastName,  Suffix,  Address, AddressLine2, City, State, ZipCode, DLN, DriversState, DOB, Sex, HairColor, Eyes, Height, Weight, DriversLicenseType, Race, EthnicityCode, OwnerFirstName, OwnerMiddleName, OwnerLastName, OwnerSuffix, OwnerAddress, OwnerAddressLine2, OwnerCity, OwnerState, OwnerZipCode, VehYear, VehMake, VehModel, VehBodyStyle, VehColor, VehicleType, IsCommercial, VLN, VLS, IsHazardous, Overload, CAFinRespProof, ViolCode1, ViolCode2, ViolCode3, ViolCode4, ViolCode5, ViolCode6, ViolCode7, ViolCode8, ViolCode1Correctable, ViolCode2Correctable, ViolCode3Correctable, ViolCode4Correctable, ViolCode5Correctable, ViolCode6Correctable, ViolCode7Correctable, ViolCode8Correctable, IssueDate, Time, AMPM, AppSpeed, MaxSpeed, SchoolZone, ViolCity, ViolStreet, StreetType, ViolCrossStreet, ViolCrossStreetType, AppearDate, AppearanceTime, OfficerBadge, OfficerLastName, Area, Division, Detail, CAToBeNotified, CACiteNotSignedByDriver, CourtCode, LEA, VehLimit, SafeSpeed,  Animal1,  Animal2,  Animal3,  Animal4,  Animal5,  Animal6,  Animal7,  Animal8,  NightCourt,  CommDriversLicense, status, created_at, created_by, updated_by, updated_at,online_update);

                            String Content = "insert into data_passes (`CitationNumber`, `OwnersResponsibility`, `Traffic`, `NonTraffic`, `PED`, `FirstName`, `MiddleName`, `LastName`, `Suffix`, `Address`, `AddressLine2`, `City`, `State`, `ZipCode`, `DLN`, `DriversState`, `DOB`, `Sex`, `HairColor`, `Eyes`, `Height`, `Weight`, `DriversLicenseType`, `Race`, `EthnicityCode`, `OwnerFirstName`, `OwnerMiddleName`, `OwnerLastName`, `OwnerSuffix`, `OwnerAddress`, `OwnerAddressLine2`, `OwnerCity`, `OwnerState`, `OwnerZipCode`, `VehYear`, `VehMake`, `VehModel`, `VehBodyStyle`, `VehColor`, `VehicleType`, `IsCommercial`, `VLN`, `VLS`, `IsHazardous`, `Overload`, `CAFinRespProof`, `ViolCode1`, `ViolCode2`, `ViolCode3`, `ViolCode4`, `ViolCode5`, `ViolCode6`, `ViolCode7`, `ViolCode8`, `ViolCode1Correctable`, `ViolCode2Correctable`, `ViolCode3Correctable`, `ViolCode4Correctable`, `ViolCode5Correctable`, `ViolCode6Correctable`, `ViolCode7Correctable`, `ViolCode8Correctable`, `IssueDate`, `Time`, `AMPM`, `AppSpeed`, `MaxSpeed`, `SchoolZone`, `ViolCity`, `ViolStreet`, `StreetType`, `ViolCrossStreet`, `ViolCrossStreetType`, `AppearDate`, `AppearanceTime`, `OfficerBadge`, `OfficerLastName`, `Area`, `Division`, `Detail`, `CAToBeNotified`, `CACiteNotSignedByDriver`, `CourtCode`, `LEA`, `VehLimit`, `SafeSpeed`, `Animal1`, `Animal2`, `Animal3`, `Animal4`, `Animal5`, `Animal6`, `Animal7`, `Animal8`, `NightCourt`, `CommDriversLicense`,`status`, `created_at`, `created_by`, `updated_by`, `updated_at`,`online_update`)"
                                    + "values('"+CitationNumber+"','"+OwnersResponsibility+"','"+Traffic+"','"+NonTraffic+"', '"+PED+"', '"+FirstName+"', '"+MiddleName+"', '"+LastName+"', '"+Suffix+"', '"+Address+"', '"+AddressLine2+"', '"+City+"', '"+State+"', '"+ZipCode+"', '"+DLN+"', '"+DriversState+"', '"+DOB+"', '"+Sex+"', '"+HairColor+"', '"+Eyes+"', '"+Height+"', '"+Weight+"', '"+DriversLicenseType+"','"+Race+"', '"+EthnicityCode+"', '"+OwnerFirstName+"', '"+OwnerMiddleName+"', '"+OwnerLastName+"', '"+OwnerSuffix+"', '"+OwnerAddress+"', '"+OwnerAddressLine2+"', '"+OwnerCity+"', '"+OwnerState+"', '"+OwnerZipCode+"', '"+VehYear+"','"+VehMake+"', '"+VehModel+"', '"+VehBodyStyle+"', '"+VehColor+"', '"+VehicleType+"', '"+IsCommercial+"', '"+VLN+"', '"+VLS+"', '"+IsHazardous+"', '"+Overload+"', '"+CAFinRespProof+"', '"+ViolCode1+"', '"+ViolCode2+"', '"+ViolCode3+"', '"+ViolCode4+"', '"+ViolCode5+"', '"+ViolCode6+"', '"+ViolCode7+"', '"+ViolCode8+"', '"+ViolCode1Correctable+"', '"+ViolCode2Correctable+"', '"+ViolCode3Correctable+"', '"+ViolCode4Correctable+"', '"+ViolCode5Correctable+"', '"+ViolCode6Correctable+"', '"+ViolCode7Correctable+"', '"+ViolCode8Correctable+"', '"+IssueDate+"', '"+Time+"', '"+AMPM+"', '"+AppSpeed+"', '"+MaxSpeed+"', '"+SchoolZone+"', '"+ViolCity+"', '"+ViolStreet+"', '"+StreetType+"', '"+ViolCrossStreet+"', '"+ViolCrossStreetType+"', '"+AppearDate+"', '"+AppearanceTime+"', '"+OfficerBadge+"', '"+OfficerLastName+"', '"+Area+"', '"+Division+"', '"+Detail+"', '"+CAToBeNotified+"', '"+CACiteNotSignedByDriver+"', '"+CourtCode+"', '"+LEA+"', '"+VehLimit+"', '"+SafeSpeed+"', '"+Animal1+"',  '"+Animal2+"',  '"+Animal3+"',  '"+Animal4+"',  '"+Animal5+"',  '"+Animal6+"',  '"+Animal7+"',  '"+Animal8+"',  '"+NightCourt+"',  '"+CommDriversLicense+"',  '"+status+"','"+created_at+"', '"+created_by+"', '"+updated_by+"', '"+updated_at+"','"+online_update+"' );";

                            String username = sessionManager.getAdminLogin().get(SessionManager.NAME);
                            String specialChar ="@";
                            String badge_no = sessionManager.getAdminLogin().get(SessionManager.BADGE_NO);
                            String firstChar = username.substring(0, 1).toUpperCase();

                            String finalS = firstChar+specialChar+badge_no+Content;
                            fileContent = MD5.encrypt(finalS);

                            savefiletoExternalStorage();

                            String data_entry_id = databaseAccess.getSaveDataId();
                            databaseAccess.close();

                            DatabaseAccess databaseAccess2 = new DatabaseAccess(ActivitySignature.this);
                            databaseAccess2.open();
                            databaseAccess2.InsertDatatoSignature(data_entry_id,EncodedImage,"","",user_id,"","","");
                            databaseAccess2.close();

                            if (NetworkConectivity.checkConnectivity(ActivitySignature.this)){

                                LangId = ConstantCodes.LANG_ID;
                                UniqueId = UUID.randomUUID().toString();
                                callGetToken();

                            } else {
                             //   sessionManager.ClearDataEntery();
                                Intent i = new Intent(ActivitySignature.this, ActivityConfirmation.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                                overridePendingTransition(R.anim.enter,R.anim.exit);
                            }

                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void callGetToken() {

        progressdialog = new ProgressDialog(ActivitySignature.this);
        progressdialog.setMessage("Loading....");
        progressdialog.setCancelable(false);
        progressdialog.show();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        officer_token officer_token = new officer_token(ConstantCodes.LANG_ID,user_id,device_no);
        Call<JsonObject> call = api.getToken(officer_token);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {


                Log.e("status", response.toString());
                assert response.body() != null;


                try {
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));

                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));
                        JSONObject userdata = jsonArray.getJSONObject(0);

                        TOKEN_VALUE = userdata.getString("token");

                        getAllCitationNumber();
                        callUpdateSyncDB();
                    }else {
                        progressdialog.dismiss();
                     //   Toast.makeText(ActivitySignature.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    progressdialog.dismiss();
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                progressdialog.dismiss();
                //  Toast.makeText(ActivityHome.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }

    private void getAllCitationNumber(){

        String user_id = sessionManager.getAdminLogin().get(SessionManager.USER_ID);
        DatabaseAccess databaseAccess = new DatabaseAccess(ActivitySignature.this);
        databaseAccess.open();
        resultSet = databaseAccess.getResults(ActivitySignature.this,user_id);
        databaseAccess.close();


    }

    private void callUpdateSyncDB() {

//        final ProgressDialog progressdialog = new ProgressDialog(ActivityHome.this);
//        progressdialog.setMessage("Loading....");
//        progressdialog.show();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                okhttp3.Request request = original.newBuilder()
                        .addHeader(ConstantCodes.KEY_BASIC_AUTH,ConstantCodes.KEY_BASIC_AUTH_VALUE)
                        .addHeader(ConstantCodes.KEY_TOKEN,TOKEN_VALUE)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        Log.e("checkdata",UniqueId+" "+device_no+" "+resultSet);
        save_data_entry save_data_entry = new save_data_entry(ConstantCodes.LANG_ID,UniqueId,device_no,resultSet);
        Call<JsonObject> call = api.setDataEntry(save_data_entry);


        //Log.e("datasasasa2", resultSet);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {

//                progressdialog.dismiss();
                //String s = null;

                assert response.body() != null;




                try {
                    Log.e("data_123", "true" + response.code());
                    if(response.code()==500){
                        progressdialog.dismiss();
                        CitationNumber=sessionManager.getHeaderSession().get(SessionManager.CITATION_NUMBER);

                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivitySignature.this);
                        databaseAccess.open();
                        databaseAccess.deleteDataFromDataPasses(CitationNumber);
                        databaseAccess.close();
                        //  sessionManager.ClearDataEntery();
                        //Toast.makeText(ActivitySignature.this,"Response code 500",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ActivitySignature.this, ActivityFailed.class);
                        i.putExtra("citation_number",CitationNumber);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.enter,R.anim.exit);
                    }
                    JSONObject totaldata = new JSONObject(String.valueOf(response.body()));
                    //Toast.makeText(ActivitySignature.this,totaldata.toString(),Toast.LENGTH_SHORT).show();
                    Log.e("data_1234556", "true" + totaldata.toString());
                    if (totaldata.getString("success").equals("1")){

                        JSONArray jsonArray = new JSONArray(totaldata.getString("data"));
                        JSONObject userdata = jsonArray.getJSONObject(0);
                        Log.e("data_123", "true" + jsonArray.toString());
                        String synced_citation_no= userdata.getString("synced_citation_no");
                        String check = synced_citation_no .replace("[", "(").replace("]", ")");


                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivitySignature.this);
                        databaseAccess.open();
                        databaseAccess.UpdateDatatoDataPasses(check);
                        databaseAccess.close();

                        progressdialog.dismiss();

                        Intent i = new Intent(ActivitySignature.this, ActivityConfirmation.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.enter,R.anim.exit);


                    }else if (totaldata.getString("success").equals("4")){
                        //Toast.makeText(ActivitySignature.this,totaldata.getString("msg"),Toast.LENGTH_SHORT).show();
                    }else {
                        progressdialog.dismiss();
                        CitationNumber=sessionManager.getHeaderSession().get(SessionManager.CITATION_NUMBER);
                        DatabaseAccess databaseAccess = new DatabaseAccess(ActivitySignature.this);
                        databaseAccess.open();
                        databaseAccess.deleteDataFromDataPasses(CitationNumber);
                        databaseAccess.close();
                        //  sessionManager.ClearDataEntery();
                        //Toast.makeText(ActivitySignature.this,"Error:"+response.toString(),Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ActivitySignature.this, ActivityFailed.class);
                        i.putExtra("citation_number",CitationNumber);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.enter,R.anim.exit);
                    }




                } catch (Exception ex) {
                    progressdialog.dismiss();
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                progressdialog.dismiss();
                //     Toast.makeText(ActivityHome.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("data t", t.getMessage());
            }
        });

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySignature.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}