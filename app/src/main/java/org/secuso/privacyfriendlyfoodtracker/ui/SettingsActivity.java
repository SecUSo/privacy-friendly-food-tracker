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
package org.secuso.privacyfriendlyfoodtracker.ui;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.view.MenuItem;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.ui.helper.BaseActivity;

import java.util.Map;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends BaseActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        //setupActionBar();


        overridePendingTransition(0, 0);
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_settings;
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    /*private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }*/

    /*@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;

            // (!super.onMenuItemSelected(featureId, item)) {
            //    NavUtils.navigateUpFromSameTask(this);
            //}
            //return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }*/

    /**
     * {@inheritDoc}
     */
    /*@Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }*/

    /**
     * {@inheritDoc}
     */
    /*@Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }*/

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     * The commented method bindPrefenceSummaryToValue should be added for all preferences
     * with a summary that is depended from the current value of the preference
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {

        public void createFoodInfoPreferences(Context context, PreferenceCategory screen){
            for(Map.Entry<String,FoodInfo> foodInfoEntry : FoodInfosToShow.getFoodInfos(context).entrySet()){
                SwitchPreference showFoodInfoPreference = new SwitchPreference(context);
                showFoodInfoPreference.setKey(foodInfoEntry.getKey());
                showFoodInfoPreference.setTitle(foodInfoEntry.getValue().getName());
                screen.addPreference(showFoodInfoPreference);
            }
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //addPreferencesFromResource(R.xml.pref_general);
            Context context = getActivity();//dont know whther this will work?
            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

            PreferenceCategory whatToShowCategory = new PreferenceCategory(context);
            whatToShowCategory.setKey("what_info_to_show_category");
            whatToShowCategory.setTitle("What Food Info To Show");
            screen.addPreference(whatToShowCategory);

            createFoodInfoPreferences(context,whatToShowCategory);

            PreferenceCategory dailyGoalsCategory = new PreferenceCategory(context);
            dailyGoalsCategory.setKey("daily_goals_category");
            dailyGoalsCategory.setTitle(R.string.daily_goals_setting_category_heading);
            screen.addPreference(dailyGoalsCategory);

            createDailyGoalsPreferences(context,dailyGoalsCategory);

            setPreferenceScreen(screen);

            //setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //bindPreferenceSummaryToValue(findPreference("example_text"));
            //bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        /***
         * Create the preferences for setting daily goals and whether those goals should not
         * be exceeded or not be subceeded
         * @param context
         * @param screen
         */
        private void createDailyGoalsPreferences(Context context, PreferenceCategory screen) {
            EditTextPreference caloriesGoalPreference = new EditTextPreference(context);
            caloriesGoalPreference.setKey(FoodInfosToShow.GOAL_KEY_PREFIX + "calories");
            caloriesGoalPreference.setTitle(getResources().getString(R.string.daily_goal_setting,"Calories", "kCal"));

            screen.addPreference(caloriesGoalPreference);

            SwitchPreference caloriesGoalSwitchPreference = new SwitchPreference(context);
            caloriesGoalSwitchPreference.setKey(FoodInfosToShow.GOAL_DONT_EXCEED + "calories");
            caloriesGoalSwitchPreference.setTitle(getResources().getString(R.string.daily_goals_dont_exceed_setting,"Calories"));
            screen.addPreference(caloriesGoalSwitchPreference);

            for(Map.Entry<String,FoodInfo> foodInfoEntry : FoodInfosToShow.getFoodInfos(context).entrySet()){
                EditTextPreference dailyGoalPreference = new EditTextPreference(context);
                dailyGoalPreference.setKey(FoodInfosToShow.GOAL_KEY_PREFIX + foodInfoEntry.getKey());
                dailyGoalPreference.setTitle(getResources().getString(R.string.daily_goal_setting,foodInfoEntry.getValue().getName(), foodInfoEntry.getValue().getUnit()));
                screen.addPreference(dailyGoalPreference);

                SwitchPreference dailyGoalSwitchPreference = new SwitchPreference(context);
                dailyGoalSwitchPreference.setKey(FoodInfosToShow.GOAL_DONT_EXCEED + foodInfoEntry.getKey());
                dailyGoalSwitchPreference.setTitle(getResources().getString(R.string.daily_goals_dont_exceed_setting,foodInfoEntry.getValue().getName()));
                screen.addPreference(dailyGoalSwitchPreference);
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                //getActivity().finish();
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
