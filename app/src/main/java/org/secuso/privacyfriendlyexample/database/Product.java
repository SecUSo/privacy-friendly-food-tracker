package org.secuso.privacyfriendlyexample.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Product {
    @PrimaryKey(autoGenerate = true)
    public final int id;
    public final String name;
    public final int energy;
    public final String barcode;

    public Product(final int id, String name, int energy, String barcode) {
        this.id = id;
        this.name = name;
        this.energy = energy;
        this.barcode = barcode;
    }
}
