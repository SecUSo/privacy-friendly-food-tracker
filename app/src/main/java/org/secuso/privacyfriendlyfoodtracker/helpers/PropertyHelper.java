package org.secuso.privacyfriendlyfoodtracker.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyHelper {

    private final static String IMPEX_PROPERTY_FILENAME = "dbimpex.properties";

    public static String getImpExProperty(String key, Context context) throws IOException {
        Properties properties = new Properties();
        ;
        AssetManager assetManager = context.getAssets();
        try {

            InputStream inputStream = assetManager.open(IMPEX_PROPERTY_FILENAME);
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (Exception e) {
            Log.e(PropertyHelper.class.toString(), e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
}