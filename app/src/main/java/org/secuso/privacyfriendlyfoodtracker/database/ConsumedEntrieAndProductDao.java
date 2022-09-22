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

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseEntry;

import java.sql.Date;
import java.util.List;

/**
 * Includes methods that offer abstract access to the app database to manage products and consumed entries.
 *
 * @author Andre Lutz
 */
@Dao
public interface ConsumedEntrieAndProductDao {
    @Query("SELECT consumedEntries.date AS unique1, (sum(product.energy*consumedEntries.amount)) AS unique2 FROM consumedEntries INNER JOIN product ON consumedEntries.productId = product.id  WHERE consumedEntries.date BETWEEN :dayst AND :dayet GROUP BY consumedEntries.date ")
    List<DateCalories> getCaloriesPerDayinPeriod(final Date dayst, final Date dayet);

    @Query("SELECT consumedEntries.date AS unique1, sum(product.energy*consumedEntries.amount/100) AS unique2 FROM consumedEntries INNER JOIN product ON consumedEntries.productId = product.id  WHERE consumedEntries.date BETWEEN :dayst AND :dayet")
    List<DateCalories> getCaloriesPeriod(final Date dayst, final Date dayet);

    @Query("SELECT consumedEntries.amount AS amount, consumedEntries.id AS id,consumedEntries.name as name, product.energy as energy FROM consumedEntries INNER JOIN product ON consumedEntries.productId = product.id WHERE consumedEntries.date=:day")
    LiveData<List<DatabaseEntry>> findConsumedEntriesForDate(final Date day);

    static class DateCalories
    {
        public Date unique1;
        public int unique2;
    }
}
