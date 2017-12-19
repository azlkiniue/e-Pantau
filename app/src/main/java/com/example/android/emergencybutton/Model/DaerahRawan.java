package com.example.android.emergencybutton.Model;

/**
 * Created by Ahmada Yusril on 10/12/2017.
 */

public class DaerahRawan {
    private int id;
    private float longitude, latitude;
    private String nama;

    public DaerahRawan(int id, float latitude, float longitude, String nama) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.nama = nama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
