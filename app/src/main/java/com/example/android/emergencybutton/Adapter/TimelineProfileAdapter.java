package com.example.android.emergencybutton.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.android.emergencybutton.Controller.URLs;
import com.example.android.emergencybutton.GlideApp;
import com.example.android.emergencybutton.Model.PostKejadian;
import com.example.android.emergencybutton.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by nugraha on 12/7/2017.
 */

public class TimelineProfileAdapter extends RecyclerViewAdapter {

    public TimelineProfileAdapter(List<PostKejadian> getDataAdapter, Context context) {
        super(getDataAdapter, context);
    }

    public void onBindViewHolder(ViewHolder Viewholder, final int position) {

        PostKejadian dataAdapterOBJ =  dataAdapters.get(position);


        String gambar = URLs.URL_GAMBAR + dataAdapterOBJ.getGambar();

        GlideApp.with(context)
                .load(Uri.parse(gambar)) // add your image url
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.picture_default)
                .apply(new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .into(Viewholder.imageViewGambar);

        String foto = URLs.URL_FOTO + dataAdapterOBJ.getFoto();

        GlideApp.with(context)
                .load(Uri.parse(foto)) // add your image url
                .placeholder(R.drawable.profil_user)
                .error(R.drawable.profil_user)
                .apply(new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .into(Viewholder.imageViewFoto);

        String str_date = dataAdapterOBJ.getTanggal_posting();
        DateFormat formatter ;
        Date date = null;
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
}
