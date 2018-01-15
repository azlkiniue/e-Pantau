package com.example.android.emergencybutton.Adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.android.emergencybutton.Controller.SharedPrefManager;
import com.example.android.emergencybutton.Controller.VolleySingleton;
import com.example.android.emergencybutton.Fragment.FragmentDialogReport;
import com.example.android.emergencybutton.GlideApp;
import com.example.android.emergencybutton.Controller.URLs;
import com.example.android.emergencybutton.Model.PostKejadian;
import com.example.android.emergencybutton.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Juned on 2/8/2017.
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    public static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static final int MAX_LINES = 3;

    Context context;
    private OnItemClicked onClick;
    protected OnItemClickedTimeline onClickTimeline;

    List<PostKejadian> dataAdapters;

    ImageLoader imageLoader, imageLoader2;

    public interface OnItemClickedTimeline {
        void onItemClick(int position);
    }

    public void setOnClickTimeline(OnItemClickedTimeline onClickTimeline) {
        this.onClickTimeline = onClickTimeline;
    }


    public interface OnItemClicked {
        void onItemClickDetailKejadian(int position);
        void onItemClickProfile(int position);
    }

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
    public void onBindViewHolder(final ViewHolder Viewholder, final int position) {

        final PostKejadian dataAdapterOBJ =  dataAdapters.get(position);

        String gambar = URLs.URL_GAMBAR + dataAdapterOBJ.getGambar();

        GlideApp.with(context)
                .load(Uri.parse(gambar)) // add your image url
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.picture_default)
                .apply(new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .into(Viewholder.imageViewGambar);

        String foto = URLs.URL_FOTO + dataAdapterOBJ.getFoto();

        GlideApp.with(context)
                .load(Uri.parse(foto))// add your image url
                .placeholder(R.drawable.profil_user)
                .error(R.drawable.profil_user)
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

        Log.d("lat", "onBindViewHolder: " + getAddressFromLocation(latitude,longitude));

        Viewholder.textViewJudul.setText(dataAdapterOBJ.getJudul());
        Viewholder.textViewCaption.setText(dataAdapterOBJ.getCaption());
        Viewholder.textViewTanggalPosting.setText(tanggal);
        Viewholder.textViewNama.setText(dataAdapterOBJ.getNama());
        Viewholder.textViewAlamat.setText(getAddressFromLocation(latitude,longitude));

        Viewholder.txtOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(context, Viewholder.txtOptionMenu);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_item_report:
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.REPORTPOST_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Snackbar snackbar = Snackbar.make(view, "Terimakasih.. Permintaan Anda sedang di proses", Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Snackbar snackbar = Snackbar.make(view, "Tolong coba lagi, error: " + error.getMessage(), Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        String currentUserId = String.valueOf(SharedPrefManager.getInstance(context).getUser().getId());
                                        params.put("id_post", String.valueOf(dataAdapterOBJ.getId_post()));
                                        params.put("id_terlapor", String.valueOf(dataAdapterOBJ.getId_user()));
                                        params.put("id_pelapor", currentUserId);
                                        params.put("isi", "Dilaporkan oleh: " + SharedPrefManager.getInstance(context).getUser().getNama());
                                        return params;
                                    }
                                };
                                VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        Viewholder.relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClickDetailKejadian(position);
            }
        });

        Viewholder.relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClickProfile(position);
            }
        });

    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
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


    public String getAddressFromLocation(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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
        public TextView txtOptionMenu;
        public ImageView imageViewGambar;
        public ImageView imageViewFoto;
        public RelativeLayout relativeLayout3;
        public RelativeLayout relativeLayout1;
        public RelativeLayout rl_timeline;


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
            relativeLayout1 = (RelativeLayout) itemView.findViewById(R.id.relativeLayout1);
            rl_timeline = (RelativeLayout) itemView.findViewById(R.id.rl_timeline);
            txtOptionMenu = (TextView) itemView.findViewById(R.id.txtOptionDigit);
        }
    }
}