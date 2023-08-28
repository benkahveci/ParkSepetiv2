package com.service.parking.parksepeti.Model;

import java.util.HashMap;
import java.util.Map;

public class KonumPini {

    private String by;
    private String visibility;
    private String pinkey;
    private Map<String,Double> pinloc  = new HashMap<>();
    private String address;
    private String mobile;
    private String area;

    public KonumPini() { }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }


    public String getVisibility() {
        return visibility;
    }


    public void setPinkey(String pinkey) {
        this.pinkey = pinkey;
    }


    public Map<String, Double> getPinloc() {
        return pinloc;
    }

    public void setPinloc(Map<String, Double> pinloc) {
        this.pinloc = pinloc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
