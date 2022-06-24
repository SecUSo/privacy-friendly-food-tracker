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
    private String offKey;

    public FoodInfo(String name, String unit, String offKey) {
        this.name = name;
        this.unit = unit;
        this.offKey = offKey;
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

    public String getOffKey() {
        return offKey;
    }

    public void setOffKey(String offKey) {
        this.offKey = offKey;
    }

    /***
     * Takes an amount in this.unit units and converts it to grams. E.g. 100mg become 0.1g
     * class member, so some special food infos which use e.g. IU or something like that where
     * conversion depends on the substance.
     * @param amountInUnits
     * @return
     */
    public float convertAmountInUnitsToGrams(float amountInUnits) throws NoConversionDefinedException {
        switch(unit){
            case "g":
                return amountInUnits;
            case "mg":
                return amountInUnits/1000;
            default:
                throw new NoConversionDefinedException();
        }
    }
    /***
     * Takes an amount in this.unit units and converts it to grams. E.g. 100mg become 0.1g
     * @param amountInGrams
     * @return
     */
    public float convertAmountInGramsToUnits(float amountInGrams) throws NoConversionDefinedException {
        switch(unit){
            case "g":
                return amountInGrams;
            case "mg":
                return amountInGrams*1000;
            default:
                throw new NoConversionDefinedException();
        }
    }

    /***
     * gets thrown when a FoodInfo has a unit for which no conversion route was given in convertAmountInUnitsToGrams or
     */
    public class NoConversionDefinedException extends Exception {
    }
}
