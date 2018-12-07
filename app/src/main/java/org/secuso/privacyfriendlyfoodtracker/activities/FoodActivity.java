package org.secuso.privacyfriendlyfoodtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.activities.adapter.DatabaseFacade;

import java.util.Date;

public class FoodActivity extends AppCompatActivity {

    private Date date;
    private int EDIT = 1;
    private int NEW_ENTRY = 0;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        final String id = intent.getStringExtra("ID");

        // default is new entry
        mode = NEW_ENTRY;
        if (!("".equals(id)) && null != id){
            mode = EDIT;
            getSupportActionBar().setTitle(R.string.edit_entry);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addEntry);
            fab.setImageResource(R.drawable.button_confirm);
        }

        if( mode == NEW_ENTRY) {
            long dateLong = intent.getLongExtra("DATE", System.currentTimeMillis());
            date = new Date();
            date.setTime(dateLong);
        } else if (mode == EDIT){
            EditText nameField = findViewById(R.id.input_food);
            nameField.setText(intent.getStringExtra("NAME"));

            EditText amountField = findViewById(R.id.input_amount);
            amountField.setText(Integer.toString(intent.getIntExtra("AMOUNT", 0)));
            amountField.requestFocus();

            EditText caloriesField = findViewById(R.id.input_calories);
            caloriesField.setText(Integer.toString(intent.getIntExtra("CALORIES", 0)));

            nameField.setKeyListener(null);
            nameField.setClickable(false);
            nameField.setFocusable(false);
            nameField.setTextColor(getResources().getColor(R.color.middlegrey));
            nameField.setHighlightColor(getResources().getColor(R.color.middlegrey));

            caloriesField.setKeyListener(null);
            caloriesField.setClickable(false);
            caloriesField.setFocusable(false);
            caloriesField.setTextColor(getResources().getColor(R.color.middlegrey));
            caloriesField.setHighlightColor(getResources().getColor(R.color.middlegrey));
        }


        FloatingActionButton fab = findViewById(R.id.addEntry);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameField = findViewById(R.id.input_food);
                String name = nameField.getText().toString();

                EditText amountField = findViewById(R.id.input_amount);
                String amount = amountField.getText().toString();

                EditText caloriesField = findViewById(R.id.input_calories);
                String calories = caloriesField.getText().toString();

                // validation
                boolean validated = validateResponses(name, amount, calories, view);

                if(validated) {
                    boolean entrySuccessful = false;
                    if(mode == NEW_ENTRY){
                        entrySuccessful = makeDatabaseEntry(name, amount, calories);
                    }else if (mode == EDIT){
                        entrySuccessful = editDatabaseEntry(amount, id);
                    }
                    if (!entrySuccessful){
                        showErrorMessage(view, R.string.error_database);
                    } else {
                        finish();
                    }
                }
            }
            });
        setupActionBar();


    }

    private boolean editDatabaseEntry(String amountString, String idString){
        DatabaseFacade facade = getDbFacade();
        int amount = Integer.parseInt(amountString);
        int id = Integer.parseInt(idString);
        return facade.editEntryById(id, amount);
    }

    private boolean makeDatabaseEntry(String name, String amountString, String caloriesString) {
        try {
            DatabaseFacade df = new DatabaseFacade(this);
            int amount = Integer.parseInt(amountString);
            int calories = Integer.parseInt(caloriesString);
            // We haven't explicitly chosen a product so the productId is 0 for unknown
            df.insertEntry(amount, date, name, calories, 0);
        } catch (Exception e) {
            // something went wrong so the entry wasn't successful
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean validateResponses(String name, String amount, String calories, View view) {
        if("".equals(name)){
            showErrorMessage(view, R.string.error_food_missing);
            return false;
        } else if ("".equals(amount)) {
            showErrorMessage(view, R.string.error_amount_missing);
            return false;
        } else if ("".equals(calories)) {
            showErrorMessage(view, R.string.error_calories_missing);
            return false;
        }

        try {
            Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            showErrorMessage(view, R.string.error_amount_nan);
            return false;
        }

        try {
            Integer.parseInt(calories);
        } catch (NumberFormatException e) {
            showErrorMessage(view, R.string.error_calories_nan);
            return false;
        }

        return true;
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showErrorMessage(View view, int errorMessageId){
        Snackbar.make(view, errorMessageId, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}
