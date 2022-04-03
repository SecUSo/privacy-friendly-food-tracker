package org.secuso.privacyfriendlyfoodtracker.ui.helper;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.secuso.privacyfriendlyfoodtracker.database.Goals;

import java.util.Date;

/**
 * Helps to draw GraphView object.
 */

public class GraphViewHelper {

    public LineGraphSeries<DataPoint> goalsSeries(Goals goals, Date startDate, Date endDate) {
        LineGraphSeries<DataPoint> seriesGoal = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(startDate.getTime(), goals.dailycalorie),
                new DataPoint(endDate.getTime(), goals.dailycalorie)
        });
        return seriesGoal;
    }

}
