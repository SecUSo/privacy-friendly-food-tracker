package org.secuso.privacyfriendlyfoodtracker.backup

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.JsonReader
import android.util.Log
import androidx.annotation.NonNull
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteDatabaseHook
import org.secuso.privacyfriendlybackup.api.backup.DatabaseUtil
import org.secuso.privacyfriendlybackup.api.backup.FileUtil
import org.secuso.privacyfriendlybackup.api.pfa.IBackupRestorer
import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase
import org.secuso.privacyfriendlyfoodtracker.helpers.KeyGenHelper
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.system.exitProcess


class BackupRestorer : IBackupRestorer {
    @Throws(IOException::class)
    private fun readDatabase(@NonNull reader: JsonReader, @NonNull context: Context) {
        reader.beginObject()
        val n1: String = reader.nextName()
        if (n1 != "version") {
            throw RuntimeException("Unknown value $n1")
        }
        val version: Int = reader.nextInt()
        val n2: String = reader.nextName()
        if (n2 != "content") {
            throw RuntimeException("Unknown value $n2")
        }

        Log.d(TAG, "Restoring database...")
        val restoreDatabaseName = "restoreDatabase"

        // delete if file already exists
        val restoreDatabaseFile = context.getDatabasePath(restoreDatabaseName)
        if (restoreDatabaseFile.exists()) {
            DatabaseUtil.deleteRoomDatabase(context, restoreDatabaseName)
        }

        // check if key exists
        if (!KeyGenHelper.isKeyGenerated()) {
            Log.d(TAG, "No key found. Generating new key...")
            KeyGenHelper.generateKey(context)
            KeyGenHelper.generatePassphrase(context)
            Log.d(TAG, "Key generated")
        }
        val databasePassword = KeyGenHelper.getSecretKeyAsChar(context)
        // create new restore database
        val db: SQLiteDatabase =
            SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(restoreDatabaseName).path, databasePassword, null, object : SQLiteDatabaseHook {
                override fun preKey(database: SQLiteDatabase?) {}
                override fun postKey(database: SQLiteDatabase) {
                    database.rawExecSQL("PRAGMA cipher_compatibility = 3;")
                }
            })

        db.beginTransaction()
        db.version = version

        Log.d(TAG, "Copying database contents...")
        DatabaseUtil.readDatabaseContent(reader, db)
        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()

        reader.endObject()

        // copy file to correct location
        val actualDatabaseFile = context.getDatabasePath(ApplicationDatabase.DATABASE_NAME)

        DatabaseUtil.deleteRoomDatabase(context, ApplicationDatabase.DATABASE_NAME)

        FileUtil.copyFile(restoreDatabaseFile, actualDatabaseFile)
        Log.d(TAG, "Database Restored")

        // delete restore database
        DatabaseUtil.deleteRoomDatabase(context, restoreDatabaseName)
    }

    @Throws(IOException::class)
    private fun readPreferences(@NonNull reader: JsonReader, @NonNull context: Context) {
        reader.beginObject()
        val pref: SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        while (reader.hasNext()) {
            val name: String = reader.nextName()
            when (name) {
                "IsFirstTimeLaunch" -> pref
                    .putBoolean(name, reader.nextBoolean())
                else -> throw RuntimeException("Unknown preference $name")
            }
        }
        pref.commit()
        reader.endObject()
    }

    override fun restoreBackup(context: Context, restoreData: InputStream): Boolean {
        return try {
            val isReader = InputStreamReader(restoreData)
            val reader = JsonReader(isReader)

            // START
            reader.beginObject()
            while (reader.hasNext()) {
                val type: String = reader.nextName()
                when (type) {
                    "database" -> readDatabase(reader, context)
                    "preferences" -> readPreferences(reader, context)
                    else -> throw RuntimeException("Can not parse type $type")
                }
            }
            reader.endObject()
            exitProcess(0)
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        const val TAG = "PFABackupRestorer"
    }
}