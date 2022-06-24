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

    //nutriments
    public final float carbs;
    public final float sugar;
    public final float protein;
    public final float fat;
    public final float satFat;
    public final float salt;
    public final float fiber;


    //vitamins and minerals
    //soluable in fat vitamins
    public final float vitaminA_retinol;
    public final float betaCarotin;
    public final float vitaminD;
    public final float vitaminE;
    public final float vitaminK;
    //soluable in water vitamins
    public final float thiamin_B1;
    public final float riboflavin_B2;
    public final float niacin;
    public final float vitaminB6;
    public final float folat;
    public final float pantothenacid;
    public final float biotin;
    public final float cobalamin_B12;
    public final float vitaminC;
    //minerals
    public final float natrium;
    public final float chlorid;
    public final float kalium;
    public final float calcium;
    public final float phosphor;
    public final float magnesium;
    //minerals (traces)
    public final float eisen;
    public final float jod;
    public final float fluorid;
    public final float zink;
    public final float selen;
    public final float kupfer;
    public final float mangan;
    public final float chrom;
    public final float molybdaen;

    public final String barcode;

    public Product(final int id, String name, float energy, float carbs, float sugar, float protein, float fat, float satFat,float salt, float fiber, float vitaminA_retinol, float betaCarotin, float vitaminD, float vitaminE, float vitaminK, float thiamin_B1, float riboflavin_B2, float niacin, float vitaminB6, float folat, float pantothenacid, float biotin, float cobalamin_B12, float vitaminC, float natrium, float chlorid, float kalium, float calcium, float phosphor, float magnesium, float eisen, float jod, float fluorid, float zink, float selen, float kupfer, float mangan, float chrom, float molybdaen, String barcode) {
        this.id = id;
        this.name = name;
        this.energy = energy;
        this.carbs = carbs;
        this.sugar = sugar;
        this.protein = protein;
        this.fat = fat;
        this.satFat = satFat;
        this.salt=salt;
        this.fiber = fiber;


        this.vitaminA_retinol=vitaminA_retinol;
        this.betaCarotin=betaCarotin;
        this.vitaminD=vitaminD;
        this.vitaminE=vitaminE;
        this.vitaminK=vitaminK;
        this.thiamin_B1=thiamin_B1;
        this.riboflavin_B2=riboflavin_B2;
        this.niacin=niacin;
        this.vitaminB6=vitaminB6;
        this.folat=folat;
        this.pantothenacid=pantothenacid;
        this.biotin=biotin;
        this.cobalamin_B12=cobalamin_B12;
        this.vitaminC=vitaminC;
        this.natrium=natrium;
        this.chlorid=chlorid;
        this.kalium=kalium;
        this.calcium=calcium;
        this.phosphor=phosphor;
        this.magnesium=magnesium;
        this.eisen=eisen;
        this.jod=jod;
        this.fluorid=fluorid;
        this.zink=zink;
        this.selen=selen;
        this.kupfer=kupfer;
        this.mangan=mangan;
        this.chrom=chrom;
        this.molybdaen=molybdaen;

        this.barcode = barcode;
    }
}
