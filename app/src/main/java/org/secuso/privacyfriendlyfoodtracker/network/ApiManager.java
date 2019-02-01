package org.secuso.privacyfriendlyfoodtracker.network;


import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creates the Open Food Facts API Service to send out network requests to the Open Food Facts Web-API.
 */
public class ApiManager implements IApiManager {

    private static ApiManager instance;
    private ProductApiService productApiService;


    public static ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }

        return instance;
    }

    private ApiManager() {
    }

    /**
     * Returns the product api service. Uses the current default language code (de=Germany search, otherwise=Global search) to define the base url.
     *
     * @return product api service
     */
    @Override
    public ProductApiService getProductApiService() {
        if (productApiService == null) {
            productApiService = createProductApiService();
        }

        return productApiService;
    }

    /**
     * Returns the product api service.
     *
     * @param languageCode the locale on which the products will be searched (de=Germany search, otherwise=Global search) to define the base url
     * @return product api service
     */
    @Override
    public ProductApiService getProductApiService(String languageCode) {
        if (productApiService == null) {
            productApiService = createProductApiService(languageCode);
        }

        return productApiService;
    }

    private ProductApiService createProductApiService() {
        String languageCode = Locale.getDefault().getLanguage();
        if (languageCode == "de") {
            languageCode = "de";
        } else {
            languageCode = "world";
        }
        productApiService = createProductApiService(languageCode);

        return productApiService;
    }


    private ProductApiService createProductApiService(String languageCode) {
        productApiService = new Retrofit.Builder()
                .baseUrl("https://" + languageCode + ".openfoodfacts.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ProductApiService.class);

        return productApiService;
    }
}