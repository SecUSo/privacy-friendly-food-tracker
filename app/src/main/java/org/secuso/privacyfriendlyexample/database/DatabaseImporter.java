package org.secuso.privacyfriendlyexample.database;

import org.json.JSONObject;

/**
 * @author Karola Marky
 * @version 20161225
 */

public class DatabaseImporter {

    private final String DEBUG_TAG = "DATABASE_IMPORTER";

    private PFASQLiteHelper database;
    JSONObject JSONDB;

    public DatabaseImporter(PFASQLiteHelper database, JSONObject JSONDB) {
        this.database = database;
        this.JSONDB = JSONDB;
    }

    public void importJSON() {

    }

}
