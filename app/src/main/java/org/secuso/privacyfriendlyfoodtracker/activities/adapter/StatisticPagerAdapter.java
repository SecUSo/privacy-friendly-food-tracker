package org.secuso.privacyfriendlyfoodtracker.activities.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.activities.MonthStatisticFragment;
import org.secuso.privacyfriendlyfoodtracker.activities.WeekStatisticFragment;

public class StatisticPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;

    public StatisticPagerAdapter(FragmentManager fm,Context context){
        super(fm);
        this.context = context;
    }

    @Override    public Fragment getItem(int position) {
        switch (position){
            case 0: return new WeekStatisticFragment();
            case 1: return new MonthStatisticFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override    public CharSequence getPageTitle(int position) {        switch (position){
        case 0: return context.getResources().getString(R.string.week);
        case 1: return context.getResources().getString(R.string.month);
        default: return null;
    }
    }
}
