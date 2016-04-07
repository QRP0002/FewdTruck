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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.GPSLocation;
import com.pommerening.quinn.foodtruck.pojo.HaversineFormula;
import com.pommerening.quinn.foodtruck.pojo.LocationData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MiniMapFragment extends SupportMapFragment{
    private SupportMapFragment fragment;
    private GoogleMap map;
    private static View view;
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    GPSLocation gps;
    private static boolean toggle = false;
    private static final int REQUEST_CODE = 200;

    public MiniMapFragment() {
        super();
    }

    public static MiniMapFragment newInstance() {
        MiniMapFragment fragment = new MiniMapFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gps = GPSLocation.locationSingleton(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        Fragment fragment  = getParentFragment();
        if(fragment != null && fragment instanceof OnMapReadyListener) {
            ((OnMapReadyListener) fragment).onMapReady();
        }
        getInitialLocation();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(toggle && LocationData.getDistance() != 0) {
            map.clear();
            getInitialLocation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        gps.stopGPS();
        toggle = true;
    }

    public interface OnMapReadyListener{
        void onMapReady();
    }

    private void searchResult() {
        double latitude = 0.0;
        double longitude = 0.0;
        String truckName= "";
        double searchDistance = LocationData.getDistance();
        ArrayList<String> nameData = new ArrayList<>();

        ArrayList<HashMap<String, String>> values = LocationData.getLocationData();

        for (HashMap<String, String> hashMap : values) {
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                if(entry.getKey().equals(TAG_LATITUDE)) {
                    latitude = Double.parseDouble(entry.getValue());
                } else if(entry.getKey().equals(TAG_LONGITUDE)) {
                    longitude = Double.parseDouble(entry.getValue());
                } else {
                    truckName = entry.getValue();
                }
            }

            if(searchDistance == 0.0) {
                LatLng test = new LatLng(latitude, longitude);
                map.addMarker(new MarkerOptions()
                        .position(test)
                        .title(truckName)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_truck)));
            } else {
                double result = HaversineFormula.distFrom(gps.getLatitude(), gps.getLongitude(),
                        latitude, longitude);
                if(result <= searchDistance) {
                    LatLng test = new LatLng(latitude, longitude);
                    map.addMarker(new MarkerOptions()
                            .position(test)
                            .title(truckName)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_truck)));
                    nameData.add(truckName);
                }
            }
        }

        if(nameData.size() > 0) {
            LocationData.setTruckNames(nameData);
        }
    }

    public void getInitialLocation() {
        if(gps == null) {
            Log.d("GPS IS NULL", "FIX");
        }
        if(gps.canGetLocation()) {
            map = getMap();
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            LatLng test = new LatLng(gps.getLatitude(), gps.getLongitude());
            map.addMarker(new MarkerOptions()
                    .position(test)
                    .title("You")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_person)));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(test, 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
            searchResult();
        }
    }
}
