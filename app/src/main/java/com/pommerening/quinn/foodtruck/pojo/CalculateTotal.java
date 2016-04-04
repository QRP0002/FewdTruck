package com.pommerening.quinn.foodtruck.pojo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Quinn Pommerening on 4/4/2016.
 */
public class CalculateTotal {
    private ArrayList<HashMap<String, String>> orderList;
    private static final String TAG_PRODUCTPRICE = "_ProductPrice";

    public CalculateTotal(ArrayList<HashMap<String, String>> orderList) {
        this.orderList = orderList;
    }

    public String findTotal() {
        double total = 0.0;

        for (HashMap<String, String> values : this.orderList) {
            for(Map.Entry<String, String> entry : values.entrySet()) {
                if(entry.getKey().equals(TAG_PRODUCTPRICE)) {
                    total += Double.parseDouble(entry.getValue());
                }
            }
        }
        DecimalFormat dc = new DecimalFormat("$##0.00");
        return "Total:   " + dc.format(total);
    }
}
