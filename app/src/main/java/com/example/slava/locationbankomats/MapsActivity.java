package com.example.slava.locationbankomats;

import android.Manifest;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.slava.locationbankomats.fragments.SplashFragment;
import com.example.slava.locationbankomats.models.BankomatAdress;
import com.example.slava.locationbankomats.models.TSOAdress;
import com.example.slava.locationbankomats.network.ATMPojo;
import com.example.slava.locationbankomats.network.ILink;
import com.example.slava.locationbankomats.network.TSOPojo;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    FragmentManager fragmentManager;

    Retrofit retrofit;
    DbHelper dbHelper = new DbHelper(this);
    String fullAdress_atm, latitude_atm, longitude_atm,
            fullAdress_tso, latitude_tso, longitude_tso;
    static ArrayList<BankomatAdress> listBank = new ArrayList<>();
    ;
    static ArrayList<TSOAdress> listTSO = new ArrayList<>();

    String LOG_TAG = "myLogs";

    private GoogleMap mMap;
    static Location imHere;
    private LocationManager locationManager;
    LatLng dneprTSO, dneprATM, dneprTSOt, dneprATMt;
    Spinner spiner;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fragmentManager = getFragmentManager();

        runSplash();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//////////////////////////////////////

        new Thread(new Runnable() {
            @Override
            public void run() {

                retrofit = new Retrofit.Builder().baseUrl("https://api.privatbank.ua/p24api/").addConverterFactory(GsonConverterFactory.create()).build();
                ILink apis = retrofit.create(ILink.class);

                try {
                    final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                    ATMPojo devices = (ATMPojo) apis.getArrayDevice().execute().body();
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

                    TSOPojo devicesTso = (TSOPojo) apis.getArrayDeviceTSO().execute().body();
                    if (devicesTso.getDevices().size() > 0) {
                        sqLiteDatabase.execSQL("delete from TSO");
                    }
                    for (int j = 0; j < devicesTso.getDevices().size(); j++) {

                        fullAdress_tso = devicesTso.getDevices().get(j).getFullAddressRu();
                        latitude_tso = devicesTso.getDevices().get(j).getLatitude();
                        longitude_tso = devicesTso.getDevices().get(j).getLongitude();

                        ContentValues contentValue = new ContentValues();
                        contentValue.put("fullAdress_tso", fullAdress_tso);
                        contentValue.put("latitude_tso", latitude_tso);
                        contentValue.put("longitude_tso", longitude_tso);
                        sqLiteDatabase.insert("TSO", null, contentValue);

                        TSOAdress tso = new TSOAdress(fullAdress_tso, latitude_tso, longitude_tso);
                        listTSO.add(tso);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        /////////////////////////////
        spiner = (Spinner) findViewById(R.id.spiner_id);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spiner.getSelectedItem().toString();
                switch (selected) {
                    case "Нечего не выбрано":
                        if ((dneprATM != null || dneprTSO != null) || (dneprATMt != null || dneprTSOt != null)) {
                            mMap.clear();
                        }
                        //удалять все
                        break;
                    case "банкоматы":

                        if ((dneprATM != null || dneprTSO != null) || (dneprATMt != null || dneprTSOt != null)) {
                            mMap.clear();
                        }
                        setATM();
                        //удалять TOS
                        break;
                    case "терминалы":

                        if ((dneprATM != null || dneprTSO != null) || (dneprATMt != null || dneprTSOt != null)) {
                            mMap.clear();
                        }
                        setTSO();
                        //удалять ATM
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                locationListener);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            imHere = location;
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lon))
                    .zoom(16)
                    .bearing(0)       //поворот карты
                    .tilt(20)// угол наклона
                    .build();

            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.animateCamera(cameraUpdate);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().isCompassEnabled();
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //начальная позиция
    }

    private void setATM() {
        Log.e("myLog", "setATM");
        if (MapsActivity.listBank == null || MapsActivity.listBank.size() < 1) {
            DbHelper dbHelper = new DbHelper(this);
            final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.query("Bancomats", null, null, null, null, null, null);
            String str;
            if (cursor.moveToFirst())
                do {
                    str = "" + cursor.getInt(cursor.getColumnIndex("id"));
                    String adresATM = cursor.getString(cursor.getColumnIndex("fullAdress_atm"));
                    double lat = cursor.getDouble(cursor.getColumnIndex("latitude_atm"));
                    double lot = cursor.getDouble(cursor.getColumnIndex("longitude_atm"));
                    dneprATMt = new LatLng(lat, lot);
                    mMap.addMarker(new MarkerOptions().position(dneprATMt).title(adresATM).icon(BitmapDescriptorFactory.fromResource(R.drawable.bankomat_mini)));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        return;
                    }
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                         return;
                    }
                    mMap.setMyLocationEnabled(true);
                }while (cursor.moveToNext());

            cursor.close();
        }else {
            for (BankomatAdress adres_atm : MapsActivity.listBank) {
                LatLng dneprATM = new LatLng(Double.parseDouble(adres_atm.getLatitude()), Double.parseDouble(adres_atm.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(dneprATM).title(adres_atm.getAndress()).icon(BitmapDescriptorFactory.fromResource(R.drawable.bankomat_mini)));
                // mMap.moveCamera(CameraUpdateFactory.newLatLng(dneprATM));
            }
        }
    }

    private void setTSO() {
        Log.e("myLog","setTSO");

        if (MapsActivity.listTSO ==null || MapsActivity.listTSO.size()<1) {
            DbHelper dbHelp = new DbHelper(this);
            final SQLiteDatabase sqLiteDatabas = dbHelp.getWritableDatabase();
            Cursor cursor = sqLiteDatabas.query("TSO", null, null, null, null, null, null);
            String str;
            if (cursor.moveToFirst())
                do {
                    str = "" + cursor.getInt(cursor.getColumnIndex("id"));
                    String adresTSO = cursor.getString(cursor.getColumnIndex("fullAdress_tso"));
                    double lat = cursor.getDouble(cursor.getColumnIndex("latitude_tso"));
                    double lot = cursor.getDouble(cursor.getColumnIndex("longitude_tso"));
                    dneprTSOt = new LatLng(lat, lot);
                    mMap.addMarker(new MarkerOptions().position(dneprTSOt).title(adresTSO).icon(BitmapDescriptorFactory.fromResource(R.drawable.terminal_mini)));
                } while (cursor.moveToNext());

            cursor.close();

        }
        else{
            for (TSOAdress adres_tso : MapsActivity.listTSO) {
                dneprTSO = new LatLng(Double.parseDouble(adres_tso.getLatitude()), Double.parseDouble(adres_tso.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(dneprTSO).title(adres_tso.getAndress()).icon(BitmapDescriptorFactory.fromResource(R.drawable.terminal_mini)));
            }    // mMap.moveCamera(CameraUpdateFactory.newLatLng(dnepr));
        }
    }

    private void runSplash() {
        SplashFragment splashfragment= new SplashFragment();
        fragmentManager.beginTransaction().replace(R.id.parent_layout,splashfragment).addToBackStack(null).commit();
    }
}
