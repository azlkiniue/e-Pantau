package com.example.android.emergencybutton.Controller;

/**
 * Created by ASUS on 9/26/2017.
 */

public class URLs {
    private static final String ROOT_URL = "http://madamita.ml/yusril/epantau/web/";
    private static final String BASE_USER_URL = ROOT_URL + "api/user";
    public static final String URL_REGISTER = BASE_USER_URL;
    public static final String URL_LOGIN = BASE_USER_URL + "/search";
    public static final String URL_EDIT = BASE_USER_URL + "/";
    public static final String ROOT_URL_LOKASI = ROOT_URL + "darurat/submit";
    public static final String UPLOAD_PROFILE_URL = BASE_USER_URL + "/upload";
    public static final String POSTKEJADIAN_URL = ROOT_URL + "api/post-kejadian";
    public static final String UPLOADKEJADIAN_URL = POSTKEJADIAN_URL + "/upload";
    public static final String KEJADIANTERKINI_URL = ROOT_URL + "api/post-kejadian-search/search?PostKejadianSearch[judul]=&expand=user&sort=-tanggal_posting";
    public static final String KEJADIANPROFILE_URL = ROOT_URL + "api/post-kejadian-search/search?";
    public static final String URL_GAMBAR = ROOT_URL + "file/post/";
    public static final String URL_FOTO = ROOT_URL + "file/profile/";
}