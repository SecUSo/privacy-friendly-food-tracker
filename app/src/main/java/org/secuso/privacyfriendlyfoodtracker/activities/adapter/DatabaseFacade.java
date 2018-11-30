package org.secuso.privacyfriendlyfoodtracker.activities.adapter;

import android.content.Context;

import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntries;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntriesDao;
import org.secuso.privacyfriendlyfoodtracker.database.Product;
import org.secuso.privacyfriendlyfoodtracker.database.ProductDao;

import java.sql.Date;
import java.util.List;

public class DatabaseFacade {
    ProductDao productDao;
    ConsumedEntriesDao consumedEntriesDao;
    public DatabaseFacade(Context context) throws Exception{
        this.productDao = ApplicationDatabase.getInstance(context).getProductDao();
        this.consumedEntriesDao = ApplicationDatabase.getInstance(context).getConsumedEntriesDao();
    }

    boolean insertEntry( int amount,  Date date,  String name,  int productId){
        try {
            List<ConsumedEntries> res = consumedEntriesDao.findExistingConsumedEntries(productId, amount,date,name);
            if(res.size() != 1){return false;}
            consumedEntriesDao.insert(new ConsumedEntries(0,amount,date,name,productId));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    boolean deleteEntryById(int id ){
        try {
            List<ConsumedEntries> res = consumedEntriesDao.findConsumedEntriesById(id);
            if(res.size() != 1){return false;}
            consumedEntriesDao.delete(res.get(0));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    boolean editEntryById(int id, int amount ){
        try {
            List<ConsumedEntries> res = consumedEntriesDao.findConsumedEntriesById(id);
            if(res.size() != 1){return false;}
            ConsumedEntries consumedEntrie = res.get(0);
            consumedEntrie.amount = amount;
            consumedEntriesDao.update(res.get(0));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    boolean insertProduct( String name, int energy,  String barcode){
        try{
            List<ConsumedEntries> res = productDao.findExistingProducts(name, energy,barcode);
            if(res.size() != 1){return false;}
            productDao.insert(new Product(0,name, energy, barcode));
            return true;
        }catch (Exception e){
            return false;}
    }

}
