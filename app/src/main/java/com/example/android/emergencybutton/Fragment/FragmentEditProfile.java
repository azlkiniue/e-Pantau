package com.example.android.emergencybutton.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.android.emergencybutton.Controller.SharedPrefManager;
import com.example.android.emergencybutton.Controller.URLs;
import com.example.android.emergencybutton.Controller.VolleySingleton;
import com.example.android.emergencybutton.GlideApp;
import com.example.android.emergencybutton.Model.User;
import com.example.android.emergencybutton.R;
import com.example.android.emergencybutton.base.BaseFragment;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ASUS on 12/17/2017.
 */

public class FragmentEditProfile extends BaseFragment {

    EditText editTextUsername, editTextPassword, editTextNIK, editTextNama, editTextAlamat, editTextTelp;
    ProgressBar progressBar;
    ImageView imageViewFoto;
    boolean check = true;
    Button buttonFoto;
    ProgressDialog progressDialog;
    Toolbar toolbar;
    private int userId;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    private String title = "Edit Profile";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_edit, null);

        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        editTextUsername = (EditText) root.findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) root.findViewById(R.id.editTextPassword);
        editTextNIK = (EditText) root.findViewById(R.id.editTextNIK);
        editTextNama = (EditText) root.findViewById(R.id.editTextNama);
        editTextAlamat = (EditText) root.findViewById(R.id.editTextAlamat);
        editTextTelp = (EditText) root.findViewById(R.id.editTextTelp);
        imageViewFoto = (ImageView) root.findViewById(R.id.imageViewFoto);

        buttonFoto = (Button) root.findViewById(R.id.buttonFoto);

        //getting the current user
        final User user = SharedPrefManager.getInstance(getActivity()).getUser();
        userId = user.getId();

        //Requesting storage permission
        requestStoragePermission();


        //setting the values to the textviews

        editTextNIK.setText(String.valueOf(user.getNik()));
        editTextNama.setText(user.getNama());
        editTextAlamat.setText(user.getAlamat());
        editTextTelp.setText(user.getTelepon());
        editTextUsername.setText(user.getUsername());

        editTextUsername.setInputType(InputType.TYPE_NULL);
        editTextUsername.setTextIsSelectable(true);
        editTextUsername.setKeyListener(null);

        editTextNIK.setInputType(InputType.TYPE_NULL);
        editTextNIK.setTextIsSelectable(true);
        editTextNIK.setKeyListener(null);

        editTextNama.setInputType(InputType.TYPE_NULL);
        editTextNama.setTextIsSelectable(true);
        editTextNama.setKeyListener(null);

        String gambar = URLs.URL_FOTO + user.getFoto();

        GlideApp.with(getActivity().getApplicationContext())
                .load(Uri.parse(gambar)) // add your image url
                .placeholder(R.drawable.profil_user)
                .error(R.drawable.profil_user)
                .apply(new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .apply(new RequestOptions().transform(new FragmentEditProfile.CircleTransform(getActivity())))// applying the image transformer
                .into(imageViewFoto);


        buttonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        root.findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Please Wait");
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                editUser(editTextNIK.getText().toString().trim(), editTextNama.getText().toString().trim(), editTextAlamat.getText().toString().trim(), editTextTelp.getText().toString().trim(), editTextUsername.getText().toString().trim(), editTextPassword.getText().toString().trim());
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

        return  root;
    }


    /*
    * This is the method responsible for image upload
    * We need the full image path and the name for the image in this method
    * */
    public void uploadMultipart() {
        //getting name for the image
        String name = editTextUsername.getText().toString().trim();


        if (filePath != null){
            //getting the actual path of the image
            String path = getPath(filePath);

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(getActivity(), uploadId, URLs.UPLOAD_PROFILE_URL)
                        .addFileToUpload(path, "image") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
                        .setBasicAuth("zero", "zerozerozero")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageViewFoto.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void editUser(final String nik, final String nama, final String alamat, final String telepon, final String username, final String password) {

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


        uploadMultipart();

        String editUrl = URLs.URL_EDIT + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, editUrl,
                new Response.Listener<String>() {
                    //                    mDrawerLayout.setDrawerListener(mDrawerToggle);
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

//                        Log.d("URL_EDIT", URLs.URL_EDIT);

                        try {
                            //converting response to json object

//                            Log.d("URL_EDIT", response);


                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.has("status")) {
                                progressDialog.dismiss();
//                                Toast.makeText(getActivity().getApplicationContext(), "Data berhasil diubah", Toast.LENGTH_SHORT).show();

                                //getting the user from the response
//                                JSONObject userJson = obj.getJSONObject("user");

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
                                SharedPrefManager.getInstance(getActivity().getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                //finish();

//                                FragmentProfile profileFragment = new FragmentProfile();
//                                FragmentManager fragmentManager = getSupportFragmentManager();
//                                fragmentManager.beginTransaction().replace(R.id.content_frame, profileFragment).commit();
//                                //setTitle("e_Pantau : Profile");
//                                mDrawerLayout.closeDrawers();
//                                startActivity(new Intent(getActivity().getApplicationContext(), ProfileActivity.class));

                                FragmentProfile fragment = new FragmentProfile();
                                Bundle args = new Bundle();
                                args.putString("id_user", String.valueOf(obj.getInt("id_user")));
                                args.putString("nama", obj.getString("nama"));
                                args.putString("foto", obj.getString("foto"));
                                fragment.setArguments(args);
                                add(fragment);
//                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap < String, String > headers = new HashMap < String, String > ();
                String encodedCredentials = Base64.encodeToString("zero:zerozerozero".getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + encodedCredentials);

                return headers;
            }

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
        };

        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    protected String getTitle() {
        return title;
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
}
