package by.jum.locationbyip.models;

import android.graphics.Bitmap;

public class LocationInformation {
    private String ip;
    private String city;
    private String country;
    private Bitmap flag;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Bitmap getFlag() {
        return flag;
    }

    public void setFlag(Bitmap flag) {
        this.flag = flag;
    }
}
