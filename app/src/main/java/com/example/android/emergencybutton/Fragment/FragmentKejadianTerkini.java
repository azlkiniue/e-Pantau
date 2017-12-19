package com.example.android.emergencybutton.Fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.emergencybutton.Adapter.RecyclerViewAdapter;
import com.example.android.emergencybutton.Model.PostKejadian;
import com.example.android.emergencybutton.R;
import com.example.android.emergencybutton.base.BaseFragment;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 10/15/2017.
 */

public class FragmentKejadianTerkini extends BaseFragment implements RecyclerViewAdapter.OnItemClicked {
    List<PostKejadian> ListOfdataAdapter;
    private String title = "Kejadian Terkini";

    RecyclerView recyclerView;

    String HTTP_JSON_URL = "http://192.168.43.251/android_coba/ImageJsonData.php";

    String judul = "judul";
    String gambar = "gambar";
    String id_post = "id_post";
    String id_user = "id_user";
    String caption = "caption";
    String tanggal_posting = "tanggal_posting";
    String latitude = "latitude";
    String longitude = "longitude";
    String nama = "nama";
    String foto = "foto";

    JsonArrayRequest RequestOfJSonArray ;

    RequestQueue requestQueue ;

    View view ;

    int RecyclerViewItemPosition ;

    RecyclerView.LayoutManager layoutManagerOfrecyclerView;

    RecyclerViewAdapter recyclerViewadapter;

    ArrayList<String> ImageTitleNameArrayListForClick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_kejadian_terkini, null);

        setHasOptionsMenu(true);

        ImageTitleNameArrayListForClick = new ArrayList<>();

        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview1);

        recyclerView.setHasFixedSize(true);

        layoutManagerOfrecyclerView = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

        int version = GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        try {
            int v = getActivity().getPackageManager().getPackageInfo("com.google.android.gms", 0 ).versionCode;
            Log.d("v", "onCreate: " + v);
            if (v < version){
                new FragmentDialog().show(getActivity().getSupportFragmentManager(), "test");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        JSON_HTTP_CALL();

        return root;

    }

    public void JSON_HTTP_CALL(){

        RequestOfJSonArray = new JsonArrayRequest(HTTP_JSON_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("OnResponse", "Success");
                        ParseJSonResponse(response);
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

            PostKejadian GetPostKejadian2 = new PostKejadian();

            JSONObject json = null;
            try {

                json = array.getJSONObject(i);

                // Adding image title name in array to display on RecyclerView click event.
                ImageTitleNameArrayListForClick.add(json.getString(judul));

                GetPostKejadian2.setJudul(json.getString(judul));
                GetPostKejadian2.setGambar(json.getString(gambar));
                GetPostKejadian2.setId_post(json.getInt(id_post));
                GetPostKejadian2.setId_user(json.getInt(id_user));
                GetPostKejadian2.setCaption(json.getString(caption));
                GetPostKejadian2.setTanggal_posting(json.getString(tanggal_posting));
                GetPostKejadian2.setLatitude(json.getString(latitude));
                GetPostKejadian2.setLongitude(json.getString(longitude));
                GetPostKejadian2.setNama(json.getString(nama));
                GetPostKejadian2.setFoto(json.getString(foto));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            ListOfdataAdapter.add(GetPostKejadian2);
        }

        recyclerViewadapter = new RecyclerViewAdapter(ListOfdataAdapter, getActivity());
        recyclerViewadapter.setOnClick(this);

        recyclerView.setAdapter(recyclerViewadapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main2, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String getTitle() {
        return title;
    }

    @Override
    public void onItemClickDetailKejadian(int position) {
        PostKejadian dataAdapterOBJ =  ListOfdataAdapter.get(position);
        FragmentDetailKejadian fragment = new FragmentDetailKejadian();
        Bundle args = new Bundle();
        args.putSerializable(fragment.dataPost_ID, dataAdapterOBJ);
        fragment.setArguments(args);
        add(fragment);
    }

    @Override
    public void onItemClickProfile(int position) {
        PostKejadian dataAdapterOBJ =  ListOfdataAdapter.get(position);
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        //args.putSerializable(fragment.dataPost_ID, dataAdapterOBJ);
        args.putString("id_user", String.valueOf(dataAdapterOBJ.getId_user()));
        args.putString("nama", dataAdapterOBJ.getNama());
        args.putString("foto", dataAdapterOBJ.getFoto());
        fragment.setArguments(args);
        add(fragment);
    }

}
