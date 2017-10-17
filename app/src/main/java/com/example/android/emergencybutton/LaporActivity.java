package com.example.android.emergencybutton;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ASUS on 10/15/2017.
 */

public class LaporActivity extends Fragment {
    public static Fragment newInstance(Context context) {
        FragmentTwo f = new FragmentTwo();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_lapor, null);
        return root;
    }
}
