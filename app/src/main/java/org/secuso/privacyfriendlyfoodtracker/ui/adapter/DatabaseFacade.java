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
import android.preference.PreferenceActivity;
import android.util.Log;

import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntrieAndProductDao;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntries;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntriesDao;
import org.secuso.privacyfriendlyfoodtracker.database.Product;
import org.secuso.privacyfriendlyfoodtracker.database.ProductDao;
import org.secuso.privacyfriendlyfoodtracker.ui.BaseAddFoodActivity;
import org.secuso.privacyfriendlyfoodtracker.ui.viewmodels.OverviewViewModel;

import java.util.ArrayList;
import java.util.Date;
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
     * @param referenceActivity
     */
    public void insertEntry(final int amount, final Date date, final String name, final float energy, final int productId, final BaseAddFoodActivity referenceActivity){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                int existingProductId = 0;
                //If the productId is 0 we need to create a new product in the database
                if (0 == productId) {
                    insertProductPrivate(name, energy, "");
                    // retrieve ProductId of newly created Product from database
                    List<Product> existingProducts = productDao.findExistingProducts(name, energy, "");
                    // There is only one existing product so we take the first one from the List
                    Product p = existingProducts.get(0);
                    existingProductId = p.id;
                } else {
                    existingProductId = productId;
                }
                consumedEntriesDao.insert(new ConsumedEntries(0, amount, new java.sql.Date(date.getTime()), name, existingProductId));
                referenceActivity.finish();
            }
        });
    }


    /**
     * Deletes a database entry by id.
     * @param id the id
     * @param date the date for refreshing the viewModel
     * @param viewModel a reference to the viewModel so it can be refreshed. This fixes a race
     *                  condition bug which resulted in deleted entrys still showing.
     *
     */
    public void deleteEntryById(final int id, final Date date, final OverviewViewModel viewModel){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<ConsumedEntries> res = consumedEntriesDao.findConsumedEntriesById(id);
                if (res.size() != 1) {
                    return;
                }
                consumedEntriesDao.delete(res.get(0));
                viewModel.init(date);
            }
        });
    }

    /**
     * Edit a database entry.
     * @param id the id
     * @param amount the new amount
     * @param date the date for refreshing the viewModel
     * @param viewModel a reference to the viewModel so it can be refreshed. This fixes a race
     *                  condition bug which resulted in changed entrys only showing changed on reload
     *                  of the overview.
     */
    public void editEntryById(final int id, final int amount, final Date date, final OverviewViewModel viewModel){
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
                viewModel.init(date);
            }
        });
    }

    /**
     * Crate a new Product
     * @param name the name
     * @param energy the energy
     * @param barcode the barcode
     */
    public void insertProduct(final String name, final float energy, final String barcode){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                insertProductPrivate(name, energy, barcode);
            }
        });
    }

    private void insertProductPrivate(String name, float energy, String barcode) {
        List<Product> res = productDao.findExistingProducts(name, energy, barcode);
        if (res.size() != 0) {
            return;
        }
        productDao.insert(new Product(0, name, energy, barcode));
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
                databaseEntries.add(new DatabaseEntry(String.valueOf(consumedEntry.id),consumedEntry.name, consumedEntry.amount, product.energy));
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
     * Returns a list of products containing the input string
     * @param name the search term
     * @return a List with products containing the search term
     */
    public List<Product> getProductByName(String name){
        return productDao.findProductsByName("%" + name + "%");
    }


}
