package com.example.danialmirza.nomnom.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

@Entity(tableName = "restaurants")
public class Restaurant implements Serializable {
    @NonNull
    @PrimaryKey
    private String restaurant_id;

    private Double longitude;
    private Double latitude;

    private String address;
    private String name;
    private String photo_path;

    @Ignore
    public Restaurant() {
    }

    public Restaurant(@NonNull String restaurant_id, Double longitude, Double latitude, String address, String name, String photo_path) {
        this.restaurant_id = restaurant_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.name = name;
        this.photo_path = photo_path;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurant_id='" + restaurant_id + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", photo_path='" + photo_path + '\'' +
                '}';
    }

    public LatLng getLatLng(){
        return new LatLng(this.latitude,this.longitude);
    }

    @NonNull
    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(@NonNull String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }
}
