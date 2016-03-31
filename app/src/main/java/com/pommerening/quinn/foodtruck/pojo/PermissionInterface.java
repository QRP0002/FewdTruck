package com.pommerening.quinn.foodtruck.pojo;

import android.location.Location;

/**
 * Created by Quinn Pommerening on 3/30/2016.
 */
public interface PermissionInterface {
    Location getLocation();
    void stopGPS();
    void changeSettings();
}
