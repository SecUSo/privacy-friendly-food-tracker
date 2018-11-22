package org.secuso.privacyfriendlyexample.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import org.secuso.privacyfriendlyexample.R;
import org.secuso.privacyfriendlyexample.activities.helper.BaseActivity;

public class CalendarActivity extends BaseActivity {
    private CalendarView calendarView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = (CalendarView) findViewById(R.id.CalendarView); // get the reference of CalendarView
        calendarView.setDate(System.currentTimeMillis(),false,true);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(CalendarActivity.this, HelpActivity.class); // TODO correct navigation
                // returns current date as milliseconds (long)
                intent.putExtra("CapturedImageUrl", calendarView.getDate() );
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_game;
    }
}
