package com.example.android.emergencybutton.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.android.emergencybutton.Controller.SharedPrefManager;
import com.example.android.emergencybutton.Model.LokasiKejadian;
import com.example.android.emergencybutton.Model.PostKejadian;
import com.example.android.emergencybutton.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        LokasiKejadian lokasi = SharedPrefManager.getInstance(MapsActivity.this).getLokasi();

        Log.d("Latitude ", String.valueOf(lokasi.getLatitude()));
        Log.d("Longitude ", String.valueOf(lokasi.getLogitude()));

    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView!=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    public void onMapReady(GoogleMap googleMap){
//        MapsInitializer.initialize(getContext());

        PostKejadian lokasi = SharedPrefManager.getInstance(MapsActivity.this).getKejadian();

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Double latitude = Double.valueOf(lokasi.getLatitude());
        Double longitude = Double.valueOf(lokasi.getLongitude());

        Log.d("Latitude ", String.valueOf(lokasi.getLatitude()));
        Log.d("Longitude ", String.valueOf(lokasi.getLongitude()));

        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("SetTitle").snippet("I hope get this location"));
        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
    }
}
