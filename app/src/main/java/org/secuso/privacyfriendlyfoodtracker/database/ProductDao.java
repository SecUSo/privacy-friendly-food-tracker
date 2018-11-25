package org.secuso.privacyfriendlyfoodtracker.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product... product);

    @Update
    void update(Product... product);

    @Delete
    void delete(Product... product);

    @Query("DELETE FROM product")
    void deleteAll();

    @Query("SELECT * FROM product")
    List<Product> getAllProducts();


}
