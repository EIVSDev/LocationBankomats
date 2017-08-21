package com.example.slava.locationbankomats;

import android.Manifest;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
static Location imHere;
    private LocationManager locationManager;
    LatLng dneprTSO,dneprATM,dneprTSOt,dneprATMt;
    Spinner spiner;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        spiner = (Spinner) findViewById(R.id.spiner_id);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spiner.getSelectedItem().toString();
                switch (selected) {
                    case "Нечего не выбрано":
                        if((dneprATM !=null || dneprTSO !=null)||(dneprATMt !=null || dneprTSOt !=null)){
                            mMap.clear();}
                        //удалять все
                        break;
                    case "банкоматы":
                        if ((dneprATM !=null || dneprTSO !=null)||(dneprATMt !=null || dneprTSOt !=null)){
                            mMap.clear();}
                        setATM();
                        //удалять TOS
                        break;
                    case "терминалы":
                        if ((dneprATM !=null || dneprTSO !=null)||(dneprATMt !=null || dneprTSOt !=null)){
                            mMap.clear();}
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
    }

    private void setATM(){
        Log.e("myLog","setATM");
        if (MainActivity.listBank ==null || MainActivity.listBank.size()<1){
            DbHelper dbHelper =new DbHelper(this);
            final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.query("Bancomats",null,null,null,null,null,null);
            String str;
            if (cursor.moveToFirst())
                do{
                    str = "" + cursor.getInt(cursor.getColumnIndex("id"));
                    String adresATM = cursor.getString(cursor.getColumnIndex("fullAdress_atm"));
                    double lat =cursor.getDouble(cursor.getColumnIndex("latitude_atm"));
                    double lot =cursor.getDouble(cursor.getColumnIndex("longitude_atm"));
                    dneprATMt = new LatLng(lat,lot);
                    mMap.addMarker(new MarkerOptions().position(dneprATMt).title(adresATM).icon(BitmapDescriptorFactory.fromResource(R.drawable.bankomat_mini)));
                }while (cursor.moveToNext());

            cursor.close();
        }else {
            for (BankomatAdress adres_atm : MainActivity.listBank) {
                LatLng dneprATM = new LatLng(Double.parseDouble(adres_atm.getLatitude()), Double.parseDouble(adres_atm.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(dneprATM).title(adres_atm.getAndress()).icon(BitmapDescriptorFactory.fromResource(R.drawable.bankomat_mini)));
                // mMap.moveCamera(CameraUpdateFactory.newLatLng(dneprATM));
            }
        }
    }

    private void setTSO() {
        Log.e("myLog","setTSO");

        if (MainActivity.listTSO ==null || MainActivity.listTSO.size()<1) {
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
            for (TSOAdress adres_tso : MainActivity.listTSO) {
                dneprTSO = new LatLng(Double.parseDouble(adres_tso.getLatitude()), Double.parseDouble(adres_tso.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(dneprTSO).title(adres_tso.getAndress()).icon(BitmapDescriptorFactory.fromResource(R.drawable.terminal_mini)));
            }    // mMap.moveCamera(CameraUpdateFactory.newLatLng(dnepr));
        }
    }

}
