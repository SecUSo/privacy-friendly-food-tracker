package org.secuso.privacyfriendlyfoodtracker.backup

import android.content.Context
import android.preference.PreferenceManager
import android.util.JsonWriter
import android.util.Log
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteDatabaseHook
import org.secuso.privacyfriendlybackup.api.backup.DatabaseUtil
import org.secuso.privacyfriendlybackup.api.backup.PreferenceUtil
import org.secuso.privacyfriendlybackup.api.pfa.IBackupCreator
import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase
import org.secuso.privacyfriendlyfoodtracker.helpers.KeyGenHelper
import java.io.OutputStream
import java.io.OutputStreamWriter

class BackupCreator : IBackupCreator {
    override fun writeBackup(context: Context, outputStream: OutputStream) {
        Log.d(TAG, "createBackup() started")
        val outputStreamWriter = OutputStreamWriter(outputStream, Charsets.UTF_8)
        val writer = JsonWriter(outputStreamWriter)
        writer.setIndent("")

        try {
            writer.beginObject()

            Log.d(TAG, "Writing database")
            writer.name("database")

            SQLiteDatabase.loadLibs(context)

            if (context.getDatabasePath(ApplicationDatabase.DATABASE_NAME).exists()) {
                val database = SQLiteDatabase.openDatabase(
                    context.getDatabasePath(ApplicationDatabase.DATABASE_NAME).path,
                    KeyGenHelper.getSecretKeyAsChar(context),
                    null,
                    SQLiteDatabase.OPEN_READONLY,
                    object : SQLiteDatabaseHook {
                        override fun preKey(database: SQLiteDatabase) {}
                        override fun postKey(database: SQLiteDatabase) {
                            database.rawExecSQL("PRAGMA cipher_compatibility = 3;")
                        }
                    })

                DatabaseUtil.writeDatabase(writer, database)
                database.close()
            } else {
                Log.d(TAG, "No database found")
                writer.beginObject()
                writer.endObject()
            }

            Log.d(TAG, "Writing preferences")
            writer.name("preferences")

            val pref = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
            PreferenceUtil.writePreferences(writer, pref, arrayOf(KeyGenHelper.PREFERENCE_ENCRYPTED_KEY_NAME))

            Log.d(TAG, "Writing files")
            writer.endObject()
            writer.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error occurred", e)
            e.printStackTrace()
        }

        Log.d(TAG, "Backup created successfully")
    }

    companion object {
        const val TAG = "PFABackupCreator"
    }
}