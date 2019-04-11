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
        // if the id is equals to 0 then the room database creates a new id (primary key)
        return new Product(0, product.getProductName(), energy_100g,  product.getCode());
    }
}
