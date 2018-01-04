package com.example.android.emergencybutton.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.emergencybutton.Controller.SharedPrefManager;
import com.example.android.emergencybutton.Controller.URLs;
import com.example.android.emergencybutton.Controller.VolleySingleton;
import com.example.android.emergencybutton.Model.Coordinate;
import com.example.android.emergencybutton.Model.LokasiKejadian;
import com.example.android.emergencybutton.Model.User;
import com.example.android.emergencybutton.R;
import com.example.android.emergencybutton.base.BaseFragment;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseDrawer;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentTombolDarurat extends BaseFragment {
    private static final int MY_PERMISSION_REQUEST_CODE = 17;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Button mEmergency;
    private Coordinate koor;
    String number = "081249127049";
    TelephonyManager mTelephonyMgr;
    User user;
    LokasiKejadian lokasi;
    SharedPrefManager sharedPrefManager;

    private Target b_emergency;

    private  int countador = 0;

    private ShowcaseView showcaseView;

    final int SHOWCASE_ID = 28;

    private String title = "Tombol Darurat";

    public static Fragment newInstance(Context context) {
        FragmentTombolDarurat f = new FragmentTombolDarurat();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_tombol_darurat, null);

        setHasOptionsMenu(true);

        // Create an instance of GoogleAPIClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        mEmergency = (Button) root.findViewById(R.id.b_emergency);
        mEmergency.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" +number));
                    startActivity(intent);
                }
                getLocation();
                notificationAlert();

                return true;
            }
        });

        return root;
    }

    @Override
    protected String getTitle() {
        return title;
    }


    private static class CustomShowcaseView implements ShowcaseDrawer {

        private final float width;
        private final float height;
        private final Paint eraserPaint;
        private final Paint basicPaint;
        private final int eraseColour;
        private final RectF renderRect;

        public CustomShowcaseView(Resources resources) {
            width = resources.getDimension(R.dimen.custom_showcase_width);
            height = resources.getDimension(R.dimen.custom_showcase_height);
            PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);
            eraserPaint = new Paint();
            eraserPaint.setColor(0xFFFFFF);
            eraserPaint.setAlpha(0);
            eraserPaint.setXfermode(xfermode);
            eraserPaint.setAntiAlias(true);
            eraseColour = resources.getColor(R.color.custom_showcase_bg);
            basicPaint = new Paint();
            renderRect = new RectF();
        }

        @Override
        public void setShowcaseColour(int color) {
            eraserPaint.setColor(color);
        }

        @Override
        public void drawShowcase(Bitmap buffer, float x, float y, float scaleMultiplier) {
            Canvas bufferCanvas = new Canvas(buffer);
            renderRect.left = x - width / 2f;
            renderRect.right = x + width / 2f;
            renderRect.top = y - height / 2f;
            renderRect.bottom = y + height / 2f;
            bufferCanvas.drawRect(renderRect, eraserPaint);
        }

        @Override
        public int getShowcaseWidth() {
            return (int) width;
        }

        @Override
        public int getShowcaseHeight() {
            return (int) height;
        }

        @Override
        public float getBlockedRadius() {
            return width;
        }

        @Override
        public void setBackgroundColour(int backgroundColor) {
            // No-op, remove this from the API?
        }

        @Override
        public void erase(Bitmap bitmapBuffer) {
            bitmapBuffer.eraseColor(eraseColour);
        }

        @Override
        public void drawToCanvas(Canvas canvas, Bitmap bitmapBuffer) {
            canvas.drawBitmap(bitmapBuffer, 0, 0, basicPaint);
        }

    }


    protected void sendLocation(final String longitude, final String latitude, final String id, final String telp, final String waktu){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ROOT_URL_LOKASI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                //Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject lokasiJson = obj.getJSONObject("lokasi");

                                //creating a new user object
                                LokasiKejadian lokasi = new LokasiKejadian(
                                        lokasiJson.getInt("id_tombol"),
                                        lokasiJson.getString("longitude"),
                                        lokasiJson.getString("latitude"),
                                        lokasiJson.getString("telp"),
                                        lokasiJson.getString("waktu")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getActivity().getApplicationContext()).setLokasi(lokasi);

                                //starting the profile activity
                                //finish();
                                //startActivity(new Intent(getActivity().getApplicationContext(), ProfileActivity.class));
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getActivity(), "gagal", Toast.LENGTH_SHORT).show();
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
//                JSONObject location = new JSONObject();
//                try {
//                    location.put("longitude", longitude);
//                    location.put("latitude", latitude);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                params.put("longitude", longitude);
                params.put("latitude", latitude);
//                params.put("message", location.toString());
//                params.put("waktu", waktu);
                params.put("name", id);
                //params.put("telp", telp);
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    protected void getLocation(){
        Log.d("GetLocation: ", "Success");
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("CheckPermission: ", "Success");
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (null != location) {
                        koor = new Coordinate();
                        onLocationChanged(location);
                        koor.setLongitude((float)location.getLongitude());
                        koor.setLatitude((float)location.getLatitude());

                        user = new User();

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
                        sendLocation(Double.toString(koor.getLongitude()).trim(), Double.toString(koor.getLatitude()).trim(), String.valueOf(user.getId()).trim(), String.valueOf(user.getTelepon()).trim(), currentDateandTime.toString() );


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
        Log.d("LocationLatitude = " , Double.toString(location.getLatitude()));
        Log.d("LocationLongitude = " , Double.toString(location.getLongitude()));
//        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        Log.d("LocationChanged: ", msg);
    }

    public void notificationAlert(){
        String tittle=String.valueOf("Notification e_Pantau").trim();
        String subject=String.valueOf("Notification e_Pantau").trim();
        String body=String.valueOf("Terima kasih. Silahkan tunggu").trim();

        NotificationManager notif=(NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notify = new Notification.Builder
                    (getActivity().getApplicationContext()).setContentTitle(tittle).setContentText(body).
                    setContentTitle(subject).setSmallIcon(R.drawable.icon_launcher).build();
        }

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            showcaseView = new ShowcaseView.Builder(getActivity())
                    .setTarget(new com.github.amlcurran.showcaseview.targets.ViewTarget(R.id.b_emergency, getActivity()))
                    .setContentTitle("Tombol Darurat")
                    .setContentText("Tekan tombol selama 2 detik" +
                            ", Anda akan terhubung ke command center dan mendapatkan notifikasi")
//                                    .setShowcaseDrawer(new CustomShowcaseView(getResources()))
                    .setStyle(R.style.showCaseViewStyle)
                    .build();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
