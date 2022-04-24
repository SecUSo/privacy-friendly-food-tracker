package org.secuso.privacyfriendlyfoodtracker.database.importexport;

import org.secuso.privacyfriendlyfoodtracker.database.ConsumedEntries;
import org.secuso.privacyfriendlyfoodtracker.database.Product;

import java.util.List;

public class ImpExModel {
    private List<Product> productList;
    private List<ConsumedEntries> consumedEntriesList;

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<ConsumedEntries> getConsumedEntriesList() {
        return consumedEntriesList;
    }

    public void setConsumedEntriesList(List<ConsumedEntries> consumedEntriesList) {
        this.consumedEntriesList = consumedEntriesList;
    }
}