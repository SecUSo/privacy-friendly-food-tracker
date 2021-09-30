package org.secuso.privacyfriendlyfoodtracker.network.utils;

import android.content.Context;

import org.secuso.privacyfriendlyfoodtracker.database.Product;
import org.secuso.privacyfriendlyfoodtracker.ui.FoodInfo;
import org.secuso.privacyfriendlyfoodtracker.ui.FoodInfosToShow;

import java.util.HashMap;
import java.util.Map;

import static org.secuso.privacyfriendlyfoodtracker.helpers.MapHelper.getOrDefault;

/**
 * Helper for conversion from NetworkProduct to the database Product object.
 *
 * @author Andre Lutz
 */
public class ProductConversionHelper {
    public static Product conversionProduct(org.secuso.privacyfriendlyfoodtracker.network.models.NetworkProduct product, Context context) {
        String energyS = product.getNutrimentEnergy();
        if (energyS.equals("")) {
            return null;
        }
        float energy_100g;
        try {
            energy_100g = Integer.parseInt(product.getNutrimentEnergy()) / 4.184f;
        } catch (NumberFormatException e){
            energy_100g = Float.parseFloat(product.getNutrimentEnergy()) / 4.184f;
        }

        Map<String, Float> fieldValuesinGper100g =new HashMap<>();
        for(Map.Entry<String, FoodInfo> foodInfoEntry : FoodInfosToShow.getAllFoodInfosAsMap(context).entrySet()){
            String amountInGtext = product.getNutrimentInGper100gByKey(foodInfoEntry.getKey());
            float amountInG = 0;
            if(!"".equals(amountInGtext)){
                try {
                    amountInG = Integer.parseInt(amountInGtext);
                } catch (NumberFormatException e){
                    amountInG = Float.parseFloat(amountInGtext);
                }
            }
            fieldValuesinGper100g.put(foodInfoEntry.getKey(),amountInG);
            
        }

        // if the id is equals to 0 then the room database creates a new id (primary key)
        return new Product(0, product.getProductName(), energy_100g, getOrDefault(fieldValuesinGper100g, "carbs", 0.0f), getOrDefault(fieldValuesinGper100g, "sugar", 0.0f), getOrDefault(fieldValuesinGper100g, "protein", 0.0f), getOrDefault(fieldValuesinGper100g, "fat", 0.0f), getOrDefault(fieldValuesinGper100g, "satFat", 0.0f), getOrDefault(fieldValuesinGper100g, "salt", 0.0f), getOrDefault(fieldValuesinGper100g, "fiber", 0.0f), getOrDefault(fieldValuesinGper100g, "vitaminA_retinol", 0.0f), getOrDefault(fieldValuesinGper100g, "betaCarotin", 0.0f), getOrDefault(fieldValuesinGper100g, "vitaminD", 0.0f), getOrDefault(fieldValuesinGper100g, "vitaminE", 0.0f), getOrDefault(fieldValuesinGper100g, "vitaminK", 0.0f), getOrDefault(fieldValuesinGper100g, "thiamin_B1", 0.0f), getOrDefault(fieldValuesinGper100g, "riboflavin_B2", 0.0f), getOrDefault(fieldValuesinGper100g, "niacin", 0.0f), getOrDefault(fieldValuesinGper100g, "vitaminB6", 0.0f), getOrDefault(fieldValuesinGper100g, "folat", 0.0f), getOrDefault(fieldValuesinGper100g, "pantothenacid", 0.0f), getOrDefault(fieldValuesinGper100g, "biotin", 0.0f), getOrDefault(fieldValuesinGper100g, "cobalamin_B12", 0.0f), getOrDefault(fieldValuesinGper100g, "vitaminC", 0.0f), getOrDefault(fieldValuesinGper100g, "natrium", 0.0f), getOrDefault(fieldValuesinGper100g, "chlorid", 0.0f), getOrDefault(fieldValuesinGper100g, "kalium", 0.0f), getOrDefault(fieldValuesinGper100g, "calcium", 0.0f), getOrDefault(fieldValuesinGper100g, "phosphor", 0.0f), getOrDefault(fieldValuesinGper100g, "magnesium", 0.0f), getOrDefault(fieldValuesinGper100g, "eisen", 0.0f), getOrDefault(fieldValuesinGper100g, "jod", 0.0f), getOrDefault(fieldValuesinGper100g, "fluorid", 0.0f), getOrDefault(fieldValuesinGper100g, "zink", 0.0f), getOrDefault(fieldValuesinGper100g, "selen", 0.0f), getOrDefault(fieldValuesinGper100g, "kupfer", 0.0f), getOrDefault(fieldValuesinGper100g, "mangan", 0.0f), getOrDefault(fieldValuesinGper100g, "chrom", 0.0f), getOrDefault(fieldValuesinGper100g, "molybdaen", 0.0f), product.getCode());
    }
}
