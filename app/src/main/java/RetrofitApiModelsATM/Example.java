package RetrofitApiModelsATM;

import java.util.List;

/**
 * Created by Slava on 22.06.2017.
 */


public class Example {

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



}