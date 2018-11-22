package org.secuso.privacyfriendlyfoodtracker.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    /*@Query("SELECT * FROM consumedEntries WHERE date=:date")
    List<ConsumedEntries> findConsumedEntriesForDate(final Date date);*/
}
