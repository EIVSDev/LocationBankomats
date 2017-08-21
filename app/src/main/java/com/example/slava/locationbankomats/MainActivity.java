package com.example.slava.locationbankomats;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.util.ArrayList;
import RetrofitApi.IBank;
import RetrofitApiModelsATM.Example;
import RetrofitApiModelsTSO.ExampleTSO;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    DbHelper dbHelper,dbHelp;
    static ArrayList<BankomatAdress> listBank;
    static ArrayList<TSOAdress> listTSO;
    Retrofit retrofit;
    String fullAdress_atm, latitude_atm, longitude_atm, fullAdress_tso, latitude_tso, longitude_tso;

    String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listBank = new ArrayList<>();
        listTSO = new ArrayList<>();
        dbHelper = new DbHelper(this);


        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();



        new Thread(new Runnable() {
            @Override
            public void run() {
                retrofit = new Retrofit.Builder().baseUrl("https://api.privatbank.ua/p24api/").addConverterFactory(GsonConverterFactory.create()).build();
                IBank apis = retrofit.create(IBank.class);



                try {

                    Example devices = (Example) apis.getArrayDevice().execute().body();
                    if (devices.getDevices().size() > 0) {
                        sqLiteDatabase.execSQL("delete from Bancomats");
                    }
                    for (int i = 0; i < devices.getDevices().size(); i++) {

                        fullAdress_atm = devices.getDevices().get(i).getFullAddressRu();
                        latitude_atm = devices.getDevices().get(i).getLatitude();
                        longitude_atm = devices.getDevices().get(i).getLongitude();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("fullAdress_atm", fullAdress_atm);
                        contentValues.put("latitude_atm", latitude_atm);
                        contentValues.put("longitude_atm", longitude_atm);
                        sqLiteDatabase.insert("Bancomats", null, contentValues);
                        BankomatAdress e = new BankomatAdress(fullAdress_atm, latitude_atm, longitude_atm);
                        listBank.add(e);

                         }


                    ExampleTSO devicesTso = (ExampleTSO) apis.getArrayDeviceTSO().execute().body();
                    if (devicesTso.getDevices().size() > 0) {
                        sqLiteDatabase.execSQL("delete from TSO");
                    }
                    for (int j = 0; j < devicesTso.getDevices().size(); j++) {

                        fullAdress_tso = devicesTso.getDevices().get(j).getFullAddressRu();
                        latitude_tso = devicesTso.getDevices().get(j).getLatitude();
                        longitude_tso = devicesTso.getDevices().get(j).getLongitude();

                        ContentValues contentValue = new ContentValues();
                        contentValue.put("fullAdress_tso",fullAdress_tso);
                        contentValue.put("latitude_tso",latitude_tso);
                        contentValue.put("longitude_tso",longitude_tso);
                        sqLiteDatabase.insert("TSO",null,contentValue);

                        TSOAdress tso = new TSOAdress(fullAdress_tso,latitude_tso,longitude_tso);
                        listTSO.add(tso);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

               new  Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent i = new Intent(MainActivity.this,MapsActivity.class);
               startActivity(i);
               finish();

           }
       },2*500);
    }
}
