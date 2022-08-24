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
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

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

    @Query("SELECT * FROM product WHERE name=:name AND energy=:energy AND barcode=:barcode")
    List<Product> findExistingProducts(String name, float energy, String barcode);

    @Query("SELECT * FROM product WHERE name LIKE :name")
    List<Product> findProductsByName(String name);
}
