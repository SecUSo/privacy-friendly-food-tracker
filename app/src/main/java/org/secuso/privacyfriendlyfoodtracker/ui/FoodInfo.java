package org.secuso.privacyfriendlyfoodtracker.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.LinkedHashMap;
import java.util.Map;

/***
 * Contains information about the infos about a food. E.g. there is a FoodInfo("carbs", "g")
 * indicating that food can contain "Carbs" which are measured in gramm.
 */
public class FoodInfo {
    private String name;
    private String unit;

    public FoodInfo(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
