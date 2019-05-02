package com.example.danialmirza.nomnom;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danialmirza.nomnom.Database.NomNomDatabase;
import com.example.danialmirza.nomnom.Utils.UniversalImageLoader;
import com.example.danialmirza.nomnom.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener
        ,HomeFragment.OnFragmentInteractionListener
        ,SearchFragment.OnFragmentInteractionListener
        ,PostFragment.OnFragmentInteractionListener
        , NotificationFragment.OnFragmentInteractionListener
        , ProfileFragment.OnFragmentInteractionListener{

    private TextView mTextMessage;
    final Fragment Home = new HomeFragment();
    final Fragment Search = new SearchFragment();
    final Fragment Post = new PostFragment();
    final Fragment Notification = new NotificationFragment();
    final Fragment Profile = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = Home;

    private static final int REQUEST_CODE = 1;
    DatabaseReference myRef;
    static final ArrayList<Restaurant> restaurantList = new ArrayList();

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


     /*   Restaurant res = new Restaurant();
        res.setRestaurant_id("sCJwZVyJBC88yDvTqaaJ");
        res.setAddress(" 22,23 C Abul Hassan Isfahani Road، Main Road، Lahore");
        res.setName("Dera Restaurant");
        res.setLatitude(31.477827);
        res.setLongitude(74.30863);
        res.setPhoto_path("https://firebasestorage.googleapis.com/v0/b/nom-nom-237911.appspot.com/o/restaurants%2Fdera_restaurant.jpg?alt=media&token=48fca182-af76-45a7-bb74-7ee0ef77cab6");
        restaurantList.add(res);

*/

        setupFirebaseAuth();

        initImageLoader();

        if(auth.getCurrentUser() != null) {
            setContentView(R.layout.activity_home);
            setup();
        }
    }

    public void setup()
    {
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
    private void verifyPermissions(){
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){
            setup();
        }else{
            ActivityCompat.requestPermissions(HomeActivity.this,
                    permissions,
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }
    private void initImageLoader(){
        UniversalImageLoader imageLoader = new UniversalImageLoader(HomeActivity.this);
        ImageLoader.getInstance().init(imageLoader.getConfig());
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authlistener);
        checkCurentUser(auth.getCurrentUser());
        if(isNetworkAvailable()) {
            myRef = FirebaseDatabase.getInstance().getReference(getString(R.string.node_restaurants));
            retrieveObjects();

        }
        else
        {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authlistener!=null)
        {
            auth.removeAuthStateListener(authlistener);
        }
    }

    public void retrieveObjects(){

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restaurant value;
                String s="";
                restaurantList.clear();
                for(DataSnapshot x:dataSnapshot.getChildren()){

                    value=x.getValue(Restaurant.class);
                    s+=value.toString()+"\n";
                    restaurantList.add(value);

                    //Insert in local database
                    new InsertRestaurant(value).execute();
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("abc", "Failed to read value.", error.toException());
            }
        });

    }
    private class InsertRestaurant extends AsyncTask {

        Restaurant res;
        public InsertRestaurant(Restaurant res)
        {
            this.res = res;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            NomNomDatabase DB = NomNomDatabase.getAppDatabase(getApplicationContext());
            DB.restaurantDao().Insert(res);
            return null;
        }
    }


    //firebase
    public static FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authlistener;

    private void setupFirebaseAuth()
    {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        auth = FirebaseAuth.getInstance();

        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if user is logged in
                checkCurentUser(user);

                if(user!=null)
                {
                    Log.d(TAG, "onAuthStateChange:signed_in" + user.getUid());
                }
                else
                {
                    Log.d(TAG,"onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void checkCurentUser(FirebaseUser user)
    {
        Log.d(TAG,"checkCurrentUser: checking user is logged in.");

        if(user == null)
        {
            Intent intent = new Intent(this, SplashScreenActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
