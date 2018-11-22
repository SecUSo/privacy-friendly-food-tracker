package org.secuso.privacyfriendlyfoodtracker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase;
import org.secuso.privacyfriendlyfoodtracker.database.Product;
import org.secuso.privacyfriendlyfoodtracker.helpers.KeyGenHelper;

import java.security.Key;

public class GenerateKeyActivity extends AppCompatActivity {
    CheckBox mCheckBox1;
    CheckBox mCheckBox2;
    CheckBox mCheckBox3;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_key);
        mCheckBox1 = findViewById(R.id.firstCB);
        mCheckBox2 = findViewById(R.id.secondCB);
        mCheckBox3 = findViewById(R.id.thirdCB);
        mProgressBar = findViewById(R.id.pBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mCheckBox1.setEnabled(false);
        mCheckBox2.setEnabled(false);
        mCheckBox3.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Thread task = new Thread() {
            @Override
            public void run() {
                if (!KeyGenHelper.isKeyGenerated()) {
                    try {
                        KeyGenHelper.generateKey(getApplicationContext());
                        mCheckBox1.setChecked(true);
                        KeyGenHelper.generatePassphrase(getApplicationContext());
                        mCheckBox2.setChecked(true);
                        Key key = KeyGenHelper.getSecretKey(getApplicationContext()); // TODO: Remove test code
                        ApplicationDatabase applicationDatabase = ApplicationDatabase.getInstance(getApplicationContext());
                        //applicationDatabase.getConsumedEntriesDao().insert(new ConsumedEntries(0, 2, new java.sql.Date(Calendar.getInstance().getTime().getTime()), "test", 0));
                      //  applicationDatabase.getProductDao().insert(new Product( 0, "name", 200, "df"));
                        mCheckBox3.setChecked(true);
                        mProgressBar.setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                        Log.e("GenerateKeyActivity", e.getMessage());
                    }
                } else {
                    mCheckBox1.setChecked(true);
                    mCheckBox2.setChecked(true);
                    mCheckBox3.setChecked(true);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
                try {
                    ApplicationDatabase.getInstance(getApplicationContext()).getProductDao().insert(new Product( 0, "name", 200, "df"));

                }catch (Exception e){

                }

            }
        };

        task.start();

    }
}

