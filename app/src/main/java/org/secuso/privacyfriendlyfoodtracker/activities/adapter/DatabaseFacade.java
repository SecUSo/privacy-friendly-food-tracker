package org.secuso.privacyfriendlyfoodtracker.activities.adapter;

import android.content.Context;
import android.util.Log;

import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase;
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
    public DatabaseFacade(Context context) throws Exception{
        this.productDao = ApplicationDatabase.getInstance(context).getProductDao();
        this.consumedEntriesDao = ApplicationDatabase.getInstance(context).getConsumedEntriesDao();
    }

    /**
     * Insert a new consumed entry.
     * @param amount the amount
     * @param date the consuming date
     * @param name the name
     * @param productId the consumed product id
     * @return true if no error occurs
     */
    public boolean insertEntry( int amount,  java.util.Date date,  String name,  int productId){
        try {
            List<ConsumedEntries> res = consumedEntriesDao.findExistingConsumedEntries(productId, amount,new java.sql.Date(date.getTime()),name);
            if(res.size() != 1){return false;}
            consumedEntriesDao.insert(new ConsumedEntries(0,amount,new java.sql.Date(date.getTime()),name,productId));
            return true;
        } catch (Exception e){
            return false;
        }
    }

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

    public boolean editEntryById(int id, int amount ){
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



    public boolean insertProduct( String name, int energy,  String barcode){
        try{
            List<Product> res = productDao.findExistingProducts(name, energy,barcode);
            if(res.size() != 1){return false;}
            productDao.insert(new Product(0,name, energy, barcode));
            return true;
        }catch (Exception e){
            return false;}
    }

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


}
