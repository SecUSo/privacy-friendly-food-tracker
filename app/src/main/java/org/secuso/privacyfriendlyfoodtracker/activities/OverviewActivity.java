package org.secuso.privacyfriendlyfoodtracker.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.secuso.privacyfriendlyfoodtracker.R;

import java.util.Date;
import org.secuso.privacyfriendlyfoodtracker.activities.adapter.DatabaseEntry;
import org.secuso.privacyfriendlyfoodtracker.activities.adapter.DatabaseAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

public class OverviewActivity extends AppCompatActivity {

    long date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOverview);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        date = intent.getLongExtra("DATE", System.currentTimeMillis());

        DatabaseAdapter adapter = new DatabaseAdapter();
        Date d = new Date();
        d.setTime(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = "";
        try{
             formattedDate = dateFormat.format(d);
        } catch(Exception e){
            e.printStackTrace();
        }




        DatabaseEntry[] entries = adapter.getEntriesForDay(d);

        LinearLayout foodList = this.findViewById(R.id.DailyList);
        int totalCalories = 0;
        for(DatabaseEntry e : entries) {
            //set up CardView for every entry
            CardView c = new CardView(this);
            c.setId(View.generateViewId());
            c.setMinimumWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            c.setMinimumHeight(40);
            c.setContentPadding(10, 10, 10, 10);
            c.setUseCompatPadding(true);
            c.setClickable(true);

            int[] attrs = new int[] { android.R.attr.selectableItemBackground };
            TypedArray ta = obtainStyledAttributes(attrs);
            Drawable drawableFromTheme = ta.getDrawable(0 );
            c.setForeground(drawableFromTheme);
            //set up Layout for every entry
            ConstraintLayout cl = new ConstraintLayout(this);
            ConstraintSet set = new ConstraintSet();

            TextView name = new TextView(this);
            name.setId(View.generateViewId());
            name.setText(e.name);
            //set.constrainHeight(name.getId(), 50);
            set.constrainWidth(name.getId(), ConstraintSet.WRAP_CONTENT);


            TextView amount = new TextView(this);
            amount.setId(View.generateViewId());
            amount.setText(Integer.toString(e.amount) + "g");
            set.constrainWidth(amount.getId(), ConstraintSet.WRAP_CONTENT);
            set.constrainHeight(amount.getId(), ConstraintSet.WRAP_CONTENT);


            TextView calories = new TextView(this);
            int consumedEnergy = (e.amount*e.energy)/100;
            totalCalories += consumedEnergy;
            calories.setText(Integer.toString(consumedEnergy) + " kCal");
            calories.setId(View.generateViewId());
            set.constrainWidth(calories.getId(), ConstraintSet.WRAP_CONTENT);


            cl.addView(name);
            cl.addView(calories);
            cl.addView(amount);
            c.addView(cl);
            foodList.addView(c);

            set.connect(name.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 20);
            set.connect(name.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);


            set.connect(calories.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 20);
            set.connect(calories.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);

            set.connect(amount.getId(), ConstraintSet.TOP, name.getId(), ConstraintSet.BOTTOM, 20);
            set.connect(amount.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 20);
            set.connect(amount.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);

            set.applyTo(cl);
            final DatabaseEntry entry = e;
            c.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    getSupportActionBar().setTitle("Delete Entry?");
                    return true;
                }
            });
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OverviewActivity.this, FoodActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("ID", entry.id);
                    intent.putExtra("NAME", entry.name);
                    intent.putExtra("AMOUNT", entry.amount);
                    intent.putExtra("CALORIES", entry.energy);
                    startActivity(intent);
                }

            });
        }
        FloatingActionButton fab = this.findViewById(R.id.addFoodItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverviewActivity.this, FoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("DATE", date);
                startActivity(intent);
            }

        });

        TextView heading = this.findViewById(R.id.overviewHeading);
        String cal =  getString(R.string.total_calories);
        heading.setText(formattedDate + ": " + totalCalories + " " + cal);
        setupActionBar();

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
