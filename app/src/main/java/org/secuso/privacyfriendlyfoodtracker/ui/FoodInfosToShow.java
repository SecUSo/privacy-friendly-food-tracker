package org.secuso.privacyfriendlyfoodtracker.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntrieAndProductDao;
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

    public static final String GOAL_DONT_EXCEED = "daily_goal_dont_exceed";
    /***
     * The prefix of the preference key storing food info's daily goal.
     */
    static final String GOAL_KEY_PREFIX = "daily_goal_";
    /***
     * Idea for later: If the need arises to allow users to change order of food infos:
     * Implement getter method which uses ordered list of food info keys and returns a new
     * linkedHashMap, where entries from foodInfo map are inserted according to the ordered list of
     * food info keys.
     *
     * How to add further foodInfos:
     *   in FoodInfosToShow:
     *   - in static block add new FoodInfo to map (foodInfos.put("carbs", new FoodInfo("Carbs", "g", "carbohydrates"));)
     *   - add key in getFoodInfoValueByKey
     *   in Product:
     *   - add fields, modify constructors etc.
     *   DatabaseFacade:
     *   - add new parameter to insertProductPrivate
     *   - insertEntry
     *   AddFoodFragment
     *   - makedatabasentry
     *   ProductDao
     *   - findexistingproduct
     *   DatabaseEntry
     *   - add field
     *   ConsumedEntrieAndProductDao
     *
     *
     */
    private static Map<String,FoodInfo> foodInfos=new LinkedHashMap<>(); // LinkedHashMap so entries are ordered by insertion

    public static Map<String,FoodInfo> getFoodInfos(Context context){
        populateFoodInfoMap(context);
        return foodInfos;
    }
    /***
     * populates the foodInfos Map with all the foodInfos. used to be a static block, but to access
     * string ressources and thus make nutriment names translatable, was made a function with access
     * to a context. Every method in here calls this method before doing anything else.
     * @param context
     */
    static void populateFoodInfoMap(Context context) {
        //TODO check whether all units are sensible
        if(!foodInfos.isEmpty()){
            return;
        }
        foodInfos.put("carbs", new FoodInfo(context.getString(R.string.nutriment_name_carbs), "g", "carbohydrates"));
        foodInfos.put("sugar", new FoodInfo(context.getString(R.string.nutriment_name_sugar), "g", "sugars"));
        foodInfos.put("protein", new FoodInfo(context.getString(R.string.nutriment_name_protein), "g", "proteins"));
        foodInfos.put("fat", new FoodInfo(context.getString(R.string.nutriment_name_fat), "g", "fat"));
        foodInfos.put("satFat", new FoodInfo(context.getString(R.string.nutriment_name_sat_fat), "g", "saturated-fat"));
        foodInfos.put("salt", new FoodInfo(context.getString(R.string.nutriment_name_salt), "g", "salt"));
        foodInfos.put("fiber", new FoodInfo(context.getString(R.string.nutriment_name_fiber), "g", "fiber"));
        foodInfos.put("vitaminA_retinol", new FoodInfo(context.getString(R.string.nutriment_name_vitamin_a), "mg", "vitamin-a"));
        foodInfos.put("betaCarotin", new FoodInfo(context.getString(R.string.nutriment_name_beta_carotene), "mg", null));
        foodInfos.put("vitaminD", new FoodInfo(context.getString(R.string.nutriment_name_vitamin_d), "mg", "vitamin-d"));
        foodInfos.put("vitaminE", new FoodInfo(context.getString(R.string.nutriment_name_vitamin_e), "mg", "vitamin-e"));
        foodInfos.put("vitaminK", new FoodInfo(context.getString(R.string.nutriment_name_vitamin_k), "mg", "vitamin-k"));
        foodInfos.put("thiamin_B1", new FoodInfo(context.getString(R.string.nutriment_name_vitamin_b1), "mg", "vitamin-b1"));
        foodInfos.put("riboflavin_B2", new FoodInfo(context.getString(R.string.nutriment_name_vitamin_b2), "mg", "vitamin-b2"));
        foodInfos.put("niacin", new FoodInfo(context.getString(R.string.nutriment_name_niacin), "mg", null));
        foodInfos.put("vitaminB6", new FoodInfo(context.getString(R.string.nutriment_name_vitamin_b6), "mg", "vitamin-b6"));
        foodInfos.put("folat", new FoodInfo(context.getString(R.string.nutriment_name_folate), "mg", null));
        foodInfos.put("pantothenacid", new FoodInfo(context.getString(R.string.nutriment_name_pantothenic_acid), "mg", "pantothenic-acid"));
        foodInfos.put("biotin", new FoodInfo(context.getString(R.string.nutriment_name_biotin), "mg", "biotin"));
        foodInfos.put("cobalamin_B12", new FoodInfo(context.getString(R.string.nutriment_name_vitamin_b12), "mg", "vitamin-b12"));
        foodInfos.put("vitaminC", new FoodInfo(context.getString(R.string.nutriment_name_vitamin_c), "mg", "vitamin-c"));
        foodInfos.put("natrium", new FoodInfo(context.getString(R.string.nutriment_name_natrium), "mg", "sodium"));
        foodInfos.put("chlorid", new FoodInfo(context.getString(R.string.nutriment_name_chloride), "mg", "chloride"));
        foodInfos.put("kalium", new FoodInfo(context.getString(R.string.nutriment_name_potassium), "mg", "potassium"));
        foodInfos.put("calcium", new FoodInfo(context.getString(R.string.nutriment_name_calcium), "mg", "calcium")); 
        foodInfos.put("phosphor", new FoodInfo(context.getString(R.string.nutriment_name_phosphorous), "mg", "phosphorus"));
        foodInfos.put("magnesium", new FoodInfo(context.getString(R.string.nutriment_name_magnesium), "mg", "magnesium"));
        foodInfos.put("eisen", new FoodInfo(context.getString(R.string.nutriment_name_iron), "mg", "iron"));
        foodInfos.put("jod", new FoodInfo(context.getString(R.string.nutriment_name_iodine), "mg", "iodine"));
        foodInfos.put("fluorid", new FoodInfo(context.getString(R.string.nutriment_name_fluoride), "mg", "fluoride"));
        foodInfos.put("zink", new FoodInfo(context.getString(R.string.nutriment_name_zinc), "mg", "zinc"));
        foodInfos.put("selen", new FoodInfo(context.getString(R.string.nutriment_name_selenium), "mg", "selenium"));
        foodInfos.put("kupfer", new FoodInfo(context.getString(R.string.nutriment_name_copper), "mg", "copper"));
        foodInfos.put("mangan", new FoodInfo(context.getString(R.string.nutriment_name_manganese), "mg", "manganese"));
        foodInfos.put("chrom", new FoodInfo(context.getString(R.string.nutriment_name_chromium), "mg", "chromium"));
        foodInfos.put("molybdaen", new FoodInfo(context.getString(R.string.nutriment_name_molybdenum), "mg", "molybdenum"));
    }




    /***
     * Returns a Map containing all FoodInfos that shall be shown in the app. E.g. if the user wished
     * to show Carbs and Fat, the returned Map would consist of <"carbs", FoodInfo(...)> and<"fat",FoodInfo>.
     * @param context
     * @return
     */
    public static Map<String, FoodInfo> getFoodInfosShownAsMap(Context context){
        populateFoodInfoMap(context);

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
     * Given a key identifying a FoodInfo, the FoodInfo object itself and a DatabaseEntry containing all FoodInfoValues, return
     * the specified FoodInfo's value from the DatabaseEntry converted to the FoodInfo objects unit. Possible keys are those present in
     * FoodInfosToShow.foodInfos, e.g. "carbs".
     *
     * Meta: I also thought about implementing getting a FoodInfo's value from a DatabaseEntry by
     * giving each FoodInfo-object a strategy object which offers a method to retrieve the value. That
     * way would be a little more object-oriented, but maybe too convoluted, so i stuck with this simple
     * key lookup.
     * @param databaseEntry
     * @param key to decide which membervariable of databaseEntry should be used
     * @param foodInfo to convert the Value to foodInfo.unit before returning it
     * @param context to make sure foodInfos have beeen populated
     * @return The nutriments value in foodInfo.unit - if the key does not match any, 0.0f is returned and an error written to log.
     */
    public static float getFoodInfoValueByKey(DatabaseEntry databaseEntry, String key, FoodInfo foodInfo, Context context){
        populateFoodInfoMap(context);
        float value=0.0f;
        switch(key){
            case "carbs":
                value= databaseEntry.carbs;
                break;
            case "sugar":
                value= databaseEntry.sugar;
                break;
            case "protein":
                value= databaseEntry.protein;
                break;
            case "fat":
                value= databaseEntry.fat;
                break;
            case "satFat":
                value= databaseEntry.satFat;
                break;
            case "salt":
                value= databaseEntry.salt;
                break;
            case "fiber":
                value= databaseEntry.fiber;
                break;
            case "vitaminA_retinol":
                value= databaseEntry.vitaminA_retinol;
                break;
            case "betaCarotin":
                value= databaseEntry.betaCarotin;
                break;
            case "vitaminD":
                value= databaseEntry.vitaminD;
                break;
            case "vitaminE":
                value= databaseEntry.vitaminE;
                break;
            case "vitaminK":
                value= databaseEntry.vitaminK;
                break;
            case "thiamin_B1":
                value= databaseEntry.thiamin_B1;
                break;
            case "riboflavin_B2":
                value= databaseEntry.riboflavin_B2;
                break;
            case "niacin":
                value= databaseEntry.niacin;
                break;
            case "vitaminB6":
                value= databaseEntry.vitaminB6;
                break;
            case "folat":
                value= databaseEntry.folat;
                break;
            case "pantothenacid":
                value= databaseEntry.pantothenacid;
                break;
            case "biotin":
                value= databaseEntry.biotin;
                break;
            case "cobalamin_B12":
                value= databaseEntry.cobalamin_B12;
                break;
            case "vitaminC":
                value= databaseEntry.vitaminC;
                break;
            case "natrium":
                value= databaseEntry.natrium;
                break;
            case "chlorid":
                value= databaseEntry.chlorid;
                break;
            case "kalium":
                value= databaseEntry.kalium;
                break;
            case "calcium":
                value= databaseEntry.calcium;
                break;
            case "phosphor":
                value= databaseEntry.phosphor;
                break;
            case "magnesium":
                value= databaseEntry.magnesium;
                break;
            case "eisen":
                value= databaseEntry.eisen;
                break;
            case "jod":
                value= databaseEntry.jod;
                break;
            case "fluorid":
                value= databaseEntry.fluorid;
                break;
            case "zink":
                value= databaseEntry.zink;
                break;
            case "selen":
                value= databaseEntry.selen;
                break;
            case "kupfer":
                value= databaseEntry.kupfer;
                break;
            case "mangan":
                value= databaseEntry.mangan;
                break;
            case "chrom":
                value= databaseEntry.chrom;
                break;
            case "molybdaen":
                value= databaseEntry.molybdaen;
                break;
        }
        try {
            value = foodInfo.convertAmountInGramsToUnits(value);
        } catch (FoodInfo.NoConversionDefinedException e) {
            Log.e("FoodInfosToShow", "getFoodInfoValueByKey: No conversion for FoodInfo with key "+key, e);
        }
        return value;
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
     * @param foodInfo
     * @param context to make sure foodInfos have beeen populated
     * @return The nutriments value in foodInfo.unit - if the key does not match any, 0.0f is returned and an error written to log.
     */
    public static float getFoodInfoValueByKey(Product product, String key, FoodInfo foodInfo, Context context){
        populateFoodInfoMap(context);
        float value = 0.0f;
        switch(key){
            case "carbs":
                value =  product.carbs;
                break;
            case "sugar":
                value =  product.sugar;
                break;
            case "protein":
                value =  product.protein;
                break;
            case "fat":
                value =  product.fat;
                break;
            case "satFat":
                value =  product.satFat;
                break;
            case "salt":
                value =  product.salt;
                break;
            case "fiber":
                value =  product.fiber;
                break;
            case "vitaminA_retinol":
                value =  product.vitaminA_retinol;
                break;
            case "betaCarotin":
                value =  product.betaCarotin;
                break;
            case "vitaminD":
                value =  product.vitaminD;
                break;
            case "vitaminE":
                value =  product.vitaminE;
                break;
            case "vitaminK":
                value =  product.vitaminK;
                break;
            case "thiamin_B1":
                value =  product.thiamin_B1;
                break;
            case "riboflavin_B2":
                value =  product.riboflavin_B2;
                break;
            case "niacin":
                value =  product.niacin;
                break;
            case "vitaminB6":
                value =  product.vitaminB6;
                break;
            case "folat":
                value =  product.folat;
                break;
            case "pantothenacid":
                value =  product.pantothenacid;
                break;
            case "biotin":
                value =  product.biotin;
                break;
            case "cobalamin_B12":
                value =  product.cobalamin_B12;
                break;
            case "vitaminC":
                value =  product.vitaminC;
                break;
            case "natrium":
                value =  product.natrium;
                break;
            case "chlorid":
                value =  product.chlorid;
                break;
            case "kalium":
                value =  product.kalium;
                break;
            case "calcium":
                value =  product.calcium;
                break;
            case "phosphor":
                value =  product.phosphor;
                break;
            case "magnesium":
                value =  product.magnesium;
                break;
            case "eisen":
                value =  product.eisen;
                break;
            case "jod":
                value =  product.jod;
                break;
            case "fluorid":
                value =  product.fluorid;
                break;
            case "zink":
                value =  product.zink;
                break;
            case "selen":
                value =  product.selen;
                break;
            case "kupfer":
                value =  product.kupfer;
                break;
            case "mangan":
                value =  product.mangan;
                break;
            case "chrom":
                value =  product.chrom;
                break;
            case "molybdaen":
                value =  product.molybdaen;
                break;
        }
        try {
            value = foodInfo.convertAmountInGramsToUnits(value);
        } catch (FoodInfo.NoConversionDefinedException e) {
            Log.e("FoodInfosToShow", "getFoodInfoValueByKey: No conversion for FoodInfo with key "+key, e);
        }
        return value;    }

    /***
     * Given a key identifying a FoodInfo and a Product containing all FoodInfoValues, return
     * the specified FoodInfo's value from the Product. use the given FoodInfo object to convert
     * the value to FoodInfo.unit before returning.
     * Possible keys are those present in
     * FoodInfosToShow.foodInfos, e.g. "carbs".
     *
     * Meta: This does exactly the same like getFoodInfoValueByKey(DatabaseEntry,String), just for the
     * Product class. It would probably be nicer to just have Product and DatabaseEntry implement an
     * interface offering getters for every food info. TODO.
     * @param product
     * @param key
     * @param foodInfo used to convert the value to foodInfo.unit
     * @param context to make sure foodInfos have beeen populated
     * @return The nutriments value in foodInfo.unit - if the key does not match any, 0.0f is returned and an error written to log.
     */
    public static float getFoodInfoValueByKey(ConsumedEntrieAndProductDao.DateNutriments product, String key, FoodInfo foodInfo, Context context){
        populateFoodInfoMap(context);
        float value = 0.0f;
        switch(key){
            case "carbs":
                value= product.carbsConsumed;
                break;
            case "sugar":
                value= product.sugarConsumed;
                break;
            case "protein":
                value= product.proteinConsumed;
                break;
            case "fat":
                value= product.fatConsumed;
                break;
            case "satFat":
                value= product.satFatConsumed;
                break;
            case "salt":
                value= product.saltConsumed;
                break;
            case "fiber":
                value= product.fiberConsumed;
                break;
            case "vitaminA_retinol":
                value= product.vitaminA_retinolConsumed;
                break;
            case "betaCarotin":
                value= product.betaCarotinConsumed;
                break;
            case "vitaminD":
                value= product.vitaminDConsumed;
                break;
            case "vitaminE":
                value= product.vitaminEConsumed;
                break;
            case "vitaminK":
                value= product.vitaminKConsumed;
                break;
            case "thiamin_B1":
                value= product.thiamin_B1Consumed;
                break;
            case "riboflavin_B2":
                value= product.riboflavin_B2Consumed;
                break;
            case "niacin":
                value= product.niacinConsumed;
                break;
            case "vitaminB6":
                value= product.vitaminB6Consumed;
                break;
            case "folat":
                value= product.folatConsumed;
                break;
            case "pantothenacid":
                value= product.pantothenacidConsumed;
                break;
            case "biotin":
                value= product.biotinConsumed;
                break;
            case "cobalamin_B12":
                value= product.cobalamin_B12Consumed;
                break;
            case "vitaminC":
                value= product.vitaminCConsumed;
                break;
            case "natrium":
                value= product.natriumConsumed;
                break;
            case "chlorid":
                value= product.chloridConsumed;
                break;
            case "kalium":
                value= product.kaliumConsumed;
                break;
            case "calcium":
                value= product.calciumConsumed;
                break;
            case "phosphor":
                value= product.phosphorConsumed;
                break;
            case "magnesium":
                value= product.magnesiumConsumed;
                break;
            case "eisen":
                value= product.eisenConsumed;
                break;
            case "jod":
                value= product.jodConsumed;
                break;
            case "fluorid":
                value= product.fluoridConsumed;
                break;
            case "zink":
                value= product.zinkConsumed;
                break;
            case "selen":
                value= product.selenConsumed;
                break;
            case "kupfer":
                value= product.kupferConsumed;
                break;
            case "mangan":
                value= product.manganConsumed;
                break;
            case "chrom":
                value= product.chromConsumed;
                break;
            case "molybdaen":
                value= product.molybdaenConsumed;
                break;
        }
        try {
            value = foodInfo.convertAmountInGramsToUnits(value);
        } catch (FoodInfo.NoConversionDefinedException e) {
            Log.e("FoodInfosToShow", "getFoodInfoValueByKey: No conversion for FoodInfo with key "+key, e);
        }
        return value;
    }

    /***
     * Returns the daily goal for a food info by key, if stored in preferences. If no goal is defined,
     * returns null.
     * @param context Context needed to get the preferences.
     * @return null if no goal defined, else the goal.
     */
    public static Float getDailyGoalFromPreferences(Context context, String key){
        populateFoodInfoMap(context);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        String dailyGoalText = sharedPreferences.getString(GOAL_KEY_PREFIX+key,"");
        if(dailyGoalText.equals("")){
            return null;
        }else{
            Float dailyGoal=Float.parseFloat(dailyGoalText);
            return dailyGoal;
        }
    }

    /***
     * return whether the user set the goal to be one that she aims to exceed or not.
     * @param context
     * @param key
     * @return
     */
    public static boolean getDontExceedDailyGoalFromPreferences(Context context, String key){
        populateFoodInfoMap(context);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(GOAL_DONT_EXCEED+key,false);
    }

    public static Map<String,FoodInfo> getAllFoodInfosAsMap(Context context) {
        populateFoodInfoMap(context);
        return foodInfos;
    }
}
