package org.secuso.privacyfriendlyfoodtracker.network;

/**
 */
public interface IApiManager {

    ProductApiService getProductApiService();

    ProductApiService getProductApiService(String languageCode);
}