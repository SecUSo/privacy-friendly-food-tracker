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
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseFacade;
import org.secuso.privacyfriendlyfoodtracker.ui.viewmodels.SharedStatisticViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


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

    Map<String,TextInputLayout> otherFoodInfoTextInputLayouts = new HashMap<>();
    Map<String, EditText> otherFoodInfoEditTexts = new HashMap<>();

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


        ConstraintLayout constraintLayout = parentHolder.findViewById(R.id.addFoodFieldLayout);

        int idOfPredecessor = R.id.inputCalories;
        for(Map.Entry<String,FoodInfo> foodInfoEntry: FoodInfosToShow.getFoodInfosShownAsMap(getContext()).entrySet()){
            FoodInfo foodInfo = foodInfoEntry.getValue();
            TextInputLayout currentTIL = new TextInputLayout(getContext());
            currentTIL.setId(View.generateViewId());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            currentTIL.setLayoutParams(layoutParams);

            EditText currentET = new EditText(getContext());
            currentET.setLayoutParams(layoutParams);
            currentET.setMaxLines(1);
            currentET.setFilters(caloriesFilter);
            currentET.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


            currentTIL.setHint(foodInfo.getName() + " in " + foodInfo.getUnit());

            currentTIL.addView(currentET);
            constraintLayout.addView(currentTIL);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            constraintSet.connect(currentTIL.getId(), ConstraintSet.TOP, idOfPredecessor, ConstraintSet.BOTTOM, 0);
            constraintSet.applyTo(constraintLayout);

            idOfPredecessor = currentTIL.getId();

            otherFoodInfoEditTexts.put(foodInfoEntry.getKey(),currentET);
            otherFoodInfoTextInputLayouts.put(foodInfoEntry.getKey(), currentTIL);
            //System.out.println("added" + foodInfo.getName() + " in " + foodInfo.getUnit());
        }


        amountField.setFilters(amountFilter);
        amountField.setInputType(InputType.TYPE_CLASS_NUMBER);

        caloriesField.setFilters(caloriesFilter);


        FloatingActionButton fab = parentHolder.findViewById(R.id.addEntry);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameField = parentHolder.findViewById(R.id.input_food);
                String name = nameField.getText().toString();

                String amount = amountField.getText().toString();

                String calories = caloriesField.getText().toString();


                // validation
                boolean validated = validateResponses(name, amount, calories);
                //now parse the other Infos and validate and put the parsed values into yet another Map
                Map<String, Float> fieldValues =new  HashMap<>();
                for(Map.Entry<String, EditText> editTextEntry : otherFoodInfoEditTexts.entrySet()){
                    try {
                        String etValue = editTextEntry.getValue().getText().toString();
                        Float fieldValue;
                        if ("".equals(etValue)){
                            fieldValue = 0.0f;
                        }else{
                            fieldValue = Float.parseFloat(etValue);
                        }
                        fieldValues.put(editTextEntry.getKey(),fieldValue);
                    } catch (NumberFormatException e) {
                        showErrorMessageString(otherFoodInfoTextInputLayouts.get(editTextEntry.getKey()), getResources().getString(R.string.error_fieldname_nan, FoodInfosToShow.foodInfos.get(editTextEntry.getKey())));
                        validated = false;
                    }
                }

                if(validated) {



                    boolean entrySuccessful = makeDatabaseEntry(name, amount, calories, fieldValues);
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



            nameField.setText(referenceActivity.name);
            nameField.setFocusable(false);
            nameField.setClickable(false);
            nameField.setTextColor(getResources().getColor(R.color.middlegrey));

            caloriesField.setText(String.format(Locale.ENGLISH, "%.2f", referenceActivity.calories));
            caloriesField.setFocusable(false);
            caloriesField.setClickable(false);
            caloriesField.setTextColor(getResources().getColor(R.color.middlegrey));

            for(Map.Entry<String,EditText> editTextEntry : otherFoodInfoEditTexts.entrySet()){
                EditText currentEt = editTextEntry.getValue();
                //TODO dafür sorgen, dass referenceActivity.otherFoodInfo gefüllt wird
                currentEt.setText(String.format(Locale.ENGLISH, "%.2f", FoodInfosToShow.getFoodInfoValueByKey(referenceActivity.selectedProduct,editTextEntry.getKey())));
                currentEt.setFocusable(false);
                currentEt.setClickable(false);
                currentEt.setTextColor(getResources().getColor(R.color.middlegrey));
            }

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

            for(Map.Entry<String,EditText> editTextEntry : otherFoodInfoEditTexts.entrySet()){
                EditText currentEt = editTextEntry.getValue();
                currentEt.setText("");
                currentEt.setFocusable(true);
                currentEt.setFocusableInTouchMode(true);
                currentEt.setClickable(true);
                currentEt.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }


    /***
     * Map.getOrDefault does not exist in Api<24 :/
     * @param map
     * @param key
     * @param defaultValue
     * @return the mapped value or the default, if no mapping exists
     */
    private Float getOrDefault(Map<String,Float> map, String key, float defaultValue){
        Float ret = map.get(key);
        if(ret==null){
            ret = defaultValue;
        }
        return ret;
    }
    /**
     * Create a new db entry
     * @param name Name of the food
     * @param amountString amount in g
     * @param caloriesString calories per 100g
     * @return true if successful
     */
    private boolean makeDatabaseEntry(String name, String amountString, String caloriesString, Map<String,Float> otherFieldValues) {
        try {
            int amount = Integer.parseInt(amountString);
            float calories = Float.parseFloat(caloriesString);


            // We haven't explicitly chosen a product so the productId is 0 for unknown
            databaseFacade.insertEntry(amount, ((BaseAddFoodActivity) referenceActivity).date, name, calories,
                    getOrDefault(otherFieldValues, "carbs", 0.0f), getOrDefault(otherFieldValues, "sugar", 0.0f), getOrDefault(otherFieldValues, "protein", 0.0f), getOrDefault(otherFieldValues, "fat", 0.0f), getOrDefault(otherFieldValues, "satFat", 0.0f), getOrDefault(otherFieldValues, "salt", 0.0f), getOrDefault(otherFieldValues, "vitaminA_retinol", 0.0f), getOrDefault(otherFieldValues, "betaCarotin", 0.0f), getOrDefault(otherFieldValues, "vitaminD", 0.0f), getOrDefault(otherFieldValues, "vitaminE", 0.0f), getOrDefault(otherFieldValues, "vitaminK", 0.0f), getOrDefault(otherFieldValues, "thiamin_B1", 0.0f), getOrDefault(otherFieldValues, "riboflavin_B2", 0.0f), getOrDefault(otherFieldValues, "niacin", 0.0f), getOrDefault(otherFieldValues, "vitaminB6", 0.0f), getOrDefault(otherFieldValues, "folat", 0.0f), getOrDefault(otherFieldValues, "pantothenacid", 0.0f), getOrDefault(otherFieldValues, "biotin", 0.0f), getOrDefault(otherFieldValues, "cobalamin_B12", 0.0f), getOrDefault(otherFieldValues, "vitaminC", 0.0f), getOrDefault(otherFieldValues, "natrium", 0.0f), getOrDefault(otherFieldValues, "chlorid", 0.0f), getOrDefault(otherFieldValues, "kalium", 0.0f), getOrDefault(otherFieldValues, "calcium", 0.0f), getOrDefault(otherFieldValues, "phosphor", 0.0f), getOrDefault(otherFieldValues, "magnesium", 0.0f), getOrDefault(otherFieldValues, "eisen", 0.0f), getOrDefault(otherFieldValues, "jod", 0.0f), getOrDefault(otherFieldValues, "fluorid", 0.0f), getOrDefault(otherFieldValues, "zink", 0.0f), getOrDefault(otherFieldValues, "selen", 0.0f), getOrDefault(otherFieldValues, "kupfer", 0.0f), getOrDefault(otherFieldValues, "mangan", 0.0f), getOrDefault(otherFieldValues, "chrom", 0.0f), getOrDefault(otherFieldValues, "molybdaen", 0.0f), 0);
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
     * @return returns true is all entries are valid
     */
    private boolean validateResponses(String name, String amount, String calories) {
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
    private void showErrorMessageString(View view, String errorMessage){
        if(view instanceof TextInputLayout){
            TextInputLayout til = (TextInputLayout) view;
            til.setError(errorMessage);
        }
    }

}
