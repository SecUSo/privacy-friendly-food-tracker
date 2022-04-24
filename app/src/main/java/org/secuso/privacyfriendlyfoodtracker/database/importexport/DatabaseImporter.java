package org.secuso.privacyfriendlyfoodtracker.database.importexport;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntries;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntriesDao;
import org.secuso.privacyfriendlyfoodtracker.database.Product;
import org.secuso.privacyfriendlyfoodtracker.database.ProductDao;

import java.lang.reflect.Type;

public class DatabaseImporter {
    private Context context;
    ApplicationDatabase database;

    public DatabaseImporter(Context context) {
        this.context = context;
        try {
            this.database = ApplicationDatabase.getInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importDatabase(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ImpExModel>() {
        }.getType();
        int products = 0;
        int entries = 0;
        try {
            ImpExModel impExModel = gson.fromJson(json, listType);
            ProductDao productDao = database.getProductDao();
            ConsumedEntriesDao consumedEntriesDao = database.getConsumedEntriesDao();
            if (null != impExModel.getProductList()) {
                for (Product product : impExModel.getProductList()) {
                    Product p = productDao.findProductById(product.id);
                    if (null == p) {
                        productDao.insert(product);
                    } else {
                        productDao.update(product);
                    }
                }
                products = impExModel.getProductList().size();
            }
            if (null != impExModel.getConsumedEntriesList()) {
                for (ConsumedEntries entity : impExModel.getConsumedEntriesList()) {
                    ConsumedEntries ent = consumedEntriesDao.findConsumedEntriesById(entity.id);
                    if (null == ent) {
                        consumedEntriesDao.insert(entity);
                    } else {
                        consumedEntriesDao.update(entity);
                    }
                }
                entries = impExModel.getConsumedEntriesList().size();
            }
        } catch (Exception e) {
            Log.e(DatabaseImporter.class.toString(), e.getMessage());
            Toast.makeText(context, "Failed to import database: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        Toast.makeText(context, "DB Import successful for " + products + " products and " + entries + " entries.", Toast.LENGTH_LONG).show();
    }

}