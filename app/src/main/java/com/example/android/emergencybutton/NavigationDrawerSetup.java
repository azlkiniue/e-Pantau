package com.example.android.emergencybutton;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Ahmada Yusril on 04/10/2017.
 */

class NavigationDrawerSetup {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private String[] mMenuTitles;

    public void configureDrawer(final AppCompatActivity activity){
//        mMenuTitles = activity.getResources().getStringArray(R.array.list_menu_array);
        mToolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(mToolbar);
        mDrawerLayout = activity.findViewById(R.id.drawer_layout);
        mNavigationView = activity.findViewById(R.id.nv_drawer);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectItem(item, activity);
                return true;
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
//        mDrawerList = (ListView) activity.findViewById(R.id.left_drawer);
//        mDrawerList.setAdapter(new ArrayAdapter<>(activity, R.layout.drawer_list_item, mMenuTitles));
//        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                selectItem(position);
//                mDrawerLayout.closeDrawer(mDrawerList);
//            }
//        });
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        activity.getActionBar().setHomeButtonEnabled(true);
//        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
//        mDrawerLayout.addDrawerListener(mDrawerToggle);
//        mDrawerToggle.syncState();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return false;
    }

    private void selectItem(MenuItem item, AppCompatActivity activity){
        switch (item.getItemId()){
            case R.id.item_emergency_button:
                break;
            case R.id.item_profile:
                break;
            case R.id.item_sign_out:
                break;
        }

        item.setChecked(true);
        activity.setTitle(item.getTitle());
        mDrawerLayout.closeDrawers();
    }

    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }

    public void setmDrawerLayout(DrawerLayout mDrawerLayout) {
        this.mDrawerLayout = mDrawerLayout;
    }

//    public ListView getmDrawerList() {
//        return mDrawerList;
//    }
//
//    public void setmDrawerList(ListView mDrawerList) {
//        this.mDrawerList = mDrawerList;
//    }

//    public String[] getmMenuTitles() {
//        return mMenuTitles;
//    }
//
//    public void setmMenuTitles(String[] mMenuTitles) {
//        this.mMenuTitles = mMenuTitles;
//    }
}
