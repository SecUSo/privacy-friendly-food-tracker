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

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntrieAndProductDao;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntries;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntriesDao;
import org.secuso.privacyfriendlyfoodtracker.database.Product;
import org.secuso.privacyfriendlyfoodtracker.database.ProductDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Database access functions.
 *
 * @author Simon Reinkemeier, Andre Lutz
 */
public class DatabaseFacade {
    ProductDao productDao;
    ConsumedEntriesDao consumedEntriesDao;
    ConsumedEntrieAndProductDao consumedEntrieAndProductDao;

    public DatabaseFacade(Context context) throws Exception {
        this.productDao = ApplicationDatabase.getInstance(context).getProductDao();
        this.consumedEntriesDao = ApplicationDatabase.getInstance(context).getConsumedEntriesDao();
        this.consumedEntrieAndProductDao = ApplicationDatabase.getInstance(context).getConsumedEntriesAndProductDao();
    }

    /**
     * Insert a new consumed entry.
     * @param amount the amount
     * @param date the consumption date in UNIX format
     * @param name the name
     * @param productId the consumed product id
     */
    public void insertEntry(final int amount, final java.util.Date date, final String name, final float energy, final float carbs, final float sugar, final float protein, final float fat, final float satFat, final int productId){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                int existingProductId = 0;
                //If the productId is 0 we need to create a new product in the database
                if (0 == productId) {
                    insertProductPrivate(name, energy, carbs, sugar, protein, fat, satFat, "");
                    // retrieve ProductId of newly created Product from database
                    List<Product> existingProducts = productDao.findExistingProducts(name, energy, carbs, sugar, protein, fat, satFat, "");
                    // There is only one existing product so we take the first one from the List
                    Product p = existingProducts.get(0);
                    existingProductId = p.id;
                } else {
                    existingProductId = productId;
                }
                consumedEntriesDao.insert(new ConsumedEntries(0, amount, new java.sql.Date(date.getTime()), name, existingProductId));
            }
        });
    }


    /**
     * Deletes a database entry by id.
     * @param id the id
     */
    public void deleteEntryById(final int id){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<ConsumedEntries> res = consumedEntriesDao.findConsumedEntriesById(id);
                if (res.size() != 1) {
                    return;
                }
                consumedEntriesDao.delete(res.get(0));
            }
        });
    }

    /**
     * Edit a database entry.
     * @param id the id
     * @param amount the new amount
     */
    public void editEntryById(final int id, final int amount){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<ConsumedEntries> res = consumedEntriesDao.findConsumedEntriesById(id);
                if (res.size() != 1) {
                    return;
                }
                ConsumedEntries consumedEntry = res.get(0);
                consumedEntry.amount = amount;
                consumedEntriesDao.update(res.get(0));
            }
        });
    }

    /**
     * Crate a new Product
     * @param name the name
     * @param energy the energy
     * @param barcode the barcode
     */
    public void insertProduct(final String name, final float energy, final float carbs, final float sugar, final float protein, final float fat, final float satFat, final String barcode){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                insertProductPrivate(name, energy, carbs,sugar, protein, fat, satFat, barcode);
            }
        });
    }

    private void insertProductPrivate(String name, float energy, float carbs, float sugar, float protein, float fat, float satFat, String barcode) {
        List<Product> res = productDao.findExistingProducts(name, energy, carbs, sugar, protein, fat, satFat, barcode);
        if (res.size() != 0) {
            return;
        }
        productDao.insert(new Product(0, name, energy, carbs, sugar, protein, fat, satFat, barcode));
    }

    /**
     * Find the most common products.
     * @return Returns a list with the most common products
     */
    public List<Product> findMostCommonProducts() {
        List<Product> products = new ArrayList<>();
        try {
            List<Integer> res = consumedEntriesDao.findMostCommonProducts();
            for (int i = 0; i < res.size(); i++) {
                products.add(productDao.findProductById(res.get(i)));
            }
        } catch (Exception e) {
            Log.e("DatabaseFacade", "Error o");
        }
        return products;
    }

    /**
     * Returns a database entry for a specified date.
     * @param date the date
     * @return DatabaseEntry
     */
    public List<DatabaseEntry> getEntriesForDay(java.util.Date date) {
        List<DatabaseEntry> databaseEntries = new ArrayList<>();
        try {
            List<ConsumedEntries> res = consumedEntriesDao.findConsumedEntriesForDate(new java.sql.Date(date.getTime()));
            for(ConsumedEntries consumedEntry : res) {
                Product product = productDao.findProductById(consumedEntry.productId);
                databaseEntries.add(new DatabaseEntry(String.valueOf(consumedEntry.id),consumedEntry.name, consumedEntry.amount, product.energy, product.carbs, product.sugar, product.protein, product.fat, product.satFat));
            }

        } catch (Exception e) {
            Log.e("DatabaseFacade", "Error o");
        }
        return databaseEntries;
    }

    /**
     * Returns the sum of calories per day for a time period.
     * @param startDate the start date
     * @param endDate the end date
     * @return the calories sum per day and the associated date
     */
    public List<ConsumedEntrieAndProductDao.DateCalories> getPeriodCalories(java.util.Date startDate, java.util.Date endDate){
        return    consumedEntrieAndProductDao.getCaloriesPeriod(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }

    /**
     * Returns the sum of calories between two dates.
     * @param startDate the start date
     * @param endDate the end date
     * @return the calories sum (list position 0)
     */
    public List<ConsumedEntrieAndProductDao.DateCalories> getCaloriesPerDayinPeriod(java.util.Date startDate, java.util.Date endDate){
        return consumedEntrieAndProductDao.getCaloriesPerDayinPeriod(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }

    /**
     * Returns the sum of carbs per day for a time period.
     * @param startDate the start date
     * @param endDate the end date
     * @return the carbs sum per day and the associated date
     */
    public List<ConsumedEntrieAndProductDao.DateCalories> getPeriodCarbs(java.util.Date startDate, java.util.Date endDate){
        return    consumedEntrieAndProductDao.getCarbsPeriod(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }

    /**
     * Returns the sum of carbs between two dates.
     * @param startDate the start date
     * @param endDate the end date
     * @return the carbs sum (list position 0)
     */
    public List<ConsumedEntrieAndProductDao.DateCalories> getCarbsPerDayinPeriod(java.util.Date startDate, java.util.Date endDate){
        return consumedEntrieAndProductDao.getCarbsPerDayinPeriod(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }

    /**
     * Returns the sum of sugar per day for a time period.
     * @param startDate the start date
     * @param endDate the end date
     * @return the sugar sum per day and the associated date
     */
    public List<ConsumedEntrieAndProductDao.DateCalories> getPeriodSugar(java.util.Date startDate, java.util.Date endDate){
        return    consumedEntrieAndProductDao.getSugarPeriod(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }

    /**
     * Returns the sum of sugar between two dates.
     * @param startDate the start date
     * @param endDate the end date
     * @return the sugar sum (list position 0)
     */
    public List<ConsumedEntrieAndProductDao.DateCalories> getSugarPerDayinPeriod(java.util.Date startDate, java.util.Date endDate){
        return consumedEntrieAndProductDao.getSugarPerDayinPeriod(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }

    /**
     * Returns the sum of protein per day for a time period.
     * @param startDate the start date
     * @param endDate the end date
     * @return the protein sum per day and the associated date
     */
    public List<ConsumedEntrieAndProductDao.DateCalories> getPeriodProtein(java.util.Date startDate, java.util.Date endDate){
        return    consumedEntrieAndProductDao.getProteinPeriod(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }

    /**
     * Returns the sum of protein between two dates.
     * @param startDate the start date
     * @param endDate the end date
     * @return the protein sum (list position 0)
     */
    public List<ConsumedEntrieAndProductDao.DateCalories> getProteinPerDayinPeriod(java.util.Date startDate, java.util.Date endDate){
        return consumedEntrieAndProductDao.getProteinPerDayinPeriod(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }

    /**
     * Returns the sum of fat per day for a time period.
     * @param startDate the start date
     * @param endDate the end date
     * @return the fat sum per day and the associated date
     */
    public List<ConsumedEntrieAndProductDao.DateCalories> getPeriodFat(java.util.Date startDate, java.util.Date endDate){
        return    consumedEntrieAndProductDao.getFatPeriod(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }

    /**
     * Returns the sum of fat between two dates.
     * @param startDate the start date
     * @param endDate the end date
     * @return the fat sum (list position 0)
     */
    public List<ConsumedEntrieAndProductDao.DateCalories> getFatPerDayinPeriod(java.util.Date startDate, java.util.Date endDate){
        return consumedEntrieAndProductDao.getFatPerDayinPeriod(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }

    /**
     * Returns the sum of saturated fat per day for a time period.
     * @param startDate the start date
     * @param endDate the end date
     * @return the fat sum per day and the associated date
     */
    public List<ConsumedEntrieAndProductDao.DateCalories> getPeriodSatFat(java.util.Date startDate, java.util.Date endDate){
        return    consumedEntrieAndProductDao.getSatFatPeriod(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }

    /**
     * Returns the sum of saturated fat between two dates.
     * @param startDate the start date
     * @param endDate the end date
     * @return the fat sum (list position 0)
     */
    public List<ConsumedEntrieAndProductDao.DateCalories> getSatFatPerDayinPeriod(java.util.Date startDate, java.util.Date endDate){
        return consumedEntrieAndProductDao.getSatFatPerDayinPeriod(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }

    /**
     * Returns a list of products containing the input string
     * @param name the search term
     * @return a List with products containing the search term
     */
    public List<Product> getProductByName(String name){
        return productDao.findProductsByName("%" + name + "%");
    }


}
