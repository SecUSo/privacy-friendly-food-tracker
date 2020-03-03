package org.secuso.privacyfriendlyfoodtracker.ui.helper;

import java.math.BigDecimal;

public class MathHelper {

    /**
     * Source: https://stackoverflow.com/questions/8911356/whats-the-best-practice-to-round-a-float-to-2-decimals
     * By Carlos Alberto Martínez
     * @param d
     * @param decimalPlace
     * @return
     */
    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
