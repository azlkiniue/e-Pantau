package com.example.android.emergencybutton.Model;

/**
 * Created by ASUS on 10/14/2017.
 */

public class Coordinate {
    private float longitude, latitude;

    public Coordinate() {
//        this.longitude = longitude;
//        this.latitude = latitude;
    }

    public Coordinate(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }
}
