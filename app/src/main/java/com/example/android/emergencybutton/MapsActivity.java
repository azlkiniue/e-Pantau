package com.example.android.emergencybutton;

import android.renderscript.Double2;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends Fragment implements OnMapReadyCallback {
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;

    public MapsActivity(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_maps, container, false);

        LokasiKejadian lokasi = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getLokasi();

        Log.d("Latitude ", String.valueOf(lokasi.getLatitude()));
        Log.d("Longitude ", String.valueOf(lokasi.getLogitude()));

        return mView;
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
        MapsInitializer.initialize(getContext());

        LokasiKejadian lokasi = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getLokasi();

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Log.d("Latitude ", String.valueOf(lokasi.getLatitude()));
        Log.d("Longitude ", String.valueOf(lokasi.getLogitude()));

        googleMap.addMarker(new MarkerOptions().position(new LatLng(-7.2749763, 112.79444122314453)).title("SetTitle").snippet("I hope get this location"));
        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(-7.2749763, 112.79444122314453)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
    }
}

//    private GoogleMap mMap;
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_tombol_darurat, null);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//        return root;
//    }
//
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        LokasiKejadian lokasi = SharedPrefManager.getInstance(getActivity()).getLokasi();
//        goToLocationZoom(Double.valueOf(lokasi.getLatitude()).doubleValue(), Double.valueOf(lokasi.getLogitude()).doubleValue(), 15);
//
//        // Add a marker in Sydney and move the camera
////        LatLng sydney = new LatLng(-34, 151);
////        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
//
//    private void goToLocationZoom(double lat, double lng, float zoom){
//        LatLng ll = new LatLng(lat, lng);
//        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
//        mMap.addMarker(new MarkerOptions().position(ll).title("You in here"));
//        mMap.moveCamera(update);
//    }
//
//    public void onCreateOptionsMenu(Menu menu){
//        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
////        return super.onCreateOptionsMenu(menu);
//    }
//
//    public boolean onOptionItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.mapTypeNone:
//                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
//                break;
//            case R.id.mapTypeNormal:
//                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                break;
//            case R.id.mapTypeSatelite:
//                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                break;
//            case R.id.mapTypeTerrain:
//                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//                break;
//            case R.id.mapTypeHybrid:
//                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}
