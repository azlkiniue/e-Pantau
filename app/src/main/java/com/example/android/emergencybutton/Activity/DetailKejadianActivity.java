package com.example.android.emergencybutton.Activity;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.android.emergencybutton.GlideApp;
import com.example.android.emergencybutton.Model.PostKejadian;
import com.example.android.emergencybutton.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailKejadianActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kejadian);
//        setupToolbar();

        TextView textViewJudul;
        TextView textViewCaption;
        TextView textViewTanggalPosting;
        TextView textViewTag1;
        TextView textViewTag2;
        TextView textViewTag3;
        TextView textViewNama;
        TextView textViewAlamat;
        ImageView imageViewGambar;
        ImageView imageViewFoto;

        textViewJudul = (TextView) findViewById(R.id.textViewJudul) ;
        textViewCaption = (TextView) findViewById(R.id.textViewCaption);
        textViewTanggalPosting = (TextView) findViewById(R.id.textViewTanggalPosting);
        textViewTag1 = (TextView) findViewById(R.id.textViewTag1);
        textViewTag2 = (TextView) findViewById(R.id.textViewTag2);
        textViewTag3 = (TextView) findViewById(R.id.textViewTag3);
        textViewNama = (TextView) findViewById(R.id.textViewNama);
        textViewAlamat = (TextView) findViewById(R.id.textViewAlamat);
        imageViewGambar = (ImageView) findViewById(R.id.imageViewGambar);
        imageViewFoto = (ImageView) findViewById(R.id.imageViewFoto);

        PostKejadian dataAdapterOBJ = (PostKejadian) getIntent().getExtras().getSerializable("bundle");


        String gambar = dataAdapterOBJ.getGambar();

        GlideApp.with(this)
                .load(Uri.parse(gambar)) // add your image url
                .error(R.drawable.ic_android_black_24dp)
                .apply(new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .into(imageViewGambar);

        String foto = dataAdapterOBJ.getFoto();

        GlideApp.with(this)
                .load(Uri.parse(foto)) // add your image url
                .apply(new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .into(imageViewFoto);

        String str_date= dataAdapterOBJ.getTanggal_posting();
        DateFormat formatter ;
        Date date =null;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = (Date)formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long output=date.getTime()/1000L;
        String str=Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;
        Log.e("TAG", "ConvertTimeStampintoAgo: "+ConvertTimeStampintoAgo(timestamp));
        String tanggal = ConvertTimeStampintoAgo(timestamp);

        Double latitude = Double.valueOf(dataAdapterOBJ.getLatitude());
        Double longitude = Double.valueOf(dataAdapterOBJ.getLongitude());

        textViewJudul.setText(dataAdapterOBJ.getJudul());
        textViewCaption.setText(dataAdapterOBJ.getCaption());
        textViewTanggalPosting.setText(tanggal);
        textViewTag1.setText(dataAdapterOBJ.getTag1());
//        textViewTag2.setText(dataAdapterOBJ.getTag2());
//        textViewTag3.setText(dataAdapterOBJ.getTag3());
        textViewNama.setText(dataAdapterOBJ.getNama());
        textViewAlamat.setText(getAddressFromLocation(latitude,longitude));
    }

    public static String ConvertTimeStampintoAgo(Long timeStamp)
    {
        try
        {
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTimeInMillis(timeStamp);
            String date = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", cal).toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date dateObj = sdf.parse(date);
            PrettyTime p = new PrettyTime();
            return p.format(dateObj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }


    private String getAddressFromLocation(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String alamat = "Alamat tidak tersedia";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();
                StringBuilder str = new StringBuilder();
                str.append(fetchedAddress.getThoroughfare()).append(", ").append(fetchedAddress.getSubThoroughfare());
                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex()-2; i++) {
                    if (i > 0)
                        strAddress.append(", ");
                    strAddress.append(fetchedAddress.getAddressLine(i));
                }
                alamat = strAddress.toString();
            } else {
                alamat = "Alamat tidak tersedia";
            }
            return alamat;
        } catch (IOException e) {
            e.printStackTrace();
            alamat = "Alamat tidak tersedia";
            //printToast("Could not get address..!");
        }
        return alamat;
    }

    private void printToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            Toast.makeText(DetailKejadianActivity.this, "Save picture", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
