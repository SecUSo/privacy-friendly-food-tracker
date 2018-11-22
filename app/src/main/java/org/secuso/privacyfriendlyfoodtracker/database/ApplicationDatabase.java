package org.secuso.privacyfriendlyfoodtracker.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.commonsware.cwac.saferoom.SafeHelperFactory;

import org.secuso.privacyfriendlyfoodtracker.Converter.DateConverter;
import org.secuso.privacyfriendlyfoodtracker.helpers.KeyGenHelper;

import static com.commonsware.cwac.saferoom.SQLCipherUtils.encrypt;
import static com.commonsware.cwac.saferoom.SQLCipherUtils.getDatabaseState;

@Database(entities = {ConsumedEntries.class, Product.class},
        version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class ApplicationDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "consumed_entries_database";

    public abstract ConsumedEntriesDao getConsumedEntriesDao();

    public abstract ProductDao getProductDao();

    private static ApplicationDatabase sInstance;

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static ApplicationDatabase getInstance(final Context context) throws Exception {
        if (sInstance == null) {
            synchronized (ApplicationDatabase.class) {
                if (sInstance == null) {
                    SafeHelperFactory factory= new SafeHelperFactory(KeyGenHelper.getSecretKeyAsChar(context));

                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            ApplicationDatabase.class, DATABASE_NAME).openHelperFactory(factory).addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            try {
                                ApplicationDatabase database = ApplicationDatabase.getInstance(context);
                                // notify that the database was created and it's ready to be used
                               /* if( getDatabaseState(context,DATABASE_NAME) == SQLCipherUtils.State.UNENCRYPTED){
                                    encrypt(context, DATABASE_NAME, KeyGenHelper.getSecretKeyAsChar(context));
                                }*/
                                database.setDatabaseCreated();
                            }catch (Exception e){
                                // TODO: pass to caller?
                                Log.e("ApplicationDatabase", e.getMessage());
                            }

                        }
                    }).build();

                }
            }
        }
        return sInstance;
    }

    /**
     * Method to insert test data
     *
     * @param database        the database singleton
     * @param consumedEntries the object to insert
     * @param product         the object to insert
     */
    private static void insertData(final ApplicationDatabase database, final ConsumedEntries consumedEntries, final Product product) {
        database.runInTransaction(new Runnable() {
            @Override
            public void run() {
                database.getConsumedEntriesDao().insert(consumedEntries);
                database.getProductDao().insert(product);
            }
        });

    }

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }


}
