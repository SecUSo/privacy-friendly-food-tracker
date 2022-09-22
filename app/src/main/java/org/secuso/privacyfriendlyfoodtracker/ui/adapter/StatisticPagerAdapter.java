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
package org.secuso.privacyfriendlyfoodtracker.ui.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.ui.MonthStatisticFragment;
import org.secuso.privacyfriendlyfoodtracker.ui.WeekStatisticFragment;

/**
 * Implementation of PagerAdapter that uses a Fragment to manage each page. This class also handles saving and restoring of fragment's state.
 *
 * @author Andre Lutz
 */
public class StatisticPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;

    /**
     * A pager adapter.
     *
     * @param fm      the fragment manager
     * @param context the context
     */
    public StatisticPagerAdapter(FragmentManager fm, Context context) {
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
                return new WeekStatisticFragment();
            case 1:
                return new MonthStatisticFragment();
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
                return context.getResources().getString(R.string.week);
            case 1:
                return context.getResources().getString(R.string.month);
            default:
                return null;
        }
    }
}
