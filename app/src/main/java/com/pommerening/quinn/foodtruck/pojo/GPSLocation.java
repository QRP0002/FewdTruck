package com.pommerening.quinn.foodtruck.pojo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
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
    private static GPSLocation instance;

    public static GPSLocation locationSingleton(Context context) {
        if(instance == null) {
            instance = new GPSLocation(context);
        }
        return instance;
    }

    private GPSLocation(Context context) {
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

    @Override
    public void changeSettings() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("GPS Settings");
        alertDialog.setMessage("GPS is not enabled. Would you like to enable it?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
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
