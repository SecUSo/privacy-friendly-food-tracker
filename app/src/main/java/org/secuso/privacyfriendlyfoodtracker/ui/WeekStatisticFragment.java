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


import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseFacade;
import org.secuso.privacyfriendlyfoodtracker.ui.helper.DateHelper;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntrieAndProductDao;
import org.secuso.privacyfriendlyfoodtracker.ui.viewmodels.SharedStatisticViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.secuso.privacyfriendlyfoodtracker.ui.helper.MathHelper.round;


/**
 * A simple {@link Fragment} subclass. Contains the statistic for a week.
 *
 * @author Andre Lutz
 */
public class WeekStatisticFragment extends Fragment {
    SharedStatisticViewModel sharedStatisticViewModel;
    Activity referenceActivity;
    View parentHolder;
    TextView textView;
    DatabaseFacade databaseFacade;
    TextView carbsTextView;
    TextView proteinTextView;
    TextView fatTextView;

    public WeekStatisticFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_month_statistic, container, false);
        sharedStatisticViewModel = ViewModelProviders.of(getActivity()).get(SharedStatisticViewModel.class);
        try {
            databaseFacade = new DatabaseFacade(referenceActivity.getApplicationContext());
        } catch (Exception e){
            Log.e("Error", e.getMessage());
        }
        textView = parentHolder.findViewById(R.id.periodCalories1);
        carbsTextView = parentHolder.findViewById(R.id.periodCarbs1);
        proteinTextView = parentHolder.findViewById(R.id.periodProtein1);
        fatTextView = parentHolder.findViewById(R.id.periodFat1);

        final TextView editText = (TextView) parentHolder.findViewById(R.id.datepicker);
        editText.setText(DateHelper.dateToString(sharedStatisticViewModel.getDate()));
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        sharedStatisticViewModel.setDate(dayOfMonth, monthOfYear , year);
                        UpdateGraph();
                    }
                };
                final Calendar currentDate = sharedStatisticViewModel.getCalendar();
                final int mYear = currentDate.get(Calendar.YEAR);
                final int mMonth = currentDate.get(Calendar.MONTH);
                final int mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(referenceActivity, dpd, mYear, mMonth, mDay);
                d.show();

            }
        });
        final Button left_arrow = parentHolder.findViewById(R.id.left_arrow);
        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWeekByValue(-1);
            }
        });
        final Button right_arrow = parentHolder.findViewById(R.id.right_arrow);
        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWeekByValue(+1);
            }
        });
        UpdateGraph();
        sharedStatisticViewModel.getLiveCalendar().observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(@Nullable Calendar calendar) {
                editText.setText(DateHelper.dateToString(getWeekByValue(-1)) + System.getProperty("line.separator") + " - " + System.getProperty("line.separator") + DateHelper.dateToString(calendar.getTime()));
                UpdateGraph();
            }
        });
        return parentHolder;
    }
    static DataPoint[] dataPointsForNutriment(List<ConsumedEntrieAndProductDao.DateCalories> consumedXYEntries){
        DataPoint[] datapointsToReturn = new DataPoint[consumedXYEntries.size()];
        for (int i = 0; i < consumedXYEntries.size(); i++) {
            datapointsToReturn[i] = (new DataPoint(consumedXYEntries.get(i).unique1.getTime(), consumedXYEntries.get(i).unique2/100));
        }
        return datapointsToReturn;
    }
    static void addSeriesToGraphById(View parentHolder, Activity referenceActivity, Date startDate, Date endDate, int graphViewId, LineGraphSeries<DataPoint> ... lineGraphSeries){
        GraphView graph = (GraphView) parentHolder.findViewById(graphViewId);
        for(LineGraphSeries<DataPoint> series : lineGraphSeries){
            graph.addSeries(series);
        }
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(referenceActivity));
        graph.getGridLabelRenderer().setHumanRounding(false, true);
        graph.getViewport().setMinX(startDate.getTime());
        graph.getViewport().setMaxX(endDate.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getGridLabelRenderer().setNumHorizontalLabels(7); // only 7 because of the space
        graph.getGridLabelRenderer().setTextSize(40);
        graph.getViewport().setScrollable(true);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(135);
    }

    /***
     *     Updates the graphs with the calories/carbs/... eaten during that period and also updates the
     *     average consumed calories/... during that period. Works for weeks and months, depending on the
     *     given start/end date and n days in period.
     *     It is static and part of the WekkStatisticFragment class, because I could not think of a
     *     better place for it. I tried making MonthStatisticFragment inheriting from Week... but this
     *     resulted in strange bugs.
     * @param startDate
     * @param endDate
     * @param periodLengthInDays Number of days in period, 7 for week, something else for e.g. month.
     */
    static void updateGraphUsingNdayPeriod(View parentHolder, Activity referenceActivity,
                                           DatabaseFacade databaseFacade, TextView textView,
                                           TextView carbsTextView, TextView proteinTextView,
                                           TextView fatTextView,
                                           Date startDate, Date endDate, int periodLengthInDays){
        if(databaseFacade == null){ //I replaced the try catch block covering all code of the method with this null-check, hoping the try catch did not catch other things a lot
            Log.e("ateGraphUsingNdayPeriod","DatabaseFacade is null! Not adding graph stuff to statistics view.");
            return; //TODO: why is databaseFacade null at the beginning?
        }
        List<ConsumedEntrieAndProductDao.DateCalories> consumedEntriesList = new ArrayList<>();
        List<ConsumedEntrieAndProductDao.DateCalories> calories = new ArrayList<>();

        consumedEntriesList = databaseFacade.getCaloriesPerDayinPeriod(startDate,endDate);
        calories = databaseFacade.getPeriodCalories(startDate,endDate);

        DataPoint[] dataPointInterfaces = dataPointsForNutriment(consumedEntriesList);

        List<ConsumedEntrieAndProductDao.DateCalories> consumedCarbsEntriesList = databaseFacade.getCarbsPerDayinPeriod(startDate, endDate);
        List<ConsumedEntrieAndProductDao.DateCalories> carbs = databaseFacade.getPeriodCarbs(startDate, endDate);
        DataPoint[] carbsDataPointInterfaces = dataPointsForNutriment(consumedCarbsEntriesList);

        List<ConsumedEntrieAndProductDao.DateCalories> consumedSugarEntriesList = databaseFacade.getSugarPerDayinPeriod(startDate, endDate);
        List<ConsumedEntrieAndProductDao.DateCalories> sugar = databaseFacade.getPeriodSugar(startDate, endDate);
        DataPoint[] sugarDataPointInterfaces = dataPointsForNutriment(consumedSugarEntriesList);

        List<ConsumedEntrieAndProductDao.DateCalories> consumedProteinEntriesList = databaseFacade.getProteinPerDayinPeriod(startDate, endDate);
        List<ConsumedEntrieAndProductDao.DateCalories> protein = databaseFacade.getPeriodProtein(startDate, endDate);
        DataPoint[] proteinDataPointInterfaces = dataPointsForNutriment(consumedProteinEntriesList);

        List<ConsumedEntrieAndProductDao.DateCalories> consumedFatEntriesList = databaseFacade.getFatPerDayinPeriod(startDate, endDate);
        List<ConsumedEntrieAndProductDao.DateCalories> fat = databaseFacade.getPeriodFat(startDate, endDate);
        DataPoint[] fatDataPointInterfaces = dataPointsForNutriment(consumedFatEntriesList);

        List<ConsumedEntrieAndProductDao.DateCalories> consumedSatFatEntriesList = databaseFacade.getSatFatPerDayinPeriod(startDate, endDate);
        List<ConsumedEntrieAndProductDao.DateCalories> satFat = databaseFacade.getPeriodSatFat(startDate, endDate);
        DataPoint[] satFatDataPointInterfaces = dataPointsForNutriment(consumedSatFatEntriesList);



        float averageCalories = calories.get(0).unique2 / periodLengthInDays;
        float averageCarbs = carbs.get(0).unique2 / periodLengthInDays;
        float averageSugar = sugar.get(0).unique2 / periodLengthInDays;
        float averageProtein = protein.get(0).unique2 / periodLengthInDays;
        float averageFat = fat.get(0).unique2 / periodLengthInDays;
        float averageSatFat = satFat.get(0).unique2 / periodLengthInDays;

        BigDecimal averageCaloriesBigDecimal = round(averageCalories,0) ;
        BigDecimal averageCarbsBigDecimal = round(averageCarbs,0) ;
        BigDecimal averageSugarBigDecimal = round(averageSugar,0) ;
        BigDecimal averageProteinBigDecimal = round(averageProtein,0) ;
        BigDecimal averageFatBigDecimal = round(averageFat,0) ;
        BigDecimal averageSatFatBigDecimal = round(averageSatFat,0) ;

        if (calories.size() != 0) {
            textView.setText(averageCaloriesBigDecimal.toString());
        }
        if (carbs.size() != 0 && sugar.size() != 0) {
            carbsTextView.setText(averageCarbsBigDecimal.toString() + " (" + averageSugarBigDecimal.toString() +")");
        }
        if (protein.size() != 0) {
            proteinTextView.setText(averageProteinBigDecimal.toString());
        }
        if (fat.size() != 0 && satFat.size() != 0) {
            fatTextView.setText(averageFatBigDecimal.toString() + " (" + averageSatFatBigDecimal.toString() +")");
        }

        LineGraphSeries<DataPoint> calSeries = new LineGraphSeries<>(dataPointInterfaces);
        addSeriesToGraphById(parentHolder,referenceActivity,startDate,endDate,R.id.graph, calSeries);

        LineGraphSeries<DataPoint> carbsSeries = new LineGraphSeries<>(carbsDataPointInterfaces);
        LineGraphSeries<DataPoint> sugarSeries = new LineGraphSeries<>(sugarDataPointInterfaces);
        sugarSeries.setColor(Color.RED);
        addSeriesToGraphById(parentHolder,referenceActivity,startDate,endDate,R.id.graphCarbs,
                carbsSeries, sugarSeries);


        LineGraphSeries<DataPoint> proteinSeries = new LineGraphSeries<>(proteinDataPointInterfaces);
        addSeriesToGraphById(parentHolder,referenceActivity,startDate,endDate,R.id.graphProtein,
                proteinSeries);


        LineGraphSeries<DataPoint> fatSeries = new LineGraphSeries<>(fatDataPointInterfaces);
        LineGraphSeries<DataPoint> satFatSeries = new LineGraphSeries<>(satFatDataPointInterfaces);
        satFatSeries.setColor(Color.RED);
        addSeriesToGraphById(parentHolder,referenceActivity,startDate,endDate,R.id.graphFat,
                fatSeries, satFatSeries);
    }
    void UpdateGraph() {
        int weekdays = 7;
        Date startDate = getWeekByValue(-1);
        startDate = DateHelper.changeDateTimeToMidnight(startDate); //set time to midnight so the period does not depend on the time of day
        Date endDate = getWeekByValue(0);
        endDate = DateHelper.changeDateTimeToMidnight(endDate);
        updateGraphUsingNdayPeriod(parentHolder,referenceActivity,databaseFacade,textView,
                carbsTextView,proteinTextView, fatTextView,startDate,endDate, weekdays);
    }

    Date changeWeekByValue(int value) {
        Date date = getWeekByValue(value);
        sharedStatisticViewModel.setCalendarWithDateObj(date);
        return date;
    }


    Date getWeekByValue(int value) {
        Date date = DateHelper.changeWeek(sharedStatisticViewModel.getDate(), value);
        return date;
    }


}
