package com.example.slava.locationbankomats.network;

import java.util.List;

/**
 * Created by Slava on 03.03.2018.
 */

public class ATMPojo {

    private String city;
    private String address;
    private List<Device> devices = null;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }


    public class Device{
        private String type;
        private String cityRU;
        private String cityUA;
        private String cityEN;
        private String fullAddressRu;
        private String fullAddressUa;
        private String fullAddressEn;
        private String placeRu;
        private String placeUa;
        private String latitude;
        private String longitude;
        private Tw tw;


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCityRU() {
            return cityRU;
        }

        public void setCityRU(String cityRU) {
            this.cityRU = cityRU;
        }

        public String getCityUA() {
            return cityUA;
        }

        public void setCityUA(String cityUA) {
            this.cityUA = cityUA;
        }

        public String getCityEN() {
            return cityEN;
        }

        public void setCityEN(String cityEN) {
            this.cityEN = cityEN;
        }

        public String getFullAddressRu() {
            return fullAddressRu;
        }

        public void setFullAddressRu(String fullAddressRu) {
            this.fullAddressRu = fullAddressRu;
        }

        public String getFullAddressUa() {
            return fullAddressUa;
        }

        public void setFullAddressUa(String fullAddressUa) {
            this.fullAddressUa = fullAddressUa;
        }

        public String getFullAddressEn() {
            return fullAddressEn;
        }

        public void setFullAddressEn(String fullAddressEn) {
            this.fullAddressEn = fullAddressEn;
        }

        public String getPlaceRu() {
            return placeRu;
        }

        public void setPlaceRu(String placeRu) {
            this.placeRu = placeRu;
        }

        public String getPlaceUa() {
            return placeUa;
        }

        public void setPlaceUa(String placeUa) {
            this.placeUa = placeUa;
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

        public Tw getTw() {
            return tw;
        }

        public void setTw(Tw tw) {
            this.tw = tw;
        }



        public class Tw {

            private String mon;
            private String tue;
            private String wed;
            private String thu;
            private String fri;
            private String sat;
            private String sun;
            private String hol;


            public String getMon() {
                return mon;
            }

            public void setMon(String mon) {
                this.mon = mon;
            }

            public String getTue() {
                return tue;
            }

            public void setTue(String tue) {
                this.tue = tue;
            }

            public String getWed() {
                return wed;
            }

            public void setWed(String wed) {
                this.wed = wed;
            }

            public String getThu() {
                return thu;
            }

            public void setThu(String thu) {
                this.thu = thu;
            }

            public String getFri() {
                return fri;
            }

            public void setFri(String fri) {
                this.fri = fri;
            }

            public String getSat() {
                return sat;
            }

            public void setSat(String sat) {
                this.sat = sat;
            }

            public String getSun() {
                return sun;
            }

            public void setSun(String sun) {
                this.sun = sun;
            }

            public String getHol() {
                return hol;
            }

            public void setHol(String hol) {
                this.hol = hol;
            }


        }


    }


}