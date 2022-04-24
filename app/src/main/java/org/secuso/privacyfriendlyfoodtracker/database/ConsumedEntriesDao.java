/*
This file is part of Privacy friendly food tracker.

Privacy friendly food tracker is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Privacy friendly food tracker is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Privacy friendly food tracker.  If not, see <https://www.gnu.org/licenses/>.
*/
package org.secuso.privacyfriendlyfoodtracker.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.sql.Date;
import java.util.List;
/**
 * Includes methods that offer abstract access to the app database to manage consumed entries.
 *
 * @author Andre Lutz
 */
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

    @Query("SELECT * FROM consumedEntries, product WHERE date BETWEEN DATE(:dayst) AND DATE(:dayet)")
    List<ConsumedEntries> findConsumedEntriesBetweenDates(final Date dayst, final Date dayet);

    @Query("SELECT * FROM consumedEntries WHERE id=:id")
    ConsumedEntries findConsumedEntriesById(final int id);

    @Query("SELECT * FROM consumedEntries WHERE productId=:productId AND amount=:amount AND date=:date AND name=:name  ")
    List<ConsumedEntries> findExistingConsumedEntries( int productId, int amount,  Date date, String name);



    @Query("DELETE FROM consumedEntries")
    void deleteAll();
}
