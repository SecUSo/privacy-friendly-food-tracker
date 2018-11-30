package org.secuso.privacyfriendlyfoodtracker.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.sql.Date;
import java.util.List;

@Dao
public interface ConsumedEntriesDao {

    @Insert
    void insert(ConsumedEntries consumedEntries);

    @Update
    void update(ConsumedEntries... consumedEntries);

    @Delete
    void delete(ConsumedEntries... consumedEntries);

    @Query("SELECT * FROM consumedEntries")
    List<ConsumedEntries> getAllConsumedEntries();

    @Query("SELECT * FROM consumedEntries WHERE productId=:consumedEntriesId")
    List<ConsumedEntries> findConsumedEntriesForProduct(final int consumedEntriesId);

    @Query("SELECT productId FROM consumedEntries GROUP BY productId ORDER BY COUNT(productId) DESC")
    List<Integer> findMostCommonProducts();

    @Query("SELECT * FROM consumedEntries WHERE date=:date")
    List<ConsumedEntries> findConsumedEntriesForDate(final Date date);

    @Query("SELECT * FROM consumedEntries WHERE id=:id")
    List<ConsumedEntries> findConsumedEntriesById(final int id);

    @Query("SELECT * FROM consumedEntries WHERE productId=:productId AND amount=:amount AND date=:date AND name=:name  ")
    List<ConsumedEntries> findExistingConsumedEntries( int productId, int amount,  Date date, String name);


    @Query("DELETE FROM consumedEntries")
    void deleteAll();
}
