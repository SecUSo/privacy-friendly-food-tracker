package org.secuso.privacyfriendlyfoodtracker.activities.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.activities.AddFoodFragment;
import org.secuso.privacyfriendlyfoodtracker.activities.SearchFoodFragment;

/**
 * Implementation of PagerAdapter that uses a Fragment to manage each page. This class also handles saving and restoring of fragment's state.
 *
 * @author Simon Reinkemeier
 */
public class AddFoodPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;

    /**
     * A pager adapter.
     *
     * @param fm      the fragment manager
     * @param context the context
     */
    public AddFoodPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    /**
     * Get the fragment for position.
     *
     * @param position the position
     * @return the fragment at position or null
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SearchFoodFragment();
            case 1:
                return new AddFoodFragment();
        }
        return null;
    }

    /**
     * The number of fragments.
     *
     * @return number of fragments
     */
    @Override
    public int getCount() {
        return 2;
    }

    /**
     * Get the page title.
     *
     * @param position
     * @return the fragment position
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.search);
            case 1:
                return context.getResources().getString(R.string.add);
            default:
                return null;
        }
    }
}
