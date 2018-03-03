package com.example.slava.locationbankomats.models;

/**
 * Created by Slava on 16.08.2017.
 */

public class TSOAdress {
    public String andress;

    public TSOAdress(String andress, String latitude, String longitude) {
        this.andress = andress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAndress() {
        return andress;
    }

    public void setAndress(String andress) {
        this.andress = andress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String latitude;
    public String longitude;
}
