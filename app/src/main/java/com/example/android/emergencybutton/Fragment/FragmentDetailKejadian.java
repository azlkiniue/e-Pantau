package com.example.android.emergencybutton.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.android.emergencybutton.GlideApp;
import com.example.android.emergencybutton.Model.PostKejadian;
import com.example.android.emergencybutton.R;
import com.example.android.emergencybutton.base.BaseFragment;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ASUS on 12/17/2017.
 */

public class FragmentDetailKejadian extends BaseFragment {

    private String title = "Detail Kejadian";
    Toolbar toolbar;
    public String dataPost_ID = "id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_detail_kejadian, null);

        TextView textViewJudul;
        TextView textViewCaption;
        TextView textViewTanggalPosting;
        TextView textViewNama;
        TextView textViewAlamat;
        ImageView imageViewGambar;
        ImageView imageViewFoto;

        textViewJudul = (TextView) root.findViewById(R.id.textViewJudul) ;
        textViewCaption = (TextView) root.findViewById(R.id.textViewCaption);
        textViewTanggalPosting = (TextView) root.findViewById(R.id.textViewTanggalPosting);
        textViewNama = (TextView) root.findViewById(R.id.textViewNama);
        textViewAlamat = (TextView) root.findViewById(R.id.textViewAlamat);
        imageViewGambar = (ImageView) root.findViewById(R.id.imageViewGambar);
        imageViewFoto = (ImageView) root.findViewById(R.id.imageViewFoto);

        PostKejadian dataAdapterOBJ;

        Bundle arguments = getArguments();

        dataAdapterOBJ = (PostKejadian) arguments.getSerializable(dataPost_ID);



        String gambar = dataAdapterOBJ.getGambar();

        GlideApp.with(this)
                .load(Uri.parse(gambar)) // add your image url
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.picture_default)
                .apply(new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .into(imageViewGambar);

        String foto = dataAdapterOBJ.getFoto();

        GlideApp.with(this)
                .load(Uri.parse(foto)) // add your image url
                .placeholder(R.drawable.profil_user)
                .error(R.drawable.profil_user)
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
        textViewNama.setText(dataAdapterOBJ.getNama());
        textViewAlamat.setText(getAddressFromLocation(latitude,longitude));

        return root;
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

    public String getAddressFromLocation(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        String alamat = "Alamat tidak tersedia";

        Log.d("j", "getAddressFromLocation: " + alamat + latitude + longitude);

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);

                alamat = fetchedAddress.getAddressLine(0);
            } else {
                alamat = "Alamat tidak tersedia";
            }
            return alamat;
        } catch (IOException e) {
            e.printStackTrace();
            alamat = "Alamat tidak tersedia";
            printToast("Could not get address..!");
            return alamat;
        }
        //printToast("HILIH!");
        //return alamat;
    }

    private void printToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
//    void setupToolbar(){
//        toolbar = (Toolbar) root.findViewById(R.id.toolbar);
//        getActivity().getActionBar().setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//    }

    @Override
    protected String getTitle() {
        return title;
    }
}
