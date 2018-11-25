package org.secuso.privacyfriendlyexample.java;

import android.content.Context;
import android.icu.util.Calendar;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntries;
import org.secuso.privacyfriendlyfoodtracker.database.Product;
import org.secuso.privacyfriendlyfoodtracker.helpers.KeyGenHelper;

import java.io.IOException;
import java.security.Key;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ApplicationDatabaseTest {

    ApplicationDatabase applicationDatabase = null;

    @Before
    public void createDb() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        if (!KeyGenHelper.isKeyGenerated()) {
            try {
                KeyGenHelper.generateKey(appContext);
                KeyGenHelper.generatePassphrase(appContext);
            } catch (Exception e) {
                Log.e("GenerateKeyActivity", e.getMessage());
            }
        }

        try {
            this.applicationDatabase = ApplicationDatabase.getInstance(appContext);
        } catch (Exception e) {
            Log.e("Error:", "database are not created");
        }
    }

    @After
    public void closeDb() throws IOException {
      //  if (applicationDatabase != null) applicationDatabase.close();
    }

    @Test
    public void addProductsToDatabase() throws Exception {
        applicationDatabase.getProductDao().deleteAll();
        applicationDatabase.getProductDao().insert(new Product(0, "Test1", 2, "Barcode"));
        applicationDatabase.getProductDao().insert(new Product(0, "Test1", 2, "Barcode"));
        applicationDatabase.getProductDao().insert(new Product(0, "Test1", 2, "Barcode"));
        applicationDatabase.getProductDao().insert(new Product(0, "Test1", 2, "Barcode"));
        int size = applicationDatabase.getProductDao().getAllProducts().size();
        assertTrue("Database should contains 4 elements", size == 4);
    }

    @Test
    public void addConsumedEntriesToDatabase() throws Exception {
        applicationDatabase.getConsumedEntriesDao().deleteAll();
        applicationDatabase.getProductDao().deleteAll();
        applicationDatabase.getProductDao().insert(new Product(1, "Test1", 2, "Barcode"));
        applicationDatabase.getProductDao().insert(new Product(2, "Test1", 2, "Barcode"));
        applicationDatabase.getProductDao().insert(new Product(3, "Test1", 2, "Barcode"));
        applicationDatabase.getProductDao().insert(new Product(4, "Test1", 2, "Barcode"));
        applicationDatabase.getConsumedEntriesDao().insert(new ConsumedEntries(0, 2, new java.sql.Date(Calendar.getInstance().getTime().getTime()), "test", 1));
        applicationDatabase.getConsumedEntriesDao().insert(new ConsumedEntries(0, 2, new java.sql.Date(Calendar.getInstance().getTime().getTime()), "test", 1));
        applicationDatabase.getConsumedEntriesDao().insert(new ConsumedEntries(0, 2, new java.sql.Date(Calendar.getInstance().getTime().getTime()), "test", 2));
        applicationDatabase.getConsumedEntriesDao().insert(new ConsumedEntries(0, 2, new java.sql.Date(Calendar.getInstance().getTime().getTime()), "test", 3));
        int size = applicationDatabase.getConsumedEntriesDao().getAllConsumedEntries().size();
        assertTrue("Database should contains 4 elements", size == 4);
    }

    @Test
    public void findMostCommonProducts() throws Exception {
        applicationDatabase.getConsumedEntriesDao().deleteAll();
        applicationDatabase.getProductDao().deleteAll();
        applicationDatabase.getProductDao().insert(new Product(1, "Test1", 2, "Barcode"));
        applicationDatabase.getProductDao().insert(new Product(2, "Test1", 2, "Barcode"));
        applicationDatabase.getProductDao().insert(new Product(3, "Test1", 2, "Barcode"));
        applicationDatabase.getProductDao().insert(new Product(4, "Test1", 2, "Barcode"));

        applicationDatabase.getConsumedEntriesDao().insert(new ConsumedEntries(0, 2, new java.sql.Date(Calendar.getInstance().getTime().getTime()), "test", 1));
        applicationDatabase.getConsumedEntriesDao().insert(new ConsumedEntries(0, 2, new java.sql.Date(Calendar.getInstance().getTime().getTime()), "test", 1));
        applicationDatabase.getConsumedEntriesDao().insert(new ConsumedEntries(0, 2, new java.sql.Date(Calendar.getInstance().getTime().getTime()), "test", 1));
        applicationDatabase.getConsumedEntriesDao().insert(new ConsumedEntries(0, 2, new java.sql.Date(Calendar.getInstance().getTime().getTime()), "test", 2));
        applicationDatabase.getConsumedEntriesDao().insert(new ConsumedEntries(0, 2, new java.sql.Date(Calendar.getInstance().getTime().getTime()), "test", 3));
        applicationDatabase.getConsumedEntriesDao().insert(new ConsumedEntries(0, 2, new java.sql.Date(Calendar.getInstance().getTime().getTime()), "test", 3));
        applicationDatabase.getConsumedEntriesDao().insert(new ConsumedEntries(0, 2, new java.sql.Date(Calendar.getInstance().getTime().getTime()), "test", 4));
        List<Integer> list = applicationDatabase.getConsumedEntriesDao().findMostCommonProducts();
        assertTrue("The first productid should be 1", list.get(0) == 1);
        assertTrue("The second productid should be 3", list.get(1) == 3);

    }
}



