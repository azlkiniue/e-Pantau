package com.example.android.emergencybutton;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class GetPhoneNumberActivity extends AppCompatActivity {
    TextView Number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_phone_number);

        Number = (TextView) findViewById(R.id.phoneNumber);
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        Number.setText(tManager.getLine1Number());

    }
}
