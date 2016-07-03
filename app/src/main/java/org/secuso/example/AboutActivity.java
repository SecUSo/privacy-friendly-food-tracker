package org.secuso.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by yonjuni on 15.06.16.
 */
public class AboutActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        overridePendingTransition(0, 0);
    }
}

