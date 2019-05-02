package com.example.danialmirza.nomnom.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.danialmirza.nomnom.R;
import com.example.danialmirza.nomnom.model.Post;
import com.example.danialmirza.nomnom.model.Restaurant;
import com.example.danialmirza.nomnom.model.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView img_profile;
    public SquareImageView img_post;

    public TextView txt_username;
    public TextView txt_location;
    public TextView num_upvotes;
    public TextView num_downvotes;

    public RelativeLayout upvote;
    public RelativeLayout downvote;

    public ImageView image_upvote;
    public ImageView image_upvote_selected;
    public ImageView image_downvote;
    public ImageView image_downvote_selected;


    public PostViewHolder(View view)
    {
        super(view);

        img_profile = view.findViewById(R.id.img_profile);
        img_post = view.findViewById(R.id.img_post);

        txt_username = view.findViewById(R.id.txt_username);
        txt_location = view.findViewById(R.id.txt_location);

        upvote = view.findViewById(R.id.upvote);
        downvote = view.findViewById(R.id.downvote);
        image_upvote = view.findViewById(R.id.image_upvote);
        image_upvote_selected = view.findViewById(R.id.image_upvote_selected);
        image_downvote = view.findViewById(R.id.image_downvote);
        image_downvote_selected = view.findViewById(R.id.image_downvote_selected);

        num_downvotes = view.findViewById(R.id.num_downvotes);
        num_upvotes = view.findViewById(R.id.num_upvotes);

    }

    public void bindToPost(Post post, User user, Restaurant res, View.OnClickListener upvoteListener, View.OnClickListener downvoteListener, View.OnClickListener imageListener)
    {
        Picasso.get().load(post.getPhoto_path()).fit().error(R.drawable.img_placeholder).into(img_post);
        Picasso.get().load(user.getPhoto_path()).fit().error(R.drawable.profile_avatar_placeholder).into(img_profile);

        txt_username.setText(user.getUsername());
        txt_location.setText(res.getName());

        num_upvotes.setText(post.getUpvotes().toString());
        num_downvotes.setText(post.getDownvotes().toString());

        if(post.getDownvoters().contains(user.getUser_id())){
            image_downvote_selected.setVisibility(View.VISIBLE);
            image_downvote.setVisibility(View.GONE);
        }
        else{
            image_downvote_selected.setVisibility(View.GONE);
            image_downvote.setVisibility(View.VISIBLE);
        }

        if(post.getUpvoters().contains(user.getUser_id())){
            image_upvote_selected.setVisibility(View.VISIBLE);
            image_upvote.setVisibility(View.GONE);
        }
        else{
            image_upvote_selected.setVisibility(View.GONE);
            image_upvote.setVisibility(View.VISIBLE);
        }


        upvote.setOnClickListener(upvoteListener);
        downvote.setOnClickListener(downvoteListener);
        img_post.setOnClickListener(imageListener);
    }

}