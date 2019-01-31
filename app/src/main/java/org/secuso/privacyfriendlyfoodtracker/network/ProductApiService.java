package org.secuso.privacyfriendlyfoodtracker.network;

import org.secuso.privacyfriendlyfoodtracker.network.models.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApiService {

    @GET("/cgi/search.pl?product_size=1&search_simple=0&action=process&json=1")
    Call<ProductResponse> listProducts(@Query("search_terms") String productName);

    @GET("/cgi/search.pl?product_size=1&search_simple=0&action=process&json=1")
    Call<ProductResponse> listProductsFromPage(@Query("search_terms") String productName, @Query("page") String page );
}