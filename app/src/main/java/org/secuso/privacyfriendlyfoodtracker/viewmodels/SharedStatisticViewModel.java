package org.secuso.privacyfriendlyfoodtracker.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.secuso.privacyfriendlyfoodtracker.activities.helper.DateHelper;

import java.util.Calendar;
import java.util.Date;

public class SharedStatisticViewModel extends ViewModel {
    private final MutableLiveData<Calendar> liveCalendar = new MutableLiveData<Calendar>();

    public void liveCalendar(Calendar item) {
        liveCalendar.setValue(item);
    }

    public LiveData<Calendar> getLiveCalendar() {
        return liveCalendar;
    }



    SharedStatisticViewModel(){
        final Calendar currentTime = Calendar.getInstance();
        calendar = currentTime;
        liveCalendar.setValue(calendar);
    }

    private Calendar calendar;


    public Date getDate() {
        return calendar.getTime();
    }

    public void setCalendar(Calendar item) {
        calendar = item;
        liveCalendar.setValue(calendar);
    }

    public void setDate(int day, int month, int year){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        calendar = cal;
        liveCalendar.setValue(calendar);
    }

    public void setCalendarWithDateObj(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        calendar = cal;
        liveCalendar.setValue(calendar);
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
