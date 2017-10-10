package com.example.android.emergencybutton;

/**
 * Created by ASUS on 9/26/2017.
 */

public class User {

    private int id;
    private String nik, nama, alamat, telepon, username;


    public User(int id, String nik, String nama, String alamat, String telepon, String username) {
        this.id = id;
        this.nik = nik;
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
        this.username = username;
        //this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public String getNik() {
        return nik;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTelepon() {
        return telepon;
    }

    public String getUsername() {
        return username;
    }

//    public String getFoto() {
//        return foto;
//    }
}
