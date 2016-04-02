package com.pommerening.quinn.foodtruck.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.CustomerPagerAdapter;

public class CustomerHomeFragment extends Fragment implements MiniMapFragment.OnMapReadyListener {
    private String mUsername;
    private String mDistance;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;

    public static CustomerHomeFragment newInstance(String username, String distance) {
        CustomerHomeFragment f = new CustomerHomeFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("distance", distance);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsername = getArguments().getString("username");
        mDistance = getArguments().getString("distance");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_home, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Map"));
        tabLayout.addTab(tabLayout.newTab().setText("Items"));
        tabLayout.addTab(tabLayout.newTab().setText("Favorites"));
        tabLayout.addTab(tabLayout.newTab().setText("Settings"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final CustomerPagerAdapter cpa = new CustomerPagerAdapter
                (getFragmentManager(), tabLayout.getTabCount(), mUsername, mDistance);
        viewPager.setAdapter(cpa);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    @Override
    public void onMapReady() {
        mMap = mMapFragment.getMap();
    }
}
