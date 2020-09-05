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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.secuso.privacyfriendlyfoodtracker.BuildConfig;
import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseFacade;
import org.secuso.privacyfriendlyfoodtracker.ui.helper.BaseActivity;

/**
 * Displays an "about" page
 * @author Simon Reinkemeier, yonjuni
 *
 */
public class StateActivity extends AppCompatActivity {

    DatabaseFacade databaseFacade;
    EditText ageField;
    EditText weightField;
    EditText heightField;
    TextView resultCaloriesField;
    AppCompatRadioButton femaleButton;
    AppCompatRadioButton slightlyActiveButton;
    AppCompatRadioButton activeButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(BaseActivity.MAIN_CONTENT_FADEIN_DURATION);
        }

        overridePendingTransition(0, 0);

        try {
            databaseFacade = new DatabaseFacade(this.getApplicationContext());
        } catch (Exception e){
            Log.e("Error", e.getMessage());
        }
        InputFilter[] ageFilter = { new InputFilter.LengthFilter(2) };
        InputFilter[] weightFilter = { new InputFilter.LengthFilter(3) };
        InputFilter[] heightFilter = { new InputFilter.LengthFilter(3) };

        resultCaloriesField = this.findViewById(R.id.text_resultCalories);
        ageField = this.findViewById(R.id.input_age);
        heightField = this.findViewById(R.id.input_height);
        weightField = this.findViewById(R.id.input_weight);
        ageField.setFilters(ageFilter);
        heightField.setFilters(heightFilter);
        weightField.setFilters(weightFilter);
        femaleButton = this.findViewById(R.id.inputSexF);
        slightlyActiveButton = this.findViewById(R.id.input_slightly_active);
        activeButton = this.findViewById(R.id.input_active);

        FloatingActionButton fab = this.findViewById(R.id.countCalories);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isFemale = femaleButton.isChecked();

                int ageValue = getIntValue(ageField.getText().toString(), ageField);
                int weightValue = getIntValue(weightField.getText().toString(),weightField);
                int heightValue = getIntValue(heightField.getText().toString(), heightField);

                //add coefficient depend from activity
                float activityState = 1.2f;
                if (slightlyActiveButton.isChecked()){
                    activityState = 1.375f;
                }
                if (activeButton.isChecked()){
                    activityState = 1.65f;
                }

                if (ageValue == 0 || weightValue == 0 || heightValue == 0){
                    return;
                }else{
                    int optimalCalories = countOptimalCalories(isFemale, ageValue, weightValue, heightValue, activityState);
                    resultCaloriesField.setText(String.valueOf(optimalCalories));
                }
            }
        });
    }

    private int countOptimalCalories(boolean isFemale, int ageValue, int weightValue, int heightValue, float activityState) {
        if (isFemale) {
            return (int) ((447.593f + (9.247f * weightValue)+(3.098f*heightValue)-(4.330*ageValue))*activityState *0.80f);
        }else{
            return (int) ((88.362f + (13.397f * weightValue)+(4.799f*heightValue)-(5.677*ageValue))*activityState *0.80f);
        }
    }

    private int getIntValue(String valueStr, EditText editText){
        try {
            return Integer.parseInt(valueStr);
        }catch (Exception e) {
            // something went wrong so the entry wasn't successful
            showErrorMessage(editText, R.string.error_food_missing);
            return 0;
        }
    }

    /**
     * Create a new db entry
     * @param dailycalorie goal dailycalorie
     * @param minweight goal minweight
     * @return true if successful
     */
    private boolean makeDatabaseEntry(int dailycalorie, int minweight) {
        try {
            // We haven't explicitly chosen a product so the productId is 0 for unknown
            databaseFacade.insertGoalEntry(dailycalorie, minweight);
        } catch (Exception e) {
            // something went wrong so the entry wasn't successful
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void showErrorMessage(View view, int errorMessageId){
        // reset error messages
        ageField.setError("");
        heightField.setError("");
        weightField.setError("");

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

