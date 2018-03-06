package com.peppe.crmdoctors.model;

import java.util.Locale;

/**
 * Created by peppe on 21/02/2018.
 */

public class Address  {
    String city;
    String street;
    String civic_number;
    String latitude;
    String longitude;



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCivic_number() {
        return civic_number;
    }

    public void setCivic_number(String civic_number) {
        this.civic_number = civic_number;
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
