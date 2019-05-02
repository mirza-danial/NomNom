package com.example.danialmirza.nomnom.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.danialmirza.nomnom.R;
import com.example.danialmirza.nomnom.model.Post;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.CustomViewHolder> {

    private  List<Post> posts;

    public List<Post> getCourses() {
        return posts;
    }

    public void setCourses(List<Post> courses) {
        this.posts = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewtype) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_mainfeed_post, parent, false);

        return new CustomViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Post course = posts.get(position);


        //RETRIEVE DATA FROM FIREBASE DATABASE AND SET CUSTOM_VIEW_HOLDER VIEWS VALUES
    }

    @Override
    public int getItemCount() {
        if(posts != null)
            return posts.size();
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView img_profile;
        public ImageView img_post;
        public TextView txt_username;
        public TextView txt_location;
        public RelativeLayout upvote;
        public RecyclerView downvote;
        public ImageView image_upvote;
        public ImageView image_upvote_selected;
        public ImageView image_downvote;
        public ImageView image_downvote_selected;

        public CustomViewHolder(View view)
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


            setButtonListeners();
        }
        private void setButtonListeners()
        {
            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(image_upvote.getVisibility() == View.VISIBLE)
                    {
                        image_upvote.setVisibility(View.INVISIBLE);
                        image_upvote_selected.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        image_upvote_selected.setVisibility(View.INVISIBLE);
                        image_upvote.setVisibility(View.VISIBLE);
                    }
                }
            });
            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(image_downvote.getVisibility() == View.VISIBLE)
                    {
                        image_downvote.setVisibility(View.INVISIBLE);
                        image_downvote_selected.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        image_downvote_selected.setVisibility(View.INVISIBLE);
                        image_downvote.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}
