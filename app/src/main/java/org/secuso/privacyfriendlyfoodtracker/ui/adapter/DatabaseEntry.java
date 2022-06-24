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
package org.secuso.privacyfriendlyfoodtracker.ui.adapter;

/**
 * Models a consumed entry.
 * @author Simon Reinkemeier
 */
public class DatabaseEntry {
    public float energy;
    public float carbs;
    public float sugar;
    public float protein;
    public float fat;
    public float satFat;
    public float salt;
    public float fiber;

    //vitamins and minerals
    //soluable in fat vitamins
    public float vitaminA_retinol;
    public float betaCarotin;
    public float vitaminD;
    public float vitaminE;
    public float vitaminK;
    //soluable in water vitamins
    public float thiamin_B1;
    public float riboflavin_B2;
    public float niacin;
    public float vitaminB6;
    public float folat;
    public float pantothenacid;
    public float biotin;
    public float cobalamin_B12;
    public float vitaminC;
    //minerals
    public float natrium;
    public float chlorid;
    public float kalium;
    public float calcium;
    public float phosphor;
    public float magnesium;
    //minerals (traces)
    public float eisen;
    public float jod;
    public float fluorid;
    public float zink;
    public float selen;
    public float kupfer;
    public float mangan;
    public float chrom;
    public float molybdaen;
    public int amount;
    public String name;
    public int id;
    public DatabaseEntry(int id, String name, int amount, float energy, float carbs, float sugar, float protein, float fat, float satFat,
                         float salt, float fiber, float vitaminA_retinol, float betaCarotin, float vitaminD, float vitaminE, float vitaminK, float thiamin_B1, float riboflavin_B2, float niacin, float vitaminB6, float folat, float pantothenacid, float biotin, float cobalamin_B12, float vitaminC, float natrium, float chlorid, float kalium, float calcium, float phosphor, float magnesium, float eisen, float jod, float fluorid, float zink, float selen, float kupfer, float mangan, float chrom, float molybdaen){
        this.id = id;
        this.amount = amount;
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

        this.name = name;
    }
}