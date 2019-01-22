package org.secuso.privacyfriendlyfoodtracker.network.utils;

import org.secuso.privacyfriendlyfoodtracker.database.Product;

public class ProductConversionHelper {
    public static Product conversionProduct(org.secuso.privacyfriendlyfoodtracker.network.models.NetworkProduct product) {
        String energyS = product.getNutrimentEnergy();
        if(energyS == ""){
            return null;
        }
        int energy_100g;
        try {
            energy_100g = Integer.parseInt(product.getNutrimentEnergy());
        } catch (NumberFormatException e){
            float energy = Float.parseFloat(product.getNutrimentEnergy());
            energy_100g = (int) energy;
        }
        Product product1 = new Product(0, product.getProductName(), energy_100g,  product.getCode()); // TODO: read correct values
        return product1;
    }
}
