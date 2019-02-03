/*
This file is part of Privacy friendly food tracker.

Privacy friendly food tracker is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Privacy friendly food tracker is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Privacy friendly food tracker.  If not, see <https://www.gnu.org/licenses/>.
*/
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
