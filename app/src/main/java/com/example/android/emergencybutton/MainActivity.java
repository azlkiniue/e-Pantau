package com.example.android.emergencybutton;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST_CODE = 17;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Button mEmergency;
    private NavigationDrawerSetup drawerSetup;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mMenuTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerSetup = new NavigationDrawerSetup();
        drawerSetup.configureDrawer(this);
        //getActionBar().setTitle("Tombol Darurat");

        // Create an instance of GoogleAPIClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mEmergency = (Button) findViewById(R.id.b_emergency);
        mEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = drawerSetup.onOptionsItemSelected(item);
        if (!result) return super.onOptionsItemSelected(item);
        else return result;
    }

    protected void getLocation(){
        Log.d("GetLocation: ", "Success");
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("CheckPermission: ", "Success");
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (null != location) {
                        onLocationChanged(location);
                        Log.d("CheckLocation: ", "Success");
                    } else {
                        Log.d("CheckLocation: ", "Failed");
                    }
                }
            });
            mFusedLocationProviderClient.getLastLocation().addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("CheckLocation: ", "Failed");
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                String[] permissionWeNeed = new String[]{ Manifest.permission.ACCESS_FINE_LOCATION };
                requestPermissions(permissionWeNeed, MY_PERMISSION_REQUEST_CODE);
            }
            Log.d("CheckPermission: ", "Failed");
        }
    }

    protected void onLocationChanged(Location location){
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        Log.d("LocationChanged: ", msg);
    }

    public static class MenuFragment extends Fragment{

    }
}
