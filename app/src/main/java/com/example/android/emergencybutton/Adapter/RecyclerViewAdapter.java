package com.example.android.emergencybutton.Adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.android.emergencybutton.Activity.DetailKejadianActivity;
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

/**
 * Created by Juned on 2/8/2017.
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    public static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static final int MAX_LINES = 3;

    Context context;

    List<PostKejadian> dataAdapters;

    ImageLoader imageLoader, imageLoader2;


    public RecyclerViewAdapter(List<PostKejadian> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

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
        String str=Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;
        Log.e("TAG", "ConvertTimeStampintoAgo: "+ConvertTimeStampintoAgo(timestamp));
        String tanggal = ConvertTimeStampintoAgo(timestamp);

        Double latitude = Double.valueOf(dataAdapterOBJ.getLatitude());
        Double longitude = Double.valueOf(dataAdapterOBJ.getLongitude());

        Viewholder.textViewJudul.setText(dataAdapterOBJ.getJudul());
        Viewholder.textViewCaption.setText(dataAdapterOBJ.getCaption());
        Viewholder.textViewTanggalPosting.setText(tanggal);
        Viewholder.textViewTag1.setText(dataAdapterOBJ.getTag1());
//        Viewholder.textViewTag2.setText(dataAdapterOBJ.getTag2());
//        Viewholder.textViewTag3.setText(dataAdapterOBJ.getTag3());
        Viewholder.textViewNama.setText(dataAdapterOBJ.getNama());
        Viewholder.textViewAlamat.setText(getAddressFromLocation(latitude,longitude));

        Bundle bundle = new Bundle();
        bundle.putSerializable("bundle", dataAdapterOBJ);
        final Intent intent = new Intent(context, DetailKejadianActivity.class);
        intent.putExtras(bundle);
        Viewholder.relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
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

    private void printToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewJudul;
        public TextView textViewCaption;
        public TextView textViewTanggalPosting;
        public TextView textViewTag1;
        public TextView textViewTag2;
        public TextView textViewTag3;
        public TextView textViewNama;
        public TextView textViewAlamat;
        public ImageView imageViewGambar;
        public ImageView imageViewFoto;
        public RelativeLayout relativeLayout3;


        public ViewHolder(View itemView) {

            super(itemView);

            textViewJudul = (TextView) itemView.findViewById(R.id.textViewJudul) ;
            textViewCaption = (TextView) itemView.findViewById(R.id.textViewCaption);
            textViewTanggalPosting = (TextView) itemView.findViewById(R.id.textViewTanggalPosting);
            textViewTag1 = (TextView) itemView.findViewById(R.id.textViewTag1);
            textViewTag2 = (TextView) itemView.findViewById(R.id.textViewTag2);
            textViewTag3 = (TextView) itemView.findViewById(R.id.textViewTag3);
            textViewNama = (TextView) itemView.findViewById(R.id.textViewNama);
            textViewAlamat = (TextView) itemView.findViewById(R.id.textViewAlamat);
            imageViewGambar = (ImageView) itemView.findViewById(R.id.imageViewGambar);
            imageViewFoto = (ImageView) itemView.findViewById(R.id.imageViewFoto);
            relativeLayout3 = (RelativeLayout) itemView.findViewById(R.id.relativeLayout3);
        }
    }
}