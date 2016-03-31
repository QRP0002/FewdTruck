package com.pommerening.quinn.foodtruck.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pommerening.quinn.foodtruck.pojo.GPSLocation;
import com.pommerening.quinn.foodtruck.pojo.LocationData;
import com.pommerening.quinn.foodtruck.pojo.PermissionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MiniMapFragment extends SupportMapFragment{
    private SupportMapFragment fragment;
    private GoogleMap map;
    private static View view;
    private static final int REQUEST_CODE = 200;
    private PermissionInterface permission;
    GPSLocation gps;
    private static final String TAG_TRUCK = "truck";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";

    public MiniMapFragment() {
        super();
    }

    public static MiniMapFragment newInstance() {
        MiniMapFragment fragment = new MiniMapFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        gps = GPSLocation.locationSingleton(getActivity());
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
                return;
            }

        } else {
            gps.changeSettings();
        }

        if(gps.canGetLocation()) {

            map = getMap();
            LatLng test = new LatLng(gps.getLatitude(), gps.getLongitude());
            map.addMarker(new MarkerOptions().position(test).title("You"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(test, 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            loadTrucks();
        } else {
            Log.d("Fuck this shit", "Location not true");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        Fragment fragment  = getParentFragment();
        if(fragment != null && fragment instanceof OnMapReadyListener) {
            ((OnMapReadyListener) fragment).onMapReady();
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        gps.stopGPS();
        Log.d("This is onPause", "We stopped the gps");
    }

    public static interface OnMapReadyListener{
        void onMapReady();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
        }
    }

    public void loadTrucks() {
        double latitude = 0.0;
        double longitude = 0.0;
        String truckName= "";

        ArrayList<HashMap<String, String>> values = LocationData.locationData;
        for (HashMap<String, String> hashMap : values) {
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                Log.d("Hello World", entry.getKey() + " => " + entry.getValue());
                if(entry.getKey().equals(TAG_LATITUDE)) {
                    latitude = Double.parseDouble(entry.getValue());
                } else if(entry.getKey().equals(TAG_LONGITUDE)) {
                    longitude = Double.parseDouble(entry.getValue());
                } else {
                    truckName = entry.getValue();
                }
            }
            LatLng test = new LatLng(latitude, longitude);
            map.addMarker(new MarkerOptions().position(test).title(truckName));
        }
    }
}
