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
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

    // Date for the entry
    Date date;
    // Name of the product
    String name;
    // Calories per 100g
    float calories;
    // ID of the product
    int id;

    long dateLong;
    // true if a productID has been set by the SearchFoodFragment
    boolean productSet = false;

    /**
     * Called when the Activity is created
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_add_food);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_food);
        AddFoodPagerAdapter myPagerAdapter = new AddFoodPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(myPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout_food);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabReselected(TabLayout.Tab t){
            }
            @Override
            public void onTabUnselected(TabLayout.Tab t){
            }
            @Override
            public void onTabSelected(TabLayout.Tab t){
                productSet = false;
            }

        });

        Intent intent = getIntent();


        dateLong = intent.getLongExtra("DATE", System.currentTimeMillis());
        date = new Date();
        date.setTime(dateLong);
        setupActionBar();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(BaseAddFoodActivity.this, OverviewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("DATE", dateLong);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Displays the "Back" or "Up" button in the Action bar
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
