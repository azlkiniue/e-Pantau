package com.example.android.emergencybutton.Activity;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.emergencybutton.Controller.SharedPrefManager;
import com.example.android.emergencybutton.Controller.URLs;
import com.example.android.emergencybutton.Controller.VolleySingleton;
import com.example.android.emergencybutton.Fragment.FragmentDialog;
import com.example.android.emergencybutton.Fragment.FragmentDialogSignUp;
import com.example.android.emergencybutton.Fragment.FragmentTombolDarurat;
import com.example.android.emergencybutton.Model.User;
import com.example.android.emergencybutton.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements FragmentDialogSignUp.InterfaceCommunicator {

    public static boolean status = false;
    StringRequest stringRequest;
    EditText editTextUsername,editTextPassword, editTextNIK, editTextNama, editTextAlamat, editTextTelp ;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, FragmentTombolDarurat.class));
            return;
        }

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextNIK = (EditText) findViewById(R.id.editTextNIK);
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextAlamat = (EditText) findViewById(R.id.editTextAlamat);
        editTextTelp = (EditText) findViewById(R.id.editTextTelp);

        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server

                registerUser(editTextNIK.getText().toString().trim(),  editTextNama.getText().toString().trim(),  editTextAlamat.getText().toString().trim(),  editTextTelp.getText().toString().trim(), editTextUsername.getText().toString().trim(), editTextPassword.getText().toString().trim());
            }
        });

        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on login
                //we will open the login screen
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });

        editTextNIK.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    view.setBackgroundResource(R.drawable.edittext_focus);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_lost);
                }
            }
        });

        editTextNama.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    view.setBackgroundResource(R.drawable.edittext_focus);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_lost);
                }
            }
        });

        editTextAlamat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    view.setBackgroundResource(R.drawable.edittext_focus);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_lost);
                }
            }
        });

        editTextTelp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    view.setBackgroundResource(R.drawable.edittext_focus);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_lost);
                }
            }
        });


        editTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    view.setBackgroundResource(R.drawable.edittext_focus);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_lost);
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    view.setBackgroundResource(R.drawable.edittext_focus);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_lost);
                }
            }
        });

    }

    private void registerUser(final String nik, final String nama, final String alamat, final String telepon, final String username,final String password ) {

        //first we will do the validations

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nik)) {
            editTextNIK.setError("Please enter your NIK");
            editTextNIK.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nama)) {
            editTextNama.setError("Please enter your Name");
            editTextNama.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(alamat)) {
            editTextAlamat.setError("Please Enter your alamat");
            editTextAlamat.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(telepon)) {
            editTextTelp.setError("Please enter your telp");
            editTextTelp.requestFocus();
            return;
        }

        FragmentDialogSignUp fragmentDialog = new FragmentDialogSignUp();
        fragmentDialog.show(getSupportFragmentManager(), "test");

        stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.has("status")) {
                                progressDialog.dismiss();

                                //creating a new user object
                                User user = new User(
                                        obj.getInt("id_user"),
                                        obj.getString("nik"),
                                        obj.getString("nama"),
                                        obj.getString("alamat"),
                                        obj.getString("telepon"),
                                        obj.getString("username"),
                                        obj.getString("foto")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));

                            } else {
                                Toast.makeText(getApplicationContext(), "Anda Gagal Sign Up", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        Toast.makeText(getApplicationContext(), error.getMessage() + "NULL", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nik", nik);
                params.put("nama", nama);
                params.put("alamat", alamat);
                params.put("telepon", telepon);
                params.put("username", username);
                params.put("password", password);
                return params;
            }

            @Override
            public Map < String, String > getHeaders() throws AuthFailureError {
                HashMap < String, String > headers = new HashMap < String, String > ();
                String encodedCredentials = Base64.encodeToString("zero:zerozerozero".getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + encodedCredentials);

                return headers;
            }
        };
    }

    @Override
    public void sendRequestCode(int code) {
        if (code == 1){
            progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Processing...");
            progressDialog.show();
            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
        Log.d("tag", "registerUser: " + code);
    }
}
