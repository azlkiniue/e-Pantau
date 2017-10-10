package com.example.android.emergencybutton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    //ImageView logo_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            //finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        //logo_login = (ImageView) findViewById(R.id.logo_login);

        ///Glide.with(this).load("@drawable/logo2").into(logo_login);

        editTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if(hasfocus){
                    view.setBackgroundResource(R.drawable.edittext_focus);
                }
                else {
                    view.setBackgroundResource(R.drawable.edittext_lost);
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if(hasfocus){
                    view.setBackgroundResource(R.drawable.edittext_focus);
                }
                else {
                    view.setBackgroundResource(R.drawable.edittext_lost);
                }
            }
        });

        //if user presses on login
        //calling the method login
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editTextUsername.getText().toString().isEmpty() && editTextPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Tolong isi data !!!!!!!!!!", Toast.LENGTH_SHORT).show();
                }else{
                    userLogin(editTextUsername.getText().toString(), editTextPassword.getText().toString());
                }

                userLogin(editTextUsername.getText().toString(), editTextPassword.getText().toString());
            }
        });

        //if user presses on not registered
        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                finish();
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            }
        });


    }

    private void userLogin(final String username, final String password) {
        //first getting the values

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }


        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
//                                JSONObject userJson = obj.getJSONObject("user");
//
//                                //creating a new user object
//                                User user = new User(
//                                        userJson.getInt("id"),
//                                        userJson.getString("nik"),
//                                        userJson.getString("nama"),
//                                        userJson.getString("alamat"),
//                                        userJson.getString("telepon"),
//                                        userJson.getString("username")
//                                );
//
//                                //storing the user in shared preferences
//                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                //finish();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {

//                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
//                                alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
//                                        alertDialogBuilder.setPositiveButton("yes",
//                                                new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface arg0, int arg1) {
//                                                        Toast.makeText(LoginActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
//                                                    }
//                                                });
//
//                                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        finish();
//                                    }
//                                });
//
//                                AlertDialog alertDialog = alertDialogBuilder.create();
//                                alertDialog.show();

                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
