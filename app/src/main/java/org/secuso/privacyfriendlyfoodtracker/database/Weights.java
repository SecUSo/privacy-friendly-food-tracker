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

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import org.secuso.privacyfriendlyfoodtracker.database.converter.DateConverter;

import java.sql.Date;

/**
 * A Weights.
 *
 * @author fialo4ka
 */
@Entity
public class Weights {
    @PrimaryKey(autoGenerate = true)
    public final int id;
    @TypeConverters({DateConverter.class}) public final Date date;
    public final int weight;

    public Weights(final int id, Date date, int weight) {
        this.id = id;
        this.date = date;
        this.weight = weight;
    }
}
