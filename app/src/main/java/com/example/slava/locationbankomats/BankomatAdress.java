package com.example.slava.locationbankomats;

/**
 * Created by Slava on 12.07.2017.
 */

public class BankomatAdress {
    public String andress;
    public String latitude;
    public String longitude;

    public BankomatAdress(String andress, String latitude, String longitude) {
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
}
