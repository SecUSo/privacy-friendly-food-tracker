package org.secuso.privacyfriendlyexample;

import android.os.Bundle;

/**
 * Created by yonjuni on 15.06.16.
 */
public class AboutActivity extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        overridePendingTransition(0, 0);
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_about;
    }
}

