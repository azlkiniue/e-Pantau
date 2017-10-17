package com.example.android.emergencybutton;

/**
 * Created by ASUS on 9/26/2017.
 */

public class User {

    private int id;
    private String nik, nama, alamat, telepon, username, foto;

    public User() {
    }

    public User(int id, String nik, String nama, String alamat, String telepon, String username, String foto) {
        this.id = id;
        this.nik = nik;
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
        this.username = username;
        this.foto = foto;
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

    public String getFoto() {
        return foto;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nik='" + nik + '\'' +
                ", nama='" + nama + '\'' +
                ", alamat='" + alamat + '\'' +
                ", telepon='" + telepon + '\'' +
                ", username='" + username + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}
