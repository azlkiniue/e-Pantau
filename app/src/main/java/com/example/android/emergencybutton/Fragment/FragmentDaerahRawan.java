package com.example.android.emergencybutton.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.emergencybutton.R;
import com.example.android.emergencybutton.base.BaseFragment;

/**
 * Created by ASUS on 10/15/2017.
 */

public class FragmentDaerahRawan extends BaseFragment {

    private String title = "Daerah Rawan";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_daerah_rawan, null);
        return root;
    }

    @Override
    protected String getTitle() {
        return title;
    }
}