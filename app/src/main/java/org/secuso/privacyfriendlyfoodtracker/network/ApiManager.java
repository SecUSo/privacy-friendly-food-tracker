package org.secuso.privacyfriendlyfoodtracker.network;


import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager implements IApiManager {

    private static ApiManager instance;
    private ProductApiService productApiService;


    public static ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }

        return instance;
    }

    private ApiManager(){
    }

    @Override
    public ProductApiService getProductApiService() {
        if (productApiService == null) {
            productApiService = createProductApiService();
        }

        return productApiService;
    }


    private ProductApiService createProductApiService() {
        String languageCode = Locale.getDefault().getLanguage();
        if(languageCode == "de"){
            languageCode = "de";
        }else{
            languageCode = "world";
        }
        productApiService = new Retrofit.Builder()
                .baseUrl("https://"+languageCode+".openfoodfacts.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ProductApiService.class);

        return productApiService;
    }
}