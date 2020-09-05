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
public interface WeightsDao {
    @Insert
    void insert(Weights... weights);

    @Update
    void update(Weights... weights);

    @Delete
    void delete(Weights... weights);

    @Query("DELETE FROM weights")
    void deleteAll();

    @Query("SELECT * FROM weights")
    LiveData<List<Weights>> getAllProducts();

    @Query("SELECT * FROM weights WHERE id=:id")
    Weights findWeightsById(final int id);

}
