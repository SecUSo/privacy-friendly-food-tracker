package org.secuso.privacyfriendlyfoodtracker.activities;


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
import org.secuso.privacyfriendlyfoodtracker.activities.adapter.DatabaseFacade;
import org.secuso.privacyfriendlyfoodtracker.activities.helper.DateHelper;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntrieAndProductDao;
import org.secuso.privacyfriendlyfoodtracker.viewmodels.SharedStatisticViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.secuso.privacyfriendlyfoodtracker.activities.helper.MathHelper.round;


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
        List<ConsumedEntrieAndProductDao.DateCalories> consumedEntriesList = new ArrayList<>();
        List<ConsumedEntrieAndProductDao.DateCalories> calories = new ArrayList<>();
        try {

            Date startDate = getWeekByValue(-1);
            Date endDate = getWeekByValue(0);
            consumedEntriesList = databaseFacade.getCaloriesPerDayinPeriod(startDate,endDate);
            calories = databaseFacade.getPeriodCalories(startDate,endDate);
            DataPoint[] dataPointInterfaces = new DataPoint[consumedEntriesList.size()];
            for (int i = 0; i < consumedEntriesList.size(); i++) {
                dataPointInterfaces[i] = (new DataPoint(consumedEntriesList.get(i).unique1.getTime(), consumedEntriesList.get(i).unique2/100));
            }
            int weekdays = 8;

            float averageCalories = calories.get(0).unique2 / weekdays;

            BigDecimal averageCaloriesBigDecimal = round(averageCalories,0) ;

            if (calories.size() != 0) {
                textView.setText(averageCaloriesBigDecimal.toString());
            }
            GraphView graph = (GraphView) parentHolder.findViewById(R.id.graph);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointInterfaces);
            graph.addSeries(series);
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(referenceActivity));
            graph.getGridLabelRenderer().setHumanRounding(false, true);
            graph.getViewport().setMinX(startDate.getTime());
            graph.getViewport().setMaxX(endDate.getTime());
            graph.getViewport().setXAxisBoundsManual(true);

            graph.getGridLabelRenderer().setNumHorizontalLabels(8); // only 4 because of the space
            graph.getGridLabelRenderer().setTextSize(40);
            graph.getViewport().setScrollable(true);
            graph.getGridLabelRenderer().setHorizontalLabelsAngle(135);
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
