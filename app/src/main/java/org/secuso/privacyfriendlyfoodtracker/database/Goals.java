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
 * Goals
 *
 * @author fialo4ka
 */
@Entity
public class Goals {
    @PrimaryKey(autoGenerate = true)
    public final int id;
    public final int dailycalorie;
    public final int minweight;
    @TypeConverters({DateConverter.class}) public final Date date;

    public Goals(final int id, int dailycalorie, int minweight, Date date) {
        this.id = id;
        this.dailycalorie = dailycalorie;
        this.minweight = minweight;
        this.date = date;
    }
}
