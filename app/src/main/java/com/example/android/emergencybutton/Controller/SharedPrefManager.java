package com.example.android.emergencybutton.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.android.emergencybutton.Activity.LoginActivity;
import com.example.android.emergencybutton.Model.LokasiKejadian;
import com.example.android.emergencybutton.Model.PostKejadian;
import com.example.android.emergencybutton.Model.User;

/**
 * Created by ASUS on 9/26/2017.
 */

public class SharedPrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // shared pref mode
    int PRIVATE_MODE = 0;

    //the constants
    private static final String SHARED_PREF_NAME = "ePantauSharedPref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_NAMA = "keynama";
    private static final String KEY_NIK = "keynik";
    private static final String KEY_ALAMAT = "keyalamat";
    private static final String KEY_TELEPON = "keytelepon";
    private static final String KEY_FOTO = "keyfoto";
    private static final String KEY_ID = "keyid";
    private static final String KEY_ID_TOMBOL = "keyidtombol";
    private static final String KEY_TOMBOL_TELEPON = "keytomboltelepon";
    private static final String KEY_LONGITUDE = "keylongitude";
    private static final String KEY_LATITUDE = "keylatitude";
    private static final String KEY_WAKTU = "keywaktu";
    private static final String KEY_ID_POST = "keyidpost";
    private static final String KEY_LONGITUDE_LAPOR = "keylongitudelapor";
    private static final String KEY_LATITUDE_LAPOR = "keylatitudelapor";
    private static final String KEY_CAPTION = "keycaption";
    private static final String KEY_ID_TAG = "keyidtag";
    private static final String KEY_TAG1 = "keytag1";
    private static final String KEY_TAG2 = "keytag2";
    private static final String KEY_TAG3 = "keytag3";
    private static final String KEY_JUDUL = "keyjudul";
    private static final String KEY_TANGGAL_POSTING = "keytanggalposting";
    private static final String KEY_GAMBAR = "keygambar";


    private static SharedPrefManager mInstance;
    private static Context mCtx;

    public SharedPrefManager(Context context) {
        mCtx = context;
        pref = mCtx.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


//    public SharedPrefManager(Context context) {
//        this._context = context;
//        pref = _context.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE);
//        editor = pref.edit();
//    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_NIK, user.getNik());
        editor.putString(KEY_NAMA, user.getNama());
        editor.putString(KEY_ALAMAT, user.getAlamat());
        editor.putString(KEY_TELEPON, user.getTelepon());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_FOTO, user.getFoto());
        //editor.apply();
        editor.commit();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    public void setLokasi(LokasiKejadian lokasi) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID_TOMBOL, lokasi.getId_tombol());
        editor.putString(KEY_LONGITUDE, lokasi.getLogitude());
        editor.putString(KEY_LATITUDE, lokasi.getLatitude());
        editor.putString(KEY_TOMBOL_TELEPON, lokasi.getTelp());
        editor.putString(KEY_WAKTU, lokasi.getWaktu());
        // editor.putString(KEY_FOTO, user.getFoto());
        //editor.apply();
        editor.commit();
    }

    public LokasiKejadian getLokasi(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new LokasiKejadian(
                sharedPreferences.getInt(KEY_ID_TOMBOL, -1),
                sharedPreferences.getString(KEY_LONGITUDE, null),
                sharedPreferences.getString(KEY_LATITUDE, null),
                sharedPreferences.getString(KEY_TOMBOL_TELEPON, null),
                sharedPreferences.getString(KEY_WAKTU, null)
                //sharedPreferences.getString(KEY_FOTO, null)
        );
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NIK, null),
                sharedPreferences.getString(KEY_NAMA, null),
                sharedPreferences.getString(KEY_ALAMAT, null),
                sharedPreferences.getString(KEY_TELEPON, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_FOTO, null)
        );
    }

    public void postKejadian(PostKejadian postKejadian) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID_POST, postKejadian.getId_post());
        editor.putInt(KEY_ID_TAG, postKejadian.getId_tag());
        editor.putString(KEY_JUDUL, postKejadian.getJudul());
        editor.putString(KEY_CAPTION, postKejadian.getCaption());
        editor.putString(KEY_TANGGAL_POSTING, postKejadian.getTanggal_posting());
        editor.putString(KEY_LONGITUDE, postKejadian.getLongitude());
        editor.putString(KEY_LATITUDE, postKejadian.getLatitude());
        editor.putString(KEY_TAG1, postKejadian.getTag1());
//        editor.putString(KEY_TAG2, postKejadian.getTag2());
//        editor.putString(KEY_TAG3, postKejadian.getTag3());
        //editor.apply();
        editor.commit();
    }

    public PostKejadian getKejadian(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new PostKejadian(
                sharedPreferences.getInt(KEY_ID_POST, -1),
                sharedPreferences.getInt(KEY_ID_TAG, -1),
//                sharedPreferences.getString(KEY_LONGITUDE_LAPOR, null),
//                sharedPreferences.getString(KEY_LATITUDE_LAPOR, null),
                sharedPreferences.getString(KEY_JUDUL, null),
                sharedPreferences.getString(KEY_CAPTION, null),
                sharedPreferences.getString(KEY_TANGGAL_POSTING, null),
                sharedPreferences.getString(KEY_LATITUDE, null),
                sharedPreferences.getString(KEY_LONGITUDE, null),
                sharedPreferences.getString(KEY_TAG1, null)
//                sharedPreferences.getString(KEY_TAG2, null),
//                sharedPreferences.getString(KEY_TAG3, null)
//                sharedPreferences.getString(KEY_GAMBAR, null)
        );
    }


    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
