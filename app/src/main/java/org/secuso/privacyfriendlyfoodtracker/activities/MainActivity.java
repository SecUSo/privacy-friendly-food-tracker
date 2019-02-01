/*
 This file is part of Privacy Friendly App Example.

 Privacy Friendly App Example is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly App Example is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly App Example. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlyfoodtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.activities.helper.BaseActivity;

import java.util.Date;

/**
 * @author Christopher Beckmann, Karola Marky
 * @version 20171016
 */
public class MainActivity extends BaseActivity {
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView = (CalendarView) findViewById(R.id.CalendarView); // get the reference of CalendarView
        calendarView.setDate(System.currentTimeMillis(), false, true);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(MainActivity.this, OverviewActivity.class); // TODO correct navigation
                // Build string from chosen date to parse into Date object
                // (month+1) because months count from 0 in java but SimpleDateFormat parses it as 1-12
                String chosenDate = dayOfMonth + "/" + (month+1) + "/" + year;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try{
                    Date date = sdf.parse(chosenDate);
                    long millis = date.getTime();
                    intent.putExtra("DATE", millis);
                } catch (ParseException e){
                    e.printStackTrace();
                }

                startActivity(intent);
            }
        });
        overridePendingTransition(0, 0);
    }

    /**
     * This method connects the Activity to the menu item
     *
     * @return ID of the menu item it belongs to
     */
    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_example;
    }

    public void onClick(View v) {
        if (v != null) switch (v.getId()) {

        }
    }
}
