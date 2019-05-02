package com.example.danialmirza.nomnom.Database;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import com.example.danialmirza.nomnom.model.Restaurant;
import java.lang.Double;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDao_Impl implements RestaurantDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfRestaurant;

  public RestaurantDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRestaurant = new EntityInsertionAdapter<Restaurant>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `restaurants`(`restaurant_id`,`longitude`,`latitude`,`address`,`name`,`photo_path`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Restaurant value) {
        if (value.getRestaurant_id() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getRestaurant_id());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindDouble(2, value.getLongitude());
        }
        if (value.getLatitude() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindDouble(3, value.getLatitude());
        }
        if (value.getAddress() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getAddress());
        }
        if (value.getName() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getName());
        }
        if (value.getPhoto_path() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPhoto_path());
        }
      }
    };
  }

  @Override
  public void Insert(Restaurant restaurant) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfRestaurant.insert(restaurant);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Restaurant> getRestaurants(String name) {
    final String _sql = "Select * from restaurants where name like ? COLLATE NOCASE";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfRestaurantId = _cursor.getColumnIndexOrThrow("restaurant_id");
      final int _cursorIndexOfLongitude = _cursor.getColumnIndexOrThrow("longitude");
      final int _cursorIndexOfLatitude = _cursor.getColumnIndexOrThrow("latitude");
      final int _cursorIndexOfAddress = _cursor.getColumnIndexOrThrow("address");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfPhotoPath = _cursor.getColumnIndexOrThrow("photo_path");
      final List<Restaurant> _result = new ArrayList<Restaurant>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Restaurant _item;
        final String _tmpRestaurant_id;
        _tmpRestaurant_id = _cursor.getString(_cursorIndexOfRestaurantId);
        final Double _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        final Double _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        final String _tmpAddress;
        _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpPhoto_path;
        _tmpPhoto_path = _cursor.getString(_cursorIndexOfPhotoPath);
        _item = new Restaurant(_tmpRestaurant_id,_tmpLongitude,_tmpLatitude,_tmpAddress,_tmpName,_tmpPhoto_path);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Restaurant> getAllRestaurants() {
    final String _sql = "Select * from restaurants";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfRestaurantId = _cursor.getColumnIndexOrThrow("restaurant_id");
      final int _cursorIndexOfLongitude = _cursor.getColumnIndexOrThrow("longitude");
      final int _cursorIndexOfLatitude = _cursor.getColumnIndexOrThrow("latitude");
      final int _cursorIndexOfAddress = _cursor.getColumnIndexOrThrow("address");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfPhotoPath = _cursor.getColumnIndexOrThrow("photo_path");
      final List<Restaurant> _result = new ArrayList<Restaurant>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Restaurant _item;
        final String _tmpRestaurant_id;
        _tmpRestaurant_id = _cursor.getString(_cursorIndexOfRestaurantId);
        final Double _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        final Double _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        final String _tmpAddress;
        _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpPhoto_path;
        _tmpPhoto_path = _cursor.getString(_cursorIndexOfPhotoPath);
        _item = new Restaurant(_tmpRestaurant_id,_tmpLongitude,_tmpLatitude,_tmpAddress,_tmpName,_tmpPhoto_path);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
