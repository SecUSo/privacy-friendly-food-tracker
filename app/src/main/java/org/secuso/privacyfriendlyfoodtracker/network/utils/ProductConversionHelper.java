package org.secuso.privacyfriendlyfoodtracker.network.utils;

import org.secuso.privacyfriendlyfoodtracker.database.Product;

/**
 * Helper for conversion from NetworkProduct to the database Product object.
 *
 * @author Andre Lutz
 */
public class ProductConversionHelper {
    public static Product conversionProduct(org.secuso.privacyfriendlyfoodtracker.network.models.NetworkProduct product) {
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

        String carbsS = product.getNutrimentCarbs();
        float carbs = 0;
        if(!"".equals(carbsS)){
            try {
                carbs = Integer.parseInt(carbsS);
            } catch (NumberFormatException e){
                carbs = Float.parseFloat(carbsS);
            }
        }

        String sugarS = product.getNutrimentSugar();
        float sugar = 0;
        if(!"".equals(sugarS)){
            try {
                sugar = Integer.parseInt(sugarS);
            } catch (NumberFormatException e){
                sugar = Float.parseFloat(sugarS);
            }
        }

        String proteinS = product.getNutrimentProtein();
        float protein = 0;
        if(!"".equals(proteinS)){
            try {
                protein = Integer.parseInt(proteinS);
            } catch (NumberFormatException e){
                protein = Float.parseFloat(proteinS);
            }
        }

        String fatS = product.getNutrimentFat();
        float fat = 0;
        if(!"".equals(fatS)){
            try {
                fat = Integer.parseInt(fatS);
            } catch (NumberFormatException e){
                fat = Float.parseFloat(fatS);
            }
        }

        String satFatS = product.getNutrimentSatFat();
        float satFat = 0;
        if(!"".equals(satFatS)){
            try {
                satFat = Integer.parseInt(satFatS);
            } catch (NumberFormatException e){
                satFat = Float.parseFloat(satFatS);
            }
        }

        // if the id is equals to 0 then the room database creates a new id (primary key)
        return new Product(0, product.getProductName(), energy_100g, carbs, sugar, protein, fat, satFat, product.getCode());
    }
}
