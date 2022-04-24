package org.secuso.privacyfriendlyfoodtracker.database.importexport;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntries;
import org.secuso.privacyfriendlyfoodtracker.database.Product;

import java.lang.reflect.Type;
import java.util.List;

public class DatabaseExporter {

    private Context context;

    public DatabaseExporter(Context context) {
        this.context = context;
    }

    public int getNumberOfProducts() {
        try {
            return ApplicationDatabase.getInstance(context).getProductDao().getAllProducts().getValue().size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getNumberOfConsumedEntries() {
        try {
            return ApplicationDatabase.getInstance(context).getConsumedEntriesDao().getAllConsumedEntries().size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String exportDatabase() {
        try {
            List<Product> products = ApplicationDatabase.getInstance(context).getProductDao().getAllProducts().getValue();
            List<ConsumedEntries> consumedEntries = ApplicationDatabase.getInstance(context).getConsumedEntriesDao().getAllConsumedEntries();
            ImpExModel model = new ImpExModel();
            model.setProductList(products);
            model.setConsumedEntriesList(consumedEntries);

            //convert to json string
            Type listType = new TypeToken<ImpExModel>() {
            }.getType();
            Gson gson = new Gson();
            String json = gson.toJson(model, listType);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String exportProductDatabase() {
        try {
            List<Product> products = ApplicationDatabase.getInstance(context).getProductDao().getAllProducts().getValue();
            ImpExModel model = new ImpExModel();
            model.setProductList(products);
            model.setConsumedEntriesList(null);

            //convert to json string
            Type listType = new TypeToken<ImpExModel>() {
            }.getType();
            Gson gson = new Gson();
            String json = gson.toJson(model, listType);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}