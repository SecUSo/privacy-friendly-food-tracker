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

package org.secuso.privacyfriendlyfoodtracker.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.database.importexport.DatabaseExporter;
import org.secuso.privacyfriendlyfoodtracker.helpers.PropertyHelper;
import org.secuso.privacyfriendlyfoodtracker.ui.helper.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Displays an "about" page
 *
 * @author Jürgen Breitenbaumer, juergenbr
 */
public class DatabaseActivity extends BaseActivity {

    private Intent intentShareFile = new Intent(Intent.ACTION_SEND);
    private DatabaseExporter dbExporter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbExporter = new DatabaseExporter(this.getApplicationContext());
        setContentView(R.layout.activity_database);
        TextView textProducts = (TextView) findViewById(R.id.text_products);
        textProducts.setText(getResources().getString(R.string.db_number_products) + " " + dbExporter.getNumberOfProducts());
        TextView consumedEntries = (TextView) findViewById(R.id.text_conssumedentries);
        consumedEntries.setText(getResources().getString(R.string.db_number_consumedentries) + " " + dbExporter.getNumberOfConsumedEntries());
    }

    protected int getNavigationDrawerID() {
        return R.id.nav_database;
    }

    public void onClickExportBtn(View v) {
        try {
            String dbJsonString = "";
            String pattern = "dd-MM-yyyy";
            String dateInString = new SimpleDateFormat(pattern).format(new Date());
            String filename = PropertyHelper.getImpExProperty("db.impex.prefix", this.getApplicationContext()) + "_" + dateInString + ".json";
            if (v.getId() == R.id.export_button) {
                dbJsonString = dbExporter.exportDatabase();
            } else if (v.getId() == R.id.export_products_button) {
                dbJsonString = dbExporter.exportProductDatabase();
                filename = PropertyHelper.getImpExProperty("db.impex.prefix", this.getApplicationContext()) + "_products_" + dateInString + ".json";
            }
            if (dbJsonString != "") {

                this.create(this.getApplicationContext(), filename, dbJsonString);
                File file = read(this.getApplicationContext(), filename);
                if (file.exists()) {
                    Uri apkURI = FileProvider.getUriForFile(
                            this.getApplicationContext(),
                            this.getApplicationContext()
                                    .getPackageName() + ".provider", file);
                    intentShareFile.setDataAndType(apkURI, "application/json");
                    intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, apkURI);

                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            "Exporting Food Tracker DB...");
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Exporting Food Tracker DB...");

                    startActivity(Intent.createChooser(intentShareFile, "Share File"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickImportBtn(View v) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean create(Context context, String fileName, String jsonString) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }
    }

    private File read(Context context, String fileName) {
        try {
            File dbImpExFile = new File(context.getFileStreamPath(fileName).getAbsolutePath());
            return dbImpExFile;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }
}