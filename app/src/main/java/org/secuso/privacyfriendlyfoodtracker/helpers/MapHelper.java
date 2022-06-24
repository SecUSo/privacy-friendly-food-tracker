package org.secuso.privacyfriendlyfoodtracker.helpers;

import java.util.Map;

/***
 * Atm only for holding one helper function
 */
public class MapHelper {
    /***
     * Map.getOrDefault does not exist in Api<24 :/
     * @param map
     * @param key
     * @param defaultValue
     * @return the mapped value or the default, if no mapping exists
     */
    public static Float getOrDefault(Map<String, Float> map, String key, float defaultValue){
        Float ret = map.get(key);
        if(ret==null){
            ret = defaultValue;
        }
        return ret;
    }
}
