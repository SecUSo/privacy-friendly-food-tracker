package org.secuso.privacyfriendlyfoodtracker.ui.helper;

import android.graphics.Color;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.secuso.privacyfriendlyfoodtracker.database.Goals;

import java.util.Calendar;
import java.util.Date;

public class GraphViewHelper {

    public LineGraphSeries<DataPoint> goalsSeries(Goals goals, Date startDate, Date endDate) {
        LineGraphSeries<DataPoint> seriesGoal = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(startDate.getTime(), goals.dailycalorie),
                new DataPoint(endDate.getTime(), goals.dailycalorie)
        });
        seriesGoal.setColor(Color.GREEN);
        return seriesGoal;
    }
}
