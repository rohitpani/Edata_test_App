package com.edatasolutions.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DbHelper extends SQLiteAssetHelper {

    private static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="edata.db";
//    private static final String CREATE_TABLE="create table "+DbContract.TABLE_NAME+
//            "(id integer primary key autoincrement,"+DbContract.USERNAME+" text,"+DbContract.PASSWORD+" text)";
//    private static final String DROP_TABLE= "drop table if exists "+DbContract.TABLE_NAME;

    public DbHelper(Context context){

        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(DROP_TABLE);
//        onCreate(db);
//    }
//
//    public void saveToLocalDatabase(){
//
//    }

//    @SuppressLint("Recycle")
//    public Cursor readFromLocalDatabase(SQLiteDatabase database){
//
//        String[] projection = {DbContract.USERNAME, DbContract.PASSWORD};
//
//        Log.e("checkk",DbContract.USERNAME);
//        return (database.query(DbContract.TABLE_NAME,projection,null,null,null,null,null));
//    }
}
