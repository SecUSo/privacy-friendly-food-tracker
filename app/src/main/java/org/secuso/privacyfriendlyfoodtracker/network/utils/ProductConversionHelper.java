package org.secuso.privacyfriendlyfoodtracker.network.utils;

import org.secuso.privacyfriendlyfoodtracker.database.Product;

/**
 * Helper for conversion from NetworkProduct to the database Product.
 */
public class ProductConversionHelper {
    public static Product conversionProduct(org.secuso.privacyfriendlyfoodtracker.network.models.NetworkProduct product) {
        String energyS = product.getNutrimentEnergy();
        if (energyS == "") {
            return null;
        }
        int energy_100g = Integer.parseInt(product.getNutrimentEnergy());
        // if the id equals to 0, then the database creates a new id
        Product product1 = new Product(0, product.getProductName(), energy_100g, product.getCode());
        return product1;
    }
}
