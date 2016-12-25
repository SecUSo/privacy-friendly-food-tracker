package org.secuso.privacyfriendlyexample.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Karola Marky
 * @version 20161225
 *          Structure based on http://tech.sarathdr.com/android-app/convert-database-cursor-result-to-json-array-android-app-development/
 *          accessed at 25th December 2016
 *          <p>
 *          This class turns a database into a JSON string
 */

public class DatabasePorter {

    private final String DEBUG_TAG = "DATABASE_PORTER";

    private String DB_PATH;
    private String DB_NAME;

    public DatabasePorter(String DB_PATH, String DB_NAME) {
        this.DB_PATH = DB_PATH;
        this.DB_NAME = DB_NAME;
    }


    /**
     * Turns a single DB table into a JSON string
     *
     * @return JSON string of the table
     */
    public JSONArray tableToJSON(String TABLE_NAME) {

        SQLiteDatabase dataBase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);


        String searchQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = dataBase.rawQuery(searchQuery, null);

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

        JSONObject finalJSON = new JSONObject();
        try {
            finalJSON.put(TABLE_NAME, resultSet);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d(DEBUG_TAG, finalJSON.toString());
        return resultSet;

    }

    /**
     * @return a list of all table names
     */
    public ArrayList<String> getTableNames() {

        SQLiteDatabase dataBase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        ArrayList<String> arrTblNames = new ArrayList<String>();
        Cursor c = dataBase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                arrTblNames.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }
        return arrTblNames;
    }

    public JSONObject dbToJSON() throws JSONException {
        ArrayList<String> tables = getTableNames();
        JSONObject listList = new JSONObject();

        for (int i = 0; i < tables.size(); i++) {
            listList.put(tables.get(i), tableToJSON(tables.get(i)));
        }

        JSONObject finalDBJSON = new JSONObject();
        finalDBJSON.put(DB_NAME, listList);

        Log.d(DEBUG_TAG, finalDBJSON.toString());

        return finalDBJSON;
    }


}
