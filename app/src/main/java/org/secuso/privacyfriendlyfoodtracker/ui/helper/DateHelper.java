package org.secuso.privacyfriendlyfoodtracker.ui.helper;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Helps to modified the week or month attributes of a date object.
 */
public class DateHelper {


    /**
     * Change the month of a date by a value.
     *
     * @param date  the current date
     * @param value number of months
     * @return new date with modified month
     */
    public static Date changeMonth(Date date, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, value);
        return calendar.getTime();
    }

    /**
     * Change the week of a date by a value.
     *
     * @param date  the current date
     * @param value number of weeks
     * @return new date with modified week
     */
    public static Date changeWeek(Date date, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //calendar.add(Calendar.DAY_OF_YEAR, value * 7);
        calendar.add(Calendar.WEEK_OF_YEAR, value);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    /**
     * Converts a date to string.
     *
     * @param date the current date
     * @return the date as localized string
     */
    public static String dateToString(Date date) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * Sets a given Date's time to 00:00:00.00, so getting a weeks daily calories works reliably.
     * @param date the date whose time shall be midnight
     * @return the new changed date
     */
    public static Date changeDateTimeToMidnight(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}
