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

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseFacade;
import org.secuso.privacyfriendlyfoodtracker.ui.viewmodels.SharedStatisticViewModel;

import java.util.Locale;


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
    EditText amountField;
    EditText caloriesField;
    TextView totalCaloriesField;

    /**
     * The required empty public constructor
     */
    public AddFoodFragment() {
        // Required empty public constructor
    }

    /**
     * Called when the view is created
     * @param inflater the inflater
     * @param container the container
     * @param savedInstanceState the saved instance state
     * @return the view that is shown in the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        referenceActivity = (BaseAddFoodActivity) getActivity();
        parentHolder = inflater.inflate(R.layout.content_food, container, false);
        sharedStatisticViewModel = new ViewModelProvider(getActivity()).get(SharedStatisticViewModel.class);
        try {
            databaseFacade = new DatabaseFacade(referenceActivity.getApplicationContext());
        } catch (Exception e){
            Log.e("Error", e.getMessage());
        }
        InputFilter[] amountFilter = { new InputFilter.LengthFilter(6) };
        InputFilter[] caloriesFilter = { new InputFilter.LengthFilter(10) };

        amountField = parentHolder.findViewById(R.id.input_amount);
        caloriesField = parentHolder.findViewById(R.id.input_calories);
        amountField.setFilters(amountFilter);
        amountField.setInputType(InputType.TYPE_CLASS_NUMBER);
        amountField.addTextChangedListener(fieldChangedWatcher);
        caloriesField.setFilters(caloriesFilter);
        caloriesField.addTextChangedListener(fieldChangedWatcher);
        FloatingActionButton fab = parentHolder.findViewById(R.id.addEntry);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameField = parentHolder.findViewById(R.id.input_food);
                String name = nameField.getText().toString();

                String amount = amountField.getText().toString();

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

        totalCaloriesField = parentHolder.findViewById(R.id.total_calories);

        return parentHolder;
    }

    public void updateTotalCaloriesField() {
        String message = "";

        try {
            int amount = Integer.parseInt(amountField.getText().toString());
            double caloriesPerOneHundredG = Double.parseDouble(caloriesField.getText().toString());
            message = String.format(Locale.ENGLISH, "%,.2f", caloriesPerOneHundredG * (amount / 100.0));
        } catch (NumberFormatException e) {
            message = "~";
        }

        totalCaloriesField.setText(String.format(getString(R.string.total_calories_n), message));
    }

    private final TextWatcher fieldChangedWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            updateTotalCaloriesField();
        }
    };

    /**
     * Called when the fragment is made visible to set correct presets for
     * amount and food input
     * @param isVisible indicates the visibility of the fragment
     */
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
            caloriesField.setText(String.format(Locale.ENGLISH, "%.2f", referenceActivity.calories));
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

    /**
     * Create a new db entry
     * @param name Name of the food
     * @param amountString amount in g
     * @param caloriesString calories per 100g
     * @return true if successful
     */
    private boolean makeDatabaseEntry(String name, String amountString, String caloriesString) {
        try {
            int amount = Integer.parseInt(amountString);
            float calories = Float.parseFloat(caloriesString);
            // We haven't explicitly chosen a product so the productId is 0 for unknown
            databaseFacade.insertEntry(amount, ((BaseAddFoodActivity) referenceActivity).date, name, calories, 0);
        } catch (Exception e) {
            // something went wrong so the entry wasn't successful
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Validates the inputs that a user has given
     * @param name name of the product
     * @param amount amount in g
     * @param calories calories per 100g
     * @param view the view
     * @return returns true is all entries are valid
     */
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
            Float.parseFloat(calories);
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
