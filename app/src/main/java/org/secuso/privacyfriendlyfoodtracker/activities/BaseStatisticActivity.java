package org.secuso.privacyfriendlyfoodtracker.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.activities.adapter.StatisticPagerAdapter;
import org.secuso.privacyfriendlyfoodtracker.activities.helper.BaseActivity;

/**
 * Base code for tapped activity.
 *
 * @author Andre Lutz
 */
public class BaseStatisticActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_statistic);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        StatisticPagerAdapter myPagerAdapter = new StatisticPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(myPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    protected int getNavigationDrawerID() {
        return R.id.nav_statistic;
    }

}
