/*
This file is part of Privacy friendly food tracker.

Privacy friendly food tracker is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Privacy friendly food tracker is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Privacy friendly food tracker.  If not, see <https://www.gnu.org/licenses/>.
*/
package org.secuso.privacyfriendlyfoodtracker.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SupportFactory;

import org.secuso.privacyfriendlyfoodtracker.database.converter.DateConverter;
import org.secuso.privacyfriendlyfoodtracker.helpers.KeyGenHelper;

/**
 * Database singleton.
 *
 * @author Andre Lutz
 */
@Database(entities = {ConsumedEntries.class, Product.class}, version = 1, exportSchema = true)
@TypeConverters({DateConverter.class})
public abstract class ApplicationDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "consumed_entries_database";

    public abstract ConsumedEntriesDao getConsumedEntriesDao();

    public abstract ProductDao getProductDao();

    public abstract ConsumedEntrieAndProductDao getConsumedEntriesAndProductDao();

    private static ApplicationDatabase sInstance;

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static ApplicationDatabase getInstance(final Context context) throws Exception {
        if (sInstance == null) {
            synchronized (ApplicationDatabase.class) {
                if (sInstance == null) {
                    SupportFactory factory = new SupportFactory(SQLiteDatabase.getBytes(KeyGenHelper.getSecretKeyAsChar(context)), new SQLiteDatabaseHook() {
                        @Override
                        public void preKey(SQLiteDatabase database) {
                        }

                        @Override
                        public void postKey(SQLiteDatabase database) {
                            database.rawExecSQL("PRAGMA cipher_compatibility = 3;");
                        }
                    });

                    sInstance = Room.databaseBuilder(context.getApplicationContext(), ApplicationDatabase.class, DATABASE_NAME)
                            .openHelperFactory(factory)
                            .allowMainThreadQueries()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    try {
                                        ApplicationDatabase database = ApplicationDatabase.getInstance(context);
                                        // notify that the database was created and it's ready to be used
                                        database.setDatabaseCreated();
                                    } catch (Exception e) {
                                        Log.e("ApplicationDatabase", e.getMessage());
                                    }
                                }
                            }).build();

                }
            }
        }
        return sInstance;
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }

    /**
     * Indicates if the database is created.
     *
     * @return if database is created
     */
    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
