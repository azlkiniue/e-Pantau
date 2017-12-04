package com.example.android.emergencybutton.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.emergencybutton.Model.PostKejadian;
import com.example.android.emergencybutton.R;
import com.example.android.emergencybutton.Adapter.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 10/15/2017.
 */

public class FragmentKejadianTerkini extends Fragment {
    List<PostKejadian> ListOfdataAdapter;

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
    String tag1 = "tag1";
    String tag2 = "tag2";
    String tag3 = "tag3";
    String nama = "nama";
    String foto = "foto";

    JsonArrayRequest RequestOfJSonArray ;

    RequestQueue requestQueue ;

    View view ;

    int RecyclerViewItemPosition ;

    RecyclerView.LayoutManager layoutManagerOfrecyclerView;

    RecyclerView.Adapter recyclerViewadapter;

    ArrayList<String> ImageTitleNameArrayListForClick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_kejadian_terkini, null);

        ImageTitleNameArrayListForClick = new ArrayList<>();

        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview1);

        recyclerView.setHasFixedSize(true);

        layoutManagerOfrecyclerView = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

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
                GetPostKejadian2.setTag1(json.getString(tag1));
//                GetPostKejadian2.setTag2(json.getString(tag2));
//                GetPostKejadian2.setTag3(json.getString(tag3));
                GetPostKejadian2.setNama(json.getString(nama));
                GetPostKejadian2.setFoto(json.getString(foto));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            ListOfdataAdapter.add(GetPostKejadian2);
        }

        recyclerViewadapter = new RecyclerViewAdapter(ListOfdataAdapter, getActivity());

        recyclerView.setAdapter(recyclerViewadapter);
    }
}
