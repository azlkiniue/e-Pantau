package com.example.android.emergencybutton.Model;

/**
 * Created by ASUS on 10/14/2017.
 */

public class LokasiKejadian {
    private int id_tombol;
    private String logitude, latitude, telp, waktu;



    public LokasiKejadian(int id_tombol, String logitude, String latitude, String telp, String waktu) {
        this.id_tombol = id_tombol;
        this.logitude = logitude;
        this.latitude = latitude;
        this.telp = telp;
        this.waktu = waktu;
    }

    public int getId_tombol() {
        return id_tombol;
    }

    public String getLogitude() {
        return logitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getTelp() {
        return telp;
    }

    public String getWaktu() {
        return waktu;
    }
}
