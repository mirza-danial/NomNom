package com.example.danialmirza.nomnom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.danialmirza.nomnom.Utils.SquareImageView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostActivity extends AppCompatActivity {

    //Widgets
    TextView txt_toolbar;
    Button btn_back;
    CircleImageView profile_image;
    TextView txt_userName;
    TextView txt_restName;
    SquareImageView post_image;
    RatingBar ratingBar;
    TextView txt_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        String username =  intent.getStringExtra("username");
        String rest_name = intent.getStringExtra("restaurant");
        Float rating = intent.getFloatExtra("rating",0);
        String review =intent.getStringExtra("review");
        String proflilepath =intent.getStringExtra("profile_imgage");
        String postpath =intent.getStringExtra("post_image");

        initWidgets();
        txt_toolbar.setText("Review");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_userName.setText(username);
        txt_restName.setText(rest_name);
        txt_review.setText(review);

        ratingBar.setRating(rating);

        Picasso.get().load(proflilepath).fit().error(R.drawable.profile_avatar_placeholder).into(profile_image);
        Picasso.get().load(postpath).fit().error(R.drawable.img_placeholder).into(post_image);


    }

    void initWidgets()
    {
        txt_toolbar = findViewById(R.id.txt_topbar);
        btn_back = findViewById(R.id.back);

        profile_image = findViewById(R.id.img_profile);
        txt_userName = findViewById(R.id.txt_username);
        txt_restName = findViewById(R.id.txt_location);
        post_image = findViewById(R.id.img_post);
        ratingBar = findViewById(R.id.ratting);
        txt_review = findViewById(R.id.txt_review);
    }
}
