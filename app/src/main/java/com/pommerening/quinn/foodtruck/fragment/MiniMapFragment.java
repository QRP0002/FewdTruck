package com.pommerening.quinn.foodtruck.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pommerening.quinn.foodtruck.R;

public class MiniMapFragment extends SupportMapFragment {
    private SupportMapFragment fragment;
    private GoogleMap map;
    private static View view;
    private final String TAG = "MiniMapFragment";

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        Fragment fragment  = getParentFragment();
        if(fragment != null && fragment instanceof OnMapReadyListner) {
            ((OnMapReadyListner) fragment).onMapReady();
        }
        return view;
    }

    public static interface OnMapReadyListner{
        void onMapReady();
    }

}
