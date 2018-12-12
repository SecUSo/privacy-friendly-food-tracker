package org.secuso.privacyfriendlyfoodtracker.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.jjoe64.graphview.series.DataPoint;

import java.sql.Date;
import java.util.List;

@Dao
public interface ConsumedEntrieAndProductDao {
    @Query("SELECT consumedEntries.date AS unique1,   (sum(product.energy*consumedEntries.amount)) AS unique2 FROM consumedEntries INNER JOIN product ON consumedEntries.productId = product.id  WHERE consumedEntries.date BETWEEN :dayst AND :dayet GROUP BY consumedEntries.date ")
    List<DateCalories> getAllConsumedEntries1(final Date dayst, final Date dayet);

    @Query("SELECT consumedEntries.date AS unique1, sum(product.energy*consumedEntries.amount) AS unique2 FROM consumedEntries INNER JOIN product ON consumedEntries.productId = product.id  WHERE consumedEntries.date BETWEEN :dayst AND :dayet")
    List<DateCalories> getCaloriesPeriod(final Date dayst, final Date dayet);

    static class DateCalories
    {
        public Date unique1;
        public int unique2;
    }
    static class Calories
    {
        public int unique2;
    }


}
