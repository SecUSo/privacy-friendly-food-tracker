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
import org.secuso.privacyfriendlyfoodtracker.activities.adapter.DatabaseAdapter;
import org.secuso.privacyfriendlyfoodtracker.activities.adapter.DatabaseEntry;

import java.text.ParseException;
import java.util.Date;

public class FoodActivity extends AppCompatActivity {

    private Date date;
    private int EDIT = 1;
    private int NEWENTRY = 0;
    private int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        final String id = intent.getStringExtra("ID");

        // default is new entry
        mode = NEWENTRY;
        if (!("".equals(id)) && null != id){
            mode = EDIT;
            getSupportActionBar().setTitle(R.string.edit_entry);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addEntry);
            fab.setImageResource(R.drawable.button_confirm);
        }

        if( mode == NEWENTRY) {
            long dateLong = intent.getLongExtra("DATE", System.currentTimeMillis());
            date = new Date();
            date.setTime(dateLong);
        } else if (mode == EDIT){
            EditText nameField = findViewById(R.id.input_food);
            nameField.setText(intent.getStringExtra("NAME"));

            EditText amountField = findViewById(R.id.input_amount);
            amountField.setText(Integer.toString(intent.getIntExtra("AMOUNT", 0)));

            EditText caloriesField = findViewById(R.id.input_calories);
            caloriesField.setText(Integer.toString(intent.getIntExtra("CALORIES", 0)));
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addEntry);
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
                    if(mode == NEWENTRY){
                        entrySuccessful = makeDatabaseEntry(name, amount, calories);
                    }else if (mode == EDIT){
                        entrySuccessful = editDatabaseEntry(name, amount, calories, id);
                    }
                    if (!entrySuccessful){
                        Snackbar.make(view, R.string.error_database, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        finish();
                    }
                }


            }

            });
        setupActionBar();


    }

    private boolean editDatabaseEntry(String name, String amountString, String caloriesString, String id){
        DatabaseAdapter da = new DatabaseAdapter();
        int amount = Integer.parseInt(amountString);
        int calories = Integer.parseInt(caloriesString);
        return da.editEntry(id, name, amount, calories);
    }

    private boolean makeDatabaseEntry(String name, String amountString, String caloriesString) {
        DatabaseAdapter da = new DatabaseAdapter();
        int amount = Integer.parseInt(amountString);
        int calories = Integer.parseInt(caloriesString);
        return da.insertEntry(name, amount, calories, date);

    }
    private boolean validateResponses(String name, String amount, String calories, View view) {
        if("".equals(name)){
            Snackbar.make(view, R.string.error_food_missing, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        } else if ("".equals(amount)) {
            Snackbar.make(view, R.string.error_amount_missing, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        } else if ("".equals(calories)) {
            Snackbar.make(view, R.string.error_calories_missing, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        try {
            Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            Snackbar.make(view, R.string.error_amount_nan, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        try {
            Integer.parseInt(calories);
        } catch (NumberFormatException e) {
            Snackbar.make(view, R.string.error_calories_nan, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}
