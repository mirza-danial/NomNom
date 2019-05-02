package com.example.danialmirza.nomnom;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danialmirza.nomnom.Database.NomNomDatabase;
import com.example.danialmirza.nomnom.Utils.RecyclerViewListener;
import com.example.danialmirza.nomnom.Utils.RestaurantAdapter;
import com.example.danialmirza.nomnom.model.Restaurant;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    RecyclerView rv;
    EditText edt_search;
    RestaurantAdapter res_adapter;
    Button back;
    Button mic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        rv = findViewById(R.id.rv_search);
        edt_search = findViewById(R.id.edt_search);
        back = findViewById(R.id.btn_back);
        mic = findViewById(R.id.btn_mic);
        final Intent intent = getIntent();
        Boolean check = intent.getBooleanExtra("results",false);

        /*
        * SET CALLBACK LISTENER TO SEARCH BAR
        * MAKE ADAPTER
        * ATTACH ADAPTER TO RECYCLER VIEW
        * */


        res_adapter = new RestaurantAdapter();
        rv.setAdapter(res_adapter);


        if(!check) {


            rv.addOnItemTouchListener(new RecyclerViewListener(getApplicationContext(),
                    rv, new RecyclerViewListener.RecyclerTouchListener() {
                @Override
                public void onClickItem(View v, int position) {

                    Restaurant value = res_adapter.getRestaurants().get(position);
                    Intent i = new Intent(getApplicationContext(), RestaurantActivity.class);
                    i.putExtra("restaurant", value);
                    startActivity(i);
                }

                @Override
                public void onLongClickItem(View v, int position) {

                }
            }));

        }
        else{
            rv.addOnItemTouchListener(new RecyclerViewListener(getApplicationContext(),
                    rv, new RecyclerViewListener.RecyclerTouchListener() {
                @Override
                public void onClickItem(View v, int position) {

                    Restaurant value = res_adapter.getRestaurants().get(position);
                    intent.putExtra("restaurant",value);

                    setResult(RESULT_OK,intent  );
                    finish();
                }

                @Override
                public void onLongClickItem(View v, int position) {

                }
            }));
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());


        /*Add OnTextChangedListener to TextView so that evertime text changes
         items in recycler view change
         - Retreive data from local database
         */

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0)
                {
                    String name = s.toString();
                    // get data from database
                    NomNomDatabase Db = NomNomDatabase.getAppDatabase(getApplicationContext());
                    ArrayList<Restaurant> results = (ArrayList<Restaurant>) Db.restaurantDao().getRestaurants(name + "%");

                    res_adapter.setRestaurants(results);
                    res_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //go back to maps if back pressed
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //enable voice search
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpeech();
            }
        });

    }
    public void getSpeech()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i, 10);
        } else {
            Toast.makeText(this, "Either Google services have been disabled by you or your " +
                    "Device doesn't support Speech Input", Toast.LENGTH_SHORT).show();
        }

    }



    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && data != null && requestCode == 10)
        {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            edt_search.setText(result.get(0));   //result.get(0) contains the speech text returned in a string, place it in your edittext for search
        }

    }


}
