package com.pommerening.quinn.foodtruck.pojo;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Quinn Pommerening on 3/30/2016.
 */
public class GPSLocation extends Service implements LocationListener, PermissionInterface {

    private final Context context;

    private LocationListener locationListener;
    protected LocationManager locationManager;
    private static final int REQUEST_CODE = 200;

    Location location;

    private double latitude;
    private double longitude;
    private static final long MIN_UPDATE_DISTANCE = 10;
    private static final long MIN_UPDATE_TIME = 1000 * 60 * 1;

    boolean isGPSEnabled = false;
    boolean canGetLocation = false;
    boolean isNetworkEnabled = false;

    public GPSLocation(Context context) {
        this.context = context;
        getLocation();
    }

    @Override
    public Location getLocation() {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isGPSEnabled && !isNetworkEnabled) {
            canGetLocation = false;
        } else {
            this.canGetLocation = true;

            if(isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, MIN_UPDATE_TIME,
                        MIN_UPDATE_DISTANCE, this);

                if(locationManager != null) {
                    location = locationManager.getLastKnownLocation(
                            LocationManager.NETWORK_PROVIDER);
                    if(location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }

            if(isGPSEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME,
                        MIN_UPDATE_DISTANCE, this);

                if(locationManager != null) {
                    location = locationManager.getLastKnownLocation(
                            LocationManager.GPS_PROVIDER);

                    if(location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
        }

        return location;
    }

    @Override
    public void stopGPS() {
        if(locationManager != null) {
            locationManager.removeUpdates(GPSLocation.this);
        }
    }

    public double getLatitude() {
        if(location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if(location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void setCanGetLocation(boolean canGetLocation) {
        this.canGetLocation = canGetLocation;
    }



    @Override
    public void changeSettings() {
        Log.d("I will change the", "settings in a dialog here");
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
