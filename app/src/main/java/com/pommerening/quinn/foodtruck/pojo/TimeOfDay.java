package com.pommerening.quinn.foodtruck.pojo;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Quinn Pommerening on 1/21/2016.
 */
public class TimeOfDay {
    private final String TAG = "SCANNER LOOP";

    public String getTimeOfDay() {
        String morning = "Good Morning!";
        String afternoon = "Good Afternoon!";
        String evening = "Good Evening!";

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String time = sdf.format(cal.getTime());

        int hour = Integer.parseInt(time);
        Log.d(TAG, "This is hour: " + hour);
        if(hour >= 0 && hour <= 12) {
            return morning ;
        } else if (hour >= 13 && hour <= 17) {
            return afternoon;
        } else {
            return evening;
        }
    }
}
