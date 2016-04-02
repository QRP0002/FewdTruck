package com.pommerening.quinn.foodtruck.pojo;

import android.util.Log;

/**
 * Created by Quinn Pommerening on 3/31/2016.
 */
public class HaversineFormula {
    private static final double R = 3958.75;
    public static double distFrom (double latIn1, double lngIn1, double latIn2, double lngIn2) {
        Double lat1 = latIn1;
        Double lon1 = lngIn1;
        Double lat2 = latIn2;
        Double lon2 = lngIn2;
        Double latDistance = toRad(lat2-lat1);
        Double lonDistance = toRad(lon2-lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }
}
