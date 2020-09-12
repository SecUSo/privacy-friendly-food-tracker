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
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.database.Goals;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseFacade;
import org.secuso.privacyfriendlyfoodtracker.ui.helper.DateHelper;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntrieAndProductDao;
import org.secuso.privacyfriendlyfoodtracker.ui.helper.GraphViewHelper;
import org.secuso.privacyfriendlyfoodtracker.ui.viewmodels.SharedStatisticViewModel;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.secuso.privacyfriendlyfoodtracker.ui.helper.MathHelper.round;


/**
 * A simple {@link Fragment} subclass. Contains the statistic for a month.
 *
 * @author Andre Lutz
 */
public class MonthStatisticFragment extends Fragment {
    SharedStatisticViewModel sharedStatisticViewModel;
    Activity referenceActivity;
    View parentHolder;
    TextView textView;
    DatabaseFacade databaseFacade;

    public MonthStatisticFragment() {
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
        final TextView editText = (TextView) parentHolder.findViewById(R.id.datepicker);
        editText.setText(DateHelper.dateToString(sharedStatisticViewModel.getDate()));
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        sharedStatisticViewModel.setDate(dayOfMonth, monthOfYear, year);
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
                changeMonthByValue(-1);
                UpdateGraph();
            }
        });
        final Button right_arrow = parentHolder.findViewById(R.id.right_arrow);
        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMonthByValue(+1);
                UpdateGraph();
            }
        });
        UpdateGraph();
        sharedStatisticViewModel.getLiveCalendar().observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(@Nullable Calendar calendar) {
                editText.setText(DateHelper.dateToString(getMonthByValue(-1)) + System.getProperty("line.separator") + " - " + System.getProperty("line.separator") + DateHelper.dateToString(calendar.getTime()));
                UpdateGraph();
            }
        });
        return parentHolder;
    }

    void UpdateGraph() {

        try {

            final Date startDate = getMonthByValue(-1);
            final Date endDate = getMonthByValue(0);
            List<ConsumedEntrieAndProductDao.DateCalories> consumedEntriesList = databaseFacade.getCaloriesPerDayinPeriod(startDate,endDate);
            List<ConsumedEntrieAndProductDao.DateCalories> calories = databaseFacade.getPeriodCalories(startDate,endDate);
            DataPoint[] dataPointInterfaces = new DataPoint[consumedEntriesList.size()];
            for (int i = 0; i < consumedEntriesList.size(); i++) {
                dataPointInterfaces[i] = (new DataPoint(consumedEntriesList.get(i).unique1.getTime(), consumedEntriesList.get(i).unique2/100));
            }
            if (calories.size() != 0) {

                Calendar startDateCalendar = Calendar.getInstance();
                startDateCalendar.setTime(startDate);
                Calendar endDateCalendar = Calendar.getInstance();
                endDateCalendar.setTime(endDate);
                float periodCalories = calories.get(0).unique2;
                float periodDays = daysBetween( endDateCalendar,startDateCalendar);
                float averageCalories = periodCalories/periodDays;
                BigDecimal averageCaloriesBigDecimal = round(averageCalories,0) ;
                textView.setText(averageCaloriesBigDecimal.toString());
            }
            GraphView graph = (GraphView) parentHolder.findViewById(R.id.graph);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointInterfaces);
            graph.addSeries(series);

            Goals goals = databaseFacade.getLastGoals();
            if (goals != null && goals.dailycalorie > 0) {
                LineGraphSeries<DataPoint> seriesGoal = new GraphViewHelper().goalsSeries(goals, startDate, endDate);
                seriesGoal.setColor(getResources().getColor(R.color.colorAccentGreen));
                graph.addSeries(seriesGoal);
            }

            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(referenceActivity));
            graph.getGridLabelRenderer().setHumanRounding(false, true);
            graph.getViewport().setMinX(startDate.getTime());
            graph.getViewport().setMaxX(endDate.getTime());
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getGridLabelRenderer().setTextSize(40);
            graph.getViewport().setScrollable(true);
            graph.getGridLabelRenderer().setHorizontalLabelsAngle(135);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
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
}
