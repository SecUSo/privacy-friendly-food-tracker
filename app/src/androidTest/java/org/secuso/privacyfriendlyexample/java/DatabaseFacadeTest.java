package org.secuso.privacyfriendlyexample.java;

import android.arch.persistence.room.Database;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.secuso.privacyfriendlyfoodtracker.activities.adapter.DatabaseFacade;
import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase;
import org.secuso.privacyfriendlyfoodtracker.helpers.KeyGenHelper;


import java.util.Date;

import static org.junit.Assert.assertTrue;

public class DatabaseFacadeTest {
    DatabaseFacade databaseFacade;

    @Before
    public void createDatabaseFacade() {
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
            this.databaseFacade = new DatabaseFacade(appContext);
        } catch (Exception e) {
            Log.e("Error:", "database are not created");
        }
    }

    @Test
    public void insertEntryTest(){
        assertTrue("Insert should return true", databaseFacade.insertEntry(2, new Date(),"name",1  ));
    }

    @Test
    public void deleteEntryByIdTest(){
        databaseFacade.insertEntry(2, new Date(),"name",1  );
        assertTrue("Delete should return true", databaseFacade.deleteEntryById(0 ));


    }

    @Test
    public void editEntryByIdTest(){

    }



    @Test
    public void insertProductTest(){
        assertTrue("Insert should return true", databaseFacade.insertProduct("", 2, "barcode"  ));
    }

    @Test
    public void findMostCommonProductsTest() {

    }


    @Test
    public void getEntriesForDayTest() {

    }


}
