/*
 This file is part of Privacy Friendly App Example.

 Privacy Friendly App Example is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly App Example is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly App Example. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlyexample.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Karola Marky
 * @version 20161223
 * Structure based on http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 * accessed at 16th June 2016
 *
 * This class defines the structure of our database.
 */

public class PFASQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    /**
     * Use the following pattern for the name of the database
     * PF_[Name of the app]_DB
     */
    public static final String DATABASE_NAME = "PF_EXAMPLE_DB";

    //Names of table in the database
    private static final String TABLE_SAMPLEDATA = "SAMPLE_DATA";

    //Names of columns in the databases in this example we only use one table
    private static final String KEY_ID = "id";
    private static final String KEY_DOMAIN = "domain";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LENGTH = "length";

    public PFASQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /**
         * Create the table sample data on the first start
         * Be careful with the final line of the query and the SQL syntax that is used in the String.
         */
        String CREATE_SAMPLEDATA_TABLE = "CREATE TABLE " + TABLE_SAMPLEDATA +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_DOMAIN + " TEXT NOT NULL," +
                KEY_USERNAME + " TEXT NOT NULL," +
                KEY_LENGTH + " INTEGER);";

        sqLiteDatabase.execSQL(CREATE_SAMPLEDATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SAMPLEDATA);

        onCreate(sqLiteDatabase);
    }


    /**
     * Adds a single sampleData to our Table
     * As no ID is provided and KEY_ID is autoincremented (see line 50)
     * the last available key of the table is taken and incremented by 1
     * @param sampleData data that will be added
     */
    public void addSampleData(PFASampleDataType sampleData) {
        SQLiteDatabase database = this.getWritableDatabase();

        //To adjust this class for your own data, please add your values here.
        ContentValues values = new ContentValues();
        values.put(KEY_DOMAIN, sampleData.getDOMAIN());
        values.put(KEY_USERNAME, sampleData.getUSERNAME());
        values.put(KEY_LENGTH, sampleData.getLENGTH());

        database.insert(TABLE_SAMPLEDATA, null, values);
        database.close();
    }

    /**
     * Adds a single sampleData to our Table
     * This method can be used for re-insertion for example an undo-action
     * Therefore, the key of the sampleData will also be written into the database
     * @param sampleData data that will be added
     * Only use this for undo options and re-insertions
     */
    public void addSampleDataWithID(PFASampleDataType sampleData) {
        SQLiteDatabase database = this.getWritableDatabase();

        //To adjust this class for your own data, please add your values here.
        ContentValues values = new ContentValues();
        values.put(KEY_ID, sampleData.getID());
        values.put(KEY_DOMAIN, sampleData.getDOMAIN());
        values.put(KEY_USERNAME, sampleData.getUSERNAME());
        values.put(KEY_LENGTH, sampleData.getLENGTH());

        database.insert(TABLE_SAMPLEDATA, null, values);

        //always close the database after insertion
        database.close();
    }


    /**
     * This method gets a single sampleData entry based on its ID
     * @param id of the sampleData that is requested, could be get by the get-method
     * @return the sampleData that is requested.
     */
    public PFASampleDataType getSampleData(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        Log.d("DATABASE", Integer.toString(id));

        Cursor cursor = database.query(TABLE_SAMPLEDATA, new String[]{KEY_ID,
                        KEY_DOMAIN, KEY_USERNAME, KEY_LENGTH}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        PFASampleDataType sampleData = new PFASampleDataType();

        if( cursor != null && cursor.moveToFirst() ){
            sampleData.setID(Integer.parseInt(cursor.getString(0)));
            sampleData.setDOMAIN(cursor.getString(1));
            sampleData.setUSERNAME(cursor.getString(2));
            sampleData.setLENGTH(Integer.parseInt(cursor.getString(3)));

            Log.d("DATABASE", "Read " + cursor.getString(1) + " from DB");

            cursor.close();
        }

        return sampleData;

    }

    /**
     * This method returns all data from the DB as a list
     * This could be used for instance to fill a recyclerView
     * @return A list of all available sampleData in the Database
     */
    public List<PFASampleDataType> getAllSampleData() {
        List<PFASampleDataType> sampleDataList = new ArrayList<PFASampleDataType>();

        String selectQuery = "SELECT  * FROM " + TABLE_SAMPLEDATA;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        PFASampleDataType sampleData = null;

        if (cursor.moveToFirst()) {
            do {
                //To adjust this class for your own data, please add your values here.
                //be careful to use the right get-method to get the data from the cursor
                sampleData = new PFASampleDataType();
                sampleData.setID(Integer.parseInt(cursor.getString(0)));
                sampleData.setDOMAIN(cursor.getString(1));
                sampleData.setUSERNAME(cursor.getString(2));
                sampleData.setLENGTH(Integer.parseInt(cursor.getString(3)));

                sampleDataList.add(sampleData);
            } while (cursor.moveToNext());
        }

        return sampleDataList;
    }

    /**
     * Updates a database entry.
     * @param sampleData
     * @return actually makes the update
     */
    public int updateSampleData(PFASampleDataType sampleData) {
        SQLiteDatabase database = this.getWritableDatabase();

        //To adjust this class for your own data, please add your values here.
        ContentValues values = new ContentValues();
        values.put(KEY_DOMAIN, sampleData.getDOMAIN());
        values.put(KEY_USERNAME, sampleData.getUSERNAME());
        values.put(KEY_LENGTH, sampleData.getLENGTH());

        return database.update(TABLE_SAMPLEDATA, values, KEY_ID + " = ?",
                new String[] { String.valueOf(sampleData.getID()) });
    }

    /**
     * Deletes sampleData from the DB
     * This method takes the sampleData and extracts its key to build the delete-query
     * @param sampleData that will be deleted
     */
    public void deleteSampleData(PFASampleDataType sampleData) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_SAMPLEDATA, KEY_ID + " = ?",
                new String[] { Integer.toString(sampleData.getID()) });
        //always close the DB after deletion of single entries
        database.close();
    }

    /**
     * deletes all sampleData from the table.
     * This could be used in case of a reset of the app.
     */
    public void deleteAllSampleData() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from "+ TABLE_SAMPLEDATA);
    }

}
