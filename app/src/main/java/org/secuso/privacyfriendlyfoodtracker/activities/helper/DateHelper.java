package org.secuso.privacyfriendlyfoodtracker.activities.helper;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateHelper {


    public static Date changeMonth(Date date, int value){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, value);
        return calendar.getTime();
    }

    public static Date changeWeek(Date date, int value){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, value*7);
        return calendar.getTime();
    }

    public static String dateToString(Date date){
        DateFormat dateFormat = DateFormat.getDateInstance( DateFormat.SHORT, Locale.getDefault());
        return dateFormat.format(date);
    }
}
