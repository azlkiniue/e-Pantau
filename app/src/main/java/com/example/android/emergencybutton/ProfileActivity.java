package com.example.android.emergencybutton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewNIK, textViewNama, textViewAlamat, textViewTelp, textViewId, textViewUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


        textViewId = (TextView) findViewById(R.id.textViewId);
        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewNIK = (TextView) findViewById(R.id.textViewNIK);
        textViewNama = (TextView) findViewById(R.id.textViewNama);
        textViewAlamat = (TextView) findViewById(R.id.textViewAlamat);
        textViewTelp = (TextView) findViewById(R.id.textViewTelepon);


        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        textViewId.setText(String.valueOf(user.getId()));
        textViewNIK.setText(String.valueOf(user.getNik()));
        textViewNama.setText(user.getNama());
        textViewAlamat.setText(user.getAlamat());
        textViewTelp.setText(user.getTelepon());
        textViewUsername.setText(user.getUsername());
        //when the user presses logout button
        //calling the logout method
        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
    }
}
