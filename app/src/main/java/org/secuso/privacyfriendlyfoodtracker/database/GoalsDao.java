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

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Includes methods that offer abstract access to the app database to manage products.
 *
 * @author Andre Lutz
 */
@Dao
public interface GoalsDao {
    @Insert
    void insert(Goals... goals);

    @Update
    void update(Goals... goals);

    @Delete
    void delete(Goals... goals);

    @Query("DELETE FROM goals")
    void deleteAll();

    @Query("SELECT * FROM goals")
    LiveData<List<Goals>> getAllProducts();

    @Query("SELECT * FROM goals WHERE id=:id")
    Goals findGoalsById(final int id);

    @Query("SELECT max(id),* FROM goals")
    Goals findMaxGoals();
}
