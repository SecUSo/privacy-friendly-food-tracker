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
package org.secuso.privacyfriendlyfoodtracker.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase;
import org.secuso.privacyfriendlyfoodtracker.helpers.FirstLaunchManager;
import org.secuso.privacyfriendlyfoodtracker.helpers.KeyGenHelper;

/**
 * Generate key activity. Manages the key creation and shows the current state.
 *
 * @author Andre Lutz
 */
public class GenerateKeyActivity extends AppCompatActivity {
    FirstLaunchManager mFirstLaunchManager;
    CheckBox mCheckBox1;
    CheckBox mCheckBox2;
    CheckBox mCheckBox3;
    ProgressBar mProgressBar;
    FloatingActionButton mFloatingActionButton;
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
        mFloatingActionButton = findViewById(R.id.fab);
        hideFAB();
        mFirstLaunchManager = new FirstLaunchManager(this);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(GenerateKeyActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mFirstLaunchManager.setFirstTimeLaunch(false);
        startActivity(intent);
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
                        ApplicationDatabase.getInstance(getApplicationContext());
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
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // update gui
                        showFAB();
                    }
                });
            }
        };

        task.start();

    }

    boolean fabShouldBeShown;
    FloatingActionButton.OnVisibilityChangedListener fabListener = new FloatingActionButton.OnVisibilityChangedListener() {
        @Override
        public void onShown(FloatingActionButton fab) {
            super.onShown(fab);
            if(!fabShouldBeShown){
                fab.hide();
            }
        }

        @Override
        public void onHidden(FloatingActionButton fab) {
            super.onHidden(fab);
            if(fabShouldBeShown){
                fab.show();
            }
        }
    };

    public void hideFAB() {
        fabShouldBeShown = false;
        mFloatingActionButton.hide(fabListener);
    }

    public void showFAB() {
        fabShouldBeShown = true;
        mFloatingActionButton.show(fabListener);
    }


    @Override
    public void onBackPressed() {

    }
}

