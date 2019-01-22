package org.secuso.privacyfriendlyfoodtracker.activities.adapter;

import android.content.Context;
import android.util.Log;

import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntrieAndProductDao;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntries;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntriesDao;
import org.secuso.privacyfriendlyfoodtracker.database.Product;
import org.secuso.privacyfriendlyfoodtracker.database.ProductDao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DatabaseFacade {
    ProductDao productDao;
    ConsumedEntriesDao consumedEntriesDao;
    ConsumedEntrieAndProductDao consumedEntrieAndProductDao;
    public DatabaseFacade(Context context) throws Exception{
        this.productDao = ApplicationDatabase.getInstance(context).getProductDao();
        this.consumedEntriesDao = ApplicationDatabase.getInstance(context).getConsumedEntriesDao();
        this.consumedEntrieAndProductDao = ApplicationDatabase.getInstance(context).getConsumedEntrieAndProductDao();
    }

    /**
     * Insert a new consumed entry.
     * @param amount the amount
     * @param date the consumption date in UNIX format
     * @param name the name
     * @param productId the consumed product id
     * @return true if no error occurs
     */
    public boolean insertEntry(int amount, java.util.Date date, String name, int energy, int productId){
        int existingProductId = 0;
        //If the productId is 0 we need to create a new product in the database
        if (0 == productId) {
            insertProduct(name, energy, "");
            // retrieve ProductId of newly created Product from database
            List<Product> existingProducts = productDao.findExistingProducts(name, energy, "");
            // There is only one existing product so we take the first one from the List
            Product p = existingProducts.get(0);
            existingProductId = p.id;
        } else {
            existingProductId = productId;
        }
        try {
            consumedEntriesDao.insert(new ConsumedEntries(0, amount, new java.sql.Date(date.getTime()), name, existingProductId));
            return true;
        } catch (Exception e){
            return false;
        }
    }


    /**
     * Deletes a database entry by id.
     * @param id the id
     * @return successfully or not
     */
    public boolean deleteEntryById(int id ){
        try {
            List<ConsumedEntries> res = consumedEntriesDao.findConsumedEntriesById(id);
            if(res.size() != 1){return false;}
            consumedEntriesDao.delete(res.get(0));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * Edit a database entry.
     * @param id the id
     * @param amount the new amount
     * @return successfully or not
     */
    public boolean editEntryById(int id, int amount){
        try {
            List<ConsumedEntries> res = consumedEntriesDao.findConsumedEntriesById(id);
            if(res.size() != 1){return false;}
            ConsumedEntries consumedEntry = res.get(0);
            consumedEntry.amount = amount;
            consumedEntriesDao.update(res.get(0));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * Crate a new Product
     * @param name the name
     * @param energy the energy
     * @param barcode the barcode
     * @return successfully or not
     */
    public boolean insertProduct( String name, int energy,  String barcode){
        try{
            List<Product> res = productDao.findExistingProducts(name, energy, barcode);
            if(res.size() != 0){
                return false;
            }
            productDao.insert(new Product(0,name, energy, barcode));
            return true;
        }catch (Exception e){
            return false;
        }
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
    public DatabaseEntry[] getEntriesForDay(java.util.Date date) {
        List<DatabaseEntry> databaseEntries = new ArrayList<>();
        try {
            List<ConsumedEntries> res = consumedEntriesDao.findConsumedEntriesForDate(new java.sql.Date(date.getTime()));
            for (int i = 0; i < res.size(); i++) {
                ConsumedEntries consumedEntry = res.get(i);
                Product product = productDao.findProductById(consumedEntry.productId);
                databaseEntries.add(new DatabaseEntry( String.valueOf(consumedEntry.id),consumedEntry.name, consumedEntry.amount, product.energy));
            }

        } catch (Exception e) {
            Log.e("DatabaseFacade", "Error o");
        }
        return databaseEntries.toArray(new DatabaseEntry[databaseEntries.size()]);
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
        return    consumedEntrieAndProductDao.getCaloriesPerDayinPeriod(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }

    public List<Product> getProductByName(String name){
        return productDao.findProductsByName("%" + name + "%");
    }

}
