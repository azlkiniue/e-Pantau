package com.example.android.emergencybutton.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.android.emergencybutton.Activity.MainActivity;
import com.example.android.emergencybutton.Controller.SharedPrefManager;
import com.example.android.emergencybutton.Controller.URLs;
import com.example.android.emergencybutton.Controller.VolleySingleton;
import com.example.android.emergencybutton.GlideApp;
import com.example.android.emergencybutton.Model.PostKejadian;
import com.example.android.emergencybutton.Model.User;
import com.example.android.emergencybutton.R;
import com.example.android.emergencybutton.base.BaseFragment;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by ASUS on 10/15/2017.
 */

public class FragmentLapor extends BaseFragment {

    TextView namaLapor, textViewLokasi, textViewLatitude, textViewLongitude;
    EditText editTextJudul, editTextCaption;
    User user1;
    boolean check = true;
    ImageButton buttonPhoto;
    ImageView imageProfile;

    double latitudeLoc, longitudeLoc;
    private String title = "Lapor";


    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    private static final String TAG = "LAPOR ACTIVITY";

    //Image request code
    private int PICK_IMAGE_REQUEST = 0;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    private ShowcaseView showcaseView;

    private  int countador = 0;

    private Target t1, t2, t3, t4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_lapor, null);

        setHasOptionsMenu(true);

        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        editTextJudul = (EditText) root.findViewById(R.id.namaKejadian);
        editTextCaption = (EditText) root.findViewById(R.id.caption);
        buttonPhoto = (ImageButton) root.findViewById(R.id.buttonPhoto);
        textViewLokasi = (TextView) root.findViewById(R.id.textViewLokasi);
        textViewLatitude = (TextView) root.findViewById(R.id.latitudeLoc);
        textViewLongitude = (TextView) root.findViewById(R.id.longitudeLoc);
        imageProfile = (ImageView) root.findViewById(R.id.profileLapor);

        textViewLatitude.setVisibility(View.GONE);
        textViewLongitude.setVisibility(View.GONE);

        user1 = new User();

        user1 = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser();

        requestStoragePermission();

        String gambar = user.getFoto();

        GlideApp.with(getActivity())
                .load(Uri.parse(gambar)) // add your image url
                .error(R.drawable.profilUser)
                .apply(new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .apply(new RequestOptions().transform(new CircleTransform(getActivity())))// applying the image transformer
                .into(imageProfile);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String currentDateandTime = sdf.format(new Date());

        root.findViewById(R.id.lokasiKejadian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
            }
        });


        root.findViewById(R.id.buttonPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postKejadian(String.valueOf(user1.getId()).trim(), editTextJudul.getText().toString().trim(),  editTextCaption.getText().toString().trim(), currentDateandTime.toString(), textViewLatitude.getText().toString().trim(), textViewLongitude.getText().toString().trim());
//                startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
//                Log.d("Latitude2", "" + textViewLatitude.getText());
//                Log.d("Longitude2", "" + textViewLongitude.getText());
            }
        });

        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        t1 = new ViewTarget(R.id.buttonPost, getActivity());
        t2 = new CustomViewTarget(R.id.buttonPhoto, -150, 50, getActivity());
        t3 = new CustomViewTarget(R.id.caption, -140, 0, getActivity());
        t4 = new CustomViewTarget(R.id.namaKejadian, -110, 0, getActivity());

        showcaseView = new ShowcaseView.Builder(getActivity())
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (countador){
                            case 0:
                                showcaseView.show();
                                showcaseView.setShowcase(t3,true);
                                showcaseView.setContentTitle("Caption");
                                showcaseView.setContentText("Tuliskan caption sesuai dengan keinginan Anda yang sesuai dengan kondisi kejadian");
                                showcaseView.setButtonText("Next");
                                break;
                            case 1:
                                showcaseView.show();
                                showcaseView.setShowcase(t2,true);
                                showcaseView.setContentTitle("Upload Data");
                                showcaseView.setContentText("Masukkan data yang benar dan lengkap");
                                showcaseView.setButtonText("Next");
                                break;
                            case 2:
                                showcaseView.show();
                                showcaseView.setShowcase(t1,true);
                                showcaseView.setContentTitle("Button Kirim");
                                showcaseView.setContentText("Tekan button setelah anda yakin semua data terisi dengan benar");
                                showcaseView.setButtonText("Close");
                                break;
                            case 3:
                                showcaseView.hide();
                                break;
                        }
                        countador++;

                    }
                })
                .setStyle(R.style.showCaseViewStyle)
                .build();
        showcaseView.setButtonText("Close");
        showcaseView.hide();

    }

    @Override
    protected String getTitle() {
        return  title;
    }


    public class CustomViewTarget implements Target {

        private final View mView;
        private int offsetX;
        private int offsetY;

        public CustomViewTarget(View view) {
            mView = view;
        }

        public CustomViewTarget(int viewId, int offsetX, int offsetY, Activity activity) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            mView = activity.findViewById(viewId);
        }

        @Override
        public Point getPoint() {
            int[] location = new int[2];
            mView.getLocationInWindow(location);
            int x = location[0] + mView.getWidth() / 2 + offsetX;
            int y = location[1] + mView.getHeight() / 2 + offsetY;
            return new Point(x, y);
        }
    }



    //Upload gambar

    /*
    * This is the method responsible for image upload
    * We need the full image path and the name for the image in this method
    * */
    public void uploadMultipart() {
        //getting name for the image
        String name = editTextJudul.getText().toString().trim();


        if (filePath != null){
            //getting the actual path of the image
            String path = getPath(filePath);

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(getActivity(), uploadId, URLs.UPLOADKEJADIAN_URL)
                        .addFileToUpload(path, "image") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
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

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST) {
            if(resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
//                imageViewFoto.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.d(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                textViewLokasi.setText(place.getName());

                LatLng latLang = place.getLatLng();
                latitudeLoc = latLang.latitude;
                longitudeLoc = latLang.longitude;

                textViewLatitude.setText(Double.toString(latitudeLoc));
                textViewLongitude.setText(Double.toString(longitudeLoc));

//                Log.d("LatLang = " , place.getLatLng().toString());
//                Log.d("Latitude is", "" + latitudeLoc);
//                Log.d("Longitude is", "" + longitudeLoc);


                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
//                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
                } else {
//                    mPlaceAttribution.setText("");
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
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
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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



    private void postKejadian(final String id, final String judul, final String caption, final String tanggalposting, final String latitude, final String longitude) {

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


        uploadMultipart();

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
                                        postKejadianJson.getString("tanggal_posting"),
                                        postKejadianJson.getString("latitude"),
                                        postKejadianJson.getString("longitude")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getActivity().getApplicationContext()).postKejadian(postKejadian);

                                FragmentKejadianTerkini fragment = new FragmentKejadianTerkini();
                                add(fragment);

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
                params.put("tanggal_posting",tanggalposting);
                params.put("latitude",latitude);
                params.put("longitude",longitude);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            countador = 0;
            showcaseView.show();
            showcaseView.setShowcase(t4,true);
            showcaseView.setContentTitle("Nama Kejadian");
            showcaseView.setContentText("Tuliskan nama kejadian yang sesuai dengan kondisi saat ini");
            showcaseView.setButtonText("Next");
            //Toast.makeText(MainActivity.this, "Save picture", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
