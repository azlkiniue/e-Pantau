package com.example.android.emergencybutton.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.android.emergencybutton.Controller.SharedPrefManager;
import com.example.android.emergencybutton.Adapter.DrawerItemCustomAdapter;
import com.example.android.emergencybutton.Fragment.FragmentDaerahRawan;
import com.example.android.emergencybutton.Fragment.FragmentLapor;
import com.example.android.emergencybutton.Fragment.FragmentProfile;
import com.example.android.emergencybutton.Fragment.FragmentTombolDarurat;
import com.example.android.emergencybutton.Fragment.FragmentKejadianTerkini;
import com.example.android.emergencybutton.GlideApp;
import com.example.android.emergencybutton.Model.DataModel;
import com.example.android.emergencybutton.Model.User;
import com.example.android.emergencybutton.R;
import com.example.android.emergencybutton.base.BaseActivity;
import com.example.android.emergencybutton.base.BaseFragment;

import java.security.MessageDigest;

public class MainActivity extends BaseActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 77;
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_lyt);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        final User user = SharedPrefManager.getInstance(this).getUser();
        TextView textViewNameNavigation = (TextView) findViewById(R.id.nameNavigation);
        ImageView imageViewFoto = (ImageView) findViewById(R.id.menuProfile);

        setupToolbar();
        //toolbar.setLogo(android.R.drawable.ic_menu_help);

        DataModel[] drawerItem = new DataModel[5];

        drawerItem[0] = new DataModel(R.drawable.menu_tombol, "Tombol Darurat");
        drawerItem[1] = new DataModel(R.drawable.menu_lapor, "Lapor");
        drawerItem[2] = new DataModel(R.drawable.menu_kejadian, "Kejadian Terkini");
        drawerItem[3] = new DataModel(R.drawable.menu_daerahrawan, "Daerah Rawan");
        drawerItem[4] = new DataModel(R.drawable.menu_logout, "Logout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_lyt);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        findViewById(R.id.profileLayout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentProfile fragmentProfile = new FragmentProfile();

                Bundle data = new Bundle();//Use bundle to pass data
                data.putString("id_user", String.valueOf(user.getId()));//put string, int, etc in bundle with a key value
                data.putString("nama", String.valueOf(user.getNama()));
                data.putString("foto", String.valueOf(user.getFoto()));
                fragmentProfile.setArguments(data);//Finally set argument bundle to fragment

                add(fragmentProfile, true);
                //setTitle("e_Pantau : Profile");
                mDrawerLayout.closeDrawers();
//                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        textViewNameNavigation.setText(user.getNama());



        String gambar = user.getFoto();

        Log.d("gambarnya", String.valueOf(user.getNama()));

        GlideApp.with(MainActivity.this)
                .load(Uri.parse(gambar)) // add your image url
                .error(R.drawable.profil_user)
                .apply(new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .apply(new RequestOptions().transform(new CircleTransform(MainActivity.this)))// applying the image transformer
                .into(imageViewFoto);

//        String number = ("tel:" + numTxt.getText());
//        Intent mIntent = new Intent(Intent.ACTION_CALL);
//        mIntent.setData(Uri.parse(number));
// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);

            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            //You already have permission
            try {
                add(new FragmentTombolDarurat(), true);
//                startActivity(mIntent);
            } catch(SecurityException e) {
                e.printStackTrace();
            }
        }

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentTombolDarurat()).commit();
        add(new FragmentTombolDarurat(), true);
    }

    @Override
    protected ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    @Override
    protected DrawerLayout getDrawer() {
        return mDrawerLayout;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        BaseFragment fragment = null;

        switch (position) {
            case 0:
                fragment = new FragmentTombolDarurat();
                break;
            case 1:
                fragment = new FragmentLapor();
//                position = 2;
                break;
            case 2:
                fragment = new FragmentKejadianTerkini();
                break;
            case 3:
                fragment = new FragmentDaerahRawan();
                break;
            case 4:
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                finish();
                //fragment = new FragmentThree();
                break;

            default:
                break;
        }

        if (fragment != null) {

            add(fragment, true);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawers();

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_help) {
//            //Toast.makeText(MainActivity.this, "Save picture", Toast.LENGTH_LONG).show();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    public boolean onCreatOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
