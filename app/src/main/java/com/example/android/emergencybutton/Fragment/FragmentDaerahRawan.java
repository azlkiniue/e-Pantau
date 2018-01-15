package com.example.android.emergencybutton.Fragment;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.emergencybutton.Adapter.RecyclerViewAdapter;
import com.example.android.emergencybutton.Controller.GeofenceTransitionsIntentService;
import com.example.android.emergencybutton.Model.DaerahRawan;
import com.example.android.emergencybutton.Model.PostKejadian;
import com.example.android.emergencybutton.R;
import com.example.android.emergencybutton.base.BaseFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.android.emergencybutton.Controller.URLs.DAERAHRAWAN_URL;
import static com.example.android.emergencybutton.Controller.URLs.KEJADIANTERKINI_URL;

public class FragmentDaerahRawan extends BaseFragment implements OnMapReadyCallback, OnCompleteListener<Void> {

    private static final String TAG = FragmentDaerahRawan.class.getSimpleName();
    private GoogleMap mMap;
    private Location mLastKnownLocation;
    private final LatLng mDefaultLocation = new LatLng(-7.276666, 112.794722);
    private static final int DEFAULT_ZOOM = 13;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    JsonArrayRequest RequestOfJSonArray;
    RequestQueue requestQueue ;

    String id_daerah_rawan ="id_daerah_rawan";
    String longitude = "longitude";
    String latitude = "latitude";
    String nama = "nama";
    String warna = "warna";
    String isi = "isi";

    private String title = "Daerah Rawan";

    @Override
    public void onComplete(@NonNull Task<Void> task) {

    }

    /**
     * Tracks whether the user requested to add or remove geofences, or to do neither.
     */
    private enum PendingGeofenceTask {
        ADD, REMOVE, NONE
    }
    /**
     * Provides access to the Geofencing API.
     */
    private GeofencingClient mGeofencingClient;

    /**
     * The list of geofences used in this sample.
     */
    private ArrayList<Geofence> mGeofenceList = new ArrayList<>();

    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;

    // Buttons for kicking off the process of adding or removing geofences.
    private Button mAddGeofencesButton;
    private Button mRemoveGeofencesButton;

    private PendingGeofenceTask mPendingGeofenceTask = PendingGeofenceTask.NONE;

    ArrayList<DaerahRawan> ListArea = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);
        //getActivity().setContentView(R.layout.activity_maps);

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_maps, null);

//        ListArea = new ArrayList<>();
//        ListArea.add(new DaerahRawan(1, -7.311346f, 112.780516f, "Jalan Ir. Soekarno (MERR)", "#ce813e"));
//        ListArea.add(new DaerahRawan(2, -7.265678f, 112.752035f, "Gubeng", "#ce473d"));
//        ListArea.add(new DaerahRawan(3, -7.244938f, 112.727752f, "Jalan Dupak", "#91ce3d"));
//        ListArea.add(new DaerahRawan(4, -7.243544f, 112.720932f, "Jalan Demak", "#3dbdce"));
//        ListArea.add(new DaerahRawan(5, -7.235295f, 112.609799f, "Benowo", "#4d3dce"));
//        ListArea.add(new DaerahRawan(6, -7.276666f, 112.794722f, "Huwalaa", "#cb3dce"));

        JSON_HTTP_CALL();


        return root;
    }

    @Override
    protected String getTitle() {
        return title;
    }

    @Override
    public void onStart() {
        super.onStart();

//        if (checkPermissions()){
//            addGeofences();
//        }
    }

    public void proses(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mapFragment).commit();
        }
        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<>();

        // Initially set the PendingIntent used in addGeofences() and removeGeofences() to null.
        mGeofencePendingIntent = null;

        //setButtonsEnabledState();

        // Get the geofences used. Geofence data is hard coded in this sample.
        populateGeofenceList();

        mGeofencingClient = LocationServices.getGeofencingClient(getActivity());

        if (checkPermissions()){
            addGeofences();
        }
    }

    @SuppressWarnings("MissingPermission")
    private void addGeofences() {
        if (!checkPermissions()) {
            showSnackbar(getString(R.string.insufficient_permissions));
            return;
        }

        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnCompleteListener(this);
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(getActivity(), GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }

    private void showSnackbar(final String text) {
        View container = getView().findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void populateGeofenceList() {
        for (DaerahRawan entry : ListArea) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(Integer.toString(entry.getId()))

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getLatitude(),
                            entry.getLongitude(),
                            1000
                    )

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(12 * 60 * 60 * 1000)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                    // Create the geofence.
                    .build());
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in some place and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));

        if (null != mLastKnownLocation)
            getDeviceLocation();
        else
            getLocationPermission();

        drawListArea();
    }

    private void drawListArea() {


        for (DaerahRawan daerahRawan : ListArea) {
            LatLng coordinate = new LatLng(daerahRawan.getLatitude(), daerahRawan.getLongitude());
            mMap.addMarker(new MarkerOptions().position(coordinate).title(daerahRawan.getNama()).snippet(daerahRawan.getIsi()));


            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(daerahRawan.getLatitude(), daerahRawan.getLongitude()))
                    .radius(1000)
                    //.fillColor(ColorUtils.setAlphaComponent(Color.RED, 128))
                    .fillColor(ColorUtils.setAlphaComponent(Color.parseColor(daerahRawan.getWarna()), 128))
                    //.fillColor(Color.parseColor(daerahRawan.getWarna()))
                    .strokeWidth(2)
                    .strokeColor(Color.parseColor(daerahRawan.getWarna())));
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext()/*getActivity().getParent()*/);
                // Add the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            else {
                                Log.d(TAG, "Current location is null. Using defaults.");
                                Log.e(TAG, "Exception: %s", task.getException());
                                mMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
        updateLocationUI();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        getDeviceLocation();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    public void JSON_HTTP_CALL(){

        RequestOfJSonArray = new JsonArrayRequest(DAERAHRAWAN_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("OnResponse", "Success");
                        ParseJSonResponse(response);
                        proses();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("OnError", "Error");
                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        requestQueue.add(RequestOfJSonArray);
    }

    public void ParseJSonResponse(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            DaerahRawan daerahRawan = new DaerahRawan();

            JSONObject json = null;
            try {

                json = array.getJSONObject(i);

                // Adding image title name in array to display on RecyclerView click event.

                daerahRawan.setId(json.getInt(id_daerah_rawan));
                daerahRawan.setLongitude((float) json.getDouble(longitude));
                daerahRawan.setLatitude((float) json.getDouble(latitude));
                daerahRawan.setNama(json.getString(nama));
                daerahRawan.setWarna(json.getString(warna));
                daerahRawan.setIsi(json.getString(isi));
            } catch (JSONException e) {

                e.printStackTrace();
            }
            //ListArea = new ArrayList<>();
            ListArea.add(daerahRawan);
        }
    }
}
