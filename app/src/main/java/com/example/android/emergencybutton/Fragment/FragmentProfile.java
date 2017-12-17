package com.example.android.emergencybutton.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.android.emergencybutton.Activity.DetailKejadianActivity;
import com.example.android.emergencybutton.Activity.EditActivity;
import com.example.android.emergencybutton.Controller.SharedPrefManager;
import com.example.android.emergencybutton.GlideApp;
import com.example.android.emergencybutton.Model.User;
import com.example.android.emergencybutton.R;
import com.example.android.emergencybutton.base.BaseFragment;

import java.security.MessageDigest;

/**
 * Created by ASUS on 10/17/2017.
 */

public class FragmentProfile extends BaseFragment {
    private DrawerLayout mDrawerLayout;
    private String title = "Profile";

    public static Fragment newInstance(Context context) {
        FragmentTombolDarurat f = new FragmentTombolDarurat();

        return f;
    }

    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_profile, null);

        User user = SharedPrefManager.getInstance(getActivity()).getUser();
//        mDrawerLayout = (DrawerLayout) root.findViewById(R.id.drawer_lyt);

        TextView nama = (TextView) root.findViewById(R.id.user_profile_name);
        ImageView imageProfile = (ImageView) root.findViewById(R.id.user_profile_photo);

        root.findViewById(R.id.detailKejadian).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DetailKejadianActivity.class));
            }
        });

        root.findViewById(R.id.buttonEditProfile).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentEditProfile fragment = new FragmentEditProfile();
                add(fragment);
//                startActivity(new Intent(getActivity(), EditActivity.class));
            }
        });

//        FragmentEditProfile fragment = new FragmentEditProfile();
//        Bundle args = new Bundle();
//        args.putInt(fragment.MOVIE_ID, selectedMovie.getId());
//        fragment.setArguments(args);
//        add(fragment);

        nama.setText(user.getNama());
        String q = user.getAlamat();
        String gambar = user.getFoto();

        Log.d("user", q);
        Log.d("gambarnya", String.valueOf(user.getFoto()));

        GlideApp.with(getActivity())
                .load(Uri.parse(gambar)) // add your image url
                .apply(new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .apply(new RequestOptions().transform(new CircleTransform(getActivity())))// applying the image transformer
                .into(imageProfile);

        return root;
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