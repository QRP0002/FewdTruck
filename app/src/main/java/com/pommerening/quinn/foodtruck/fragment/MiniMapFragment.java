package com.pommerening.quinn.foodtruck.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.GPSLocation;
import com.pommerening.quinn.foodtruck.pojo.PermissionInterface;

public class MiniMapFragment extends SupportMapFragment{
    private SupportMapFragment fragment;
    private GoogleMap map;
    private static View view;
    private static final int REQUEST_CODE = 200;
    private PermissionInterface permission;

    public MiniMapFragment() {
        super();
    }

    public static MiniMapFragment newInstance() {
        MiniMapFragment fragment = new MiniMapFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
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
            permission.changeSettings();
        }
        GPSLocation gps = new GPSLocation(getActivity());
        if(gps.canGetLocation()) {
            map = getMap();
            LatLng test = new LatLng(gps.getLatitude(), gps.getLongitude());
            map.addMarker(new MarkerOptions().position(test).title("You"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(test, 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
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
        permission.stopGPS();
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
}
