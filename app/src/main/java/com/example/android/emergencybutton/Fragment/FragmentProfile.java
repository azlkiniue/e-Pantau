package com.example.android.emergencybutton.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.android.emergencybutton.Adapter.RecyclerViewAdapter;
import com.example.android.emergencybutton.Adapter.TimelineProfileAdapter;
import com.example.android.emergencybutton.Controller.SharedPrefManager;
import com.example.android.emergencybutton.GlideApp;
import com.example.android.emergencybutton.Model.PostKejadian;
import com.example.android.emergencybutton.Model.User;
import com.example.android.emergencybutton.R;
import com.example.android.emergencybutton.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.android.emergencybutton.Controller.URLs.URL_FOTO;
import static com.example.android.emergencybutton.Controller.URLs.URL_GAMBAR;

/**
 * Created by ASUS on 10/17/2017.
 */

public class FragmentProfile extends BaseFragment implements TimelineProfileAdapter.OnItemClickedTimeline{

    private String title = "Profile";

    private DrawerLayout mDrawerLayout;

    List<PostKejadian> ListOfdataAdapter;
    RecyclerView recyclerView;
    String HTTP_JSON_URL = "http://192.168.43.251/android_coba/profile.php";

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

    JsonArrayRequest RequestOfJSonArray;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    View view;
    int RecyclerViewItemPosition;
    RecyclerView.LayoutManager layoutManagerOfrecyclerView;
    RecyclerViewAdapter recyclerViewadapter;
    ArrayList<String> ImageTitleNameArrayListForClick;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_profile, null);

        User user = SharedPrefManager.getInstance(getActivity()).getUser();
//        mDrawerLayout = (DrawerLayout) root.findViewById(R.id.drawer_lyt);

        TextView nama = (TextView) root.findViewById(R.id.user_profile_name);
        ImageView imageProfile = (ImageView) root.findViewById(R.id.user_profile_photo);

//        root.findViewById(R.id.detailKejadian).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), DetailKejadianActivity.class));
//            }
//        });

        root.findViewById(R.id.buttonEditProfile).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentEditProfile fragment = new FragmentEditProfile();
                add(fragment);
//                startActivity(new Intent(getActivity(), EditActivity.class));
            }
        });

        String getIdUser = getArguments().getString("id_user");
        String getNama = getArguments().getString("nama");
        String getFoto = getArguments().getString("foto");

        nama.setText(getNama);
        String q = user.getAlamat();
        String gambar = URL_FOTO + getFoto;

        if (!getIdUser.equals(String.valueOf(user.getId()))){
            LinearLayout linearLayout = (LinearLayout) root.findViewById(R.id.buttonEditProfile);
            linearLayout.setVisibility(View.GONE);
        }
        Log.d("qwe", "onCreate: " + getIdUser + " " + user.getId());

        Log.d("user", q);
        Log.d("gambarnya", String.valueOf(user.getFoto()));

        GlideApp.with(getActivity())
                .load(Uri.parse(gambar)) // add your image url
                .error(R.drawable.profil_user)
                //.apply(new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .apply(new RequestOptions().transform(new CircleTransform(getActivity())))// applying the image transformer
                .into(imageProfile);

        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview2);

        layoutManagerOfrecyclerView = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

        JSON_HTTP_CALL(getIdUser);

        return root;
    }

    @Override
    protected String getTitle() {
        return title;
    }

    @Override
    public void onItemClick(int position) {
        PostKejadian dataAdapterOBJ =  ListOfdataAdapter.get(position);
        FragmentDetailKejadian fragment = new FragmentDetailKejadian();
        Bundle args = new Bundle();
        args.putSerializable(fragment.dataPost_ID, dataAdapterOBJ);
        fragment.setArguments(args);
        add(fragment);
//        final Intent intent = new Intent(context, DetailKejadianActivity.class);
//        intent.putExtras(bundle);
    }

    public class CircleTransform extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        public String getId() {
            return getClass().getName();
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }
    }


    public void JSON_HTTP_CALL(final String getIdUser) {

        stringRequest = new StringRequest(Request.Method.POST, HTTP_JSON_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ParseJSonResponse(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getIdUser);
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(stringRequest);
    }

    public void ParseJSonResponse(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {

            PostKejadian GetDataAdapter2 = new PostKejadian();

            JSONObject json = null;
            try {

                json = array.getJSONObject(i);

                // Adding image title name in array to display on RecyclerView click event.

                GetDataAdapter2.setJudul(json.getString(judul));
                GetDataAdapter2.setGambar(json.getString(gambar));
                GetDataAdapter2.setId_post(json.getInt(id_post));
                GetDataAdapter2.setId_user(json.getInt(id_user));
                GetDataAdapter2.setCaption(json.getString(caption));
                GetDataAdapter2.setTanggal_posting(json.getString(tanggal_posting));
                GetDataAdapter2.setLatitude(json.getString(latitude));
                GetDataAdapter2.setLongitude(json.getString(longitude));
                GetDataAdapter2.setNama(json.getString(nama));
                GetDataAdapter2.setFoto(json.getString(foto));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            ListOfdataAdapter.add(GetDataAdapter2);
        }

        recyclerViewadapter = new TimelineProfileAdapter(ListOfdataAdapter, getActivity());
        recyclerViewadapter.setOnClickTimeline(this);

        recyclerView.setAdapter(recyclerViewadapter);
    }
}
