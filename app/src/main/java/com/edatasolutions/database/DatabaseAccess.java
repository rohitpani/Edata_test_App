package com.edatasolutions.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseAccess {

    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    Cursor c = null;

    public DatabaseAccess(Context context){

        this.sqLiteOpenHelper=new DbHelper(context);
    }

    public static DatabaseAccess getInstance(Context context){
        if (instance==null){
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open(){
        this.database = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close(){
        if (database!=null){
            database.close();
        }
    }

    public String getPassword(String email){
        c=database.rawQuery("select password from users where email = '"+email+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String pass = c.getString(0);
            buffer.append(""+pass);
        }
        return buffer.toString();
    }

    public String getUserRoleId(String email){
        c=database.rawQuery("select role_id from users where email = '"+email+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String pass = c.getString(0);
            buffer.append(""+pass);
        }
        return buffer.toString();
    }

    public ArrayList<String> getRole(){
        String status = "Y";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select user_role from roles where status = '"+status+"'", new String[]{});
       // StringBuffer buffer = new StringBuffer();
       // arrayList.add("Select Type");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
           // buffer.append(""+role);
        }
        return arrayList;
    }


    public String getOfficerId(String email){

        c=database.rawQuery("select id from users where email = '"+email+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getOfficerBadgeNo(String email){

        c=database.rawQuery("select badge_no from users where email = '"+email+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getOfficerName(String email){

        c=database.rawQuery("select name from users where email = '"+email+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getroleId(String role){
        c=database.rawQuery("select id from roles where user_role = '"+role+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getBadgeNumber(String user_id){
        c=database.rawQuery("select badge_no from users where id = '"+user_id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }


    public String getStartingCitationNumber(String id){

        c=database.rawQuery("select citation_series_from from users where id = '"+id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String num  = c.getString(0);
            buffer.append(""+num);
        }
        return buffer.toString();
    }

    public String getLastCitationNumber(String id){

        c=database.rawQuery("select citation_series_to from users where id = '"+id+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String num  = c.getString(0);
            buffer.append(""+num);
        }
        return buffer.toString();
    }

    public String getLastUpdatedCitationNumber(String user_id){

      //  c=database.rawQuery("select CitationNumber from data_passes where created_by = '"+user_id+"' ", new String[]{});

        c=database.rawQuery("SELECT max(CitationNumber) as last_citation_no FROM `data_passes` where created_by = '"+user_id+"'", new String[]{});


        StringBuffer buffer = new StringBuffer();

        while (c.moveToNext()){
            String id = c.getString(0);
            buffer.append(""+id);
        }

        return buffer.toString();
    }




    public ArrayList<String> getVehDescription(){
        String vehtype = "VehicleType";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select code_description from common_codes where code_type = '"+vehtype+"'", new String[]{});
        arrayList.add("VEHICLE TYPE");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }


    public String getVehType(String code_description){
        String vehtype = "VehicleType";
        c=database.rawQuery("select code_value from common_codes where code_type = '"+vehtype+"' AND  code_description = '"+code_description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getVehTypeId(String code_value, String code_description){
        String vehtype = "VehicleType";
        c=database.rawQuery("select id from common_codes where code_description = '"+code_description+"' AND code_type = '"+vehtype+"' AND  code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getVehTypeName(String code_value, String id){
        String vehtype = "VehicleType";
        c=database.rawQuery("select code_description from common_codes where code_type = '"+vehtype+"' AND id = '"+id+"' AND  code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }


    public ArrayList<String> getSuffix(){
        String string = "Suffix";
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select field_value from common_fields where field_type = '"+string+"'", new String[]{});
        arrayList.add("SUFFIX");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSuffixId(String field_value){
        String string = "Suffix";
        c=database.rawQuery("select id from common_fields where field_type = '"+string+"' AND  field_value = '"+field_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSuffixName(String id){
        String string = "Suffix";
        c=database.rawQuery("select field_value from common_fields where field_type = '"+string+"' AND  id = '"+id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }


    public String checkSingleZipcode(String state_id, String city_id){
        c=database.rawQuery("select zip from zip_city_states where state_id = '"+state_id+"' AND city_id = '"+city_id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public ArrayList<String> checkZipcode(String state_id, String city_id){

        Log.e("heyy",state_id+"  "+city_id);
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select zip from zip_city_states where state_id = '"+state_id+"' AND city_id = '"+city_id+"'  ", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getBaseString(String user_id){

        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select sign_string from signatures where created_by = '"+user_id+"' ", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String checkNameWithNameMaster(String name){
        c=database.rawQuery("select value from name_masters where value = '"+name+"' ", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }



    public void DeleteDatatoDataPasses(String CitationNumber,String OwnersResponsibility, String Traffic,String NonTraffic, String PED,String FirstName, String MiddleName, String LastName, String Suffix,String  Address,String AddressLine2,String City,String State,String ZipCode,String DLN,String DriversState,String DOB,String Sex,String HairColor,String Eyes,String Height,String Weight,String DriversLicenseType,String Race,String EthnicityCode,String OwnerFirstName,String OwnerMiddleName,String OwnerLastName,String OwnerSuffix,String OwnerAddress,String OwnerAddressLine2,String OwnerCity,String OwnerState,String OwnerZipCode,String VehYear,String VehMake,String VehModel,String VehBodyStyle,String VehColor,String VehicleType,String IsCommercial,String VLN,String VLS,String IsHazardous,String Overload,String CAFinRespProof,String ViolCode1,String ViolCode2,String ViolCode3,String ViolCode4,String ViolCode5,String ViolCode6,String ViolCode7,String ViolCode8,String ViolCode1Correctable,String ViolCode2Correctable,String ViolCode3Correctable,String ViolCode4Correctable,String ViolCode5Correctable,String ViolCode6Correctable,String ViolCode7Correctable,String ViolCode8Correctable,String IssueDate,String Time,String AMPM,String AppSpeed,String MaxSpeed,String SchoolZone,String ViolCity,String ViolStreet,String StreetType,String ViolCrossStreet,String ViolCrossStreetType,String AppearDate,String AppearanceTime,String OfficerBadge,String OfficerLastName,String Area,String Division,String Detail,String CAToBeNotified,String CACiteNotSignedByDriver,String CourtCode,String LEA,String VehLimit,String SafeSpeed, String Animal1, String Animal2, String Animal3, String Animal4, String Animal5, String Animal6, String Animal7, String Animal8, String NightCourt, String CommDriversLicense,String status,String created_at,String created_by,String updated_by,String updated_at, String online_update){

        database.execSQL("insert into data_passes (`CitationNumber`, `OwnersResponsibility`, `Traffic`, `NonTraffic`, `PED`, `FirstName`, `MiddleName`, `LastName`, `Suffix`, `Address`, `AddressLine2`, `City`, `State`, `ZipCode`, `DLN`, `DriversState`, `DOB`, `Sex`, `HairColor`, `Eyes`, `Height`, `Weight`, `DriversLicenseType`, `Race`, `EthnicityCode`, `OwnerFirstName`, `OwnerMiddleName`, `OwnerLastName`, `OwnerSuffix`, `OwnerAddress`, `OwnerAddressLine2`, `OwnerCity`, `OwnerState`, `OwnerZipCode`, `VehYear`, `VehMake`, `VehModel`, `VehBodyStyle`, `VehColor`, `VehicleType`, `IsCommercial`, `VLN`, `VLS`, `IsHazardous`, `Overload`, `CAFinRespProof`, `ViolCode1`, `ViolCode2`, `ViolCode3`, `ViolCode4`, `ViolCode5`, `ViolCode6`, `ViolCode7`, `ViolCode8`, `ViolCode1Correctable`, `ViolCode2Correctable`, `ViolCode3Correctable`, `ViolCode4Correctable`, `ViolCode5Correctable`, `ViolCode6Correctable`, `ViolCode7Correctable`, `ViolCode8Correctable`, `IssueDate`, `Time`, `AMPM`, `AppSpeed`, `MaxSpeed`, `SchoolZone`, `ViolCity`, `ViolStreet`, `StreetType`, `ViolCrossStreet`, `ViolCrossStreetType`, `AppearDate`, `AppearanceTime`, `OfficerBadge`, `OfficerLastName`, `Area`, `Division`, `Detail`, `CAToBeNotified`, `CACiteNotSignedByDriver`, `CourtCode`, `LEA`, `VehLimit`, `SafeSpeed`, `Animal1`, `Animal2`, `Animal3`, `Animal4`, `Animal5`, `Animal6`, `Animal7`, `Animal8`, `NightCourt`, `CommDriversLicense`,`status`, `created_at`, `created_by`, `updated_by`, `updated_at`,`online_update`)"
                + "values('"+CitationNumber+"','"+OwnersResponsibility+"','"+Traffic+"','"+NonTraffic+"', '"+PED+"', '"+FirstName+"', '"+MiddleName+"', '"+LastName+"', '"+Suffix+"', '"+Address+"', '"+AddressLine2+"', '"+City+"', '"+State+"', '"+ZipCode+"', '"+DLN+"', '"+DriversState+"', '"+DOB+"', '"+Sex+"', '"+HairColor+"', '"+Eyes+"', '"+Height+"', '"+Weight+"', '"+DriversLicenseType+"','"+Race+"', '"+EthnicityCode+"', '"+OwnerFirstName+"', '"+OwnerMiddleName+"', '"+OwnerLastName+"', '"+OwnerSuffix+"', '"+OwnerAddress+"', '"+OwnerAddressLine2+"', '"+OwnerCity+"', '"+OwnerState+"', '"+OwnerZipCode+"', '"+VehYear+"','"+VehMake+"', '"+VehModel+"', '"+VehBodyStyle+"', '"+VehColor+"', '"+VehicleType+"', '"+IsCommercial+"', '"+VLN+"', '"+VLS+"', '"+IsHazardous+"', '"+Overload+"', '"+CAFinRespProof+"', '"+ViolCode1+"', '"+ViolCode2+"', '"+ViolCode3+"', '"+ViolCode4+"', '"+ViolCode5+"', '"+ViolCode6+"', '"+ViolCode7+"', '"+ViolCode8+"', '"+ViolCode1Correctable+"', '"+ViolCode2Correctable+"', '"+ViolCode3Correctable+"', '"+ViolCode4Correctable+"', '"+ViolCode5Correctable+"', '"+ViolCode6Correctable+"', '"+ViolCode7Correctable+"', '"+ViolCode8Correctable+"', '"+IssueDate+"', '"+Time+"', '"+AMPM+"', '"+AppSpeed+"', '"+MaxSpeed+"', '"+SchoolZone+"', '"+ViolCity+"', '"+ViolStreet+"', '"+StreetType+"', '"+ViolCrossStreet+"', '"+ViolCrossStreetType+"', '"+AppearDate+"', '"+AppearanceTime+"', '"+OfficerBadge+"', '"+OfficerLastName+"', '"+Area+"', '"+Division+"', '"+Detail+"', '"+CAToBeNotified+"', '"+CACiteNotSignedByDriver+"', '"+CourtCode+"', '"+LEA+"', '"+VehLimit+"', '"+SafeSpeed+"', '"+Animal1+"',  '"+Animal2+"',  '"+Animal3+"',  '"+Animal4+"',  '"+Animal5+"',  '"+Animal6+"',  '"+Animal7+"',  '"+Animal8+"',  '"+NightCourt+"',  '"+CommDriversLicense+"',  '"+status+"','"+created_at+"', '"+created_by+"', '"+updated_by+"', '"+updated_at+"','"+online_update+"' );");

    }


    public void InsertDatatoDataPasses(String CitationNumber,String OwnersResponsibility, String Traffic,String NonTraffic, String PED,String FirstName, String MiddleName, String LastName, String Suffix,String  Address,String AddressLine2,String City,String State,String ZipCode,String DLN,String DriversState,String DOB,String Sex,String HairColor,String Eyes,String Height,String Weight,String DriversLicenseType,String Race,String EthnicityCode,String OwnerFirstName,String OwnerMiddleName,String OwnerLastName,String OwnerSuffix,String OwnerAddress,String OwnerAddressLine2,String OwnerCity,String OwnerState,String OwnerZipCode,String VehYear,String VehMake,String VehModel,String VehBodyStyle,String VehColor,String VehicleType,String IsCommercial,String VLN,String VLS,String IsHazardous,String Overload,String CAFinRespProof,String ViolCode1,String ViolCode2,String ViolCode3,String ViolCode4,String ViolCode5,String ViolCode6,String ViolCode7,String ViolCode8,String ViolCode1Correctable,String ViolCode2Correctable,String ViolCode3Correctable,String ViolCode4Correctable,String ViolCode5Correctable,String ViolCode6Correctable,String ViolCode7Correctable,String ViolCode8Correctable,String IssueDate,String Time,String AMPM,String AppSpeed,String MaxSpeed,String SchoolZone,String ViolCity,String ViolStreet,String StreetType,String ViolCrossStreet,String ViolCrossStreetType,String AppearDate,String AppearanceTime,String OfficerBadge,String OfficerLastName,String Area,String Division,String Detail,String CAToBeNotified,String CACiteNotSignedByDriver,String CourtCode,String LEA,String VehLimit,String SafeSpeed, String Animal1, String Animal2, String Animal3, String Animal4, String Animal5, String Animal6, String Animal7, String Animal8, String NightCourt, String CommDriversLicense,String status,String created_at,String created_by,String updated_by,String updated_at, String online_update){

        database.execSQL("insert into data_passes (`CitationNumber`, `OwnersResponsibility`, `Traffic`, `NonTraffic`, `PED`, `FirstName`, `MiddleName`, `LastName`, `Suffix`, `Address`, `AddressLine2`, `City`, `State`, `ZipCode`, `DLN`, `DriversState`, `DOB`, `Sex`, `HairColor`, `Eyes`, `Height`, `Weight`, `DriversLicenseType`, `Race`, `EthnicityCode`, `OwnerFirstName`, `OwnerMiddleName`, `OwnerLastName`, `OwnerSuffix`, `OwnerAddress`, `OwnerAddressLine2`, `OwnerCity`, `OwnerState`, `OwnerZipCode`, `VehYear`, `VehMake`, `VehModel`, `VehBodyStyle`, `VehColor`, `VehicleType`, `IsCommercial`, `VLN`, `VLS`, `IsHazardous`, `Overload`, `CAFinRespProof`, `ViolCode1`, `ViolCode2`, `ViolCode3`, `ViolCode4`, `ViolCode5`, `ViolCode6`, `ViolCode7`, `ViolCode8`, `ViolCode1Correctable`, `ViolCode2Correctable`, `ViolCode3Correctable`, `ViolCode4Correctable`, `ViolCode5Correctable`, `ViolCode6Correctable`, `ViolCode7Correctable`, `ViolCode8Correctable`, `IssueDate`, `Time`, `AMPM`, `AppSpeed`, `MaxSpeed`, `SchoolZone`, `ViolCity`, `ViolStreet`, `StreetType`, `ViolCrossStreet`, `ViolCrossStreetType`, `AppearDate`, `AppearanceTime`, `OfficerBadge`, `OfficerLastName`, `Area`, `Division`, `Detail`, `CAToBeNotified`, `CACiteNotSignedByDriver`, `CourtCode`, `LEA`, `VehLimit`, `SafeSpeed`, `Animal1`, `Animal2`, `Animal3`, `Animal4`, `Animal5`, `Animal6`, `Animal7`, `Animal8`, `NightCourt`, `CommDriversLicense`,`status`, `created_at`, `created_by`, `updated_by`, `updated_at`,`online_update`)"
                + "values('"+CitationNumber+"','"+OwnersResponsibility+"','"+Traffic+"','"+NonTraffic+"', '"+PED+"', '"+FirstName+"', '"+MiddleName+"', '"+LastName+"', '"+Suffix+"', '"+Address+"', '"+AddressLine2+"', '"+City+"', '"+State+"', '"+ZipCode+"', '"+DLN+"', '"+DriversState+"', '"+DOB+"', '"+Sex+"', '"+HairColor+"', '"+Eyes+"', '"+Height+"', '"+Weight+"', '"+DriversLicenseType+"','"+Race+"', '"+EthnicityCode+"', '"+OwnerFirstName+"', '"+OwnerMiddleName+"', '"+OwnerLastName+"', '"+OwnerSuffix+"', '"+OwnerAddress+"', '"+OwnerAddressLine2+"', '"+OwnerCity+"', '"+OwnerState+"', '"+OwnerZipCode+"', '"+VehYear+"','"+VehMake+"', '"+VehModel+"', '"+VehBodyStyle+"', '"+VehColor+"', '"+VehicleType+"', '"+IsCommercial+"', '"+VLN+"', '"+VLS+"', '"+IsHazardous+"', '"+Overload+"', '"+CAFinRespProof+"', '"+ViolCode1+"', '"+ViolCode2+"', '"+ViolCode3+"', '"+ViolCode4+"', '"+ViolCode5+"', '"+ViolCode6+"', '"+ViolCode7+"', '"+ViolCode8+"', '"+ViolCode1Correctable+"', '"+ViolCode2Correctable+"', '"+ViolCode3Correctable+"', '"+ViolCode4Correctable+"', '"+ViolCode5Correctable+"', '"+ViolCode6Correctable+"', '"+ViolCode7Correctable+"', '"+ViolCode8Correctable+"', '"+IssueDate+"', '"+Time+"', '"+AMPM+"', '"+AppSpeed+"', '"+MaxSpeed+"', '"+SchoolZone+"', '"+ViolCity+"', '"+ViolStreet+"', '"+StreetType+"', '"+ViolCrossStreet+"', '"+ViolCrossStreetType+"', '"+AppearDate+"', '"+AppearanceTime+"', '"+OfficerBadge+"', '"+OfficerLastName+"', '"+Area+"', '"+Division+"', '"+Detail+"', '"+CAToBeNotified+"', '"+CACiteNotSignedByDriver+"', '"+CourtCode+"', '"+LEA+"', '"+VehLimit+"', '"+SafeSpeed+"', '"+Animal1+"',  '"+Animal2+"',  '"+Animal3+"',  '"+Animal4+"',  '"+Animal5+"',  '"+Animal6+"',  '"+Animal7+"',  '"+Animal8+"',  '"+NightCourt+"',  '"+CommDriversLicense+"',  '"+status+"','"+created_at+"', '"+created_by+"', '"+updated_by+"', '"+updated_at+"','"+online_update+"' );");

    }

    public void UpdateDatatoDataPasses(String CitationNumber){
//     update tblName set  onlineupd = 'Y' where citNo In ('1001','1002')
        Log.e("getttt",CitationNumber);

     //   UPDATE data_passes SET 'online_update' = 'Y'  WHERE  CitationNumber IN ('1001','1002')
        database.execSQL("UPDATE data_passes SET " + "'online_update' = 'Y'" + "WHERE  CitationNumber IN "+CitationNumber+" ");



    }

    public String GetDatatoDataPasses(){

        c = database.rawQuery("SELECT data_passes.*  FROM `data_passes` WHERE `online_update`='Y'", new String[]{});
        //  c=database.rawQuery("select last id from data_passes where id = '"+id+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String num  = c.getString(0);
            buffer.append(""+num);
        }
        return buffer.toString();
    }

    public void InsertDatatoSignature(String data_entry_id,String sign_string, String sign_binary,String image_path, String created_by,String updated_by, String created_at, String updated_at){
        database.execSQL("insert into signatures (`data_entry_id`, `sign_string`, `sign_binary`, `image_path`, `created_by`, `updated_by`, `created_at`, `updated_at`)"
                    + "values('"+data_entry_id+"','"+sign_string+"','"+sign_binary+"','"+image_path+"', '"+created_by+"', '"+updated_by+"', '"+created_at+"', '"+updated_at+"' );");
    }

    public String getSaveDataId(){

        c=database.rawQuery("SELECT * FROM data_passes ORDER BY id DESC LIMIT 1", null);
      //  c=database.rawQuery("select last id from data_passes where id = '"+id+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String num  = c.getString(0);
            buffer.append(""+num);
        }
        return buffer.toString();
    }

    public void deleteDataFromDataPasses(String citation_no){

        database.execSQL("DELETE FROM `data_passes` WHERE `CitationNumber`= '"+citation_no+"' " , new String[]{});
        //  c=database.rawQuery("select last id from data_passes where id = '"+id+"'", new String[]{});

    }

    public JsonArray getResults(Context context, String user_id)
    {

//        String online_update = "N";
        String myPath = context.getDatabasePath("edata.db").toString();// Set path to your database

        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        String searchQuery = "SELECT data_passes.* , signatures.sign_string image FROM `data_passes` join signatures ON (signatures.data_entry_id = data_passes.id) WHERE `online_update`='N' and data_passes.created_by = '"+user_id+"'";
        Cursor cursor = myDataBase.rawQuery(searchQuery, null );

        JSONArray resultSet = new JSONArray();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try

                    {
                        if( cursor.getString(i) != null )
                        {
                            Log.e("TAG_NAME", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i));
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d("TAG_NAME123", e.getMessage()  );
                    }
                }
            }



          //  Log.d("TAG_NAME1", jsonObject.toString());
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(String.valueOf(resultSet)).getAsJsonArray();
        Log.d("TAG_NAME2", jsonArray.toString());
        return jsonArray;
    }

    public ArrayList<String> getAllCitationNumber(String user_id){

        ArrayList<String> arrayList = new ArrayList<>();

        c = database.query("data_passes where created_by = '"+user_id+"'", null, null, null, null, null, null, null);
       // c = database.rawQuery("select * from data_passes where created_by = '"+user_id+"'" ,null);
        //c=database.rawQuery("select * from  , new String[]{});

        Log.e("check1",c.toString());
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String name = c.getString(c.getColumnIndex(null));
               // String name1 = c.getString(c.getColumnIndex("CitationNumber"));

                arrayList.add(name);
            //    arrayList.add(name1);
                c.moveToNext();
            }
        }


//        Log.e("check",c.toString());
//        while (c.moveToNext()){
//            String role = c.getString(0);
//            arrayList.add(role);
//        }
          Log.e("check",arrayList.toString());
        return arrayList;
    }




//    private void InsertDatatoDataPasses(String CitationNumber,String OwnersResponsibility, String Traffic,String NonTraffic, String PED,String FirstName, String MiddleName, String LastName, String Suffix,String  Address,String AddressLine2,String City,String State,String ZipCode,String DLN,String DriversState,String DOB,String Sex,String HairColor,String Eyes,String Height,String Weight,String DriversLicenseType,String Race,String EthnicityCode,String OwnerFirstName,String OwnerMiddleName,String OwnerLastName,String OwnerSuffix,String OwnerAddress,String OwnerAddressLine2,String OwnerCity,String OwnerState,String OwnerZipCode,String VehYear,String VehMake,String VehModel,String VehBodyStyle,String VehColor,String VehicleType,String IsCommercial,String VLN,String VLS,String IsHazardous,String Overload,String CAFinRespProof,String ViolCode1,String ViolCode2,String ViolCode3,String ViolCode4,String ViolCode5,String ViolCode6,String ViolCode7,String ViolCode8,String ViolCode1Correctable,String ViolCode2Correctable,String ViolCode3Correctable,String ViolCode4Correctable,String ViolCode5Correctable,String ViolCode6Correctable,String ViolCode7Correctable,String ViolCode8Correctable,String IssueDate,String Time,String AMPM,String AppSpeed,String MaxSpeed,String SchoolZone,String ViolCity,String ViolStreet,String StreetType,String ViolCrossStreet,String ViolCrossStreetType,String AppearDate,String AppearanceTime,String OfficerBadge,String OfficerLastName,String Area,String Division,String Detail,String CAToBeNotified,String CACiteNotSignedByDriver,String CourtCode,String LEA,String VehLimit,String SafeSpeed, String Animal1, String Animal2, String Animal3, String Animal4, String Animal5, String Animal6, String Animal7, String Animal8, String NightCourt, String CommDriversLicense,String created_at,String created_by,String updated_by,String updated_at){
//
//        database.execSQL("insert into data_passes (`CitationNumber`, `OwnersResponsibility`, `Traffic`, `NonTraffic`, `PED`, `FirstName`, `MiddleName`, `LastName`, `Suffix`, `Address`, `AddressLine2`, `City`, `State`, `ZipCode`, `DLN`, `DriversState`, `DOB`, `Sex`, `HairColor`, `Eyes`, `Height`, `Weight`, `DriversLicenseType`, `Race`, `EthnicityCode`, `OwnerFirstName`, `OwnerMiddleName`, `OwnerLastName`, `OwnerSuffix`, `OwnerAddress`, `OwnerAddressLine2`, `OwnerCity`, `OwnerState`, `OwnerZipCode`, `VehYear`, `VehMake`, `VehModel`, `VehBodyStyle`, `VehColor`, `VehicleType`, `IsCommercial`, `VLN`, `VLS`, `IsHazardous`, `Overload`, `CAFinRespProof`, `ViolCode1`, `ViolCode2`, `ViolCode3`, `ViolCode4`, `ViolCode5`, `ViolCode6`, `ViolCode7`, `ViolCode8`, `ViolCode1Correctable`, `ViolCode2Correctable`, `ViolCode3Correctable`, `ViolCode4Correctable`, `ViolCode5Correctable`, `ViolCode6Correctable`, `ViolCode7Correctable`, `ViolCode8Correctable`, `IssueDate`, `Time`, `AMPM`, `AppSpeed`, `MaxSpeed`, `SchoolZone`, `ViolCity`, `ViolStreet`, `StreetType`, `ViolCrossStreet`, `ViolCrossStreetType`, `AppearDate`, `AppearanceTime`, `OfficerBadge`, `OfficerLastName`, `Area`, `Division`, `Detail`, `CAToBeNotified`, `CACiteNotSignedByDriver`, `CourtCode`, `LEA`, `VehLimit`, `SafeSpeed`, `Animal1`, `Animal2`, `Animal3`, `Animal4`, `Animal5`, `Animal6`, `Animal7`, `Animal8`, `NightCourt`, `CommDriversLicense`, `created_at`, `created_by`, `updated_by`, `updated_at`)"
//                + "values("+CitationNumber+","+OwnersResponsibility+","+Traffic+","+NonTraffic+", "+PED+", "+FirstName+", "+MiddleName+", "+LastName+", "+Suffix+", "+Address+", "+AddressLine2+", "+City+", "+State+", "+ZipCode+", "+DLN+", "+DriversState+", "+DOB+", "+Sex+", "+HairColor+", "+Eyes+", "+Height+", "+Weight+", "+DriversLicenseType+","+Race+", "+EthnicityCode+", "+OwnerFirstName+", "+OwnerMiddleName+", "+OwnerLastName+", "+OwnerSuffix+", "+OwnerAddress+", "+OwnerAddressLine2+", "+OwnerCity+", "+OwnerState+", "+OwnerZipCode+", "+VehYear+","+VehMake+", "+VehModel+", "+VehBodyStyle+", "+VehColor+", "+VehicleType+", "+IsCommercial+", "+VLN+", "+VLS+", "+IsHazardous+", "+Overload+", "+CAFinRespProof+", "+ViolCode1+", "+ViolCode2+", "+ViolCode3+", "+ViolCode4+", "+ViolCode5+", "+ViolCode6+", "+ViolCode7+", "+ViolCode8+", "+ViolCode1Correctable+", "+ViolCode2Correctable+", "+ViolCode3Correctable+", "+ViolCode4Correctable+", "+ViolCode5Correctable+", "+ViolCode6Correctable+", "+ViolCode7Correctable+", "+ViolCode8Correctable+", "+IssueDate+", "+Time+", "+AMPM+", "+AppSpeed+", "+MaxSpeed+", "+SchoolZone+", "+ViolCity+", "+ViolStreet+", "+StreetType+", "+ViolCrossStreet+", "+ViolCrossStreetType+", "+AppearDate+", "+AppearanceTime+", "+OfficerBadge+", "+OfficerLastName+", "+Area+", "+Division+", "+Detail+", "+CAToBeNotified+", "+CACiteNotSignedByDriver+", "+CourtCode+", "+LEA+", "+VehLimit+", "+SafeSpeed+", "+Animal1+",  "+Animal2+",  "+Animal3+",  "+Animal4+",  "+Animal5+",  "+Animal6+",  "+Animal7+",  "+Animal8+",  "+NightCourt+",  "+CommDriversLicense+", "+created_at+", "+created_by+", "+updated_by+", "+updated_at+" );");
//
//
//
//    }




    ///SYNC DATABASE WITH ADMIN

    public void InsertDatatoUsers(String id, String name,String badge_no,String email, String password,String role_id,String citation_series_from,String citation_series_to, String access_token,String mkey, String msalt, String last_sync_datetime, String status, String last_login,String fpwd_flag, String last_pwd_update_time, String created_by, String updated_by, String created_at, String updated_at){

        database.execSQL("insert or ignore into users (`id`,`name`,`badge_no`, `email`, `password`, `role_id`,`citation_series_from`,`citation_series_to`, `access_token`, `mkey`, `msalt`, `last_sync_datetime`,`status`, `last_login`, `fpwd_flag`,`last_pwd_update_time`, `created_by`, `updated_by`,`created_at`,`updated_at`)"
                + "values('"+id+"','"+name+"','"+badge_no+"','"+email+"','"+password+"','"+role_id+"','"+citation_series_from+"','"+citation_series_to+"', '"+access_token+"', '"+mkey+"', '"+msalt+"', '"+last_sync_datetime+"','"+status+"','"+last_login+"','"+fpwd_flag+"','"+last_pwd_update_time+"','"+created_by+"','"+updated_by+"','"+created_at+"','"+updated_at+"' );");
    }

    public void UpdateDatatoUsers(String id, String name,String badge_no,String email, String password,String role_id,String citation_series_from,String citation_series_to, String access_token,String mkey, String msalt, String last_sync_datetime, String status, String last_login,String fpwd_flag, String last_pwd_update_time, String created_by, String updated_by, String created_at, String updated_at){

        database.execSQL("UPDATE users SET " + "'name' = '"+name+"', 'badge_no' = '"+badge_no+"' ,'email' = '"+email+"','password' = '"+password+"','role_id' = '"+role_id+"','citation_series_from' = '"+citation_series_from+"','citation_series_to' = '"+citation_series_to+"','access_token' = '"+access_token+"' ,'mkey' = '"+mkey+"','msalt' = '"+msalt+"','last_sync_datetime' = '"+last_sync_datetime+"','status' = '"+status+"','last_login' = '"+last_login+"', 'fpwd_flag' = '"+fpwd_flag+"', 'last_pwd_update_time' = '"+last_pwd_update_time+"', 'created_by' = '"+created_by+"' , 'updated_by' = '"+updated_by+"','created_at' = '"+created_at+"','updated_at' = '"+updated_at+"'" + "WHERE  id = '"+id+"'");

    }


    public void InsertDatatoRoles(String id, String user_role,String status, String created_by, String updated_by, String created_at, String updated_at){
        database.execSQL("insert or ignore into roles (`id`,`user_role`,`status`,  `created_by`, `updated_by`,`created_at`,`updated_at`)"
                + "values('"+id+"','"+user_role+"','"+status+"','"+created_by+"','"+updated_by+"','"+created_at+"','"+updated_at+"' );");
    }

    public void UpdateDatatoRoles(String id, String user_role,String status, String created_by, String updated_by, String created_at, String updated_at){

        database.execSQL("UPDATE roles SET " + "'user_role' = '"+user_role+"', 'status' = '"+status+"' , 'created_by' = '"+created_by+"' , 'updated_by' = '"+updated_by+"','created_at' = '"+created_at+"','updated_at' = '"+updated_at+"'" + "WHERE  id = '"+id+"'");

    }


    public void InsertDatatoDivisionOfficerAreas(String id, String division_code,String division_desc,String content, String office_area, String status, String created_by, String updated_by, String created_at, String updated_at){
        database.execSQL("insert or ignore into division_officer_areas (`id`,`division_code`,`division_desc`,`content`,`office_area`,`status`,  `created_by`, `updated_by`,`created_at`,`updated_at`)"
                + "values('"+id+"','"+division_code+"','"+division_desc+"','"+content+"','"+office_area+"','"+status+"','"+created_by+"','"+updated_by+"','"+created_at+"','"+updated_at+"' );");
    }

    public void UpdateDatatoDivisionOfficerAreas(String id, String division_code,String division_desc,String content, String office_area, String status, String created_by, String updated_by, String created_at, String updated_at){

        database.execSQL("UPDATE division_officer_areas SET " + "'division_code' = '"+division_code+"', 'division_desc' = '"+division_desc+"' , 'content' = '"+content+"' , 'office_area' = '"+office_area+"' , 'status' = '"+status+"',  'created_by' = '"+created_by+"' , 'updated_by' = '"+updated_by+"','created_at' = '"+created_at+"','updated_at' = '"+updated_at+"'" + "WHERE  id = '"+id+"'");

    }

    public void InsertDatatoNameMaster(String id,String value, String status, String created_by, String updated_by, String created_at, String updated_at){
        database.execSQL("insert or ignore into name_masters (`id`,`value`,`status`,  `created_by`, `updated_by`,`created_at`,`updated_at`)"
                + "values('"+id+"','"+value+"','"+status+"','"+created_by+"','"+updated_by+"','"+created_at+"','"+updated_at+"' );");
    }

    public void UpdateDatatoNameMaster(String id, String value, String status, String created_by, String updated_by, String created_at, String updated_at){

        database.execSQL("UPDATE name_masters SET " + "'value' = '"+value+"', 'status' = '"+status+"',  'created_by' = '"+created_by+"' , 'updated_by' = '"+updated_by+"','created_at' = '"+created_at+"','updated_at' = '"+updated_at+"'" + "WHERE  id = '"+id+"'");

    }


    public void InsertDatatoCityMaster(String id,String value,String description, String state_id, String status, String created_by, String updated_by, String created_at, String updated_at){
        database.execSQL("insert or ignore into city_masters (`id`,`value`,`description`,`state_id`,`status`,  `created_by`, `updated_by`,`created_at`,`updated_at`)"
                + "values('"+id+"','"+value+"','"+description+"','"+state_id+"','"+status+"','"+created_by+"','"+updated_by+"','"+created_at+"','"+updated_at+"' );");
    }

    public void UpdateDatatoCityMaster(String id, String value,String description,String state_id, String status, String created_by, String updated_by, String created_at, String updated_at){

        database.execSQL("UPDATE city_masters SET " + "'value' = '"+value+"', 'description' = '"+description+"' ,'state_id' = '"+state_id+"' , 'status' = '"+status+"',  'created_by' = '"+created_by+"' , 'updated_by' = '"+updated_by+"','created_at' = '"+created_at+"','updated_at' = '"+updated_at+"'" + "WHERE  id = '"+id+"'");

    }

    public void InsertDatatoStateMaster(String id,String value,String description, String status, String created_by, String updated_by, String created_at, String updated_at){
        database.execSQL("insert or ignore into state_masters (`id`,`value`,`description`,`status`,  `created_by`, `updated_by`,`created_at`,`updated_at`)"
                + "values('"+id+"','"+value+"','"+description+"','"+status+"','"+created_by+"','"+updated_by+"','"+created_at+"','"+updated_at+"' );");
    }

    public void UpdateDatatoStateMaster(String id, String value,String description, String status, String created_by, String updated_by, String created_at, String updated_at){

        database.execSQL("UPDATE state_masters SET " + "'value' = '"+value+"', 'description' = '"+description+"' , 'status' = '"+status+"',  'created_by' = '"+created_by+"' , 'updated_by' = '"+updated_by+"','created_at' = '"+created_at+"','updated_at' = '"+updated_at+"'" + "WHERE  id = '"+id+"'");

    }

    public void InsertDatatoVehicleMakes(String id,String make_code,String make_description, String status, String created_by, String updated_by, String created_at, String updated_at){


        database.execSQL("insert or ignore into vehicle_makes (`id`,`make_code`,`make_description`,`status`,  `created_by`, `updated_by`,`created_at`,`updated_at`)"
                + "values('"+id+"','"+make_code+"','"+make_description+"','"+status+"','"+created_by+"','"+updated_by+"','"+created_at+"','"+updated_at+"' );");
    }

    public void UpdateDatatoVehicleMakes(String id, String make_code,String make_description, String status, String created_by, String updated_by, String created_at, String updated_at){

        database.execSQL("UPDATE vehicle_makes SET " + "'make_code' = '"+make_code+"', 'make_description' = '"+make_description+"' , 'status' = '"+status+"',  'created_by' = '"+created_by+"' , 'updated_by' = '"+updated_by+"','created_at' = '"+created_at+"','updated_at' = '"+updated_at+"'" + "WHERE  id = '"+id+"'");

    }

    public void InsertDatatoVehicleModels(String id,String value,String description,String vehicle_make_id, String status, String created_by, String updated_by, String created_at, String updated_at){
        database.execSQL("insert or ignore into vehicle_models (`id`,`value`,`description`, `vehicle_make_id`, `status`,  `created_by`, `updated_by`,`created_at`,`updated_at`)"
                + "values('"+id+"','"+value+"','"+description+"','"+vehicle_make_id+"','"+status+"','"+created_by+"','"+updated_by+"','"+created_at+"','"+updated_at+"' );");
    }

    public void UpdateDatatoVehicleModels(String id, String value,String description, String vehicle_make_id, String status, String created_by, String updated_by, String created_at, String updated_at){

        database.execSQL("UPDATE vehicle_models SET " + "'value' = '"+value+"', 'description' = '"+description+"' ,'vehicle_make_id' = '"+vehicle_make_id+"' , 'status' = '"+status+"',  'created_by' = '"+created_by+"' , 'updated_by' = '"+updated_by+"','created_at' = '"+created_at+"','updated_at' = '"+updated_at+"'" + "WHERE  id = '"+id+"'");

    }




    public void InsertDatatoViolations(String id,String value,String description, String is_speeding, String status, String created_by, String updated_by, String created_at, String updated_at){
        database.execSQL("insert or ignore into violations (`id`,`value`,`description`,`is_speeding`,`status`, `created_by`,`updated_by`,`created_at`,`updated_at`)"
                + "values('"+id+"','"+value+"','"+description+"','"+is_speeding+"','"+status+"','"+created_by+"','"+updated_by+"','"+created_at+"','"+updated_at+"' );");
    }

    public void UpdateDatatoViolations(String id, String value,String description,String is_speeding, String status, String created_by, String updated_by, String created_at, String updated_at){

        database.execSQL("UPDATE violations SET " + "'value' = '"+value+"', 'description' = '"+description+"' ,'is_speeding'='"+is_speeding+"', 'status' = '"+status+"',  'created_by' = '"+created_by+"' , 'updated_by' = '"+updated_by+"','created_at' = '"+created_at+"','updated_at' = '"+updated_at+"'" + "WHERE  id = '"+id+"'");

    }

    public void InsertDatatoZipCityStates(String id,String name,String zip,String city_id, String state_id, String status, String created_by, String updated_by, String created_at, String updated_at){
        database.execSQL("insert or ignore into zip_city_states (`id`,`name`,`zip`,`city_id`,`state_id`,`status`,  `created_by`, `updated_by`,`created_at`,`updated_at`)"
                + "values('"+id+"','"+name+"','"+zip+"','"+city_id+"','"+state_id+"','"+status+"','"+created_by+"','"+updated_by+"','"+created_at+"','"+updated_at+"' );");
    }

    public void UpdateDatatoZipCityStates(String id, String name,String zip,String city_id, String state_id, String status, String created_by, String updated_by, String created_at, String updated_at){

        database.execSQL("UPDATE zip_city_states SET " + "'name' = '"+name+"', 'zip' = '"+zip+"' , 'city_id' = '"+city_id+"' , 'state_id' = '"+state_id+"' , 'status' = '"+status+"',  'created_by' = '"+created_by+"' , 'updated_by' = '"+updated_by+"','created_at' = '"+created_at+"','updated_at' = '"+updated_at+"'" + "WHERE  id = '"+id+"'");

    }

    public void InsertDatatoCommonCodes(String id,String code_type,String code_value,String code_description,  String status, String created_by, String updated_by, String created_at, String updated_at){
        database.execSQL("insert or ignore into common_codes (`id`,`code_type`,`code_value`,`code_description`,`status`,  `created_by`, `updated_by`,`created_at`,`updated_at`)"
                + "values('"+id+"','"+code_type+"','"+code_value+"','"+code_description+"','"+status+"','"+created_by+"','"+updated_by+"','"+created_at+"','"+updated_at+"' );");
    }

    public void UpdateDatatoCommonCodes(String id, String code_type,String code_value,String code_description, String status, String created_by, String updated_by, String created_at, String updated_at){

        database.execSQL("UPDATE common_codes SET " + "'code_type' = '"+code_type+"', 'code_value' = '"+code_value+"' , 'code_description' = '"+code_description+"' , 'status' = '"+status+"',  'created_by' = '"+created_by+"' , 'updated_by' = '"+updated_by+"','created_at' = '"+created_at+"','updated_at' = '"+updated_at+"'" + "WHERE  id = '"+id+"'");

    }

    public void InsertDatatoCommonFields(String id,String field_type,String field_value, String status, String created_by, String updated_by, String created_at, String updated_at){
        database.execSQL("insert or ignore into common_fields (`id`,`field_type`,`field_value`,`status`,  `created_by`, `updated_by`,`created_at`,`updated_at`)"
                + "values('"+id+"','"+field_type+"','"+field_value+"','"+status+"','"+created_by+"','"+updated_by+"','"+created_at+"','"+updated_at+"' );");
    }

    public void UpdateDatatoCommonFields(String id, String field_type,String field_value, String status, String created_by, String updated_by, String created_at, String updated_at){

        database.execSQL("UPDATE common_fields SET " + "'field_type' = '"+field_type+"', 'field_value' = '"+field_value+"' , 'status' = '"+status+"',  'created_by' = '"+created_by+"' , 'updated_by' = '"+updated_by+"','created_at' = '"+created_at+"','updated_at' = '"+updated_at+"'" + "WHERE  id = '"+id+"'");

    }

    public void UpdateCitationInUsersData(String citation_series_from, String id){

        database.execSQL("UPDATE users SET " + " 'citation_series_from' = '"+citation_series_from+"' " + " WHERE  id = '"+id+"' ");

        //database.execSQL("UPDATE users SET " + "'citation_series_from' = '"+citation_series_from+"'" + "WHERE  id = '"+id+"'");
    }

    public String getUserId(){

        c=database.rawQuery("select id from users ", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String num  = c.getString(0);
            buffer.append(""+num);
        }
        return buffer.toString();
    }

    public String getLastSyncDateTime(String id){

        c=database.rawQuery("select last_sync_datetime from users where id = '"+id+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String num  = c.getString(0);
            buffer.append(""+num);
        }
        return buffer.toString();
    }

    public ArrayList<String> getUsersData(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select name from users ", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }



    public ArrayList<String> getRolesData(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select user_role from roles ", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getCommonCodes(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select code_type from common_codes", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getCommonFields(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select field_type from common_fields", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getDivisionOfficerAreas(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select division_code from division_officer_areas", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getNameMaster(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select value from name_masters", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getCityMaster(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select value from city_masters", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getStateMaster(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select value from state_masters", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getVehicleMakeList(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select make_description from vehicle_makes", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }


    public ArrayList<String> getVehicleModelList(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select value from vehicle_models", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getViolationList(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select value from violations", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getDataPasses(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select CitationNumber from data_passes", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getZipCityStateList(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select name from zip_city_states", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getEyes(){
        String String = "Eyes";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select code_description from common_codes where code_type = '"+String+"'", new String[]{});
        arrayList.add("EYES");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedEyesValue(String description){
        String string = "Eyes";
        c=database.rawQuery("select code_value from common_codes where code_type = '"+string+"' AND code_description='"+description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSelectedEyesId(String code_value, String code_description){
        String string = "Eyes";
        c=database.rawQuery("select id from common_codes  where code_type = '"+string+"' AND code_value = '"+code_value+"' AND code_description = '"+code_description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSelectedEyesName(String code_value, String id){
        String string = "Eyes";
        c=database.rawQuery("select code_description from common_codes  where id = '"+id+"' AND code_type = '"+string+"' AND code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSelectedEyesNameWithoutId(String code_value){
        String string = "Eyes";
        c=database.rawQuery("select code_description from common_codes  where code_type = '"+string+"' AND code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public ArrayList<String> getHairs(){
        String String = "Hair";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select code_description from common_codes where code_type = '"+String+"'", new String[]{});
        arrayList.add("HAIR");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedHairValue(String description){
        String string = "Hair";
        c=database.rawQuery("select code_value from common_codes where code_type = '"+string+"' AND code_description='"+description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSelectedHairId(String code_value, String code_description){
        String string = "Hair";
        c=database.rawQuery("select id from common_codes where code_type = '"+string+"' AND code_description = '"+code_description+"' AND code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSelectedHairName(String code_value, String id){
        String string = "Hair";
        c=database.rawQuery("select code_description from common_codes where code_type = '"+string+"' AND id = '"+id+"' AND  code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSelectedHairNameWithoutId(String code_value){
        String string = "Hair";
        c=database.rawQuery("select code_description from common_codes where code_type = '"+string+"'  AND  code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public ArrayList<String> getCity(){
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select description from city_masters", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedCityIdwithStateId(String state_description, String value, String state_id){
        c=database.rawQuery("select id from city_masters where description = '"+state_description+"' AND value = '"+value+"' AND state_id = '"+state_id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public ArrayList<String> getCityWithStateId(String state_id){
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select description from city_masters where state_id ='"+state_id+"'", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedCityValue(String state_description, String state_id){
        c=database.rawQuery("select value from city_masters where description = '"+state_description+"' AND state_id ='"+state_id+"' ", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSelectedCityName(String value,String id){
        c=database.rawQuery("select description from city_masters where value = '"+value+"' AND id ='"+id+"' ", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSelectedCityNameWithoutId(String value){
        c=database.rawQuery("select description from city_masters where value = '"+value+"' ", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSelectedCityId(String value, String description){
        c=database.rawQuery("select id from city_masters where value = '"+value+"' AND description = '"+description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }


    public ArrayList<String> getState(){
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select description from state_masters", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedStateDescription(String value){
        c=database.rawQuery("select description from state_masters where value = '"+value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSelectedStateValue(String state_description){
        c=database.rawQuery("select value from state_masters where description = '"+state_description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }


    public String getSelectedOnlyStateId( String value){
        c=database.rawQuery("select id from state_masters where value = '"+value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSelectedStateId(String state_description, String value){
        c=database.rawQuery("select id from state_masters where description = '"+state_description+"' AND value = '"+value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSelectedStateName(String value, String id){
        c=database.rawQuery("select description from state_masters where value = '"+value+"' AND id ='"+id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public String getSelectedStateNameWithoutId(String value){
        c=database.rawQuery("select description from state_masters where value = '"+value+"' ", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String roleid = c.getString(0);
            buffer.append(""+roleid);
        }
        return buffer.toString();
    }

    public ArrayList<String> getDriversLicenseType(){
        String string = "DriversLicenseType";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select code_description from common_codes where code_type = '"+string+"'", new String[]{});
        arrayList.add("Drivers License Type");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }


    public String getSelectedDriversLicenseTypeCodeDesc(String code_value){
        String string = "DriversLicenseType";
        c=database.rawQuery("select code_description from common_codes where code_value = '"+code_value+"' AND code_type = '"+string+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string1 = c.getString(0);
            buffer.append(""+string1);
        }
        return buffer.toString();
    }

    public String getSelectedDriversLicenseType(String state_description){
        c=database.rawQuery("select code_value from common_codes where code_description = '"+state_description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedDriversLicenseTypeId(String code_value, String code_description){
        c=database.rawQuery("select id from common_codes where code_value = '"+code_value+"' AND code_description = '"+code_description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedDriversLicenseTypeName(String code_value,String id){
        c=database.rawQuery("select code_description from common_codes where code_value = '"+code_value+"' AND id = '"+id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedDriversLicenseTypeNameWithoutId(String code_value){
        c=database.rawQuery("select code_description from common_codes where code_value = '"+code_value+"' ", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public ArrayList<String> getRace(){
        String race = "Race";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select code_description from common_codes where code_type = '"+race+"'", new String[]{});
        arrayList.add("DESCENT/RACE");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedRace(String state_description){
        String race = "Race";
        c=database.rawQuery("select code_value from common_codes where code_type = '"+race+"' AND code_description = '"+state_description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedRaceId(String code_value,String code_description){
        String race = "Race";
        c=database.rawQuery("select id from common_codes where code_type = '"+race+"' AND  code_value = '"+code_value+"' AND code_description= '"+code_description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedRaceName(String code_value, String id){
        String race = "Race";
        c=database.rawQuery("select code_description from common_codes where code_type = '"+race+"' AND id = '"+id+"' AND  code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public ArrayList<String> getEthnicity(){
        String ethnicity = "Ethnicity";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select code_description from common_codes where code_type = '"+ethnicity+"'", new String[]{});
        arrayList.add("ETHNCITY");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedEthnicity(String state_description){
        String ethnicity = "Ethnicity";
        c=database.rawQuery("select code_value from common_codes where  code_type='"+ethnicity+"' AND code_description = '"+state_description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedEthnicityId(String code_value,String code_description){
        String ethnicity = "Ethnicity";
        c=database.rawQuery("select id from common_codes where  code_type='"+ethnicity+"' AND code_value = '"+code_value+"' AND code_description = '"+code_description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedEthnicityName(String code_value, String id){
        String ethnicity = "Ethnicity";
        c=database.rawQuery("select code_description from common_codes where  code_type='"+ethnicity+"' AND code_value = '"+code_value+"'  AND id = '"+id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public ArrayList<String> getVehicleMake(){
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select make_description from vehicle_makes ", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedVehicleMake(String make_description){
        c=database.rawQuery("select make_code from vehicle_makes where make_description = '"+make_description+"' ", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedVehicleMakeId(String make_code, String make_description){
        c=database.rawQuery("select id from vehicle_makes where make_code = '"+make_code+"' AND  make_description = '"+make_description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }


    public String getSelectedVehicleMakeValue(String make_code, String id){
        c=database.rawQuery("select make_description from vehicle_makes where make_code = '"+make_code+"' AND id = '"+id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public ArrayList<String> getVehicleModel(){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select description from vehicle_models" , new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getVehicleModelWithVehMakeId(String vehicle_make_id){
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select description from vehicle_models where vehicle_make_id = '"+vehicle_make_id+"'" , new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }


    public String getSelectedVehicleModel(String description, String s_vehmakeid){
        c=database.rawQuery("select value from vehicle_models where description = '"+description+"' AND vehicle_make_id='"+s_vehmakeid+"' ", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedVehicleModelId(String description, String value,String s_vehmakeid){
        c=database.rawQuery("select id from vehicle_models where description = '"+description+"' AND value ='"+value+"' AND vehicle_make_id='"+s_vehmakeid+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }
    public String getSelectedVehicleModelName(String value, String id){
        c=database.rawQuery("select description from vehicle_models where value = '"+value+"' AND  id = '"+id+"' ", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public ArrayList<String> getVehBody(){
        String vehbody = "VehicleBody";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select code_description from common_codes where code_type = '"+vehbody+"'", new String[]{});
        arrayList.add("VEHICLE BODY");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedVehBodyId(String code_value, String code_description){
        String vehbody = "VehicleBody";
        c=database.rawQuery("select id from common_codes where code_value = '"+code_value+"' AND code_description = '"+code_description+"' AND code_type = '"+vehbody+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedVehBody(String description){
        String vehbody = "VehicleBody";
        c=database.rawQuery("select code_value from common_codes where code_description = '"+description+"' AND code_type = '"+vehbody+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedVehBodyName(String code_value,String id){
        String vehbody = "VehicleBody";
        c=database.rawQuery("select code_description from common_codes where code_value = '"+code_value+"' AND id = '"+id+"' AND  code_type = '"+vehbody+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public ArrayList<String> getVehColor(){
        String vehcolor = "VehicleColor";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select code_description from common_codes where code_type = '"+vehcolor+"'", new String[]{});
        arrayList.add("VEHICLE COLOR");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedVehColor(String description){
        String vehcolor ="VehicleColor";
        c=database.rawQuery("select code_value from common_codes where code_type = '"+vehcolor+"' AND code_description = '"+description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedVehColorId(String code_value, String code_description){
        String vehcolor ="VehicleColor";
        c=database.rawQuery("select id from common_codes where code_type = '"+vehcolor+"' AND code_description = '"+code_description+"' AND code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedVehColorName(String code_value, String id){
        String vehcolor ="VehicleColor";
        c=database.rawQuery("select code_description from common_codes where code_type = '"+vehcolor+"' AND id = '"+id+"' AND code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public ArrayList<String> getVIOLATIONList(){

        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select description from violations", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedVIOLATION(String description){
        c=database.rawQuery("select value from violations where description = '"+description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedVIOLATIONid(String value,String description){
        c=database.rawQuery("select id from violations where value = '"+value+"' AND description ='"+description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedIsSpeeding(String value,String id){
        c=database.rawQuery("select is_speeding from violations where value = '"+value+"' AND id = '"+id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }


    public String getSelectedVIOLATIONvalue(String value, String id){
        c=database.rawQuery("select description from violations where value = '"+value+"' AND  id = '"+id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }


    public ArrayList<String> getViolationCity(){
        String string = "ViolationCity";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select code_description from common_codes where code_type = '"+string+"'", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedViolationCity(String description){
        String violationcity = "ViolationCity";
        c=database.rawQuery("select code_value from common_codes where  code_type = '"+violationcity+"' AND code_description = '"+description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }


    public String getSelectedViolationCityId(String code_value, String code_description){
        String violationcity = "ViolationCity";
        c=database.rawQuery("select id from common_codes where  code_type = '"+violationcity+"' AND code_description = '"+code_description+"' AND code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedViolationCityValue(String code_value,String id){
        String violationcity = "ViolationCity";
        c=database.rawQuery("select code_description from common_codes where  code_type = '"+violationcity+"' AND id = '"+id+"' AND code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }


    public ArrayList<String> getViolationcsttyp(){
        String string = "Street";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select field_value from common_fields where field_type = '"+string+"'", new String[]{});
        arrayList.add("VIOLATIONCSTTYP");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedViolationcsttypId(String field_value){
        String string1 = "Street";
        c=database.rawQuery("select id from common_fields where field_type = '"+string1+"' AND field_value = '"+field_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedViolationcsttypValue(String id){
        String string1 = "Street";
        c=database.rawQuery("select field_value from common_fields where field_type = '"+string1+"' AND id = '"+id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public ArrayList<String> getViolationsttyp(){
        String string = "Street";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select field_value from common_fields where field_type = '"+string+"'", new String[]{});
        arrayList.add("VIOLATIONSTTYP");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedViolationsttypId(String field_value){
        String string1 = "Street";
        c=database.rawQuery("select id from common_fields where field_type = '"+string1+"' AND field_value = '"+field_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedViolationsttypValue(String id){
        String string1 = "Street";
        c=database.rawQuery("select field_value from common_fields where field_type = '"+string1+"' AND id = '"+id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }


    public ArrayList<String> getContentDivisionAreaCodes(){
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select content from division_officer_areas ", new String[]{});
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getDivisionAreaCodeId(String content){
        c=database.rawQuery("select id from division_officer_areas where content = '"+content+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getDivisionAreaCodecodevalue(String content, String id){
        c=database.rawQuery("select division_code from division_officer_areas where content = '"+content+"' AND id = '"+id+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getDivisionAreaCodeValue(String id,String division_code){
        c=database.rawQuery("select content from division_officer_areas where id = '"+id+"' AND division_code = '"+division_code+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public ArrayList<String> getAreaCode(){
        String string = "AreaOfOccurance";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select code_description from common_codes where code_type = '"+string+"'", new String[]{});
        arrayList.add("OFFICER AREA");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedAreaCode(String description){
        String areacode = "AreaOfOccurance";
        c=database.rawQuery("select code_value from common_codes where code_type = '"+areacode+"' AND  code_description = '"+description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedAreaCodeId(String code_value, String description){
        String areacode = "AreaOfOccurance";
        c=database.rawQuery("select id from common_codes where code_type = '"+areacode+"' AND code_value = '"+code_value+"' AND code_description = '"+description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedAreaCodeValue(String code_value,String id){
        String areacode = "AreaOfOccurance";
        c=database.rawQuery("select code_description from common_codes where code_type = '"+areacode+"' AND id = '"+id+"' AND  code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public Map<List<String>, List<String>> eg_getCourtCode(){

        Map<List<String>, List<String>> mapOfPosts = new HashMap<List<String>, List<String>>();

        String status = "Y";
        String string = "CourtCode";
//        String condition = "code_type";
        ArrayList<String> arrayList_description = new ArrayList<>();
        ArrayList<String> arrayList_value = new ArrayList<>();

        Cursor c_description=database.rawQuery("select code_description from common_codes where code_type = '"+string+"' AND status = '"+status+"'", new String[]{});
        Cursor c_value=database.rawQuery("select code_value from common_codes where code_type = '"+string+"' AND status = '"+status+"'", new String[]{});

//        arrayList.add("COURT CODE");
        while (c_description.moveToNext()){
            String desc = c_description.getString(0);
            arrayList_description.add(desc);
        }

        while (c_value.moveToNext()){
            String value = c_value.getString(0);
            arrayList_value.add(value);
        }
        mapOfPosts.put(arrayList_description,arrayList_value);

        Set<Map.Entry<List<String>, List<String>> > entrySet
                = mapOfPosts.entrySet();

        ArrayList<Map.Entry<List<String>, List<String>> > listOfEntry
                = new ArrayList<Map.Entry<List<String>, List<String>>>(entrySet);

        return mapOfPosts;
    }

    public ArrayList<String> getCourtCodeValue(){
        String status = "Y";
        String string = "CourtCode";
//        String condition = "code_type";
        ArrayList<String> arrayList = new ArrayList<>();

//        String[] columns = {"code_value", "code_description"};
//
//        c=database.query("common_codes",columns,string+" = "+condition,null,null,null,null);
        c=database.rawQuery("select code_value from common_codes where code_type = '"+string+"' AND status = '"+status+"'", new String[]{});
        arrayList.add("CODE");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public ArrayList<String> getCourtCodeDesc(){
        String status = "Y";
        String string = "CourtCode";
//        String condition = "code_type";
        ArrayList<String> arrayList = new ArrayList<>();

//        String[] columns = {"code_value", "code_description"};
//
//        c=database.query("common_codes",columns,string+" = "+condition,null,null,null,null);
        c=database.rawQuery("select code_description from common_codes where code_type = '"+string+"' AND status = '"+status+"'", new String[]{});
        arrayList.add("COURT");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedCourtCodeId(String description, String code_value){
        String status = "Y";
        String courtcode = "CourtCode";
        c=database.rawQuery("select id from common_codes where code_value = '"+code_value+"' AND code_type = '"+courtcode+"' AND  code_description = '"+description+"' AND status = '"+status+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedCourtCode(String description){
        String status = "Y";
        String courtcode = "CourtCode";
        c=database.rawQuery("select code_value from common_codes where code_type = '"+courtcode+"' AND  code_description = '"+description+"' AND status = '"+status+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedCourtCodeName(String code_value, String id){
        String status = "Y";
        String courtcode = "CourtCode";
        c=database.rawQuery("select code_description from common_codes where  id = '"+id+"' AND code_type = '"+courtcode+"' AND  code_value = '"+code_value+"' AND status = '"+status+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }


    public ArrayList<String> getLea(){
        String status = "Y";
        String string = "LEA";
        ArrayList<String> arrayList = new ArrayList<>();

        c=database.rawQuery("select field_value from common_fields where field_type = '"+string+"' and status = '"+status+"' ", new String[]{});

        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getLeaId(String field_value){
        String status = "Y";
        String lea = "LEA";

        c=database.rawQuery("select id from common_fields where field_type = '"+lea+"' AND field_value = '"+field_value+"' AND status = '"+status+"'  ", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getLeaName(String id){
        String status = "Y";
        String lea = "LEA";

        c=database.rawQuery("select field_value from common_fields where field_type = '"+lea+"' AND id = '"+id+"' AND status = '"+status+"' ", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }


    public ArrayList<String> getHeights(){
        String string = "Height";
        ArrayList<String> arrayList = new ArrayList<>();
        c=database.rawQuery("select code_description from common_codes where code_type = '"+string+"'", new String[]{});
        arrayList.add("HEIGHT");
        while (c.moveToNext()){
            String role = c.getString(0);
            arrayList.add(role);
        }
        return arrayList;
    }

    public String getSelectedHeightValue(String description){
        String height = "Height";
        c=database.rawQuery("select code_value from common_codes where code_type = '"+height+"' AND  code_description = '"+description+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

    public String getSelectedHeight(String code_value){
        String height = "Height";
        c=database.rawQuery("select code_description from common_codes where code_type = '"+height+"' AND  code_value = '"+code_value+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String string = c.getString(0);
            buffer.append(""+string);
        }
        return buffer.toString();
    }

}
