package org.secuso.privacyfriendlyexample.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import org.secuso.privacyfriendlyexample.Converter.DateConverter;


import java.sql.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Product.class,
        parentColumns = "id",
        childColumns = "productId",
        onDelete = CASCADE))
public class ConsumedEntries {
    @PrimaryKey(autoGenerate = true)
    public final int id;
    public final int productId;
    public final int amount;
    @TypeConverters({DateConverter.class}) public final Date date;
    public final String name;

    public ConsumedEntries(final int id, final int amount, final Date date, final String name, final int productId) {
        this.id = id;
        this.productId = productId;
        this.amount = amount;
        this.date = date;
        this.name = name;
    }
}
