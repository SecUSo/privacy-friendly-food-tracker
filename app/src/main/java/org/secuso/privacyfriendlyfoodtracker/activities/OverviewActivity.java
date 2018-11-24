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
import org.secuso.privacyfriendlyfoodtracker.customviews.CheckableCardView;
import org.w3c.dom.Text;

import android.support.v7.app.ActionBar;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

public class OverviewActivity extends AppCompatActivity {

    long date;
    private int selectedCards;

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

        selectedCards = 0;


        DatabaseEntry[] entries = adapter.getEntriesForDay(d);

        LinearLayout foodList = this.findViewById(R.id.DailyList);
        int totalCalories = 0;
        for(DatabaseEntry e : entries) {
            //set up CardView for every entry
            CheckableCardView c = new CheckableCardView(this);
            setCardViewOptions(c);

            //set up Layout for every entry
            ConstraintLayout cl = new ConstraintLayout(this);
            ConstraintSet set = new ConstraintSet();

            TextView name = new TextView(this);
            name.setId(View.generateViewId());
            name.setText(e.name);
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

            TextView id = new TextView(this);
            id.setText(e.id);
            id.setVisibility(View.INVISIBLE);


            cl.addView(name);
            cl.addView(calories);
            cl.addView(amount);
            c.addView(cl);
            c.addView(id);
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
                    int newSelectedCards = countSelectedCards();
                    if (newSelectedCards == 0){
                        toggleDeletionMenuVisibility();
                    } else if (selectedCards == 0 && newSelectedCards > 0) {
                        toggleDeletionMenuVisibility();
                    }
                    selectedCards = newSelectedCards;
                    return true;
                }
            });
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedCards > 0) {
                        CheckableCardView card = (CheckableCardView) v;
                        card.toggle();
                        card.refreshDrawableState();
                        selectedCards = countSelectedCards();
                        if (selectedCards == 0) {
                            toggleDeletionMenuVisibility();
                        }
                    } else {
                        Intent intent = new Intent(OverviewActivity.this, FoodActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("ID", entry.id);
                        intent.putExtra("NAME", entry.name);
                        intent.putExtra("AMOUNT", entry.amount);
                        intent.putExtra("CALORIES", entry.energy);
                        startActivity(intent);
                    }

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

    private int countSelectedCards(){
        int count = 0;
        LinearLayout foodList = this.findViewById(R.id.DailyList);
        for (int i = 0; i<foodList.getChildCount(); i++){
            View v = foodList.getChildAt(i);
            if (v instanceof CheckableCardView) {
                CheckableCardView c = (CheckableCardView) v;
                if (c.isChecked()) {
                    count++;
                }
            }

        }
        return count;
    }
    private void setCardViewOptions(CheckableCardView c){
        c.setCardBackgroundColor(
                ContextCompat.getColorStateList(
                        this,
                        R.color.selector_card_view_colors
                )
        );
        c.setId(View.generateViewId());
        c.setMinimumWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        c.setMinimumHeight(40);
        c.setContentPadding(10, 10, 10, 10);
        c.setUseCompatPadding(true);
        c.setClickable(true);

        // Ripple effect and checkable colours
        int[] attrs = new int[] { android.R.attr.selectableItemBackground };
        TypedArray ta = obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0 );
        c.setForeground(drawableFromTheme);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cancel_deletion:
                unselectAllCards();
                selectedCards = 0;
                toggleDeletionMenuVisibility();
                resetTotalCalorieCounter();
                break;
            case R.id.action_confirm_deletion:
                deleteSelectedCards();
                selectedCards = 0;
                toggleDeletionMenuVisibility();
                resetTotalCalorieCounter();
                break;
            default:
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.overview_actionbar, menu);
        MenuItem del = menu.findItem(R.id.action_cancel_deletion);
        MenuItem conf = menu.findItem(R.id.action_confirm_deletion);
        if (selectedCards > 0) {
            del.setVisible(true);
            del.setEnabled(true);
            conf.setVisible(true);
            conf.setEnabled(true);
            getSupportActionBar().setTitle(R.string.delete_entry_prompt);


        } else {
            del.setVisible(false);
            del.setEnabled(false);
            conf.setVisible(false);
            conf.setEnabled(false);
            getSupportActionBar().setTitle(R.string.title_activity_overview);

        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    private void unselectAllCards() {
        LinearLayout foodList = this.findViewById(R.id.DailyList);
        for (int i = 0; i<foodList.getChildCount(); i++){
            View v = foodList.getChildAt(i);
            if (v instanceof CheckableCardView) {
                CheckableCardView c = (CheckableCardView) v;
                if (c.isChecked()) {
                    c.toggle();
                    c.refreshDrawableState();
                }
            }

        }
    }

    private void toggleDeletionMenuVisibility() {
        invalidateOptionsMenu();
    }

    private void deleteSelectedCards() {
        LinearLayout foodList = this.findViewById(R.id.DailyList);
        List<View> cardsToRemove = new LinkedList<View>();
        for (int i = 0; i<foodList.getChildCount(); i++){
            View v = foodList.getChildAt(i);
            if (v instanceof CheckableCardView) {
                CheckableCardView c = (CheckableCardView) v;
                if (c.isChecked()) {
                    TextView idView =(TextView) c.getChildAt(1);
                    String id = idView.getText().toString();
                    DatabaseAdapter da = new DatabaseAdapter();
                    da.removeEntry(id);
                    cardsToRemove.add(v);
                }
            }
        }

        for (View v : cardsToRemove) {
            foodList.removeView(v);
        }

    }

    private void resetTotalCalorieCounter() {
        LinearLayout foodList = this.findViewById(R.id.DailyList);
        int totalCalories = 0;
        TextView heading = this.findViewById(R.id.overviewHeading);
        String cal =  getString(R.string.total_calories);
        Date d = new Date();
        d.setTime(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = "";
        try{
            formattedDate = dateFormat.format(d);
        } catch(Exception e){
            e.printStackTrace();
        }
        DatabaseAdapter da = new DatabaseAdapter();
        DatabaseEntry[] entries = da.getEntriesForDay(d);
        for(DatabaseEntry e : entries) {
            totalCalories += (e.energy * e.amount) / 100;
        }

        heading.setText(formattedDate + ": " + totalCalories + " " + cal);

    }

}
