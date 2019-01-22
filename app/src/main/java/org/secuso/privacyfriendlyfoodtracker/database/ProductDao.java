package org.secuso.privacyfriendlyfoodtracker.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.sql.Date;
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

    @Query("SELECT * FROM product WHERE id=:id")
    Product findProductById(final int id);


    @Query("SELECT * FROM product WHERE name=:name AND energy=:energy AND barcode=:barcode")
    List<Product> findExistingProducts(String name, int energy, String barcode);

    @Query("SELECT * FROM product WHERE name LIKE :name")
    List<Product> findProductsByName(String name);


}
