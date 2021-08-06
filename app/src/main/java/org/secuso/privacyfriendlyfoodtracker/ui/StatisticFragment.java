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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.secuso.privacyfriendlyfoodtracker.ui.helper.MathHelper.round;


/**
 * A simple {@link Fragment} subclass. Contains the statistic for a week or a month, depending on
 * the value of isWeek(which can be set via newInstance).
 *
 * @author Andre Lutz
 */
public class StatisticFragment extends Fragment {
    private static final String EXTRA_ISWEEK = "EXTRA_ISWEEK";
    SharedStatisticViewModel sharedStatisticViewModel;
    Activity referenceActivity;
    View parentHolder;
    TextView textView;
    DatabaseFacade databaseFacade;
    GraphView graphViewCalories;

    /***
     * true, if this fragment shall show weekly statistics, false if this shall show monthly statistics.
     */
    boolean isWeek;

    Map<String, GraphView> graphViewMap = new HashMap<>();
    Map<String, TextView> tvNutrimentsAverageMap = new HashMap<>();

    public StatisticFragment() {
        // Required empty public constructor
    }

    /***
     * Since fragments need an empty constructor, newInstance is used to create fragments for either
     * week or month statistics display
     * @param isWeek Whether to show weekly or monthly statistic
     * @return
     */
    public static final StatisticFragment newInstance(boolean isWeek) {
        StatisticFragment f = new StatisticFragment();
        Bundle bdl = new Bundle(2);
        bdl.putBoolean(EXTRA_ISWEEK, isWeek);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        isWeek = getArguments().getBoolean(EXTRA_ISWEEK,true);
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_statistic, container, false);
        sharedStatisticViewModel = ViewModelProviders.of(getActivity()).get(SharedStatisticViewModel.class);
        try {
            databaseFacade = new DatabaseFacade(referenceActivity.getApplicationContext());
        } catch (Exception e){
            Log.e("Error", e.getMessage());
        }
        textView = parentHolder.findViewById(R.id.periodCalories1);
        graphViewCalories = (GraphView) parentHolder.findViewById(R.id.graph);


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
        final Button right_arrow = parentHolder.findViewById(R.id.right_arrow);
        if(isWeek) {
            left_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeWeekByValue(-1);
                    UpdateGraph();
                }
            });
            right_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeWeekByValue(+1);
                    UpdateGraph();
                }
            });
        }else{
            left_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeMonthByValue(-1);
                    UpdateGraph();
                }
            });
            right_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeMonthByValue(+1);
                    UpdateGraph();
                }
            });
        }
        UpdateGraph();
        Observer observer = null;
        if(isWeek){
            observer =new Observer<Calendar>() {
                @Override
                public void onChanged(@Nullable Calendar calendar) {
                    editText.setText(DateHelper.dateToString(getWeekByValue(-1)) + System.getProperty("line.separator") + " - " + System.getProperty("line.separator") + DateHelper.dateToString(calendar.getTime()));
                    UpdateGraph();
                }
            };
        }else{
            observer =new Observer<Calendar>() {
                @Override
                public void onChanged(@Nullable Calendar calendar) {
                    editText.setText(DateHelper.dateToString(getMonthByValue(-1)) + System.getProperty("line.separator") + " - " + System.getProperty("line.separator") + DateHelper.dateToString(calendar.getTime()));
                    UpdateGraph();
                }
            };
        }
        sharedStatisticViewModel.getLiveCalendar().observe(this, observer);
        return parentHolder;
    }

    /***
     * Adds a LineGraphSeries to a GraphView, setting startDate and endDate as x-axis start/end
     * @param graph
     * @param startDate
     * @param endDate
     * @param lineGraphSeries
     */
    void addSeriesToGraph(GraphView graph, Date startDate, Date endDate, LineGraphSeries<DataPoint> ... lineGraphSeries){
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
    void updateGraphUsingNdayPeriod(Date startDate, Date endDate, int periodLengthInDays){
        if(databaseFacade == null){ //I replaced the try catch block covering all code of the method with this null-check, hoping the try catch did not catch other things a lot
            Log.e("ateGraphUsingNdayPeriod","DatabaseFacade is null! Not adding graph stuff to statistics view.");
            return; //TODO: why is databaseFacade null at the beginning?
        }
        List<ConsumedEntrieAndProductDao.DateCalories> consumedEntriesList = new ArrayList<>();
        List<ConsumedEntrieAndProductDao.DateCalories> calories = new ArrayList<>();

        consumedEntriesList = databaseFacade.getCaloriesPerDayinPeriod(startDate,endDate);
        calories = databaseFacade.getPeriodCalories(startDate,endDate);
        DataPoint[] dataPointInterfaces = new DataPoint[consumedEntriesList.size()];
        for (int i = 0; i < consumedEntriesList.size(); i++) {
            dataPointInterfaces[i] = (new DataPoint(consumedEntriesList.get(i).unique1.getTime(), consumedEntriesList.get(i).unique2/100));
        }
        LineGraphSeries<DataPoint> calSeries = new LineGraphSeries<>(dataPointInterfaces);
        addSeriesToGraph(graphViewCalories,startDate,endDate, calSeries);

        float averageCalories = calories.get(0).unique2 / periodLengthInDays;

        BigDecimal averageCaloriesBigDecimal = round(averageCalories,0) ;

        if (calories.size() != 0) {
            textView.setText(averageCaloriesBigDecimal.toString());
        }

        List<ConsumedEntrieAndProductDao.DateNutriments> consumedNutrimentsEntriesList =
                databaseFacade.getNutrimentsPerDayinPeriod(startDate, endDate);
        List<ConsumedEntrieAndProductDao.DateNutriments> consumedNutriments =
                databaseFacade.getPeriodNutriments(startDate,endDate);
        for(Map.Entry<String,FoodInfo> foodInfoEntry : FoodInfosToShow.getFoodInfosShownAsMap(getContext()).entrySet()){
            DataPoint[] nutrimentDataPoints = new DataPoint[consumedNutrimentsEntriesList.size()];
            for (int i = 0; i < consumedNutrimentsEntriesList.size(); i++) {
                nutrimentDataPoints[i] = (new DataPoint(consumedNutrimentsEntriesList.get(i).dateOfConsumption.getTime(), FoodInfosToShow.getFoodInfoValueByKey(consumedNutrimentsEntriesList.get(i), foodInfoEntry.getKey())/100));
            }
            float averageNutriment = FoodInfosToShow.getFoodInfoValueByKey(consumedNutriments.get(0),foodInfoEntry.getKey()) / periodLengthInDays;
            BigDecimal averageNutrimentBigDecimal = round(averageNutriment,0) ;

            TextView tvAverageNutriment = null;
            LinearLayout ll = (LinearLayout)parentHolder.findViewById(R.id.statisticLinLayout);

            if(tvNutrimentsAverageMap.containsKey(foodInfoEntry.getKey())){
                tvAverageNutriment = tvNutrimentsAverageMap.get(foodInfoEntry.getKey());
            }else{
                tvAverageNutriment = new TextView(getContext());
                ll.addView(tvAverageNutriment);
                tvNutrimentsAverageMap.put(foodInfoEntry.getKey(), tvAverageNutriment);
            }

            GraphView graphView = null;
            if(graphViewMap.containsKey(foodInfoEntry.getKey())){
                graphView = graphViewMap.get(foodInfoEntry.getKey());
            }else{
                graphView = (GraphView)LayoutInflater.from(getActivity())
                        .inflate(R.layout.graphview_nutriments, ll, false);
                ll.addView(graphView);
                graphViewMap.put(foodInfoEntry.getKey(),graphView);
            }

            tvAverageNutriment.setText(getString(R.string.statistics_average_nutriments,
                    foodInfoEntry.getValue().getName(),
                    averageNutrimentBigDecimal,
                    foodInfoEntry.getValue().getUnit()));

            LineGraphSeries<DataPoint> nutrimentSeries = new LineGraphSeries<>(nutrimentDataPoints);
            addSeriesToGraph(graphView,startDate,endDate, nutrimentSeries);


        }


    }
    void UpdateGraph() {
        Date startDate;
        Date endDate;
        int days;
        if (isWeek) {
            startDate = getWeekByValue(-1);
            endDate = getWeekByValue(0);
            days = 7;
        }else{
            startDate = getMonthByValue(-1);
            endDate = getMonthByValue(0);
            Calendar startDateCalendar = Calendar.getInstance();
            startDateCalendar.setTime(startDate);
            Calendar endDateCalendar = Calendar.getInstance();
            endDateCalendar.setTime(endDate);
            days = (int) daysBetween( endDateCalendar,startDateCalendar);
        }
        startDate = DateHelper.changeDateTimeToMidnight(startDate); //set time to midnight so the period does not depend on the time of day
        endDate = DateHelper.changeDateTimeToMidnight(endDate);
        updateGraphUsingNdayPeriod(startDate,endDate, days);
    }

    /**
     * Source: https://stackoverflow.com/questions/20165564/calculating-days-between-two-dates-with-java
     * By Pim Beers
     * @param day1
     * @param day2
     * @return
     */
    private static int daysBetween(Calendar day1, Calendar day2){
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays ;
        }
    }

    Date changeMonthByValue(int value) {
        Date date = getMonthByValue(value);
        sharedStatisticViewModel.setCalendarWithDateObj(date);
        return date;
    }

    Date getMonthByValue(int value) {
        Date date = DateHelper.changeMonth(sharedStatisticViewModel.getDate(), value);
        return date;
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
