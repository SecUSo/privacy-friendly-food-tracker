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

    void UpdateGraph() {

        try {

            Date startDate = getWeekByValue(-1);
            Date endDate = getWeekByValue(0);

            List<ConsumedEntrieAndProductDao.DateCalories> consumedEntriesList = databaseFacade.getCaloriesPerDayinPeriod(startDate, endDate);
            List<ConsumedEntrieAndProductDao.DateCalories> calories = databaseFacade.getPeriodCalories(startDate, endDate);
            DataPoint[] dataPointInterfaces = new DataPoint[consumedEntriesList.size()];
            for (int i = 0; i < consumedEntriesList.size(); i++) {
                dataPointInterfaces[i] = (new DataPoint(consumedEntriesList.get(i).unique1.getTime(), consumedEntriesList.get(i).unique2/100));
            }

            List<ConsumedEntrieAndProductDao.DateCalories> consumedCarbsEntriesList = databaseFacade.getCarbsPerDayinPeriod(startDate, endDate);
            List<ConsumedEntrieAndProductDao.DateCalories> carbs = databaseFacade.getPeriodCarbs(startDate, endDate);
            DataPoint[] carbsDataPointInterfaces = new DataPoint[consumedCarbsEntriesList.size()];
            for (int i = 0; i < consumedCarbsEntriesList.size(); i++) {
                carbsDataPointInterfaces[i] = (new DataPoint(consumedCarbsEntriesList.get(i).unique1.getTime(), consumedCarbsEntriesList.get(i).unique2/100));
            }

            List<ConsumedEntrieAndProductDao.DateCalories> consumedSugarEntriesList = databaseFacade.getSugarPerDayinPeriod(startDate, endDate);
            List<ConsumedEntrieAndProductDao.DateCalories> sugar = databaseFacade.getPeriodSugar(startDate, endDate);
            DataPoint[] sugarDataPointInterfaces = new DataPoint[consumedSugarEntriesList.size()];
            for (int i = 0; i < consumedSugarEntriesList.size(); i++) {
                sugarDataPointInterfaces[i] = (new DataPoint(consumedSugarEntriesList.get(i).unique1.getTime(), consumedSugarEntriesList.get(i).unique2/100));
            }

            List<ConsumedEntrieAndProductDao.DateCalories> consumedProteinEntriesList = databaseFacade.getProteinPerDayinPeriod(startDate, endDate);
            List<ConsumedEntrieAndProductDao.DateCalories> protein = databaseFacade.getPeriodProtein(startDate, endDate);
            DataPoint[] proteinDataPointInterfaces = new DataPoint[consumedProteinEntriesList.size()];
            for (int i = 0; i < consumedProteinEntriesList.size(); i++) {
                proteinDataPointInterfaces[i] = (new DataPoint(consumedProteinEntriesList.get(i).unique1.getTime(), consumedProteinEntriesList.get(i).unique2/100));
            }

            List<ConsumedEntrieAndProductDao.DateCalories> consumedFatEntriesList = databaseFacade.getFatPerDayinPeriod(startDate, endDate);
            List<ConsumedEntrieAndProductDao.DateCalories> fat = databaseFacade.getPeriodFat(startDate, endDate);
            DataPoint[] fatDataPointInterfaces = new DataPoint[consumedFatEntriesList.size()];
            for (int i = 0; i < consumedFatEntriesList.size(); i++) {
                fatDataPointInterfaces[i] = (new DataPoint(consumedFatEntriesList.get(i).unique1.getTime(), consumedFatEntriesList.get(i).unique2/100));
            }

            List<ConsumedEntrieAndProductDao.DateCalories> consumedSatFatEntriesList = databaseFacade.getSatFatPerDayinPeriod(startDate, endDate);
            List<ConsumedEntrieAndProductDao.DateCalories> satFat = databaseFacade.getPeriodSatFat(startDate, endDate);
            DataPoint[] satFatDataPointInterfaces = new DataPoint[consumedSatFatEntriesList.size()];
            for (int i = 0; i < consumedSatFatEntriesList.size(); i++) {
                satFatDataPointInterfaces[i] = (new DataPoint(consumedSatFatEntriesList.get(i).unique1.getTime(), consumedSatFatEntriesList.get(i).unique2/100));
            }

            int weekdays = 8;

            float averageCalories = calories.get(0).unique2 / weekdays;
            float averageCarbs = carbs.get(0).unique2 / weekdays;
            float averageSugar = sugar.get(0).unique2 / weekdays;
            float averageProtein = protein.get(0).unique2 / weekdays;
            float averageFat = fat.get(0).unique2 / weekdays;
            float averageSatFat = satFat.get(0).unique2 / weekdays;

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
            GraphView graph = (GraphView) parentHolder.findViewById(R.id.graph);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointInterfaces);
            graph.addSeries(series);
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(referenceActivity));
            graph.getGridLabelRenderer().setHumanRounding(false, true);
            graph.getViewport().setMinX(startDate.getTime());
            graph.getViewport().setMaxX(endDate.getTime());
            graph.getViewport().setXAxisBoundsManual(true);

            graph.getGridLabelRenderer().setNumHorizontalLabels(7); // only 7 because of the space
            graph.getGridLabelRenderer().setTextSize(40);
            graph.getViewport().setScrollable(true);
            graph.getGridLabelRenderer().setHorizontalLabelsAngle(135);

            GraphView carbsGraph = (GraphView) parentHolder.findViewById(R.id.graphCarbs);
            LineGraphSeries<DataPoint> carbsSeries = new LineGraphSeries<>(carbsDataPointInterfaces);
            carbsGraph.addSeries(carbsSeries);
            LineGraphSeries<DataPoint> sugarSeries = new LineGraphSeries<>(sugarDataPointInterfaces);
            sugarSeries.setColor(Color.RED);
            carbsGraph.addSeries(sugarSeries);
            carbsGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(referenceActivity));
            carbsGraph.getGridLabelRenderer().setHumanRounding(false, true);
            carbsGraph.getViewport().setMinX(startDate.getTime());
            carbsGraph.getViewport().setMaxX(endDate.getTime());
            carbsGraph.getViewport().setXAxisBoundsManual(true);

            carbsGraph.getGridLabelRenderer().setNumHorizontalLabels(7); // only 7 because of the space
            carbsGraph.getGridLabelRenderer().setTextSize(40);
            carbsGraph.getViewport().setScrollable(true);
            carbsGraph.getGridLabelRenderer().setHorizontalLabelsAngle(135);

            GraphView proteinGraph = (GraphView) parentHolder.findViewById(R.id.graphProtein);
            LineGraphSeries<DataPoint> proteinSeries = new LineGraphSeries<>(proteinDataPointInterfaces);
            proteinGraph.addSeries(proteinSeries);
            proteinGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(referenceActivity));
            proteinGraph.getGridLabelRenderer().setHumanRounding(false, true);
            proteinGraph.getViewport().setMinX(startDate.getTime());
            proteinGraph.getViewport().setMaxX(endDate.getTime());
            proteinGraph.getViewport().setXAxisBoundsManual(true);

            proteinGraph.getGridLabelRenderer().setNumHorizontalLabels(7); // only 7 because of the space
            proteinGraph.getGridLabelRenderer().setTextSize(40);
            proteinGraph.getViewport().setScrollable(true);
            proteinGraph.getGridLabelRenderer().setHorizontalLabelsAngle(135);

            GraphView fatGraph = (GraphView) parentHolder.findViewById(R.id.graphFat);
            LineGraphSeries<DataPoint> fatSeries = new LineGraphSeries<>(fatDataPointInterfaces);
            fatGraph.addSeries(fatSeries);
            LineGraphSeries<DataPoint> satFatSeries = new LineGraphSeries<>(satFatDataPointInterfaces);
            satFatSeries.setColor(Color.RED);
            fatGraph.addSeries(satFatSeries);
            fatGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(referenceActivity));
            fatGraph.getGridLabelRenderer().setHumanRounding(false, true);
            fatGraph.getViewport().setMinX(startDate.getTime());
            fatGraph.getViewport().setMaxX(endDate.getTime());
            fatGraph.getViewport().setXAxisBoundsManual(true);

            fatGraph.getGridLabelRenderer().setNumHorizontalLabels(7); // only 7 because of the space
            fatGraph.getGridLabelRenderer().setTextSize(40);
            fatGraph.getViewport().setScrollable(true);
            fatGraph.getGridLabelRenderer().setHorizontalLabelsAngle(135);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
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
