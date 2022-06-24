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

import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntries;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseEntry;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseFacade;
import org.secuso.privacyfriendlyfoodtracker.ui.viewmodels.OverviewViewModel;
import org.secuso.privacyfriendlyfoodtracker.ui.viewmodels.SharedStatisticViewModel;
import org.secuso.privacyfriendlyfoodtracker.ui.views.CheckableCardView;
import org.secuso.privacyfriendlyfoodtracker.R;
import org.w3c.dom.Text;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Database;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Barrier;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * The overview for a day
 * @author Simon Reinkemeier
 */
public class OverviewActivity extends AppCompatActivity {

    // The date for the activity. This is tied to each OverviewActivity as
    // each OverviewActivity is created as an overview for a separate date
    long date;
    // Keeps track of selected Entries / CardViews
    // This is important for deciding whether to display the delete dialog
    // I.e. if more than 0 cards are selected, the user should be able to delete them
    private int selectedCards;

    private OverviewViewModel viewModel;

    /***
     * Maps containing UI elements for the food infos to show.
     */
    private Map<String, TextView> tvFoodInfoNameMap = new HashMap<>();
    private Map<String, TextView> tvFoodInfoAmountMap = new HashMap<>();
    private Map<String, ProgressBar> pbFoodInfoProgressMap = new HashMap<>();

    private ProgressBar progressBarCalories = null;

    /**
     * sets up the activity
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOverview);
        setSupportActionBar(toolbar);

        // Set up global variables
        // Set the date. If no date is passed on, the system date is chosen by default.
        //this NEEDS to happen before viewModel.init(), else viewmodel will load data for 1.1.1970
        Intent intent = getIntent();
        date = intent.getLongExtra("DATE", System.currentTimeMillis());

        viewModel = ViewModelProviders.of(this).get(OverviewViewModel.class);
        viewModel.init(getDateForActivity());

        viewModel.getList().observe(this, new Observer<List<DatabaseEntry>>() {
            @Override
            public void onChanged(@Nullable List<DatabaseEntry> databaseEntries) {
                refreshFoodList(databaseEntries);
                refreshTotalCalorieCounter(databaseEntries);
            }
        });


        selectedCards = 0;

        setUpFloatingActionButton();
        setupActionBar();
    }

    /**
     * refresh the food list and calorie counter in case the database
     * was altered by another activity
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Called if either 'delete' or 'cancel' was clicked
     * @param item The item that was clicked
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel_deletion:
                unselectAllCards();
                break;
            case R.id.action_confirm_deletion:
                deleteSelectedCards();
                break;
            default:
                finish();
                return true;

        }
        selectedCards = 0;
        toggleDeletionMenuVisibility();
        return true;
    }


    /**
     * set the menu to say the correct things
     * @param menu the menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.overview_actionbar, menu);
        if (selectedCards > 0) {
            // if more than one card is selected, we want to enable the 'cancel' and 'confirm' buttons
            setMenuVisibility(true, menu);
            getSupportActionBar().setTitle(R.string.delete_entry_prompt);
        } else {
            // if no card is selected, hide the menu
            setMenuVisibility(false, menu);
            getSupportActionBar().setTitle(R.string.title_activity_overview);
        }
        return true;
    }

    /**
     * sets the visibility of the Deletion menu
     *
     * @param isVisible The visibility. false for invisible, true for visible.
     * @param menu      The deletion menu
     */
    private void setMenuVisibility(boolean isVisible, Menu menu) {
        MenuItem del = menu.findItem(R.id.action_cancel_deletion);
        MenuItem conf = menu.findItem(R.id.action_confirm_deletion);
        del.setVisible(isVisible);
        del.setEnabled(isVisible);
        conf.setVisible(isVisible);
        conf.setEnabled(isVisible);

    }

