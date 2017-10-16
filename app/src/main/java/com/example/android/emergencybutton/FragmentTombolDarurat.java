package com.example.android.emergencybutton;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.android.emergencybutton.R.id.editTextNama;
import static com.example.android.emergencybutton.R.id.editTextPassword;

public class FragmentTombolDarurat extends Fragment {
    private static final int MY_PERMISSION_REQUEST_CODE = 17;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Button mEmergency;
    private Coordinate koor;
    String number = "081249127049";
    TelephonyManager mTelephonyMgr;
    User user;
    LokasiKejadian lokasi;
    SharedPrefManager sharedPrefManager;
    ProgressBar progressBar;

        public static Fragment newInstance(Context context) {
            FragmentTombolDarurat f = new FragmentTombolDarurat();

            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_tombol_darurat, null);

            // Create an instance of GoogleAPIClient.
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            mEmergency = (Button) root.findViewById(R.id.b_emergency);
            user = new User();
            koor = new Coordinate();
            mEmergency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    lokasi = new LokasiKejadian(String.valueOf(koor.getLongitude()).trim(), String.valueOf(koor.getLatitude()).trim(), String.valueOf(user.getId()).trim(), String.valueOf(user.getTelepon());
                    getLocation();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" +number));
                    startActivity(intent);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date());

                    user = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser();
                    Log.d("user", String.valueOf(user.getId()));
                    Log.d("telepon", String.valueOf(user.getTelepon()));
                    Log.d("waktu", String.valueOf(currentDateandTime));
                    Log.d("Loatitude = " , Double.toString(koor.getLatitude()));
                    Log.d("Longitude = " , Double.toString(koor.getLongitude()));
//                    Toast.makeText(getActivity(), user.toString(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                    sendLocation(Double.toString(koor.getLatitude()).trim(), Double.toString(koor.getLatitude()).trim(), String.valueOf(user.getId()).trim(), String.valueOf(user.getTelepon()).trim(), currentDateandTime.toString() );

                }
            });

            return root;
        }

    protected void sendLocation(final String longitude, final String latitude, final String id, final String telp, final String waktu){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ROOT_URL_LOKASI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject lokasiJson = obj.getJSONObject("lokasi");

//                                //creating a new user object
//                                LokasiKejadian lokasi = new LokasiKejadian(
//                                        lokasiJson.getInt("longitude"),
//                                        lokasiJson.getString("latitude"),
//                                        lokasiJson.getString("telp"),
//                                        lokasiJson.getString("waktu")
//                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getActivity().getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                //finish();
                                //startActivity(new Intent(getActivity().getApplicationContext(), ProfileActivity.class));
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("longitude", longitude);
                params.put("latitude", latitude);
                params.put("id_user", id);
                params.put("telp", telp);
                params.put("waktu", waktu);
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    protected void getLocation(){
//        Coordinate koors;
//        final Location[] locations = new Location[1];
        Log.d("GetLocation: ", "Success");
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("CheckPermission: ", "Success");
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (null != location) {
//                        locations[0] = location;
                        onLocationChanged(location);
                        koor.setLongitude((float)location.getLongitude());
                        koor.setLatitude((float)location.getLatitude());
//                        koor = new Coordinate((float)location.getLongitude(), (float)location.getLatitude());
                        Log.d("CheckLocation: ", "Success");
                    } else {
                        Log.d("CheckLocation: ", "Failed");
                    }
                }
            });
            mFusedLocationProviderClient.getLastLocation().addOnFailureListener(getActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("CheckLocation: ", "Failed");
                }
            });

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                String[] permissionWeNeed = new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION };
                requestPermissions(permissionWeNeed, MY_PERMISSION_REQUEST_CODE);
            }
            Log.d("CheckPermission: ", "Failed");
        }
    }

    protected void onLocationChanged(Location location){
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        Log.d("LocationChanged: ", msg);
    }

}