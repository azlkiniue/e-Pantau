package com.example.android.emergencybutton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.android.emergencybutton.R.id.editTextNIK;

/**
 * Created by ASUS on 10/15/2017.
 */

public class LaporActivity extends Fragment {

    TextView namaLapor;
    EditText editTextJudul, editTextCaption, editTextTag;
    User user1;
    ImageButton lokasi;

    public static Fragment newInstance(Context context) {
        FragmentTwo f = new FragmentTwo();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_lapor, null);

        namaLapor = (TextView) root.findViewById(R.id.namaLapor);

        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        namaLapor.setText(user.getNama());

        editTextJudul = (EditText) root.findViewById(R.id.namaKejadian);
        editTextCaption = (EditText) root.findViewById(R.id.caption);
        editTextTag = (EditText) root.findViewById(R.id.tag);

        user1 = new User();

        user1 = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String currentDateandTime = sdf.format(new Date());

        root.findViewById(R.id.buttonPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postKejadian(String.valueOf(user1.getId()).trim(), editTextJudul.getText().toString().trim(),  editTextCaption.getText().toString().trim(),  editTextTag.getText().toString().trim(), currentDateandTime.toString());
            }
        });

        root.findViewById(R.id.lokasiKejadian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), LaporMapsActivity.class));
            }
        });

        return root;
    }

    private void postKejadian(final String id, final String judul, final String caption, final String tag, final String tanggalposting ) {

        //first we will do the validations

        if (TextUtils.isEmpty(judul)) {
            editTextJudul.setError("Please enter Judul");
            editTextJudul.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(caption)) {
            editTextCaption.setError("Please enter Caption");
            editTextCaption.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(tag)) {
            editTextTag.setError("Please enter Tag");
            editTextTag.requestFocus();
            return;
        }



        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.POSTKEJADIAN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getActivity().getApplicationContext(), "Berhasil" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject postKejadianJson = obj.getJSONObject("post_kejadian");

                                //creating a new user object
                                PostKejadian postKejadian = new PostKejadian(
                                        postKejadianJson.getInt("id_post"),
                                        postKejadianJson.getString("judul"),
                                        postKejadianJson.getString("caption"),
                                        postKejadianJson.getString("tag"),
                                        postKejadianJson.getString("tanggal_posting")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getActivity().getApplicationContext()).postKejadian(postKejadian);

                                //starting the profile activity
                                //finish();
                                startActivity(new Intent(getActivity().getApplicationContext(), KejadianTerkiniActivity.class));
//                                finish();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Gagal" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("id_user", id);
                params.put("judul", judul);
                params.put("caption", caption);
                params.put("tag", tag);
                params.put("tanggal_posting",tanggalposting);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
