package com.example.android.emergencybutton;

/**
 * Created by ASUS on 11/6/2017.
 */

public class PostKejadian {
    private int id_post;
    private String judul, tanggal_posting, logitude, latitude, caption, tag, gambar;

    public PostKejadian(int id_post, String judul, String tanggal_posting, String caption, String tag) {
        this.id_post = id_post;
        this.judul = judul;
        this.tanggal_posting = tanggal_posting;
//        this.logitude = logitude;
//        this.latitude = latitude;
        this.caption = caption;
        this.tag = tag;
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

    public String getLogitude() {
        return logitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getCaption() {
        return caption;
    }

    public String getTag() {
        return tag;
    }
}
