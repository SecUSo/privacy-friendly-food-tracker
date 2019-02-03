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
