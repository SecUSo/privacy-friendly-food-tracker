package org.secuso.privacyfriendlyfoodtracker.network.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Java Model for the JSON Open Food Facts response. Representation a product.
 */
public class ProductResponse {

    public List<NetworkProduct> getProducts() {
        return products;
    }

    String skip;
    String count;
    String page;
    String page_size;
    List<NetworkProduct> products;

    public ProductResponse() {
        products = new ArrayList<NetworkProduct>();
    }

    public static ProductResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        ProductResponse productResponse = gson.fromJson(response, ProductResponse.class);
        return productResponse;
    }


}
