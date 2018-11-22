package org.secuso.privacyfriendlyfoodtracker.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product... product);

    @Update
    void update(Product... product);

    @Delete
    void delete(Product... product);
}
