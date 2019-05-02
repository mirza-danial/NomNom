package com.example.danialmirza.nomnom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danialmirza.nomnom.model.Restaurant;
import com.squareup.picasso.Picasso;

public class RestaurantActivity extends AppCompatActivity {

    ImageView rest_image;
    TextView rest_name;
    TextView rest_location;
    Button map_button;
    Button back_button;
    TextView topTitle;
    Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Intent i = getIntent();

        restaurant =(Restaurant) i.getSerializableExtra("restaurant");

        initWidgets();
        setValues();
        attachCallbacks();

    }

    void initWidgets()
    {
        rest_image = findViewById(R.id.img_restaurant);
        rest_name = findViewById(R.id.restName);
        rest_location = findViewById(R.id.restLocation);

        back_button = findViewById(R.id.back);
        topTitle = findViewById(R.id.txt_topbar);

        map_button = findViewById(R.id.btn_map);
    }

    void setValues()
    {
        rest_name.setText(restaurant.getName());
        rest_location.setText(restaurant.getAddress());
        topTitle.setText(restaurant.getName());
        Picasso.get().load(restaurant.getPhoto_path()).fit().error(R.drawable.img_placeholder).into(rest_image);
    }
    void attachCallbacks()
    {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open maps activity
                String Lat = restaurant.getLatitude().toString();
                String Lon = restaurant.getLongitude().toString();

                Uri gmmIntentUri = Uri.parse(Lat + "," + Lon);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);




                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    // Attempt to start an activity that can handle the Intent
                    startActivity(mapIntent);
                }
            }
        });
    }
}
