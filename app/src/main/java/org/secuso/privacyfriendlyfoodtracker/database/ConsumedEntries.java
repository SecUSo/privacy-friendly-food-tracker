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

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.secuso.privacyfriendlyfoodtracker.database.converter.DateConverter;


import java.sql.Date;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Information about consuming.
 *
 * @author Andre Lutz
 */
@Entity(foreignKeys = @ForeignKey(entity = Product.class,
        parentColumns = "id",
        childColumns = "productId",
        onDelete = CASCADE))
public class ConsumedEntries {
    @PrimaryKey(autoGenerate = true)
    public final int id;
    public final int productId;
    public int amount;
    @TypeConverters({DateConverter.class}) public final Date date;
    public final String name;

    public ConsumedEntries(final int id, final int amount, final Date date, final String name, final int productId) {
        this.id = id;
        this.productId = productId;
        this.amount = amount;
        this.date = date;
        this.name = name;
    }
}
