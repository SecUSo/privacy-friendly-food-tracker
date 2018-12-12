package org.secuso.privacyfriendlyfoodtracker;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BaseStatisticActivity extends AppCompatActivity {

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