    /**
     * Needed to set the menu to the correct items
     * @param menu the menu
     * @return true
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }


    /**
     * unselects all selected cards when called.
     */
    private void unselectAllCards() {
        LinearLayout foodList = this.findViewById(R.id.DailyList);
        for (int i = 0; i < foodList.getChildCount(); i++) {
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

    /**
     * toggles deletion menu visibility.
     */
    private void toggleDeletionMenuVisibility() {
        View fab = findViewById(R.id.addFoodItem);
        View cancelDeleteFab = this.findViewById(R.id.cancelDeleteFoodItem);
        View deleteFab = this.findViewById(R.id.deleteFoodItem);
        if(deleteFab.getVisibility()==View.VISIBLE) {
            fab.setVisibility(View.VISIBLE);
            //cancelDeleteFab.setVisibility(View.GONE);
            deleteFab.setVisibility(View.GONE);
        }
        else{
            fab.setVisibility(View.GONE);
            //cancelDeleteFab.setVisibility(View.VISIBLE);
            deleteFab.setVisibility(View.VISIBLE);
        }
    }


    /**
     * deletes all currently selected cards and associatedEntries
     */
    private void deleteSelectedCards() {
        ViewGroup foodList = getEntryList();
        List<View> cardsToRemove = new LinkedList<>();
        for (int i = 0; i < foodList.getChildCount(); i++) {
            View v = foodList.getChildAt(i);
            if (v instanceof CheckableCardView) {
                CheckableCardView c = (CheckableCardView) v;
                if (c.isChecked()) {
                    TextView idView = (TextView) c.getChildAt(1);
                    String id = idView.getText().toString();
                    viewModel.deleteEntryById(Integer.parseInt(id));
                    cardsToRemove.add(v);
                }
            }
        }
        for (View v : cardsToRemove) {
            foodList.removeView(v);
        }
    }

    /**
     * Gets the list where all entries are stores
     *
     * @return A ViewGroup containing all CheckedCardViews
     */
    private ViewGroup getEntryList() {
        LinearLayout foodlist = this.findViewById(R.id.DailyList);
        return foodlist;
    }

    /**
     * Refreshes the calorie and macroCounter counter at the top of the activity.
     * Recounts calories for all Entries of the day.
     */
    private void refreshTotalCalorieCounter(@Nullable List<DatabaseEntry> entries) {
        BigDecimal totalCalories = new BigDecimal("0");

        TextView heading = this.findViewById(R.id.overviewHeading);


        String cal = getString(R.string.total_calories);
        Date d = getDateForActivity();
        String formattedDate = getFormattedDate(d);
        if(entries != null) {
            for (DatabaseEntry e : entries) {
                totalCalories = totalCalories.add(BigDecimal.valueOf(e.energy * e.amount / 100));
            }
        }
        heading.setText(String.format(Locale.ENGLISH, "%s", formattedDate));

        Float caloriesDailyGoal = FoodInfosToShow.getDailyGoalFromPreferences(this,"calories");

        TextView tvCaloriesAmount = this.findViewById(R.id.caloriesAmountTv);
        String caloriesAmountText;
        if(caloriesDailyGoal != null){
            caloriesAmountText = getResources().getString( R.string.overview_nutriment_amount_of_goal, totalCalories, caloriesDailyGoal, cal);
        }else{
            caloriesAmountText = getResources().getString( R.string.overview_nutriment_amount, totalCalories, cal);
        }
        tvCaloriesAmount.setText(caloriesAmountText);

        boolean rebuildLayout=false;


        Map<String,FoodInfo> foodInfosToShow = FoodInfosToShow.getFoodInfosShownAsMap(this);
        if(!tvFoodInfoAmountMap.isEmpty()){// if there already is a map with textviews, check if all foodinfos have a view, if not the maps get cleared, so the layout can be created in the next loop without problems
            for(Map.Entry<String,FoodInfo> foodInfoEntry : foodInfosToShow.entrySet()){
                if(!tvFoodInfoAmountMap.containsKey(foodInfoEntry.getKey())){
                    clearLayoutAndMaps();
                    rebuildLayout = true;
                    break;
                }
            }
            for(Map.Entry<String, TextView> textViewEntry : tvFoodInfoAmountMap.entrySet()){//do the same as above for textview map entries, to see if maybe food infos have changed to not show, so an empty textview would be dangling around
                if(!foodInfosToShow.containsKey(textViewEntry.getKey())){
                    clearLayoutAndMaps();
                    rebuildLayout = true;
                    break;
                }
            }
        }else{
            rebuildLayout = true;
        }


        ConstraintLayout constraintLayout = findViewById(R.id.overviewContentConstraintLayout);


        //add progressBar if daily goal specified
        if(caloriesDailyGoal != null){
            if(progressBarCalories == null){
                clearLayoutAndMaps();
                rebuildLayout = true;

                progressBarCalories = new ProgressBar(this, null,
                        android.R.attr.progressBarStyleHorizontal);
                progressBarCalories.setId(View.generateViewId());
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                progressBarCalories.setLayoutParams(layoutParams);

                constraintLayout.addView(progressBarCalories);


                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);

                constraintSet.connect(progressBarCalories.getId(), ConstraintSet.TOP, R.id.caloriesNameTv, ConstraintSet.BOTTOM, 0);

                constraintSet.connect(progressBarCalories.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, 0);
                constraintSet.connect(progressBarCalories.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT, 0);


                constraintSet.applyTo(constraintLayout);

            }
            styleProgressBarByFoodInfoKey(progressBarCalories,"calories",totalCalories.floatValue(), caloriesDailyGoal);

        }else{
            if(progressBarCalories != null){
                constraintLayout.removeView(progressBarCalories);

                clearLayoutAndMaps();
                rebuildLayout = true;
            }
        }

        int idOfPredecessor = caloriesDailyGoal != null ? progressBarCalories.getId() : R.id.caloriesNameTv;
        for(Map.Entry<String,FoodInfo> foodInfoEntry : foodInfosToShow.entrySet()){

            TextView tvFoodInfoName=null;
            TextView tvFoodInfoAmount=null;
            ProgressBar progressBar = null;
            Float dailyGoal = FoodInfosToShow.getDailyGoalFromPreferences(this,foodInfoEntry.getKey());
            if(tvFoodInfoAmountMap.containsKey(foodInfoEntry.getKey())){ //this could be also replaced by a check for rebuildLayout
                tvFoodInfoAmount = tvFoodInfoAmountMap.get(foodInfoEntry.getKey());
                tvFoodInfoName = tvFoodInfoNameMap.get(foodInfoEntry.getKey()); //tvFoodInfoName should never be needed, when it already exists. But atm I leave it there.
                if(dailyGoal != null){
                    progressBar = pbFoodInfoProgressMap.get(foodInfoEntry.getKey());
                }
            }else {
                tvFoodInfoAmount = new TextView(this);
                tvFoodInfoName = new TextView(this);


                tvFoodInfoName.setId(View.generateViewId());
                tvFoodInfoAmount.setId(View.generateViewId());

                if(dailyGoal != null) {
                    progressBar = new ProgressBar(this, null,
                            android.R.attr.progressBarStyleHorizontal);
                    progressBar.setId(View.generateViewId());
                }

                tvFoodInfoName.setText(foodInfoEntry.getValue().getName());

                tvFoodInfoName.setGravity(Gravity.LEFT);
                tvFoodInfoAmount.setGravity(Gravity.RIGHT);


                constraintLayout.addView(tvFoodInfoName);
                constraintLayout.addView(tvFoodInfoAmount);

                if(dailyGoal != null) {
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    progressBar.setLayoutParams(layoutParams);
                    constraintLayout.addView(progressBar);
                }


                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);

                constraintSet.connect(tvFoodInfoName.getId(), ConstraintSet.TOP, idOfPredecessor, ConstraintSet.BOTTOM, 0);
                constraintSet.connect(tvFoodInfoAmount.getId(), ConstraintSet.TOP, idOfPredecessor, ConstraintSet.BOTTOM, 0);

                constraintSet.connect(tvFoodInfoName.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, 0);
                constraintSet.connect(tvFoodInfoAmount.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT, 0);

                if(dailyGoal != null) {
                    constraintSet.connect(progressBar.getId(), ConstraintSet.TOP, tvFoodInfoAmount.getId(), ConstraintSet.BOTTOM, 0);
                    constraintSet.connect(progressBar.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, 0);
                    constraintSet.connect(progressBar.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT, 0);
                }

                constraintSet.applyTo(constraintLayout);

                tvFoodInfoAmountMap.put(foodInfoEntry.getKey(),tvFoodInfoAmount);
                tvFoodInfoNameMap.put(foodInfoEntry.getKey(),tvFoodInfoName);

                if(dailyGoal != null) {
                    pbFoodInfoProgressMap.put(foodInfoEntry.getKey(), progressBar);
                }
            }





            float totalAmount = 0;

            for (DatabaseEntry e : entries) {
                totalAmount = totalAmount + (FoodInfosToShow.getFoodInfoValueByKey(e,foodInfoEntry.getKey(), foodInfoEntry.getValue(), getApplicationContext()) * e.amount / 100);
            }

            String nutrimentFoodAmountText;
            if(dailyGoal != null){
                nutrimentFoodAmountText = getResources().getString( R.string.overview_nutriment_amount_of_goal, totalAmount, dailyGoal, foodInfoEntry.getValue().getUnit());
            }else{
                nutrimentFoodAmountText = getResources().getString( R.string.overview_nutriment_amount, totalAmount, foodInfoEntry.getValue().getUnit());
            }

            tvFoodInfoAmount.setText(nutrimentFoodAmountText);

            if(dailyGoal != null){
                styleProgressBarByFoodInfoKey(progressBar,foodInfoEntry.getKey(),totalAmount, dailyGoal);
            }

            idOfPredecessor = dailyGoal != null ? progressBar.getId() : tvFoodInfoName.getId();
        }
        if(rebuildLayout) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            constraintSet.connect(R.id.DailyList, ConstraintSet.TOP, idOfPredecessor, ConstraintSet.BOTTOM, 0);

            constraintSet.applyTo(constraintLayout);
        }

    }

    /***
     * Sets a progressBar's progress and styles its colors depending on whether the set goal has been
     * exceed or subceeded and whether this was wanted.
     * @param progressBar
     * @param foodInfoKey
     * @param amount
     * @param goal
     */
    private void styleProgressBarByFoodInfoKey(ProgressBar progressBar, String foodInfoKey,float amount, float goal){
        boolean dontExceed = FoodInfosToShow.getDontExceedDailyGoalFromPreferences(this,foodInfoKey);
        float fraction = amount/goal;
        boolean isExceeded = fraction > 1.0;

        if(isExceeded){
            progressBar.setProgress((int)(1/fraction * 100));
            if(dontExceed){
                setProgressBarProgressAndRemainingColor(progressBar, getResources().getColor(R.color.progress_too_much_allowed), getResources().getColor(R.color.progress_too_much_extent));
            }else{
                setProgressBarProgressAndRemainingColor(progressBar, getResources().getColor(R.color.progress_enough_allowed), getResources().getColor(R.color.progress_enough_extent));
            }
        }else {
            progressBar.setProgress((int)(fraction * 100));
            if(dontExceed){
                progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.progress_enough_allowed)));
            }else{
                progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.progress_too_much_allowed)));
            }
        }
    }

    /***
     * Changes a progress bars progress-part to colorProgress and the remaining part of the bar to colorRemaining.
     * @param progressBar
     * @param colorProgress
     * @param colorRemaining
     */
    private void setProgressBarProgressAndRemainingColor(ProgressBar progressBar, int colorProgress, int colorRemaining) {
        progressBar.setProgressTintList(ColorStateList.valueOf(colorProgress));
        //parts of the progressbar are accessable as a layerd list/LayerDrawable
        LayerDrawable layers = (LayerDrawable) progressBar.getProgressDrawable().mutate();

        layers.getDrawable(0).setTintMode(PorterDuff.Mode.SRC);
        layers.getDrawable(0).setTint(colorRemaining);

    }

    /***
     * Removes all views that are in maps and then clears the maps. Call if new foodInfos shall be shown
     * or something about the daily goals changes
     */
    private void clearLayoutAndMaps() {
        ConstraintLayout constraintLayout = findViewById(R.id.overviewContentConstraintLayout);
        for(Map.Entry<String, TextView> tvEntry : tvFoodInfoAmountMap.entrySet()){
            constraintLayout.removeView(tvEntry.getValue());
        }
        for(Map.Entry<String, TextView> tvEntry : tvFoodInfoNameMap.entrySet()){
            constraintLayout.removeView(tvEntry.getValue());
        }
        for(Map.Entry<String, ProgressBar> pbEntry : pbFoodInfoProgressMap.entrySet()){
            constraintLayout.removeView(pbEntry.getValue());
        }
        tvFoodInfoAmountMap.clear();
        tvFoodInfoNameMap.clear();
        pbFoodInfoProgressMap.clear();
    }

    /**
     * java.util.Date object for the activity
     *
     * @return The Date object created from the date long variable.
     */
    private Date getDateForActivity() {
        Date d = new Date();
        d.setTime(date);
        return d;
    }

    /**
     * formats a given Date in dd.MM.yyyy format
     *
     * @param d the Date object to format
     * @return a String containing the formatted date
     */
    private String getFormattedDate(Date d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = "";
        try {
            formattedDate = dateFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    /**
     * sets up the floating action button to add an entry
     */
    private void setUpFloatingActionButton() {
        FloatingActionButton fab = this.findViewById(R.id.addFoodItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverviewActivity.this, BaseAddFoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("DATE", date);
                startActivity(intent);
            }

        });

        FloatingActionButton cancelDeleteFab = this.findViewById(R.id.cancelDeleteFoodItem);
        cancelDeleteFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unselectAllCards();

                //toggleDeletionMenuVisibility();
            }
        });

        FloatingActionButton deleteFab = this.findViewById(R.id.deleteFoodItem);
        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedCards();
                selectedCards = 0;
                toggleDeletionMenuVisibility();
            }

        });

    }

    /**
     * The consumed energy per entry
     *
     * @param e a DatabaseEntry
     * @return the calculated consumed calories
     */
    private float getConsumedCaloriesForEntry(DatabaseEntry e) {
        // energy is kCal/100 so divide by 100 at the end
        return (e.amount * e.energy) / 100;
    }

    /***
     * Given a key identifying a FoodInfo (e.g. "carbs") and a db entry calculate the consumed amount
     * of the FoodInfo
     * @param key
     * @return
     */
    private float getConsumedFoodInfoAmountForEntry(DatabaseEntry e,String key, FoodInfo foodInfo) {
        return (e.amount * FoodInfosToShow.getFoodInfoValueByKey(e,key, foodInfo, getApplicationContext())) / 100;
    }
    /**
     * Creates a CheckedCardView from the entry
     *
     * @param e
     * @return
     */
    private CardView createCardViewForEntry(DatabaseEntry e) {
        // set up CardView for entry
        CheckableCardView c = new CheckableCardView(this);
        setCardViewOptions(c);

        // set up Layout that gets used inside the CardView
        ConstraintLayout cl = new ConstraintLayout(this);
        ConstraintSet set = new ConstraintSet();


        // set up Textviews for cards
        TextView name = new TextView(this);
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.slide_actions));
        TextView amount = new TextView(this);
        TextView energy = new TextView(this);
        TextView calories = new TextView(this);
        calories.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.slide_actions));
        TextView id = new TextView(this);


        // Each CardView needs an ID to reference in the ConstraintText
        name.setId(View.generateViewId());
        amount.setId(View.generateViewId());
        energy.setId(View.generateViewId());
        calories.setId(View.generateViewId());
        id.setText(Integer.toString(e.id));


        name.setText(e.name);
        amount.setText(Integer.toString(e.amount) + "g");
        energy.setText(String.format(Locale.ENGLISH, "   %.2f kCal/100g", e.energy));

        String calorieAndMacrosText = String.format(Locale.ENGLISH, "%.2f kCal",getConsumedCaloriesForEntry(e));

        for(Map.Entry<String,FoodInfo> foodInfoEntry : FoodInfosToShow.getFoodInfosShownAsMap(getApplicationContext()).entrySet()){
            float consumedAmount = getConsumedFoodInfoAmountForEntry(e,foodInfoEntry.getKey(), foodInfoEntry.getValue());
            if(consumedAmount > 0.01){
                calorieAndMacrosText += String.format(Locale.ENGLISH, "\n%1$.2f%2$s %3$s",consumedAmount,foodInfoEntry.getValue().getUnit(), foodInfoEntry.getValue().getName());
            }
        }
        calories.setText(calorieAndMacrosText);
        // id is just an invisible attribute on each card
        id.setVisibility(View.INVISIBLE);


        set.constrainWidth(amount.getId(), ConstraintSet.WRAP_CONTENT);
        set.constrainHeight(amount.getId(), ConstraintSet.WRAP_CONTENT);
        set.constrainWidth(energy.getId(), ConstraintSet.WRAP_CONTENT);
        set.constrainHeight(energy.getId(), ConstraintSet.WRAP_CONTENT);
        set.constrainWidth(calories.getId(), ConstraintSet.WRAP_CONTENT);

        cl.addView(name);
        cl.addView(calories);
        cl.addView(amount);
        cl.addView(energy);
        c.addView(cl);
        c.addView(id);


        set.connect(name.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 20);
        set.connect(name.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);
        set.connect(name.getId(), ConstraintSet.RIGHT, calories.getId(), ConstraintSet.LEFT, 40);
        set.constrainDefaultHeight(name.getId(), ConstraintSet.MATCH_CONSTRAINT_WRAP);


        set.connect(calories.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 20);
        set.connect(calories.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);
        set.constrainDefaultHeight(calories.getId(), ConstraintSet.MATCH_CONSTRAINT_WRAP);
        set.constrainDefaultWidth(calories.getId(), ConstraintSet.MATCH_CONSTRAINT_WRAP);

        int barrierId = View.generateViewId();
        set.createBarrier(barrierId,ConstraintSet.TOP,name.getId(),calories.getId());

        set.connect(amount.getId(), ConstraintSet.TOP, barrierId, ConstraintSet.BOTTOM, 20);
        set.connect(amount.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 20);
        set.connect(amount.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);

        set.connect(energy.getId(), ConstraintSet.TOP, barrierId, ConstraintSet.BOTTOM, 20);
        set.connect(energy.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 20);
        set.connect(energy.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);

        set.applyTo(cl);

        setListenersForCardView(c, e);
        return c;
    }



    /**
     * Set up listeners for whenever an entry is touched
     * @param c the cardview
     * @param e the entry belonging to the card
     */
    private void setListenersForCardView(CardView c, DatabaseEntry e) {
        // need to make final to use in Listener methods
        final DatabaseEntry entry = e;
        c.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int newSelectedCards = countSelectedCards();
                if (newSelectedCards == 0) {
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
                    LayoutInflater factory = LayoutInflater.from(OverviewActivity.this);
                    final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(OverviewActivity.this);

                    deleteDialog.setTitle(getResources().getString(R.string.edit_entry));
                    //deleteDialog.setMessage("Message");
                    final EditText input = new EditText(OverviewActivity.this);
                        input.setText(String.valueOf(entry.amount));
                    InputFilter[] fa= new InputFilter[1];
                    fa[0] = new InputFilter.LengthFilter(5);
                    input.setFilters(fa);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    deleteDialog.setView(input);

                    deleteDialog.setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String amountField = input.getText().toString();
                            if(amountField.length() != 0 ){
                                editDatabaseEntry(amountField, entry.id);
                            }
                        }
                    });
                    deleteDialog.setNegativeButton(getResources().getString(R.string.decline), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //deleteDialog.dismiss();
                        }
                    });
                    deleteDialog.show();

                  /*  EditText amountField = deleteDialogView.findViewById(R.id.input_amount);
                    amountField.setText("");
                    amountField.requestFocus();
                    Log.d("OverviewActivity", String.valueOf(entry.id));
                    deleteDialog.show();*/
                }


            }
        });
    }

    /**
     * Edit a database entry
     * @param amountString the new amount
     * @param id the id of the entry
     * @return true if the entry was successful
     */
    private void editDatabaseEntry(String amountString, int id) {
        int amount = Integer.parseInt(amountString);
        viewModel.editEntryById(id, amount);
    }

    /**
     * refresh the list of entries
     */
    private void refreshFoodList(List<DatabaseEntry> entries) {
        if(entries != null) {
            ViewGroup foodList = getEntryList();
            foodList.removeAllViews();
            for (DatabaseEntry e : entries) {
                CardView c = createCardViewForEntry(e);
                foodList.addView(c);
            }
        }
    }

    /**
     * set up the 'back' button
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // We want to show the 'back' button in the app
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    /**
     * Count how many cards are currently selected. Important for determining what menus to show.
     * @return the number of cards selected
     */
    private int countSelectedCards() {
        int count = 0;
        ViewGroup foodList = getEntryList();
        for (int i = 0; i < foodList.getChildCount(); i++) {
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

    /**
     * Set up cardview
     * @param c a cardview
     */
    private void setCardViewOptions(CheckableCardView c) {
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
        int[] attrs = new int[]{android.R.attr.selectableItemBackground};
        TypedArray ta = obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0);
        c.setForeground(drawableFromTheme);
    }
}
