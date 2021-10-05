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
import android.arch.persistence.room.Query;

import com.jjoe64.graphview.series.DataPoint;

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

    @Query("SELECT consumedEntries.date AS dateOfConsumption, (sum(product.energy*consumedEntries.amount)) AS energyConsumed, (sum(product.carbs*consumedEntries.amount)) AS carbsConsumed, (sum(product.sugar*consumedEntries.amount)) AS sugarConsumed, (sum(product.protein*consumedEntries.amount)) AS proteinConsumed, (sum(product.fat*consumedEntries.amount)) AS fatConsumed, (sum(product.satFat*consumedEntries.amount)) AS satFatConsumed, (sum(product.salt*consumedEntries.amount)) AS saltConsumed, (sum(product.fiber*consumedEntries.amount)) AS fiberConsumed, (sum(product.vitaminA_retinol*consumedEntries.amount)) AS vitaminA_retinolConsumed, (sum(product.betaCarotin*consumedEntries.amount)) AS betaCarotinConsumed, (sum(product.vitaminD*consumedEntries.amount)) AS vitaminDConsumed, (sum(product.vitaminE*consumedEntries.amount)) AS vitaminEConsumed, (sum(product.vitaminK*consumedEntries.amount)) AS vitaminKConsumed, (sum(product.thiamin_B1*consumedEntries.amount)) AS thiamin_B1Consumed, (sum(product.riboflavin_B2*consumedEntries.amount)) AS riboflavin_B2Consumed, (sum(product.niacin*consumedEntries.amount)) AS niacinConsumed, (sum(product.vitaminB6*consumedEntries.amount)) AS vitaminB6Consumed, (sum(product.folat*consumedEntries.amount)) AS folatConsumed, (sum(product.pantothenacid*consumedEntries.amount)) AS pantothenacidConsumed, (sum(product.biotin*consumedEntries.amount)) AS biotinConsumed, (sum(product.cobalamin_B12*consumedEntries.amount)) AS cobalamin_B12Consumed, (sum(product.vitaminC*consumedEntries.amount)) AS vitaminCConsumed, (sum(product.natrium*consumedEntries.amount)) AS natriumConsumed, (sum(product.chlorid*consumedEntries.amount)) AS chloridConsumed, (sum(product.kalium*consumedEntries.amount)) AS kaliumConsumed, (sum(product.calcium*consumedEntries.amount)) AS calciumConsumed, (sum(product.phosphor*consumedEntries.amount)) AS phosphorConsumed, (sum(product.magnesium*consumedEntries.amount)) AS magnesiumConsumed, (sum(product.eisen*consumedEntries.amount)) AS eisenConsumed, (sum(product.jod*consumedEntries.amount)) AS jodConsumed, (sum(product.fluorid*consumedEntries.amount)) AS fluoridConsumed, (sum(product.zink*consumedEntries.amount)) AS zinkConsumed, (sum(product.selen*consumedEntries.amount)) AS selenConsumed, (sum(product.kupfer*consumedEntries.amount)) AS kupferConsumed, (sum(product.mangan*consumedEntries.amount)) AS manganConsumed, (sum(product.chrom*consumedEntries.amount)) AS chromConsumed, (sum(product.molybdaen*consumedEntries.amount)) AS molybdaenConsumed FROM consumedEntries INNER JOIN product ON consumedEntries.productId = product.id  WHERE consumedEntries.date BETWEEN :dayst AND :dayet GROUP BY consumedEntries.date ")
    List<DateNutriments> getNutrimentsPerDayinPeriod(final Date dayst, final Date dayet);

    @Query("SELECT consumedEntries.date AS dateOfConsumption, (sum(product.energy*consumedEntries.amount/100)) AS energyConsumed, (sum(product.carbs*consumedEntries.amount/100)) AS carbsConsumed, (sum(product.sugar*consumedEntries.amount/100)) AS sugarConsumed, (sum(product.protein*consumedEntries.amount/100)) AS proteinConsumed, (sum(product.fat*consumedEntries.amount/100)) AS fatConsumed, (sum(product.satFat*consumedEntries.amount/100)) AS satFatConsumed, (sum(product.salt*consumedEntries.amount/100)) AS saltConsumed, (sum(product.fiber*consumedEntries.amount/100)) AS fiberConsumed, (sum(product.vitaminA_retinol*consumedEntries.amount/100)) AS vitaminA_retinolConsumed, (sum(product.betaCarotin*consumedEntries.amount/100)) AS betaCarotinConsumed, (sum(product.vitaminD*consumedEntries.amount/100)) AS vitaminDConsumed, (sum(product.vitaminE*consumedEntries.amount/100)) AS vitaminEConsumed, (sum(product.vitaminK*consumedEntries.amount/100)) AS vitaminKConsumed, (sum(product.thiamin_B1*consumedEntries.amount/100)) AS thiamin_B1Consumed, (sum(product.riboflavin_B2*consumedEntries.amount/100)) AS riboflavin_B2Consumed, (sum(product.niacin*consumedEntries.amount/100)) AS niacinConsumed, (sum(product.vitaminB6*consumedEntries.amount/100)) AS vitaminB6Consumed, (sum(product.folat*consumedEntries.amount/100)) AS folatConsumed, (sum(product.pantothenacid*consumedEntries.amount/100)) AS pantothenacidConsumed, (sum(product.biotin*consumedEntries.amount/100)) AS biotinConsumed, (sum(product.cobalamin_B12*consumedEntries.amount/100)) AS cobalamin_B12Consumed, (sum(product.vitaminC*consumedEntries.amount/100)) AS vitaminCConsumed, (sum(product.natrium*consumedEntries.amount/100)) AS natriumConsumed, (sum(product.chlorid*consumedEntries.amount/100)) AS chloridConsumed, (sum(product.kalium*consumedEntries.amount/100)) AS kaliumConsumed, (sum(product.calcium*consumedEntries.amount/100)) AS calciumConsumed, (sum(product.phosphor*consumedEntries.amount/100)) AS phosphorConsumed, (sum(product.magnesium*consumedEntries.amount/100)) AS magnesiumConsumed, (sum(product.eisen*consumedEntries.amount/100)) AS eisenConsumed, (sum(product.jod*consumedEntries.amount/100)) AS jodConsumed, (sum(product.fluorid*consumedEntries.amount/100)) AS fluoridConsumed, (sum(product.zink*consumedEntries.amount/100)) AS zinkConsumed, (sum(product.selen*consumedEntries.amount/100)) AS selenConsumed, (sum(product.kupfer*consumedEntries.amount/100)) AS kupferConsumed, (sum(product.mangan*consumedEntries.amount/100)) AS manganConsumed, (sum(product.chrom*consumedEntries.amount/100)) AS chromConsumed, (sum(product.molybdaen*consumedEntries.amount/100)) AS molybdaenConsumed FROM consumedEntries INNER JOIN product ON consumedEntries.productId = product.id  WHERE consumedEntries.date BETWEEN :dayst AND :dayet")
    List<DateNutriments> getNutrimentsPeriod(final Date dayst, final Date dayet);

  
    @Query("SELECT consumedEntries.amount AS amount, consumedEntries.id AS id,consumedEntries.name as name, product.energy as energy, product.carbs as carbs, product.sugar as sugar, product.protein as protein, product.fat as fat, product.satFat as satFat, product.salt AS salt, product.fiber AS fiber, product.vitaminA_retinol AS vitaminA_retinol, product.betaCarotin AS betaCarotin, product.vitaminD AS vitaminD, product.vitaminE AS vitaminE, product.vitaminK AS vitaminK, product.thiamin_B1 AS thiamin_B1, product.riboflavin_B2 AS riboflavin_B2, product.niacin AS niacin, product.vitaminB6 AS vitaminB6, product.folat AS folat, product.pantothenacid AS pantothenacid, product.biotin AS biotin, product.cobalamin_B12 AS cobalamin_B12, product.vitaminC AS vitaminC, product.natrium AS natrium, product.chlorid AS chlorid, product.kalium AS kalium, product.calcium AS calcium, product.phosphor AS phosphor, product.magnesium AS magnesium, product.eisen AS eisen, product.jod AS jod, product.fluorid AS fluorid, product.zink AS zink, product.selen AS selen, product.kupfer AS kupfer, product.mangan AS mangan, product.chrom AS chrom, product.molybdaen AS molybdaen FROM consumedEntries INNER JOIN product ON consumedEntries.productId = product.id WHERE consumedEntries.date=:day")
    LiveData<List<DatabaseEntry>> findConsumedEntriesForDate(final Date day);

    static class DateCalories
    {
        public Date unique1;
        public int unique2;
    }
    class DateNutriments{
        public Date dateOfConsumption;
        public float energyConsumed;
        public float carbsConsumed;
        public float sugarConsumed;
        public float proteinConsumed;
        public float fatConsumed;
        public float satFatConsumed;
        public float saltConsumed;
        public float fiberConsumed;

        //vitamins and minerals
        //soluable in fat vitamins
        public float vitaminA_retinolConsumed;
        public float betaCarotinConsumed;
        public float vitaminDConsumed;
        public float vitaminEConsumed;
        public float vitaminKConsumed;
        //soluable in water vitamins
        public float thiamin_B1Consumed;
        public float riboflavin_B2Consumed;
        public float niacinConsumed;
        public float vitaminB6Consumed;
        public float folatConsumed;
        public float pantothenacidConsumed;
        public float biotinConsumed;
        public float cobalamin_B12Consumed;
        public float vitaminCConsumed;
        //minerals
        public float natriumConsumed;
        public float chloridConsumed;
        public float kaliumConsumed;
        public float calciumConsumed;
        public float phosphorConsumed;
        public float magnesiumConsumed;
        //minerals (traces)
        public float eisenConsumed;
        public float jodConsumed;
        public float fluoridConsumed;
        public float zinkConsumed;
        public float selenConsumed;
        public float kupferConsumed;
        public float manganConsumed;
        public float chromConsumed;
        public float molybdaenConsumed;
    }
}
