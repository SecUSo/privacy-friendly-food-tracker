package org.secuso.privacyfriendlyfoodtracker.network;

import org.secuso.privacyfriendlyfoodtracker.network.models.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductApiService {

    @GET("/cgi/search.pl?search_terms=banane&product_size=1&search_simple=1&action=process&json=1")
    Call<List<ProductResponse>> listProducts();
}