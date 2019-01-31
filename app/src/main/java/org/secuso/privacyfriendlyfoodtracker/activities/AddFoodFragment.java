package org.secuso.privacyfriendlyfoodtracker.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.view.ViewTreeObserver;
import android.graphics.Rect;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.activities.adapter.DatabaseFacade;
import org.secuso.privacyfriendlyfoodtracker.activities.helper.DateHelper;
import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntrieAndProductDao;
import org.secuso.privacyfriendlyfoodtracker.viewmodels.SharedStatisticViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass. Contains a possibility to add a new entry.
 *
 * @author Simon Reinkemeier
 */
public class AddFoodFragment extends Fragment {
    SharedStatisticViewModel sharedStatisticViewModel;
    BaseAddFoodActivity referenceActivity;
    View parentHolder;
    TextView textView;
    DatabaseFacade databaseFacade;

    public AddFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        referenceActivity = (BaseAddFoodActivity) getActivity();
        parentHolder = inflater.inflate(R.layout.content_food, container, false);
        sharedStatisticViewModel = ViewModelProviders.of(getActivity()).get(SharedStatisticViewModel.class);
        try {
            databaseFacade = new DatabaseFacade(referenceActivity.getApplicationContext());
        } catch (Exception e){
            Log.e("Error", e.getMessage());
        }


        FloatingActionButton fab = parentHolder.findViewById(R.id.addEntry);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameField = parentHolder.findViewById(R.id.input_food);
                String name = nameField.getText().toString();

                EditText amountField = parentHolder.findViewById(R.id.input_amount);
                String amount = amountField.getText().toString();

                EditText caloriesField = parentHolder.findViewById(R.id.input_calories);
                String calories = caloriesField.getText().toString();

                // validation
                boolean validated = validateResponses(name, amount, calories, view);

                if(validated) {
                    boolean entrySuccessful = makeDatabaseEntry(name, amount, calories);
                    if (!entrySuccessful){
                        showErrorMessage(view, R.string.error_database);
                    } else {
                        referenceActivity.finish();
                    }
                }
            }
        });


        return parentHolder;
    }

    @Override
    public void setUserVisibleHint(boolean isVisible) {
        super.setUserVisibleHint(isVisible);
        if(isVisible && referenceActivity.productSet) {
            EditText nameField = parentHolder.findViewById(R.id.input_food);
            EditText caloriesField = parentHolder.findViewById(R.id.input_calories);
            nameField.setText(referenceActivity.name);
            nameField.setFocusable(false);
            nameField.setClickable(false);
            nameField.setTextColor(getResources().getColor(R.color.middlegrey));
            caloriesField.setText(Integer.toString(referenceActivity.calories));
            caloriesField.setFocusable(false);
            caloriesField.setClickable(false);
            caloriesField.setTextColor(getResources().getColor(R.color.middlegrey));
        } else if (isVisible && !referenceActivity.productSet) {
            EditText nameField = parentHolder.findViewById(R.id.input_food);
            EditText caloriesField = parentHolder.findViewById(R.id.input_calories);
            nameField.setText("");
            nameField.setFocusable(true);
            nameField.setFocusableInTouchMode(true);
            nameField.setClickable(true);
            nameField.setTextColor(getResources().getColor(R.color.black));
            caloriesField.setText("");
            caloriesField.setFocusable(true);
            caloriesField.setFocusableInTouchMode(true);
            caloriesField.setClickable(true);
            caloriesField.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private boolean makeDatabaseEntry(String name, String amountString, String caloriesString) {
        try {
            int amount = Integer.parseInt(amountString);
            int calories = Integer.parseInt(caloriesString);
            // We haven't explicitly chosen a product so the productId is 0 for unknown
            databaseFacade.insertEntry(amount, ((BaseAddFoodActivity) referenceActivity).date, name, calories, 0);
        } catch (Exception e) {
            // something went wrong so the entry wasn't successful
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean validateResponses(String name, String amount, String calories, View view) {
        if("".equals(name)){
            showErrorMessage(referenceActivity.findViewById(R.id.inputFoodName), R.string.error_food_missing);
            return false;
        } else if ("".equals(amount)) {
            showErrorMessage(referenceActivity.findViewById(R.id.inputFoodAmount), R.string.error_amount_missing);
            return false;
        } else if ("".equals(calories)) {
            showErrorMessage(referenceActivity.findViewById(R.id.inputCalories), R.string.error_calories_missing);
            return false;
        }

        try {
            Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            showErrorMessage(referenceActivity.findViewById(R.id.inputFoodAmount), R.string.error_amount_nan);
            return false;
        }

        try {
            Integer.parseInt(calories);
        } catch (NumberFormatException e) {
            showErrorMessage(referenceActivity.findViewById(R.id.inputCalories), R.string.error_calories_nan);
            return false;
        }

        return true;
    }

    private void showErrorMessage(View view, int errorMessageId){
        // reset error messages
        ((TextInputLayout)referenceActivity.findViewById(R.id.inputFoodName)).setError("");
        ((TextInputLayout)referenceActivity.findViewById(R.id.inputCalories)).setError("");
        ((TextInputLayout)referenceActivity.findViewById(R.id.inputFoodAmount)).setError("");

        // if the view that this is called on is a TextInputlayout, we can show the error on the TextinputLayout
        if(view instanceof TextInputLayout){
            TextInputLayout til = (TextInputLayout) view;
            til.setError(getString(errorMessageId));
        } else {
            //otherwise show a generic error message
            Snackbar.make(view, errorMessageId, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

}
