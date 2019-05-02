package com.example.danialmirza.nomnom.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danialmirza.nomnom.R;
import com.example.danialmirza.nomnom.model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.CustomViewHolder> {

    private List<Restaurant> restaurants;


    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> courses) {
        this.restaurants = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewtype) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_restaurant_rv, parent, false);

        return new CustomViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
        Restaurant res = restaurants.get(position);


        holder.setRestaurant(res);
    }

    @Override
    public int getItemCount() {
        if(restaurants != null)
            return restaurants.size();
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_restaurant;
        private TextView txt_res_name;
        private TextView txt_res_location;

        private Restaurant res;

        public CustomViewHolder(View view)
        {
            super(view);
            img_restaurant = view.findViewById(R.id.img_rest);
            txt_res_location = view.findViewById(R.id.txt_res_loc);
            txt_res_name = view.findViewById(R.id.txt_res_name);


        }
        public void setRestaurant(Restaurant res)
        {
            this.res = res;
            this.txt_res_name.setText(res.getName());
            this.txt_res_location.setText(res.getAddress());
            Picasso.get().load(res.getPhoto_path()).fit().into(this.img_restaurant);
        }


    }
}