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
package org.secuso.privacyfriendlyfoodtracker.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;

/**
 * For sharing data between WeekStatisticFragment and MonthStatisticFragment.
 */
public class SharedStatisticViewModel extends ViewModel {
    private final MutableLiveData<Calendar> liveCalendar = new MutableLiveData<Calendar>();

    public LiveData<Calendar> getLiveCalendar() {
        return liveCalendar;
    }

    /**
     * Constructor. Sets the calender value to the current day.
     */
    public SharedStatisticViewModel() {
        final Calendar currentTime = Calendar.getInstance();
        calendar = currentTime;
        liveCalendar.setValue(calendar);
    }

    private Calendar calendar;


    public Date getDate() {
        return calendar.getTime();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setDate(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        calendar = cal;
        liveCalendar.setValue(calendar);
    }

    public void setCalendarWithDateObj(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        calendar = cal;
        liveCalendar.setValue(calendar);
    }
}
