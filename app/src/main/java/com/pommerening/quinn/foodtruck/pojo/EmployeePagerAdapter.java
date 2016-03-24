package com.pommerening.quinn.foodtruck.pojo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.android.gms.maps.SupportMapFragment;
import com.pommerening.quinn.foodtruck.fragment.MiniMapFragment;
import com.pommerening.quinn.foodtruck.fragment.tabs.employee.EmployeeItemsFragment;
import com.pommerening.quinn.foodtruck.fragment.tabs.employee.EmployeeOrderFragment;
import com.pommerening.quinn.foodtruck.fragment.tabs.employee.EmployeeSettingsFragment;

/**
 * Created by Quinn Pommerening on 3/23/2016.
 */
public class EmployeePagerAdapter extends FragmentStatePagerAdapter {
    int mTabNumbers;
    private String mUsername;

    public EmployeePagerAdapter(FragmentManager fm, int NumberOfTabs, String username) {
        super(fm);
        this.mTabNumbers = NumberOfTabs;
        this.mUsername = username;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                EmployeeItemsFragment invTab = new EmployeeItemsFragment().newInstance(mUsername);
                return invTab;
            case 1:
                EmployeeOrderFragment orderTab = new EmployeeOrderFragment();
                return orderTab;
            case 2:
                EmployeeSettingsFragment setTab = new EmployeeSettingsFragment();
                return setTab;
            case 3:
                SupportMapFragment tabMap = MiniMapFragment.newInstance();
                return tabMap;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabNumbers;
    }
}
