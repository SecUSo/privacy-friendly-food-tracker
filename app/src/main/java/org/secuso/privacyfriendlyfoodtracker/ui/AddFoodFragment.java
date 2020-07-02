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

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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
    EditText carbsField;
    EditText sugarField;
    EditText proteinField;
    EditText fatField;
    EditText satFatField;
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
        sharedStatisticViewModel = ViewModelProviders.of(getActivity()).get(SharedStatisticViewModel.class);
        try {
            databaseFacade = new DatabaseFacade(referenceActivity.getApplicationContext());
        } catch (Exception e){
            Log.e("Error", e.getMessage());
        }
        InputFilter[] amountFilter = { new InputFilter.LengthFilter(6) };
        InputFilter[] caloriesFilter = { new InputFilter.LengthFilter(10) };

        amountField = parentHolder.findViewById(R.id.input_amount);
        caloriesField = parentHolder.findViewById(R.id.input_calories);
        carbsField = parentHolder.findViewById(R.id.input_carbs);
        sugarField = parentHolder.findViewById(R.id.input_sugar);
        proteinField = parentHolder.findViewById(R.id.input_protein);
        fatField = parentHolder.findViewById(R.id.input_fat);
        satFatField = parentHolder.findViewById(R.id.input_satFat);

        amountField.setFilters(amountFilter);
        amountField.setInputType(InputType.TYPE_CLASS_NUMBER);

        caloriesField.setFilters(caloriesFilter);
        carbsField.setFilters(caloriesFilter);
        sugarField.setFilters(caloriesFilter);
        proteinField.setFilters(caloriesFilter);
        fatField.setFilters(caloriesFilter);
        satFatField.setFilters(caloriesFilter);


        FloatingActionButton fab = parentHolder.findViewById(R.id.addEntry);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameField = parentHolder.findViewById(R.id.input_food);
                String name = nameField.getText().toString();

                String amount = amountField.getText().toString();

                String calories = caloriesField.getText().toString();

                String carbs = carbsField.getText().toString();

                String sugar = sugarField.getText().toString();

                String protein = proteinField.getText().toString();

                String fat = fatField.getText().toString();

                String satFat = satFatField.getText().toString();


                //replace empty macro slots with "0"
                if("".equals(carbs)){
                    carbs="0";
                }
                if("".equals(sugar)){
                    sugar="0";
                }
                if("".equals(protein)){
                    protein="0";
                }
                if("".equals(fat)){
                    fat="0";
                }
                if("".equals(satFat)){
                    satFat="0";
                }
                // validation
                boolean validated = validateResponses(name, amount, calories, carbs, sugar, protein, fat, satFat, view);

                if(validated) {
                    boolean entrySuccessful = makeDatabaseEntry(name, amount, calories, carbs, sugar, protein, fat, satFat);
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
            EditText carbsField = parentHolder.findViewById(R.id.input_carbs);
            EditText sugarField = parentHolder.findViewById(R.id.input_sugar);
            EditText proteinField = parentHolder.findViewById(R.id.input_protein);
            EditText fatField = parentHolder.findViewById(R.id.input_fat);
            EditText satFatField = parentHolder.findViewById(R.id.input_satFat);


            nameField.setText(referenceActivity.name);
            nameField.setFocusable(false);
            nameField.setClickable(false);
            nameField.setTextColor(getResources().getColor(R.color.middlegrey));

            caloriesField.setText(String.format(Locale.ENGLISH, "%.2f", referenceActivity.calories));
            caloriesField.setFocusable(false);
            caloriesField.setClickable(false);
            caloriesField.setTextColor(getResources().getColor(R.color.middlegrey));

            carbsField.setText(String.format(Locale.ENGLISH, "%.2f", referenceActivity.carbs));
            carbsField.setFocusable(false);
            carbsField.setClickable(false);
            carbsField.setTextColor(getResources().getColor(R.color.middlegrey));

            sugarField.setText(String.format(Locale.ENGLISH, "%.2f", referenceActivity.sugar));
            sugarField.setFocusable(false);
            sugarField.setClickable(false);
            sugarField.setTextColor(getResources().getColor(R.color.middlegrey));

            proteinField.setText(String.format(Locale.ENGLISH, "%.2f", referenceActivity.protein));
            proteinField.setFocusable(false);
            proteinField.setClickable(false);
            proteinField.setTextColor(getResources().getColor(R.color.middlegrey));

            fatField.setText(String.format(Locale.ENGLISH, "%.2f", referenceActivity.fat));
            fatField.setFocusable(false);
            fatField.setClickable(false);
            fatField.setTextColor(getResources().getColor(R.color.middlegrey));

            satFatField.setText(String.format(Locale.ENGLISH, "%.2f", referenceActivity.satFat));
            satFatField.setFocusable(false);
            satFatField.setClickable(false);
            satFatField.setTextColor(getResources().getColor(R.color.middlegrey));

        } else if (isVisible && !referenceActivity.productSet) {
            EditText nameField = parentHolder.findViewById(R.id.input_food);
            EditText caloriesField = parentHolder.findViewById(R.id.input_calories);
            EditText carbsField = parentHolder.findViewById(R.id.input_carbs);
            EditText sugarField = parentHolder.findViewById(R.id.input_sugar);
            EditText proteinField = parentHolder.findViewById(R.id.input_protein);
            EditText fatField = parentHolder.findViewById(R.id.input_fat);
            EditText satFatField = parentHolder.findViewById(R.id.input_satFat);

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

            carbsField.setText("");
            carbsField.setFocusable(true);
            carbsField.setFocusableInTouchMode(true);
            carbsField.setClickable(true);
            carbsField.setTextColor(getResources().getColor(R.color.black));

            sugarField.setText("");
            sugarField.setFocusable(true);
            sugarField.setFocusableInTouchMode(true);
            sugarField.setClickable(true);
            sugarField.setTextColor(getResources().getColor(R.color.black));

            proteinField.setText("");
            proteinField.setFocusable(true);
            proteinField.setFocusableInTouchMode(true);
            proteinField.setClickable(true);
            proteinField.setTextColor(getResources().getColor(R.color.black));

            fatField.setText("");
            fatField.setFocusable(true);
            fatField.setFocusableInTouchMode(true);
            fatField.setClickable(true);
            fatField.setTextColor(getResources().getColor(R.color.black));

            satFatField.setText("");
            satFatField.setFocusable(true);
            satFatField.setFocusableInTouchMode(true);
            satFatField.setClickable(true);
            satFatField.setTextColor(getResources().getColor(R.color.black));
        }
    }

    /**
     * Create a new db entry
     * @param name Name of the food
     * @param amountString amount in g
     * @param caloriesString calories per 100g
     * @return true if successful
     */
    private boolean makeDatabaseEntry(String name, String amountString, String caloriesString, String carbsString, String sugarString, String proteinString, String fatString, String satFatString) {
        try {
            int amount = Integer.parseInt(amountString);
            float calories = Float.parseFloat(caloriesString);
            float carbs = Float.parseFloat(carbsString);
            float sugar = Float.parseFloat(sugarString);
            float protein = Float.parseFloat(proteinString);
            float fat = Float.parseFloat(fatString);
            float satFat = Float.parseFloat(satFatString);

            // We haven't explicitly chosen a product so the productId is 0 for unknown
            databaseFacade.insertEntry(amount, ((BaseAddFoodActivity) referenceActivity).date, name, calories, carbs, sugar, protein, fat, satFat, 0);
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
    private boolean validateResponses(String name, String amount, String calories, String carbs, String sugar, String protein, String fat, String satFat, View view) {
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
        try {
            Float.parseFloat(carbs);
        } catch (NumberFormatException e) {
            showErrorMessage(referenceActivity.findViewById(R.id.inputCalories), R.string.error_carbs_nan);
            return false;
        }
        try {
            Float.parseFloat(protein);
        } catch (NumberFormatException e) {
            showErrorMessage(referenceActivity.findViewById(R.id.inputCalories), R.string.error_protein_nan);
            return false;
        }
        try {
            Float.parseFloat(fat);
        } catch (NumberFormatException e) {
            showErrorMessage(referenceActivity.findViewById(R.id.inputCalories), R.string.error_fat_nan);
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
