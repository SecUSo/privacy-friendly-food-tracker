package org.secuso.privacyfriendlyfoodtracker.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.activities.adapter.AddFoodPagerAdapter;
import org.secuso.privacyfriendlyfoodtracker.activities.helper.BaseActivity;

import java.util.Date;

/**
 * Base code for tapped activity.
 *
 * @author Simon Reinkemeier
 */
public class BaseAddFoodActivity extends AppCompatActivity {

    Date date;
    String name;
    int calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_base_add_food);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_food);
        AddFoodPagerAdapter myPagerAdapter = new AddFoodPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(myPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout_food);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();


        long dateLong = intent.getLongExtra("DATE", System.currentTimeMillis());
        date = new Date();
        date.setTime(dateLong);
        setupActionBar();

    }

    protected int getNavigationDrawerID() {
        return R.id.nav_statistic;
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
