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
package org.secuso.privacyfriendlyfoodtracker.network;

import org.secuso.privacyfriendlyfoodtracker.network.models.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Defines the Open Food Facts API requests.
 *
 * @author Andre Lutz
 */
public interface ProductApiService {

    @GET("/cgi/search.pl?product_size=1&search_simple=0&action=process&json=1")
    Call<ProductResponse> listProducts(@Query("search_terms") String productName);

    @GET("/cgi/search.pl?product_size=1&search_simple=0&action=process&json=1")
    Call<ProductResponse> listProductsFromPage(@Query("search_terms") String productName, @Query("page") String page );
}