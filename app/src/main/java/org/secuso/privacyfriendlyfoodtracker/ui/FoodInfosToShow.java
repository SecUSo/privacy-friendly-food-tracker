package org.secuso.privacyfriendlyfoodtracker.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.secuso.privacyfriendlyfoodtracker.database.Product;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * This class provides access to all the possible food infos (nutriments, vitamins etc) which the
 * user can choose to display in the app.
 */
public class FoodInfosToShow {

    /***
     * Idea for later: If the need arises to allow users to change order of food infos:
     * Implement getter method which uses ordered list of food info keys and returns a new
     * linkedHashMap, where entries from foodInfo map are inserted according to the ordered list of
     * food info keys.
     */
    public static Map<String,FoodInfo> foodInfos=new LinkedHashMap<>(); // LinkedHashMap so entries are ordered by insertion
    static {
        //TODO check whether all units are correct, especially vitamins etc. maybe some of them are rather micro than milligram
        foodInfos.put("carbs", new FoodInfo("Carbs", "g"));
        foodInfos.put("sugar", new FoodInfo("Sugar", "g"));
        foodInfos.put("protein", new FoodInfo("Protein", "g"));
        foodInfos.put("fat", new FoodInfo("Fat", "g"));
        foodInfos.put("satFat", new FoodInfo("SatFat", "g"));
        foodInfos.put("salt", new FoodInfo("Salt", "g"));
        foodInfos.put("vitaminA_retinol", new FoodInfo("Vitamin A", "mg"));
        foodInfos.put("betaCarotin", new FoodInfo("Beta-Carotin", "mg"));
        foodInfos.put("vitaminD", new FoodInfo("Vitamin D", "mg"));
        foodInfos.put("vitaminE", new FoodInfo("Vitamin E", "mg"));
        foodInfos.put("vitaminK", new FoodInfo("Vitamin K", "mg"));
        foodInfos.put("thiamin_B1", new FoodInfo("Vitamin B1", "mg"));
        foodInfos.put("riboflavin_B2", new FoodInfo("Vitamin B2", "mg"));
        foodInfos.put("niacin", new FoodInfo("Niacin", "mg"));
        foodInfos.put("vitaminB6", new FoodInfo("Vitamin B6", "mg"));
        foodInfos.put("folat", new FoodInfo("Folat", "mg"));
        foodInfos.put("pantothenacid", new FoodInfo("Pantothenacid", "mg"));
        foodInfos.put("biotin", new FoodInfo("Biotin", "mg"));
        foodInfos.put("cobalamin_B12", new FoodInfo("Vitamin B12", "mg"));
        foodInfos.put("vitaminC", new FoodInfo("Vitamin C", "mg"));
        foodInfos.put("natrium", new FoodInfo("Natrium", "mg"));
        foodInfos.put("chlorid", new FoodInfo("Chlorid", "mg"));
        foodInfos.put("kalium", new FoodInfo("Kalium", "mg"));
        foodInfos.put("calcium", new FoodInfo("Calcium", "mg"));
        foodInfos.put("phosphor", new FoodInfo("Phosphor", "mg"));
        foodInfos.put("magnesium", new FoodInfo("Magnesium", "mg"));
        foodInfos.put("eisen", new FoodInfo("Eisen", "mg"));
        foodInfos.put("jod", new FoodInfo("Jod", "mg"));
        foodInfos.put("fluorid", new FoodInfo("Fluorid", "mg"));
        foodInfos.put("zink", new FoodInfo("Zink", "mg"));
        foodInfos.put("selen", new FoodInfo("Selen", "mg"));
        foodInfos.put("kupfer", new FoodInfo("Kupfer", "mg"));
        foodInfos.put("mangan", new FoodInfo("Mangan", "mg"));
        foodInfos.put("chrom", new FoodInfo("Chrom", "mg"));
        foodInfos.put("molybdaen", new FoodInfo("Molybdaen", "mg"));
    }



    public static List<FoodInfo> getFoodInfosShown(Context context){
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        List<FoodInfo> foodInfosToShow = new ArrayList<>();
        for(Map.Entry<String,FoodInfo> foodInfoEntry : foodInfos.entrySet()){
            if(sharedPreferences.getBoolean(foodInfoEntry.getKey(),false)){
                foodInfosToShow.add(foodInfoEntry.getValue());
            }
        }
        return foodInfosToShow;
    }

    /***
     * Returns a Map containing all FoodInfos that shall be shown in the app. E.g. if the user wished
     * to show Carbs and Fat, the returned Map would consist of <"carbs", FoodInfo(...)> and<"fat",FoodInfo>.
     * @param context
     * @return
     */
    public static Map<String, FoodInfo> getFoodInfosShownAsMap(Context context){
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, FoodInfo> foodInfosToShow = new LinkedHashMap<>(); //LinkedHashMap to keep order
        for(Map.Entry<String,FoodInfo> foodInfoEntry : foodInfos.entrySet()){
            if(sharedPreferences.getBoolean(foodInfoEntry.getKey(),false)){
                foodInfosToShow.put(foodInfoEntry.getKey(),foodInfoEntry.getValue());
            }
        }
        return foodInfosToShow;
    }



