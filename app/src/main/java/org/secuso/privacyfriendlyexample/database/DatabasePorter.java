package org.secuso.privacyfriendlyexample.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Karola Marky
 * @version 20161225
 * Structure based on http://tech.sarathdr.com/android-app/convert-database-cursor-result-to-json-array-android-app-development/
 * accessed at 25th December 2016
 *
 * This class turns a database into a JSON string
 */

public class DatabasePorter {

    private final String DEBUG_TAG = "DATABASE_PORTER";

    private String DB_PATH;
    private String TABLE_NAME;


    public DatabasePorter(String DB_PATH, String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
        this.DB_PATH = DB_PATH;
    }


    public JSONArray getResults() {

        String myPath = DB_PATH;


        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        String searchQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = myDataBase.rawQuery(searchQuery, null);

        JSONArray resultSet = new JSONArray();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {

                    try {

                        if (cursor.getString(i) != null) {
                            //Log.d(DEBUG_TAG, cursor.getString(i));
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.d(DEBUG_TAG, e.getMessage());
                    }
                }

            }

            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        Log.d(DEBUG_TAG, resultSet.toString());
        return resultSet;

    }

}
