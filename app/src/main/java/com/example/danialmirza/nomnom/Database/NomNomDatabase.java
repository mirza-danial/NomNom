package com.example.danialmirza.nomnom.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.danialmirza.nomnom.model.Restaurant;

@Database(entities = {Restaurant.class}, version = 1)
public abstract class NomNomDatabase extends RoomDatabase {

    private static NomNomDatabase INSTANCE;

    public abstract RestaurantDao restaurantDao();


    public static NomNomDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, NomNomDatabase.class, "NomNomDatabase")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