    /***
     * Given a key identifying a FoodInfo and a DatabaseEntry containing all FoodInfoValues, return
     * the specified FoodInfo's value from the DatabaseEntry. Possible keys are those present in
     * FoodInfosToShow.foodInfos, e.g. "carbs".
     *
     * Meta: I also thought about implementing getting a FoodInfo's value from a DatabaseEntry by
     * giving each FoodInfo-object a strategy object which offers a method to retrieve the value. That
     * way would be a little more object-oriented, but maybe too convoluted, so i stuck with this simple
     * key lookup.
     * @param databaseEntry
     * @param key
     * @return
     */
    public static float getFoodInfoValueByKey(DatabaseEntry databaseEntry, String key){
        switch(key){
            case "carbs":
                return databaseEntry.carbs;
            case "sugar":
                    return databaseEntry.sugar;
            case "protein":
                return databaseEntry.protein;
            case "fat":
                return databaseEntry.fat;
            case "satFat":
                return databaseEntry.satFat;
            case "salt":
                return databaseEntry.salt;
            case "vitaminA_retinol":
                return databaseEntry.vitaminA_retinol;
            case "betaCarotin":
                return databaseEntry.betaCarotin;
            case "vitaminD":
                return databaseEntry.vitaminD;
            case "vitaminE":
                return databaseEntry.vitaminE;
            case "vitaminK":
                return databaseEntry.vitaminK;
            case "thiamin_B1":
                return databaseEntry.thiamin_B1;
            case "riboflavin_B2":
                return databaseEntry.riboflavin_B2;
            case "niacin":
                return databaseEntry.niacin;
            case "vitaminB6":
                return databaseEntry.vitaminB6;
            case "folat":
                return databaseEntry.folat;
            case "pantothenacid":
                return databaseEntry.pantothenacid;
            case "biotin":
                return databaseEntry.biotin;
            case "cobalamin_B12":
                return databaseEntry.cobalamin_B12;
            case "vitaminC":
                return databaseEntry.vitaminC;
            case "natrium":
                return databaseEntry.natrium;
            case "chlorid":
                return databaseEntry.chlorid;
            case "kalium":
                return databaseEntry.kalium;
            case "calcium":
                return databaseEntry.calcium;
            case "phosphor":
                return databaseEntry.phosphor;
            case "magnesium":
                return databaseEntry.magnesium;
            case "eisen":
                return databaseEntry.eisen;
            case "jod":
                return databaseEntry.jod;
            case "fluorid":
                return databaseEntry.fluorid;
            case "zink":
                return databaseEntry.zink;
            case "selen":
                return databaseEntry.selen;
            case "kupfer":
                return databaseEntry.kupfer;
            case "mangan":
                return databaseEntry.mangan;
            case "chrom":
                return databaseEntry.chrom;
            case "molybdaen":
                return databaseEntry.molybdaen;
        }
        return 0.0f;
    }
    /***
     * Given a key identifying a FoodInfo and a Product containing all FoodInfoValues, return
     * the specified FoodInfo's value from the Product. Possible keys are those present in
     * FoodInfosToShow.foodInfos, e.g. "carbs".
     *
     * Meta: This does exactly the same like getFoodInfoValueByKey(DatabaseEntry,String), just for the
     * Product class. It would probably be nicer to just have Product and DatabaseEntry implement an
     * interface offering getters for every food info. TODO.
     * @param product
     * @param key
     * @return
     */
    public static float getFoodInfoValueByKey(Product product, String key){
        switch(key){
            case "carbs":
                return product.carbs;
            case "sugar":
                return product.sugar;
            case "protein":
                return product.protein;
            case "fat":
                return product.fat;
            case "satFat":
                return product.satFat;
            case "salt":
                return product.salt;
            case "vitaminA_retinol":
                return product.vitaminA_retinol;
            case "betaCarotin":
                return product.betaCarotin;
            case "vitaminD":
                return product.vitaminD;
            case "vitaminE":
                return product.vitaminE;
            case "vitaminK":
                return product.vitaminK;
            case "thiamin_B1":
                return product.thiamin_B1;
            case "riboflavin_B2":
                return product.riboflavin_B2;
            case "niacin":
                return product.niacin;
            case "vitaminB6":
                return product.vitaminB6;
            case "folat":
                return product.folat;
            case "pantothenacid":
                return product.pantothenacid;
            case "biotin":
                return product.biotin;
            case "cobalamin_B12":
                return product.cobalamin_B12;
            case "vitaminC":
                return product.vitaminC;
            case "natrium":
                return product.natrium;
            case "chlorid":
                return product.chlorid;
            case "kalium":
                return product.kalium;
            case "calcium":
                return product.calcium;
            case "phosphor":
                return product.phosphor;
            case "magnesium":
                return product.magnesium;
            case "eisen":
                return product.eisen;
            case "jod":
                return product.jod;
            case "fluorid":
                return product.fluorid;
            case "zink":
                return product.zink;
            case "selen":
                return product.selen;
            case "kupfer":
                return product.kupfer;
            case "mangan":
                return product.mangan;
            case "chrom":
                return product.chrom;
            case "molybdaen":
                return product.molybdaen;
        }
        return 0.0f;
    }
}
