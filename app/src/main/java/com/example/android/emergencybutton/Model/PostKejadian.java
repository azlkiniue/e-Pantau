package com.example.android.emergencybutton.Model;

import java.io.Serializable;

/**
 * Created by ASUS on 11/6/2017.
 */

public class PostKejadian implements Serializable {
    private int id_post, id_tag, id_user;
    private String judul, tanggal_posting, longitude, latitude, caption, gambar, foto, nama;

    public PostKejadian() {
    }

    public PostKejadian(int id_post, String judul, String tanggal_posting, String caption, String latitude, String longitude) {
        this.id_post = id_post;
        this.judul = judul;
        this.tanggal_posting = tanggal_posting;
        this.longitude = longitude;
        this.latitude = latitude;
        this.caption = caption;
//        this.gambar = gambar;
    }

    public int getId_post() {
        return id_post;
    }

    public String getJudul() {
        return judul;
    }

    public String getTanggal_posting() {
        return tanggal_posting;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getCaption() {
        return caption;
    }

    public String getGambar() {
        return gambar;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setTanggal_posting(String tanggal_posting) {
        this.tanggal_posting = tanggal_posting;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }


    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
