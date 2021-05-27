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

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * A Product.
 *
 * @author Andre Lutz
 */
@Entity
public class Product {
    @PrimaryKey(autoGenerate = true)
    public final int id;
    public final String name;
    public final float energy;
    public final float carbs;
    public final float sugar;
    public final float protein;
    public final float fat;
    public final float satFat;
    public final String barcode;

    public Product(final int id, String name, float energy, float carbs, float sugar, float protein, float fat, float satFat, String barcode) {
        this.id = id;
        this.name = name;
        this.energy = energy;
        this.carbs = carbs;
        this.sugar = sugar;
        this.protein = protein;
        this.fat = fat;
        this.satFat = satFat;
        this.barcode = barcode;
    }
}
