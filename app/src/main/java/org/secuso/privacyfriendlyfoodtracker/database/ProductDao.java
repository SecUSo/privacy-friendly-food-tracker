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

import java.sql.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Includes methods that offer abstract access to the app database to manage products.
 *
 * @author Andre Lutz
 */
@Dao
public interface ProductDao {
    @Insert
    void insert(Product... product);

    @Update
    void update(Product... product);

    @Delete
    void delete(Product... product);

    @Query("DELETE FROM product")
    void deleteAll();

    @Query("SELECT * FROM product")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM product WHERE id=:id")
    Product findProductById(final int id);

    @Query("SELECT * FROM product WHERE name=:name AND energy=:energy AND carbs=:carbs AND sugar=:sugar AND protein=:protein AND fat=:fat AND satFat=:satFat AND salt=:salt AND vitaminA_retinol=:vitaminA_retinol AND betaCarotin=:betaCarotin AND vitaminD=:vitaminD AND vitaminE=:vitaminE AND vitaminK=:vitaminK AND thiamin_B1=:thiamin_B1 AND riboflavin_B2=:riboflavin_B2 AND niacin=:niacin AND vitaminB6=:vitaminB6 AND folat=:folat AND pantothenacid=:pantothenacid AND biotin=:biotin AND cobalamin_B12=:cobalamin_B12 AND vitaminC=:vitaminC AND natrium=:natrium AND chlorid=:chlorid AND kalium=:kalium AND calcium=:calcium AND phosphor=:phosphor AND magnesium=:magnesium AND eisen=:eisen AND jod=:jod AND fluorid=:fluorid AND zink=:zink AND selen=:selen AND kupfer=:kupfer AND mangan=:mangan AND chrom=:chrom AND molybdaen=:molybdaen  AND barcode=:barcode")
    List<Product> findExistingProducts(String name, float energy, float carbs, float sugar, float protein, float fat, float satFat, float salt, float vitaminA_retinol, float betaCarotin, float vitaminD, float vitaminE, float vitaminK, float thiamin_B1, float riboflavin_B2, float niacin, float vitaminB6, float folat, float pantothenacid, float biotin, float cobalamin_B12, float vitaminC, float natrium, float chlorid, float kalium, float calcium, float phosphor, float magnesium, float eisen, float jod, float fluorid, float zink, float selen, float kupfer, float mangan, float chrom, float molybdaen, String barcode);

    @Query("SELECT * FROM product WHERE name LIKE :name")
    List<Product> findProductsByName(String name);
}
