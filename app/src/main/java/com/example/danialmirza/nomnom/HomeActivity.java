package com.example.danialmirza.nomnom;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener
        ,HomeFragment.OnFragmentInteractionListener
        ,SearchFragment.OnFragmentInteractionListener
        ,PostFragment.OnFragmentInteractionListener
        ,NotificationFragment.OnFragmentInteractionListener
        ,ProfileFragment.OnFragmentInteractionListener{

    private TextView mTextMessage;
    final Fragment Home = new HomeFragment();
    final Fragment Search = new SearchFragment();
    final Fragment Post = new PostFragment();
    final Fragment Notification = new NotificationFragment();
    final Fragment Profile = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = Home;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setItemIconTintList(null);

        // loadFragment(Home);

        fm.beginTransaction().add(R.id.fragment_container, Profile, "3").hide(Profile).commit();
        fm.beginTransaction().add(R.id.fragment_container, Notification, "2").hide(Notification).commit();
        fm.beginTransaction().add(R.id.fragment_container, Post, "2").hide(Post).commit();
        fm.beginTransaction().add(R.id.fragment_container, Search, "2").hide(Search).commit();
        fm.beginTransaction().add(R.id.fragment_container,Home, "1").commit();


    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch(menuItem.getItemId())
        {
            case R.id.navigation_home:
                fm.beginTransaction().hide(active).show(Home).commit();
                active = Home;
                return true;
            case R.id.navigation_search:
                fm.beginTransaction().hide(active).show(Search).commit();
                active = Search;
                return true;
            case R.id.navigation_post:
                fm.beginTransaction().hide(active).show(Post).commit();
                active = Post;
                return true;
            case R.id.navigation_notifications:
                fm.beginTransaction().hide(active).show(Notification).commit();
                active = Notification;
                return true;
            case R.id.navigation_profile:
                fm.beginTransaction().hide(active).show(Profile).commit();
                active = Profile;
                return true;


        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
