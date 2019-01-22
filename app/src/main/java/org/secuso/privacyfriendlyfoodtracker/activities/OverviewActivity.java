package org.secuso.privacyfriendlyfoodtracker.activities;

import org.secuso.privacyfriendlyfoodtracker.activities.adapter.DatabaseEntry;
import org.secuso.privacyfriendlyfoodtracker.activities.adapter.DatabaseFacade;
import org.secuso.privacyfriendlyfoodtracker.customviews.CheckableCardView;
import org.secuso.privacyfriendlyfoodtracker.R;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;

public class OverviewActivity extends AppCompatActivity {

    // The date for the activity. This is tied to each OverviewActivity as
    // each OverviewActivity is created as an overview for a separate date
    long date;
    // Keeps track of selected Entries / CardViews
    // This is important for deciding whether to display the delete dialog
    // I.e. if more than 0 cards are selected, the user should be able to delete them
    private int selectedCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOverview);
        setSupportActionBar(toolbar);

        // Set up global variables
        // Set the date. If no date is passed on, the system date is chosen by default.
        Intent intent = getIntent();
        date = intent.getLongExtra("DATE", System.currentTimeMillis());
        selectedCards = 0;

        setUpFloatingActionButton();
        setupActionBar();
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshFoodList();
        refreshTotalCalorieCounter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cancel_deletion:
                unselectAllCards();
                break;
            case R.id.action_confirm_deletion:
                deleteSelectedCards();
                break;
            default:
                finish();
                break;
        }
        selectedCards = 0;
        toggleDeletionMenuVisibility();
        refreshTotalCalorieCounter();
        return true;
    }

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
     * @param isVisible The visibility. false for invisible, true for visible.
     * @param menu The deletion menu
     */
    private void setMenuVisibility(boolean isVisible, Menu menu){
        MenuItem del = menu.findItem(R.id.action_cancel_deletion);
        MenuItem conf = menu.findItem(R.id.action_confirm_deletion);
        del.setVisible(isVisible);
        del.setEnabled(isVisible);
        conf.setVisible(isVisible);
        conf.setEnabled(isVisible);

    }

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

    /**
     * toggles deletion menu visibility.
     */
    private void toggleDeletionMenuVisibility() {
        // Causes onCreateOptionsMenu to be called and the menu to be inflated
        invalidateOptionsMenu();
    }

    /**
     * deletes all currently selected cards and associatedEntries
     */
    private void deleteSelectedCards() {
        ViewGroup foodList = getEntryList();
        List<View> cardsToRemove = new LinkedList<>();
        for (int i = 0; i<foodList.getChildCount(); i++){
            View v = foodList.getChildAt(i);
            if (v instanceof CheckableCardView) {
                CheckableCardView c = (CheckableCardView) v;
                if (c.isChecked()) {
                    TextView idView =(TextView) c.getChildAt(1);
                    String id = idView.getText().toString();
                    DatabaseFacade facade = getDbFacade();
                    facade.deleteEntryById(Integer.parseInt(id));
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
     * @return A ViewGroup containing all CheckedCardViews
     */
    private ViewGroup getEntryList(){
        LinearLayout foodlist = this.findViewById(R.id.DailyList);
        return foodlist;
    }

    /**
     * creates a database facade that can be used to call database functions
     * @return a DatabaseFacade object
     */
    private DatabaseFacade getDbFacade(){
        DatabaseFacade facade;
        try {
            facade = new DatabaseFacade(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return facade;
    }

    /**
     * Refreshes the calorie counter at the top of the activity.
     * Recounts calories for all Entries of the day.
     */
    private void refreshTotalCalorieCounter() {
        int totalCalories = 0;
        TextView heading = this.findViewById(R.id.overviewHeading);
        String cal =  getString(R.string.total_calories);
        Date d = getDateForActivity();
        String formattedDate = getFormattedDate(d);
        DatabaseFacade facade = getDbFacade();
        DatabaseEntry[] entries = facade.getEntriesForDay(d);
        for(DatabaseEntry e : entries) {
            totalCalories += (e.energy * e.amount) / 100;
        }
        heading.setText(formattedDate + ": " + totalCalories + " " + cal);
    }

    /**
     * java.util.Date object for the activity
     * @return The Date object created from the date long variable.
     */
    private Date getDateForActivity(){
        Date d = new Date();
        d.setTime(date);
        return d;
    }

    /**
     * formats a given Date in dd.MM.yyyy format
     * @param d the Date object to format
     * @return a String containing the formatted date
     */
    private String getFormattedDate(Date d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = "";
        try{
            formattedDate = dateFormat.format(d);
        } catch(Exception e){
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
    }

    /**
     * The consumed energy per entry
     * @param e a DatabaseEntry
     * @return the calculated consumed calories
     */
    private int getConsumedCaloriesForEntry(DatabaseEntry e) {
        // energy is kCal/100 so divide by 100 at the end
        int consumedEnergy = (e.amount*e.energy)/100;
        return consumedEnergy;
    }

    /**
     * Creates a CheckedCardView from the entry
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
        TextView amount = new TextView(this);
        TextView energy = new TextView(this);
        TextView calories = new TextView(this);
        TextView id = new TextView(this);

        // Each CardView needs an ID to reference in the ConstraintText
        name.setId(View.generateViewId());
        amount.setId(View.generateViewId());
        energy.setId(View.generateViewId());
        calories.setId(View.generateViewId());
        id.setText(e.id);

        name.setText(e.name);
        amount.setText(Integer.toString(e.amount) + "g");
        energy.setText(Integer.toString(e.energy) + " kCal/100");
        calories.setText(Integer.toString(getConsumedCaloriesForEntry(e)) + " kCal");
        // id is just an invisible attribute on each card
        id.setVisibility(View.INVISIBLE);

        set.constrainWidth(name.getId(), ConstraintSet.WRAP_CONTENT);
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

        set.connect(calories.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 20);
        set.connect(calories.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);

        set.connect(amount.getId(), ConstraintSet.TOP, calories.getId(), ConstraintSet.BOTTOM, 20);
        set.connect(amount.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 20);
        set.connect(amount.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);

        set.connect(energy.getId(), ConstraintSet.TOP, name.getId(), ConstraintSet.BOTTOM, 20);
        set.connect(energy.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 20);
        set.connect(energy.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);

        set.applyTo(cl);

        setListenersForCardView(c, e);
        return c;
    }

    private void setListenersForCardView(CardView c, DatabaseEntry e){
        // need to make final to use in Listener methods
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

    private void refreshFoodList() {
        Date d = getDateForActivity();
        DatabaseFacade facade = getDbFacade();
        DatabaseEntry[] entries = facade.getEntriesForDay(d);
        ViewGroup foodList = getEntryList();
        foodList.removeAllViews();
        for(DatabaseEntry e : entries) {
            CardView c = createCardViewForEntry(e);
            foodList.addView(c);
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // We want to show the 'back' button in the app
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private int countSelectedCards(){
        int count = 0;
        ViewGroup foodList = getEntryList();
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
}
