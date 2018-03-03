package com.example.slava.locationbankomats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Slava on 17.08.2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context,  "bank.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table Bancomats('id' integer primary key autoincrement, 'fullAdress_atm' text, 'latitude_atm' text, 'longitude_atm' text);");

        sqLiteDatabase.execSQL("create table TSO('id' integer primary key autoincrement, 'fullAdress_tso' text, 'latitude_tso' text, 'longitude_tso' text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }












}
