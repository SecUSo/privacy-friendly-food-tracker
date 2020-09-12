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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.database.Goals;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseFacade;
import org.secuso.privacyfriendlyfoodtracker.ui.helper.BaseActivity;
import org.secuso.privacyfriendlyfoodtracker.ui.helper.InputFilterMinMax;

import java.util.Locale;

/**
 * Displays an "goals" page
 *
 * @author fialo4ka
 */
public class GoalsActivity extends AppCompatActivity {

    DatabaseFacade databaseFacade;
    EditText resultCaloriesField;
    TextView nowGoalText;
    AlertDialog.Builder goalDialog;
    EditText ageField;
    EditText weightField;
    EditText heightField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);


        LayoutInflater layoutInflater = (LayoutInflater) this.getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addGoalView = layoutInflater.inflate(R.layout.add_goal, null);
        goalDialog = new AlertDialog.Builder(GoalsActivity.this);
        setGoalDialog(addGoalView);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
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
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        ageField = this.findViewById(R.id.input_age);
        heightField = this.findViewById(R.id.input_height);
        weightField = this.findViewById(R.id.input_weight);
        ageField.setFilters(new InputFilter[]{new InputFilterMinMax(1, 110)});
        heightField.setFilters(new InputFilter[]{new InputFilterMinMax(1, 230)});
        weightField.setFilters(new InputFilter[]{new InputFilterMinMax(1, 400)});
        final AppCompatRadioButton femaleButton = this.findViewById(R.id.inputSexF);
        final AppCompatRadioButton slightlyActiveButton = this.findViewById(R.id.input_slightly_active);
        final AppCompatRadioButton activeButton = this.findViewById(R.id.input_active);
        final AppCompatRadioButton loseWeightButton = this.findViewById(R.id.input_loseWeight);


        resultCaloriesField = addGoalView.findViewById(R.id.input_resultCalories);
        nowGoalText = this.findViewById(R.id.nowGoal);

        final AppCompatCheckBox pregm = this.findViewById(R.id.inputIsPregnant);
        final AppCompatRadioButton pregnant3Button = this.findViewById(R.id.input_pregnant3);
        final AppCompatRadioButton pregnant6Button = this.findViewById(R.id.input_pregnant6);
        final RadioGroup months = this.findViewById(R.id.input_monthsPregnant);

        FloatingActionButton fab = this.findViewById(R.id.countCalories);

        Goals goals = databaseFacade.getLastGoals();
        if (goals != null && goals.dailycalorie > 0 && goals.minweight > 0) {
            nowGoalText.setText(String.format(Locale.ENGLISH, "%s: %dkCal, %dkg",getResources().getText(R.string.hint_now_goal), goals.dailycalorie,goals.minweight));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean loosWeight = loseWeightButton.isChecked();
                boolean isFemale = femaleButton.isChecked();
                boolean ispragnant = pregm.isChecked();
                PregnantTime pregnantTime = PregnantTime.THIRD;
                if (ispragnant && pregnant6Button.isChecked()) {
                    pregnantTime = PregnantTime.SECOND;
                }
                if (ispragnant && pregnant3Button.isChecked()){
                    pregnantTime = PregnantTime.FIRST;
                }

                int ageValue = getIntValue(view, ageField.getText().toString(), ageField, R.string.error_age_missing);
                int weightValue = getIntValue(view, weightField.getText().toString(), weightField,R.string.error_weight_missing);
                int heightValue = getIntValue(view, heightField.getText().toString(), heightField,R.string.error_height_missing);

                //add coefficient depend from activity
                float activityState = 1.2f;
                if (slightlyActiveButton.isChecked()) {
                    activityState = 1.375f;
                }
                if (activeButton.isChecked()) {
                    activityState = 1.65f;
                }

                if (ageValue == 0 || weightValue == 0 || heightValue == 0) {
                    return;
                } else {
                    int optimalCalories = countOptimalCalories(loosWeight, isFemale, ageValue, weightValue, heightValue, activityState, ispragnant, pregnantTime);
                    resultCaloriesField.setText(String.valueOf(optimalCalories));
                    if (addGoalView.getParent() != null)
                        ((ViewGroup) addGoalView.getParent()).removeView(addGoalView);
                    goalDialog.setView(addGoalView);
                    goalDialog.show();
                }
            }
        });

        pregm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pregm.isChecked()) {
                    months.setVisibility(View.VISIBLE);
                } else {
                    months.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setGoalDialog(final View addGoalView) {

        goalDialog.setPositiveButton(getResources().getString(R.string.save),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int resultCaloriesInt = getIntValue(addGoalView,resultCaloriesField.getText().toString(), resultCaloriesField, R.string.error_calories_missing);
                        EditText idealWeightField = addGoalView.findViewById(R.id.input_IdealWeight);
                        int idealWeightInt = getIntValue(addGoalView,idealWeightField.getText().toString(), idealWeightField, R.string.error_weight_missing);
                        if (resultCaloriesInt > 0 && idealWeightInt > 0) {
                            makeDatabaseEntry(resultCaloriesInt, idealWeightInt);
                        }
                    }
                });
        goalDialog.setNegativeButton(getResources().getString(R.string.decline), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
    }

    private int countOptimalCalories(boolean loosWeight, boolean isFemale, int ageValue, int weightValue, int heightValue, float activityState, boolean isPregnant, PregnantTime pregnantTime) {
        float calories = 0f;
        if (isFemale) {
            calories = (float) ((447.593f + (9.247f * weightValue) + (3.098f * heightValue) - (4.330 * ageValue)) * activityState);
        } else {
            calories = (float) ((88.362f + (13.397f * weightValue) + (4.799f * heightValue) - (5.677 * ageValue)) * activityState);
        }
        if (loosWeight && !isPregnant){
            calories *= 0.83f;
        }
        if (isPregnant) {
            switch(pregnantTime) {
                case FIRST:
                    calories +=100;
                    break;
                case SECOND:
                    calories +=200;
                    break;
                case THIRD:
                    calories +=400;
                    break;
            }
        }
        return (int) calories;
    }

    private int getIntValue(View view, String valueStr, EditText editText, int error) {
        try {
            editText.setError(null);
            return Integer.parseInt(valueStr);

        } catch (Exception e) {
            editText.setError(getText(error));
            Snackbar.make(view, error, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return 0;
        }
    }

    /**
     * Create a new db entry
     *
     * @param dailycalorie goal dailycalorie
     * @param minweight    goal minweight
     * @return true if successful
     */
    private boolean makeDatabaseEntry(int dailycalorie, int minweight) {
        try {
            databaseFacade.insertGoalEntry(dailycalorie, minweight);
        } catch (Exception e) {
            // something went wrong so the entry wasn't successful
            e.printStackTrace();
            return false;
        }
        return true;
    }

    enum PregnantTime {
        FIRST,
        SECOND,
        THIRD
    }
}

