package com.example.android.emergencybutton.Adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.android.emergencybutton.Activity.DetailKejadianActivity;
import com.example.android.emergencybutton.GlideApp;
import com.example.android.emergencybutton.Model.PostKejadian;
import com.example.android.emergencybutton.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by nugraha on 12/7/2017.
 */

public class TimelineProfileAdapter extends RecyclerViewAdapter {

    public TimelineProfileAdapter(List<PostKejadian> getDataAdapter, Context context) {
        super(getDataAdapter, context);
    }

    public void onBindViewHolder(ViewHolder Viewholder, final int position) {

        PostKejadian dataAdapterOBJ =  dataAdapters.get(position);

//        imageLoader = ImageAdapter.getInstance(context).getImageLoader();
//
//        imageLoader.get(dataAdapterOBJ.getGambar(),
//                ImageLoader.getImageListener(
//                        Viewholder.imageViewGambar,//Server Image
//                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
//                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
//                )
//        );
//
//        Viewholder.imageViewGambar.setImageUrl(dataAdapterOBJ.getGambar(), imageLoader);
//        Viewholder.imageViewFoto.setImageUrl(dataAdapterOBJ.getFoto(), imageLoader);

        String gambar = dataAdapterOBJ.getGambar();

        GlideApp.with(context)
                .load(Uri.parse(gambar)) // add your image url
                .error(R.drawable.ic_android_black_24dp)
                .apply(new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .into(Viewholder.imageViewGambar);

        String foto = dataAdapterOBJ.getFoto();

        GlideApp.with(context)
                .load(Uri.parse(foto)) // add your image url
                .apply(new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .into(Viewholder.imageViewFoto);

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
        String str= Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;
        Log.e("TAG", "ConvertTimeStampintoAgo: "+ConvertTimeStampintoAgo(timestamp));
        String tanggal = ConvertTimeStampintoAgo(timestamp);

        Double latitude = Double.valueOf(dataAdapterOBJ.getLatitude());
        Double longitude = Double.valueOf(dataAdapterOBJ.getLongitude());

        Viewholder.textViewJudul.setText(dataAdapterOBJ.getJudul());
        Viewholder.textViewCaption.setText(dataAdapterOBJ.getCaption());
        Viewholder.textViewTanggalPosting.setText(tanggal);
        Viewholder.textViewNama.setText(dataAdapterOBJ.getNama());
        Viewholder.textViewAlamat.setText(getAddressFromLocation(latitude,longitude));

        Viewholder.rl_timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTimeline.onItemClick(position);
            }
        });

    }

    private String getAddressFromLocation(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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

}
