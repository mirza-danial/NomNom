package com.example.danialmirza.nomnom.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.danialmirza.nomnom.model.Restaurant;

import java.util.List;

@Dao
public interface RestaurantDao {

    @Query("Select * from restaurants where name like :name COLLATE NOCASE")
    public List<Restaurant> getRestaurants(String name);

    @Query("Select * from restaurants")
    public List<Restaurant> getAllRestaurants();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void Insert(Restaurant restaurant);

}
