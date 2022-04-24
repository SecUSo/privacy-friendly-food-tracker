package org.secuso.privacyfriendlyfoodtracker.database.importexport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.json.JsonSanitizer;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.ui.helper.BaseActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DbImpExpFileReceivedActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseImporter dbImporter = new DatabaseImporter(this.getApplicationContext());
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) || Intent.ACTION_VIEW.equals(action) && type != null) {
            if ("application/json".equals(type)) {
                // handle db file import
                String content = readData(intent.getData());
                if (isJson(content)) {
                    String wellFormedJson = JsonSanitizer.sanitize(content);
                    dbImporter.importDatabase(wellFormedJson);
                }
            }
        }
    }

    public static boolean isJson(String jsonInString) {
        try {
            Gson gson = new Gson();
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    private String readData(Uri uri) {
        String content = "";
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }
            content = total.toString();
        } catch (Exception e) {

        }
        return content;
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_database;
    }
}